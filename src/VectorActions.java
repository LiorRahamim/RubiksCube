/*
 * this class contains functions that manipulate 1 dimensional arrays
 * 
 */
public class VectorActions 
{
 
     // copy srcVec to vector
	public static double[] vCopy(double[] vector, double[] srcVec) {
		vector[0] = srcVec[0];
		vector[1] = srcVec[1];
		vector[2] = srcVec[2];
		return vector;
	}
	
	// product of two vectors
	public static double vProd(double[] vec1, double[] vec2) {
		return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];
	}

	// unit vector
	public static double[] vNorm(double[] vector) {
		double length = Math.sqrt(vProd(vector, vector));
		vector[0] /= length;
		vector[1] /= length;
		vector[2] /= length;
		return vector;
	}

	// multiply vector by value
	public static double[] vScale(double[] vector, double value) {
		vector[0] *= value;
		vector[1] *= value;
		vector[2] *= value;
		return vector;
	}

	// add srcVec to vector
	public static double[] vAdd(double[] vector, double[] srcVec) {
		vector[0] += srcVec[0];
		vector[1] += srcVec[1];
		vector[2] += srcVec[2];
		return vector;
	}

	// reduce srcVec from vector
	public static double[] vSub(double[] vector, double[] srcVec) {
		vector[0] -= srcVec[0];
		vector[1] -= srcVec[1];
		vector[2] -= srcVec[2];
		return vector;
	}

	// vector multiplication
	public static double[] vMul(double[] vector, double[] vec1, double[] vec2) {
		vector[0] = vec1[1] * vec2[2] - vec1[2] * vec2[1];
		vector[1] = vec1[2] * vec2[0] - vec1[0] * vec2[2];
		vector[2] = vec1[0] * vec2[1] - vec1[1] * vec2[0];
		return vector;
	}
}

