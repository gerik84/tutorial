package actors.Thumbnail;

import models.File;

public class ThumbnailerFactory {

    public static IThumbnailer makeThumbnailer(File.TYPE type) {
        switch (type) {
            case image:
                return new ImageThumbnailer();
        }
        return null;
    }
}
