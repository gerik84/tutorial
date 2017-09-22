package actors;




public class NewImageEvent
{
    public String url;
    public String uploadedFile;

    public NewImageEvent(String url, String uploadedFile) {
        this.url = url;
        this.uploadedFile = uploadedFile;
    }

    public NewImageEvent() {

    }
}
