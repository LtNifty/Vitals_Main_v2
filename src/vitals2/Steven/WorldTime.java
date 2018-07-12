package vitals2.Steven;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class WorldTime {
	
	public static Boolean isNight = false, firstRun = true;
	static List<UUID> track = new ArrayList<>();
	
	public static void sunHandler() {
		
		long worldTime = Bukkit.getServer().getWorld("6d7").getTime();
		Boolean fire1 = false, fire2 = false, fire3 = false, fire4 = false;
		
		if (worldTime == 0) {
			if (Main.players()) {
				if (fire1 == false) {
					fire1 = true;
					fire4 = false;
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[Worldtime] " + ChatColor.AQUA + "A new day has started.");
					NightReward();
				}
			}
				isNight = false;
				firstRun = true;
				return;
		}
		if (worldTime == 12000) {
			if (Main.players() ) {
				if (fire2 == false) {
					fire2 = true;
					fire1 = false;
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[Worldtime] " + ChatColor.AQUA + "Darkness begins to fall.");
				}
				return;
			}
		}
		if (worldTime == 13000) {
			if (Main.players()) {
				if (fire3 == false) {
					fire3 = true;
					fire2 = false;
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[Worldtime] " + ChatColor.AQUA + "Night time has fallen.");
				}
			}
				isNight = true;
				return;
		}
		if (worldTime == 23000) {
			if (Main.players()) {
				if (fire4 == false) {
					fire4 = true;
					fire1 = false;
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[Worldtime] " + ChatColor.AQUA + "The Suns light starts to become visable.");
				}
				return;
			}
		}
		else
			return;
	}

	private static void NightReward() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (track.contains(player.getUniqueId())) {
				Main.economy.depositPlayer(player, 500);
				player.sendMessage(ChatColor.GREEN + "You have survived the night and been awarded $500.");
			}
		}
		return;
	}

	public static void deathTracker(Player dead) {
		if (isNight) {
			if (firstRun) {
				firstRun = false;
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.getGameMode() == GameMode.SURVIVAL) {
						player.sendMessage(ChatColor.GOLD + "Survive the night to get a reward.");
						track.add(player.getUniqueId());
					}
				}
				return;
			}
			else if (dead != null) {
				if (track.contains(dead.getUniqueId()) && dead.getUniqueId() != null) {
					track.remove(dead.getUniqueId());
				}
			}
		}
		else
			return;
	}
	
	public static String time(String world) {
		long time = Bukkit.getServer().getWorld(world).getFullTime();
		int hours = (int)((time/1000+8)%24);
		int minutes = (int)(60*(time%1000)/1000);
		return String.format("%02d:%02d", hours, minutes);
	}
}
