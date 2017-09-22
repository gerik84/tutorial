package files;

import play.Play;

/**
 * Created by vlad on 21/02/2016.
 */
public enum FileStorageType {

    FILESYSTEM {
        @Override
        public FileStorageSystem initStorage() {
            final String uploadRootDir = Play.application().configuration().getString("upload.root.dir");
            return new FSStorageSystem(uploadRootDir);
        }
    },

    DATABASE {
        @Override
        public FileStorageSystem initStorage() {
            return new DBStorageSystem();
        }
    };

    public abstract FileStorageSystem initStorage();
}
