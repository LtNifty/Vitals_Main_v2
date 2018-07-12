package vitals2.Steven;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FeatherFly {
	
	public static void itemRemove() {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if(!player.getGameMode().equals(GameMode.CREATIVE) && !player.hasPermission("essentials.fly")) {
				boolean hasFeather = false;
				int featherSlot = 0;
				for (int i = 0; i < 9; i++) {
					ItemStack held = player.getInventory().getItem(i);
					if (held != null) {
						if (held.getType() == Material.FEATHER) {
							featherSlot = i;
							hasFeather = true;
						}
					}
				}
				if (hasFeather) {
					ItemStack held = player.getInventory().getItem(featherSlot);
					if (player.isFlying()) {
						int amountLeft = held.getAmount() - 1;
						if (amountLeft == 1)
							player.sendMessage(ChatColor.BLUE + "You have one feather left, land soon to avoid damage.");
						else if (amountLeft == 0)
							player.sendMessage(ChatColor.RED + "You have no feathers left.");
						held.setAmount(amountLeft);
						return;
					}
					if (player.getAllowFlight() == true && player.isFlying() == false) {
						player.setAllowFlight(false);
						player.sendMessage(ChatColor.BLUE + "FeatherFlight has been cancled.");
						return;
					}
				}
				if (!hasFeather && player.isFlying()) {
					player.sendMessage(ChatColor.RED + "You have no more feathers and can no longer fly!");
					player.setFlying(false);
					player.setAllowFlight(false);
					EventsClass.safeFall.add(player.getUniqueId());
				}
				else
					return;
			}
		}
	}
}
