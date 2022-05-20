package model.image;

import javafx.scene.image.Image;
import tools.Files;

import java.util.Hashtable;

/**
 * An object restricted to have only one instance.
 * Used to load and cache images in memory that can be accessed from every other object in a synchronised manner.
 */
public class ImageCache {

    private String filename;
    private Image image;
    private double width, height;

    /**
     * Make use of the synchronised Java classloader for thread-safety.
     */
    private static class Holder{
        private static final Hashtable<String, Image> CACHE = new Hashtable<>();
    }

    private ImageCache(String filename, double width, double height){
        this.filename = filename;
        this.width = width;
        this.height = height;
    }

    private Image get(){
        if(image == null){
            String newFilename = filename+'?'+width+'x'+height;
            if(!Holder.CACHE.containsKey(newFilename)){
                if(width != 0 && height != 0)
                    Holder.CACHE.put(newFilename,new Image(filename,width,height,true,true,true));
                else
                    Holder.CACHE.put(newFilename,new Image(filename,true));
            }
            image = Holder.CACHE.get(newFilename);
        }
        return image;
    }

    /**
     * Gives an image to the requesting object with custom width and height.
     * @param url The image url.
     * @param width The requested width.
     * @param height The requested height.
     * @return The requested image.
     */
    public static Image getImage(String url, double width, double height) {
        return new ImageCache(Files.IMAGE_PREFIX + url,width,height).get();
    }

    /**
     * Gives an image to the requesting object.
     * @param url The image url.
     * @return The requested image.
     */
    public static Image getImage(String url) {
        return getImage(url,0,0);
    }

}
