package comp34120.ex2;
import java.util.*;
public class LinearEq
{
	ArrayList<Record> records = null;
	
	float a0 = 0;
	float a1 = 0;
	
	public LinearEq(ArrayList<Record> records)
	{
		this.records = records;
	}
	
	public void doRegression()
	{
		float a = 0;
		float b = 0;
		float avLeader = getAv(PlayerType.LEADER);
		float avFollower = getAv(PlayerType.FOLLOWER);
		for(Record r: records)
		{
			a += (r.m_leaderPrice - avLeader) * (r.m_followerPrice - avFollower);
			b += (r.m_leaderPrice - avLeader) * (r.m_leaderPrice - avLeader);
		}
		
		this.a1 = a/b;
		this.a0 = avFollower - a1*avLeader;
	}
	
	public float getReactionEstimate(float leaderPrice)
	{
		return a0 + (a1*leaderPrice);
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
	
}
