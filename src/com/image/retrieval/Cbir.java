package com.image.retrieval;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Cbir {

    public static double[] getNormalizedHistogram(BufferedImage inImage) {

        float[] hsb;

        int argb, r, g, b;
      
        int HSBInt[] = new int[inImage.getWidth() * inImage.getHeight()];
        int c = 0;
        double[] NormHisto;
        for (int i = 0; i < inImage.getWidth(); i++) {
            for (int j = 0; j < inImage.getHeight(); j++) {

                argb = inImage.getRGB(i, j);
                           b = ((argb) & 0xFF);
                g = ((argb >> 8) & 0xFF);
                r = ((argb >> 16) & 0xFF);
                hsb = Color.RGBtoHSB(r, g, b, null);
                HSBInt[c++] = quantizeHSB1(hsb);

            }
        }

     Histogram hist = new Histogram();
        NormHisto = hist.createhistogram(HSBInt);

        return NormHisto;

    }

    public static int quantizeHSB1(float[] hsb) {
    
        int h = Float.floatToIntBits((hsb[0] * 15f) / 4194303f);
        int s = Float.floatToIntBits((hsb[1] * 3f) / 4194303f);
        int b = Float.floatToIntBits((hsb[2] * 3f) / 4194303f);

        h = h & 0x007fffff;
        h = (h >> 19) & 0xF;

        s = s & 0x007fffff;
        s = (s >> 21) & 0x3;

        b = b & 0x007fffff;
        b = (b >> 21) & 0x3;

        int single = 0;

        single += h & 0xF;
        single = (single << 2);
        single += s & 0x3;
        single = (single << 2);
        single += b & 0x3;

         return single;
    }

    public static float[] convert8bitInttoFloatArray(int num) {
               float[] hsbNew = new float[3];
        //
        int b = (num & 0x00000003);
        String DecToFloatb = "." + Integer.toString(b);
        hsbNew[2] = Float.valueOf(DecToFloatb);

        int s = (num & 0x0000000C);
        s = s >> 2;
        String DecToFloats = "." + Integer.toString(s);
        hsbNew[1] = Float.valueOf(DecToFloats);

        int h = (num & 0x000000F0);
        h = h >> 4;
        String DecToFloath = "." + Integer.toString(h);
        hsbNew[0] = Float.valueOf(DecToFloath);
      
        return hsbNew;
    }

    public static int[] convert8bitInttoIntArray(int num) {
      
        int[] hsbNew = new int[3];
        //
        int b = (num & 0x00000003);
      
        hsbNew[2] = b;

        int s = (num & 0x0000000C);
        s = s >> 2;
      
        hsbNew[1] = s;

        int h = (num & 0x000000F0);
        h = h >> 4;
        
        hsbNew[0] = h;
       
        return hsbNew;
    }

}