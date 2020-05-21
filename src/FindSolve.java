/*
 * class with artificial intelligence to solve
 * the puzzle completely
 * 
 */

/*
 * 0 - up
 * 1 - down
 * 2 - front
 * 3 - back
 * 4 - left
 * 5 - right
 * 
 *     678
 *     345
 *     012
 * 210 036 036 036
 * 543 147 147 147
 * 876 258 258 258
 *     036
 *     147
 *     258
 */
import java.util.ArrayList;
import java.util.SimpleTimeZone;

import javax.naming.TimeLimitExceededException;

public class FindSolve {
	private static ArrayList<Integer> tempSol = new ArrayList<>();
	private static ArrayList<Integer> solution = new ArrayList<>();
	private static boolean isSolved = true;
	private static int[][] cube = new int[6][9];

	public static ArrayList<Integer> solve(int[][] origCube) {
		
		
		
		
		

		copyCube(origCube, cube);
		while (!isComplete(cube)) {
			cube=makeSolution(origCube);
			
		}
		return solution;
	}
	
	// just gathering the whole sub functions
	private static int[][] makeSolution(int[][] origCube)
	{
		int[][] cube = new int[6][9];
		boolean solved = false;
		while (!solved) {
			solved=true;
			solution.removeAll(solution);
			copyCube(origCube, cube);

			solvePlus(cube);
			solveSFace(cube);
			solveMiddle(cube);
			if(!firstTwo(cube))
				solved=false;
			solveTopPlus(cube);
			solveTopLayer(cube);
			solveCouples(cube);
			solveCompletely(cube);
		}
		removeFour(solution);
		return cube;
	}

	// is the cube entirely solved
	private static boolean isComplete(int[][] cube)
	{
		for(int i=0;i<6;i++)
		{
			for(int j=0;j<9;j++)
			{
				if(cube[i][j]!=cube[i][4])
					return false;
			}
		}
		return true;
	}
	
	// are the first two layers solved well
	private static boolean firstTwo(int[][] cube) {
		// check bottom layer
		for (int i = 0; i < 9; i++) {
			if (cube[1][i] != cube[1][4])
				return false;
		}

		// check left
		for (int i = 3; i < 9; i++) {
			if (cube[4][i] != cube[4][4])
				return false;
		}

		// check front
		for (int i = 0; i < 9; i++) {
			if (i % 3 != 0 && cube[2][i] != cube[2][4])
				return false;
		}

		// check right
		for (int i = 0; i < 9; i++) {
			if (i % 3 != 0 && cube[5][i] != cube[5][4])
				return false;
		}

		// check back
		for (int i = 0; i < 9; i++) {
			if (i % 3 != 0 && cube[3][i] != cube[3][4])
				return false;
		}

		return true;
	}

	// solve the last parts of the cube
	private static void solveCompletely(int[][] cube) {
		// algorithms that swap the middle parts on top layer
		int[] complete1 = new int[] { 4, 4, 0, 0, 0, 2, 2, 2, 3, 4, 4, 2, 3, 3, 3, 0, 0, 0, 4, 4 }; // counter
																									// clock
																									// wise
		int[] complete2 = new int[] { 4, 4, 0, 2, 2, 2, 3, 4, 4, 2, 3, 3, 3, 0, 4, 4 }; // clock
																						// wise

		if (howManyTopSide(cube) == 4) {
			while (cube[2][0] != cube[2][4]) {
				cwTwist(cube, 0);
				solution.add(0);
			}
			return;
		}

		if (howManyTopSide(cube) == 0) {
			doAlgorithm(cube, complete1);
			solveCompletely(cube);
			return;
		}

		if (howManyTopSide(cube) == 1) {
			while (cube[5][3] != cube[5][0]) {
				cwTwist(cube, 0);
				solution.add(0);
				if(solution.size()>1000000)
					return;
			}
			if (cube[4][1] == cube[2][6]) {
				doAlgorithm(cube, complete1);
				solveCompletely(cube);
				return;
			}
			if (cube[2][3] == cube[4][0]) {
				doAlgorithm(cube, complete2);
				solveCompletely(cube);
				return;
			}
		}
		isSolved = false;
	}

