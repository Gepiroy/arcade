package games;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import arcade.Events;
import arcade.gameStarter;
import arcade.main;
import utils.GepUtil;

public class manrain {
	static int speed = 0;
	static int spawn = 0;
	static void end(){
		gameStarter.end("А-ле-луйя!","Фу, блин, ФУ!",new String[]{
				ChatColor.AQUA+"0"+ChatColor.BLUE+"Мужицкий дождь"+ChatColor.AQUA+"0",
				"/wons/"
		},true,null);
	}
	public static void start(){
		speed=0;
		spawn=1000;
		main.timer=60;
		main.myScore=true;
	}
	public static void base(){
		if(main.players.size()<=1)end();
		if(main.timer<=0)end();
	}
	public static void fast(){
		speed++;
		spawn-=speed;
		if(spawn<=0){
			spawn=1000;
			spawnMan();
		}
	}
	public static void fall(EntityDamageEvent e){
		if(e.getEntityType().equals(EntityType.VILLAGER)){
			e.getEntity().remove();
			for(Entity en:e.getEntity().getNearbyEntities(0.5, 0.5, 0.5)){
				if(en instanceof Player){
					Player p = (Player) en;
					Events.loose(p);
				}
			}
		}
	}
	static void spawnMan(){
		Location loc = main.randLocFromPlatform(main.platformSize, true);
		loc.add(0,50,0);
		Entity en = loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		en.setVelocity(new Vector((new Random().nextInt(3)-1)/5.0,0,(new Random().nextInt(3)-1)/5.0));
	}
	public static void updateScoreboard(){
		for(Player p:Bukkit.getOnlinePlayers()){
			List<String> strs = new ArrayList<>();
			strs.add(ChatColor.DARK_GREEN+"Игра: "+ChatColor.YELLOW+main.game);
			strs.add(ChatColor.BLUE+"Скорость: "+ChatColor.AQUA+GepUtil.CylDouble(speed/50.000,"#0.000")+"/сек.");//1000/20=50;50=1/sec
			strs.add(ChatColor.BLUE+"Время: "+ChatColor.YELLOW+main.timer+ChatColor.BLUE+" сек.");
			GepUtil.upscor(p,strs,ChatColor.AQUA+"(:-==Gep Craft==-:)");
		}
	}
}
