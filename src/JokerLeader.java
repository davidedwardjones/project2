package comp34120.ex2;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
/**
 * A pseudo leader. The members m_platformStub and m_type are declared
 * in the PlayerImpl, and feel free to use them. You may want to check
 * the implementation of the PlayerImpl. You will use m_platformStub to access
 * the platform by calling the remote method provided by it.
 * @author Xin
 */
final class JokerLeader
	extends PlayerImpl
{
	/**
	 * In the constructor, you need to call the constructor
	 * of PlayerImpl in the first line, so that you don't need to
	 * care about how to connect to the platform. You may want to throw
	 * the two exceptions declared in the prototype, or you may handle it
	 * by using "try {} catch {}". It's all up to you.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	JokerLeader()
		throws RemoteException, NotBoundException
	{
		/* The first parameter *MUST* be PlayerType.LEADER, you can change
		 * the second parameter, the name of the leader, such as "My Leader" */
		super(PlayerType.LEADER, "Pseudo Leader");
	}

	
	/**
	 * To inform this instance to proceed to a new simulation day
	 * @param p_date The date of the new day
	 * @throws RemoteException This exception *MUST* be thrown by this method
	 */
	@Override
	public void proceedNewDay(int p_date)
		throws RemoteException
	{
		/*
		 * Check for new price
		 * Record l_newRecord = m_platformStub.query(m_type, p_date);
		 *
		 * Your own math model to compute the price here
		 * ...
		 * float l_newPrice = getPrice(getAllRecords(p_date));
		 *
		 * Submit your new price, and end your phase
		 * m_platformStub.publishPrice(m_type, l_newPrice);
		 */
		 
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
		ArrayList<Record> records = new ArrayList<>();
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
	
}