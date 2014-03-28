package comp34120.ex2;
import java.util.*;
public class LinearEq
{
	ArrayList<Record> records = null;
	
	double a0 = 0;
	double a1 = 0;
	
	public LinearEq(ArrayList<Record> records)
	{
		this.records = records;
	}
	
	public void doRegression()
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
	
	/*
	//lecture 4, slide 20
	public void doRegression()
	{
		//x= leader
		//y= follower
		
		float sumxsquared = 0;
		float sumy = 0;
		float sumx = 0;
		float sumxy = 0;
		int T = records.size();
		
		for(Record r: records)
		{
			sumy += r.m_followerPrice;
			sumx += r.m_leaderPrice;
			sumxsquared += r.m_leaderPrice*r.m_leaderPrice;
			sumxy += r.m_leaderPrice * r.m_followerPrice;
		
		}
		
		this.a0 = (sumxsquared*sumy - sumx*sumxy) / (T*(sumxsquared - (sumx*sumx)));
		this.a1 = T*(sumxy - sumx*sumy)/(T*sumxsquared - (sumx*sumx));
	}*/
	
	public float getReactionEstimate(float leaderPrice)
	{
		return (float)(a0 + (a1*leaderPrice));
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
