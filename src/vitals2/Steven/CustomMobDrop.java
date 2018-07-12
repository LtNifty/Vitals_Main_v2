package vitals2.Steven;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class CustomMobDrop {

	public static void chicken() {
		for (Entity entity : Bukkit.getServer().getWorld("world").getEntities()) {
			if (entity.getType() == EntityType.CHICKEN) {
				ItemStack feather = new ItemStack(Material.FEATHER);
				Random Random = new Random();
				int rand = Random.nextInt(5) + 1;
				if (rand == 2 || rand == 4) 
					entity.getWorld().dropItemNaturally(entity.getLocation().add(0, 0.5, 0), feather);
			}
		}
	}
}
