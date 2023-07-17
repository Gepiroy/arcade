package games;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import arcade.main;
import objArcade.Chick;
import utils.ItemUtil;

public class egg {
	static ArrayList<Chick> chicks = new ArrayList<>();
	static int EggGiveRate=0;
	public static void start(){
		main.stage="game";
		main.timer=30;
		for(Player p:Bukkit.getOnlinePlayers()){
			p.getInventory().addItem(ItemUtil.create(Material.EGG, ChatColor.LIGHT_PURPLE+"Ўвырни мен€! :D"));
		}
	}
	static void end(){
		chicks.clear();
		for(Entity en:Bukkit.getWorld("world").getEntities()){
			if(!(en instanceof Player)){
				en.remove();
			}
		}
	}
	public static void base(){
		for(Chick c:new ArrayList<>(chicks)){
			if(Bukkit.getEntity(c.id)==null){
				chicks.remove(c);
			}
			if(c.target==null||Bukkit.getEntity(c.target)==null){
				reTarget(c);
			}
			else{
				if(Bukkit.getEntity(c.id).getLocation().distance(Bukkit.getEntity(c.target).getLocation())<=1){
					((Damageable) Bukkit.getEntity(c.target)).damage(1);
				}
			}
		}
		EggGiveRate++;
		if(EggGiveRate>=3)for(Player p:Bukkit.getOnlinePlayers())p.getInventory().addItem(ItemUtil.create(Material.EGG, ChatColor.LIGHT_PURPLE+"Ўвырни мен€! :D"));
		if(main.timer<=0){
			end();
		}
	}
	static void reTarget(Chick c){
		double dist = 100;
		Chick target = null;
		for(Chick ch:chicks){
			if(!ch.equals(c)&&Bukkit.getEntity(ch.id)!=null&&Bukkit.getEntity(c.id).getLocation().distance(Bukkit.getEntity(ch.id).getLocation())<dist){
				dist=Bukkit.getEntity(c.id).getLocation().distance(Bukkit.getEntity(ch.id).getLocation());
				target=ch;
			}
		}
		if(target!=null)c.target=target.id;
	}
	public static void tp(Player p){
		Location loc = p.getLocation();
		while(!loc.getBlock().getType().equals(Material.AIR)){
			loc.setY(loc.getY()+1);
		}
		p.teleport(loc);
	}
    public static void spawnChick(ProjectileHitEvent e){
    	if(e.getEntity().getType().equals(EntityType.EGG)){
    		if(new Random().nextDouble()<=0.2){
    			Egg egg = (Egg) e.getEntity();
    			Entity en = egg.getWorld().spawnEntity(egg.getLocation(), EntityType.CHICKEN);
    			chicks.add(new Chick(((CommandSender) egg.getShooter()).getName(), en.getUniqueId()));
    		}
    	}
    }
    public static void enev(EntityDamageByEntityEvent e){
    	if(e.getEntityType().equals(EntityType.CHICKEN)&&e.getDamager() instanceof Player)e.setCancelled(true);
    }
    /*public void followPlayer(Location l, LivingEntity entity, double d) {
        final LivingEntity e = entity;
        final Player p = player;
        final float f = (float) d;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                ((EntityInsentient) ((CraftEntity) e).getHandle()).getNavigation().a(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), f);
            }
        }, 0 * 20, 2 * 20);
    }*/
}
