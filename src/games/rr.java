package games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import arcade.Events;
import arcade.gameStarter;
import arcade.main;
import utils.ItemUtil;

public class rr {
	public static HashMap<String,List<String>> players = new HashMap<>();
	public static HashMap<String,Integer> leaders = new HashMap<>();
	public static void first(Player p){
		p.getInventory().addItem(ItemUtil.create(Material.FERMENTED_SPIDER_EYE, 1, (byte)0, ChatColor.DARK_RED+"Русская рулетка", null, null, null));
	}
	static void end(){
		String comment="";
		int max = 0;
		String maxp = "";
		for(Entry<String, Integer> ES:leaders.entrySet()){
			if(max<ES.getValue()){
				max=ES.getValue();
				maxp=ES.getKey();
			}
		}
		comment=ChatColor.GREEN+"Он набрал "+ChatColor.DARK_RED+max+" очков!";
		if(players.size()==1)comment=ChatColor.LIGHT_PURPLE+"Все соперники просто застрелились!";
		gameStarter.end("Сектор 'приз' на барабане!","Это не рулетка Якубовича.",new String[]{
				ChatColor.GOLD+"["+ChatColor.DARK_RED+"Русская рулетка"+ChatColor.GOLD+"]",
				"/wons/",
				comment
		},false, maxp);
		players.clear();
		leaders.clear();
	}
	public static void base(){
		if(players.size()<=0){
			main.title(ChatColor.YELLOW+"ЧТО?",ChatColor.DARK_PURPLE+"Все застрелились!",10,30,20, Sound.ENTITY_VILLAGER_DEATH);
			main.stage="won";
			main.timer=3;
			players.clear();
			leaders.clear();
			return;
		}
		if(players.size()==1){
			end();
			return;
		}
		else if(players.size()>=2&&main.timer<=0){
			int max = 0;
			String maxp = "";
			for(Entry<String, Integer> ES:leaders.entrySet()){
				if(max<=ES.getValue()){
					max=ES.getValue();
					maxp=ES.getKey();
				}
			}
			main.title(ChatColor.GREEN+"Победил игрок "+ChatColor.YELLOW+maxp,ChatColor.GREEN+"Самый рисковый игрок!",10,30,20, Sound.ENTITY_VILLAGER_YES);
			main.stage="won";
			main.timer=3;
			players.clear();
			leaders.clear();
			return;
		}
	}
	
	public static void change(Player p){
		String[] drops = {ChatColor.BLUE+"+1",ChatColor.GREEN+"+2",ChatColor.DARK_GREEN+"+3",ChatColor.RED+"Смерть"};
		List<String> set = new ArrayList<>();
		int howMany=8;
		if(main.hard)howMany=6;
		for(int i=0;i<howMany;i++){
			set.add(drops[new Random().nextInt(drops.length)]);
		}
		int diec = 0;
		for(String drop:set){
			if(drop.equals(ChatColor.RED+"Смерть")){
				diec++;
			}
		}
		List<String> setit = new ArrayList<>();
		for(String drop:set){
			if(drop.equals(ChatColor.BLUE+"+1")){
				String sti = ChatColor.BLUE+"+"+(1*(diec+1));
				setit.add(sti);
			}
			if(drop.equals(ChatColor.GREEN+"+2")){
				String sti = ChatColor.GREEN+"+"+(2*(diec+1));
				setit.add(sti);
			}
			if(drop.equals(ChatColor.DARK_GREEN+"+3")){
				String sti = ChatColor.DARK_GREEN+"+"+(3*(diec+1));
				setit.add(sti);
			}
			if(drop.equals(ChatColor.RED+"Смерть")){
				setit.add(drop);
			}
		}
		if(players.containsKey(p.getName()))players.replace(p.getName(), setit);
		else{
			players.put(p.getName(), setit);
			leaders.put(p.getName(), 0);
		}
	}
	public static void updateScoreboard(){
		for(Player p:Bukkit.getOnlinePlayers()){
			Scoreboard s = p.getScoreboard();
			for(String e:s.getEntries()){
				s.resetScores(e);
			}
			Objective o = s.getObjective("stats");
			o.getScore(ChatColor.DARK_GREEN+"Игра: "+ChatColor.YELLOW+main.game).setScore(10);
			o.getScore(ChatColor.YELLOW+"Стадия игры: "+ChatColor.GOLD+main.stage).setScore(9);
			if(players.containsKey(p.getName())){
				o.getScore(ChatColor.GOLD+"На барабане:").setScore(8);
				int i=0;
				for(String ES:players.get(p.getName())){
					o.getScore((i+1)+". "+ES).setScore(i);
					i++;
				}
			}else{o.getScore(ChatColor.RED+"Вы застрелились.").setScore(7);}
			int max = 0;
			String maxp = "";
			for(Entry<String, Integer> ES:leaders.entrySet()){
				if(max<=ES.getValue()){
					max=ES.getValue();
					maxp=ES.getKey();
				}
			}
			o.getScore(ChatColor.DARK_RED+"Лидер: "+ChatColor.YELLOW+maxp+" ("+ChatColor.GOLD+max+ChatColor.YELLOW+")").setScore(0);
		}
	}
	public static void use(Player p){
		String drop = players.get(p.getName()).get(new Random().nextInt(players.get(p.getName()).size()));
		if(drop.equals(ChatColor.RED+"Смерть")){
			Events.loose(p);
			main.globMessage(ChatColor.YELLOW+p.getName()+" застрелился.", Sound.ENTITY_LIGHTNING_THUNDER);
			return;
		}
		if(drop.contains(ChatColor.BLUE+"")){
			int value = Integer.parseInt(drop.charAt(3)+"");
			leaders.replace(p.getName(), leaders.get(p.getName())+value);
			p.sendTitle(drop+".", "", 5, 10, 5);
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
		}
		if(drop.contains(ChatColor.GREEN+"")){
			int value = Integer.parseInt(drop.charAt(3)+"");
			leaders.replace(p.getName(), leaders.get(p.getName())+value);
			p.sendTitle(drop, "", 5, 10, 5);
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		}
		if(drop.contains(ChatColor.DARK_GREEN+"")){
			int value = Integer.parseInt(drop.charAt(3)+"");
			leaders.replace(p.getName(), leaders.get(p.getName())+value);
			p.sendTitle(drop+"!", "", 5, 10, 5);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		}
		change(p);
	}
}
