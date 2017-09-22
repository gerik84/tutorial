package utils;

import io.ebean.*;
import models.BaseModel;
import models.ModerateModel;
import models.UUIDBaseModel;
import org.joda.time.DateTime;
import play.Logger;
import play.mvc.Http;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static play.mvc.Http.HeaderNames.IF_MODIFIED_SINCE;

/**
 * Created by root on 14.04.2017.
 */
public class ModelLists {

    public static final String TOTAL_COUNT_HEADER="X-TOTAL-COUNT";

    public interface ICustomQuery <T> {
        void q(ExpressionList<T> q);
    }

    public static final String fromTimeParam = "from";
    public static final String toTimeParam = "to";
    public static final String fromRowParam = "first";
    public static final String maxRowParam = "max";
    public static final String sortParam = "sort";
    public static final String sortDirectionParam = "direction";
    public static final String countParam = "count";
    public static final String availableParam = "available";
    public static final String filterPrefix = "filter_";

    public static final String defaultSortField = "whenCreated";
    public static final String defaultPaginationField = "whenCreated";
    public static final String updateField = "whenUpdated";


    public static  String findField(Class clazz, String fieldName) {
        String[] parts = fieldName.split("\\.");
        Class cClass = clazz;
        for (int i = 0; i < parts.length-1; i++) {
            String f = parts[i];
            cClass = findClass(cClass, f);
            if(cClass == null || !Model.class.isAssignableFrom(cClass)) {
                Logger.error(String.format("try filter on non-model field '%s' model '%s'", f, clazz.getName()));
                return null;
            }
        }
        String type = findFieldType(cClass, parts[parts.length-1]);
        if(type == null) {
            Logger.error(String.format("try filter on other field '%s' model '%s'", parts[parts.length-1], cClass.getName()));
        }
        return type;
    }


    public static Class findClass(Class clazz, String fieldName) {
        String methodName = "get"+fieldName;
        Method[] methods = clazz.getMethods();

        for(Method fromMethod: methods) {
            if (fromMethod.getName().equalsIgnoreCase(methodName)) {
                Type type = fromMethod.getGenericReturnType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType)type;
                    Type[] arr = pType.getActualTypeArguments();
                    if (arr.length > 0) {
                        return  (Class<?>)arr[0];
                    } else {
                        return null;
                    }
                }
                return type instanceof Class ? (Class) type : null;
                //return fromMethod.getReturnType();
            }
        }
        return null;
    }

    public static  String findFieldType(Class clazz, String fieldName) {
        String methodName = "get"+fieldName;
        Method[] methods = clazz.getMethods();
        for(Method fromMethod: methods) {
            if (fromMethod.getName().equalsIgnoreCase(methodName)) {
                AnnotatedType rtype = fromMethod.getAnnotatedReturnType();
                return rtype.getType().getTypeName();
            }
        }
        return null;

    }

    public static void makeLike(ExpressionList<?> q, String field, String val) {
        if(val != null) {
            q.ilike(field, "%"+val.toLowerCase()+"%");
        }
    }

    public static int getQueryInteger(Http.Request request, String param, int default_val) {
        try {
            String q = request.getQueryString(param);
            if(q == null || q.isEmpty()) return default_val;
            return Integer.valueOf(q);
        } catch (NumberFormatException e) {
            Logger.error("getQueryInteger: Can't get parameter '"+param+"'", e);
        }
        return default_val;
    }

    public static Long getQueryLong(Http.Request request, String param, Long default_val) {
        try {
            String q = request.getQueryString(param);
            if(q == null || q.isEmpty()) return default_val;
            return Long.valueOf(q);
        } catch (NumberFormatException e) {
            Logger.error("getQueryInteger: Can't get parameter '"+param+"'", e);
        }
        return default_val;
    }

    public static int getMaxRows(Http.Request request) {
        return getQueryInteger(request, maxRowParam, Const.defaultListResults);
    }
    public static int getFirstRow(Http.Request request) {
        return getQueryInteger(request, fromRowParam, 0);
    }

    public static Map<String, String> makeFilters(Http.Request request) {
        HashMap<String, String> filter = null;
        Map<String, String[]> stringMap = request.queryString();
        if(stringMap == null || stringMap.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, String[]> entry: stringMap.entrySet()) {
            String key = entry.getKey();
            String[] val = entry.getValue();
            if(val != null && val.length > 0 && key.startsWith(filterPrefix)) {
                String param = key.substring(filterPrefix.length());
                if(filter == null) {
                    filter = new HashMap<>();
                }
                filter.put(param, val[0]);
            }
        }
        return filter;
    }

    private static <T extends BaseModel> ExpressionList<T> makeQuery(Class<T> model,
                                                                     String paginationField,
                                                                     Long fromTime,
                                                                     Long toTime,
                                                                     Map<String, String> filter,
                                                                     boolean available,
                                                                     ICustomQuery customQuery,
                                                                     boolean isClient) {

        ExpressionList<T> q = Ebean.find(model).where();
        if(fromTime != null) {
            q.gt(paginationField, fromTime);
        }
        if(toTime != null) {
            q.le(paginationField, toTime);
        }
        if(available) {
            q.isNull("whenDeleted");
        }
        if(customQuery != null) {
            customQuery.q(q);
        }
        if(isClient && model.isAssignableFrom(ModerateModel.class)) {
            ModerateModel.makeClientRestriction(q);
        }
        if(filter != null) {
            filter.forEach((property, value) -> {
                String paramType = findField(model, property);
                if(paramType != null) {
                    if(paramType.equals("java.lang.String")) {
                        makeLike(q, property, value);
                    } if(paramType.equals("java.util.UUID")) {
                        try {
                            q.eq(property, UUID.fromString(value));
                        } catch (IllegalArgumentException e) {
                            Logger.error(String.format("Expect UUID value for param '%s' while got '%s'", property, value));
                        }

                    }

                }
            });
        }

        return q;
    }

    public static <T extends BaseModel> int getListCount(Class<T> model, String paginationField, Long fromTime, Long toTime, Map<String, String> filter, boolean available, ICustomQuery customQuery, boolean isClient) {
        ExpressionList<T> q = makeQuery(model, paginationField,
                fromTime,
                toTime,
                filter,
                available,
                customQuery,
                isClient
                );
        return q.findCount();
    }

