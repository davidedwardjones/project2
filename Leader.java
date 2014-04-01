import comp34120.ex2.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
/**
 * A very simple leader implementation that only generates random prices
 * @author Xin
 */
final class Leader
	extends PlayerImpl
{
	/* The randomizer used to generate random price */
	private final Random m_randomizer = new Random(System.currentTimeMillis());

	private Leader()
		throws RemoteException, NotBoundException
	{
		super(PlayerType.LEADER, "Leader");
	}

	@Override
	public void goodbye()
		throws RemoteException
	{
		ExitTask.exit(500);
	}

	/**
	 * To inform this instance to proceed to a new simulation day
	 * @param p_date The date of the new day
	 * @throws RemoteException
	 */
	@Override
	public void proceedNewDay(int p_date)
		throws RemoteException
	{
		float l_newPrice = getPrice(getAllRecords(p_date));
		 
		 m_platformStub.publishPrice(m_type, l_newPrice);
	}

	private float getPrice(ArrayList<Record> records)
	{
		//linear regression for records
		LinearEq lin = new LinearEq(records, m_platformStub);
		lin.doRegression1();
		//lin.doRegression2();
		//System.out.println(lin.toString());
		
		//find best strat here
		return findMax(lin);
	}

  int windowSize = 100;
	//Return array of all previous transactions
	private ArrayList<Record> getAllRecords(int numDays)
	{
		//for all dates, get records
		ArrayList<Record> records = new ArrayList();
		try
		{
			for(int i = numDays-windowSize; i < numDays; i++)
			{
				records.add(m_platformStub.query(m_type, i));
			}
		}
		catch(RemoteException e)
		{
			System.err.println(e);		
		}
		
		return records;
	}

	public static void main(final String[] p_args)
		throws RemoteException, NotBoundException
	{
    // windowSize = Integer.parseInt(p_args[0]);
		new Leader();
	}

	/**
	 * The task used to automatically exit the leader process
	 * @author Xin
	 */
	private static class ExitTask
		extends TimerTask
	{
		static void exit(final long p_delay)
		{
			(new Timer()).schedule(new ExitTask(), p_delay);
		}
		
		@Override
		public void run()
		{
			System.exit(0);
		}
	}

  double totalProfit = 0.0;
  private float findMax(LinearEq lin)
  {
    return (float) ((2.7 + 0.3 * lin.a0) / (2.0 - 0.6 * lin.a1));
  }

  @Override
  public void startSimulation(int p_steps)
    throws RemoteException
  {
    super.startSimulation(p_steps);
    totalProfit = 0.0;
    //TO DO: delete the line above and put your own initialization code here
  }

  /**
   * You may want to delete this method if you don't want to do any
   * finalization
   * @throws RemoteException If implemented, the RemoteException *MUST* be
   * thrown by this method
   */
  @Override
  public void endSimulation()
    throws RemoteException
  {
    super.endSimulation();
    System.out.println("total profit: " + getTotalProfit());
    //TO DO: delete the line above and put your own finalization code here
  }

  public float getTotalProfit()
  {
    //for all dates, get records
    double totalProfit = 0.0;
    try
    {
      for(int i = 101; i <= 130; i++)
      {
        Record rec = m_platformStub.query(m_type, i);
        totalProfit += (rec.m_leaderPrice - 1)*(2 - rec.m_leaderPrice + 0.3*rec.m_followerPrice);
      }
    }
    catch(RemoteException e)
    {
      System.err.println(e);    
    }
    return (float)totalProfit;
  }
}