	// make couples of colours from the faces on the sides
	private static void solveCouples(int[][] cube) {
		// the algorithm to solve couples
		int[] couples = new int[] { 5, 5, 5, 2, 5, 5, 5, 3, 3, 5, 2, 2, 2, 5, 5, 5, 3, 3, 5, 5 };

		if (cube[3][0] == cube[3][6] && cube[5][0] == cube[5][6])
			return;
		for (int i = 0; i < 4; i++) {
			if (cube[3][0] == cube[3][6]) {
				doAlgorithm(cube, couples);
				return;
			}
			cwTwist(cube, 0);
			solution.add(0);
			if(solution.size()>1000000)
				return;
		}
		doAlgorithm(cube, couples);
		solveCouples(cube);
	}

	// make all top side on right color
	private static void solveTopLayer(int[][] cube) {

		// algorithms to make another step correcting all top layer
		int[] topLayer1 = new int[] { 5, 0, 5, 5, 5, 0, 5, 0, 0, 5, 5, 5 };
		int[] topLayer2 = new int[] { 4, 4, 4, 0, 0, 0, 4, 0, 0, 0, 4, 4, 4, 0, 0, 4 };
		if (howManyTopCorners(cube) == 4)
			return;
		// there was a problem in former steps, trying to solve again
		if (howManyTopCorners(cube) == 3) {
			isSolved = false;
			return;
		}
		if (howManyTopCorners(cube) == 1) {
			if (cube[2][0] == cube[0][4] || cube[5][0] == cube[0][4]) {
				while (cube[0][2] != cube[0][4]) {
					cwTwist(cube, 0);
					solution.add(0);
					if(solution.size()>1000000)
						return;
				}
				doAlgorithm(cube, topLayer2);
				return;
			}
			if (cube[2][6] == cube[0][4] || cube[5][6] == cube[0][4]) {
				while (cube[0][0] != cube[0][4]) {
					cwTwist(cube, 0);
					solution.add(0);
					if(solution.size()>1000000)
						return;
				}
				doAlgorithm(cube, topLayer1);
				return;
			}
		}
		if (howManyTopCorners(cube) == 0) {
			while (cube[4][0] != cube[0][4]) {
				cwTwist(cube, 0);
				solution.add(0);
				if(solution.size()>1000000)
					return;
			}
			doAlgorithm(cube, topLayer1);
			solveTopLayer(cube);
			return;
		}
		if (howManyTopCorners(cube) == 2) {
			while (cube[2][2] != cube[0][4]) {
				cwTwist(cube, 0);
				solution.add(0);
				if(solution.size()>1000000)
					return;
				
			}
			doAlgorithm(cube, topLayer1);
			solveTopLayer(cube);
			return;
		}
	}

	private static void solveTopPlus(int[][] cube) {
		// algorithm to make another step forming a plus on top layer
		int[] topPlus = new int[] { 2, 5, 0, 5, 5, 5, 0, 0, 0, 2, 2, 2 };

		// return if top plus is solved
		if (howManyTopPlus(cube) == 4)
			return;

		// if there is a vertical line
		if (cube[0][1] == cube[0][4] && cube[0][7] == cube[0][4]) {
			solution.add(0);
			cwTwist(cube, 0);
		}

		// if there is an horizontal
		if (cube[0][3] == cube[0][4] && cube[0][5] == cube[0][4]) {
			doAlgorithm(cube, topPlus);
			return;
		}

		// if there is an L
		if (howManyTopPlus(cube) == 2) {
			// i want the L with back and left pieces
			while (!(cube[0][3] == cube[0][4] && cube[0][7] == cube[0][4])) {
				solution.add(0);
				cwTwist(cube, 0);
			}
			doAlgorithm(cube, topPlus);
			doAlgorithm(cube, topPlus);
			return;
		}

		// if the code reached here, then the plus is only the middle piece
		doAlgorithm(cube, topPlus);
		solution.add(0);
		cwTwist(cube, 0);
		solution.add(0);
		cwTwist(cube, 0);
		doAlgorithm(cube, topPlus);
		doAlgorithm(cube, topPlus);
	}

