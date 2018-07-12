package vitals2.Steven;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CheckItems {

	public static void giveItem(Player player, ItemStack aItem, ItemMeta meta) {
		ItemStack Item = new ItemStack(aItem.getType(), aItem.getAmount(), aItem.getDurability());
		Item.setItemMeta(meta);
		player.getInventory().addItem(Item);
	}
}
