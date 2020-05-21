import java.util.ArrayList;

public class AlterCube {
	
	// this function returns a random set of moves
	public static ArrayList<Integer> scramble()
	{
		// the set of moves
		ArrayList<Integer> moves=new ArrayList<>();
		// what side to spin
		int side;
		// how many times to spin this side
		int howMany;
		
		int times = (int) ((Math.random()*10)+20);
		
		// the set of moves will be the size of times
		for(int i=0;i<times;i++)
		{
			side = (int) (Math.random()*6);
			howMany = (int) ((Math.random()*3)+1);
			for(int j=0;j<howMany;j++)
			{
				moves.add(side);
			}
		}
		return moves;
		
	}

	// returns is the cube solved
	public static boolean isSolved(int[][] cube)
	{
		for(int i=0;i<cube.length;i++)
		{
			for(int j=1;j<cube[i].length;j++)
			{
				if(cube[i][j]!=cube[i][0])
					return false;
			}
		}
		return true;
	}
	
}
