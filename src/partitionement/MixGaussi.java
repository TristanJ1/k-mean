package partitionement;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.Random;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner; 

import javax.imageio.ImageIO;

public class MixGaussi {
	
	
	static int nbCentres = 8; //15; 
	
	
	
	static BufferedImage bi;
	static Random  seed = new Random();
	
	
	//Assignation de la valeur de r
	public static double[][] assign(double[][] donnees, double[][] centre, double[][] sigma, double[] p){
		double[][] ret = new double[donnees.length][centre.length];
		
		for(int d = 0; d < donnees.length; d++) {
			double[] num = gaussienne(donnees[d], centre, sigma, p);
			double den = 0;
			for(int j = 0; j < num.length; j++) {
				den += num[j];
			}
			for(int k = 0; k < centre.length; k++) {
				ret[d][k] = num[k]/den;	
			}
		}
		return ret;
	}
	
	//genere une gaussienne
	public static double[] gaussienne(double[] donnees, double[][] centre, double[][] sigma, double[] p) {
		double[] ret = new double[centre.length];
		for(int i = 0; i < ret.length; i++) {
			ret[i] = p[i];
		}
		for(int k = 0; k < centre.length; k++) {
			for(int i = 0; i < centre[0].length; i++) {
				ret[k] *= (1/java.lang.Math.sqrt(2*Math.PI*sigma[k][i])) * java.lang.Math.exp(-((donnees[i]-centre[k][i])*(donnees[i]-centre[k][i])) /(2*sigma[k][i]));
			}
		}
		return ret;
	}
	//Mis a jour des autres parametres
	public static void Deplct(double[][] donnees, double[][] centre, double[][] sigma, double[] p, double[][] proba) {
		double[] R = new double[centre.length];
		for(int i = 0; i < R.length; i++) {
			R[i] = 0;   //Rk = 0
		}
		for(int k = 0; k < R.length; k++) {
			for(int d = 0; d < donnees.length; d++) {
				R[k] += proba[d][k];   //Rk
			}
		}
		for(int i = 0; i < centre.length; i++) {
			for(int j = 0; j < centre[0].length; j++) {
				centre[i][j] = 0;  //m = 0
			}
		}
		for(int k = 0; k < centre.length; k++) {
			for(int i = 0; i < centre[0].length; i++) {
				for(int d = 0; d < donnees.length; d++) {
					centre[k][i] += proba[d][k]*donnees[d][i]; //m 
				}
				centre[k][i] = centre[k][i]/R[k];
			}
		}
		for(int k = 0; k < sigma.length; k++) {
			for(int i = 0; i < sigma[0].length; i++) {
				sigma[k][i] = 0;  //sigma = 0
			}
		}
		for(int k = 0; k < sigma.length; k++) {
			for(int i = 0; i < sigma[0].length; i++) {
				for(int d = 0; d < donnees.length; d++) {
					sigma[k][i] += (proba[d][k]*(donnees[d][i] - centre[k][i])*(donnees[d][i] - centre[k][i])); //sigma
				} 
				sigma[k][i] = sigma[k][i]/R[k];
			}
		}
		for(int k = 0; k < p.length; k++) {
			p[k] = R[k]/donnees.length; //p
		}
	}
	
	//entraine le modele et renvoie le score final 
	public static double entrainement(double[][] donnees, double[][] centre) {
		double[][] sigma = new double[centre.length][centre[0].length];
		double[] p = new double[centre.length];
		
		// Initialisation: sigma = 0,5 
		//  			   rho = 1/K
		for(int i = 0; i < sigma.length; i++) {
			for(int j = 0; j < sigma[0].length; j++) {
				sigma[i][j] = 0.5;
			}
		}
		
		for(int i = 0; i < p.length; i++) {
			p[i] =  (double)1/donnees.length;
		}
		double[][] proba = new double[donnees.length][centre.length];
		
		int nbEpoques = 50;
		
		for(int i = 0; i < nbEpoques; i++) {
			proba = assign(donnees, centre, sigma, p);
			Deplct(donnees, centre, sigma, p, proba);
		}
		return score(donnees,centre,sigma,p);
	}
	
	//calcule le score du modele
	public static double score(double[][] donnees, double[][] centre,double[][] sigma, double[] p) {
		double[] s = new double[donnees.length];
		double[] score = new double[donnees.length];
		double res = 0;
		
		for (int d=0;d<donnees.length;d++) {
			s = gaussienne(donnees[d], centre, sigma, p);
			for (int i=0;i<s.length;i++) {
				score[d] += s[i];
			}
			res += Math.log(score[d]);
		}
		
		return res/donnees.length;
	}
	
	// Fonction assigner de l'algorithme des k-moyennes permettant d'associer a chaque pixel
	// le centre le plus proche. C'est utile pour generer l'image de sortie
	public static int[] Assigner(double[][] donnees, double[][] centres) {
		int[] ret = new int[donnees.length];
		for(int i = 0; i < ret.length; i++) ret[i] = 0;
		for(int i = 0; i < donnees.length; i++) {
			for(int k = 1; k < centres.length; k++) {
				if(distance(donnees[i], centres[k]) < distance(donnees[i], centres[ret[i]])) {
					ret[i] = k;
				}
			}
		}
		return ret;
	}
	
	public static double distance(double[] x1, double[] x2) {
		double dist = 0;
		for(int i = 0; i < x1.length; i++) {
			dist += java.lang.Math.pow((x1[i] - x2[i]) ,2);
		}
		return java.lang.Math.sqrt(dist);
	}
	
	
    
	public static void main(String[] args) throws IOException {
		
		//Stockage de l'image dans un tableau "donnees"
        String path = "/home/tristan/Bureau/";
        String imageMMS = path + "mms.png";

        // Lecture de l'image ici
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
	    
		//Afichage des resultats
	    double [][] res = new double[height*width][2];
	    System.out.println("Score: " + entrainement(donnees, centres));
	    int[] ass = Assigner(donnees,centres);
	    for(int i = 0; i < centres.length; i++) {
	    	for (int j=0; j<centres[0].length;j++) {
	    		System.out.println("Centre " + i + j + ": " + centres[i][j]*255);
	    	}
	    }
        
        //creation de l'image de sortie
	   BufferedImage bui_out = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        for(int i=0 ; i<height*width ; i++) {
        	res[i] = centres[ass[i]];
        }
	    for(int i = 0; i < height; i++) {
	    	for (int j=0; j<width;j++) {
	    	 Color r = new Color((float)res[i*width+j][0],(float) res[i*width+j][1],(float) res[i*width+j][2]);
        	bui_out.setRGB(j,i,r.getRGB());}}
        ImageIO.write(bui_out, "PNG", new File(path+"test.png"));
	}
}

