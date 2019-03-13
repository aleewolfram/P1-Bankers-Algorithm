import java.util.ArrayList;
import java.util.Arrays;

public class Bank {


    public static int COUNT = 5; //number of eventual customer threads
    public static int NUM_OF_RESOURCES = 3;

    public static int[] available = new int[NUM_OF_RESOURCES]; //current resources available
    public static int[][] allocated = new int[COUNT][NUM_OF_RESOURCES]; //how many currently held
    public static int[][] need = new int[COUNT][NUM_OF_RESOURCES];  //how many more needed
    public static int[][] maxDemand = new int[COUNT][NUM_OF_RESOURCES]; //how many total until completed

    public static int end = 5; //counter so that the process will end when all threads complete
    public static ArrayList<Integer> doneThreads = new ArrayList<Integer>(COUNT);


    public static boolean check(int custNum, int[] request, int[]availCopy){
        boolean safe = true;

        for (int i = 0; i < NUM_OF_RESOURCES; i++)
        {
            if (request[i] > need[custNum][i]) { //request greater than need
                safe = false;
            }
            if (safe && request[i] > availCopy[i]) {
                safe = false;
            }
        }

        return safe;
    }

    public static boolean isSafe(int[] avail, int[][] maxD, int[][] alloc)
    {

        int[][] needCopy = new int[COUNT][NUM_OF_RESOURCES];

        for (int i = 0; i < NUM_OF_RESOURCES; i++) {
            for (int j = 0; j < COUNT; j++) {
                needCopy[j][i] = maxD[j][i] - alloc[j][i];
            }
        }


        boolean[] finish = new boolean[COUNT];
        for(int j = 0; j < COUNT; j++)
        {
            finish[j] = false;
        }


        int[] safeSeq = new int[COUNT];


        int[] work = new int[NUM_OF_RESOURCES];
        for (int i = 0; i < NUM_OF_RESOURCES ; i++) {
            work[i] = avail[i];
        }

        int count = 0;
        int numCorrect = 0;
        while (count < COUNT)
        {
            boolean found = false;
            for (int c = 0; c < COUNT; c++)
            {
                if (finish[c] == false)
                {
                    for (int j = 0; j < NUM_OF_RESOURCES; j++) {
                        numCorrect++;
                        if (needCopy[c][j] > work[j] && check(c, needCopy[c], work)) {
                            break;
                        }
                    }

                    if (numCorrect == NUM_OF_RESOURCES)
                    {

                        for (int k = 0 ; k < NUM_OF_RESOURCES ; k++)
                            work[k] += alloc[c][k];


                        safeSeq[count++] = c;

                        finish[c] = true;

                        found = true;
                    }
                }
            }


            if (found == false)
            {
                return false;
            }
        }

        System.out.print("System is in safe state. Safe sequence is: ");
       for (int i = 0; i < COUNT ; i++)
            System.out.print(safeSeq[i] + " ");

        return true;
    }


    public static boolean requestResources(int custNum, int[] request)
    {


        boolean safe = isSafe(available, maxDemand, allocated) && check(custNum,request,available);



        if (safe) {
            for (int i = 0; i < NUM_OF_RESOURCES; i++) {
                available[i] -= request[i];
                allocated[custNum][i] += request[i];
                need[custNum][i] -= request[i];
                for (int j = 0; j < request[i]; j++) {
                    Customer customer = new Customer(custNum);
                    customer.start();
                }
            }
            int counter = 0;
            int[] holder = new int[NUM_OF_RESOURCES];
            for(int i = 0; i < NUM_OF_RESOURCES; i++)
            {
                holder[i] = allocated[custNum][i];
            }
            for (int i = 0; i < NUM_OF_RESOURCES; i++) {
                if (need[custNum][i] == 0) {
                    counter++;
                }
            }
            if(counter == NUM_OF_RESOURCES)
            {
                if (!doneThreads.contains(custNum))
                {
                    doneThreads.add(custNum);
                    releaseResources(custNum,holder);
                    inactivateCustomer(custNum);
                }



            }
            return true;
        }
        else
            return false;
    }

    public static int releaseResources(int custNum, int[] release)
    {

        for(int i = 0; i < NUM_OF_RESOURCES; i++)
        {
            available[i] += release[i];
            need[custNum][i] += release[i];
            allocated[custNum][i] -= release[i];
        }
        return 0;
    }

    public static void getState()
    {
// outputs available, allocation, maxDemand, and need matrices
        System.out.println("AVAILABLE: \n" + Arrays.toString(available) + "\nALLOCATED: \n" + Arrays.deepToString(allocated).replace("], ", "]\n") +
                "\nMAX DEMAND: \n" + Arrays.deepToString(maxDemand).replace("], ", "]\n") +
                "\nNEED: \n" + Arrays.deepToString(need).replace("], ", "]\n"));
    }

    public int[] getAllocated(int threadNum)
    {
        int[] holder = new int[NUM_OF_RESOURCES];
        for(int i = 0; i < NUM_OF_RESOURCES; i++)
        {
            holder[i] = allocated[threadNum][i];
        }
        return holder;
    }

    public static void inactivateCustomer(int threadNum)
    {
        for(int i = 0; i < NUM_OF_RESOURCES; i++)
        {
            maxDemand[threadNum][i] = 0;
            allocated[threadNum][i] = 0;
            need[threadNum][i] = 0;
        }
        end--;
    }
}
