package actors.Thumbnail;

import models.File;
import models.Media;

public class ThumbnailerFactory {

    public static IThumbnailer makeThumbnailer(Media.MediaType type) {
        switch (type) {
            case Image:
                return new ImageThumbnailer();
        }
        return null;
    }
}
