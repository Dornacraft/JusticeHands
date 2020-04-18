package fr.dornacraft.justicehands.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class SqlKeysKeeper {

	//Englobe toutes les méthodes en lien avec KeysKepper et la base de données
	private Connection connection;

	// Constructeur
	public SqlKeysKeeper(Connection connection) {
		this.connection = connection;
	}
	
	public List<Long> getPlayerMutesEDLong(Player player) {
		ArrayList<Long> playerMuteList = new ArrayList<>();
		try {
			PreparedStatement q = connection.prepareStatement("SELECT * FROM sanctions_list WHERE type = ?");
			q.setString(1, "mute");

			ResultSet rs = q.executeQuery();
			while (rs.next()) {
				Long expireDateLong = rs.getTimestamp("expiredate").getTime();
				playerMuteList.add(expireDateLong);
			}
			q.close();
			return playerMuteList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
