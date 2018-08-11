package vitals2.Steven;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Inv implements Listener {
	private Main plugin = Main.getPlugin(Main.class);
	
	public void BuyrankInventory(Player player) {
		
		int slot = 100;
		
		List<String> rankList = plugin.getConfig().getStringList("Ranks");
		for (int i = 0; i < rankList.size(); i++) {
			if (rankList.get(i).equalsIgnoreCase(getGroup(player))) {
				slot = i;
				break;
			}
		}
		
		String name = "&d&lBuyrank Menu";
		Inventory I = plugin.getServer().createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', name));
		ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
		ItemMeta eMeta = empty.getItemMeta();
		eMeta.setDisplayName(ChatColor.GRAY + " ");
		empty.setItemMeta(eMeta);
		
		ItemStack noble = new ItemStack(Material.WOOL,1,(byte) 4);
		ItemStack merchant = new ItemStack(Material.WOOL,1,(byte) 1);
		ItemStack knight = new ItemStack(Material.WOOL,1,(byte) 13);
		ItemStack baron = new ItemStack(Material.WOOL,1,(byte) 5);
		ItemStack duke = new ItemStack(Material.WOOL,1,(byte) 10);
		ItemStack chancellor = new ItemStack(Material.WOOL,1,(byte) 9);
		ItemStack viceroy = new ItemStack(Material.WOOL,1,(byte) 3);
		ItemStack guardian = new ItemStack(Material.WOOL,1,(byte) 11);
		ItemStack avatar = new ItemStack(Material.WOOL,1,(byte) 14);
		
		// Creates wool for Noble
		ItemMeta nobleMeta = noble.getItemMeta();
		nobleMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eNoble"));
		ArrayList<String> nobleLore = new ArrayList<String>();
		nobleLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,2)));
		nobleMeta.setLore(nobleLore);
		if (getGroup(player).equalsIgnoreCase("Noble")) {
			nobleMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			nobleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		noble.setItemMeta(nobleMeta);
		
		// Creates wool for Merchant
		ItemMeta merchantMeta = merchant.getItemMeta();
		merchantMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Merchant"));
		ArrayList<String> merchantLore = new ArrayList<String>();
		merchantLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,3)));
		merchantMeta.setLore(merchantLore);
		if (getGroup(player).equalsIgnoreCase("Merchant")) {
			merchantMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			merchantMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		merchant.setItemMeta(merchantMeta);
		
		// Creates wool for Knight
		ItemMeta knightMeta = knight.getItemMeta();
		knightMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2Knight"));
		ArrayList<String> knightLore = new ArrayList<String>();
		knightLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,4)));
		knightMeta.setLore(knightLore);
		if (getGroup(player).equalsIgnoreCase("Knight")) {
			knightMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			knightMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		knight.setItemMeta(knightMeta);
		
		// Creates wool for Baron
		ItemMeta baronMeta = baron.getItemMeta();
		baronMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aBaron"));
		ArrayList<String> baronLore = new ArrayList<String>();
		baronLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,5)));
		baronMeta.setLore(baronLore);
		if (getGroup(player).equalsIgnoreCase("Baron")) {
			baronMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			baronMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		baron.setItemMeta(baronMeta);
		
		// Creates wool for Duke
		ItemMeta dukeMeta = duke.getItemMeta();
		dukeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5Duke"));
		ArrayList<String> dukeLore = new ArrayList<String>();
		dukeLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,6)));
		dukeMeta.setLore(dukeLore);
		if (getGroup(player).equalsIgnoreCase("Duke")) {
			dukeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			dukeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		duke.setItemMeta(dukeMeta);
		
		// Creates wool for Chancellor
		ItemMeta chancellorMeta = chancellor.getItemMeta();
		chancellorMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3Chancellor"));
		ArrayList<String> chancellorLore = new ArrayList<String>();
		chancellorLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,7)));
		chancellorMeta.setLore(chancellorLore);
		if (getGroup(player).equalsIgnoreCase("Chancellor")) {
			chancellorMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			chancellorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		chancellor.setItemMeta(chancellorMeta);
		
		// Creates wool for Viceroy
		ItemMeta viceroyMeta = viceroy.getItemMeta();
		viceroyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bViceroy"));
		ArrayList<String> viceroyLore = new ArrayList<String>();
		viceroyLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,8)));
		viceroyMeta.setLore(viceroyLore);
		if (getGroup(player).equalsIgnoreCase("Viceroy")) {
			viceroyMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			viceroyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		viceroy.setItemMeta(viceroyMeta);
		
		// Creates wool for Guardian
		ItemMeta guardianMeta = guardian.getItemMeta();
		guardianMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&1Guardian"));
		ArrayList<String> guardianLore = new ArrayList<String>();
		guardianLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,9)));
		guardianMeta.setLore(guardianLore);
		if (getGroup(player).equalsIgnoreCase("Guardian")) {
			guardianMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			guardianMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		guardian.setItemMeta(guardianMeta);
		
		// Creates wool for Avatar
		ItemMeta avatarMeta = guardian.getItemMeta();
		avatarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Avatar"));
		ArrayList<String> avatarLore = new ArrayList<String>();
		avatarLore.add(ChatColor.translateAlternateColorCodes('&', getCost(slot,10)));
		avatarMeta.setLore(avatarLore);
		if (getGroup(player).equalsIgnoreCase("Avatar")) {
			avatarMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			avatarMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		avatar.setItemMeta(avatarMeta);
		
		I.setItem(0, noble);
		I.setItem(1, merchant);
		I.setItem(2, knight);
		I.setItem(3, baron);
		I.setItem(4, duke);
		I.setItem(5, chancellor);
		I.setItem(6, viceroy);
		I.setItem(7, guardian);
		I.setItem(8, avatar);
		
		player.openInventory(I);
	}
	
	public void confirmGUI(Player player) {
		String name = "&d&lConfirmation Menu";
		Inventory I = plugin.getServer().createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', name));
		
		ItemStack Y = new ItemStack(Material.EMERALD_BLOCK, 1);
		ItemStack e = new ItemStack(Material.STICK, 1);
		ItemStack N = new ItemStack(Material.REDSTONE_BLOCK, 1);
		
		ItemMeta YMeta = Y.getItemMeta();
		YMeta.setDisplayName(ChatColor.GREEN + "COMFIRM");
		Y.setItemMeta(YMeta);
		
		ItemMeta eMeta = e.getItemMeta();
		eMeta.setDisplayName("");
		e.setItemMeta(eMeta);
		
		ItemMeta NMeta = N.getItemMeta();
		NMeta.setDisplayName(ChatColor.RED + "CANCEL");
		N.setItemMeta(NMeta);
		
		I.setItem(0, Y);
		I.setItem(1, Y);
		I.setItem(2, Y);
		I.setItem(3, Y);
		I.setItem(4, e);
		I.setItem(5, N);
		I.setItem(6, N);
		I.setItem(7, N);
		I.setItem(8, N);
		I.setItem(9, Y);
		I.setItem(10, Y);
		I.setItem(11, Y);
		I.setItem(12, Y);
		I.setItem(13, e);
		I.setItem(14, N);
		I.setItem(15, N);
		I.setItem(16, N);
		I.setItem(17, N);
		I.setItem(18, Y);
		I.setItem(19, Y);
		I.setItem(20, Y);
		I.setItem(21, Y);
		I.setItem(22, e);
		I.setItem(23, N);
		I.setItem(24, N);
		I.setItem(25, N);
		I.setItem(26, N);
		I.setItem(27, Y);
		I.setItem(28, Y);
		I.setItem(29, Y);
		I.setItem(30, Y);
		I.setItem(31, e);
		I.setItem(32, N);
		I.setItem(33, N);
		I.setItem(34, N);
		I.setItem(35, N);
		I.setItem(36, Y);
		I.setItem(37, Y);
		I.setItem(38, Y);
		I.setItem(39, Y);
		I.setItem(40, e);
		I.setItem(41, N);
		I.setItem(42, N);
		I.setItem(43, N);
		I.setItem(44, N);
		I.setItem(45, Y);
		I.setItem(46, Y);
		I.setItem(47, Y);
		I.setItem(48, Y);
		I.setItem(49, e);
		I.setItem(50, N);
		I.setItem(51, N);
		I.setItem(52, N);
		I.setItem(53, N);
		
		player.openInventory(I);
	}
	
	public String getCost(int slot, int clickSlot) {
		List<Integer> rankCost = plugin.getConfig().getIntegerList("rank_cost");
		int cost = 0;
		if (clickSlot < slot) {
			return "&cUghie";
		}
		else if (clickSlot == slot) {
			return "You are dis rank already";
		}
		else {
			for (int i = slot + 1; i <= clickSlot; i++) {
				cost += rankCost.get(i);
			}
			String usd = String.format("%,d", cost);
			return ("&eCost: $" + usd);
		}
	}
	
	String getGroup(Player player) {
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
