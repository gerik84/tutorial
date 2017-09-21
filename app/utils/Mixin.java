package utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import play.i18n.Lang;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static utils.Const.defaultLocale;

/**
 * Created by redline on 21.09.17.
 */
public class Mixin {


    public static ObjectMapper createMapper(@Nullable User user) {
        return createMapper(user, null, defaultLocale);
    }

    public static ObjectMapper createMapper(@Nullable User user, Class<?> model, Locale locale) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        if(model != null) {
//            addMixInClassForUser(mapper, user, model);
//        }
        if(locale != null) {
            mapper.setLocale(locale);
        }
        return mapper;
    }

    public static ObjectMapper createMapper(@Nullable User user, Class<?> model, List<Lang> acceptedLanguages) {
        Locale locale = null;
        if(acceptedLanguages != null && !acceptedLanguages.isEmpty()) {
            for (Lang l: acceptedLanguages) {
                try {
//                    LocalizedString.LANGUAGE.valueOf(l.language());
                    //found first accepted language
                    locale = new Locale(l.language(), l.country());
                    break;
                } catch (IllegalArgumentException e) {
                    //we don't accept this language  - do nothing
                }
            }
        }
        if(locale == null) {
            locale = Const.defaultLocale;
        }
        return createMapper(user, model, locale);
    }

    public static void merge(Object obj, Object update, HashSet<Class> includeMethodsFrom, HashSet<String> excludeGetMethods){
        if(!obj.getClass().isAssignableFrom(update.getClass())){
            return;
        }

        Method[] methods = obj.getClass().getMethods();

        for(Method fromMethod: methods){
            Class cls = fromMethod.getDeclaringClass();
            if(includeMethodsFrom.contains(cls) || cls.equals(obj.getClass())
                    && fromMethod.getName().startsWith("get")){

                String fromName = fromMethod.getName();
                if(excludeGetMethods != null && excludeGetMethods.contains(fromName)) {
                    continue;
                }
                String toName = fromName.replace("get", "set");
                try {
                    Method toMetod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
                    Object value = fromMethod.invoke(update, (Object[])null);
                    if(value != null){
                        toMetod.invoke(obj, value);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
