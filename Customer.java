import java.util.Random;

class Customer implements Runnable {

    private Thread t;
    private String threadName = "default";
    private int numOfResources;
    private int[] maxDemandDemand;
    private int customerNum;

    Random rand = new Random();

    Bank theBank = new Bank();

    Customer(int custNum)
    {
        threadName = Integer.toString(custNum);
    }
    public void run()
    {
        int[] resource = new int[3];
        for(int i = 0; i < theBank.NUM_OF_RESOURCES; i++)
        {
            resource[i] = theBank.allocated[customerNum][i];
        }
        int sleep = rand.nextInt(5) + 1;
        try
        {
            Thread.sleep(sleep*100);
        }
        catch (InterruptedException ioe)
        {}
       // Bank Banker = new Bank();
       // Banker.releaseResources(custNum, resource);

    }
    public void start()
    {
        if (t==null)
        {
            t = new Thread(this,threadName);
            t.start();
        }//end of start
    }

}