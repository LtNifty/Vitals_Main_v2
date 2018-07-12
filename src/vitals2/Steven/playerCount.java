package vitals2.Steven;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class playerCount {
	
	public static int playerCount, SurvivalCount, CreativeCount;
	
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

}
