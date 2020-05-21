import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


/*
 * FINAL PROJECT
 * 
 * TECHNICIAN DIPLOMA
 * 
 * LIOR RAHAMIM
 * 
 * 2016 
 */

public final class RubiksCube extends JFrame implements Runnable, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	// initial observer co-ordinate axes (view)
	private final double[] eye = { 0.0, 0.0 , -1.0 };
	private final double[] eyeX = { 1.0 , 0.0 , 0.0 }; // (sideways)
	public ArrayList<Integer> solution=new ArrayList<>();
	private final double[] eyeY = new double[3]; // (vertical)
	private double currentAngle; // edited angle of twisted layer
	private double originalAngle; // angle of twisted layer
	int stepNum=1;

	// state of buttons
	private int progressHeight = 6;

	public RubiksCube()
	{
		init();
	}
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable(){
	        @Override
	        public void run() {
	            new RubiksCube();
	        }
	    });
		
	}
	
	public void init() {
		// receive mouse events
		addMouseMotionListener(this);
		addMouseListener(this);
		// create animation thread
		animThread = new Thread(this);

		States.setScale(1);
		States.setEditable(true);
		States.setScrambled(false);
		
		// initialize cube colours 
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 9; j++)
				Physics.getCube()[i][j] = i;
		VectorActions.vNorm(VectorActions.vMul(eyeY, eye, eyeX)); // make eyeY
		
		Button scramble = new Button("SCRAMBLE CUBE");
		Button solve = new Button("FIND SOLUTION");
		Button single=new Button("SINGLE STEP");
		single.setEnabled(false);
		Button ten=new Button("TEN STEPS");
		ten.setEnabled(false);
	    JPanel pane = new JPanel(); // on north, contains the buttons
	    
	    JFrame solvefr=new JFrame("NEXT STEP - "+String.valueOf(stepNum)); // solution frame
	    JPanel solvepn=new JPanel();
	    JLabel solvest=new JLabel();
	    JScrollPane scroller = new JScrollPane(solvepn, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	    
	    solvefr.setSize(300,700);
	    solvepn.add(solvest);
	    solvefr.add(scroller);
	    
	    
		
		solve.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				States.setEditable(false);
				solution=FindSolve.solve(Physics.getCube());
				stepNum=1;
				solvefr.setVisible(true);
				solvefr.setTitle("NEXT STEP - 1");
				solvest.setText(updateSolvest(solution));
				States.setScrambled(false);////////////////
				if(!solution.isEmpty())
				{
					ten.setEnabled(true);
					single.setEnabled(true);
				}
				System.out.println("**********");
				/*while(!solution.isEmpty())
				{
					RubiksCube.twistLayers(Physics.getCube(), solution.get(0), 1, 0);
					System.out.print(solution.remove(0)+" "); ////////////////
					repaint();
				}*/
			}
		});
		scramble.addActionListener( new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	States.setScrambled(true);
				States.setEditable(true);
		    	moveCube(AlterCube.scramble());
		    	repaint();
		    	solvefr.setVisible(false);
	    		/*System.out.println("*************");
		    	for(int i=0;i<6;i++)
		    	{
		    		for(int j=0;j<9;j++)
		    		{
		    			System.out.print(Physics.getCube()[i][j]+" ");
		    		}
		    		System.out.println("");
		    	}*/
		    	
		    }
		});
		single.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!solution.isEmpty())
				{
					stepNum++;
					solvefr.setTitle("NEXT STEP - "+String.valueOf(stepNum));
					RubiksCube.twistLayers(Physics.getCube(), solution.get(0), 1, 0);
					solution.remove(0);
					repaint();
				}
				if(solution.isEmpty())
				{
					ten.setEnabled(false);
					single.setEnabled(false);
					solvefr.setTitle("THE CUBE IS SOLVED");
				}
			}
		});
		ten.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				for(int i=0;i<10;i++)
				{
					if(!solution.isEmpty())
					{
						stepNum++;
						solvefr.setTitle("NEXT STEP - "+String.valueOf(stepNum));
						RubiksCube.twistLayers(Physics.getCube(), solution.get(0), 1, 0);
						solution.remove(0);
						repaint();
					}
					if(solution.isEmpty())
					{
						ten.setEnabled(false);
						single.setEnabled(false);
						solvefr.setTitle("THE CUBE IS SOLVED");
					}
				}
			}
		});
		
		this.setLayout(new BorderLayout());
	    pane.setLayout(new GridLayout(2, 2));
		pane.add(solve);
		pane.add(scramble);
		pane.add(single);
		pane.add(ten);
		this.add(pane,BorderLayout.NORTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setSize(500,700);
		this.setVisible(true);
	}
	
	// convert number to colour
	public String numToCol(int n)
	{
		if(n==0)
			return "WHITE";
		if(n==1)
			return "YELLOW";
		if(n==2)
			return "ORANGE";
		if(n==3)
			return "RED";
		if(n==4)
			return "BLUE";
		if(n==5)
			return "GREEN";
		return "";
	}
	
	// a function to update solvest string commands
	public String updateSolvest(ArrayList<Integer> solution)
	{
		String solve="<html>";
		for(int i=0;i<solution.size();i++)
		{
			solve=solve+String.valueOf(i+1)+". ";
			solve=solve+"turn the "+numToCol(solution.get(i))+" side clockwise";
			solve=solve+"<br>";
			//solve=solve+System.getProperty("line.separator");
		}
		return solve;
	}
	
	// a function to perform series of moves
	public void moveCube(ArrayList<Integer> solution)
	{
		int single;// this is the next move
		// while there are still moves
		while(!solution.isEmpty())
		{
			single=solution.remove(0);
			//Physics.setTwistedLayer(single);
	    	twistLayers(Physics.getCube(), single, 1, 0);
	    	repaint();
		}
	}

	public void run(){
		
	}

	private Thread animThread = null; // thread to perform the animation

	// little cube dimensions
	private final int[][][] topBlocks = new int[6][][];
	private final int[][][] midBlocks = new int[6][][];
	private final int[][][] botBlocks = new int[6][][];

	private void splitCube(int layer) {
		for (int i = 0; i < 6; i++) { // for all faces
			topBlocks[i] = AssistingArrays.getTopblocktable()[AssistingArrays.getTopblockfacedim()[layer][i]];
			botBlocks[i] = AssistingArrays.getTopblocktable()[AssistingArrays.getBotblockfacedim()[layer][i]];
			midBlocks[i] = AssistingArrays.getMidblocktable()[AssistingArrays.getMidblockfacedim()[layer][i]];
		}

		States.setNatural(false);
	}

	public static void twistLayers(int[][] cube, int layer, int num, int mode) {
		switch (mode) {
		case 3:
			twistLayer(cube, layer ^ 1, num, false);
		case 2:
			twistLayer(cube, layer, 4 - num, false);
		case 1:
			twistLayer(cube, layer, 4 - num, true);
			break;
		case 5:
			twistLayer(cube, layer ^ 1, 4 - num, false);
			twistLayer(cube, layer, 4 - num, false);
			break;
		case 4:
			twistLayer(cube, layer ^ 1, num, false);
		default:
			twistLayer(cube, layer, 4 - num, false);
		}
	}

	private final static int[] twistBuffer = new int[12];

	public static void twistLayer(int[][] cube, int layer, int num, boolean middle) {
		if (!middle) { // twist top facelets
			for (int i = 0; i < 8; i++) // to buffer
				twistBuffer[(i + num * 2) % 8] = cube[layer][AssistingArrays.getCycleorder()[i]];
			for (int i = 0; i < 8; i++) // to cube
				cube[layer][AssistingArrays.getCycleorder()[i]] = twistBuffer[i];
		}
		int k = num * 3;
		for (int i = 0; i < 4; i++) { // twist side facelets
			int n = AssistingArrays.getNearfaces()[layer][i];
			int c = middle ? AssistingArrays.getCyclecenters()[layer][i]
					: AssistingArrays.getCyclelayersides()[layer][i];
			int factor = AssistingArrays.getCyclefactors()[c];
			int offset = AssistingArrays.getCycleoffsets()[c];
			for (int j = 0; j < 3; j++) {
				twistBuffer[k % 12] = cube[n][j * factor + offset];
				k++;
			}
		}
		k = 0; 
		for (int i = 0; i < 4; i++) {
			int n = AssistingArrays.getNearfaces()[layer][i];
			int c = middle ? AssistingArrays.getCyclecenters()[layer][i]
					: AssistingArrays.getCyclelayersides()[layer][i];
			int factor = AssistingArrays.getCyclefactors()[c];
			int offset = AssistingArrays.getCycleoffsets()[c];
			int j = 0; 
			while (j < 3) {
				cube[n][j * factor + offset] = twistBuffer[k];
				j++;
				k++;
			}
		}
	}

	// animation
	private Graphics graphics = null;
	private Image image = null;
	// window size is changeable
	private int width;
	private int height;
	// last position of mouse (for dragging the cube)
	private int lastX;
	private int lastY;
	// last position of mouse (when waiting for clear decision)
	private int lastDragX;
	private int lastDragY;
	// drag areas
	private int dragAreas;
	private final int[][] dragCornersX = new int[18][4];
	private final int[][] dragCornersY = new int[18][4];
	private final double[] dragDirsX = new double[18];
	private final double[] dragDirsY = new double[18];

	private int[] dragLayers = new int[18]; // which layers belongs to
											// dragCorners
	private int[] dragModes = new int[18]; // which layer modes dragCorners
	// current drag directions
	private double dragX;
	private double dragY;
	// temporary eye vectors for twisted sub-cube rotation
	private final double[] tempEye = new double[3];
	private final double[] tempEyeX = new double[3];
	private final double[] tempEyeY = new double[3];
	// temporary eye vectors for second twisted sub-cube rotation (antislice)
	private final double[] tempEye2 = new double[3];
	private final double[] tempEyeX2 = new double[3];
	private final double[] tempEyeY2 = new double[3];
	// temporary vectors to compute visibility in perspective projection
	private final double[] perspEye = new double[3];
	private final double[] perspEyeI = new double[3];
	private final double[] perspNormal = new double[3];
	// eye arrays to store various eyes for various modes
	private final double[][] eyeArray = new double[3][];
	private final double[][] eyeArrayX = new double[3][];
	private final double[][] eyeArrayY = new double[3][];

	public void paint(Graphics g) {
	    Graphics2D g2 = (Graphics2D)g;
	    RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_TEXT_ANTIALIASING,
	             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g2.setRenderingHints(rh);
	    
		Dimension size = getSize(); 
		if (image == null || size.width != width ) {
			width = size.width;
			height = size.height;
			image = createImage(width, height);
			graphics = image.getGraphics();
		}
		graphics.setColor(Color.white);
		graphics.setClip(0, 0, width, height);
		graphics.fillRect(0, 0, width, height);
		synchronized (animThread) {
			dragAreas = 0;
			if (States.isNatural()) // nothing twisted
				fixBlock(eye, eyeX, eyeY, AssistingArrays.getCubeblocks(), 3); // fill cube
			// drag areas
			else { // in twisted state
					// compute top observer
				double cosA = Math.cos(originalAngle + currentAngle);
				double sinA = Math.sin(originalAngle + currentAngle) * AssistingArrays.getRotsign()[Physics.getTwistedLayer()];
				for (int i = 0; i < 3; i++) {
					tempEye[i] = 0;
					tempEyeX[i] = 0;
					for (int j = 0; j < 3; j++) {
						int axis = Physics.getTwistedLayer() / 2;
						tempEye[i] += eye[j] * (AssistingArrays.getRotvec()[axis][i][j]
								+ AssistingArrays.getRotcos()[axis][i][j] * cosA
								+ AssistingArrays.getRotsin()[axis][i][j] * sinA);
						tempEyeX[i] += eyeX[j] * (AssistingArrays.getRotvec()[axis][i][j]
								+ AssistingArrays.getRotcos()[axis][i][j] * cosA
								+ AssistingArrays.getRotsin()[axis][i][j] * sinA);
					}
				}
				VectorActions.vMul(tempEyeY, tempEye, tempEyeX);
				// compute bottom anti-observer
				double cosB = Math.cos(originalAngle - currentAngle);
				double sinB = Math.sin(originalAngle - currentAngle) * AssistingArrays.getRotsign()[Physics.getTwistedLayer()];
				for (int i = 0; i < 3; i++) {
					tempEye2[i] = 0;
					tempEyeX2[i] = 0;
					for (int j = 0; j < 3; j++) {
						int axis = Physics.getTwistedLayer() / 2;
						tempEye2[i] += eye[j] * (AssistingArrays.getRotvec()[axis][i][j]
								+ AssistingArrays.getRotcos()[axis][i][j] * cosB
								+ AssistingArrays.getRotsin()[axis][i][j] * sinB);
						tempEyeX2[i] += eyeX[j] * (AssistingArrays.getRotvec()[axis][i][j]
								+ AssistingArrays.getRotcos()[axis][i][j] * cosB
								+ AssistingArrays.getRotsin()[axis][i][j] * sinB);
					}
				}
				VectorActions.vMul(tempEyeY2, tempEye2, tempEyeX2);
				eyeArray[0] = eye;
				eyeArrayX[0] = eyeX;
				eyeArrayY[0] = eyeY;
				eyeArray[1] = tempEye;
				eyeArrayX[1] = tempEyeX;
				eyeArrayY[1] = tempEyeY;
				eyeArray[2] = tempEye2;
				eyeArrayX[2] = tempEyeX2;
				eyeArrayY[2] = tempEyeY2;
				AssistingArrays.getBlockArray()[0] = topBlocks;
				AssistingArrays.getBlockArray()[1] = midBlocks;
				AssistingArrays.getBlockArray()[2] = botBlocks;
				// perspective corrections
				VectorActions.vSub(VectorActions.vScale(VectorActions.vCopy(perspEye, eye), 5.0 + States.getPersp()),
						VectorActions.vScale(
								VectorActions.vCopy(perspNormal, AssistingArrays.getPlanenormals()[Physics.getTwistedLayer()]),
								1.0 / 3.0));
				VectorActions.vSub(VectorActions.vScale(VectorActions.vCopy(perspEyeI, eye), 5.0 + States.getPersp()),
						VectorActions.vScale(
								VectorActions.vCopy(perspNormal, AssistingArrays.getPlanenormals()[Physics.getTwistedLayer() ^ 1]),
								1.0 / 3.0));
				double topProd = VectorActions.vProd(perspEye, AssistingArrays.getPlanenormals()[Physics.getTwistedLayer()]);
				double botProd = VectorActions.vProd(perspEyeI, AssistingArrays.getPlanenormals()[Physics.getTwistedLayer() ^ 1]);
				int orderMode;
				if (topProd < 0 && botProd > 0) // top facing away
					orderMode = 0;
				else if (topProd > 0 && botProd < 0) // bottom facing away: draw
														// it first
					orderMode = 1;
				else // both top and bottom layer facing away: draw them first
					orderMode = 2;
				fixBlock(
						eyeArray[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][0]]],
						eyeArrayX[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][0]]],
						eyeArrayY[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][0]]],
						AssistingArrays.getBlockArray()[AssistingArrays.getDrawOrder()[orderMode][0]],
						AssistingArrays.getBlockMode()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][0]]);
				fixBlock(
						eyeArray[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][1]]],
						eyeArrayX[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][1]]],
						eyeArrayY[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][1]]],
						AssistingArrays.getBlockArray()[AssistingArrays.getDrawOrder()[orderMode][1]],
						AssistingArrays.getBlockMode()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][1]]);
				fixBlock(
						eyeArray[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][2]]],
						eyeArrayX[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][2]]],
						eyeArrayY[AssistingArrays
								.getEyeOrder()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][2]]],
						AssistingArrays.getBlockArray()[AssistingArrays.getDrawOrder()[orderMode][2]],
						AssistingArrays.getBlockMode()[Physics.getTwistedMode()][AssistingArrays.getDrawOrder()[orderMode][2]]);
			}
		}
		g.drawImage(image, 0, 0, this);
	} 

	public void update(Graphics g) {
		paint(g);
	}

	// polygon co-ordinates to fill (cube faces or facelets)
	private final int[] fillX = new int[4];
	private final int[] fillY = new int[4];
	// projected vertex co-ordinates (to screen)
	private final double[] coordsX = new double[8];
	
	private final double[] coordsY = new double[8];
	private final double[][] cooX = new double[6][4];
	private final double[][] cooY = new double[6][4];
	private final double[] tempNormal = new double[3];

	private void fixBlock(double[] eye, double[] eyeX, double[] eyeY, int[][][] blocks, int mode) {
		for (int i = 0; i < 8; i++) {
			double min = Math.min(height-100, width-50); // not getting out of the window
			double x = min / 3.7 * VectorActions.vProd(AssistingArrays.getCornerpos()[i], eyeX) * States.getScale();
			double y = min / 3.7 * VectorActions.vProd(AssistingArrays.getCornerpos()[i], eyeY) * States.getScale();
			double z = min / (5.0 + States.getPersp()) * VectorActions.vProd(AssistingArrays.getCornerpos()[i], eye)
					* States.getScale();
			x = x / (1 - z / min); // perspective transformation
			y = y / (1 - z / min); // perspective transformation
			coordsX[i] = width / 2.0 + x;
			coordsY[i] = (height - progressHeight) / 2.0 * States.getScale() - y;
		}
		// setup corner co-ordinates for all faces
		for (int i = 0; i < 6; i++) { // all faces
			for (int j = 0; j < 4; j++) { // all face corners
				cooX[i][j] = coordsX[AssistingArrays.getPlanecorners()[i][j]];
				cooY[i][j] = coordsY[AssistingArrays.getPlanecorners()[i][j]];
			}
		}
		
		// find and draw black inner faces
		for (int i = 0; i < 6; i++) { // all faces
			int sideW = blocks[i][0][1] - blocks[i][0][0];
			int sideH = blocks[i][1][1] - blocks[i][1][0];
			if (sideW <= 0 || sideH <= 0) { // this face is inner and only black
				for (int j = 0; j < 4; j++) { // for all corners
					int k = AssistingArrays.getOppositecorners()[i][j];
					fillX[j] = (int) (cooX[i][j] + (cooX[i ^ 1][k] - cooX[i][j]) * 2.0 / 3.0);
					fillY[j] = (int) (cooY[i][j] + (cooY[i ^ 1][k] - cooY[i][j]) * 2.0 / 3.0);
					if (States.isMirrored())
						fillX[j] = width - fillX[j];
				}
				graphics.setColor(Color.black);
				graphics.fillPolygon(fillX, fillY, 4);
			} else {
				// draw black background 
				for (int j = 0; j < 4; j++) // corner co-ordinates
					getCorners(i, j, fillX, fillY, blocks[i][0][AssistingArrays.getFactors()[j][0]],
							blocks[i][1][AssistingArrays.getFactors()[j][1]], States.isMirrored());
				graphics.setColor(Color.black);
				graphics.fillPolygon(fillX, fillY, 4);
			}
		}
		// draw only visible faces and get dragging regions
		for (int i = 0; i < 6; i++) { // all faces
			VectorActions.vSub(VectorActions.vScale(VectorActions.vCopy(perspEye, eye), 5.0 + States.getPersp()),
					AssistingArrays.getPlanenormals()[i]); // perspective
			// correction
			if (VectorActions.vProd(perspEye, AssistingArrays.getPlanenormals()[i]) > 0) { // draw the faces toward perspective
				int sideW = blocks[i][0][1] - blocks[i][0][0];
				int sideH = blocks[i][1][1] - blocks[i][1][0];
				if (sideW > 0 && sideH > 0) { // this side is not only black
					// draw colored facelets
					for (int n = 0, p = blocks[i][1][0]; n < sideH; n++, p++) {
						for (int o = 0, q = blocks[i][0][0]; o < sideW; o++, q++) {
							for (int j = 0; j < 4; j++)
								getCorners(i, j, fillX, fillY, q + AssistingArrays.getBorder()[j][0],
										p + AssistingArrays.getBorder()[j][1], States.isMirrored());
							graphics.setColor(ColorsSetup.getColors()[Physics.getCube()[i][p * 3 + q]].darker());
							graphics.drawPolygon(fillX, fillY, 4);
							graphics.setColor(ColorsSetup.getColors()[Physics.getCube()[i][p * 3 + q]]);
							graphics.fillPolygon(fillX, fillY, 4);
						}
					}
				}
				if (!States.isEditable() || States.isAnimating()) // no twists as animating
					continue;
				// horizontal and vertical directions of face - interpolated
				double dxh = (cooX[i][1] - cooX[i][0] + cooX[i][2] - cooX[i][3]) / 6.0;
				double dyh = (cooX[i][3] - cooX[i][0] + cooX[i][2] - cooX[i][1]) / 6.0;
				double dxv = (cooY[i][1] - cooY[i][0] + cooY[i][2] - cooY[i][3]) / 6.0;
				double dyv = (cooY[i][3] - cooY[i][0] + cooY[i][2] - cooY[i][1]) / 6.0;
				if (mode == 3) { // just the normal cube
					for (int j = 0; j < 6; j++) { // 4 areas 3x1 per face + 2
													// center slices
						for (int k = 0; k < 4; k++) // 4 points per area
							getCorners(i, k, dragCornersX[dragAreas], dragCornersY[dragAreas],
									AssistingArrays.getDragblocks()[j][k][0], AssistingArrays.getDragblocks()[j][k][1],
									false);
						dragDirsX[dragAreas] = (dxh * AssistingArrays.getAreadirs()[j][0]
								+ dxv * AssistingArrays.getAreadirs()[j][1]) * AssistingArrays.getTwistdirs()[i][j];
						dragDirsY[dragAreas] = (dyh * AssistingArrays.getAreadirs()[j][0]
								+ dyv * AssistingArrays.getAreadirs()[j][1]) * AssistingArrays.getTwistdirs()[i][j];
						dragLayers[dragAreas] = AssistingArrays.getNearfaces()[i][j % 4];
						if (j >= 4)
							dragLayers[dragAreas] &= ~1;
						dragModes[dragAreas] = j / 4;
						dragAreas++;
						if (dragAreas == 18)
							break;
					}
				} else if (mode == 0) { // twistable top layer
					if (i != Physics.getTwistedLayer() && sideW > 0 && sideH > 0) { // only
																		// 3x1
																		// faces
						int j = sideW == 3 ? (blocks[i][1][0] == 0 ? 0 : 2) : (blocks[i][0][0] == 0 ? 3 : 1);
						for (int k = 0; k < 4; k++)
							getCorners(i, k, dragCornersX[dragAreas], dragCornersY[dragAreas],
									AssistingArrays.getDragblocks()[j][k][0], AssistingArrays.getDragblocks()[j][k][1],
									false);
						dragDirsX[dragAreas] = (dxh * AssistingArrays.getAreadirs()[j][0]
								+ dxv * AssistingArrays.getAreadirs()[j][1]) * AssistingArrays.getTwistdirs()[i][j];
						dragDirsY[dragAreas] = (dyh * AssistingArrays.getAreadirs()[j][0]
								+ dyv * AssistingArrays.getAreadirs()[j][1]) * AssistingArrays.getTwistdirs()[i][j];
						dragLayers[dragAreas] = Physics.getTwistedLayer();
						dragModes[dragAreas] = 0;
						dragAreas++;
					}
				} else if (mode == 1) { // twistable center layer
					if (i != Physics.getTwistedLayer() && sideW > 0 && sideH > 0) { // only
																		// 3x1
																		// faces
						int j = sideW == 3 ? 4 : 5;
						for (int k = 0; k < 4; k++)
							getCorners(i, k, dragCornersX[dragAreas], dragCornersY[dragAreas],
									AssistingArrays.getDragblocks()[j][k][0], AssistingArrays.getDragblocks()[j][k][1],
									false);
						dragDirsX[dragAreas] = (dxh * AssistingArrays.getAreadirs()[j][0]
								+ dxv * AssistingArrays.getAreadirs()[j][1]) * AssistingArrays.getTwistdirs()[i][j];
						dragDirsY[dragAreas] = (dyh * AssistingArrays.getAreadirs()[j][0]
								+ dyv * AssistingArrays.getAreadirs()[j][1]) * AssistingArrays.getTwistdirs()[i][j];
						dragLayers[dragAreas] = Physics.getTwistedLayer();
						dragModes[dragAreas] = 1;
						dragAreas++;
					}
				}
			}
		}
	}

	private void getCorners(int face, int corner, int[] cornersX, int[] cornersY, double factor1, double factor2,
			boolean mirror) {
		factor1 /= 3.0;
		factor2 /= 3.0;
		double x1 = cooX[face][0] + (cooX[face][1] - cooX[face][0]) * factor1;
		double y1 = cooY[face][0] + (cooY[face][1] - cooY[face][0]) * factor1;
		double x2 = cooX[face][3] + (cooX[face][2] - cooX[face][3]) * factor1;
		double y2 = cooY[face][3] + (cooY[face][2] - cooY[face][3]) * factor1;
		cornersX[corner] = (int) (0.5 + x1 + (x2 - x1) * factor2);
		cornersY[corner] = (int) (0.5 + y1 + (y2 - y1) * factor2);
		//if (mirror)
			//cornersX[corner] = width - cornersX[corner];
	}

	// must have mouse functions due to implementation 

	public void mousePressed(MouseEvent e) {
		lastDragX = lastX = e.getX();
		lastDragY = lastY = e.getY();
		States.setToTwist(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (States.isTwisting() && !States.isSpinning()) {
			States.setTwisting(false);
			originalAngle += currentAngle;
			currentAngle = 0.0;
			double angle = originalAngle;
			while (angle < 0.0)
				angle += 32.0 * Math.PI;
			int num = (int) (angle * 8.0 / Math.PI) % 16; // 2pi ~ 16
			if (num % 4 == 0 || num % 4 == 3) { // close enough to a corner
				num = (num + 1) / 4; // 2pi ~ 4
				if (AssistingArrays.getFacetwistdirs()[Physics.getTwistedLayer()] > 0)
					num = (4 - num) % 4;
				originalAngle = 0;
				States.setNatural(true); // the cube in the natural state
				twistLayers(Physics.getCube(), Physics.getTwistedLayer(), num, Physics.getTwistedMode()); // rotate the
																	// facelets
			}
			repaint();
		}
		
		// check if cube solved once mouse released
		if(AlterCube.isSolved(Physics.getCube())&&States.isScrambled())
		{
			States.setScrambled(false);
			JFrame congrats=new JFrame("CONGRATULATIONS");
			JLabel label=new JLabel("YOU BEAT THE CUBE!");
			congrats.add(label);
			congrats.setSize(200,100);
			congrats.setLocationRelativeTo(null);
			congrats.setVisible(true);
		}
		
		if(AlterCube.isSolved(Physics.getCube()))
			States.setEditable(true);
	}

	private final double[] eyeD = new double[3];

	public void mouseDragged(MouseEvent e) {
		int x = States.isMirrored() ? width - e.getX() : e.getX();
		int y = e.getY();
		int dx = x - lastX;
		int dy = y - lastY;
		if (States.isEditable() && States.isToTwist() && !States.isTwisting() && !States.isAnimating()) { // no twist
			// but we can
			lastDragX = x;
			lastDragY = y;
			for (int i = 0; i < dragAreas; i++) { // check if inside a drag area
				double d1 = dragCornersX[i][0];
				double x1 = dragCornersX[i][1] - d1;
				double y1 = dragCornersX[i][3] - d1;
				double d2 = dragCornersY[i][0];
				double x2 = dragCornersY[i][1] - d2;
				double y2 = dragCornersY[i][3] - d2;
				double a = (y2 * (lastX - d1) - y1 * (lastY - d2)) / (x1 * y2 - y1 * x2);
				double b = (-x2 * (lastX - d1) + x1 * (lastY - d2)) / (x1 * y2 - y1 * x2);
				if (a > 0 && a < 1 && b > 0 && b < 1) { // we are in
					if (dx * dx + dy * dy < 144) // delay the decision about
													// twisting
						return;
					dragX = dragDirsX[i];
					dragY = dragDirsY[i];
					double d = Math.abs(dragX * dx + dragY * dy)
							/ Math.sqrt((dragX * dragX + dragY * dragY) * (dx * dx + dy * dy));
					if (d > 0.75) {
						States.setTwisting(true);
						Physics.setTwistedLayer(dragLayers[i]);
						Physics.setTwistedMode(dragModes[i]);
						break;
					}
				}
			}
			States.setToTwist(false);
			lastX = lastDragX;
			lastY = lastDragY;
		}
		dx = x - lastX;
		dy = y - lastY;
		if (!States.isTwisting() || States.isAnimating()) { // cube rotation
			VectorActions.vNorm(VectorActions.vAdd(eye, VectorActions.vScale(VectorActions.vCopy(eyeD, eyeX), dx * -0.016)));
			VectorActions.vNorm(VectorActions.vMul(eyeX, eyeY, eye));
			VectorActions.vNorm(VectorActions.vAdd(eye, VectorActions.vScale(VectorActions.vCopy(eyeD, eyeY), dy * 0.016)));
			VectorActions.vNorm(VectorActions.vMul(eyeY, eye, eyeX));
			lastX = x;
			lastY = y;
		} else {
			splitCube(Physics.getTwistedLayer());
			currentAngle = 0.03 * (dragX * dx + dragY * dy) / Math.sqrt(dragX * dragX + dragY * dragY); 
		}
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
