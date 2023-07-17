package games;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import arcade.gameStarter;
import arcade.main;
import utils.GepUtil;

public class cake {
	static int bonus=0;
	public static HashMap<String, Integer> breaked = new HashMap<>();
	public static void start(){
		main.myScore=true;
		int i=0;
		for(;;){
			if(new File(main.instance.getDataFolder()+File.separator+"/schematics/cake"+(i+1)+".schematic").exists())i++;
			else break;
		}
		int ver = new Random().nextInt(i)+1;
		main.loadSchematic(main.locs.get("center"), "cake"+ver);
		main.stage="game";
		main.timer=60;
		for(Player p:Bukkit.getOnlinePlayers()){
			main.tpRandPlatform(p, main.platformSize, false);
		}
	}
	public static void base(){
		bonus++;
		if(bonus>=5)respTool();
		if(main.timer<=0){
			gameStarter.end("Эй, толстый)", "Всё равно пожрал)", new String[]{
					"/wons/"
			}, false, GepUtil.leader(breaked,"all"));
			breaked.clear();
			return;
		}
	}
	static void respTool(){
		Location loc = main.randLocFromPlatform(main.platformSize, true);
		while(!loc.getBlock().getType().equals(Material.AIR)){
			loc.setY(loc.getY()+1);
		}
		Material[] mats = {Material.WOOD_SPADE,Material.SHEARS};
		ItemStack item = new ItemStack(mats[new Random().nextInt(mats.length)]);
		item.setDurability((short) (item.getType().getMaxDurability()/2));
		loc.getWorld().dropItem(loc, item);
		bonus-=5;
	}
	public static void updateScoreboard(){
		for(Player p:Bukkit.getOnlinePlayers()){
			ArrayList<String> strs = new ArrayList<>();
			HashMap<String,Integer> breaks = new HashMap<>(breaked);
			for(int i=1;i<6;i++){
				strs.add(GepUtil.boolCol(i==1)+""+i+". "+GepUtil.boolCol(ChatColor.AQUA, ChatColor.YELLOW, GepUtil.leader(breaks, "name").equals(p.getName()))+GepUtil.leader(breaks, "name")+ChatColor.GRAY+" ("+ChatColor.GOLD+GepUtil.leader(breaks, "score")+ChatColor.GRAY+")");
				breaks.remove(GepUtil.leader(breaks, "name"));
			}
			strs.add(ChatColor.BLUE+"Время: "+ChatColor.YELLOW+main.timer+ChatColor.BLUE+" сек.");
			GepUtil.upscor(p, strs, main.bord);
		}
	}
}
