package Assets;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Asset {

    String path;
    int width,height;
    BufferedImage image;
    private final Set<String> scene = new HashSet<>();
    Boolean loaded = false;

    public Asset(String path, int width, int height, String... scene){

        this.path = path;
        this.width = width;
        this.height = height;
        this.scene.addAll(Arrays.asList(scene));

    }
    public void initImage(){
        try{
            image = ImageIO.read(getClass().getResourceAsStream(path + ".png"));
            image = scaleImage(image, width, height);
            loaded = true;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void clearImage() {
        image = null; // Allow garbage collection to free memory
        loaded = false;
    }

    public BufferedImage scaleImage(BufferedImage original, int width, int height){
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height,null);
        g2.dispose();

        return scaledImage;
    }

    public BufferedImage getImage(){
        return image;
    }

    public BufferedImage getAndResize(int width,int height){
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(image, 0, 0, width, height,null);
        g2.dispose();
        return scaledImage;
    }

    //Used in things like spells that can be equiped or unequiped
    public void addScene(String scene){
        this.scene.add(scene);
    }
    //Used in spells, that can be equiped and unequiped
    public void removeScene(String scene){
        this.scene.remove(scene);
    }

    public boolean isUsedInScene(String scene){
        return this.scene.contains(scene);
    }


}
