package actors.Thumbnail;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import files.FileStorageSystem;
import play.Logger;
import utils.Const;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageThumbnailer implements IThumbnailer
{

    public static int getOrientation(InputStream imageFile) throws ImageProcessingException, IOException, MetadataException {
        int orientation = 1;
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        if (metadata == null) return orientation;
        ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (directory == null) return orientation;

        try {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (MetadataException me) {
            Logger.warn("Could not get orientation");
        }
        return orientation;
    }

    public static boolean isRotated(int orientation) {
        switch (orientation) {
            case 3: // PI rotation
            case 5: // - PI/2 and Flip X
            case 6: // -PI/2 and -width
            case 7: // PI/2 and Flip
            case 8: // PI / 2
                return true;

        }
        return false;
    }

    public static AffineTransform getTransform(int width, int height, int orientation) {
        AffineTransform t = new AffineTransform();

        switch (orientation) {
            case 1:
                break;
            case 2: // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-width, 0);
                break;
            case 3: // PI rotation
                t.translate(width, height);
                t.rotate(Math.PI);
                break;
            case 4: // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -height);
                break;
            case 5: // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                t.translate(height, 0);
                t.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(-height, 0);
                t.translate(0, width);
                t.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                t.translate(0, width);
                t.rotate(3 * Math.PI / 2);
                break;
        }
        return t;
    }

    public static BufferedImage transformImage(BufferedImage image, AffineTransform transform, int orientation) throws Exception {

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
        int w = image.getWidth();
        int h = image.getHeight();
        if (isRotated(orientation)) {
            w = image.getHeight();
            h = image.getWidth();
        }

        BufferedImage destinationImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = destinationImage.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
        destinationImage = op.filter(image, destinationImage);
        ;
        return destinationImage;
    }

    @Override
    public boolean makeThumbnail(FileStorageSystem fileStorageSystem, String idFile) {
        try {

            //File imageFile = new File(filePath);
            InputStream is = fileStorageSystem.getFile(idFile);
            if (is == null) return false;
            BufferedImage sourceImage = ImageIO.read(is);//ImageIO.read(imageFile);
            if (sourceImage == null) return false;
            int width = sourceImage.getWidth();
            int height = sourceImage.getHeight();

            int ThumbWidth = width > Const.ThumbSize ? Const.ThumbSize : width;
            int ThumbHeight = height > Const.ThumbSize ? Const.ThumbSize : height;

            BufferedImage img2;
            if (width > height) {
                float extraSize = height - ThumbHeight;
                float percentHight = (extraSize / height) * 100;
                float percentWidth = width - ((width / 100) * percentHight);
                BufferedImage img = new BufferedImage((int) percentWidth, ThumbHeight, BufferedImage.TYPE_INT_RGB);
                Image scaledImage = sourceImage.getScaledInstance((int) percentWidth, ThumbHeight, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                if(percentWidth > ThumbWidth) {
                    img2 = img.getSubimage((int) ((percentWidth - ThumbWidth) / 2), 0, ThumbWidth, ThumbHeight);
                } else {
                    img2 = img;
                }

            } else {
                float extraSize = width - ThumbWidth;
                float percentWidth = (extraSize / width) * 100;
                float percentHight = height - ((height / 100) * percentWidth);
                BufferedImage img = new BufferedImage(ThumbWidth, (int) percentHight, BufferedImage.TYPE_INT_RGB);
                Image scaledImage = sourceImage.getScaledInstance(ThumbWidth, (int) percentHight, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                if(percentHight > ThumbHeight) {
                    img2 = img.getSubimage(0, (int) ((percentHight - ThumbHeight) / 2), ThumbWidth, ThumbHeight);
                } else {
                    img2 = img;
                }


            }
            try {
                int orientation = getOrientation(fileStorageSystem.getFile(idFile));
                if (orientation > 1) {
                    img2 = transformImage(img2, getTransform(img2.getWidth(), img2.getHeight(), orientation), orientation);
                }
            } catch (ImageProcessingException e) {
                e.printStackTrace();
            } catch (MetadataException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (img2 != null) {
                //ImageIO.write(img2, "jpg", new File(outputFile));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img2, "jpg", baos);
                baos.flush();
                fileStorageSystem.saveThumbnail(idFile, baos.toByteArray());
                baos.close();
            } else {
                return false;
            }
            return true;

        } catch (IOException e) {
            Logger.error("saveScaledImage", e);

            return false;
        }
    }
}
