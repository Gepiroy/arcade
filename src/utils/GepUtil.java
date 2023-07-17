package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class GepUtil {
	public static boolean HashMapReplacer(HashMap<String,Integer> hm, String key, int val, boolean zero, boolean set){
		if(hm.containsKey(key)){
			if(set)hm.replace(key, val);
			else hm.replace(key, hm.get(key)+val);
		}
		else{
			hm.put(key, val);
		}
		if(zero){
			if(hm.get(key)<=0){
				hm.remove(key);
				return true;
			}
		}
		return false;
	}
	public static void globMessage(String mes, Sound sound, float vol, float speed, String title, String subtitle, int spawn, int hold, int remove){
		for(Player p:Bukkit.getOnlinePlayers()){
			if(mes!=null)p.sendMessage(mes);
			if(sound!=null){
				p.playSound(p.getLocation(), sound, vol, speed);
			}
			if(title!=null||subtitle!=null) {
				p.sendTitle(title, subtitle, spawn, hold, remove);
			}
		}
	}
	public static ChatColor boolCol(boolean arg){
		if(arg)return ChatColor.GREEN;
		else return ChatColor.RED;
	}
	public static ChatColor boolCol(ChatColor Tcolor, ChatColor Fcolor, boolean arg){
		if(arg)return Tcolor;
		else return Fcolor;
	}
	public static void debug(String message, String whoCaused, String type){
		String prefix = ChatColor.GRAY+"[DEBUG";
		if(whoCaused!=null)prefix+="(from "+ChatColor.YELLOW+whoCaused+ChatColor.GRAY+")";
		prefix+="]";
		if(type.equals("error"))prefix+=ChatColor.RED;
		if(type.equals("info"))prefix+=ChatColor.AQUA;
		if(Bukkit.getPlayer("Gepiroy")!=null){
			Bukkit.getPlayer("Gepiroy").sendMessage(prefix+message);
		}
		Bukkit.getConsoleSender().sendMessage(prefix+message);
	}
	public static boolean chance(int ch){
		return new Random().nextInt(100)+1<=ch;
	}
	public static boolean chance(double ch){
		return new Random().nextDouble()<=ch;
	}
	public static String chances(String[] sts, double[] chs){
		double r = new Random().nextInt(100)+new Random().nextDouble();
		double ch = 0.000;
		for(int i=0;i<sts.length;i++){
			if(r>ch&&r<=ch+chs[i]){
				return sts[i];
			}
			ch+=chs[i];
		}
		return "";
	}
	public static boolean itemName(ItemStack item, String name) {
		if(!item.hasItemMeta())return false;
		if(!item.getItemMeta().hasDisplayName())return false;
		if(item.getItemMeta().getDisplayName().equals(name))return true;
		return false;
	}
	public static boolean isFullyItem(ItemStack item, String name, Material mat){
		if(mat!=null&&!item.getType().equals(mat))return false;
		if(!item.hasItemMeta())return false;
		if(!item.getItemMeta().hasDisplayName())return false;
		if(!item.getItemMeta().hasLore())return false;
		if(name!=null&&!item.getItemMeta().getDisplayName().equals(name))return false;
		return true;
	}
	public static ArrayList<String> stringToArrayList(String st){
		ArrayList<String> ret = new ArrayList<>();
		String toadd = "";
		for(int i=0;i<st.length();i++){
			String c = st.charAt(i)+"";
			if(!c.equals(";")){
				toadd=toadd+c;
			}
			else{
				ret.add(toadd);
				toadd="";
			}
		}
		return ret;
	}
	public static String ArrayListToString(ArrayList<String> ara){
		String ret = "";
		for(String st:ara){
			ret = ret+st+";";
		}
		return ret;
	}
	public static String leader(HashMap<String,Integer> leaders, String retType){
		String maxp = "";
		int max = 0;
		for(Entry<String, Integer> ES:leaders.entrySet()){
			if(max<=ES.getValue()){
				max=ES.getValue();
				maxp=ES.getKey();
			}
		}
		if(maxp.equals(""))return ChatColor.RED+"...";
		else if(retType.equals("name"))return maxp;
		else if(retType.equals("score"))return ""+max;
		else return maxp+ChatColor.YELLOW+" ("+max+")";
	}
	public static String leaderD(HashMap<String,Double> leaders, String retType){
		String maxp = "";
		double max = 0;
		for(Entry<String, Double> ES:leaders.entrySet()){
			if(max<=ES.getValue()){
				max=ES.getValue();
				maxp=ES.getKey();
			}
		}
		if(maxp.equals(""))return ChatColor.RED+"...";
		else if(retType.equals("name"))return maxp;
		else if(retType.equals("score"))return CylDouble(max,"#0.00");
		else return maxp+ChatColor.YELLOW+" ("+CylDouble(max,"#0.00")+")";
	}
	public static String leaderF(HashMap<String,Float> leaders, String retType){
		String maxp = "";
		double max = 0;
		for(Entry<String, Float> ES:leaders.entrySet()){
			if(max<=ES.getValue()){
				max=ES.getValue();
				maxp=ES.getKey();
			}
		}
		if(maxp.equals(""))return ChatColor.RED+"...";
		else if(retType.equals("name"))return maxp;
		else if(retType.equals("score"))return CylDouble(max,"#0.00");
		else return maxp+ChatColor.YELLOW+" ("+CylDouble(max,"#0.00")+")";
	}
	public static void upscor(Player p, List<String> strings, String borders){
		Scoreboard s = p.getScoreboard();
		for(String e:s.getEntries()){
			s.resetScores(e);
		}
		Objective o = s.getObjective("stats");
		int i=strings.size();
		o.getScore(ChatColor.RED+borders).setScore(i+1);
		for(String st:strings){
			o.getScore(st).setScore(i);
			i--;
		}
		o.getScore(ChatColor.BLUE+borders).setScore(i);
	}
	public static Location nearest(Location loc, List<Location> locs){
		double dist = 10000;
		Location ret = null;
		for(Location l:locs){
			if(loc.distance(l)<dist){
				dist=loc.distance(l);
				ret=l;
			}
		}
		return ret;
	}
	public static Entity nearestEntity(Location loc, List<Entity> ents){
		double dist = 10000;
		Entity ret = null;
		for(Entity en:ents){
			if(loc.distance(en.getLocation())<dist){
				dist=loc.distance(en.getLocation());
				ret=en;
			}
		}
		return ret;
	}
	public static String CylDouble(double d, String cyl){
		return new DecimalFormat(cyl).format(d).replaceAll(",", ".");
	}
}