	// solve the middle layer
	private static void solveMiddle(int[][] cube) {
		int spins = 0;

		long iters=0;
		// algorithms
		
		// turn front up piece to the left
		int[] frontLeft = new int[] { 0, 0, 0, 4, 4, 4, 0, 4, 0, 2, 0, 0, 0, 2, 2, 2 }; 
		
		// turn front up piece to the right
		int[] frontRight = new int[] { 0, 5, 0, 0, 0, 5, 5, 5, 0, 0, 0, 2, 2, 2, 0, 2 }; 
		
		// turn left up piece to the left
		int[] leftLeft = new int[] { 0, 0, 0, 3, 3, 3, 0, 3, 0, 4, 0, 0, 0, 4, 4, 4 }; 

		// turn left up piece to the right
		int[] leftRight = new int[] { 0, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 4, 4, 4, 0, 4 }; 

		// turn back up piece to the left
		int[] backLeft = new int[] { 0, 0, 0, 5, 5, 5, 0, 5, 0, 3, 0, 0, 0, 3, 3, 3 }; 

		// turn back up piece to the right
		int[] backRight = new int[] { 0, 4, 0, 0, 0, 4, 4, 4, 0, 0, 0, 3, 3, 3, 0, 3 }; 
		
		// turn right up piece to the left
		int[] rightLeft = new int[] { 0, 0, 0, 2, 2, 2, 0, 2, 0, 5, 0, 0, 0, 5, 5, 5 }; 
		
		// turn right up piece to the right
		int[] rightRight = new int[] { 0, 3, 0, 0, 0, 3, 3, 3, 0, 0, 0, 5, 5, 5, 0, 5 }; 

		while (howManyMiddle(cube) < 4 && spins < 30) {
			// front
			if (cube[2][3] == cube[2][4]) {
				if (cube[0][1] == cube[5][4])
					doAlgorithm(cube, frontRight);
				if (cube[0][1] == cube[4][4])
					doAlgorithm(cube, frontLeft);
			}

			// right
			if (cube[5][3] == cube[5][4]) {
				if (cube[0][5] == cube[2][4])
					doAlgorithm(cube, rightLeft);
				if (cube[0][5] == cube[3][4])
					doAlgorithm(cube, rightRight);
			}

			// left
			if (cube[4][1] == cube[4][4]) {
				if (cube[0][3] == cube[3][4])
					doAlgorithm(cube, leftLeft);
				if (cube[0][3] == cube[2][4])
					doAlgorithm(cube, leftRight);
			}

			// back
			if (cube[3][3] == cube[3][4]) {
				if (cube[0][7] == cube[5][4])
					doAlgorithm(cube, backLeft);
				if (cube[0][7] == cube[4][4])
					doAlgorithm(cube, backRight);
			}
			spins++;
			solution.add(0);
			cwTwist(cube, 0); // twist up

			// if front right part is swapped, put it out
			if (spins > 10 && (cube[2][7] == cube[0][4] || cube[5][1] == cube[0][4]))
				doAlgorithm(cube, frontRight);
			// if front left part is swapped, put it out
			if (spins > 10 && (cube[2][1] == cube[0][4] || cube[4][1] == cube[0][4]))
				doAlgorithm(cube, frontLeft);
			// if back right part is swapped, put it out
			if (spins > 10 && (cube[3][7] == cube[0][4] || cube[4][5] == cube[0][4]))
				doAlgorithm(cube, backRight);
			// if back left part is swapped, put it out
			if (spins > 10 && (cube[3][1] == cube[0][4] || cube[5][7] == cube[0][4]))
				doAlgorithm(cube, backLeft);

			if (spins > 10 && cube[2][7] == cube[5][4] && cube[5][1] == cube[2][4])
				doAlgorithm(cube, frontRight);
			// if front left part is swapped, put it out
			if (spins > 10 && cube[2][1] == cube[2][4] && cube[4][1] == cube[2][4])
				doAlgorithm(cube, frontLeft);
			// if back right part is swapped, put it out
			if (spins > 10 && cube[3][7] == cube[4][4] && cube[4][5] == cube[3][4])
				doAlgorithm(cube, backRight);
			// if back left part is swapped, put it out
			if (spins > 10 && cube[3][1] == cube[5][4] && cube[5][7] == cube[3][4])
				doAlgorithm(cube, backLeft);
			if(iters>1000000)
				return;
		}
	}

