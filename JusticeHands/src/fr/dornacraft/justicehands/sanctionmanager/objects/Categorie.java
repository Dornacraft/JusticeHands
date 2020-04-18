package fr.dornacraft.justicehands.sanctionmanager.objects;

import java.util.ArrayList;

public class Categorie {

	private String categoryName;
	private String categoryDesc;
	private ArrayList<Sanction> sanctionsList = new ArrayList<>();
	private int categoryLineSlot;
	private int categoryColumSlot;
	
	public Categorie() {
	}
	
	//*** MUTATEURS ***//
	public void setName(String categoryName) {
		this.categoryName = categoryName;
	}
	public void setDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public void setLineSlot(int categoryLineSlot) {
		this.categoryLineSlot = categoryLineSlot;
	}
	public void setColumSlot(int categoryColumSlot) {
		this.categoryColumSlot = categoryColumSlot;
	}
	public void addSanction(Sanction sanction) {
		sanctionsList.add(sanction);
	}
	
	//*** ACCESSEURS ***//
	public String getName() {
		return this.categoryName;
	}
	public String getDesc() {
		return this.categoryDesc;
	}
	public int getLineSlot() {
		return this.categoryLineSlot;
	}
	public int getColumSlot() {
		return this.categoryColumSlot;
	}
	public ArrayList<Sanction> getSanctionsList() {
		return this.sanctionsList;
	}

	
}
