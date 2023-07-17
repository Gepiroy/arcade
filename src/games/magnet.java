package games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import arcade.gameStarter;
import arcade.main;
import utils.GepUtil;
import utils.ItemUtil;

@SuppressWarnings("deprecation")
public class magnet {
	static HashMap<String, Integer> mSize = new HashMap<>();
	static HashMap<String, Integer> points = new HashMap<>();
	
	public static void start(){
		main.myScore=true;
		main.timer=90;
		for(Player p:Bukkit.getOnlinePlayers()){
			p.getInventory().setItem(0, ItemUtil.create(Material.IRON_BARDING, ChatColor.BLUE+"Магнит"));
			mSize.put(p.getName(), 75);
			points.put(p.getName(), 0);
		}
	}
	public static void base(){
		if(main.timer<=0){
			gameStarter.end("Король-электрик", "У тебя причёска дыбом!", new String[]{
					ChatColor.YELLOW+"~<~"+ChatColor.BLUE+"Магнит"+ChatColor.YELLOW+"~>~"
					,"/wons/"
					,ChatColor.BLUE+"Самый мощный магнит - "+ChatColor.AQUA+GepUtil.leader(mSize, "all")
			}, false, GepUtil.leader(points,"all"));
			points.clear();
			mSize.clear();
			return;
		}
		for(int i=0;i<Bukkit.getOnlinePlayers().size();i++){
			Material mat = Material.DIAMOND;
			double r = new Random().nextDouble();
			if(r<=0.05)mat=Material.FEATHER;
			else if(r<=0.15)mat=Material.GOLD_INGOT;
			Item item = Bukkit.getWorld("world").dropItem(main.randLocFromPlatform(main.platformSize, true).add(0,3,0), new ItemStack(mat));
			item.setGravity(false);
			item.setVelocity(new Vector(0,new Random().nextInt(31)/100.00,0));
		}
	}
	public static void fast(){
		for(Player p:Bukkit.getOnlinePlayers()){
			if(p.getInventory().getItemInMainHand().getType().equals(Material.IRON_BARDING)){
				double d = mSize.get(p.getName())/10.0;
				for(Entity en:p.getNearbyEntities(d, d, d)){
					if(en.getType().equals(EntityType.DROPPED_ITEM)){
						Item item = (Item) en;
						Location point1 = p.getLocation();
						point1.setY(point1.getY()+1);
					    Location point2 = item.getLocation();
					    double distance = point1.distance(point2);
					    if(distance>d)continue;
					    Vector p1 = point1.toVector();
					    Vector p2 = point2.toVector();
					    Vector vector = p2.clone().subtract(p1).normalize().multiply(d/100.00-distance/100.00);
					    vector.multiply(-1);
					    if(vector.getY()>=0)vector.setY(-0.01);
						item.setVelocity(item.getVelocity().add(vector));
						drawLine(p, item.getLocation(), distance/5.0, Particle.CRIT_MAGIC, 0.2);
					}
				}
			}
		}
	}
	public static void pickUp(PlayerPickupItemEvent e){
		Material mat = e.getItem().getItemStack().getType();
		Player p = e.getPlayer();
		e.setCancelled(true);
		e.getItem().remove();
		if(mat.equals(Material.FEATHER)){
			p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 10));
			p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_HURT, 2, 1);
		}
		if(mat.equals(Material.GOLD_INGOT)){
			GepUtil.HashMapReplacer(mSize, p.getName(), new Random().nextInt(5)+1, false, false);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
		}
		if(mat.equals(Material.DIAMOND)){
			GepUtil.HashMapReplacer(points, p.getName(), 1, false, false);
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
		}
	}
	static void drawLine(Player p, Location point2, double space, Particle part, double chance) {
		Location point1 = p.getLocation();
		point1.setY(point1.getY()+1);
	    World world = point1.getWorld();
	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double length = 0;
	    for (; length < distance; p1.add(vector)) {
	        if(new Random().nextDouble()<=chance)p.getWorld().spawnParticle(part, p1.getX(), p1.getY(), p1.getZ(), 1, 0, 0, 0, 0);
	        length += space;
	    }
	}
	public static void updateScoreboard(){
		for(Player p:Bukkit.getOnlinePlayers()){
			List<String> strs = new ArrayList<>();
			strs.add(ChatColor.DARK_GREEN+"Игра: "+ChatColor.YELLOW+main.game);
			HashMap<String,Integer> breaks = new HashMap<>(points);
			for(int i=1;i<6;i++){
				strs.add(GepUtil.boolCol(i==1)+""+i+". "+GepUtil.boolCol(ChatColor.AQUA, ChatColor.YELLOW, GepUtil.leader(breaks, "name").equals(p.getName()))+GepUtil.leader(breaks, "name")+ChatColor.GRAY+" ("+ChatColor.GOLD+GepUtil.leader(breaks, "score")+ChatColor.GRAY+")");
				breaks.remove(GepUtil.leader(breaks, "name"));
			}
			strs.add(ChatColor.BLUE+"Мощность магнита: "+ChatColor.AQUA+mSize.get(p.getName()));
			strs.add(ChatColor.BLUE+"Время: "+ChatColor.YELLOW+main.timer+ChatColor.BLUE+" сек.");
			GepUtil.upscor(p,strs,main.bord);
		}
	}
}
