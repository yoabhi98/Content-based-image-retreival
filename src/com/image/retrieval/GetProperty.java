package com.image.retrieval;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.lang.Math; 
import java.applet.*;
public class GetProperty 
{
	 Image rawimg1;
	   int imgcols1,imgrows1;
	   MediaTracker tracker1;
	   int[] pix1;
	   int[][][] pix3;
	   float[][] hes=new float[256][4];
	   float[][] hes1=new float[256][4];
	   float w1=0,w3;
	   int fileno=1;
	   static int n=1;
	   int l=10,k=10,m=0;
	   Image img1;
	   static String str1,str4,str11,strn1,strn2,strn,strn0;
	   int x,y;
	   public void input123(String s)
	   {
	    try
	    {

	     //creating image object
	     System.out.println("processing"+s);
	        rawimg1=Toolkit.getDefaultToolkit().getImage(s);
	     tracker1= new MediaTracker(this);
	     tracker1.addImage(rawimg1,1);

	     //checking the image is loaded or not
	     try
	     {
	       if(!tracker1.waitForID(1,10000))
	       {
	        System.out.println("load error");
	        System.exit(1);
	       }
	     }
	     catch(Exception e)
	     {
	       e.printStackTrace();
	       System.exit(1);
	     }

	     //checking whether all the pixels are loaded of not
	     if((tracker1.statusAll(false)&MediaTracker.ERRORED&MediaTracker.ABORTED)!=0)
	     {
	       System.out.println("Load error");
	       System.exit(1);
	     }
	     
	     imgcols1=rawimg1.getWidth(this);
	     imgrows1=rawimg1.getHeight(this);
	 
	     
	     pix1=new int[imgcols1*imgrows1];
	     try
	     {
	      PixelGrabber pgobj=new PixelGrabber(rawimg1,0,0,imgcols1,imgrows1,pix1,0,imgcols1);
	      pgobj.grabPixels();
	     }
	     catch(Exception e)
	     {
	     }

	     
	     pix3=new int[imgrows1][imgcols1][4];
	     
	     for(int row=0;row<imgrows1;row++)
	     {
	       int[] aRow=new int[imgcols1];
	       for(int col=0;col<imgcols1;col++)
	       {
	        int element=row*imgcols1+col;
	        aRow[col]=pix1[element];
	       }
	       for(int col=0;col<imgcols1;col++)
	       {
	        pix3[row][col][0]=(aRow[col]>>24)&0xFF;
	        pix3[row][col][1]=(aRow[col]>>16)&0xFF;
	        pix3[row][col][2]=(aRow[col]>>8)&0xFF;
	        pix3[row][col][3]=(aRow[col])&0xFF;
	       }
	     }


	     int tt1=imgrows1*imgcols1;
	     
	     for(int i=0;i<256;i++)
	     for(int j=0;j<4;j++)
	     hes[i][j]=0;

	     for(int i=0;i<imgrows1;i++)
	     {
	     for(int j=0;j<imgcols1;j++)
	     {
	     for(int k=1;k<4;k++)
	     {
	       int l=pix3[i][j][k];
	       hes[l][k]=hes[l][k]+1;
	     }
	     }
	     }
	     for(int i=0;i<256;i++)
	     {
	     for(int j=1;j<4;j++) 
	     {
	      hes[i][j]=hes[i][j]/tt1;
	     }
	     }

	    //reading from the histogram file
	    Histogram hist = new Histogram();

	   for(int k=1;k<=94;k++)
	   { 

	     for(int i=0;i<256;i++)
	     {
	     for(int j=1;j<4;j++) 
	     {
	      hes1[i][j]=hes[i][j];
	     }
	     }
	     
	     FileInputStream fis = new FileInputStream("C:\\irmproject\\irmfinal2\\histogram"+k+".dat");
	     ObjectInputStream ois = new ObjectInputStream(fis);
	     hist=(Histogram)ois.readObject(); 
	     
	     int[][] f1=new int[256][256];
	     int[] temp1=new int[256];
	     int[] t1=new int[256];
	     int[] t2=new int[256];
	  
	 
	     System.out.println("reading image no:"+hist.imgno+"image name:"+hist.imgname);
	       
	     for(int i=0;i<256;i++)
	     {
	       temp1[i]=i;
	       t2[i]=0;
	       t1[i]=0;
	     }
	  
	  
	     for(int i=0;i<256;i++)
	     {
	      for(int j=0;j<256;j++)
	      {
	       f1[i][j]=Math.abs(temp1[i]-temp1[j]);
	      }
	     }

	     for(int l=0;l<256;l++)
	     {
	      for(int i=0;i<256;i++)
	      {
	       for(int j=0;j<256;j++)
	       {
	        if(f1[i][j]==l&&t1[i]==0&&t2[j]==0)
	        {
	         float l1,l2;
	         l1=hes1[i][1];
	         l2=hist.he1[j][1];
	         if(l1<l2)
	         {
	             w1=w1+l*l1;
	             hes1[i][3]=0;
	             t1[i]=1;
	             hist.he1[j][3]=l2-l1;
	            } 
	        
	            if(l1>l2)
	            {
	             w1=w1+l*l2;
	             hes1[i][3]=l1-l2;
	             t2[j]=1;
	             hist.he1[j][3]=0;
	            } 
	         
	            if(l1==l2)
	            {
	             w1=w1+l*l1;
	             hes1[i][3]=0;
	             t1[i]=1;
	             t2[j]=1;
	             hist.he1[j][3]=0;
	            } 
	           }
	          }
	         }
	        }
	        System.out.println("distance for this image is:"+w1);  
	        int[][] w2;
			w2[k][0]=k;
	        w2[k][1]=w1;
	        w1=0;
	        ois.close();
	        fis.close(); 
	       }
	      }catch(Exception e2){System.out.println(e2);} 
	     }
	 

	   }





	   	
