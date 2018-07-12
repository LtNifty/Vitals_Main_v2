package vitals2.Steven;

import org.bukkit.plugin.Plugin;

public class ZoneControl {
	
	//private static ArrayList<Player> entered = new ArrayList<>();
	//private static ArrayList<Player> left = new ArrayList<>();
	static Plugin getWorldGuard = Main.getWorldGuard();
	
	/*public static void enterRegion(Player player) {
		LocalPlayer localPlayer = ((WorldGuardPlugin) getWorldGuard).wrapPlayer(player);
		com.sk89q.worldedit.Vector playerVector = localPlayer.getPosition();
		RegionManager regionManager = ((WorldGuardPlugin) getWorldGuard).getRegionManager(player.getWorld());
		ApplicableRegionSet applicableRegionSet = regionManager.getApplicableRegions(playerVector);
		
		for (ProtectedRegion regions : applicableRegionSet) {
			if (regions.contains(playerVector)) {
				if (!entered.contains(player)) {
					try { 
						entered.add(player);
						left.remove(player);
						String owners = regions.getOwners().toPlayersString();
						owners = owners.replace("uuid:", "");
						
						Player regionPlayer = Bukkit.getServer().getPlayer(UUID.fromString(owners));
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
						player.sendMessage(ChatColor.GREEN + "Now Entering: " + ChatColor.BLUE + regionPlayer.getName() + "'s property");
						}
					catch (Exception e) { e.printStackTrace(); }
				}
			}
		}
		if (!left.contains(player)) {
			if (applicableRegionSet.size() == 0) {
				entered.remove(player);
				left.add(player);
			}
		}
	}
   /* private HashMap<String,List<String>> pzones = new HashMap<String,List<String>>();
	private HashMap<String,List<Entity>> armies = new HashMap<String,List<Entity>>();
	private Main v = Main.getPlugin(Main.class);
	
	boolean has(String[] args, int index, String s) { if (args.length >= index + 1) return args[index].equalsIgnoreCase(s); return false; }
	
	void jobEjectUnauthorizedPlayers() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			for (String zone : zonesContaining(p.getLocation())) {
				int rankNeeded = zoneFlag(zone, "entryguildrank").equals("") ? 0 : Integer.parseInt(zoneFlag(zone, "entryguildrank"));
				String guildNeeded = zoneFlag(zone, "entryguild"), denyReason = "";
				boolean denyGuild = !guildNeeded.equals("") && v.guildRank(p, guildNeeded) == 0;
				if (denyGuild) denyReason = "need [" + guildNeeded + "]";
				boolean denyRank = false;
				if (!guildNeeded.equals("")) {
					denyRank = rankNeeded > 0 && v.guildRank(p, guildNeeded) < rankNeeded;
					if (denyRank) denyReason = "need [" + guildNeeded + " L" + rankNeeded + "] have [L" + v.guildRank(p, guildNeeded) + "]";
				} else if (rankNeeded > 0) {
					denyRank = v.guildBestRank(p) < rankNeeded;
					if (denyRank) denyReason = "need [Any guild L" + rankNeeded + "] have [L" + v.guildBestRank(p) + "]";
				}
				if (denyGuild || denyRank) {
					zoneEject(p, zone); v.log.info("[zones] ejected " + p.getName() + " from " + zone + ": " + denyReason);
				}
			}
		}
	}
	void jobAnnounceEntryExit() {
		for (Player p : v.getServer().getOnlinePlayers()) {
			String announceTo = v.getConfig().getString("zonecontrol_announce", "none");
			if (announceTo.equals("everyone") || announceTo.equals("ops") && p.isOp()) {
				if (!pzones.containsKey(p.getName())) pzones.put(p.getName(), new ArrayList<String>());
				List<String> zonesPrev = pzones.get(p.getName()), zonesNow = zonesContaining(p.getLocation());
				if (zonesPrev.equals(zonesNow)) continue;
				for (String z : zonesPrev) {
					if (zonesNow.contains(z)) continue;
					if (zoneFlag(z, "hidden").equals("")) {
						p.sendMessage(colorize("&e--- Leaving " + (zoneFlag(z, "friendlyname").equals("") ? z : zoneFlag(z, "friendlyname")) + " ---"));
					}
					for (Player pp : v.getServer().getOnlinePlayers()) {
						if (pp.equals(p) && zoneFlag(z, "hidden").equals("")) continue;
						String zonespy = v.playerSetting(pp.getName(), "zonespy");
						if (zonespy.equals("all") || (zonespy.equals("me") && p.equals(pp))) {
							pp.sendMessage(colorize("&8" + p.getName() + " left [" + z + "]"));
						}
					}
				}
				for (String z : zonesNow) {
					if (zonesPrev.contains(z)) continue;
					if (zoneFlag(z, "hidden").equals("")) {
						p.sendMessage(colorize("&e--- Entering " + (zoneFlag(z, "friendlyname").equals("") ? z : zoneFlag(z, "friendlyname")) + " ---"));
						if (config("zonedata").contains(z + ".resetminutes")) {
							int resetminutes = 0;
							try { resetminutes = Integer.parseInt(config("zonedata").getString(z + ".resetminutes")); } catch (Exception e) {}
							long resetlast = config("zonedata").getLong(z + ".resetlast", 0);
							String timeLeft = v.timeLeftShort(resetminutes * 60000 - (new Date().getTime() - resetlast));
							p.sendMessage(colorize("&e--- This area regenerates every " + resetminutes + " minutes / next reset in " + timeLeft + " ---"));
						}
					}
					for (Player pp : v.getServer().getOnlinePlayers()) {
						if (pp.equals(p) && zoneFlag(z, "hidden").equals("")) continue;
						String zonespy = v.playerSetting(pp.getName(), "zonespy");
						if (zonespy.equals("all") || (zonespy.equals("me") && p.equals(pp))) {
							pp.sendMessage(colorize("&8" + p.getName() + " entered [" + z + "]"));
						}
					}
				}
				pzones.put(p.getName(), zonesNow);
			}
		}
	}
	void jobElevators(long step) {
		Material m = Material.matchMaterial(v.getConfig().getString("zonecontrol_elevatorblock", ""));
		if (m == null) return;
		for (String zone : config("zonedata").getKeys(false)) if (zoneFlag(zone, "elevator").equals("true")) {
			for (Player p : v.getServer().getOnlinePlayers()) if (zonesContaining(p.getLocation()).contains(zone)) {
				p.teleport(p.getLocation().add(0, 1, 0));
			}
			Location zmin = v.stringLocation(zoneFlag(zone, "locmin"));
			Location zmax = v.stringLocation(zoneFlag(zone, "locmax"));
			for (int y = zmin.getBlockY(); y <= zmax.getBlockY(); y++) {
				Material mm = y % 5 == step % 5 || y == zmin.getBlockY() ? m : Material.AIR;
				for (int x = zmin.getBlockX(); x <= zmax.getBlockX(); x++) for (int z = zmin.getBlockZ(); z <= zmax.getBlockZ(); z++) {
					new Location(zmin.getWorld(), x, y, z).getBlock().setType(mm);
				}
			}
		}
	}
	int jobRemoveDisabledEntities() {
		int numRemoved = 0;
		for (World w : v.getServer().getWorlds()) {
			for (Entity e : w.getEntities()) {
				for (String zone : zonesContaining(e.getLocation())) {
					if (zoneFlag(zone, "autospawn").equalsIgnoreCase(e.getType().toString())) continue;
					if (e instanceof Monster && zoneFlag(zone, "nomonsters").equals("true")) { numRemoved++; e.remove(); }
					if (e instanceof Animals && zoneFlag(zone, "noanimals").equals("true")) { numRemoved++; e.remove(); }
				}
			}
		}
		return numRemoved;
	}
	String jobRestoreInterval() {
		for (String zone : config("zonedata").getKeys(false)) {
			int resetminutes = 0;
			try { resetminutes = Integer.parseInt(zoneFlag(zone, "resetminutes")); }
			catch (Exception e) { }
			if (resetminutes == 0) continue;
			if (!config("zonedata").contains(zone + ".resetlast")) config("zonedata").set(zone + ".resetlast", 0);
			long resetlast = config("zonedata").getLong(zone + ".resetlast");
			if ((new Date().getTime() - resetlast)/60000 >= resetminutes) {
				config("zonedata").set(zone + ".resetlast", new Date().getTime());
				v.saveMyConfig("zonedata");
				v.task("zonerestorebatch", zoneRestoreBatch(zone, null));
				return zone + " (" + resetminutes + "min cycle)";
			}
		}
		return "";
	}
	String jobRegenerateOneMine() {
		List<String> mines = new ArrayList<String>();
		for (String zone : config("zonedata").getKeys(false)) {
			int regenrate = 0;
			try { regenrate = Integer.parseInt(zoneFlag(zone, "regenrate")); } catch (Exception e) { }
			if (regenrate > 0 && !zoneFlag(zone, "regendist").equals("")) mines.add(zone);
		}
		if (mines.size() == 0) return "";
		String mine = mines.get(v.random.nextInt(mines.size()));
		String result = zoneRegenMine(mine);
		return mine + result;
	}
	String jobRegenerateOthers(String zonetype) {
		StringBuilder sb = new StringBuilder();
		for (String zone : config("zonedata").getKeys(false)) {
			int regenrate = 0;
			try { regenrate = Integer.parseInt(zoneFlag(zone, "regenrate")); } catch (Exception e) { }
			if (regenrate < 1) continue;
			if (zonetype.equals("orchards") && !zoneFlag(zone, "orchardtype").equals("")) { sb.append(zoneRegenOrchard(zone)); }
			if (zonetype.equals("fields") && !zoneFlag(zone, "fieldtype").equals("")) { sb.append(zoneRegenField(zone)); }
			if (zonetype.equals("autospawns") && !zoneFlag(zone, "autospawn").equals("")) { zoneAutospawn(zone); }
		}
		return sb.toString();
	}
	void jobUpdateSigns() {
		for (String zone : config("zonedata").getKeys(false)) {
			if (!zoneFlag(zone, "signloc").equals("")) {
				String guildNeeded = zoneFlag(zone, "entryguild");
				int rankNeeded = zoneFlag(zone, "entryguildrank").equals("") ? 0 : Integer.parseInt(zoneFlag(zone, "entryguildrank"));
				String access1 = "&aPUBLIC ACCESS", access2 = "", access3 = "";
				if (rankNeeded > 0) { access1 = "&4&oRESTRICTED"; access2 = "Any Guild"; access3 = "Rank " + rankNeeded; }
				if (!guildNeeded.equals("")) { access1 = "&4&oRESTRICTED"; access2 = guildNeeded + " Guild"; }
				Block wallBlock = v.stringLocation(zoneFlag(zone, "signloc")).getBlock();
				BlockFace signface = BlockFace.valueOf(config("zonedata").getString(zone + ".signface"));
				String depleted = ""; String regenrate = "RegenRate " + zoneFlag(zone, "regenrate");
				List<String> top = new ArrayList<String>();
				if (!zoneFlag(zone, "regendist").equals("")) {
					int numShown = 0, numToShow = 8;
					TreeMap<String,Double> sorted_map = zoneDistribution(zone);
			        for (String key : sorted_map.keySet()) if (numShown++ < numToShow) {
			        	top.add(Math.round(sorted_map.get(key)) + " " + key.replaceAll("_","").toLowerCase());
			        }
				}
				for (int i = top.size(); i < 8; i++) top.add("");
				int nullExceptions = 0;
				for (int y = 3; y >= 0; y--) {
					Block target = wallBlock.getRelative(0, y, 0);
					target.setType(Material.WALL_SIGN);
					Sign sign = null;
					try {
						if (!(target.getState() instanceof Sign)) continue;
						sign = (Sign)target.getState();
					}
					catch (NullPointerException e) {
						nullExceptions++; continue;
					}
					org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
					signData.setFacingDirection(signface);
					sign.update();
					switch (y) {
					case 3: signWrite(sign, colorize("&n" + zone), colorize(access1), access2, access3); break;
					case 2: signWrite(sign, top.get(0), top.get(1), top.get(2), top.get(3)); break;
					case 1: signWrite(sign, top.get(4), top.get(5), top.get(6), top.get(7)); break;
					case 0: signWrite(sign, regenrate, "", "", depleted); break;
					}
				}
				if (nullExceptions > 0) {
					v.log.warning("Encountered " + nullExceptions + " NullPointerExceptions while updating stat signs for [" + zone + "]");
				}
			}
		}
	}
	void zoneCmd(Player p, String[] args) {
		if (args.length == 0) {
			v.showUsage(p, "zone");
		} else if (has(args, 0, "list")) {
			StringBuilder sb = new StringBuilder();
			for (String zone : config("zonedata").getKeys(false)) {
				sb.append(config("zonedata").contains(zone + ".resetminutes") ? "&a" : "&7");
				sb.append(" "); sb.append(zone);
			}
			p.sendMessage(colorize("&cZones:" + sb.toString()));
			p.sendMessage("Your current location is within these zones: " + zonesContaining(p.getLocation()).toString());
		} else if (has(args, 0, "setup")) {
			v.setupNew(p, "zone", args[1], args.length >= 3 ? args[2] : "");
		} else if (config("zonedata").getKeys(false).contains(args[0])) {
			if (has(args, 1, "delete")) {
				config("zonedata").set(args[0], null);
				v.saveMyConfig("zonedata");
				p.sendMessage("Zone '" + args[0] + "' was deleted.");
			} else if (has(args, 1, "rename")) {
				for (String key : config("zonedata").getConfigurationSection(args[0]).getKeys(false)) {
					config("zonedata").set(args[2] + "." + key, config("zonedata").get(args[0] + "." + key));
				}
				config("zonedata").set(args[0], null); v.saveMyConfig("zonedata");
				p.sendMessage("Zone " + args[0] + " has been renamed to " + args[2]);
			} else if (has(args, 1, "vertical")) {
				Location min = v.stringLocation(zoneFlag(args[0], "locmin")), max = v.stringLocation(zoneFlag(args[0], "locmax"));
				min.setY(0); max.setY(max.getWorld().getMaxHeight() - 1);
				config("zonedata").set(args[0] + ".locmin", v.locationString(min.getBlock()));
				config("zonedata").set(args[0] + ".locmax", v.locationString(max.getBlock()));
				v.saveMyConfig("zonedata");
				p.sendMessage("Zone '" + args[0] + "' has been expanded from bedrock to sky.");
			} else if (has(args, 1, "eraseallblocks")) {
				Location min = v.stringLocation(zoneFlag(args[0], "locmin")), max = v.stringLocation(zoneFlag(args[0], "locmax"));
				for (int x = min.getBlockX(); x <= max.getX(); x++) for (int y = min.getBlockY(); y <= max.getY(); y++) for (int z = min.getBlockZ(); z <= max.getZ(); z++) {
					new Location(min.getWorld(), x, y, z).getBlock().setType(Material.AIR);
				}
				p.sendMessage("Zone " + args[0] + " has been wiped.");
			} else if (has(args, 1, "info")) {
				p.sendMessage(colorize("&cZone '" + args[0] + "'"));
				Location min = v.stringLocation(zoneFlag(args[0], "locmin")), max = v.stringLocation(zoneFlag(args[0], "locmax"));
				int kvolume = (int) Math.ceil((max.getX() - min.getX() + 1) * (max.getY() - min.getY() + 1) * (max.getZ() - min.getZ() + 1) / 1000.0);
				p.sendMessage("volume: " + (kvolume <= 1 ? "<=" : "") + kvolume + "k blocks");
				for (String key : config("zonedata").getConfigurationSection(args[0]).getKeys(false)) {
					if (key.equals("resetlast")) {
						long resetLast = config("zonedata").getLong(args[0]+".resetlast");
						p.sendMessage(key + ": " + resetLast + " / " + v.timestamp(new Date(resetLast)) + " / " + v.timeLeftShort(new Date().getTime() - resetLast) + " ago");
					} else {
						p.sendMessage(key + ": " + config("zonedata").getString(args[0] + "." + key));
					}
				}
			} else if (has(args, 1, "set") && args.length == 4) {
				config("zonedata").set(args[0] + "." + args[2], args[3].equals("null") ? null : args[3]);
				v.saveMyConfig("zonedata");
				p.sendMessage("Zone '" + args[0] + "' changed setting " + args[2] + " = " + args[3]);
			} else if (has(args, 1, "tpmin")) {
				p.sendMessage("Teleporting you to min of zone '" + args[0] + "'...");
				p.teleport(v.stringLocation(config("zonedata").getString(args[0] + ".locmin")));
			} else if (has(args, 1, "tpmax")) {
				p.sendMessage("Teleporting you to max of zone '" + args[0] + "'...");
				p.teleport(v.stringLocation(config("zonedata").getString(args[0] + ".locmax")));
			} else if (has(args, 1, "snapshot")) {
				v.deleteConfig("zonesnapshot_" + args[0]);
				v.task("zonesnapshotbatch", zoneSnapshotBatch(args[0], args.length >= 3 ? args[2] : "", p));
			} else if (has(args, 1, "restore")) {
				if (v.taskExists("zonerestorebatch")) p.sendMessage("Canceled restore operation in progress to start new restore...");
				v.task("zonerestorebatch", zoneRestoreBatch(args[0], p));
			} else if (has(args, 1, "signs")) {
				List<Block> los = p.getLineOfSight(null, 50);
				Block wall = los.get(los.size()-1), target = los.get(los.size()-2);
				BlockFace signface = null;
				for (BlockFace bf : BlockFace.values()) if (target.getRelative(bf).equals(wall)) signface = bf.getOppositeFace();
				config("zonedata").set(args[0] + ".signloc", v.locationString(target));
				config("zonedata").set(args[0] + ".signface", signface.toString());
				v.saveMyConfig("zonedata"); p.sendMessage("Zone sign location saved."); return;
			} else {
				v.showUsage(p, "zone");
			}
		} else {
			p.sendMessage("No zone by that name."); v.showUsage(p, "zone");
		}
	}
	void zoneAutospawn(String zone) {
		Location lmin = v.stringLocation(config("zonedata").getString(zone + ".locmin"));
		Location lmax = v.stringLocation(config("zonedata").getString(zone + ".locmax"));
		Location center = new Location(lmin.getWorld(), (lmax.getX()+lmin.getX())/2, 1+(lmax.getY()+lmin.getY())/2, (lmax.getZ()+lmin.getZ())/2);
		EntityType et;
		try { et = EntityType.valueOf(zoneFlag(zone, "autospawn")); } catch (IllegalArgumentException e) { return; }
		int regenrate = Integer.parseInt(zoneFlag(zone, "regenrate"));
		String spawntrack = zoneFlag(zone, "spawntrack");
		if (!spawntrack.equals("")) {
			try {
				List<Entity> oldarmy = armies.containsKey(zone) ? armies.get(zone) : new ArrayList<Entity>();
				List<Entity> army = new ArrayList<Entity>();
				for (Entity ent : oldarmy) if (!ent.isDead()) {
					army.add(ent);
					//if (loc.distanceSquared(ent.getLocation()) > 400) ent.teleport(loc);
				}
				if (army.size() >= regenrate) {
					v.taskdebug("[spawn:" + zone + "] has " + army.size() + " entities");
				} else {
					v.taskdebug("[spawn:" + zone + "] spawning " + (regenrate - army.size()) + " entities");
						for (int i = 0; i < regenrate - army.size(); i++) {
							int rx = v.random.nextInt(lmax.getBlockX() - lmin.getBlockX()) + lmin.getBlockX();
							int rz = v.random.nextInt(lmax.getBlockZ() - lmin.getBlockZ()) + lmin.getBlockZ();
							int ry = 2 + v.getHighestFreeBlockAt(lmin.getWorld(), rx, rz);
							Location loc = new Location(lmin.getWorld(), rx, ry, rz);
							army.add(loc.getWorld().spawnEntity(loc, et));
						}
						armies.put(zone, army);
				}
			}
			catch (Exception e) {
				v.taskdebug("[spawn:" + zone + "] invalid npctype or npcamount");
			}
		} else {
			for (int i = 0; i < regenrate; i++) center.getWorld().spawnEntity(center, et);
			if (zoneFlag(zone, "autospawn").equals("SHEEP")) {
				for (int y = lmin.getBlockY(); y <= lmax.getBlockY(); y++) {
					for (int x = lmin.getBlockX(); x <= lmax.getBlockX(); x++) {
						for (int z = lmin.getBlockZ(); z <= lmax.getBlockZ(); z++) {
							Block b = new Location(lmin.getWorld(), x, y, z).getBlock();
							if (b.getType() == Material.DIRT) b.setType(Material.GRASS);
						}
					}
				}
			}
		}
	}
	TreeMap<String,Double> zoneDistribution(String zone) {
		Location lmin = v.stringLocation(zoneFlag(zone, "locmin")), lmax = v.stringLocation(zoneFlag(zone, "locmax"));
		HashMap<String,Double> map = new HashMap<String,Double>();
		for (int x = lmin.getBlockX(); x <= lmax.getBlockX(); x++) {
			for (int y = lmin.getBlockY(); y <= lmax.getBlockY(); y++) {
				for (int z = lmin.getBlockZ(); z <= lmax.getBlockZ(); z++) {
					Material m = new Location(lmin.getWorld(), x, y, z).getBlock().getType();
					map.put(m.toString(), map.containsKey(m.toString()) ? map.get(m.toString()) + 1.0D : 1.0D);
				}
			}
		}
        return v.mapSort(map);
	}
	void zoneEject(Player p, String zone) {
		Location lmin = v.stringLocation(zoneFlag(zone, "locmin"));
		Location lmax = v.stringLocation(zoneFlag(zone, "locmax"));
		Location center = new Location(lmin.getWorld(), (lmax.getX()+lmin.getX())/2, (lmax.getY()+lmin.getY())/2, (lmax.getZ()+lmin.getZ())/2);
		Location l = p.getLocation();
		int dx = l.getX() > center.getX() ? 3 : -3, dz = l.getZ() > center.getZ() ? 3 : -3;
		while (zonesContaining(l).contains(zone)) l.add(dx, 0, dz);
		p.teleport(l); p.sendMessage("You don't have access to go there.");
	}
    String zoneFlag(String zone, String setting) {
		return zone == null ? "" : config("zonedata").getString(zone + "." + setting, "");
	}
	String zoneRegenField(String zone) {
		int totalchanged = 0;
		Location lmin = v.stringLocation(config("zonedata").getString(zone + ".locmin"));
		Location lmax = v.stringLocation(config("zonedata").getString(zone + ".locmax"));
		Material m = Material.getMaterial(zoneFlag(zone, "fieldtype"));
		Material underBlock = Material.SOIL;
		if (m == Material.SUGAR_CANE_BLOCK || m == Material.PUMPKIN || m == Material.MELON_BLOCK) underBlock = Material.GRASS;
		if (m == Material.NETHER_WARTS) underBlock = Material.SOUL_SAND;
		Material underBlock2 = underBlock == Material.GRASS ? Material.DIRT : null;
		if (m == Material.CACTUS) underBlock = Material.SAND;
		if (m == null) { v.log.warning("Invalid [zonecontrol] setting 'fieldtype' for field " + zone); return ""; }
		int maxToChange = Integer.parseInt(zoneFlag(zone, "regenrate"));
		for (int y = lmin.getBlockY(); y <= lmax.getBlockY(); y++) {
			for (int x = lmin.getBlockX(); x <= lmax.getBlockX(); x++) {
				for (int z = lmin.getBlockZ(); z <= lmax.getBlockZ(); z++) {
					if (totalchanged > maxToChange) break;
					if (v.random.nextInt(2) == 0) continue;
					Block b = new Location(lmin.getWorld(), x, y, z).getBlock();
					if (b.getType() != Material.AIR || (b.getRelative(BlockFace.DOWN).getType() != underBlock && b.getRelative(BlockFace.DOWN).getType() != underBlock2)) continue;
					if (m == Material.CACTUS && ((x + z) % 2 == 0)) continue;
					totalchanged++;
					if (m != null) {
						b.setType(m);
						if (m == Material.CROPS || m == Material.CARROT || m == Material.POTATO || m == Material.NETHER_WARTS) {
							b.setTypeIdAndData(m.getId(), (byte) 7, true);
						}
						if (m == Material.CACTUS || m == Material.SUGAR_CANE_BLOCK) {
							b.getRelative(BlockFace.UP).setType(m); b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(m);
						}
					}
				}
			}
		}
		return totalchanged == 0 ? "" : " " + zone + " (" + totalchanged + ")";
	}
    String zoneRegenMine(String zone) {
		for (Player p : v.getServer().getOnlinePlayers()) if (zonesContaining(p.getLocation()).contains(zone)) return "";
		TreeMap<String,Double> currentDist = zoneDistribution(zone);
		Location lmin = v.stringLocation(config("zonedata").getString(zone + ".locmin"));
		Location lmax = v.stringLocation(config("zonedata").getString(zone + ".locmax"));
		Double volume = (lmax.getX() - lmin.getX() + 1) * (lmax.getY() - lmin.getY() + 1) * (lmax.getZ() - lmin.getZ() + 1);
		int totaldist = 0, totalchanged = 0, regenrate = Integer.parseInt(zoneFlag(zone, "regenrate"));
		String[] dist = zoneFlag(zone, "regendist").split(","); Material commonMaterial = null; int maxPct = 0;
		HashMap<String,Integer> bdist = new HashMap<String,Integer>();
		boolean understocked = false;
		for (int i = 0; i < dist.length; i++) {
			String[] s = dist[i].split("%");
			try {
				int s0 = (int)(Double.parseDouble(s[0]) * 10); bdist.put(s[1], s0); totaldist += s0;
				if (s0 > maxPct) { maxPct = s0; commonMaterial = Material.getMaterial(s[1].toUpperCase()); }
				else if (currentDist.get(s[0].toUpperCase()) < s0 * 0.1 * volume) understocked = true;
			}
			catch (Exception e) { }
		}
		String last = "";
		for (int y = lmin.getBlockY(); y <= lmax.getBlockY(); y++) {
			for (int x = lmin.getBlockX(); x <= lmax.getBlockX(); x++) {
				for (int z = lmin.getBlockZ(); z <= lmax.getBlockZ(); z++) {
					if (totalchanged > regenrate) break;
					if (v.random.nextInt(2) == 0) continue;
					Block b = new Location(lmin.getWorld(), x, y, z).getBlock();
					if (bdist.containsKey(b.getType().toString().toLowerCase())) {
						if (!understocked || b.getType() != commonMaterial) continue;
					}
					totalchanged++;
					int choice = v.random.nextInt(totaldist), itotal = 0; Material m = null;
					for (String s : bdist.keySet()) {
						if (choice < itotal + bdist.get(s)) { m = Material.getMaterial(s.toUpperCase()); break; }
						else { itotal += bdist.get(s); }
					}
					if (m != null) { last = b.getType().toString() + "->" + m.toString(); b.setType(m); }
				}
			}
		}
		return totalchanged == 0 ? "" : " " + zone + " (" + totalchanged + " " + last + ")";
	}
    String zoneRegenOrchard(String zone) {
		int totalchanged = 0;
		Location lmin = v.stringLocation(config("zonedata").getString(zone + ".locmin"));
		Location lmax = v.stringLocation(config("zonedata").getString(zone + ".locmax"));
		for (int y = lmin.getBlockY(); y <= lmax.getBlockY(); y++) {
			for (int x = lmin.getBlockX() + 3; x <= lmax.getBlockX() - 3; x++) {
				for (int z = lmin.getBlockZ() + 3; z <= lmax.getBlockZ() - 3; z++) {
					if (totalchanged > Integer.parseInt(zoneFlag(zone, "regenrate"))) break;
					if (v.random.nextInt(2) == 0) continue;
					Block b = new Location(lmin.getWorld(), x, y, z).getBlock();
					if (b.getType() != Material.AIR || (b.getRelative(BlockFace.DOWN).getType() != Material.GRASS && b.getRelative(BlockFace.DOWN).getType() != Material.DIRT)) continue;
					totalchanged++;
					b.setTypeIdAndData(Material.SAPLING.getId(), (byte)Integer.parseInt(zoneFlag(zone, "orchardtype")), true);
					Block bb = b;
					for (int yy = y + 1; yy <= lmax.getBlockY(); yy++) {
						bb = bb.getRelative(BlockFace.UP); bb.setType(Material.AIR);
					}
				}
			}
		}
		return totalchanged == 0 ? "" : " " + zone + " (" + totalchanged + ")";
	}
	int zoneRestoreBatch(final String zone, final Player notify) {
		File f = new File(v.getDataFolder() + File.separator + "zonesnapshots", zone + ".yml");
		if (!f.exists()) { notify.sendMessage("There is no snapshot available for the zone '" + zone + "'"); return -1; }
		final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		v.log.info("[zones] restoring zone " + zone + "...");
		return v.getServer().getScheduler().scheduleSyncRepeatingTask(v, new Runnable() {
			World w = v.getServer().getWorld(cfg.getString("world"));
			int blockNum = 0, n = 0, blocksPer = v.getConfig().getInt("global_maximumsimultaneousblockchanges");
			Double minx = cfg.getDouble("minx"), miny = cfg.getDouble("miny"), minz = cfg.getDouble("minz");
			boolean didAir = false, didStone = false, didDirt = false, didWater = false, didGrass = false, didSand = false, didGravel = false, didBedrock = false, didLava = false;
			String[] air = cfg.getString("air").split(","), stone = cfg.getString("stone").split(","), dirt = cfg.getString("dirt").split(",");
			String[] water = cfg.getString("water").split(","), grass = cfg.getString("grass").split(","), sand = cfg.getString("sand").split(",");
			String[] gravel = cfg.getString("gravel").split(","), bedrock = cfg.getString("bedrock").split(","), lava = cfg.getString("lava").split(",");
			public void run() {
				int i = 0, j; Block b; String[] data, coords; Integer blockType; byte blockData; List<ItemStack> contents = new ArrayList<ItemStack>(), c;
				if (!didAir && air.length > 1) { didAir = true; n += j = zonerestoreArray(w, air, Material.AIR, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				if (!didStone && stone.length > 1) { didStone = true; n += j = zonerestoreArray(w, stone, Material.STONE, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				if (!didDirt && dirt.length > 1) { didDirt = true; n += j = zonerestoreArray(w, dirt, Material.DIRT, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				if (!didWater && water.length > 1) { didWater = true; n += j = zonerestoreArray(w, water, Material.STATIONARY_WATER, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				if (!didGrass && grass.length > 1) { didGrass = true; n += j = zonerestoreArray(w, grass, Material.GRASS, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				if (!didSand && sand.length > 1) { didSand = true; n += j = zonerestoreArray(w, sand, Material.SAND, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				if (!didGravel && gravel.length > 1) { didGravel = true; n += j = zonerestoreArray(w, gravel, Material.GRAVEL, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				if (!didBedrock && bedrock.length > 1) { didBedrock = true; n += j = zonerestoreArray(w, bedrock, Material.BEDROCK, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				if (!didLava && lava.length > 1) { didLava = true; n += j = zonerestoreArray(w, lava, Material.STATIONARY_LAVA, minx, miny, minz, notify); if (n > blocksPer && j > 0) return; }
				for (String pos : cfg.getConfigurationSection("data").getKeys(false)) if (!pos.contains("contents")) {
					data = cfg.getString("data."+pos).split("_");
					coords = pos.split("_");
					blockType = Integer.parseInt(data[0]); blockData = (byte)Integer.parseInt(data[1]);
					b = new Location(w, minx+Double.parseDouble(coords[0]), miny+Double.parseDouble(coords[1]), minz+Double.parseDouble(coords[2])).getBlock();
					//v.getServer().broadcastMessage("i:" + i + ", bType:" + b.getType() + ", fType:" + blockType + ", bData:" + b.getData() + ", fData:" + blockData);
					if (b.getTypeId() != blockType || b.getData() != blockData || b.getState() instanceof InventoryHolder) {
						if (++i < blockNum) continue;
						if (i >= blockNum + blocksPer) { blockNum = i; if (notify != null) notify.sendMessage("restored " + i + " miscellaneous blocks so far..."); return; }
						b.setTypeIdAndData(blockType, blockData, false);
						if (b.getState() instanceof InventoryHolder) {
							contents.clear(); for (String item : cfg.getStringList("data."+pos+"_contents")) contents.add(new CardboardBox(item).unbox());
							if (notify != null) notify.sendMessage("restoring " + b.getType() + " contents - " + contents.size() + " itemstacks");
							Inventory inv = ((InventoryHolder)b.getState()).getInventory(); inv.clear(); inv.addItem(contents.toArray(new ItemStack[0]));
							c = Arrays.asList(inv.getContents()); Collections.shuffle(c); inv.setContents(c.toArray(new ItemStack[0]));
						}
					}
				}
				v.log.info("[zones] zone restore complete");
				if (notify != null) notify.sendMessage("FINISHED! The zone '" + zone + "' has been restored (" + (n+i) + " blocks changed)");
				v.task("zonerestorebatch", -1);
			}
		}, 0L, 100L);
	}
	List<String> zonesContaining(Location l) {
		List<String> r = new ArrayList<String>();
		Location lmin, lmax; int border;
		for (String zone : config("zonedata").getKeys(false)) {
			lmin = v.stringLocation(config("zonedata").getString(zone + ".locmin"));
			lmax = v.stringLocation(config("zonedata").getString(zone + ".locmax"));
			border = zone.contains("Mine") || zone.contains("Orchard") ? 6 : zone.contains("Field") ? 1 : 0;
			if (l.getWorld().equals(lmin.getWorld())) {
				if (l.getX() >= lmin.getX() - border && l.getX() < lmax.getX() + 1 + border) {
					if (l.getY() >= lmin.getY() - border && l.getY() < lmax.getY() + 1 + border) {
						if (l.getZ() >= lmin.getZ() - border && l.getZ() < lmax.getZ() + 1 + border) {
							r.add(zone);
						}
					}
				}
			}
		}
		return r;
	}
	private int zonerestoreArray(World w, String[] array, Material m, Double minx, Double miny, Double minz, Player notify) {
		Block b; int blockNum = 0;
		for (int i = 0; i < array.length; i+=3) {
			b = new Location(w, minx+Double.parseDouble(array[i]), miny+Double.parseDouble(array[i+1]), minz+Double.parseDouble(array[i+2])).getBlock();
			if (b.getType() != m) { blockNum++; b.setTypeIdAndData(m.getId(), (byte)0, false); }
		}
		if (notify != null && blockNum > 0) notify.sendMessage("restored " + blockNum + " " + m.toString().toLowerCase() + " blocks...");
		return blockNum;
	}

	private int zoneSnapshotBatch(final String zone, final String option, final Player notify) {
		final File f = new File(v.getDataFolder() + File.separator + "zonesnapshots", zone + ".yml");
		f.getParentFile().mkdirs();
		return v.getServer().getScheduler().scheduleSyncRepeatingTask(v, new Runnable() {
			YamlConfiguration cfg = new YamlConfiguration();
			Block b1 = v.stringLocation(config("zonedata").getString(zone + ".locmin")).getBlock();
			Block b2 = v.stringLocation(config("zonedata").getString(zone + ".locmax")).getBlock();
			Double x1 = (double) b1.getX(), y1 = (double) b1.getY(), z1 = (double) b1.getZ(), x2 = (double) b2.getX(), y2 = (double) b2.getY(), z2 = (double) b2.getZ();
			Double minx = Math.min(x1,x2), miny = Math.min(y1,y2), minz = Math.min(z1,z2), maxx = Math.max(x1,x2), maxy = Math.max(y1,y2), maxz = Math.max(z1,z2);
			World w = b1.getWorld(); int blockNum = 0, blocksPer = v.getConfig().getInt("global_maximumsimultaneousblockchanges");
			StringBuilder air = new StringBuilder(), stone = new StringBuilder(), dirt = new StringBuilder(), water = new StringBuilder(), grass = new StringBuilder(), sand = new StringBuilder(), gravel = new StringBuilder(), bedrock = new StringBuilder(), lava = new StringBuilder();
			public void run() {
				int i = 0;
				if (blockNum == 0) {
					cfg.set("world", b1.getWorld().getName());
					cfg.set("minx", minx);
					cfg.set("miny", miny);
					cfg.set("minz", minz);
				}
				for (long x = 0; x <= maxx-minx; x++) for (long y = 0; y <= maxy-miny; y++) for (long z = 0; z <= maxz-minz; z++) {
					Block b = new Location(w, minx+x, miny+y, minz+z).getBlock();
					if (!option.equals("inventory") || b.getState() instanceof InventoryHolder) {
						if (++i < blockNum) continue;
						if (i >= blockNum + blocksPer) { blockNum = i; if (i % (blocksPer * 5) == 0) notify.sendMessage("saved " + (i/1000) + "k blocks so far..."); return; }
						if (b.getType() == Material.AIR) { if (air.length() > 0) air.append(","); air.append(x); air.append(","); air.append(y); air.append(","); air.append(z); continue; }
						if (b.getType() == Material.STONE) { if (stone.length() > 0) stone.append(","); stone.append(x); stone.append(","); stone.append(y); stone.append(","); stone.append(z); continue; }
						if (b.getType() == Material.DIRT) { if (dirt.length() > 0) dirt.append(","); dirt.append(x); dirt.append(","); dirt.append(y); dirt.append(","); dirt.append(z); continue; }
						if (b.getType() == Material.STATIONARY_WATER) { if (water.length() > 0) water.append(","); water.append(x); water.append(","); water.append(y); water.append(","); water.append(z); continue; }
						if (b.getType() == Material.GRASS) { if (grass.length() > 0) grass.append(","); grass.append(x); grass.append(","); grass.append(y); grass.append(","); grass.append(z); continue; }
						if (b.getType() == Material.SAND) { if (sand.length() > 0) sand.append(","); sand.append(x); sand.append(","); sand.append(y); sand.append(","); sand.append(z); continue; }
						if (b.getType() == Material.GRAVEL) { if (gravel.length() > 0) gravel.append(","); gravel.append(x); gravel.append(","); gravel.append(y); gravel.append(","); gravel.append(z); continue; }
						if (b.getType() == Material.BEDROCK) { if (bedrock.length() > 0) bedrock.append(","); bedrock.append(x); bedrock.append(","); bedrock.append(y); bedrock.append(","); bedrock.append(z); continue; }
						if (b.getType() == Material.STATIONARY_LAVA) { if (lava.length() > 0) lava.append(","); lava.append(x); lava.append(","); lava.append(y); lava.append(","); lava.append(z); continue; }
						StringBuilder key = new StringBuilder(), val = new StringBuilder();
						key.append("data."); key.append(x); key.append("_"); key.append(y); key.append("_"); key.append(z);
						val.append(b.getTypeId()); val.append("_"); val.append(b.getData());
						cfg.set(key.toString(), val.toString());
						if (b.getState() instanceof InventoryHolder) {
							Inventory inv = ((InventoryHolder)b.getState()).getInventory(); List<String> contents = new ArrayList<String>();
							for (ItemStack is : inv.getContents()) if (is != null) contents.add(new CardboardBox(is).toString());
									cfg.set(key + "_contents", contents); notify.sendMessage("saved " + b.getType() + " inventory (" + contents.size() + " itemstacks)");
						}
					}
				}
				cfg.set("air", air.toString()); cfg.set("stone", stone.toString()); cfg.set("dirt", dirt.toString());
				cfg.set("water", water.toString()); cfg.set("grass", grass.toString()); cfg.set("sand", sand.toString());
				cfg.set("gravel", gravel.toString()); cfg.set("bedrock", bedrock.toString()); cfg.set("lava", lava.toString());
				try { cfg.save(f); }
				catch (IOException e) { v.log.severe("IO Error while saving file 'zonesnapshots" + File.separator + zone + ".yml' to plugin data folder."); e.printStackTrace(); }
				notify.sendMessage("FINISHED! The zone '" + zone + "' has been saved. (total " + i + " blocks)");
				v.endTask("zonesnapshotbatch");
			}
		}, 0L, 4L);
	}
	void zonespy(Player p, String arg) {
		if (arg == null) {
			p.sendMessage("/zonespy [me,all,none]");
		} else {
			String val = (arg.equals("all") || arg.equals("me")) ? arg : null;
			config("users").set(p.getName() + ".zonespy", val);
			v.saveMyConfig("users");
			p.sendMessage(colorize("&7ZoneSpy " + (val == null ? "disabled" : "set to: " + val)));
		}
	}
	
	void signWrite(Sign sign, String line1, String line2, String line3, String line4) {
		sign.setLine(0, colorize(line1)); sign.setLine(1, colorize(line2));
		sign.setLine(2, colorize(line3)); sign.setLine(3, colorize(line4)); sign.update(); 
	}
	
	String colorize(String string) { return string.replaceAll("(?i)&([a-r0-9])", "\u00A7$1"); }
    YamlConfiguration config(String configName) {
		if (!configs.containsKey(configName)) configs.put(configName, YamlConfiguration.loadConfiguration(new File(getDataFolder(), configName + ".yml")));
		return configs.get(configName);
	}*/
}