	// perform given series of moves
	private static void doAlgorithm(int[][] cube, int[] algorithm) {
		for (int i = 0; i < algorithm.length; i++) {
			cwTwist(cube, algorithm[i]);
			solution.add(algorithm[i]);
		}
	}

	// solve the whole bottom side
	private static void solveSFace(int[][] origCube) {
		long iters=0;
		int i = 0, j = 0;
		int prev, current;
		int[][] cube = new int[6][9]; // this will be a backup
		copyCube(origCube, cube);

		// while face not solved
		while (howManyCorners(cube) < 4) {
			prev = howManyCorners(cube);
			tempSol.clear();
			j = (int) ((Math.random() * 10) + 1);
			for (i = 0; i < j; i++) // might want to change number of iterations
			{
				ranTwist(origCube, false);
				current = howManyCorners(origCube);
				// if made another corner and did not destroy plus
				if (current > prev && howManyPlus(origCube) == 4) {
					solution.addAll(tempSol);
					copyCube(origCube, cube);
					break;
				}
			}
			// if didn't make another part, put the cube back
			if (i == j)
				copyCube(cube, origCube);

			if(iters>1000000)
				return;
		}
	}

	// make plus on the bottom side
	private static void solvePlus(int[][] origCube) {
		int i = 0, j = 0;
		int prev, current;
		int[][] cube = new int[6][9]; // this will be a backup
		copyCube(origCube, cube);
		long iters=0;
		// while plus not solved
		while (howManyPlus(origCube) < 4) {
			iters++;
			prev = howManyPlus(origCube);
			tempSol.clear();
			j = (int) ((Math.random() * 5) + 1);
			for (i = 0; i < j; i++) // might want to change number of iterations
			{
				ranTwist(origCube, true);
				current = howManyPlus(origCube);
				// if made another part of the plus
				if (current > prev) {
					solution.addAll(tempSol);
					copyCube(origCube, cube);
					break;
				}
			}
			// if didn't make another part, put the cube back
			if (i == j)
				copyCube(cube, origCube);
			if(iters>1000000)
				return;
		}
	}

	// how many of the top middle parts are on the right side
	private static int howManyTopSide(int[][] cube) {
		int sum = 0;
		if (cube[2][3] == cube[2][6])
			sum++;
		if (cube[5][3] == cube[5][6])
			sum++;
		if (cube[3][3] == cube[3][6])
			sum++;
		if (cube[4][2] == cube[4][1])
			sum++;
		return sum;
	}

	// return how many top corners are positioned correctly
	private static int howManyTopCorners(int[][] cube) {
		int sum = 0;
		if (cube[0][0] == cube[0][4])
			sum++;
		if (cube[0][2] == cube[0][4])
			sum++;
		if (cube[0][6] == cube[0][4])
			sum++;
		if (cube[0][8] == cube[0][4])
			sum++;
		return sum;
	}

	// return how many parts in top plus are solved
	private static int howManyTopPlus(int[][] cube) {
		int sum = 0;
		if (cube[0][1] == cube[0][4])
			sum++;
		if (cube[0][3] == cube[0][4])
			sum++;
		if (cube[0][5] == cube[0][4])
			sum++;
		if (cube[0][7] == cube[0][4])
			sum++;
		return sum;
	}

