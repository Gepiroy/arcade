package games;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import arcade.gameStarter;
import arcade.main;
import utils.ItemUtil;

public class sixsec {
	static int timer=0;
	static HashMap<String,Integer> saved = new HashMap<>();
	public static void start(){
		if(main.hard)main.pvp=true;
		main.stage="game";
		main.timer=60;
		Bukkit.getWorld("world").getWorldBorder().setWarningDistance(100);
		String[] strs={"MELLOHI","STRAD","WARD"};
		main.locs.get("center").getWorld().playSound(main.locs.get("center"), Sound.valueOf("RECORD_"+strs[new Random().nextInt(strs.length)]), 10, 10);
		for(Player p:Bukkit.getOnlinePlayers()){
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 2, false, false));
		}
	}
	static void end(){
		int max=0;
		String maxname="";
		String tolore=ChatColor.RED+"Не бойтесь лавы, когда до смерти остаются считанные секунды, лава становится безопасной!";
		for(String st:saved.keySet()){
			if(saved.get(st)>max){
				max=saved.get(st);
				maxname=st;
			}
		}
		if(max>0){
			if(max>10)tolore=ChatColor.YELLOW+maxname+ChatColor.GOLD+" унёс с собой "+ChatColor.YELLOW+max+ChatColor.GOLD+" супа!";
			else tolore=ChatColor.GREEN+"Что? Это правда случилось? ВСЕ ИГРОКИ УНЕСЛИ НЕ БОЛЬШЕ 10 СУПА?!!";
		}
		gameStarter.end("Кажется, мы детей забыли...","Что ж, привет, гриб...",new String[]{
				ChatColor.GOLD+"["+ChatColor.YELLOW+"60 seconds"+ChatColor.GOLD+"]",
				"/wons/",
				tolore
		},false, null);
		saved.clear();
		for(Player p:Bukkit.getOnlinePlayers()){
			p.stopSound(Sound.RECORD_MELLOHI);
			p.stopSound(Sound.RECORD_STRAD);
			p.stopSound(Sound.RECORD_WARD);
			p.removePotionEffect(PotionEffectType.SPEED);
		}
	}
	public static void base(){
		for(Player p:Bukkit.getOnlinePlayers()){
			if(!main.players.contains(p.getName()))continue;
			int cos = 0;
			for(int i=0;i<p.getInventory().getSize();i++){
				if(p.getInventory().getItem(i)!=null){
					ItemStack item = p.getInventory().getItem(i);
					if(item.getType().equals(Material.MUSHROOM_SOUP)){
						cos++;
					}
				}
			}
			p.sendTitle(ChatColor.RED+""+main.timer, ChatColor.YELLOW+"Супа: "+ChatColor.GREEN+cos+ChatColor.GOLD+"/10", 0, 25, 10);
		}
		timer+=main.players.size();
		while(timer>=5){
			spawnfood();
			timer-=5;
		}
		if(main.players.size()<=0||main.timer<=0){
			end();
		}
		if(main.stage.equals("won")){
			Bukkit.getWorld("world").getWorldBorder().setWarningDistance(0);
		}
	}
	public static void spawnfood(){
		Location loc = main.randLocFromPlatform(main.platformSize-1, true);
		loc.getWorld().dropItemNaturally(loc, ItemUtil.create(Material.MUSHROOM_SOUP, ChatColor.GOLD+"Суп"));
	}
	public static void save(Player p){
		if(!main.players.contains(p.getName()))return;
		int cos = 0;
		for(int i=0;i<p.getInventory().getSize();i++){
			if(p.getInventory().getItem(i)!=null){
				ItemStack item = p.getInventory().getItem(i);
				if(item.getType().equals(Material.MUSHROOM_SOUP)){
					cos++;
				}
			}
		}
		if(cos>=10){
			p.teleport(main.won);
			p.getInventory().clear();
			main.players.remove(p.getName());
			saved.put(p.getName(), cos);
			main.globMessage(ChatColor.YELLOW+p.getName()+" спасся!", Sound.ENTITY_GHAST_AMBIENT);
		}
		else{
			p.teleport(main.locs.get("center"));
			p.sendMessage(ChatColor.RED+"Недостаточно супа! Нужно ещё!");
		}
	}
}
