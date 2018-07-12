package vitals2.Steven;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EconomyHandler {
	

	public static void transfer(Player send, String rec, double amount) {
		Boolean isonline = false;
		Player receiver = null;
		
		if (!(send.getName().equals(rec))) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player.getName().equals(rec)) {
					isonline = true;
					receiver = player;
				}
			}
			if (isonline) {
				double sendbal = Main.economy.getBalance(send);
				if (sendbal >= amount) {
					OfflinePlayer sender = send;
					String newbal = String.format("%,.2f", Main.economy.getBalance(sender)-amount);
					send.sendMessage(ChatColor.GREEN + "You have sent $" + amount + " to " + rec + ".\nYour new balance is $" + newbal + ".");
					Main.economy.withdrawPlayer(send, amount);
					Main.economy.depositPlayer(receiver, amount);
					receiver.sendMessage(ChatColor.GREEN + "You have received $" + amount + " from " + send.getName());
					return;
				}
				else {
					send.sendMessage(ChatColor.RED + "You do not have sufficent balance to send $" + amount + " to " + rec);
					return;
				}
			}
			else {
				send.sendMessage(ChatColor.RED + rec + " is not online.");
				return;
			}
		}
		else {
			send.sendMessage(ChatColor.RED + "You cannot transfer money to yourself.");
			return;
		}
	}
	
	public static void buyRank(Player player) {
		String test = getGroup(player);
		switch(test) {
			case "Wanderer":
				player.sendMessage(ChatColor.RED + "You do not rank this way.");
				break;
			case "Citizen":
				if(Main.economy.getBalance(player) >= 10000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Noble!");
					Main.economy.withdrawPlayer(player, 10000);
					Main.permission.playerAddGroup("6d7", (OfflinePlayer)player, "Noble");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $10,000 to rank up.");
				}
				return;
			case "Noble":
				if(Main.economy.getBalance(player) >= 25000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Merchant!");
					Main.economy.withdrawPlayer(player, 25000);
					Main.permission.playerAddGroup(player, "Merchant");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $25,000 to rank up.");
				}
				return;
			case "Merchant":
				if(Main.economy.getBalance(player) >= 50000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Knight!");
					Main.economy.withdrawPlayer(player, 50000);
					Main.permission.playerAddGroup(player, "Knight");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $50,000 to rank up.");
				}
				return;
			case "Knight":
				if(Main.economy.getBalance(player) >= 100000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Baron!");
					Main.economy.withdrawPlayer(player, 100000);
					Main.permission.playerAddGroup(player, "Baron");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $100,000 to rank up.");
				}
				return;
			case "Baron":
				if(Main.economy.getBalance(player) >= 250000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Duke!");
					Main.economy.withdrawPlayer(player, 250000);
					Main.permission.playerAddGroup(player, "Duke");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $250,000 to rank up.");
				}
				return;
			case "Duke":
				if(Main.economy.getBalance(player) >= 500000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Chancellor!");
					Main.economy.withdrawPlayer(player, 500000);
					Main.permission.playerAddGroup(player, "Chancellor");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $500,000 to rank up.");
				}
				return;
			case "Chancellor":
				if(Main.economy.getBalance(player) >= 1000000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Viceroy!");
					Main.economy.withdrawPlayer(player, 1000000);
					Main.permission.playerAddGroup(player, "Viceroy");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $1,000,000 to rank up.");
				}
				return;
			case "Viceroy":
				if(Main.economy.getBalance(player) >= 2500000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Guardian!");
					Main.economy.withdrawPlayer(player, 2500000);
					Main.permission.playerAddGroup(player, "Guardian");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $2,500,000 to rank up.");
				}
				return;
			case "Guardian":
				if(Main.economy.getBalance(player) >= 10000000) {
					player.sendMessage(ChatColor.GOLD + "You are now a Avatar!");
					Main.economy.withdrawPlayer(player, 10000000);
					Main.permission.playerAddGroup(player, "Avatar");
				}
				else {
					player.sendMessage(ChatColor.RED + "You do not have enough money to rank up. You need $10,000,000 to rank up.");
				}
				return;
			case "Avatar":
				player.sendMessage(ChatColor.GOLD + "Your are the highest rank!");
				return;
			default:
				player.sendMessage("Error");
				break;
		}
		return;		
	}
	
	static String getGroup(Player player) {
		if (player.hasPermission("wanderer.v")) {
			return "Wanderer";
		}
		else if (player.hasPermission("citizen.v")) {
			return "Citizen";
		}
		else if (player.hasPermission("noble.v")) {
			return "Noble";
		}
		else if (player.hasPermission("merchant.v")) {
			return "Merchant";
		}
		else if (player.hasPermission("knight.v")) {
			return "Knight";
		}
		else if (player.hasPermission("baron.v")) {
			return "Baron";
		}
		else if (player.hasPermission("duke.v")) {
			return "Duke";
		}
		else if (player.hasPermission("chancellor.v")) {
			return "Chancellor";
		}
		else if (player.hasPermission("viceroy.v")) {
			return "Viceroy";
		}
		else if (player.hasPermission("guardian.v")) {
			return "Guardian";
		}
		else if (player.hasPermission("avatar.v")) {
			return "Avatar";
		}
		else
			return "Unknown";
	}

}
