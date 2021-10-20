package com.cgreen.ygocardtracker.util;

import org.imgscalr.Scalr;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class CardImageSaver {
    private static final String IMAGES_TOP_DIR = "card_images";
    private static final String IMAGES_DIR = "images";
    private static final String SMALL_IMAGES_DIR = "images_small";
    
    public static Image getCardImage(String remoteUrl) throws MalformedURLException, IOException {
        URL url = new URL(remoteUrl);
        Image image = ImageIO.read(url);
        return image;
    }
    
    public static String saveCardImageFile(Image image, Integer passcode, boolean resizeToFit) throws IOException {
        File homeDir = new File(System.getProperty("user.dir"));
        File allImages = new File(homeDir, IMAGES_TOP_DIR);
        File allRegImages = new File(allImages, IMAGES_DIR);
        allRegImages.mkdirs();
        BufferedImage bi;
        if (resizeToFit) {
            bi = Scalr.resize((BufferedImage) image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, 421, 614);
        } else {
            bi = (BufferedImage) image;
        }
        File outputFile = new File(allRegImages, passcode + ".jpg");
        ImageIO.write(bi, "jpg", outputFile);
        return outputFile.getAbsolutePath();
    }
    
    public static String saveCardImageFileSmall(Image image, Integer passcode, boolean resizeToFit) throws IOException {
        File homeDir = new File(System.getProperty("user.dir"));
        File allImages = new File(homeDir, IMAGES_TOP_DIR);
        File allSmallImages = new File(allImages, SMALL_IMAGES_DIR);
        allSmallImages.mkdirs();
        BufferedImage bi;
        if (resizeToFit) {
            bi = Scalr.resize((BufferedImage)image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, 168, 246);
        } else {
            bi = (BufferedImage) image;
        }
        File outputFile = new File(allSmallImages, passcode + ".jpg");
        ImageIO.write(bi, "jpg", outputFile);
        return outputFile.getAbsolutePath();
    }
    
    public static Path getImagesDir() {
        return Paths.get(IMAGES_TOP_DIR, IMAGES_DIR);
    }
    
    public static Path getSmallImagesDir() {
        return Paths.get(IMAGES_TOP_DIR, SMALL_IMAGES_DIR);
    }
}
