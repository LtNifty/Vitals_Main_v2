package vitals2.Steven;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class playerCount implements Listener {
	
	public static int playerCount, SurvivalCount, CreativeCount;
	
	private Main plugin = Main.getPlugin(Main.class);
	
	public static void Counter() {
		int count = 0, count1 = 0, count2 = 0;
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getGameMode() == GameMode.SURVIVAL)
				count++;
			else if (player.getGameMode() == GameMode.CREATIVE)
				count1++;
			else
				count2++;
		}
		playerCount = count2;
		CreativeCount = count1;
		SurvivalCount = count;
		return;
	}

	public void playtime() {
		if (Main.players()) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (plugin.cfgm.getPlayers().contains(player.getUniqueId().toString())) {
					int playtime = plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + ".playTime");
					playtime++;
					int time = plugin.getConfig().getInt("wanderer_time");
					if (playtime == time) {
						if (plugin.getConfig().getBoolean("2hr_citizen") && player.hasPermission("Wanderer.v")) {
							autoRank(player);
						}
					}
					plugin.cfgm.getPlayers().set(player.getUniqueId().toString() + ".playTime", playtime);
				}
				else {
					if (plugin.getConfig().getBoolean("Play_time")) {
						if (!(plugin.cfgm.getPlayers().contains(player.getUniqueId().toString()))) {
							plugin.cfgm.getPlayers().set(player.getUniqueId().toString() + ".playTime", 0);
						}
						plugin.cfgm.savePlayers();
						return;
					}
					else
						return;
				}
			}
		}
		else
			return;
		plugin.cfgm.savePlayers();
		return;
	}

	private void autoRank(Player player) {
		player.sendMessage(ChatColor.GOLD + "Your total playtime has reached 2 hours. You are now a Citizen!");
		Main.permission.playerAddGroup("6d7", (OfflinePlayer) player, "Citizen");
		return;
	}

	
	/*case "Citizen":
				if(Main.economy.getBalance(player) >= 10000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Noble!");
					Main.economy.withdrawPlayer(player, 10000);
					Main.permission.playerAddGroup("6d7", (OfflinePlayer)player, "Noble");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $10,000 to rank up.");
				}
				return;*/
}
