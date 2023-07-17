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
			,"Русская рулетка"
			,"Математичка-босс"
			,"Рандомное пвп"
			,"Реакция"
			,"60 Seconds"
			,"Крушитель"
			,"Обезьяна с гранатой"
			,"Торт"
			,"Изумрудная лихорадка"
			,"Лава пати"
			,"Магнит"
			,"Мужицкий дождь"};
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
		if(main.game.equals("murderer"))tut(ChatColor.RED+"Убийца", ChatColor.YELLOW+"Одному игроку дают нож.", ChatColor.RED+"Он должен всех убить.", ChatColor.GREEN+"Другие должны выжить.");
		if(main.game.equals("Русская рулетка"))tut(ChatColor.RED+"Русская рулетка", ChatColor.YELLOW+"Сбоку подписывается содержимое рулетки.", ChatColor.RED+"ПКМ - выстрелить.", ChatColor.GREEN+"'Q' - сменить барабан.");
		if(main.game.equals("Математичка-босс"))tut(ChatColor.RED+"Математичка-босс", ChatColor.RED+"Математичка разгневалась из-за двоечников.", ChatColor.YELLOW+"Правельные ответы вас лечат.", ChatColor.GREEN+"Остерегайтесь этих ведьм!");
		if(main.game.equals("Рандомное пвп"))tut(ChatColor.RED+"Рандомное пвп", ChatColor.YELLOW+"Игрокам дают рандомное снаряжение.", ChatColor.RED+"Потом начинается ПВП.", ChatColor.GREEN+"Выйграет последний выживший.");
		if(main.game.equals("Реакция"))tut(ChatColor.YELLOW+"Реакция", ChatColor.YELLOW+"На экране пишут задания.", ChatColor.GOLD+"Игроки должны быстро среагировать.", ChatColor.GREEN+"Если успел - молодец)");
		if(main.game.equals("60 Seconds"))tut(ChatColor.YELLOW+"60 Seconds", ChatColor.RED+"60 секунд до ядерного взрыва!", ChatColor.YELLOW+"Чтобы выжить, нужно 10 еды.", ChatColor.GREEN+"Соберите, и прыгайте в лаву!");
		if(main.game.equals("Крушитель"))tut(ChatColor.GOLD+"Крушитель", ChatColor.RED+"Игрокам даётся динамит...", ChatColor.YELLOW+"За золото увеличивается взрыв.", ChatColor.GREEN+"Победит тот, у кого больше очков!");
		if(main.game.equals("Обезьяна с гранатой"))tut(ChatColor.DARK_GREEN+"Обезьяна с гранатой", ChatColor.YELLOW+"Остерегайтесь обезьян!", ChatColor.DARK_GREEN+"У них в руках гранаты!", ChatColor.RED+"Никто не знает, когда они взорвутся!");
		if(main.game.equals("Торт"))tut(ChatColor.LIGHT_PURPLE+"Торт", ChatColor.YELLOW+"Есть огромный торт.", ChatColor.DARK_GREEN+"Вы должны съесть его!", ChatColor.DARK_RED+"ЛЮБОЙ ЦЕНОЙ!!!");
		if(main.game.equals("Изумрудная лихорадка"))tut(ChatColor.DARK_GREEN+"Изумрудная лихорадка", ChatColor.GREEN+"Изумруды дают очки.", ChatColor.DARK_GREEN+"Ударом вы крадёте изумруд.", ChatColor.DARK_GREEN+"Так же можно красть очки.");
		if(main.game.equals("Лава пати"))tut(ChatColor.GOLD+"Лава "+ChatColor.LIGHT_PURPLE+"пати", ChatColor.YELLOW+"Вам выдадут цвет.", ChatColor.RED+"Все блоки не этого цвета ломаются.", ChatColor.GOLD+"Так же некоторые блоки горят сами.");
		if(main.game.equals("Магнит"))tut(ChatColor.BLUE+"Магнит", ChatColor.AQUA+"Алмазы дают очки.", ChatColor.GOLD+"Золото усиливает магнит.", ChatColor.WHITE+"Перья подбрасывают вверх.");
		if(main.game.equals("Мужицкий дождь"))tut(ChatColor.BLUE+"Мужицкий дождь", ChatColor.AQUA+"ЭТО МУЖИЦКИЙ ДОЖДЬ!", ChatColor.GOLD+"А-ЛЕ-ЛУЙЯ!", ChatColor.AQUA+"ДОЖДЬ ИЗ МУЖИКОВ!");
		
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
		if(main.game.equals("Русская рулетка"))rusr();
		if(main.game.equals("Математичка-босс"))matboss.start();
		if(main.game.equals("Рандомное пвп"))ranpvp.start();
		if(main.game.equals("Реакция"))reaction.start();
		if(main.game.equals("60 Seconds"))sixsec.start();
		if(main.game.equals("Крушитель"))breaker.start();
		if(main.game.equals("Обезьяна с гранатой"))monkeygre.start();
		if(main.game.equals("Торт"))games.cake.start();
		if(main.game.equals("Изумрудная лихорадка"))games.steal.start();
		if(main.game.equals("Лава пати"))games.lavaparty.start();
		if(main.game.equals("Магнит"))games.magnet.start();
		if(main.game.equals("Мужицкий дождь"))games.manrain.start();
	}
	static void murderer(){
		Player pl = Bukkit.getPlayer(main.players.get(new Random().nextInt(main.players.size())));
		pl.sendTitle(ChatColor.RED+"Вы - убийца!", ChatColor.YELLOW+"Вы получите свой нож через 5 секунд.", 10, 50, 30);
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
		if(main.game.equals("Рандомное пвп")&&main.stage.equals("setting"))games.ranpvp.setting();
		if(!main.stage.equals("game"))return;
		if(main.game.equals("murderer"))games.murderer.base();
		if(main.game.equals("Русская рулетка"))games.rr.base();
		if(main.game.equals("Математичка-босс"))matboss.base();
		if(main.game.equals("Рандомное пвп"))ranpvp.base();
		if(main.game.equals("Реакция"))reaction.base();
		if(main.game.equals("60 Seconds"))sixsec.base();
		if(main.game.equals("Крушитель"))breaker.base();
		if(main.game.equals("Обезьяна с гранатой"))monkeygre.base();
		if(main.game.equals("Торт"))games.cake.base();
		if(main.game.equals("Изумрудная лихорадка"))games.steal.base();
		if(main.game.equals("Лава пати"))games.lavaparty.base();
		if(main.game.equals("Магнит"))games.magnet.base();
		if(main.game.equals("Мужицкий дождь"))games.manrain.base();
	}
	public static void myScore(){
		String game = main.game;
		if(game.equals("Русская рулетка"))games.rr.updateScoreboard();
		if(game.equals("Математичка-босс"))games.matboss.updateScoreboard();
		if(game.equals("Крушитель"))games.breaker.updateScoreboard();
		if(game.equals("Изумрудная лихорадка"))games.steal.updateScoreboard();
		if(game.equals("Торт"))games.cake.updateScoreboard();
		if(game.equals("Магнит"))games.magnet.updateScoreboard();
		if(game.equals("Мужицкий дождь"))games.manrain.updateScoreboard();
	}
	static void tickGC(){
		if(!main.stage.equals("game"))return;
		if(main.game.equals("Магнит"))games.magnet.fast();
		if(main.game.equals("Мужицкий дождь"))games.manrain.fast();
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
				title=ChatColor.GREEN+"Вы справились!";
			}
			else{
				sub=ChatColor.YELLOW+LoseTitle;
				title=ChatColor.RED+"Вы не справились...";
			}
			p.sendTitle(title, sub, 10, 30, 20);
		}
		if(chat!=null){
			for(String st:chat){
				if(st.equals("/wons/")){
					if(winner!=null){
						st=ChatColor.GREEN+"В этом раунде победил "+ChatColor.GOLD+winner;
					}
					else if(plist){
						if(main.players.size()==0)st=ChatColor.RED+"В этом раунде никто не победил...";
						else if(main.players.size()==Bukkit.getOnlinePlayers().size())st=ChatColor.GREEN+"В этом раунде справились ВСЕ!";
						else if(main.players.size()==1){
							st=ChatColor.GREEN+"В этом раунде победил только "+ChatColor.YELLOW;
							for(String str:main.players){
								st+=str+".";
							}
						}
						else if(main.players.size()<=3){
							st=ChatColor.GREEN+"В этом раунде победили только "+ChatColor.GOLD;
							int i=1;
							for(String str:main.players){
								if(main.players.size()>1&&i==main.players.size())st+="и "+str+".";
								else st+=str+", ";
								i++;
							}
						}
						else st=ChatColor.GREEN+"В этом раунде победило "+main.players.size()+ChatColor.YELLOW+"/"+Bukkit.getOnlinePlayers().size();
					}
					else{
						if(main.players.size()==0)st=ChatColor.GREEN+"В этом раунде победили ВСЕ!";
						else if(main.players.size()==Bukkit.getOnlinePlayers().size())st=ChatColor.RED+"В этом раунде никто не справился...";
						else if(main.players.size()==1){
							st=ChatColor.GREEN+"В этом раунде побеил только "+ChatColor.YELLOW;
							for(Player p:Bukkit.getOnlinePlayers()){
								if(main.players.contains(p.getName()))continue;
								String str = p.getName();
								st+=str+".";
							}
						}
						else if(Bukkit.getOnlinePlayers().size()-main.players.size()<=3){
							st=ChatColor.GREEN+"В этом раунде победили только "+ChatColor.GOLD;
							int i=1;
							for(Player p:Bukkit.getOnlinePlayers()){
								if(main.players.contains(p.getName()))continue;
								String str = p.getName();
								if(main.players.size()>1&&i==main.players.size())st+="и "+str+".";
								else st+=str+", ";
								i++;
							}
						}
						else st=ChatColor.GREEN+"В этом раунде победило "+(Bukkit.getOnlinePlayers().size()-main.players.size())+ChatColor.YELLOW+"/"+Bukkit.getOnlinePlayers().size();
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
