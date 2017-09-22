package actors;

import actors.Thumbnail.IThumbnailer;
import actors.Thumbnail.ThumbnailerFactory;
import akka.actor.Props;
import akka.actor.UntypedActor;
import files.FileStorageSystem;
import models.File;
import play.Logger;

import java.util.HashSet;

public class MakeThumbnailActor extends UntypedActor
{

    public final static String ThumbFileNamePostfix = "_thumb";

    // TODO change this, because this is a bad idea in terms of cluster
    private static final HashSet<String> workingUrls = new HashSet<>();
    private static final Object LOCK_QUEUE = new Object();

    private final FileStorageSystem storageSystem;

    public MakeThumbnailActor(FileStorageSystem storageSystem) {
        this.storageSystem = storageSystem;
    }
    // end bad idea

    public static Props props(FileStorageSystem storageSystem) {
        return Props.create(MakeThumbnailActor.class, () -> new MakeThumbnailActor(storageSystem));
    }

    public static boolean addToQueue(String url) {
        synchronized (LOCK_QUEUE) {
            if (workingUrls.contains(url)) return false;
            workingUrls.add(url);
            return true;
        }
    }

    private static void deleteFromQueue(String url) {
        synchronized (LOCK_QUEUE) {
            workingUrls.remove(url);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof NewImageEvent) {
            NewImageEvent event = (NewImageEvent) message;
            IThumbnailer thumbnailer = ThumbnailerFactory.makeThumbnailer(event.type);
            if (thumbnailer != null) {
                long start = System.currentTimeMillis();
                if (thumbnailer.makeThumbnail(storageSystem, event.uploadedFile)) {
                    Logger.info("make thumb for " + event.uploadedFile + " for " + (System.currentTimeMillis() - start) + " ms");
                    /*
                    Message m = Message.findImageMessage(event.url);
                    if (m != null && m.getId() != null) {
                        m.update();
                        m.getChat().update();
                        eventSystem.publish(new ChatEvent(event.chatID, null, ChatEvent.EVENT.CHANGED));
                    }
                    */
                    deleteFromQueue(event.url);

                } else {
                    //todo place icon instead wrong thumb
                    Logger.error("Can't make thumb for " + event.uploadedFile);
                }

            } else {
                Logger.error("Can't make thumbnailer for type " + event.type);
            }
            return;
        }
        unhandled(message);
    }
}
