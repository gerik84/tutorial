package files;

import actors.MakeThumbnailActor;
import actors.NewImageEvent;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.InputStream;


public abstract class FileStorageSystem {

    public final String application_pdf_type = "application/pdf";

    private ActorSystem actorSystem;

    /**
     * @return id of the uploaded file
     */
    public abstract String upload(String type, Http.Request request);

    public abstract String upload(String type, File file);

    public abstract String upload(String type, byte[] data);

    public abstract InputStream getFile(String id);

    public abstract boolean saveThumbnail(String id, byte[] thumbnail);

    public abstract Result download(String id, Http.Request request, Http.Response response);


//
    protected void requestThumbnail(NewImageEvent event) {
        if (actorSystem != null && MakeThumbnailActor.addToQueue(event.url)) {
            ActorRef thumbActor = actorSystem.actorOf(MakeThumbnailActor.props(this).withDispatcher("thumb-dispatcher"));
            if (thumbActor != null) {
                thumbActor.tell(event, null);
            }
        }
    }

    public void setActorSystem(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }
}
