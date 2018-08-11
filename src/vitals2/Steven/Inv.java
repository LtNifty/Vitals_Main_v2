package vitals2.Steven;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Inv implements Listener {
	private Main plugin = Main.getPlugin(Main.class);
	
	public void BuyrankInventory(Player player) {
		String name = "&6&lBuyrank Menu";
		Inventory I = plugin.getServer().createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', name));
		ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
		ItemMeta eMeta = empty.getItemMeta();
		eMeta.setDisplayName(ChatColor.GRAY + " ");
		empty.setItemMeta(eMeta);
		
		ItemStack noble = new ItemStack(Material.WOOL,1,(byte) 4);
		/*ItemStack merchant = new ItemStack(Material.WOOL,1,(byte) 1);
		ItemStack knight = new ItemStack(Material.WOOL,1,(byte) 13);
		ItemStack baron = new ItemStack(Material.WOOL,1,(byte) 5);
		ItemStack duke = new ItemStack(Material.WOOL,1,(byte) 10);
		ItemStack chancellor = new ItemStack(Material.WOOL,1,(byte) 9);
		ItemStack viceroy = new ItemStack(Material.WOOL,1,(byte) 3);
		ItemStack guardian = new ItemStack(Material.WOOL,1,(byte) 11);
		ItemStack avatar = new ItemStack(Material.WOOL,1,(byte) 14); */
		
		ItemMeta nobleMeta = noble.getItemMeta();
		nobleMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eNoble"));
		ArrayList<String> nobleLore = new ArrayList<String>();
		nobleLore.add(ChatColor.translateAlternateColorCodes('&', "&c&ltest"));
		nobleMeta.setLore(nobleLore);
		noble.setItemMeta(nobleMeta);
		
		I.setItem(0, noble);
		I.setItem(1, empty);
		I.setItem(2, empty);
		I.setItem(3, empty);
		I.setItem(4, empty);
		I.setItem(5, empty);
		I.setItem(6, empty);
		I.setItem(7, empty);
		I.setItem(8, empty);
		
		player.openInventory(I);
	}
}
