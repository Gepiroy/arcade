package games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

import arcade.gameStarter;
import arcade.main;

@SuppressWarnings("deprecation")
public class lavaparty {
	static int time=0;
	static DyeColor color = DyeColor.values()[new Random().nextInt(DyeColor.values().length)];
	static ArrayList<Location> fired = new ArrayList<>();
	static HashMap<Location, Integer> preFire = new HashMap<>();
	public static void start(){
		main.stage="game";
		main.timer=70;
		main.lavakill=true;
	}
	public static void end(){
		gameStarter.end("Танцуем, несмотря ни на что!","Ой, попка подгорела!",new String[]{
				ChatColor.RED+"["+ChatColor.LIGHT_PURPLE+"Лава пати"+ChatColor.RED+"]",
				"/wons/",
				ChatColor.GOLD+"От ваших этих танцов сгорело "+ChatColor.RED+fired.size()+ChatColor.GOLD+" блоков!"
		},true,null);
	}
	public static void base(){
		if(time==2){
			remPlatform();
		}
		if(time<=0){
			color = DyeColor.values()[new Random().nextInt(DyeColor.values().length/2)*2];
			if(main.hard)color = DyeColor.values()[new Random().nextInt(DyeColor.values().length)];
			for(Player p:Bukkit.getOnlinePlayers()){
				if(main.players.contains(p.getName())){
					ItemStack item = new ItemStack(Material.WOOL,1,color.getWoolData());
					for(int i=0;i<36;i++){
						p.getInventory().setItem(i,item);
					}
					p.getInventory().setHelmet(item);
				}
			}
			time=main.timer/20+3;
			setPlatform();
		}
		for(Location loc:new ArrayList<Location>(preFire.keySet())){
			if(preFire.containsKey(loc)&&preFire.get(loc)==1)loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc.getX()+0.5, loc.getY()+1, loc.getZ()+0.5, 5, 0.2, 0.2, 0.2, 0);
			if(preFire.containsKey(loc)&&preFire.get(loc)==2)loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.getX()+0.5, loc.getY()+1, loc.getZ()+0.5, 10, 0.2, 0.2, 0.2, 0);
			if(preFire.containsKey(loc)&&preFire.get(loc)==3)loc.getWorld().spawnParticle(Particle.FLAME, loc.getX()+0.5, loc.getY()+1, loc.getZ()+0.5, 15, 0.2, 0.2, 0.2, 0.05);
			if(!preFire.containsKey(loc))main.globMessage("preFirenocont."+loc, null);
		}
		if(main.timer<=0||main.players.size()<=1)end();
		time--;
		for(Location loc:new ArrayList<Location>(preFire.keySet())){
			if(new Random().nextDouble()>0.1)continue;
			if(preFire.get(loc)>=3){
				fired.add(loc);
				preFire.remove(loc);
				loc.getBlock().breakNaturally();
				continue;
			}
			preFire.replace(loc, preFire.get(loc)+1);
		}
	}
	static void setPlatform(){
		int cx=0;
		int cz=0;
		int x=(int) (main.locs.get("center").getX()-main.platformSize);
		for(;cx<main.platformSize*2+1;cx++){
			int z=(int) (main.locs.get("center").getZ()-main.platformSize);
			for(;cz<main.platformSize*2+1;cz++){
				Location loc = new Location(main.locs.get("center").getWorld(),x,main.locs.get("center").getY()-1,z);
				if(fired.contains(loc)){
					z++;
					continue;
				}
				if(new Random().nextDouble()<0.5/(100.00-main.timer)&&!preFire.containsKey(loc)){
					preFire.put(loc, 1);
				}
				loc.getBlock().setType(Material.WOOL);
				BlockState bs = loc.getBlock().getState();
				Wool wool = (Wool) bs.getData();
				if(!main.hard)wool.setColor(DyeColor.values()[new Random().nextInt(DyeColor.values().length/2)*2]);
				else wool.setColor(DyeColor.values()[new Random().nextInt(DyeColor.values().length)]);
				bs.update();
				z++;
			}
			x++;
			cz=0;
		}
	}
	static void remPlatform(){
		int x=(int) (main.locs.get("center").getX()-main.platformSize);
		for(;x<main.platformSize*2+1;x++){
			int z=(int) (main.locs.get("center").getZ()-main.platformSize);
			for(;z<main.platformSize*2+1;z++){
				Location loc = new Location(main.locs.get("center").getWorld(),x,main.locs.get("center").getY()-1,z);
				if(loc.getBlock().getType().equals(Material.WOOL)&&!((Wool)loc.getBlock().getState().getData()).getColor().equals(color)){
					if(new Random().nextDouble()<0.1)loc.getBlock().breakNaturally();
					else loc.getBlock().setType(Material.AIR);
				}
			}
		}
	}
}