//    public static <T extends BaseModel> int getListCountFromRequest(Class<T> model, Http.Request request, boolean isClient) {
//        return getListCountFromRequest(model, request, null, isClient);
//
//    }

    public static <T extends BaseModel> int getListCountFromRequest(Class<T> model, Http.Request request, ICustomQuery q, boolean isClient) {
        Long lastModified = getIfModifiedSince(request);
        if(lastModified != null) {
            return getListCount(model, updateField,
                    lastModified,
                    null,
                    makeFilters(request),
                    request.getQueryString(availableParam) != null,
                    q,
                    isClient
            );
        }
        return getListCount(model, defaultPaginationField,
                getQueryLong(request, fromTimeParam, null),
                getQueryLong(request, toTimeParam, null),
                makeFilters(request),
                request.getQueryString(availableParam) != null,
                q,
                isClient
        );
    }

//    public static <T extends BaseModel> List<T> getListFromRequest(Class<T> model, Http.Request request, boolean isClient) {
//        return getListFromRequest(model, request, null,isClient);
//    }

    public static Long getIfModifiedSince(Http.Request request) {
        String lastModified = request.getHeader(IF_MODIFIED_SINCE);
        if (lastModified != null) {
            try {
                DateTime time = DateTime.parse(lastModified);
                return time.toInstant().getMillis();
            } catch (Exception e) {
                Logger.error("Got invalid "+IF_MODIFIED_SINCE+" header", e);
            }
        }
        return null;
    }

    public static <T extends BaseModel> PagedList<T> getListFromRequest(Class<T> model, Http.Request request, ICustomQuery q, boolean isClient) {
        Long lastModified = getIfModifiedSince(request);
        if(lastModified != null) {
            return  getList(model, updateField,
                    getFirstRow(request),
                    getMaxRows(request),
                    lastModified,
                    null,
                    null,
                    null,
                    defaultSortField,
                    makeFilters(request),
                    request.getQueryString(availableParam) != null,
                    q,
                    isClient
            );
        }
        return  getList(model, defaultPaginationField,
                getFirstRow(request),
                getMaxRows(request),
                getQueryLong(request, fromTimeParam, null),
                getQueryLong(request, toTimeParam, null),
                request.getQueryString(sortParam),
                request.getQueryString(sortDirectionParam),
                defaultSortField,
                makeFilters(request),
                request.getQueryString(availableParam) != null,
                q,
                isClient
        );
    }



    public static <T extends UUIDBaseModel> Long lastUpdate(Class<T> model) {
         T em = Ebean.find(model).select(updateField).setDisableLazyLoading(true).order(updateField+" desc").setMaxRows(1).findUnique();
         return em != null ? em.getWhenUpdated():null;
    }

    public static <T extends BaseModel> PagedList<T> getList(Class<T> model, String paginationField,
                                                                      Integer firstRow,
                                                                      Integer maxRows,
                                                                      Long fromTime,
                                                                      Long toTime,
                                                                      String sort,
                                                                      String direction,
                                                                      String defaultSort,
                                                                      Map<String, String> filter,
                                                                      boolean available,
                                                                      ICustomQuery customQuery,
                                                                      boolean isClient) {
        ExpressionList<T> q = makeQuery(model, paginationField,
                fromTime,
                toTime,
                filter,
                available,
                customQuery,
                isClient
        );
        if(firstRow != null) {
            q.setFirstRow(firstRow);
        }
        if(maxRows != null && q.query().getMaxRows() <= 0) {
            q.setMaxRows(maxRows);
        }
        makeOrderBy(q, model, sort, direction, defaultSort);
        return q.findPagedList();
    }


    public static void makeOrderBy(ExpressionList<?> q, Class clazz, String sort, String direction, String defaultSort) {
        if(!q.query().order().isEmpty()) {
            return;
        }
        if(sort != null) {
            if(findField(clazz, sort) != null) {
                if(direction == null || !"desc".equals(direction)) {
                    direction = "asc";
                }
                q.setOrderBy(sort+" "+direction);
            }
        } else if(defaultSort != null) {
            q.setOrderBy(defaultSort);
        }
    }

    public static <T extends BaseModel> boolean checkUniqueFields(Class<T> model, String[] fields, String[] val, UUID origID) {
        ExpressionList<T> q = Ebean.find(model).where();
        if(fields.length != val.length) {
            return false;
        }
        if(origID != null) {
            q.ne("id", origID);
        }
        Junction<T> j = q.or();
        for (int i = 0; i < fields.length; i++) {
            j.eq(fields[i],val[i]);
        }
        q.setMaxRows(1);
        return q.findList().size() == 0;
    }


}
