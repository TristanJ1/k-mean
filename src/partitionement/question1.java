package partitionement;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.Random;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import javax.imageio.ImageIO;

public class question1 {
	
	
	static int nbCentres = 10;
	
	
	
	
	static BufferedImage bi;
	static Random  seed = new Random();
    
	public static void main(String[] args) throws IOException {
		
        String path = "/home/tristan/Bureau/";
        String imageMMS = path + "balloon.jpg";

        // extraction de l'image
        BufferedImage bui = ImageIO.read(new File(imageMMS));

        int width = bui.getWidth();
        int height = bui.getHeight();
    
        int pixel = bui.getRGB(0, 0);
        Color c = new Color(pixel);   
        double[][] donnees = new double[width*height][3];
        int p;
        for (int i=0;i<width;i++) {
        	for (int j=0; j<height;j++) {
        		p = bui.getRGB(i, j);
        	    c = new Color(p);
        	        donnees[i+j*width][0] = (double) c.getRed()/255.0;
        	        donnees[i+j*width][1] = (double) c.getGreen()/255.0;
        	        donnees[i+j*width][2] = (double) c.getBlue()/255.0;
        	}
        }
	
		//generation aleatoire des centres
		int D=3; 
		double[][] centres = new double[nbCentres][D];
		
		for (int i=0;i<nbCentres;i++) {
			centres[i][0] = seed.nextDouble();    
		    centres[i][1] = seed.nextDouble();   
		    centres[i][2] = seed.nextDouble();
		}
	    
		//Affichage des resultats
	    double [][] res = new double[height*width][2];
	    System.out.println(MixGaussi.entrainement(donnees, centres));
	    int[] ass = MixGaussi.Assigner(donnees,centres);
	    for(int i = 0; i < centres.length; i++) {
	    	for (int j=0; j<centres[0].length;j++) {
	    		System.out.println("Centre " + i + j + ": " + centres[i][j]*255);
	    	}
	    }
        
        //Generation de l'image de sortie
	   BufferedImage bui_out = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        for(int i=0 ; i<height*width ; i++) {
        	res[i] = centres[ass[i]];
        }
	    for(int i = 0; i < height; i++) {
	    	for (int j=0; j<width;j++) {
	    	 Color r = new Color((float)res[i*width+j][0],(float) res[i*width+j][1],(float) res[i*width+j][2]);
        	bui_out.setRGB(j,i,r.getRGB());}}
        ImageIO.write(bui_out, "PNG", new File(path+"test2.png"));
	}
}
