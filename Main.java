import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main extends Bank {

    public static void main(String[] args) {
        java.util.Random rand = new Random();
        int[] alloc = {10, 5, 7};
        Scanner S = new Scanner(System.in);
        int randomCustNum, custNum;
        int[] requestResource = new int[NUM_OF_RESOURCES];
        int[] needsRow = new int[NUM_OF_RESOURCES];

        try (BufferedReader br = new BufferedReader(new FileReader("Infile.txt"))) {
            String line;
            int customerLineNum = 0;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                for (int i = 0; i < tokens.length; i++) //going through token
                {
                    if (i < 3) {
                        allocated[customerLineNum][i] = Integer.parseInt(tokens[i]);
                    }
                    if (i >= 3) {
                        maxDemand[customerLineNum][i - 3] = Integer.parseInt(tokens[i]);
                    }
                }
                customerLineNum++;
            }

            available = alloc;
            for (int i = 0; i < NUM_OF_RESOURCES; i++) {

                for (int j = 0; j < COUNT; j++) {
                    need[j][i] = maxDemand[j][i] - allocated[j][i];
                }
            }

            System.out.println("Please type 'S' for a simulation state or 'I' for an Interactive state ...");
            String state = S.nextLine().toLowerCase();

            if(state.equals("i")) {
                while (end > 0) {

                    System.out.println("Enter * to get state of system, OR Enter <RQ | RL> <customer number> <resource #0> <#1> <#2> ...");
                    String entry;
                    entry = S.nextLine();

                    if (entry.equals("*")) {
                        getState();
                    } else {
                        String[] info = entry.split(" ");
                        for (int i = 0; i < info.length; i++) {
                            System.out.print(info[i] + " ");
                        }
                        String choice = info[0];
                        custNum = Integer.parseInt(info[1]);
                        for (int i = 0; i < NUM_OF_RESOURCES; i++)
                        {
                            requestResource[i] = Integer.parseInt(info[i+2]);
                        }

                        if (choice.equals("RQ")) {
                            System.out.print("#P" + custNum + " Requested: " + Arrays.toString(requestResource) + " Needs: " + Arrays.toString(needsRow) + " Available: " + Arrays.toString(available));
                            for (int i = 0; i < NUM_OF_RESOURCES; i++) {
                                needsRow[i] = need[custNum][i];
                            }
                            boolean safe = requestResources(custNum, requestResource);
                            if (safe == true) {
                                System.out.println(" ---> APPROVED!");
                            }
                            try
                            {
                                int sleep = rand.nextInt(5) + 1;
                                Thread.sleep(sleep*100);
                            }
                            catch (InterruptedException ex)
                            {
                                System.err.println("InterruptedException!");
                            }
                            if (safe == false) {
                                System.out.println(" DENIED");
                            }
                        }
                        if (choice.equals("RL")) {
                            releaseResources(custNum, requestResource);
                        }
                    }

                }
                System.out.println("You have finished allocating for all customers! Good job!");
            }

            if(state.equals("s"))
            {
                getState();
               // boolean nonzero = false;
                while(end > 0)
                {
                    randomCustNum = rand.nextInt(5);
                    while(doneThreads.contains(randomCustNum))
                    {
                        randomCustNum = rand.nextInt(5);
                    }
                        for (int i = 0; i < NUM_OF_RESOURCES; i++) {
                            requestResource[i] = rand.nextInt(3);
                            needsRow[i] = need[randomCustNum][i];
                        }


                    System.out.print("#P" + randomCustNum + " Requested: " + Arrays.toString(requestResource) + " Needs: " + Arrays.toString(needsRow) + " Available: " + Arrays.toString(available));
                    boolean safe = requestResources(randomCustNum, requestResource);
                    if (safe == true) {
                        System.out.println(" ---> APPROVED!");
                        getState();
                    }
                    try
                    {
                        int sleep = rand.nextInt(5) + 1;
                        Thread.sleep(sleep*100);
                    }
                    catch (InterruptedException ex)
                    {
                        System.err.println("InterruptedException!");
                    }
                    if (safe == false)
                    {
                        System.out.println(" DENIED");
                    }
                }
                System.out.println("System has finished allocating for all customers! Yay!");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

