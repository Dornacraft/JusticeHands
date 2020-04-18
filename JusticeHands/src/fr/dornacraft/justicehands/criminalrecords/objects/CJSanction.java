package fr.dornacraft.justicehands.criminalrecords.objects;

import java.sql.Timestamp;

import org.bukkit.entity.Player;

/*
 *  Cet objet est une sanction provenant du casier judiciaire du joueur,
 *  il a donc plus d'informations qu'une sanction provenant de la configuration
 *  du plugin (fichier : config.yml)
 */

public class CJSanction {
	private String sanctionName;
	private String sanctionReason;
	private int sanctionPoints;
	private String sanctionType;
	private Player sanctionModerator;
	private String sanctionID;
	private Timestamp sanctionTS;
	private Timestamp sanctionETS;
	private Player sanctionPlayer;
	private String sanctionState;

	public CJSanction() {
	}

	// *** MUTATEURS ***//
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

	public void setModerator(Player sanctionModerator) {
		this.sanctionModerator = sanctionModerator;
	}

	public void setID(String sanctionID) {
		this.sanctionID = sanctionID;
	}

	public void setTSDate(Timestamp sanctionTS) {
		this.sanctionTS = sanctionTS;
	}

	public void setTSExpireDate(Timestamp sanctionETS) {
		this.sanctionETS = sanctionETS;
	}

	public void setPlayer(Player sanctionPlayer) {
		this.sanctionPlayer = sanctionPlayer;
	}
	
	public void setState(String sanctionState) {
		this.sanctionState = sanctionState;
	}

	// *** ACCESSEURS ***//
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

	public Player getModerator() {
		return this.sanctionModerator;
	}

	public String getID() {
		return this.sanctionID;
	}

	public Timestamp getTSDate() {
		return this.sanctionTS;
	}

	public Timestamp getTSExpireDate() {
		return this.sanctionETS;
	}

	public Player getPlayer() {
		return this.sanctionPlayer;
	}
	
	public String getState() {
		return this.sanctionState;
	}

	@Override
	public String toString() {
		return "[{sanctionName=" + this.sanctionName +
				"},{sanctionReason=" + this.sanctionReason +
				"},{sanctionPoints=" + this.sanctionPoints +
				"},{sanctionType=" + this.sanctionType + 
				"},{sanctionModerator=" + this.sanctionModerator+ 
				"},{sanctionID=" + this.sanctionID +
				"},{sanctionTS=" + this.sanctionTS +
				"},{sanctionETS=" + this.sanctionETS +
				"},{sanctionPlayer" + this.sanctionPlayer +
				"},{sanctionState" + this.sanctionState + "}]";
	}
}
