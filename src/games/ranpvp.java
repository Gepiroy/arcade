package games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import arcade.gameStarter;
import arcade.main;
import utils.ItemUtil;

public class ranpvp {
	static HashMap<String,List<String>> drops = new HashMap<>();
	static int next = 0;
	public static void start(){
		next = 0;
		main.timer=5;
		main.stage="setting";
		drops.clear();
		for(Player p:Bukkit.getOnlinePlayers()){
			List<String> list = new ArrayList<>();
			list.add(ChatColor.RED+"[���] ");list.add(ChatColor.RED+"[����] ");list.add(ChatColor.RED+"[���������] ");list.add(ChatColor.RED+"[�����] ");list.add(ChatColor.RED+"[�������] ");
			drops.put(p.getName(), list);
		}
	}
	public static void base(){
		if(main.timer<=0&&main.stage.equals("game")||main.players.size()<=1){
			String s="[��������� ���]";
			String st="";
			for(int i=0;i<s.length();i++){
				st+=ChatColor.values()[new Random().nextInt(ChatColor.values().length)]+""+s.charAt(i);
			}
			gameStarter.end("����-���)","� ��������� ��� ������...",new String[]{
					st,
					"/wons/"
			},true,null);
			return;
		}
	}
	public static void setting(){
	if(main.timer<=0&&main.stage.equals("setting")){
		main.stage="game";
		main.timer=60;
		main.title(ChatColor.RED+"FIGHT!", "", 5, 10, 5, Sound.ENTITY_VILLAGER_YES);
		main.pvp=true;
		main.lavakill=true;
		for(Player p:Bukkit.getOnlinePlayers()){
			give(p);
		}
	}
	}
	public static void sr(){
		if(main.timer<=0)return;
		for(Player p:Bukkit.getOnlinePlayers()){
			setrand(p,next);
		}
		next++;
	}
	static void setrand(Player p, int i){
		List<String> drps = drops.get(p.getName());
		String[] types = {ChatColor.AQUA+"[���.] ",ChatColor.WHITE+"[���.] ",ChatColor.YELLOW+"[���.] "};
		String type = types[new Random().nextInt(types.length)];
		drps.set(i, type);
		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
		titledrops(p);
	}
	static void titledrops(Player p){
		String st = "";
		for(String s:drops.get(p.getName())){
			st=st+s;
		}
		p.sendTitle(ChatColor.GREEN+"���� ����������:", st, 5, 20, 10);
	}
	static void give(Player p){
		if(drops.get(p.getName()).get(0).equals(ChatColor.AQUA+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.DIAMOND_SWORD, null));
		if(drops.get(p.getName()).get(0).equals(ChatColor.WHITE+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.IRON_SWORD, null));
		if(drops.get(p.getName()).get(0).equals(ChatColor.YELLOW+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.GOLD_SWORD, null));
		if(drops.get(p.getName()).get(1).equals(ChatColor.AQUA+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.DIAMOND_HELMET, null));
		if(drops.get(p.getName()).get(1).equals(ChatColor.WHITE+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.IRON_HELMET, null));
		if(drops.get(p.getName()).get(1).equals(ChatColor.YELLOW+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.GOLD_HELMET, null));
		if(drops.get(p.getName()).get(2).equals(ChatColor.AQUA+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.DIAMOND_CHESTPLATE, null));
		if(drops.get(p.getName()).get(2).equals(ChatColor.WHITE+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.IRON_CHESTPLATE, null));
		if(drops.get(p.getName()).get(2).equals(ChatColor.YELLOW+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.GOLD_CHESTPLATE, null));
		if(drops.get(p.getName()).get(3).equals(ChatColor.AQUA+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.DIAMOND_LEGGINGS, null));
		if(drops.get(p.getName()).get(3).equals(ChatColor.WHITE+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.IRON_LEGGINGS, null));
		if(drops.get(p.getName()).get(3).equals(ChatColor.YELLOW+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.GOLD_LEGGINGS, null));
		if(drops.get(p.getName()).get(4).equals(ChatColor.AQUA+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.DIAMOND_BOOTS, null));
		if(drops.get(p.getName()).get(4).equals(ChatColor.WHITE+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.IRON_BOOTS, null));
		if(drops.get(p.getName()).get(4).equals(ChatColor.YELLOW+"[���.] "))p.getInventory().addItem(ItemUtil.create(Material.GOLD_BOOTS, null));
	}
}
