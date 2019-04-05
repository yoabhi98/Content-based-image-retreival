package com.image.retrieval;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class HashMapIndexing {

    static File root = new File("ImageDatabase/");
    static File hashFile = new File("hashmap.ser");
    Map<String, ImageAttr> NewMap = Collections.synchronizedMap(new HashMap<String, ImageAttr>());
    int noOfFiles;

    public int getNoOfFiles() {
        return noOfFiles;
    }

    public void setNoOfFiles(int noOfFiles) {
        this.noOfFiles = noOfFiles;
    }
    
  
    public Map<String, ImageAttr> getNewMap() {
        return NewMap;
    }

    public void setNewMap(Map<String, ImageAttr> NewMap) {
        this.NewMap = NewMap;
    }

    public void createMap() throws IOException, NoSuchAlgorithmException {

      
        if (!root.exists()) {
            infoBox("The Image Database does not exist. Please create the same !!", "Create Image Database");
            return;
        }

        if (hashFile.exists()) {
            infoBox("HashMap has already been created for the Image Database. You might want to Update !!", "Illegal Operation");
            return;
        }

        noOfFiles = root.listFiles().length;
        System.out.println("Total Files Discovered : " + noOfFiles);

        for (File file : root.listFiles()) {
            String md5 = CheckSum.checkSumMD5(file.getAbsolutePath());
          
            NewMap.put(md5, new ImageAttr(Cbir.getNormalizedHistogram(BuffImageFromFile(file)),file.getName(),file.length(),file.getAbsolutePath()));
            serializeHashMap(NewMap);
        }
     infoBox("Images have been Indexed Now !","Indexed");
          }

    public int createMapMultiThreaded() throws IOException, NoSuchAlgorithmException, InterruptedException {
     
        if (!root.exists()) {
            infoBox("The Image Database does not exist. Please create the same !!", "Create Image Database");
           return 1;
        }

        if (hashFile.exists()) {
            infoBox("HashMap has already been created for the Image Database ", "Illegal Operation");
           return 1;
        }

        noOfFiles = root.listFiles().length;
        System.out.println("Total Files Discovered : " + noOfFiles);

       
        List threads1 = new ArrayList();
     
        for (File file : root.listFiles()) {
          {
             Thread t = new Thread(new Runnable() {
                public void run() {
                    String md5 = null;
                    try {
                        md5 = CheckSum.checkSumMD5(file.getAbsolutePath());
                    } catch (IOException ex) {
                        Logger.getLogger(HashMapIndexing.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HashMapIndexing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    NewMap.put(md5, new ImageAttr(Cbir.getNormalizedHistogram(BuffImageFromFile(file)),file.getName(),file.length(),file.getAbsolutePath()));
                    serializeHashMap(NewMap);
                }
            }
            );
            t.start();
            threads1.add(t);
            
        }
          for (int k = 0; k < threads1.size(); k++)
    {
        
        ((Thread)threads1.get(k)).join();
    }
       

      
        }
        return 0;
    }

    public void addImageToHashMap(File Image) throws IOException, NoSuchAlgorithmException {
      
        if (!root.exists()) {
            infoBox("The Image Database does not exist. Please create the same !!", "Create Image Database");
           return;
        }

        if (!hashFile.exists()) {
            infoBox("Image DB does not Exist !!", "Illegal Operation");
            return;
        }
        if(NewMap.isEmpty())
        {
            deserializeHashMap(hashFile.getName());
        }

        String md5 = CheckSum.checkSumMD5(Image.getAbsolutePath());
        if (NewMap.containsKey(md5)) {
            infoBox("The Image " + Image.getName() + " already exists in the DB ", "Image Already Exists !");
        } else {
            NewMap.put(md5, new ImageAttr(Cbir.getNormalizedHistogram(BuffImageFromFile(Image)),Image.getName(),Image.length(),Image.getAbsolutePath()));
            Path src1 = Paths.get(Image.getPath());
            Path dest1 = Paths.get("./ImageDatabase/");
            Files.copy(src1, dest1.resolve(src1.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            serializeHashMap(NewMap);
            infoBox("Image is added TO DB ! ", "Image Added to DB!");
        }

        
    }

    public String[] returnNearest5(File queryImage,String distType) throws IOException, NoSuchAlgorithmException {
        
        String[] returnMatchImagePaths = new String[5];
        String sortOrder = "ASC";
        HashMap<String, Float> hash = new HashMap<String, Float>();
         if(NewMap.isEmpty())
        {
            deserializeHashMap(hashFile.getName());
        }
       
        double[] queryImagehist = Cbir.getNormalizedHistogram(BuffImageFromFile(queryImage));

        Distance dist = new Distance();

      
        Set set = NewMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            ImageAttr localObj= (ImageAttr) mentry.getValue();
                      if(distType.equalsIgnoreCase("Minkowski")) {
            hash.put(mentry.getKey().toString(), dist.minkowskiDist(queryImagehist, localObj.getHistogram()));}
            else if(distType.equalsIgnoreCase("RelativeDeviation")){
            hash.put(mentry.getKey().toString(), dist.relativeDeviationDist(queryImagehist, localObj.getHistogram()));}
            if(distType.equalsIgnoreCase("HistogramIntersection")){
            hash.put(mentry.getKey().toString(), dist.histogramIntersectionDist(queryImagehist, localObj.getHistogram()));
            sortOrder="DESC";}
        
          
        }

        int g = 0;
        
        hash = sortHashMapByValuesD(hash, sortOrder);
        for (Map.Entry<String, Float> entry : hash.entrySet()) {
            System.out.println("Key " + entry.getKey() + " with value " + entry.getValue());
            returnMatchImagePaths[g]=returnNearestFileName(entry.getKey());
            g++;
            if (g == 2) {
                break;
            }
        }
        
       return returnMatchImagePaths;
    }
    
    public String returnNearestFileName(String key)
    {
         if(NewMap.isEmpty())
        {
            deserializeHashMap(hashFile.getName());
        }

         ImageAttr a = NewMap.get(key);
         return a.getImagePath();
         
    }
    
    
    public LinkedHashMap sortHashMapByValuesD(HashMap passedMap, String sortOrder) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());

        if (sortOrder.matches("ASC")) {
            Collections.sort(mapValues);
            Collections.sort(mapKeys);
        } else if (sortOrder.matches("DESC")) {
            Collections.sort(mapValues, Collections.reverseOrder());
            Collections.sort(mapKeys, Collections.reverseOrder());
        }

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String) key, (Float) val);
                    break;
                }

            }

        }
        return sortedMap;
    }

    
   
    public void serializeHashMap(Map NewMap) {
        try {
            try (FileOutputStream fos = new FileOutputStream("hashmap.ser"); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(NewMap);
            }
          
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void deserializeHashMap(String sername) {
     
        try {
            FileInputStream fis = new FileInputStream(sername);
            ObjectInputStream ois = new ObjectInputStream(fis);
            NewMap = (Map) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
     
        Set set = NewMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
           
            ImageAttr localObj= (ImageAttr) mentry.getValue();
         
        }
    }

    public static BufferedImage BuffImageFromFile(File ImageFilePath) {

        BufferedImage myImage = ImageIo.readImage(ImageFilePath.getAbsolutePath());

        return myImage;

    }

    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

}