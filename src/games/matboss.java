package games;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import arcade.gameStarter;
import arcade.main;
import utils.GepUtil;

public class matboss {
	public static HashMap<String,Integer> HM = new HashMap<>();
	public static HashMap<String,Integer> leaders = new HashMap<>();
	static int spawnTimer = 0;
	public static void base(){
		spawnTimer+=(1+main.players.size());
		if(spawnTimer>=35){
			spawnTimer-=35;
			spawnMat();
		}
		if(main.players.size()==0||main.timer<=0){
			for(Player p:Bukkit.getOnlinePlayers()){
				for(PotionEffect pef:p.getActivePotionEffects()){
					p.removePotionEffect(pef.getType());
				}
			}
			HM.clear();
			gameStarter.end("Всё равно 2...","Она бы всё равно 2 поставила...",new String[]{
					ChatColor.GOLD+"*"+ChatColor.DARK_RED+"Математичка-БОСС"+ChatColor.GOLD+"*",
					"/wons/",
					ChatColor.DARK_PURPLE+"Любимчик - "+ChatColor.GREEN+leader()
			},true,null);
			leaders.clear();
		}
	}
	public static void start(){
		spawnTimer = 35;
		main.timer=60;
		main.myScore=true;
		main.lavakill=true;
		for(Player p:Bukkit.getOnlinePlayers()){
			games.matboss.genNumb(p);
		}
	}
	public static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}
	public static void genNumb(Player p){
		int fir = new Random().nextInt(9)+1;
		int sec = new Random().nextInt(9)+1;
		String[] pl = {"-","+","*"};
		String znak = "";
		if(main.hard)znak=pl[new Random().nextInt(3)];
		else znak=pl[new Random().nextInt(2)];
		String target = fir+znak+sec;
		p.sendTitle(ChatColor.RED+target, ChatColor.GREEN+"Напишите ответ в чат.", 5, 1000, 10);
		int numb = 0;
		if(znak.equals("-"))numb = fir-sec;
		else if(znak.equals("+"))numb = fir+sec;
		else if(znak.equals("*"))numb = fir*sec;
		addHM(p,numb);
	}
	static void addHM(Player p, int n){
		if(HM.containsKey(p.getName())){
			HM.replace(p.getName(), n);
		}
		else{
			HM.put(p.getName(), n);
		}
	}
	public static void tryToAnswer(Player p, int ans){
		if(HM.get(p.getName())==ans){
			genNumb(p);
			GepUtil.HashMapReplacer(leaders, p.getName(), 1, false, false);
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
			double set=p.getHealth();
			set+=new Random().nextInt(4)+2;
			if(set>20)set=20;
			p.setHealth(set);
		}
		else{
			p.sendMessage(ChatColor.RED+"Неправильно. Попробуй ещё раз.");
			p.damage(1);
		}
	}
	static String leader(){
		String maxp = "";
		int max = 0;
		for(Entry<String, Integer> ES:leaders.entrySet()){
			if(max<=ES.getValue()){
				max=ES.getValue();
				maxp=ES.getKey();
			}
		}
		return maxp+ChatColor.YELLOW+" ("+max+")";
	}
	public static void updateScoreboard(){
		for(Player p:Bukkit.getOnlinePlayers()){
			Scoreboard s = p.getScoreboard();
			for(String e:s.getEntries()){
				s.resetScores(e);
			}
			Objective o = s.getObjective("stats");
			o.getScore(ChatColor.DARK_GREEN+"Игра: "+ChatColor.YELLOW+main.game).setScore(3);
			o.getScore(ChatColor.YELLOW+"Стадия игры: "+ChatColor.GOLD+main.stage).setScore(2);
			o.getScore(ChatColor.BLUE+"Время: "+ChatColor.YELLOW+main.timer+ChatColor.BLUE+" секунд.").setScore(0);
		}
	}
	static void spawnMat(){
		Location loc = main.randLocFromPlatform(main.platformSize, true);
		loc.setX(loc.getX()+0.5);loc.setZ(loc.getZ()+0.5);
		Witch w = (Witch) loc.getWorld().spawnEntity(loc, EntityType.WITCH);
		w.setCustomName(ChatColor.DARK_RED+"МАТЕМАТИЧКА");
	}
}
