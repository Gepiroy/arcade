package games;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import arcade.gameStarter;
import arcade.main;
import utils.ItemUtil;

public class reaction {
	static String[] targs = {"Прыгни!","Ударь другого игрока!","Кликни на алмазный блок!","Shift!"};
	public static String target = "";
	static int timer = 0;
	static List<String> wons = new ArrayList<>();
	public static void base(){
		if(target.equals("Shift!")){
			for(String st:main.players){
				Player p = Bukkit.getPlayer(st);
				if(p.isSneaking()){
					compl(p);
				}
			}
		}
		if(timer<=0){
			change();
			if(main.timer>=12){
				timer=3;
			}
			if(main.timer<=11&&main.timer>=5){
				timer=2;
			}
			if(main.timer<=4){
				timer=1;
			}
		}
		timer--;
		if(main.players.size()>=2&&main.timer<=0||main.players.size()<=1){
			end();
			return;
		}
	}
	static void end(){
		gameStarter.end("Теперь можно успокоиться)","Теперь можно успокоиться...",new String[]{
				ChatColor.GOLD+"<"+ChatColor.AQUA+"Реакция"+ChatColor.GOLD+">",
				"/wons/"
		},true,null);
	}
	public static void start(){
		main.stage="game";
		main.timer=30;
		for(Player p:Bukkit.getOnlinePlayers()){
			wons.add(p.getName());
		}
		timer=3;
		change();
	}
	public static void compl(Player p){
		if(wons.contains(p.getName()))return;
		//main.players.remove(p.getName());
		//p.teleport(main.won);
		//change();
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		wons.add(p.getName());
		p.sendTitle(ChatColor.GREEN+"Реакция!", ChatColor.DARK_GREEN+"Выполнено!", 5, 200, 10);
	}
	static void change(){
		main.pvp=false;
		List<String> pls = new ArrayList<>(main.players);
		for(String p:pls){
			Bukkit.getPlayer(p).getInventory().clear();
			if(!wons.contains(p)){
				Bukkit.getPlayer(p).teleport(main.lose);
				main.players.remove(p);
			}
		}
		wons.clear();
		main.clearPlatform();
		target = targs[new Random().nextInt(targs.length)];
		main.title(ChatColor.GREEN+"Реакция!", ChatColor.YELLOW+target, 5, 200, 10, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
		if(target.equals("Кликни на алмазный блок!")){
			for(Block b : main.getPlatform(4)){
				double coef = 0.05;
				if(main.hard)coef=0.025;
				if(new Random().nextDouble()<coef){
					Location loc = b.getLocation();
					loc.getBlock().setType(Material.DIAMOND_BLOCK);
				}
			}
		}
		else if(target.equals("Ударь другого игрока!")){
			main.pvp=true;
			if(main.hard)for(String st:main.players){
				Bukkit.getPlayer(st).getInventory().setItemInMainHand(ItemUtil.create(Material.STICK, 1, ChatColor.GOLD+"Палочка-посылалочка", null, new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{2}));
			}
		}
	}
}
