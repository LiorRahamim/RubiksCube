// this class contains the assisting arrays to represent the rubik's cube physics

// it uses the following hypothesis:
/*
 * 0 - up
 * 1 - down
 * 2 - front
 * 3 - back
 * 4 - left 
 * 5 - right
 * 
 */

public class AssistingArrays {
	// various sign tables for computation of directions of rotations
	private static final int[][][] rotCos = { { { 1, 0, 0 }, { 0, 0, 0 }, { 0, 0, 1 } }, // U-D
			{ { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }, // F-B
			{ { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } } // L-R
	};
	private static final int[][][] rotSin = { { { 0, 0, 1 }, { 0, 0, 0 }, { -1, 0, 0 } }, // U-D
			{ { 0, 1, 0 }, { -1, 0, 0 }, { 0, 0, 0 } }, // F-B
			{ { 0, 0, 0 }, { 0, 0, 1 }, { 0, -1, 0 } } // L-R
	};
	private static final int[][][] rotVec = { { { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }, // U-D
			{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 1 } }, // F-B
			{ { 1, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } } // L-R
	};
	private static final int[] rotSign = { 1, -1, 1, -1, 1, -1 }; // U, D, F, B,
																	// L, R

	// vertices coordinates
	private static final double[][] cornerPos = { { -1, -1, -1 }, // UFL
			{ 1, -1, -1 }, // UFR
			{ 1, -1, 1 }, // UBR
			{ -1, -1, 1 }, // UBL
			{ -1, 1, -1 }, // DFL
			{ 1, 1, -1 }, // DFR
			{ 1, 1, 1 }, // DBR
			{ -1, 1, 1 } // DBL
	};

	// corresponding corners on the opposite face
	private static final int[][] oppositeCorners = { { 0, 3, 2, 1 }, // U->D
			{ 0, 3, 2, 1 }, // D->U
			{ 3, 2, 1, 0 }, // F->B
			{ 3, 2, 1, 0 }, // B->F
			{ 0, 3, 2, 1 }, // L->R
			{ 0, 3, 2, 1 }, // R->L
	};

	// vertices of each side
	private static final int[][] planeCorners = { { 0, 1, 2, 3 }, // U: UFL UFR
																	// UBR UBL
			{ 4, 7, 6, 5 }, // D: DFL DBL DBR DFR
			{ 0, 4, 5, 1 }, // F: UFL DFL DFR UFR
			{ 2, 6, 7, 3 }, // B: UBR DBR DBL UBL
			{ 0, 3, 7, 4 }, // L: UFL UBL DBL DFL
			{ 1, 5, 6, 2 } // R: UFR DFR DBR UBR
	};

	// face normal vectors
	private static final double[][] planeNormals = { { 0, -1, 0 }, // upper
			{ 0, 1, 0 }, // down
			{ 0, 0, -1 }, // front
			{ 0, 0, 1 }, // back
			{ -1, 0, 0 }, // left
			{ 1, 0, 0 } // right
	};

	// faces near to each face
	private static final int[][] nearFaces = { { 2, 5, 3, 4 }, // U: F R B L
			{ 4, 3, 5, 2 }, // D: L B R F
			{ 4, 1, 5, 0 }, // F: L D R U
			{ 5, 1, 4, 0 }, // B: R D L U
			{ 0, 3, 1, 2 }, // L: U B D F
			{ 2, 1, 3, 0 } // R: F D B U
	};

	// transformation
	private static final int[] posFaceTransform = { 3, 2, 0, 5, 1, 4 };

	// transformation
	private static final int[][] posFaceletTransform = { { 6, 3, 0, 7, 4, 1, 8, 5, 2 }, // B
																						// +27
			{ 2, 5, 8, 1, 4, 7, 0, 3, 6 }, // F +18
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8 }, // U +0
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8 }, // R +45
			{ 6, 3, 0, 7, 4, 1, 8, 5, 2 }, // D +9
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8 } // L +36
	};

	private static final int[] moveModes = { 0, 0, 0, 0, 0, 0, // UDFBLR
			1, 1, 1, // ESM
			3, 3, 3, 3, 3, 3, // XYZxyz
			2, 2, 2, 2, 2, 2 // udfblr
	};
	private static final int[] moveCodes = { 0, 1, 2, 3, 4, 5, // UDFBLR
			1, 2, 4, // ESM
			5, 2, 0, 5, 2, 0, // XYZxyz
			0, 1, 2, 3, 4, 5 // udfblr
	};

	// cube dimensions in number of facelets (mincol, maxcol, minrow, maxrow)
	// for compact cube
	private static final int[][][] cubeBlocks = { { { 0, 3 }, { 0, 3 } }, // U
			{ { 0, 3 }, { 0, 3 } }, // D
			{ { 0, 3 }, { 0, 3 } }, // F
			{ { 0, 3 }, { 0, 3 } }, // B
			{ { 0, 3 }, { 0, 3 } }, // L
			{ { 0, 3 }, { 0, 3 } } // R
	};
	// all possible subcube dimensions for top and bottom layers
	private static final int[][][] topBlockTable = { { { 0, 0 }, { 0, 0 } }, { { 0, 3 }, { 0, 3 } },
			{ { 0, 3 }, { 0, 1 } }, { { 0, 1 }, { 0, 3 } }, { { 0, 3 }, { 2, 3 } }, { { 2, 3 }, { 0, 3 } } };
	// subcube dimmensions for middle layers
	private static final int[][][] midBlockTable = { { { 0, 0 }, { 0, 0 } }, { { 0, 3 }, { 1, 2 } },
			{ { 1, 2 }, { 0, 3 } } };
	// indices to topBlockTable[] and botBlockTable[] for each twistedLayer
	// value
	private static final int[][] topBlockFaceDim = {
			// U D F B L R
			{ 1, 0, 3, 3, 2, 3 }, // U
			{ 0, 1, 5, 5, 4, 5 }, // D
			{ 2, 3, 1, 0, 3, 2 }, // F
			{ 4, 5, 0, 1, 5, 4 }, // B
			{ 3, 2, 2, 4, 1, 0 }, // L
			{ 5, 4, 4, 2, 0, 1 } // R
	};
	private static final int[][] midBlockFaceDim = {
			// U D F B L R
			{ 0, 0, 2, 2, 1, 2 }, // U
			{ 0, 0, 2, 2, 1, 2 }, // D
			{ 1, 2, 0, 0, 2, 1 }, // F
			{ 1, 2, 0, 0, 2, 1 }, // B
			{ 2, 1, 1, 1, 0, 0 }, // L
			{ 2, 1, 1, 1, 0, 0 } // R
	};
	private static final int[][] botBlockFaceDim = {
			// U D F B L R
			{ 0, 1, 5, 5, 4, 5 }, // U
			{ 1, 0, 3, 3, 2, 3 }, // D
			{ 4, 5, 0, 1, 5, 4 }, // F
			{ 2, 3, 1, 0, 3, 2 }, // B
			{ 5, 4, 4, 2, 0, 1 }, // L
			{ 3, 2, 2, 4, 1, 0 } // R
	};
	// top facelet cycle
	private static final int[] cycleOrder = { 0, 1, 2, 5, 8, 7, 6, 3 };
	// side facelet cycle offsets
	private static final int[] cycleFactors = { 1, 3, -1, -3, 1, 3, -1, -3 };
	private static final int[] cycleOffsets = { 0, 2, 8, 6, 3, 1, 5, 7 };
	// indices for faces of layers
	private static final int[][] cycleLayerSides = { { 3, 3, 3, 0 }, // U:
																		// F=6-3k
																		// R=6-3k
																		// B=6-3k
																		// L=k
			{ 2, 1, 1, 1 }, // D: L=8-k B=2+3k R=2+3k F=2+3k
			{ 3, 3, 0, 0 }, // F: L=6-3k D=6-3k R=k U=k
			{ 2, 1, 1, 2 }, // B: R=8-k D=2+3k L=2+3k U=8-k
			{ 3, 2, 0, 0 }, // L: U=6-3k B=8-k D=k F=k
			{ 2, 2, 0, 1 } // R: F=8-k D=8-k B=k U=2+3k
	};
	// indices for sides of center layers
	private static final int[][] cycleCenters = { { 7, 7, 7, 4 }, // E'(U):
																	// F=7-3k
																	// R=7-3k
																	// B=7-3k
																	// L=3+k
			{ 6, 5, 5, 5 }, // E (D): L=5-k B=1+3k R=1+3k F=1+3k
			{ 7, 7, 4, 4 }, // S (F): L=7-3k D=7-3k R=3+k U=3+k
			{ 6, 5, 5, 6 }, // S'(B): R=5-k D=1+3k L=1+3k U=5-k
			{ 7, 6, 4, 4 }, // M (L): U=7-3k B=8-k D=3+k F=3+k
			{ 6, 6, 4, 5 } // M'(R): F=5-k D=5-k B=3+k U=1+3k
	};
	private static final int[][][] dragBlocks = { { { 0, 0 }, { 3, 0 }, { 3, 1 }, { 0, 1 } },
			{ { 3, 0 }, { 3, 3 }, { 2, 3 }, { 2, 0 } }, { { 3, 3 }, { 0, 3 }, { 0, 2 }, { 3, 2 } },
			{ { 0, 3 }, { 0, 0 }, { 1, 0 }, { 1, 3 } },
			// center slices
			{ { 0, 1 }, { 3, 1 }, { 3, 2 }, { 0, 2 } }, { { 2, 0 }, { 2, 3 }, { 1, 3 }, { 1, 0 } } };
	private static final int[][] areaDirs = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
	private static final int[][] twistDirs = { { 1, 1, 1, 1, 1, -1 }, // U
			{ 1, 1, 1, 1, 1, -1 }, // D
			{ 1, -1, 1, -1, 1, 1 }, // F
			{ 1, -1, 1, -1, -1, 1 }, // B
			{ -1, 1, -1, 1, -1, -1 }, // L
			{ 1, -1, 1, -1, 1, 1 } // R
	};
	private final static int[][] eyeOrder = { { 1, 0, 0 }, { 0, 1, 0 }, { 1, 1, 0 }, { 1, 1, 1 }, { 1, 0, 1 }, { 1, 0, 2 } };
	private final static int[][][][] blockArray = new int[3][][][];
	private final static int[][] blockMode = { { 0, 2, 2 }, { 2, 1, 2 }, { 2, 2, 2 }, { 2, 2, 2 }, { 2, 2, 2 }, { 2, 2, 2 } };
	private final static int[][] drawOrder = { { 0, 1, 2 }, { 2, 1, 0 }, { 0, 2, 1 } };

	private static final double[][] border = { { 0.10, 0.10 }, { 0.90, 0.10 }, { 0.90, 0.90 }, { 0.10, 0.90 } };
	private static final int[][] factors = { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 1, 0 } };

	// directions of facelet cycling for all faces
	private static final int[] faceTwistDirs = { 1, 1, -1, -1, -1, -1 };

	public static int[] getPosfacetransform() {
		return posFaceTransform;
	}

	public static int[][] getPosfacelettransform() {
		return posFaceletTransform;
	}

	public static int[] getFacetwistdirs() {
		return faceTwistDirs;
	}

	public static int[][] getNearfaces() {
		return nearFaces;
	}

	public static double[][] getCornerpos() {
		return cornerPos;
	}

	public static double[][] getPlanenormals() {
		return planeNormals;
	}

	public static int[][] getPlanecorners() {
		return planeCorners;
	}

	public static int[] getMovemodes() {
		return moveModes;
	}

	public static int[] getMovecodes() {
		return moveCodes;
	}

	public static int[][] getOppositecorners() {
		return oppositeCorners;
	}

	public static int[][][] getRotcos() {
		return rotCos;
	}

	public static int[][][] getRotsin() {
		return rotSin;
	}

	public static int[][][] getRotvec() {
		return rotVec;
	}

	public static int[][][] getCubeblocks() {
		return cubeBlocks;
	}

	public static int[][][] getTopblocktable() {
		return topBlockTable;
	}

	public static int[][][] getMidblocktable() {
		return midBlockTable;
	}

	public static int[][] getTopblockfacedim() {
		return topBlockFaceDim;
	}

	public static int[][] getMidblockfacedim() {
		return midBlockFaceDim;
	}

	public static int[][] getBotblockfacedim() {
		return botBlockFaceDim;
	}

	public static int[] getCycleorder() {
		return cycleOrder;
	}

	public static int[] getCyclefactors() {
		return cycleFactors;
	}

	public static int[] getCycleoffsets() {
		return cycleOffsets;
	}

	public static int[][] getCyclelayersides() {
		return cycleLayerSides;
	}

	public static int[][] getCyclecenters() {
		return cycleCenters;
	}

	public static int[][][] getDragblocks() {
		return dragBlocks;
	}

	public static int[][] getAreadirs() {
		return areaDirs;
	}

	public static int[][] getTwistdirs() {
		return twistDirs;
	}

	public static int[][] getEyeOrder() {
		return eyeOrder;
	}

	public static int[][][][] getBlockArray() {
		return blockArray;
	}

	public static int[][] getBlockMode() {
		return blockMode;
	}

	public static int[][] getDrawOrder() {
		return drawOrder;
	}

	public static double[][] getBorder() {
		return border;
	}

	public static int[][] getFactors() {
		return factors;
	}

	public static int[] getRotsign() {
		return rotSign;
	}

}
