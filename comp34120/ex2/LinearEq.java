package comp34120.ex2;
import java.util.*;
import java.rmi.RemoteException;
public class LinearEq
{
	ArrayList<Record> records = null;
	
	public double a0 = 0;
	public double a1 = 0;
	private Platform platform;
	public LinearEq(ArrayList<Record> records, Platform platform)
	{
		this.records = records;
		this.platform = platform;
	}
	
	
	public void doRegression1()
	{
		double a = 0;
		double b = 0;
		double avLeader = getAv(PlayerType.LEADER);
		double avFollower = getAv(PlayerType.FOLLOWER);
		for(Record r: records)
		{
			a += (r.m_leaderPrice - avLeader) * (r.m_followerPrice - avFollower);
			b += (r.m_leaderPrice - avLeader) * (r.m_leaderPrice - avLeader);
		}
		
		this.a1 = a/b;
		this.a0 = avFollower - a1*avLeader;
	}

  private final double forgettingFactor = 0.95;
	//lecture 4, slide 20
	public void doRegression2()
	{
		//x= leader
		//y= follower
		
		float sumxsquared = 0;
		float sumy = 0;
		float sumx = 0;
		float sumxy = 0;
		int T = records.size() - 1;
		
		for(int i = 0; i < records.size(); i++)
		{
			Record r = records.get(i);
//			System.out.println(r.m_date + "\tforgetting factor power = " + (records.size() - i));
			double lambda = Math.pow(forgettingFactor, records.size() - i);
			sumy += lambda * r.m_followerPrice;
			sumx += lambda * r.m_leaderPrice;
			sumxsquared += lambda *r.m_leaderPrice*r.m_leaderPrice;
			sumxy += lambda * r.m_leaderPrice * r.m_followerPrice;
		
		}
		
		this.a0 = (sumxsquared*sumy - sumx*sumxy) / (T*sumxsquared - (sumx*sumx));
		this.a1 = (T*sumxy - sumx*sumy)/(T*sumxsquared - (sumx*sumx));
	}
	
	public float getReactionEstimate(float leaderPrice)
	{
		return (float)(a0 + (a1*leaderPrice));
	}
	
	public float getError() throws RemoteException
	{
		float preEstimate = getReactionEstimate(platform.query(PlayerType.LEADER, records.size() - 1).m_leaderPrice);
		float preFollowerPrice = platform.query(PlayerType.FOLLOWER, records.size() - 1).m_followerPrice; 
		System.out.println(Math.abs(preEstimate - preFollowerPrice));
		return Math.abs(preEstimate - preFollowerPrice);
	}
	
	public float getAv(PlayerType type)
	{
		float total = 0;
		
		for(Record r: records)
		{
			total += (type == PlayerType.LEADER)? r.m_leaderPrice: r.m_followerPrice;
		}
		
		return total/records.size();
	}
	
	public String toString()
	{
		return "Ur = " + String.format("%.5f",a0) + " + " + String.format("%.5f",a1) + "*Ul";
	}
	
}
