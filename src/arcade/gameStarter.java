package arcade;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import games.breaker;
import games.matboss;
import games.monkeygre;
import games.ranpvp;
import games.reaction;
import games.sixsec;
import utils.GepUtil;

public class gameStarter {
	public static String[] Games = {
			"murderer"
			,"������� �������"
			,"�����������-����"
			,"��������� ���"
			,"�������"
			,"60 Seconds"
			,"���������"
			,"�������� � ��������"
			,"����"
			,"���������� ���������"
			,"���� ����"
			,"������"
			,"�������� �����"};
	public static void selectGame(){
		main.clearPlatform();
		if(main.blocked.size()>=10){
			main.game="";
			main.stage="";
			main.timer=60;
			main.blocked.clear();
			int i=0;
			for(;;){
				if(new File(main.instance.getDataFolder()+File.separator+"/schematics/center"+(i+1)+".schematic").exists())i++;
				else break;
			}
			int ver = new Random().nextInt(i)+1;
			main.loadSchematic(main.locs.get("center"), "center"+ver);
			return;
		}
		String game = Games[new Random().nextInt(Games.length)];
		if(main.blocked.containsKey(game)){
			selectGame();
			return;
		}
		main.game=game;
		for(String st:new ArrayList<String>(main.blocked.keySet())){
			GepUtil.HashMapReplacer(main.blocked, st, -1, true, false);
		}
		main.blocked.put(game, Games.length-2);
		main.stage="tell";
		main.timer = 10;
		main.pvp=false;
		main.lavakill=false;
		for(Player p:Bukkit.getOnlinePlayers()){
			p.getInventory().clear();
			p.teleport(main.randLocFromPlatform(main.platformSize/3, true));
		}
		int i=0;
		for(;;){
			if(new File(main.instance.getDataFolder()+File.separator+"/schematics/center"+(i+1)+".schematic").exists())i++;
			else break;
		}
		int ver = new Random().nextInt(i)+1;
		main.loadSchematic(main.locs.get("center"), "center"+ver);
	}
	static void tell(){
		if(!main.stage.equals("tell"))return;
		if(main.game.equals("murderer"))tut(ChatColor.RED+"������", ChatColor.YELLOW+"������ ������ ���� ���.", ChatColor.RED+"�� ������ ���� �����.", ChatColor.GREEN+"������ ������ ������.");
		if(main.game.equals("������� �������"))tut(ChatColor.RED+"������� �������", ChatColor.YELLOW+"����� ������������� ���������� �������.", ChatColor.RED+"��� - ����������.", ChatColor.GREEN+"'Q' - ������� �������.");
		if(main.game.equals("�����������-����"))tut(ChatColor.RED+"�����������-����", ChatColor.RED+"����������� ������������ ��-�� ����������.", ChatColor.YELLOW+"���������� ������ ��� �����.", ChatColor.GREEN+"������������� ���� �����!");
		if(main.game.equals("��������� ���"))tut(ChatColor.RED+"��������� ���", ChatColor.YELLOW+"������� ���� ��������� ����������.", ChatColor.RED+"����� ���������� ���.", ChatColor.GREEN+"�������� ��������� ��������.");
		if(main.game.equals("�������"))tut(ChatColor.YELLOW+"�������", ChatColor.YELLOW+"�� ������ ����� �������.", ChatColor.GOLD+"������ ������ ������ ������������.", ChatColor.GREEN+"���� ����� - �������)");
		if(main.game.equals("60 Seconds"))tut(ChatColor.YELLOW+"60 Seconds", ChatColor.RED+"60 ������ �� �������� ������!", ChatColor.YELLOW+"����� ������, ����� 10 ���.", ChatColor.GREEN+"��������, � �������� � ����!");
		if(main.game.equals("���������"))tut(ChatColor.GOLD+"���������", ChatColor.RED+"������� ����� �������...", ChatColor.YELLOW+"�� ������ ������������� �����.", ChatColor.GREEN+"������� ���, � ���� ������ �����!");
		if(main.game.equals("�������� � ��������"))tut(ChatColor.DARK_GREEN+"�������� � ��������", ChatColor.YELLOW+"������������� �������!", ChatColor.DARK_GREEN+"� ��� � ����� �������!", ChatColor.RED+"����� �� �����, ����� ��� ���������!");
		if(main.game.equals("����"))tut(ChatColor.LIGHT_PURPLE+"����", ChatColor.YELLOW+"���� �������� ����.", ChatColor.DARK_GREEN+"�� ������ ������ ���!", ChatColor.DARK_RED+"����� �����!!!");
		if(main.game.equals("���������� ���������"))tut(ChatColor.DARK_GREEN+"���������� ���������", ChatColor.GREEN+"�������� ���� ����.", ChatColor.DARK_GREEN+"������ �� ������ �������.", ChatColor.DARK_GREEN+"��� �� ����� ������ ����.");
		if(main.game.equals("���� ����"))tut(ChatColor.GOLD+"���� "+ChatColor.LIGHT_PURPLE+"����", ChatColor.YELLOW+"��� ������� ����.", ChatColor.RED+"��� ����� �� ����� ����� ��������.", ChatColor.GOLD+"��� �� ��������� ����� ����� ����.");
		if(main.game.equals("������"))tut(ChatColor.BLUE+"������", ChatColor.AQUA+"������ ���� ����.", ChatColor.GOLD+"������ ��������� ������.", ChatColor.WHITE+"����� ������������ �����.");
		if(main.game.equals("�������� �����"))tut(ChatColor.BLUE+"�������� �����", ChatColor.AQUA+"��� �������� �����!", ChatColor.GOLD+"�-��-����!", ChatColor.AQUA+"����� �� �������!");
		
		for(Player p:Bukkit.getOnlinePlayers()){
			p.setHealth(20);
		}
	}
	static void tut(String title, String sub1, String sub2, String sub3){
		if(main.timer==9){
			main.title(title, sub1, 5, 80, 5, Sound.ENTITY_VILLAGER_AMBIENT);
			for(Player p:Bukkit.getOnlinePlayers()){
				p.sendMessage(ChatColor.GOLD+"=="+title+ChatColor.GOLD+"==");
				p.sendMessage(sub1);p.sendMessage(sub2);p.sendMessage(sub3);
				p.sendMessage(ChatColor.GOLD+"=="+title+ChatColor.GOLD+"==");
			}
		}
		if(main.timer==7) main.title(title, sub2, 0, 80, 5, Sound.ENTITY_VILLAGER_AMBIENT);
		if(main.timer==5) main.title(title, sub3, 0, 80, 5, Sound.ENTITY_VILLAGER_AMBIENT);
		if(main.timer<=3&&main.timer>=0){
			if(main.timer==0) main.title(ChatColor.GREEN+"GO!", "", 0, 10, 10, Sound.ENTITY_VILLAGER_YES);
			else main.title(title, ChatColor.YELLOW+""+main.timer, 0, 100, 5, null);
		}
		if(main.timer==0)main.stage="game";
		if(main.timer==0)StartGame();
	}
	static void StartGame(){
		main.players.clear();
		for(Player p:Bukkit.getOnlinePlayers()){
			main.players.add(p.getName());
		}
		if(main.game.equals("murderer"))murderer();
		if(main.game.equals("������� �������"))rusr();
		if(main.game.equals("�����������-����"))matboss.start();
		if(main.game.equals("��������� ���"))ranpvp.start();
		if(main.game.equals("�������"))reaction.start();
		if(main.game.equals("60 Seconds"))sixsec.start();
		if(main.game.equals("���������"))breaker.start();
		if(main.game.equals("�������� � ��������"))monkeygre.start();
		if(main.game.equals("����"))games.cake.start();
		if(main.game.equals("���������� ���������"))games.steal.start();
		if(main.game.equals("���� ����"))games.lavaparty.start();
		if(main.game.equals("������"))games.magnet.start();
		if(main.game.equals("�������� �����"))games.manrain.start();
	}
	static void murderer(){
		Player pl = Bukkit.getPlayer(main.players.get(new Random().nextInt(main.players.size())));
		pl.sendTitle(ChatColor.RED+"�� - ������!", ChatColor.YELLOW+"�� �������� ���� ��� ����� 5 ������.", 10, 50, 30);
		main.timer=20;
		games.murderer.murderer=pl;
	}
	static void rusr(){
		main.timer=60;
		main.myScore=true;
		for(Player p:Bukkit.getOnlinePlayers()){
			games.rr.change(p);
			games.rr.first(p);
		}
	}
	static void gameClass(){
		if(main.game.equals("��������� ���")&&main.stage.equals("setting"))games.ranpvp.setting();
		if(!main.stage.equals("game"))return;
		if(main.game.equals("murderer"))games.murderer.base();
		if(main.game.equals("������� �������"))games.rr.base();
		if(main.game.equals("�����������-����"))matboss.base();
		if(main.game.equals("��������� ���"))ranpvp.base();
		if(main.game.equals("�������"))reaction.base();
		if(main.game.equals("60 Seconds"))sixsec.base();
		if(main.game.equals("���������"))breaker.base();
		if(main.game.equals("�������� � ��������"))monkeygre.base();
		if(main.game.equals("����"))games.cake.base();
		if(main.game.equals("���������� ���������"))games.steal.base();
		if(main.game.equals("���� ����"))games.lavaparty.base();
		if(main.game.equals("������"))games.magnet.base();
		if(main.game.equals("�������� �����"))games.manrain.base();
	}
	public static void myScore(){
		String game = main.game;
		if(game.equals("������� �������"))games.rr.updateScoreboard();
		if(game.equals("�����������-����"))games.matboss.updateScoreboard();
		if(game.equals("���������"))games.breaker.updateScoreboard();
		if(game.equals("���������� ���������"))games.steal.updateScoreboard();
		if(game.equals("����"))games.cake.updateScoreboard();
		if(game.equals("������"))games.magnet.updateScoreboard();
		if(game.equals("�������� �����"))games.manrain.updateScoreboard();
	}
	static void tickGC(){
		if(!main.stage.equals("game"))return;
		if(main.game.equals("������"))games.magnet.fast();
		if(main.game.equals("�������� �����"))games.manrain.fast();
	}
	public static void end(String WonTitle, String LoseTitle, String[] chat, boolean plist, String winner){
		main.pvp=false;
		main.lavakill=false;
		main.canBuild=false;
		main.myScore=false;
		main.clearPlatform();
		for(Entity en:Bukkit.getWorld("world").getEntities()){
			if(!(en instanceof Player)){
				en.remove();
			}
		}
		for(Player p:Bukkit.getOnlinePlayers()){
			boolean win=main.players.contains(p.getName())==plist;
			if(winner!=null)win=winner.contains(p.getName());
			String title = "";
			String sub = "";
			if(win){
				sub=ChatColor.YELLOW+WonTitle;
				title=ChatColor.GREEN+"�� ����������!";
			}
			else{
				sub=ChatColor.YELLOW+LoseTitle;
				title=ChatColor.RED+"�� �� ����������...";
			}
			p.sendTitle(title, sub, 10, 30, 20);
		}
		if(chat!=null){
			for(String st:chat){
				if(st.equals("/wons/")){
					if(winner!=null){
						st=ChatColor.GREEN+"� ���� ������ ������� "+ChatColor.GOLD+winner;
					}
					else if(plist){
						if(main.players.size()==0)st=ChatColor.RED+"� ���� ������ ����� �� �������...";
						else if(main.players.size()==Bukkit.getOnlinePlayers().size())st=ChatColor.GREEN+"� ���� ������ ���������� ���!";
						else if(main.players.size()==1){
							st=ChatColor.GREEN+"� ���� ������ ������� ������ "+ChatColor.YELLOW;
							for(String str:main.players){
								st+=str+".";
							}
						}
						else if(main.players.size()<=3){
							st=ChatColor.GREEN+"� ���� ������ �������� ������ "+ChatColor.GOLD;
							int i=1;
							for(String str:main.players){
								if(main.players.size()>1&&i==main.players.size())st+="� "+str+".";
								else st+=str+", ";
								i++;
							}
						}
						else st=ChatColor.GREEN+"� ���� ������ �������� "+main.players.size()+ChatColor.YELLOW+"/"+Bukkit.getOnlinePlayers().size();
					}
					else{
						if(main.players.size()==0)st=ChatColor.GREEN+"� ���� ������ �������� ���!";
						else if(main.players.size()==Bukkit.getOnlinePlayers().size())st=ChatColor.RED+"� ���� ������ ����� �� ���������...";
						else if(main.players.size()==1){
							st=ChatColor.GREEN+"� ���� ������ ������ ������ "+ChatColor.YELLOW;
							for(Player p:Bukkit.getOnlinePlayers()){
								if(main.players.contains(p.getName()))continue;
								String str = p.getName();
								st+=str+".";
							}
						}
						else if(Bukkit.getOnlinePlayers().size()-main.players.size()<=3){
							st=ChatColor.GREEN+"� ���� ������ �������� ������ "+ChatColor.GOLD;
							int i=1;
							for(Player p:Bukkit.getOnlinePlayers()){
								if(main.players.contains(p.getName()))continue;
								String str = p.getName();
								if(main.players.size()>1&&i==main.players.size())st+="� "+str+".";
								else st+=str+", ";
								i++;
							}
						}
						else st=ChatColor.GREEN+"� ���� ������ �������� "+(Bukkit.getOnlinePlayers().size()-main.players.size())+ChatColor.YELLOW+"/"+Bukkit.getOnlinePlayers().size();
					}
				}
				main.globMessage(st, null);
			}
			main.globMessage(ChatColor.GOLD+"-----------------", null);
		}
		main.stage="won";
		main.timer=3;
	}
}
