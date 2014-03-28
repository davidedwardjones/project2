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

	/**
	 * Generate a random price based Gaussian distribution. The mean is p_mean,
	 * and the diversity is p_diversity
	 * @param p_mean The mean of the Gaussian distribution
	 * @param p_diversity The diversity of the Gaussian distribution
	 * @return The generated price
	 */
	private float genPrice(final float p_mean, final float p_diversity)
	{
		return (float) (p_mean + m_randomizer.nextGaussian() * p_diversity);
	}

	private float getPrice(ArrayList<Record> records)
	{
		//linear regression for records
		LinearEq lin = new LinearEq(records);
		lin.doRegression();
		
		//find best strat here
		
		return 0;
	}

	//Return array of all previous transactions
	private ArrayList<Record> getAllRecords(int numDays)
	{
		//for all dates, get records
		ArrayList<Record> records = new ArrayList();
		try
		{
			for(int i = 1; i < numDays; i++)
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
}
