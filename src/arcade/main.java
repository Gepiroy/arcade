package arcade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;

import games.ranpvp;
import utils.GepUtil;

@SuppressWarnings("deprecation")
public class main extends JavaPlugin{
	public static HashMap<String,Location> locs = new HashMap<>();
	public static String game = "";
	public static String stage = "";
	public static int timer = 0;
	public static int platformSize = 9;
	public static boolean pvp = false;
	public static boolean lavakill = false;
	public static boolean canBuild = false;
	public static boolean freeze = false;
	public static boolean myScore = false;
	public static Location lose = null;
	public static Location won = null;
	public static List<String> players = new ArrayList<>();
	public static Scoreboard s;
	public static int secrate = 0;
	public static main instance;
	public static boolean hard = false;
	public static HashMap<String,Integer> blocked = new HashMap<>();
	public static String bord=ChatColor.AQUA+"(:-==Gep Craft==-:)";
	int online = 0;
	public void onEnable(){
		instance=this;
		if(getConfig().contains("locs"))for (String b : getConfig().getConfigurationSection("locs").getKeys(false)) {
			locs.put(b, new Location(Bukkit.getWorld(getConfig().getString("locs." + b + ".World")),getConfig().getDouble("locs." + b + ".X"), getConfig().getDouble("locs." + b + ".Y"), getConfig().getDouble("locs." + b + ".Z")));
		}
		lose=new Location(Bukkit.getWorld("world"),-12.5, 7, 8.5);
		won=new Location(Bukkit.getWorld("world"),30.5, 7, 8.5);
		s = Bukkit.getScoreboardManager().getMainScoreboard();
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.getPluginCommand("loc").setExecutor(new cmd());
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				online=Bukkit.getOnlinePlayers().size();
				if(secrate>=20&&Bukkit.getOnlinePlayers().size()>=2){
					if(stage.equals("setting")&&game.equals("Рандомное пвп")){
						ranpvp.sr();
					}
					for(Player p:Bukkit.getOnlinePlayers()){
						p.setFireTicks(0);
						p.setFoodLevel(20);
					}
					gameStarter.gameClass();
					if(online>=2&&game.equals("")&&stage.equals("")){
						timer=60;
						stage="wait";
					}
					if(online<=1){
						if(stage.equals("wait")){
							stage="";
							globMessage(ChatColor.RED+"Недостаточно игроков для старта.",Sound.ENTITY_VILLAGER_HURT);
							timer=60;
						}
						else if(!stage.equals("")){
							stage="won";
							globMessage(ChatColor.YELLOW+"Ваши соперники сдались.",Sound.ENTITY_VILLAGER_YES);
						}
					}
					if(timer<=0)gameStarter.selectGame();
					if(timer<=4&&Bukkit.getOnlinePlayers().size()>=2){
						globMessage(null,Sound.BLOCK_NOTE_HAT);
					}
					if(stage.equals("game")&&myScore)gameStarter.myScore();
					else updateScoreboard();
					for(Player p:Bukkit.getOnlinePlayers()){
						p.setLevel(timer);
					}
					timer--;
					gameStarter.tell();
				}
				if(secrate>=20)secrate-=20;
				if(freeze==false)secrate++;
				gameStarter.tickGC();
			}
		},0,1);
		Bukkit.getWorld("world").getWorldBorder().setCenter(locs.get("center"));
		Bukkit.getWorld("world").getWorldBorder().setSize(100);
	}
	
	public void onDisable(){
		saveDefaultConfig();
		for(String st:locs.keySet()){
			Location loc=locs.get(st);
			getConfig().set("locs."+st+".World", loc.getWorld().getName());
			getConfig().set("locs."+st+".X", loc.getX());
			getConfig().set("locs."+st+".Y", loc.getY());
			getConfig().set("locs."+st+".Z", loc.getZ());
		}
		saveConfig();
	}
	
	public static void title(String title, String subtitle, int spawn, int hold, int die, Sound sound){
		for(Player p:Bukkit.getOnlinePlayers()){
			p.sendTitle(title, subtitle, spawn, hold, die);
			if(sound!=null)p.playSound(p.getLocation(), sound, 1, 1);
		}
	}
	public static void globMessage(String message,Sound sound){
		for(Player p:Bukkit.getOnlinePlayers()){
			if(message!=null)p.sendMessage(message);
			if(sound!=null)p.playSound(p.getLocation(), sound, 1, 1);
		}
	}
	static void updateScoreboard(){
		for(Player p:Bukkit.getOnlinePlayers()){
			List<String> strings = new ArrayList<>();
			int bad = 0;
			for(Player pl:Bukkit.getOnlinePlayers()){
				if(!players.contains(pl.getName())){
					bad++;
				}
			}
			strings.add(ChatColor.DARK_GREEN+"Игра: "+ChatColor.YELLOW+game);
			strings.add(ChatColor.YELLOW+"Стадия игры: "+ChatColor.GOLD+stage);
			strings.add(ChatColor.BLUE+"");
			strings.add(ChatColor.GREEN+"В игре: "+ChatColor.YELLOW+players.size());
			strings.add(ChatColor.RED+"Не в игре: "+ChatColor.YELLOW+bad);
			strings.add(ChatColor.RED+"");
			strings.add(ChatColor.BLUE+"Время: "+ChatColor.YELLOW+main.timer+ChatColor.BLUE+" секунд.");
			strings.add(ChatColor.GOLD+""+blocked.size()+ChatColor.YELLOW+"/10 игр.");
			GepUtil.upscor(p, strings, bord);
		}
	}
	public static void clearPlatform(){
		for(int y=4;y<50;y++){
			for(Block b:getPlatform(y)){
				b.setType(Material.AIR);
			}
		}
	}
	public static ArrayList<Block> getPlatform(int y){
		int x=locs.get("center").getBlockX()-platformSize,z=locs.get("center").getBlockZ()-platformSize;
		ArrayList<Block> ret = new ArrayList<>();
		for(int dx=0;dx<platformSize*2+1;dx++){
			for(int dz=0;dz<platformSize*2+1;dz++){
				ret.add(Bukkit.getWorld("world").getBlockAt(x+dx, y, z+dz));
			}
		}
		return ret;
	}
	public static void loadSchematic(Location loc, String schName){
		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		File schematic = new File(instance.getDataFolder()+File.separator+"/schematics/"+schName+".schematic");
		EditSession session = we.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), 1000000);
		try{
			CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
			clipboard.paste(session, new Vector(loc.getX(),loc.getY(),loc.getZ()), false);
		}catch(MaxChangedBlocksException|DataException|IOException e){
			e.printStackTrace();
		}
	}
	public static Location randLocFromPlatform(int radius, boolean add){
		Location loc=locs.get("center").clone();
		loc.setX(loc.getX()+(radius-((new Random().nextInt(radius+1))+(new Random().nextInt(radius+1)))));
		loc.setZ(loc.getZ()+(radius-((new Random().nextInt(radius+1))+(new Random().nextInt(radius+1)))));
		if(add){
			loc.setX(loc.getX()+0.5);
			loc.setZ(loc.getZ()+0.5);
		}
		return loc;
	}
	public static void tpRandPlatform(Player p, int radius, boolean up){
		Location loc = randLocFromPlatform(radius, true);
		while(!loc.getBlock().getType().equals(Material.AIR)){
			if(up)loc.setY(loc.getY()+1);
			else loc=randLocFromPlatform(radius, true);
		}
		p.teleport(loc);
	}
	public static boolean locIsBlocked(Location loc){
		Location center = locs.get("center");
		if(loc.getX()-platformSize>center.getX()||loc.getX()+platformSize<center.getX())return true;
		if(loc.getZ()-platformSize>center.getZ()||loc.getZ()+platformSize<center.getZ())return true;
		if(loc.getY()<center.getY())return true;
		return false;
	}
	public static void displayTop(HashMap<String, Integer> leaders){
		for(Player p:Bukkit.getOnlinePlayers()){
			ArrayList<String> strs = new ArrayList<>();
			HashMap<String,Integer> breaks = new HashMap<>(leaders);
			for(int i=1;i<6;i++){
				strs.add(GepUtil.boolCol(i==1)+""+i+". "+GepUtil.boolCol(ChatColor.AQUA, ChatColor.YELLOW, GepUtil.leader(breaks, "name").equals(p.getName()))+GepUtil.leader(breaks, "name")+ChatColor.GRAY+" ("+ChatColor.GOLD+GepUtil.leader(breaks, "score")+ChatColor.GRAY+")");
				breaks.remove(GepUtil.leader(breaks, "name"));
			}
			strs.add(ChatColor.BLUE+"Время: "+ChatColor.YELLOW+main.timer+ChatColor.BLUE+" сек.");
			GepUtil.upscor(p, strs, ChatColor.AQUA+"---==|Gep Arcade|==---");
		}
	}
}
