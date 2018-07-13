package vitals2.Steven;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	
	private Main plugin = Main.getPlugin(Main.class);
	
	//files and configs here
	public FileConfiguration playerData;
	public File players;
	
	public FileConfiguration playerTime;
	public File playtime;
	
	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		players = new File(plugin.getDataFolder(), "players.yml");
		playtime = new File(plugin.getDataFolder(), "playtime.yml");
		
		if (!playtime.exists()) {
			try {
				playtime.createNewFile();
			}
			catch(IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not create the playtime.yml file");
			}
		}
		
		if (!players.exists()) {
			try {
				players.createNewFile();
			}
			catch(IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not create the players.yml file");
			}
		}
		
		playerData = YamlConfiguration.loadConfiguration(players);
	}
	
	public FileConfiguration getPlayers() {
		return playerData;
	}
	
	public void savePlayers() {
		try {
			playerData.save(players);
		}
		catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the players.yml file");
		}
	}
	
	public void reloadPlayers() {
		playerData = YamlConfiguration.loadConfiguration(players);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The players.yml file has been reloaded");
	}
	
	public FileConfiguration getPlaytime() {
		return playerTime;
	}
	
	public void savePlaytime() {
		try {
			playerTime.save(playtime);
		}
		catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the playtime.yml file");
		}
	}
	
	public void reloadPlaytime() {
		playerTime = YamlConfiguration.loadConfiguration(playtime);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The playtime.yml file has been reloaded");
	}
}
