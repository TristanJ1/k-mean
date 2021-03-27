package partitionement;

import java.util.Random;

public class TasGaussien {
	
	static Random  seed = new Random();
	
	public static double[] testLib(int nb, boolean gauss, boolean quest4) {
		double moy = 0;
		double[] x = new double[nb];
		if(quest4 == false) {
			for(int i = 0; i < nb; i++) {
				if(gauss == false) {
					x[i] = seed.nextDouble();
				}else {
					x[i] = seed.nextGaussian();
				}
				moy += x[i];
			}
		}else {
			for(int i = 0; i < nb; i++) {
				if(i < nb/4) {
					x[i] = seed.nextGaussian() - 5;  //-6  -4
				}else if(i < 2*nb/4){
					x[i] = seed.nextGaussian() + -1; //-2  0
				}else if(i < 3*nb/4){
					x[i] = seed.nextGaussian() + 2;  //1  3
				}else {
					x[i] = seed.nextGaussian() + 6;  //5  7
				}
			}
		}
		moy /= nb;
		System.out.println("moyenne = " + moy);
		double var = 0;
		for(int i = 0; i < nb; i++) {
			var += java.lang.Math.pow((x[i]-moy), 2);
		}
		var /= nb;
		double eca = java.lang.Math.sqrt(var);
		System.out.println("variance = " + var);
		System.out.println("écart-type = " + eca);
		double[][] his = new double[0][0];
		if(gauss == false) {
			his = histogramme(0,1,10,x);
		}else {
			his = histogramme(-10,10,20,x);
		}
		for(int i = 0; i < his[0].length; i++) {
			System.out.println(his[0][i] + " " + his[1][i]);
		}
		return x;
	}
	
	
	public static double[][] histogramme(double xmin, double xmax, int NbCases, double[] ech) {
	    double[][] Histo = new double[2][NbCases];
	    // TODO: Calcule de la taille d'une case
	    double tailleCase = (xmax - xmin)/NbCases;
	    for(int i = 0; i < NbCases; i++) {
	    	Histo[0][i] = xmin + tailleCase*(i+1) - tailleCase/2;
	    }

	    for(int i = 0; i<ech.length; i++) {
	        // TODO: pour chaque valeur: trouver a quelle case elle appartient et incrementer de un l'histogramme
	    	for(int j = 0; j < NbCases; j++) {
	    		if(ech[i] < (j+1)*tailleCase + xmin) {
	    			Histo[1][j]++;
	    			break;
	    		}
	    	}
	    }
	    return Histo;
	}
	
	public static void main(String[] args) {
		double[] x = testLib(10000, true, true);
	}

}
