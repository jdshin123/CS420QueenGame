package cs420Project2;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * Joshua Shin
 * 
 * Genetic algorthim
 */
public class Genetic {


    private int n1;
    private List<NthQueen> pop;
    private int[] popChance;
    private int popSize;

    private Random RAND = new Random();

    private boolean isBatchTest = false;
/*
 * constructor that passes 2 int values
 */
    public Genetic(int n, int initPopSize) {
        n1 = n;
        pop = new ArrayList<>();
        popChance = new int[initPopSize];
        popSize = initPopSize;

        //init pop
        for (int i = 0; i < initPopSize; ++i) {
            NthQueen child = new NthQueen(n);
            child.boardGenerator();

            pop.add(child);
        }
    }
/*
 * boolean method to check if we are running batch test
 * or single test
 */
    public void setBatchTest(boolean isBatchTest) {
        this.isBatchTest = isBatchTest;
    }
/*
 * comparator method that compares different queen attack
 * values.
 */
    class FitnessComparator implements Comparator<NthQueen> {
        @Override
        public int compare(NthQueen q1, NthQueen q2) {
            return q1.getNonAttackNumber() - q2.getNonAttackNumber();
        }
    }

/*
 * fitness method that gives the successful reproductive
 * cycles.
 */
    private void Fitness() {
        Collections.sort(pop, new FitnessComparator());

        int sum = 0;
        int i = 0;
        for (NthQueen child : pop) {
            sum += child.getNonAttackNumber() + 1;
            popChance[i] = sum;
            ++i;
        }
    }
/*
 * method that randomly selects from the list of queens
 */
    private List<NthQueen> randomSelect() {
        int sum = popChance[popSize-1];

        int parent1 = 0;
        int rand = RAND.nextInt(sum);
        for (; parent1 < popSize; ++parent1)
            if (rand < popChance[parent1])
                break;

        int parent2 = parent1;
        while (parent2 == parent1) {
            rand = RAND.nextInt(sum);
            for (parent2 = 0; parent2 < popSize; ++parent2)
                if (rand < popChance[parent2])
                    break;
        }

        List<NthQueen> parents = new ArrayList<>();
        parents.add(pop.get(parent1));
        parents.add(pop.get(parent2));

        return parents;
    }
/*
 * method that reproduce the next move from the existing queen
 */
    private List<NthQueen> reproduce(List<NthQueen> parents) {
        int[] dna1 = parents.get(0).getColumn().clone();
        int[] dna2 = parents.get(1).getColumn().clone();

        int splice = RAND.nextInt(n1);
        int start, end;
        if (RAND.nextBoolean()) {
            start = 0;
            end = splice + 1;
        } else {
            start = splice;
            end = n1;

        }

        for (int i = start; i < end; ++i) {
            int temp = dna1[i];
            dna1[i] = dna2[i];
            dna2[i] = temp;
        }

        List<NthQueen> children = new ArrayList<>();
        children.add(new NthQueen(dna1));
        children.add(new NthQueen(dna2));

        return children;
    }
/*
 * method that mutates the queen using random
 */
    private NthQueen mutate(NthQueen child, int mutationRate) {
        if (RAND.nextInt(mutationRate) < 1) {
            int[] dna = child.getColumn().clone();
            dna[RAND.nextInt(n1)] = RAND.nextInt(n1);
            child.setColumn(dna);
        }

        return child;
    }


    public int breed(int maxGeneration) {
        int generation = 0;

        int maxNonAttacks = 0;
        while (generation < maxGeneration) {
            Fitness();

            List<NthQueen> newPopulation = new ArrayList<>();

            for (int pair = 0; pair < popSize / 2; ++pair) {
                List<NthQueen> parents = randomSelect();

                List<NthQueen> children = reproduce(parents);

                newPopulation.addAll(children);
            }

            for (NthQueen child : newPopulation) {
                child = mutate(child, 10);

                if (child.getNonAttackNumber() > maxNonAttacks) {
                    maxNonAttacks = child.getNonAttackNumber();
                }

                // success
                if (child.goalState()) {
                    if (!isBatchTest)
                        child.printBoard();
                    return generation;
                }
            }

            pop = newPopulation;
            popSize = newPopulation.size();
            popChance = new int[popSize];

            ++generation;
            if (!isBatchTest && generation % 10000 == 0)
                System.out.println(generation + " Generation: current non-attack queens = " + maxNonAttacks);
        }

        return generation;
    }

    public static void run() 
    {
        int maxGeneration = 10000;
        int maxRound = 2;

        String choice;
        System.out.println("(Genetic)Select mode: ");
        System.out.print("(Genetic) (a)One run, (b)Batch run: ");
        Scanner keyboard = new Scanner(System.in);
        choice = keyboard.nextLine();
        if (choice.equals("a") || choice.equals("A")) {
            while (!choice.equals("x") && !choice.equals("X")) {
                System.out.println(" Number of Queens: ");

                keyboard = new Scanner(System.in);
                int n = Integer.valueOf(keyboard.nextLine());

                System.out.println("Size of population (even number): ");

                keyboard = new Scanner(System.in);
                int populationSize = Integer.valueOf(keyboard.nextLine());

                Genetic ga = new Genetic(n, populationSize);
                int generation = ga.breed(maxGeneration);

                if (generation < maxGeneration)
                    System.out.printf("Goal reached at %d generation.\n", generation);
                else
                    System.out.println("no goal found.");

                System.out.println(" type X to exit Genetic, else continue...");
                keyboard = new Scanner(System.in);
                choice = keyboard.nextLine();
            }
        } else if (choice.equals("b") || choice.equals("B")) {
            System.out.println("Number of Queens: ");
            keyboard = new Scanner(System.in);
            int n = Integer.valueOf(keyboard.nextLine());

            for (; n >= 4; --n) {
                long totalGeneration = 0;
                System.out.println("Number of queens: " + n);
                for (int round = 0; round < maxRound; ++round) {
                    Genetic ga = new Genetic(n, 4);
                    ga.setBatchTest(true);

                    int generation = ga.breed(maxGeneration);

                    if (generation < maxGeneration)
                        System.out.printf("Goal reached at %d generation(s).\n", generation);
                    else
                        System.out.println("Couldn't find goal.");

                    totalGeneration += generation;
                }

                System.out.println("number of queens = " + n + ", round = " + maxRound
                        + ", average generation = " + totalGeneration / maxRound);

                maxRound *= 2;
            }
        }

    }

}