package files;

import com.google.common.io.Files;
import play.Configuration;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.*;
import java.util.UUID;

public class FSStorageSystem extends FileStorageSystem {


    @Inject
    private Configuration configuration;

    public final static String pdf_extension = ".pdf";
    public final static String ThumbFileNamePostfix = "_thumb";

    private final String uploadRootDir;

    public FSStorageSystem(String uploadRootDir) {
        this.uploadRootDir = uploadRootDir;
    }

    @Override
    public String upload(String type, Http.Request request) {
        return upload(type, request.body().asRaw().asFile());
    }

    @Override
    public String upload(String type, File fileIn) {
        String fn = "public";
        File dir = new File(uploadRootDir + fn + "/");
        dir.mkdirs();
        boolean isPdf = type.equals(application_pdf_type);
        String fnf = UUID.randomUUID().toString() + (isPdf ? pdf_extension : "");
        File file = new File(dir.getAbsolutePath() + "/" + fnf);
        try {
            fileIn.renameTo(file);
        } catch (Exception e) {
            Logger.error("FSStorageSystem: can't save file: " + e.getMessage(), e);
            return null;
        }

        String url = configuration.getString("file.download.url", "/download/") + fn + "/" + fnf; //routes.Application.download(fn + "/" + fnf).absoluteURL(request);

        Logger.info("upload request thumb for " + file.getAbsolutePath());
        //requestThumbnail(new NewImageEvent(url, file.getAbsolutePath(), Media.MediaType.Image));
        return url;
    }

    @Override
    public String upload(String type, byte[] data) {
        String fn = "public";
        File file = new File(uploadRootDir + fn + "/" +  UUID.randomUUID().toString());
        try {
            Files.write(data, file);
        } catch (IOException e) {
            Logger.error("FSStorageSystem: can't save file: " + e.getMessage(), e);
            return null;
        }
        return upload(type, file);
    }

    @Override
    public InputStream getFile(String id) {
        try {
            return new FileInputStream(id);
        } catch (FileNotFoundException e) {
            Logger.error("FSStorageSystem getFile failed: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean saveThumbnail(String id, byte[] thumbnail) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(id + ThumbFileNamePostfix);
            fos.write(thumbnail);
            fos.close();
            return true;
        } catch (Exception e) {
            Logger.error("FSStorageSystem saveThumbnail failed: " + e.getMessage());
        }
        return false;
    }


    @Override
    public Result download(String id, Http.Request request, Http.Response response) {
       /* File file = new File(uploadRootDir + "/" + id).getAbsoluteFile();
        if (!file.exists()) {
            if (file.getAbsolutePath().endsWith(MakeThumbnailActor.ThumbFileNamePostfix) && file.getAbsolutePath().length() > MakeThumbnailActor.ThumbFileNamePostfix.length()) {
                String pf = file.getAbsolutePath();
                String pfile = pf.substring(0, pf.length() - MakeThumbnailActor.ThumbFileNamePostfix.length());
                String chatID = id.substring(0, id.indexOf("/"));
                Logger.info("download request thumb for " + pfile);
                String url = request.secure() ? "https://" : "http://" + request.host() + request.uri().substring(0, request.uri().length() - MakeThumbnailActor.ThumbFileNamePostfix.length());
                requestThumbnail(new NewImageEvent(url, pfile, *//*pfile.endsWith(pdf_extension) ? Message.MessageType.PDF : Message.MessageType.IMAGE)*//*Media.MediaType.Image));
            } else {
                Logger.error("File " + file.getAbsolutePath() + " not exitsts");
            }
            return Results.notFound("file not found");
        }

        String type;

        if (file.getAbsolutePath().endsWith(pdf_extension)) {
            type = application_pdf_type;
        } else {
            //image by default
            type = "image*//*";
        }

        return Results.ok(file).as(type);*/

        return null;
    }

}
