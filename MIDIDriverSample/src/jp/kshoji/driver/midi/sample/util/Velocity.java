package jp.kshoji.driver.midi.sample.util;

public class Velocity
{
	private int local_velocity=127;
	private boolean boolFixedVelocity=true;
	public Velocity(){
		
		
		
		
	}

	public void setLocal_velocity(int local_velocity)
	{
		this.local_velocity = local_velocity;
	}

	public int getLocal_velocity(int intVelocity)
	{
		
		if(boolFixedVelocity==true){
			
			return local_velocity;
					
		}else{
			
			if(intVelocity>=local_velocity){
				
				
				return local_velocity;
				
			}else{
				
				return intVelocity;
				
			}
		}
	}

	public void setBoolFixedVelocity(boolean boolFixedVelocity)
	{
		this.boolFixedVelocity = boolFixedVelocity;
	}

	public boolean isBoolFixedVelocity()
	{
		return boolFixedVelocity;
	}
	
	
	
	
}
