package vitals2.Steven;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

//import org.bukkit.configuration.file.YamlConfiguration;

public class Main extends JavaPlugin {
	
	private Commands commands = new Commands(this);
	private int Ticks = 0, RunTime = 0;
	public static Economy economy = null;
	public static Chat chat = null;
	public static Permission permission = null;
	public ConfigManager cfgm;
	public playerCount players;
	public EconomyHandler ecoH;
	//Logger log = getLogger();
	//private String languagefile = global("language") + ".yml";
	//private YamlConfiguration local = YamlConfiguration.loadConfiguration(new File(getDataFolder(), languagefile));	
	
	public void onEnable() {
		getCommand(commands.cmd1).setExecutor(commands);
		getCommand(commands.cmd2).setExecutor(commands);
		getCommand(commands.cmd3).setExecutor(commands);
		getCommand(commands.cmd4).setExecutor(commands);
		getCommand(commands.cmd5).setExecutor(commands);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Vitals has be enabled.");
		getServer().getPluginManager().registerEvents(new EventsClass(this), this);
		setupEconomy();
		setupPermissions();
		loadConfig();
		loadConfigManager();
		timer();
		getWorldGuard();
		playerInfo();
		//setupChat();
		//writeConfig();
		//writeLanguageFile();
	}
	
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "Vitals has be disabled.");		
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void loadConfigManager() {
		cfgm = new ConfigManager();
		cfgm.setup();
		cfgm.savePlayers();
		cfgm.reloadPlayers();
		cfgm.getPlaytime();
		cfgm.savePlaytime();
		cfgm.reloadPlaytime();
	}
	
	public void playerInfo() {
		players = new playerCount();
		players.playtime();
	}
	
	public void timer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				
				Ticks++;
				
				if (getConfig().getBoolean("World_time")) {
					WorldTime.sunHandler();
					WorldTime.deathTracker(null);
				}
				Auction.timer();
				
				if (Ticks % 20 == 0) { //every 1 seconds
					players.playtime();
				}
				if (Ticks % 200 == 0) { //every 10 seconds
					if (getConfig().getBoolean("FeatherFly"))
						FeatherFly.itemRemove();
				}
				if (Ticks % 600 == 0) { //every 30 secs
				}
				if (Ticks == 1200) { //every minute
					RunTime++;
					//CustomMobDrop.chicken();
					if (RunTime == 5) { //makes every 5 minutes
						//Cleanup.startup();
						RunTime = 0;
					}
				}
				
				if (Ticks == 2000) { //every 100 seconds and reset timer
					Ticks = 0;
				}				
			}
		}.runTaskTimerAsynchronously(this, 0, 1);
	}
	
	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null;
		}
		
		return (WorldGuardPlugin) plugin;
	}
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyprovider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyprovider != null) {
			economy = economyprovider.getProvider();
		}
		return (economy != null);
	}
	
	//this can be used to stop things when there is no players on
	//good for keeping console clean
	public static boolean players() {
		if (Bukkit.getServer().getOnlinePlayers().size() == 0)
			return false;
		else
			return true;
	}
	
	/*private boolean setupChat() {
		RegisteredServiceProvider<Chat> chatprovider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatprovider != null) {
			chat = chatprovider.getProvider();
		}
		return (chat != null);	
	}*/
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}