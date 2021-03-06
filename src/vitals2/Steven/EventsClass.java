package vitals2.Steven;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

public class EventsClass implements Listener {
	
	public static boolean isChainmail(Material m) { return m == Material.CHAINMAIL_BOOTS || m == Material.CHAINMAIL_CHESTPLATE || m == Material.CHAINMAIL_HELMET || m == Material.CHAINMAIL_LEGGINGS; }
	public static Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
	public static Objective object = sb.registerNewObjective("Player Stats", "dummy");
	public static List<UUID> safeFall = new ArrayList<>();
	
	//get config
	private Main plugin;
	
	public EventsClass(Main pl) {
		plugin = pl;
	}
				
	@EventHandler
	public void negateFall(EntityDamageEvent event) {
		
		Entity entity = event.getEntity();
		
		if (entity instanceof Player) {
			if (event.getCause() == DamageCause.FALL) {
				Material landingOn = entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
				if (safeFall.contains(entity.getUniqueId())) {
					event.setCancelled(true);
					safeFall.remove(entity.getUniqueId());
				}
				else if (landingOn == Material.SPONGE || landingOn == Material.EMERALD_BLOCK) {
					event.setCancelled(true);
					return;
				}
				else
					return;
			}
			else if (((Player) entity).isFlying() && !((HumanEntity) entity).getGameMode().equals(GameMode.CREATIVE)) {
				((Player) entity).setFlying(false);
				safeFall.add(entity.getUniqueId());
			}
			else
				return;
		}
		else return;
	}
	
	@EventHandler
	public void betterChain(EntityDamageEvent event) {
		
		Entity player = event.getEntity();
		
		if (player instanceof Player) {
			if (player.hasPermission("betterchain.v")) {
				if (plugin.getConfig().getBoolean("betterchain")) {
					int numChainmail = 0;
					double damageFactor = 1;
					
					for (int i = 36; i <= 39; i++) {     //checks armor slots
						if (((Player) player).getInventory().getItem(i) != null) { 	//checks if the slot has an item
							if (isChainmail(((Player) player).getInventory().getItem(i).getType())) { 	//checks if that item is a piece of chain mail armor
								numChainmail++;
								damageFactor *= 0.8;
							}
						}
					}
					if (numChainmail > 0) { 	//if the player has more then one piece of armor
						player.sendMessage(ChatColor.AQUA + "Your chainmail armor reduced the damage taken by " + Math.round(100*(1 - damageFactor)) + "%!");
						event.setDamage((int) Math.round(damageFactor * event.getDamage()));
						//player.sendMessage("damageFactor = " + damageFactor);
						return;
					}
					else
						return;
				}
				else
					return;				
			}
			else
				return;
		}
		else
			return;
	}
	
