package com.image.retrieval;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Result {
	  private javax.swing.JLabel myLabel;
	    private javax.swing.JLabel myLabelRT2;
	    private javax.swing.JLabel myLabelRT3;
	    private javax.swing.JLabel myLabelRT4;
	    private javax.swing.JLabel myLabelRT5;
	    private javax.swing.JLabel jLabelRT1;
	   public void displayResults(String[] imagePaths) {

	        File f0 = new File(imagePaths[0]);
	        resizeToFitJlabel(f0, jLabelRT1);
	        File f1 = new File(imagePaths[1]);
	        resizeToFitJlabel(f1, myLabelRT2);
	        File f2 = new File(imagePaths[2]);
	        resizeToFitJlabel(f2, myLabelRT3);
	        File f3 = new File(imagePaths[3]);
	        resizeToFitJlabel(f3, myLabelRT4);
	        File f4 = new File(imagePaths[4]);
	        resizeToFitJlabel(f4, myLabelRT5);

	    }
	   public void resizeToFitJlabel(File imageToBeResized, JLabel imageJLabel) {
	        int labelwidth = imageJLabel.getWidth();
	        int labelheight = imageJLabel.getHeight();
	        ImageIcon Icon = new ImageIcon(imageToBeResized.getAbsolutePath());
	        Image img1 = Icon.getImage();
	        imageJLabel.setIcon(new ImageIcon(img1.getScaledInstance(labelwidth, labelheight, Image.SCALE_SMOOTH)));
	    }
	   public static String[] returnFileNameFromPaths(String[] imagePaths) {
	        String[] imageNames = null;
	        for (int i = 0; i < imagePaths.length - 1; i++) {
	            File f = new File(imagePaths[i]);
	            // Path p = Paths.get(imagePaths[i]);
	            imageNames[i] = f.getName();
	        }
	        return imageNames;
	    }

	    public static void print(String[] imagePaths) {
	        for (int i = 0; i < imagePaths.length; i++) {
	            System.out.println(imagePaths[i]);
	        }

	    }
	    public static void infoBox(String infoMessage, String titleBar) {
	        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	    }

}
