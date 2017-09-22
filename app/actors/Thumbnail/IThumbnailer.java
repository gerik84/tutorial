package actors.Thumbnail;

import files.FileStorageSystem;

public interface IThumbnailer {
    boolean makeThumbnail(FileStorageSystem fileStorageSystem, String idFile);
}
