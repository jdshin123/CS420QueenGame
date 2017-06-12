package cs420Project2;
import java.util.Random;
import java.util.Scanner;
/*
 * Joshua Shin
 * N Queen project2 solved using Hill climbing
 * and genetic algorithms
 */
public class NthQueen 
{
	private int [][] chessBoard;
	private int[] col;
	private int nonAttackNum = 0;
	private int num1;
	private final Random random = new Random();
	/*
	 * main method that runs Hill climing or genetic depending on input a or b
	 */
	public static void main(String[]args)
	{
		String choice ="";


        while (!choice.equals("x") && !choice.equals("X")) {
        	System.out.println("please choose either one");
            System.out.print("(a)Hill-Climbing Algorithm, (b)Genetic Algorithm: ");
            Scanner keyboard = new Scanner(System.in);
            choice = keyboard.nextLine();

            if (choice.equals("a") || choice.equals("A")) {
                HillClimbing.run();
            } else if (choice.equals("b") || choice.equals("B")) {
                Genetic.run();
            }

            System.out.println("Enter x to exit, Else continue: ");
            keyboard = new Scanner(System.in);
            choice = keyboard.nextLine();
        }
    }

		
	
	
	/*
	 * default constructor
	 */
	public NthQueen()
	{
		chessBoard = new int[8][8];
		col = new int[8];
		num1 = 8;
		
	}
	
	/*
	 * constructor with input int
	 */
	public NthQueen(int n)
	{
		chessBoard = new int [n][n];
		col = new int [n];
		num1 = n;
		
	}
	
	/*
	 * constructor with input int [][]
	 */
	public NthQueen(int []n)
	{
		num1 = n.length;
		chessBoard = new int[num1][num1];
		col = n.clone();
		boardGenerator(n);
	}
	
	/*
	 * generates the chess board with specific input []n
	 */
	public void boardGenerator(int[] n)
	{	
		
		col = n;
		for(int i = 0; i<num1;i++)
		{
			int k = n[i];
			int j = i;
			chessBoard[k][j] = 1;
		}
		nonAttackNum = countNonAttacks();
	}
	/*
	 * generates a random board 
	 */
	public void boardGenerator()
	{
		clearBoard();
		int[] c = new int[num1];
		for(int nQ = 0;nQ <num1;nQ++)
		{
			int r = random.nextInt(num1); //0 to 8
			int rj = random.nextInt(num1);
			if(c[rj]==0)
			{
				chessBoard[r][rj] = 1;
				col[rj] = r;
				++c[rj];
			}
			else
				--nQ;
		}
		nonAttackNum= countNonAttacks();
	}
	
	/*
	 * Getters and setter
	 * gets the value of non attack numbers
	 */
	public int getNonAttackNumber()
	{
		return nonAttackNum;
	}
	
	public int[] getColumn()
	{
		return col;
	}
	
	public void setColumn(int[] c)
	{
		boardGenerator(c);
	}
	
	/*
	 * method that clears the board
	 */
	public void clearBoard()
	{
		for(int i = 0; i<num1;++i)
		{
			col[i] = 0;
			for(int j = 0; j<num1;++j)
			{
				chessBoard[i][j] = 0;
			}
		}
		nonAttackNum = 0;
	}
	public void printBoard()
	{
		StringBuilder s = new StringBuilder();
		for(int i=0;i<num1;i++)
		{
			for(int j =0; j<num1;j++)
			{
				s.append(chessBoard[i][j]);
				s.append(" ");
			}
			s.append("\n");
			
		}
		System.out.println(s.toString());
	}
	/*
	 * a counter that keeps track of lines of attack
	 */
	private int countLineAttacks(int[] line)
	{
		int attackNum = 0;
		for(int elm:line)
		{
			if(elm>1){
				attackNum += elm;
			}
		}
		return attackNum;
	}
	/*
	 * counter to get the number of attacks
	 */
	public int countAttacks()
	{
		int[] row = new int[num1];
		int[] coll = new int [num1];
		int[] d1 = new int[2*num1-1];
		int[] d2= new int[2*num1-1];
		for(int i = 0; i <num1;++i)
		{
			for(int j =0; j< num1;++j)
			{
				if(chessBoard[i][j]!= 0)
				{
					++row[i];
					++coll[j];
					++d1[i+j];
					++d2[num1-1+j-i];
				}
			}
		}
		int numOfAttacks = countLineAttacks(row);
		numOfAttacks += countLineAttacks(coll);
		numOfAttacks += countLineAttacks(d1);
		numOfAttacks += countLineAttacks(d2);
		return numOfAttacks;	
	}
	/*
	 * gets the number of non attack moves
	 */
	public int countNonAttacks() {
        int numOfNonAttacks = 0;
        for (int j1 = 0; j1 < num1; ++j1) {
            int numOfAttacks = -1;
            int i1 = col[j1];
            for (int j2 = 0; j2 < num1; ++j2 ) {
                int i2 = col[j2];

                if ((i1 == i2) || (i1 + j1 == i2 + j2) || (i1 - j1 == i2 - j2))
                    ++numOfAttacks;
            }

            if (numOfAttacks == 0)
                ++numOfNonAttacks;
        }

        return numOfNonAttacks;
    }

	/*
	 * returns true if goal state is met
	 */
	public boolean goalState() {
        return (countNonAttacks() == num1);
    }

	
	
	
}
