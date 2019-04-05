package com.image.retrieval;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import  com.image.retrieval.CheckSum;
@WebServlet("/Aashi")
public class Aashi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public Aashi() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		  String[] retrievedImagesMink, retrievedImagesRD, retrievedImagesHI;
		  HashMapIndexing hashmapobj = new HashMapIndexing();
		
		  Result rs=new Result();
		 BufferedImage img = null; 
	        File f = null; 
	        String description = request.getParameter("description");
	        String index=request.getParameter("index");
	        System.out.println(description);
	        String file = request.getParameter("file"); 
	        System.out.println(file);
	 String filename=description+"\\"+"\\"+file;
	 System.out.println(filename);
		   try
	        { 
	            f = new File(filename); 
	             img = ImageIO.read(f);
	            
	           
	        } 
	        catch(IOException e) 
	        { 
	            System.out.println(e); 
	        } 
		double[] hsbNew= Cbir.getNormalizedHistogram(img) ;
		
		 CheckSum cs=new CheckSum();
         try {
			String abhi=cs.checkSumSHA1(filename);
			String mish=cs.checkSumMD5(filename);
			System.out.println(abhi); 
			System.out.println(mish); 

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		System.out.println(hsbNew);
	        int width = img.getWidth(); 
	        int height = img.getHeight(); 
	        for (int y = 0; y < height; y++) 
	        { 
	            for (int x = 0; x < width; x++) 
	            { 
	        int p = img.getRGB(x,y); 
	  
	  

	        int a = (p>>24) & 0xff; 
	  

	        int r = (p>>16) & 0xff; 
	  
	        
	        int g = (p>>8) & 0xff; 
	  
 
	        int b= p & 0xff; 
	        
	            }
	        }
	        String y=request.getParameter("add");
	        if (y== "Select Query Image") {
	        	
	        }
	        String mishra = request.getParameter("gender");
	        if (mishra=="Minkowki") {
	        try {
                retrievedImagesMink = hashmapobj.returnNearest5(f, "Minkowski");
                // print(retrievedImagesMink);
                rs.displayResults(retrievedImagesMink);
                out.print("<script> alert('Matching Images Retrieved based on Minkowskis Distance')</script>");
              
            } catch (IOException ex) {
                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
                return;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
	        }
	   
	        if (mishra=="Histogram Intersection")
	        {
	            try {
	            	 retrievedImagesHI = hashmapobj.returnNearest5(f, "HistogramIntersection");
	                 rs.displayResults(retrievedImagesHI);
	                 out.print("<script> alert('Matching Images Retrieved based on Histogram Intersection')</script>");
	            } catch (IOException ex) {
	                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
	                return;
	            } catch (NoSuchAlgorithmException ex) {
	                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
	                return;
	            }
	        }
	        
	        if (mishra=="Relative Dviation") {
	            try {
	                retrievedImagesRD = hashmapobj.returnNearest5(f, "RelativeDeviation");
	                rs.displayResults(retrievedImagesRD);
	               out.print("<script>alert('Matching Images Retrieved based on Relative Deviation')<script>");
	            } catch (IOException ex) {
	                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
	                return;
	            } catch (NoSuchAlgorithmException ex) {
	                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
	                return;
	            }
	        }
	if (y=="Add Image") {
           
	        HashMapIndexing hasObj = new HashMapIndexing();
	        if (index=="Index") 
	        {
	        try {
	            hasObj.addImageToHashMap(f);
	        } catch (IOException ex) {
	            Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
	            return;
	        } catch (NoSuchAlgorithmException ex) {
	            Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
	            return;
	        }

	        }

	}
	if (y== "Index Image") {
	 try {
		  HashMapIndexing hashMapObj = new HashMapIndexing();
         int stat = hashMapObj.createMapMultiThreaded();
         if (stat == 0) {
            out.print( "<script>alert('Images have been Indexed Now !')</script>");
         }
     } catch (IOException ex) {
         Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
     } catch (NoSuchAlgorithmException ex) {
         Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
     } catch (InterruptedException ex) {
         Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
     }
 } else {
     try {
    	 HashMapIndexing hashMapObj = new HashMapIndexing();
         hashMapObj.createMap();

     } catch (IOException ex) {
         Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
     } catch (NoSuchAlgorithmException ex) {
         Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
     }
 }

}
	}
