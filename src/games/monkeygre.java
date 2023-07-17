package games;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import arcade.gameStarter;
import arcade.main;
import utils.ItemUtil;

public class monkeygre {
	static HashMap<UUID,Integer> monkeys = new HashMap<>();
	static int timer = 0;
	public static void start(){
		if(main.hard)main.pvp=true;
		timer=30;
		main.timer=60;
		main.stage="game";
		main.lavakill=true;
	}
	public static void base(){
		timer+=main.players.size();
		if(timer>=30){
			spawnMonkey();
			timer-=30;
		}
		for(UUID st:monkeys.keySet()){
			if(monkeys.get(st)<=0){
				monkeyExplode(Bukkit.getEntity(st));
				return;
			}
			monkeys.replace(st,monkeys.get(st)-1);
		}
		if(main.timer<=0||main.players.size()<=1){
			for(Entity en:Bukkit.getWorld("world").getEntities()){
				if(!(en instanceof Player)){
					en.remove();
				}
			}
			monkeys.clear();
			gameStarter.end("явно умнее обезь€ны...","’орошо, что им калаш не дали...",new String[]{
					ChatColor.GOLD+"("+ChatColor.DARK_GREEN+"ќбезь€на с гранатой"+ChatColor.GOLD+")",
					"/wons/"
			},true,null);
		}
	}
	static void spawnMonkey(){
		Location loc = main.randLocFromPlatform(main.platformSize, true);
		Zombie z = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		z.getEquipment().setBoots(ItemUtil.create(Material.LEATHER_BOOTS, null));
		z.getEquipment().setLeggings(ItemUtil.create(Material.LEATHER_LEGGINGS, null));
		z.getEquipment().setChestplate(ItemUtil.create(Material.LEATHER_CHESTPLATE, null));
		z.getEquipment().setHelmet(ItemUtil.create(Material.LEATHER_HELMET, null));
		z.getEquipment().setItemInMainHand(ItemUtil.create(Material.TNT, null));
		z.setBaby(true);
		z.setCustomName(ChatColor.DARK_GREEN+"ќбезь€на с гранатой");
		monkeys.put(z.getUniqueId(),new Random().nextInt(16)+4);
	}
	static void monkeyExplode(Entity en){
		Location loc = en.getLocation();
		en.getWorld().createExplosion(loc.getX(), loc.getY()+1, loc.getZ(), 3, false, false);
		en.remove();
		monkeys.remove(en.getUniqueId());
	}
}
