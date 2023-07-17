package games;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import arcade.gameStarter;
import arcade.main;
import utils.GepUtil;
import utils.ItemUtil;

public class breaker {
	public static HashMap<Integer,String> tnts = new HashMap<>();
	public static HashMap<String,Float> powers = new HashMap<>();
	public static HashMap<String,Integer> points = new HashMap<>();
	
	public static void start(){
		main.stage="game";
		main.timer=30;
		main.myScore=true;
		main.canBuild=true;
		int cob = 0;
		for(int i=0;i<main.players.size();i++){
			cob+=new Random().nextInt(500)+250;
		}
		while(cob>1){
			Location loc = main.randLocFromPlatform(main.platformSize, true);
			while(!loc.getBlock().getType().equals(Material.AIR)){
				loc.setY(loc.getY()+1);
				if(loc.getY()>=50)break;
			}
			float r = new Random().nextFloat();
			if(r<=0.50)loc.getBlock().setType(Material.STONE);
			if(r>=0.51&&r<=0.80)loc.getBlock().setType(Material.DIRT);
			if(r>=0.81&&r<=0.95)loc.getBlock().setType(Material.IRON_ORE);
			if(r>=0.96)loc.getBlock().setType(Material.GOLD_ORE);
			cob--;
		}
		for(Player p:Bukkit.getOnlinePlayers()){
			tp(p);
			powers.put(p.getName(), (float) 1.4);
			points.put(p.getName(), 0);
			p.getInventory().addItem(ItemUtil.create(Material.TNT, ChatColor.RED+"ВЗРЫВЧАТКА!"));
		}
	}
	public static void base(){
		if(main.timer<=0){
			int max = 0;
			String maxp = "";
			for(Entry<String, Integer> ES:points.entrySet()){
				if(max<=ES.getValue()){
					max=ES.getValue();
					maxp=ES.getKey();
				}
			}
			gameStarter.end("Истинный халк!", "Да не бомбит у меня!", new String[]{
					"/wons/"
					,"Самый мощный динамит - "+GepUtil.leaderF(powers, "all")
			}, false, maxp);
			powers.clear();
			points.clear();
			return;
		}
	}
	public static void tp(Player p){
		Location loc = p.getLocation();
		while(!loc.getBlock().getType().equals(Material.AIR)){
			loc.setY(loc.getY()+1);
		}
		p.teleport(loc);
	}
	public static void updateScoreboard(){
		for(Player p:Bukkit.getOnlinePlayers()){
			Scoreboard s = p.getScoreboard();
			for(String e:s.getEntries()){
				s.resetScores(e);
			}
			Objective o = s.getObjective("stats");
			o.getScore(ChatColor.DARK_GREEN+"Игра: "+ChatColor.YELLOW+main.game).setScore(6);
			o.getScore(ChatColor.YELLOW+"Стадия игры: "+ChatColor.GOLD+main.stage).setScore(5);
			o.getScore(ChatColor.BLUE+"").setScore(4);
			String power = new DecimalFormat("#0.00").format(powers.get(p.getName()));
			o.getScore(ChatColor.RED+"Мощность взрыва: "+ChatColor.GOLD+power).setScore(3);
			o.getScore(ChatColor.GREEN+"Очки: "+ChatColor.DARK_GREEN+points.get(p.getName())).setScore(2);
			int max = 0;
			String maxp = "";
			for(Entry<String, Integer> ES:points.entrySet()){
				if(max<=ES.getValue()){
					max=ES.getValue();
					maxp=ES.getKey();
				}
			}
			o.getScore(ChatColor.DARK_RED+"Лидер: "+ChatColor.YELLOW+maxp+" ("+ChatColor.GOLD+max+ChatColor.YELLOW+")").setScore(1);
			o.getScore(ChatColor.BLUE+"Время: "+ChatColor.YELLOW+main.timer+ChatColor.BLUE+" секунд.").setScore(0);
		}
	}
}
