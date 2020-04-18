package fr.dornacraft.justicehands.sanctionmanager.objects;

public class Sanction implements Cloneable{
	private String sanctionName;
	private String sanctionReason;
	private int sanctionPoints;
	private String sanctionType;
	
	public Sanction() {
		
	}
	// Clone une sanction
	@Override 
	public Object clone() {
		Sanction sanction = new Sanction();
		sanction.sanctionName = new String (this.sanctionName);
		sanction.sanctionPoints = this.sanctionPoints;
		sanction.sanctionReason = new String (this.sanctionReason);
		sanction.sanctionType = new String (this.sanctionType);
		return sanction;
	}
	
	//*** MUTATEURS ***//
	public void setName(String sanctionName) {
		this.sanctionName = sanctionName;
	}
	public void setReason(String sanctionReason) {
		this.sanctionReason = sanctionReason;
	}
	public void setPoints(int sanctionPoints) {
		this.sanctionPoints = sanctionPoints;
	}
	public void setInitialType(String sanctionType) {
		this.sanctionType = sanctionType;
	}
	
	//*** ACCESSEURS ***//
	public String getName() {
		return sanctionName;
	}
	public String getReason() {
		return sanctionReason;
	}
	public int getPoints() {
		return sanctionPoints;
	}
	public String getInitialType() {
		return sanctionType;
	}
}
