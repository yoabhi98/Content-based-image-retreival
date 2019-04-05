package com.image.retrieval;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Bassam
 */
public class ImageAttr implements Serializable {
    public double[] histogram;
    public String imageName;
   public long    imageSize;
   public String   imagePath;

    public ImageAttr(double[] histogram, String imageName, long imageSize, String imagePath) {
        this.histogram = histogram;
        this.imageName = imageName;
        this.imageSize = imageSize;
        this.imagePath = imagePath;
    }

  

    public double[] getHistogram() {
        return histogram;
    }

    public void setHistogram(double[] histogram) {
        this.histogram = histogram;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}