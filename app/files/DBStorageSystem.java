package files;

import actors.MakeThumbnailActor;
import actors.NewImageEvent;
import com.google.common.io.Files;
import com.typesafe.config.Config;
import models.File;
import models.Media;
import play.Configuration;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DBStorageSystem extends FileStorageSystem {

    @Inject
    private Configuration configuration;

    @Override
    public String upload(String type, Http.Request request) {
        return upload(type, request.body().asRaw().asFile());
    }

    @Override
    public String upload(String type, java.io.File fileIO) {
        try {
            return upload(type, Files.toByteArray(fileIO));
        } catch (Exception e) {
            Logger.error("DBStorageSystem: can't save file: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String upload(String type, byte[] data) {
        File file = new File();
        file.setType(type);

        if (data == null) {
            Logger.error("DBStorageSystem: can't save file ");
            return null;
        }
        file.setData(data);
        file.setLength(data.length);

        file.save();
        String url = file.getId().toString();//"/file/"+file.getId();//configuration.getString("file.download.url","/download/")+file.getId();//Routes routes.Application.download(file.getId().toString()).absoluteURL(request);
        requestThumbnail(new NewImageEvent(url, file.getId().toString(), Media.MediaType.Image));
        return url;
    }

    @Override
    public InputStream getFile(String id) {
        File file = File.getFile(id);
        if (file == null || file.getData() == null) return null;
        ByteArrayInputStream is = new ByteArrayInputStream(file.getData());
        return is;
    }

    @Override
    public boolean saveThumbnail(String id, byte[] thumbnail) {
        File file = File.getFile(id);
        if (file == null) return false;
        file.setThumbnail(thumbnail);
        file.save();
        return true;
    }

    @Override
    public Result download(String id, Http.Request request, Http.Response response) {
        String r_id;
        boolean isThumb = false;
        if (id.endsWith(MakeThumbnailActor.ThumbFileNamePostfix)) {
            isThumb = true;
            r_id = id.substring(0, id.length() - MakeThumbnailActor.ThumbFileNamePostfix.length());
        } else {
            r_id = id;
        }
        File file = File.getFile(r_id);
        if (file == null) return Results.notFound("file not found");
        byte[] data = isThumb ? file.getThumbnail() : file.getData();
        if (data == null) {
            return Results.notFound("file not found");
        }
        return Results.ok(new ByteArrayInputStream(data)).as(file.getType());
    }


}