	// return how many parts in middle layer are solved
	private static int howManyMiddle(int[][] cube) {
		int sum = 0;
		if (cube[4][3] == cube[4][4] && cube[2][1] == cube[2][4])
			sum++;
		if (cube[5][1] == cube[5][4] && cube[2][7] == cube[2][4])
			sum++;
		if (cube[5][7] == cube[5][4] && cube[3][1] == cube[3][4])
			sum++;
		if (cube[4][5] == cube[4][4] && cube[3][7] == cube[3][4])
			sum++;
		return sum;
	}

	// return how many corners from bottom face positioned correctly
	private static int howManyCorners(int[][] cube) {
		int sum = 0;
		if (cube[1][0] == cube[1][4] && cube[2][2] == cube[2][4] && cube[4][6] == cube[4][4])
			sum++;
		if (cube[1][6] == cube[1][4] && cube[2][8] == cube[2][4] && cube[5][2] == cube[5][4])
			sum++;
		if (cube[1][8] == cube[1][4] && cube[5][8] == cube[5][4] && cube[3][2] == cube[3][4])
			sum++;
		if (cube[1][2] == cube[1][4] && cube[3][8] == cube[3][4] && cube[4][8] == cube[4][4])
			sum++;
		return sum;
	}

	// return how many parts of the plus on the bottom side is complete
	private static int howManyPlus(int[][] cube) {
		int sum = 0;
		if (cube[1][3] == cube[1][4] && cube[2][5] == cube[2][4])
			sum++;
		if (cube[1][1] == cube[1][4] && cube[4][7] == cube[4][4])
			sum++;
		if (cube[1][5] == cube[1][4] && cube[0][7] == cube[0][4])
			sum++;
		if (cube[1][7] == cube[1][4] && cube[5][5] == cube[5][4])
			sum++;
		return sum;
	}

	// perform clock wise twist
	private static void cwTwist(int[][] cube, int face) {
		RubiksCube.twistLayers(cube, face, 1, 0);
	}

	// perform double twist
	private static void doubleTwist(int[][] cube, int face) {
		RubiksCube.twistLayers(cube, face, 2, 0);
	}

	// perform counter clock wise twist
	private static void ccwTwist(int[][] cube, int face) {
		RubiksCube.twistLayers(cube, face, 3, 0);
	}

	// perform random twist on a random face
	private static void ranTwist(int[][] cube, boolean twistBottom) {
		int howMany = (int) ((Math.random() * 3) + 1);
		int face;
		if (twistBottom)
			face = (int) (Math.random() * 6);
		else {
			face = (int) (Math.random() * 5);
			if (face == 1)
				face = 5;
		}
		RubiksCube.twistLayers(cube, face, howMany, 0);
		for (int i = 0; i < howMany; i++)
			tempSol.add(face);
	}

	// copy orig to dest
	private static void copyCube(int[][] orig, int[][] dest) {
		for (int i = 0; i < orig.length; i++) {
			for (int j = 0; j < orig[i].length; j++) {
				dest[i][j] = orig[i][j];
			}
		}
	}

	// remove unnecessary four twists on same face
	private static void removeFour(ArrayList<Integer> solution) {
		int n0, n1, n2, n3; // consistent four twists
		/*
		 * System.out.println("***********FIRST"); for(int
		 * i=0;i<solution.size();i++) { System.out.print(solution.get(i)+" "); }
		 */
		for (int i = 0; i < solution.size() - 4; i++) {
			n0 = solution.get(i);
			n1 = solution.get(i + 1);
			n2 = solution.get(i + 2);
			n3 = solution.get(i + 3);
			if (n0 == n1 && n1 == n2 && n2 == n3) {
				solution.remove(i + 3);
				solution.remove(i + 2);
				solution.remove(i + 1);
				solution.remove(i);
			}
		}
	}
}
