package partitionement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class question3 {
	
	static Random  seed = new Random();
	
	//extraction des donnees du fichier
	public static double[][] readFile() {
    	double[][] res = new double[4000][2];
        try {
          File myObj = new File("/home/tristan/Bureau/gmm_data.d");
          Scanner myReader = new Scanner(myObj);
          int i=0;
          while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] mots = data.split(" ");
            res[i][0] = Double.valueOf(mots[0]);
            res[i][1] = Double.valueOf(mots[1]);
            i++;
          }
          myReader.close();
        } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
        return res;
      }
	
	
	
	public static void main(String[] args) {
        
	    double data[][] = readFile();
	    
	    int D=2; 
	  //calcul de la meilleur partition pour chaque valeur de k
		for (int k=2;k<11;k++) {
			double meilleurScore = -10;
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
