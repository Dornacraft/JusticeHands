package fr.dornacraft.justicehands;

import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.justicehands.criminalrecords.CommandCR;
import fr.dornacraft.justicehands.criminalrecords.listeners.PlayerJoinListener;
import fr.dornacraft.justicehands.keyskeeper.listeners.AsyncChatListener;
import fr.dornacraft.justicehands.sanctionmanager.CategoriesList;
import fr.dornacraft.justicehands.sanctionmanager.CommandSM;
import fr.dornacraft.justicehands.sql.SqlConnection;
import fr.dornacraft.justicehands.sql.SqlKeysKeeper;
import fr.dornacraft.justicehands.sql.SqlPlayerAccount;
import fr.dornacraft.justicehands.sql.SqlSanctionManager;

public class Main extends JavaPlugin {
	private static SqlConnection sql = null;
	private static SqlPlayerAccount sqlPA = null;
	private static SqlSanctionManager sqlSM = null;
	private static SqlKeysKeeper sqlKK = null;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();

		// Connexion à la base de données
		sql = new SqlConnection("jdbc:mysql://","localhost","dornacraft","root","");
		sql.connection();
		
		//On donne la connection
		sqlPA = new SqlPlayerAccount(sql.getConnection());
		sqlSM = new SqlSanctionManager(sql.getConnection());
		sqlKK = new SqlKeysKeeper(sql.getConnection());
		
		// On active commandes et listeners
		activeCommands();
		activeListeners();
		
		// On charge les sanctions du dossier de configuration
		CategoriesList.getSanctionsConfig(getConfig());
	}
	
	@Override
	public void onDisable() {
		// Déconnection del a base de données
		sql.disconnect();
	}

	private void activeListeners() {
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
	}

	private void activeCommands() {
		getCommand("cr").setExecutor(new CommandCR());
		//getCommand("mt").setExecutor(new CommandMT(this));
		getCommand("sm").setExecutor(new CommandSM(this.getConfig()));
	}

	//// **** ACCESSEURS & MUTATEURS ****////
	public static SqlConnection getSql() {
		return sql;
	}
	public static SqlPlayerAccount getSqlPA() {
		return sqlPA;
	}
	public static SqlSanctionManager getSqlSM() {
		return sqlSM;
	}
	public static SqlKeysKeeper getSqlKK() {
		return sqlKK;
	}
	//////////////////////////////////////////
}
