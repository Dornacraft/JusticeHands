package fr.dornacraft.justicehands.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlPlayerAccount {

	//Méthodes permettant de récupérer des informations sur le compte du joueur (par exemple le nombre de points)
	private Connection connection;
	
	// Constructeur
	public SqlPlayerAccount(Connection connection) {
		this.connection = connection;
	}

	// Création d'un compte
	public void createAccount(UUID playerUUID) {
		if (!hasAccount(playerUUID)) {
			try {
				PreparedStatement q = connection.prepareStatement("INSERT INTO players_points(uuid,points) VALUES (?,?)");
				q.setString(1, playerUUID.toString());
				q.setInt(2, 0);
				q.execute();
				q.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	// Vérification d'un compte déjà existant
	public boolean hasAccount(UUID playerUUID) {
		try {
			PreparedStatement q = connection.prepareStatement("SELECT uuid FROM players_points WHERE uuid = ?");
			q.setString(1, playerUUID.toString());
			ResultSet resultat  = q.executeQuery();
			boolean hasAccount = resultat.next();
			q.close();
			return hasAccount;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Récupération du compte
	public UUID getAccount(UUID playerUUID) {
		try {
			PreparedStatement q = connection.prepareStatement("SELECT uuid FROM players_points WHERE uuid = ?");
			q.setString(1, playerUUID.toString());
			ResultSet rs  = q.executeQuery();
			
			while(rs.next()) {
				return playerUUID;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	// Récupération du nombre de points du joueur
	public int getPoints(UUID playerUUID) {
		try {
			PreparedStatement q = connection.prepareStatement("SELECT points FROM players_points WHERE uuid = ?");
			q.setString(1, playerUUID.toString());
			
			int points = 0;
			ResultSet rs = q.executeQuery();
			
			while(rs.next()) {
				points = rs.getInt("points");
			}
			
			q.close();
			return points;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// Actualisation du nombre de point du joueur
	public void setPoints(UUID playerUUID) {
		//TODO
	}
}