	@EventHandler
	public void emeraldsAndsponges(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		Vector vector = player.getVelocity();
		Material standingOn = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
		
		if (plugin.getConfig().getBoolean("Movement")) {
			//SpeedBoost on Emeralds and jump on sponge
			if (standingOn == Material.EMERALD_BLOCK) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 5), true);
				return;
			}
			else if (standingOn == Material.SPONGE) {
				vector.setY(2);
				player.setVelocity(vector);
				return;
			}
		}
		else
			return;
	}
	
	@EventHandler
	public void elevatorUp(PlayerMoveEvent event) {

		Player player = event.getPlayer();
		Material standingOn = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
		Location loc = player.getEyeLocation().add(0,1,0);
		
		if (plugin.getConfig().getBoolean("elevator")) {
			if (standingOn == Material.BEDROCK) {
				if ((event.getTo().getY() > event.getFrom().getY()) && player.isFlying() == false) {
					while (loc.getY() < 256) {
						if (loc.getBlock().getType() == Material.BEDROCK) {
							player.teleport(loc.add(0,1,0));
							player.sendMessage("up");
							return;
						}
						loc.add(0,1,0);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void elevatorDown(PlayerToggleSneakEvent event) {

		Player player = event.getPlayer();
		Material standingOn = player.getEyeLocation().subtract(0,2,0).getBlock().getType();
		Location loc = player.getEyeLocation().add(0,1,0);
		
		if (plugin.getConfig().getBoolean("elevator")) {
			if (standingOn == Material.BEDROCK) {
				if (player.isSneaking() && player.isFlying() == false) {
					loc.subtract(0,4,0);
					while (loc.getY() > 30) {
						if (loc.getBlock().getType() == Material.BEDROCK) {
							player.teleport(loc.add(0,1,0));
							player.sendMessage("down");
							return;
						}
						loc.subtract(0,1,0);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		plugin.cfgm.reloadPlaytime();
		Player player = event.getPlayer();
		int veteranTime = plugin.getConfig().getInt("veteran_time");
		int playerTime = 0;
		if (plugin.cfgm.getPlaytime().contains(player.getName().toString())) {
			playerTime = plugin.cfgm.getPlaytime().getInt(player.getName().toString() + "." + "playtime");
		}	
		if (playerTime / 3600 >= veteranTime) {
			if (!player.hasPermission("veteran.v")) {
			player.sendMessage(ChatColor.GOLD + "You played on the old 6d7 server for over 50 hours. You are now a Veteran!");
			//Main.permission.playerAddGroup(player, "Veteran");
			plugin.cfgm.getPlayers().set(player.getUniqueId().toString() + ".hasVet", true);
			}
		}
		
		if (plugin.getConfig().getBoolean("Play_time")) {
			if (!(plugin.cfgm.getPlayers().contains(player.getUniqueId().toString()))) {
				plugin.cfgm.getPlayers().set(player.getUniqueId().toString() + ".playTime", 0);
				plugin.cfgm.getPlayers().set(player.getUniqueId().toString() + ".hasVet", false);
			}
			plugin.cfgm.savePlayers();
			return;
		}
		else
			return;
	}
	
	@EventHandler
	public void Featherfly(PlayerInteractEvent event) {
		
		Action action = event.getAction();
		Player player = event.getPlayer();
		ItemStack held = player.getInventory().getItemInMainHand();
		
		if (player.hasPermission("featherfly.v")) {
			if (plugin.getConfig().getBoolean("FeatherFly")) {
				if ((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && held.getType() == Material.FEATHER) {
					if (player.isFlying()) {
						player.sendMessage(ChatColor.BLUE + "You are no longer flying.");
						player.setAllowFlight(false);
						player.setFlying(false);
						return;
					}
					if (player.getAllowFlight() && !player.isFlying()) {
						return;
					}
					else if (!player.getAllowFlight()) {
						player.sendMessage(ChatColor.AQUA + "You started to fly.");
						player.setAllowFlight(true);
						player.getLocation().add(0, 2, 0);
						player.setFlying(true);
						held.setAmount(held.getAmount() - 1);
						return;
					}
				}
				else
					return;
			}
			else
				return;
		}
		else
			return;
	}
	
	@EventHandler
	public void blazeCast(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack held = player.getInventory().getItemInMainHand();
		
		if (player.hasPermission("blazecast.v")) {
			if (plugin.getConfig().getBoolean("Blaze_case")) {
				if (action.equals(Action.LEFT_CLICK_AIR) && held.getType() == Material.BLAZE_ROD) {
					player.launchProjectile(Fireball.class).setIsIncendiary(false);
					held.setAmount(held.getAmount() - 1);
					return;
					}
					else
						
						return;
			}
		}
		else
			return;
	}
	
	@EventHandler
	public void consumeEmmy(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack held = player.getInventory().getItemInMainHand();
		
		if (player.hasPermission("consumeemmy.v")) {
			if (plugin.getConfig().getBoolean("Consume_emmy")) {
				if ((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && held.getType() == Material.EMERALD) {
					if (plugin.emmy.contains(player.getUniqueId())) {
						plugin.emmy.remove(player.getUniqueId());
						return;
					}
					
					else {
						plugin.emmy.add(player.getUniqueId());
						player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Hasten your way on wind-touched heels!");
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2), true);
						held.setAmount(held.getAmount() - 1);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void anything(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String name = "&d&lBuyrank Menu";
		Inventory open = event.getInventory();
		ItemStack item = event.getCurrentItem();
		 int slot = 100, clickSlot = 100;
		if (open.getName().equals(ChatColor.translateAlternateColorCodes('&', name))) {
			if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
				item.getItemMeta().getLore();
			}
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
	
	/*@EventHandler
	public void zoneMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		ZoneControl.enterRegion(player);
	}
	
	/*@EventHandler
	public void playerDeath(PlayerDeathEvent event) {
		Player player = ((OfflinePlayer) event).getPlayer();
		WorldTime.deathTracker(player);
	}*/
}
