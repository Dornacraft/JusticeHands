package fr.dornacraft.justicehands.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {

	//Connexion et déconnexion de la base de données du serveur
	private Connection connection;
	private String urlbase, host, database, user, pass;

	// Constructeur
	public SqlConnection(String urlbase, String host, String database, String user, String pass) {
		this.urlbase = urlbase;
		this.host = host;
		this.database = database;
		this.user = user;
		this.pass = pass;
	}

	// Connexion à la base de données
	public void connection() {
		if (!isConnected()) {
			try {
				connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass);
				System.out.println("[Dornacraft] Base de données connectée");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// Déconnexion de la base de données
	public void disconnect() {
		if (isConnected()) {
			try {
				connection.close();
				System.out.println("[Dornacraft] Base de données déconnectée");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Déja connecté ?
	public boolean isConnected() {
		return connection != null;
	}
	
	// Récuperer la connexion
	public Connection getConnection() {
		if (isConnected()) {
			return connection;
		}
		return null;
	}
}
