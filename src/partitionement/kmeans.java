package partitionement;

import java.io.IOException;

public class kmeans {
	public static int[] Assigner(double[][] X, double[][] centres) {
		int[] ret = new int[X.length];
		for(int i = 0; i < ret.length; i++) ret[i] = 0;
		for(int i = 0; i < X.length; i++) {
			for(int k = 1; k < centres.length; k++) {
				if(distance(X[i], centres[k], X[i].length) < distance(X[i], centres[ret[i]], X[i].length)) {
					ret[i] = k;
				}
			}
		}
		return ret;
	}
	
	public static double distance(double[] x1, double[] x2, int dim) {
		double dist = 0;
		for(int i = 0; i < dim; i++) {
			dist += java.lang.Math.pow((x1[i] - x2[i]) ,2);
		}
		return java.lang.Math.sqrt(dist);
	}
	
	public static double Deplct(double[][] X, double[][] centres, int[] assign) {
		double ret = 0;
		for(int i = 0; i < centres.length; i++) {
			double[] newCentre = new double[centres[i].length]; 
			int[] r = new int[X.length];
			int nbAssign = 0;
			for(int j = 0; j < X.length; j++) {
				if(assign[j] == i) {
					r[j] = 1;
					nbAssign++;
				}else {
					r[j] = 0;
				}
			}
			if(nbAssign != 0) {
				double[] num = new double[X[0].length];
				for(int dim = 0; dim < X[0].length; dim++) {
					for(int j = 0; j < X.length; j++) {
					num[dim] += (r[j]*X[j][dim]);
					}
				}
				//System.out.print("newCentre " + i + ": ");
				for(int dim = 0; dim < X[0].length; dim++) {
					newCentre[dim] = num[dim]/nbAssign;
					//System.out.print(newCentre[dim] + " ");
				}
				//System.out.println();
				ret += distance(centres[i], newCentre, newCentre.length);
				centres[i] = newCentre;
			}
		}
		return ret;
	
	}
	
	public static void main(String[] args) throws IOException {
		
		// creation d'un jeu de donnes simples pour tester l'algo
/*
		int D=2; // deux dimensions
		int k=2; // deux centres
		double[][] X = new double[6][D]; // 6 points en D dimensions
		double[][] centres = new double[k][D];

		    centres[0][0] = -1; centres[1][0] = 1;
		    centres[0][1] = 0; centres[1][1] = 0;

		    // position des donnees
		    X[0][0] = -3;   X[0][1] = 1;
		    X[1][0] = -2.5; X[1][1] = -0.5;
		    X[2][0] = -4;   X[2][1] = 0;
		    X[3][0] = 2;    X[3][1] = 2;
		    X[4][0] = 2.5;  X[4][1] = -0.5;
		    X[5][0] = 1.5;  X[5][1] = -1;  
		*/
		/*double[] x = TasGaussien.testLib(100000, true, true);
		double[][] X = new double[x.length][1];
		for(int i = 0; i < x.length; i++) {
			X[i][0] = x[i];
		}
		int D=1; 
		int k=4;
		double[][] centres = new double[k][D];
		centres[0][0] = -1; 
	    centres[1][0] = 0; 
	    centres[2][0] = 2;
	    centres[3][0] = 5; */
		
		
		
		int D=3;
		int k=6;
		double[][] X = new double[54060][D];
	    X = PNG.extractImage();
		double[][] centres = new double[k][D];
		centres[0][0] = 0; 
		centres[1][1] = 2; 
		centres[3][2] = -1; 
		centres[1][0] = 4; 
		centres[4][3] = 2; 
		centres[0][1] = -2; 
		
		
		double eps=0.001;
		double maj = 10;
		int[] ass = new int[X.length];
		while(maj>eps) {
			System.out.println(maj);
		    ass = Assigner(X,centres);
		    maj = Deplct(X,centres,ass);
		}

		// verification
		for(int i=0; i<X.length; i++) {
		    System.out.println("Pt "+i+" assigné à "+ass[i]);
		}
		for(int i=0; i<centres.length; i++) {
		    System.out.println("Pos centre "+i+": "+centres[i][0]      + " " + centres[i][1]);
		} 
	}

}
