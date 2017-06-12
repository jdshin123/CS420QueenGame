package cs420Project2;

import java.util.Scanner;

/*
 *Joshua Shin
 *Hill Climbing Algorithm 
 */
public class HillClimbing 
{
	private int num1;
	private NthQueen prob;
	private boolean test = false;
	
	/*
	 * constructor
	 */
	public HillClimbing(int n)
	{
		num1 = n;
		prob = new NthQueen(n);
		prob.boardGenerator();
	}
	
	/*
	 * method to check if we are running
	 * a batch test
	 */
	public void setBatchTest(boolean bt)
	{
		this.test = bt;
	}
	
	public boolean solve()
	{
		boolean nextStep = true;
		int currentNonAttack = 0;
		NthQueen currentState = null;while (!prob.goalState() && nextStep)
		{
            nextStep = false;
            int[] columnMark = prob.getColumn();

            for (int j = 0; j < num1; ++j) {
                for (int i = 0; i < num1; ++i) {
                    if (i != columnMark[j]) {
                        int[] possibleMove = columnMark.clone();
                        possibleMove[j] = i;

                        NthQueen possibleState = new NthQueen(possibleMove);

                        int possibleNonAttacks = possibleState.countNonAttacks();

                        if (possibleNonAttacks> currentNonAttack) {
                            currentNonAttack = possibleNonAttacks;
                            currentState = possibleState;

                            nextStep = true;
                        }
                    }
                }
            }

            prob = currentState;
        }

        if (!test)
            prob.printBoard();

        if (prob.goalState())
            return true;
        else
            return false;

		
		
	}
	public static void run() {
		// TODO Auto-generated method stub
		String choice;
		System.out.println("(Hill-Climbing) Select mode: ");
        System.out.print("(Hill-Climbing) (a)One run, (b)Batch run: ");
        Scanner keyboard = new Scanner(System.in);
        choice = keyboard.nextLine();
        if (choice.equals("a") || choice.equals("A")) {
            while (!choice.equals("x") && !choice.equals("X")) {
                System.out.println("(Hill-CLimbing)Please specify the number of queens:");

                keyboard = new Scanner(System.in);
                int n = Integer.valueOf(keyboard.nextLine());

                HillClimbing hc = new HillClimbing(n);
                if (hc.solve())
                    System.out.println("Problem solved");
                else
                    System.out.println("Failed to solve problem, try again");

                System.out.println("(Hill-Climbing)X to exit, else continue...");
                keyboard = new Scanner(System.in);
                choice = keyboard.nextLine();
            }

        } else if (choice.equals("b") || choice.equals("B")) {
            System.out.println("(Hill-Climbing)Please specify the number of queens:");

            keyboard = new Scanner(System.in);
            int n = Integer.valueOf(keyboard.nextLine());

            int numOfSolved = 0;
            for (int i = 0; i < 100; ++i) {
                HillClimbing hc = new HillClimbing(n);
                hc.setBatchTest(true);
                if (hc.solve())
                    ++numOfSolved;
            }

            System.out.println(numOfSolved + " out of 100 solved.");
        }
    }

	}


