package partitionement;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class question2 {
static Random  seed = new Random();
	
	
	
	public static void main(String[] args) throws IOException {
        
		//extraction de l'image
		String path = "/home/tristan/Bureau/";
        String imageMMS = path + "mms.png";

        BufferedImage bui = ImageIO.read(new File(imageMMS));

        int width = bui.getWidth();
        int height = bui.getHeight();
    
        int pixel = bui.getRGB(0, 0);
        Color c = new Color(pixel);
        
        double[][] data = new double[width*height][3];
        int p;
        for (int i=0;i<width;i++) {
        	for (int j=0; j<height;j++) {
        		p = bui.getRGB(i, j);
        	    c = new Color(p);
        	        data[i+j*width][0] = (double) c.getRed()/255.0;
        	        data[i+j*width][1] = (double) c.getGreen()/255.0;
        	        data[i+j*width][2] = (double) c.getBlue()/255.0;
        	}
        }
		
    	int D=3; 
    	
    	//calcul de la meilleur partition pour chaque valeur de k
		for (int k=2;k<11;k++) {
			double meilleurScore = 0;
			double res = 0;
			for (int condInit=0;condInit<10;condInit++) {
				double[][] centres = new double[k][D];
	
				for (int i=0;i<k;i++) {
					for (int j=0;j<D;j++) {
						centres[i][j] = seed.nextDouble(); 
					}
				}
	   
				res = MixGaussi.entrainement(data, centres);
				if (res > meilleurScore)
					meilleurScore = res;
			}
			System.out.println(k + " " + meilleurScore);
		}
	}
}
