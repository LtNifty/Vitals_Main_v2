package vitals2.Steven;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.minecraft.server.v1_12_R1.CommandExecute;

public class Commands extends CommandExecute implements Listener, CommandExecutor {
	
	
	//get config
	private Main plugin;
	
	public Commands(Main pl) {
		plugin = pl;
	}
	
	//list of all commands
	public String cmd1 = "startauction";
	public String cmd2 = "bid";
	public String cmd3 = "buyrank";
	public String cmd4 = "V_reload";
	public String cmd5 = "playtime";
	
	public String noPerms = "You do not have permission to run this command.";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase(cmd1)) {
				if (plugin.getConfig().getBoolean("Auctions")) {
					if (sender.hasPermission("auctionstart.v")) {
						if (!(Auction.auctionRunning)) {
							if (args.length == 0) {
								sender.sendMessage(ChatColor.RED + "You must have a starting amount after the command!");
								return true;
							}
							else if (args.length == 1) {
								if (args[0].equalsIgnoreCase("+")) {
									Auction.startamount = 100;
									Auction.startAuction(sender);
									((Player)sender).getInventory().getItemInMainHand().setAmount(0);
									return true;
								}
								else {
									Auction.startamount = Integer.parseInt(args[0]);
									Auction.startAuction(sender);
									((Player) sender).getInventory().getItemInMainHand().setAmount(0);
									return true;
								}
							}
						}
						else {
							sender.sendMessage(ChatColor.RED + "There is currently an active auction, please wait until the auction ends.");
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + noPerms);
					}
				}
				else
					return true;
				
			}
			if (cmd.getName().equalsIgnoreCase(cmd2)) {
				if (plugin.getConfig().getBoolean("Auctions")) {
					if (sender.hasPermission("auctionbid.v")) {
						if (!(Auction.auctionRunning)) {
							sender.sendMessage(ChatColor.RED + "There is no active auction to bid on.");
							return true;
						}
						else if (args.length == 0) {
							sender.sendMessage(ChatColor.RED + "You must have a bit amount after the command!");
							return true;
						}
						else if (args.length == 1) {
							int bidamount = Integer.parseInt(args[0]);
							Auction.bid(sender, bidamount);
							return true;
						}
					}
					else
						sender.sendMessage(ChatColor.RED + noPerms);
				}
				else
					return true;
				
			}
			if (cmd.getName().equalsIgnoreCase(cmd3)) {
				if (plugin.getConfig().getBoolean("Buy_rank")) {
					Inv I = new Inv();
					I.BuyrankInventory((Player) sender);
					
					return true;
				}
				else
					return true;
			}
			if (cmd.getName().equalsIgnoreCase(cmd4)) {
				reloadAllCfg();
				plugin.reloadConfig();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase(cmd5)) {
				getTime((Player) sender);
				return true;
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
			return true;
		}
		return false;		
	}

	public void reloadAllCfg() {
		plugin.cfgm.reloadPlayers();
		plugin.reloadConfig();
		plugin.cfgm.reloadPlaytime();
	}

	private void getTime(Player player) {
		int hr, min, sec, time;
		String comb = null;
		time = plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + ".playTime");
		hr = time / 3600;
		min = (time % 3600) / 60;
		sec = time % 60;
		comb = String.format("%d:%2d:%2d", hr, min, sec);
		player.sendMessage(ChatColor.GOLD + "Your current playtime is: " + comb);
	}

	/*String unicodeList() {
    	StringBuilder sb = new StringBuilder();
    	for (String key : unicodes.keySet()) { 
    		sb.append(unicize(key)); 
    		sb.append(key); 
    		sb.append(" ");
    	}
    	return sb.toString();
	}
	
	private final HashMap<String,Integer> unicodes = new HashMap<String,Integer>();
	{
		unicodes.put(":airplane:", 0x2708); unicodes.put(":asterism:", 0x2042); unicodes.put(":notes:", 0x266b);
		unicodes.put(":biohazard:", 0x2623); unicodes.put(":cloud:", 0x2601); unicodes.put(":coffee:", 0x2615);
		unicodes.put(":comet:", 0x2604); unicodes.put(":flower:", 0x2698); unicodes.put(":frowny:", 0x2639);
		unicodes.put(":gear:", 0x2699); unicodes.put(":russia:", 0x262d); unicodes.put(":heart:", 0x2764);
		unicodes.put(":peace:", 0x262e); unicodes.put(":face:", 0x3020); unicodes.put(":note:", 0x266a);
		unicodes.put(":radioactive:", 0x2622); unicodes.put(":skull:", 0x2620); unicodes.put(":smiley:", 0x263a);
		unicodes.put(":snowflake:", 0x2744); unicodes.put(":snowman:", 0x2603); unicodes.put(":squiggly:", 0x2368);
		unicodes.put(":star:", 0x2605); unicodes.put(":sun:", 0x2600); unicodes.put(":umbrella:", 0x2602);
		unicodes.put(":lightning:", 0x26a1); unicodes.put(":yinyang:", 0x262f); unicodes.put(":spades:", 0x2660);
		unicodes.put(":clubs:", 0x2663); unicodes.put(":hearts:", 0x2665); unicodes.put(":diamonds:", 0x2666);
		unicodes.put(":smiley2:", 0x263b); unicodes.put(":wking:", 0x2654); unicodes.put(":wqueen:", 0x2655);
		unicodes.put(":wrook:", 0x2656); unicodes.put(":wbishop:", 0x2657); unicodes.put(":wknight:", 0x2658);
		unicodes.put(":wpawn:", 0x2659); unicodes.put(":bking:", 0x2660); unicodes.put(":bqueen:", 0x2661);
		unicodes.put(":brook:", 0x2662); unicodes.put(":bbishop:", 0x2663); unicodes.put(":bknight:", 0x2664);
		unicodes.put(":bpawn:", 0x2665);
	}
	
	String unicize(String string) {
		String s = string;
		StringBuffer buf; Pattern pat; Matcher mat;
		buf = new StringBuffer(); pat = Pattern.compile(":[0-9a-f]{4}:"); mat = pat.matcher(s);
		while (mat.find()) mat.appendReplacement(buf, Character.toString((char) Integer.parseInt(mat.group().substring(1, 5), 16)));
		mat.appendTail(buf); s = buf.toString();
		for (String key : unicodes.keySet()) s = s.replaceAll(key, Character.toString((char) unicodes.get(key).intValue()));
		return s;
	}*/

}
