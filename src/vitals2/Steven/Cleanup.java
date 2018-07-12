package vitals2.Steven;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Cleanup {
	
	public static void startup() {
		boatsAndCarts();
		mobs();
	}

	public static void mobs() {
		return;
	}

	public static void boatsAndCarts() {
		
		int EmptyBoats = 0, EmptyCarts = 0, TotalRemoved = 0;
		Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "Cleanup started");
		
		for (Entity entity : Bukkit.getServer().getWorld("world").getEntities()) {
			if (entity.getType() == EntityType.BOAT) {
				if (!(entity.getPassengers() instanceof Player)) {
					EmptyBoats++;
					if (EmptyBoats == 11) {
						entity.remove();
						Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + entity.getType().toString() + " at " + entity.getLocation().toString() + " has been removed.");
						EmptyBoats--;
						TotalRemoved++;
					}
				}
			}
			if (entity.getType() == EntityType.MINECART) {
				if (!(entity.getPassengers() instanceof Player)) {
					EmptyCarts++;
					if (EmptyCarts == 11) {
						entity.remove();
						Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + entity.getType().toString() + " at " + entity.getLocation().toString() + " has been removed.");
						EmptyCarts--;
						TotalRemoved++;
					}
				}
			}
		}
		if (TotalRemoved > 0) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_BLUE + "Number of carts and boats removed: " + TotalRemoved);
		}
		Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "Cleanup Done.");
		return;
	}

}
