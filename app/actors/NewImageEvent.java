package actors;


import models.Media;

public class NewImageEvent
{
    public String url;
    public String uploadedFile;
    public Media.MediaType type;

    public NewImageEvent(String url, String uploadedFile, Media.MediaType type) {
        this.url = url;
        this.uploadedFile = uploadedFile;
        this.type = type;
    }

    public NewImageEvent() {

    }
}
