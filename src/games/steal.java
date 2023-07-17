package games;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import arcade.gameStarter;
import arcade.main;
import utils.GepUtil;
import utils.ItemUtil;

public class steal {
	static ArrayList<String> emeralds = new ArrayList<>();
	static HashMap<String, Integer> points = new HashMap<>();
	static HashMap<String, Integer> stealed = new HashMap<>();
	static HashMap<String, Integer> loosed = new HashMap<>();
	public static void end(){
		gameStarter.end("Нужно БОООЛЬШЕ зоолотаа...", "Воровать - нихарашо(", new String[]{
				"/wons/"
				,ChatColor.DARK_GREEN+"Игрок "+ChatColor.YELLOW+GepUtil.leader(stealed, "name")+ChatColor.DARK_GREEN+" украл очко у других игроков "+ChatColor.GOLD+GepUtil.leader(stealed, "score")+" раз!"
				,ChatColor.LIGHT_PURPLE+"У игрока "+ChatColor.YELLOW+GepUtil.leader(loosed, "name")+ChatColor.LIGHT_PURPLE+" украли очко "+ChatColor.RED+GepUtil.leader(loosed, "score")+" раз!"
		}, false, GepUtil.leader(points,"all"));
		emeralds.clear();
		points.clear();
		stealed.clear();
		loosed.clear();
	}
	public static void start(){
		main.stage="game";
		main.timer=30;
		main.pvp=true;
		main.myScore=true;
		int CoE=Bukkit.getOnlinePlayers().size()/3+1;
		while(CoE>0){
			for(Player p:Bukkit.getOnlinePlayers()){
				if(!emeralds.contains(p.getName())){
					setPToEm(p, null);
					CoE--;
					break;
				}
			}
		}
		for(Player p:Bukkit.getOnlinePlayers()){
			points.put(p.getName(), 0);
			stealed.put(p.getName(), 0);
			loosed.put(p.getName(), 0);
		}
	}
	public static void base(){
		for(Player p:Bukkit.getOnlinePlayers()){
			if(emeralds.contains(p.getName())){
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
				GepUtil.HashMapReplacer(points, p.getName(), 1, false, false);
			}
		}
		if(main.timer<=0){
			end();
		}
	}
	static void setPToEm(Player p, Player fromWho){
		if(fromWho!=null){
			fromWho.getInventory().clear();
			emeralds.remove(fromWho.getName());
		}
		emeralds.add(p.getName());
		p.getInventory().setHelmet(ItemUtil.create(Material.EMERALD_BLOCK, ChatColor.GREEN+"ДЕНЬГИИИ!!!"));
	}
	public static void hurt(EntityDamageByEntityEvent e){
		if(!main.stage.equals("game"))return;
		if(e.getEntity() instanceof Player&&e.getDamager() instanceof Player){
			Player p = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			if(emeralds.contains(p.getName())&&!emeralds.contains(damager.getName()))setPToEm(damager, p);
			if(points.get(p.getName())>0){
				p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 2, 2);
				GepUtil.HashMapReplacer(points, p.getName(), -1, false, false);
				GepUtil.HashMapReplacer(loosed, p.getName(), 1, false, false);
				damager.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 2, 2);
				GepUtil.HashMapReplacer(points, damager.getName(), 1, false, false);
				GepUtil.HashMapReplacer(stealed, damager.getName(), 1, false, false);
			}
			p.setHealth(20);
		}
	}
	public static void updateScoreboard(){
		for(Player p:Bukkit.getOnlinePlayers()){
			ArrayList<String> strs = new ArrayList<>();
			HashMap<String,Integer> breaks = new HashMap<>(points);
			for(int i=1;i<6;i++){
				strs.add(GepUtil.boolCol(i==1)+""+i+". "+GepUtil.boolCol(ChatColor.AQUA, ChatColor.YELLOW, GepUtil.leader(breaks, "name").equals(p.getName()))+GepUtil.leader(breaks, "name")+ChatColor.GRAY+" ("+ChatColor.GOLD+GepUtil.leader(breaks, "score")+ChatColor.GRAY+")");
				breaks.remove(GepUtil.leader(breaks, "name"));
			}
			strs.add(ChatColor.BLUE+"Время: "+ChatColor.YELLOW+main.timer+ChatColor.BLUE+" сек.");
			GepUtil.upscor(p, strs, ChatColor.AQUA+"---==|Gep Arcade|==---");
		}
	}
}
