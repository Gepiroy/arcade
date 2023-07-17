package arcade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import games.breaker;
import games.egg;
import games.manrain;
import games.matboss;
import games.murderer;
import games.reaction;
import games.rr;
import games.sixsec;
import games.steal;
import utils.GepUtil;
import utils.ItemUtil;

@SuppressWarnings("deprecation")
public class Events implements Listener{
	public static void loose(Player p){
		p.setHealth(20);
		p.teleport(main.lose);
		p.getInventory().clear();
		if(main.players.contains(p.getName()))main.players.remove(p.getName());
		if(main.game.equals("Русская рулетка")){
			rr.leaders.remove(p.getName());
			rr.players.remove(p.getName());
		}
	}
	@EventHandler
	public void leave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(main.players.contains(p.getName()))main.players.remove(p.getName());
	}
	@EventHandler
	public void j(PlayerJoinEvent e){
		Player p = e.getPlayer();
		p.teleport(main.randLocFromPlatform(main.platformSize/3, true));
		Scoreboard newScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective o = newScoreboard.registerNewObjective("stats", "dummy");
		o.setDisplayName(ChatColor.AQUA+"Just"+ChatColor.GOLD+" Аркадка)");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(newScoreboard);
		main.updateScoreboard();
	}
	@EventHandler
	public void hurt(EntityDamageEvent e){
		if(main.game.equals("Мужицкий дождь")){
			manrain.fall(e);
		}
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(e.getCause().equals(DamageCause.LAVA)||e.getCause().equals(DamageCause.VOID)){
				lavakill(p);
			}
			if(e.getCause().equals(DamageCause.ENTITY_ATTACK)){
				if(main.pvp==false)e.setCancelled(true);
				EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
				if(main.game.equals("Изумрудная лихорадка"))steal.hurt(ee);
				if(main.game.equals("Математичка-босс")&&ee.getDamager().getType().equals(EntityType.SPLASH_POTION)){
					e.setCancelled(false);
				}
				if(ee.getDamager() instanceof Player){
					Player damager = (Player) ee.getDamager();
					if(main.game.equals("murderer")
							&&main.stage.equals("game")
							&&main.timer<=15
							&&damager.getName().equals(murderer.murderer.getName())
							&&damager.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD)){
						loose(p);
					}
					if(main.game.equals("Реакция")
							&&main.stage.equals("game")
							&&reaction.target.equals("Ударь другого игрока!")){
						if(main.players.contains(damager.getName())){
							reaction.compl(damager);
							p.setHealth(20);
						}
						else{
							e.setCancelled(true);
						}
					}
				}
			}
			if(e.getCause().equals(DamageCause.FALL)||e.getCause().equals(DamageCause.FLY_INTO_WALL)){
				e.setCancelled(true);
			}
			if(e.getCause().equals(DamageCause.BLOCK_EXPLOSION)||e.getCause().equals(DamageCause.ENTITY_EXPLOSION)){
				if(main.game.equals("Крушитель")&&!main.hard)e.setDamage(e.getDamage()/4.0);
				if(main.game.equals("Обезьяна с гранатой")&&!main.hard)e.setDamage(e.getDamage()*1.5);
				if(main.game.equals("Обезьяна с гранатой")&&main.hard)e.setDamage(e.getDamage()*2);
			}
			if(!e.isCancelled()&&p.getHealth()-e.getFinalDamage()<=0){
				e.setCancelled(true);
				loose(p);
			}
		}
		if(main.game.equals("Обезьяна с гранатой")){
			if(e.getEntityType().equals(EntityType.ZOMBIE))e.setCancelled(true);
		}
	}
	@EventHandler
	public void enev(EntityDamageByEntityEvent e){
		if(main.game.equals("Петушиные бои"))egg.enev(e);
		if(main.game.equals("Математичка-босс")&&e.getEntity().getType().equals(EntityType.WITCH)&&e.getDamager() instanceof Player){
			((Damageable) e.getDamager()).damage(5);
		}
	}
	public static void lavakill(Player p){
		if(main.game.equals("60 Seconds")){
			sixsec.save(p);
			return;
		}
		if(main.stage.equals("game")&&main.lavakill==true){
			loose(p);
		}
		else{
			if(main.game.equals("Торт")){
				main.tpRandPlatform(p, main.platformSize, false);
			}
			else p.teleport(main.randLocFromPlatform(main.platformSize/3, true));
		}
		if(main.game.equals("Крушитель")){
			breaker.tp(p);
		}
	}
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		e.setCancelled(true);
		if(main.game.equals("Русская рулетка")){
			rr.change(e.getPlayer());
		}
	}
	@EventHandler
	public void pick(PlayerPickupItemEvent e){
		if(main.game.equals("Магнит")){
			games.magnet.pickUp(e);
		}
	}
	@EventHandler
	public void interact(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if (!e.getHand().equals(EquipmentSlot.HAND)){return;}
		if (main.game.equals("Русская рулетка")&&main.stage.equals("game")&&p.getInventory().getItemInMainHand().getType().equals(Material.FERMENTED_SPIDER_EYE)){
			rr.use(p);
		}
		if(p.getGameMode().equals(GameMode.CREATIVE) && p.getInventory().getItemInMainHand().getType().equals(Material.ICE)){
			main.freeze = !main.freeze;
		}
		if(main.game.equals("Реакция") && main.stage.equals("game") && reaction.target.equals("Кликни на алмазный блок!") && e.hasBlock() && e.getClickedBlock().getType().equals(Material.DIAMOND_BLOCK) && main.players.contains(p.getName())&&!main.locIsBlocked(e.getClickedBlock().getLocation())){
			reaction.compl(p);
			if(main.hard==true)e.getClickedBlock().breakNaturally();
		}
	}
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e){
	    String mes = e.getMessage();
	    Player p = e.getPlayer();
	    if(main.game.equals("Математичка-босс")&&main.stage.equals("game")&&matboss.isNumeric(mes)&&main.players.contains(p.getName())){
	    	if(mes.length()>3){
	    		e.setCancelled(true);
	    		p.sendMessage(ChatColor.RED+"С ума сошёл?");
	    		return;
	    	}
	    	int ans = Integer.parseInt(mes);
	    	matboss.tryToAnswer(p, ans);
	    	e.setCancelled(true);
	    }
	    if(p.isOp()){
	    	if(mes.equals("hard")){
	    		main.hard=!main.hard;
	    		p.sendMessage(ChatColor.LIGHT_PURPLE+""+main.hard);
	    		e.setCancelled(true);
	    	}
	    	if(mes.equals("time")){
	    		main.timer=1;
	    		e.setCancelled(true);
	    	}
	    	for(String st:gameStarter.Games){
	    		if(st.equals(mes)){
	    			main.game=mes;
	    			main.stage="tell";
	    			main.timer = 10;
	    			main.pvp=false;
	    			main.lavakill=false;
	    			for(Player pl:Bukkit.getOnlinePlayers()){
	    				pl.getInventory().clear();
	    				pl.teleport(main.randLocFromPlatform(main.platformSize/3, true));
	    			}
	    			p.sendMessage(ChatColor.LIGHT_PURPLE+"set game to"+main.game);
	    			e.setCancelled(true);
	    		}
	    	}
	    }
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
	  Player p = e.getPlayer();
	  if(!p.getGameMode().equals(GameMode.CREATIVE)&&p.getLocation().getY()<=3.5){
			lavakill(p);
		}
	  boolean isJumping = p.getVelocity().getY() > -0.0784000015258789;
	  if(isJumping==true && main.game.equals("Реакция") && main.stage.equals("game") && reaction.target.equals("Прыгни!")){
		  if(main.players.contains(e.getPlayer().getName()))reaction.compl(e.getPlayer());
	  }
	}
	@EventHandler
	public void placeb(BlockPlaceEvent e){
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if(!main.canBuild||main.locIsBlocked(b.getLocation()))if(!p.isOp()||!p.getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
		}
		if(b.getType().equals(Material.TNT)&&main.game.equals("Крушитель")){
			if(!main.stage.equals("game"))e.setCancelled(true);
			if(!e.isCancelled()){
				b.setType(Material.AIR);
				Location loc = b.getLocation();
				TNTPrimed tnt = (TNTPrimed) b.getWorld().spawnEntity(new Location(loc.getWorld(),loc.getX()+0.5,loc.getY(),loc.getZ()+0.5), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(15);
				tnt.setYield(breaker.powers.get(p.getName()));
				breaker.tnts.put(tnt.getEntityId(), p.getName());
			}
		}
	}
	@EventHandler
	public void explode(EntityExplodeEvent e){
		if(e.getEntityType().equals(EntityType.PRIMED_TNT)){
			e.setCancelled(true);
			if(main.game.equals("Крушитель")&&main.stage.equals("game")){
				if(Bukkit.getPlayer(breaker.tnts.get(e.getEntity().getEntityId()))==null){
					return;
				}
				Player p = Bukkit.getPlayer(breaker.tnts.get(e.getEntity().getEntityId()));
				int points=0;
				for(Block b:e.blockList()){
					if(!main.locIsBlocked(b.getLocation())){
						if(b.getType().equals(Material.DIRT)){
							points+=1;
						}
						if(b.getType().equals(Material.STONE)){
							points+=2;
						}
						if(b.getType().equals(Material.IRON_ORE)){
							points+=3;
						}
						if(b.getType().equals(Material.GOLD_ORE)){
							points+=3;
							if(breaker.powers.get(p.getName())<=4.8)
							breaker.powers.replace(p.getName(), (float) (breaker.powers.get(p.getName())+0.2));
						}
						b.setType(Material.AIR);
					}
				}
				p.sendTitle(ChatColor.GREEN+"+"+ChatColor.YELLOW+points, "", 5, 10, 5);
				breaker.points.replace(p.getName(), breaker.points.get(p.getName())+points);
				p.getInventory().addItem(ItemUtil.create(Material.TNT, ChatColor.RED+"ВЗРЫВЧАТКА!"));
			}
			if(breaker.tnts.containsKey(e.getEntity().getEntityId())){
				breaker.tnts.remove(e.getEntity().getEntityId());
			}
		}
	}
	@EventHandler
	public void brb(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(main.locIsBlocked(e.getBlock().getLocation()))
		if(!p.isOp()||!p.getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
		}
		if(main.game.equals("Торт"))GepUtil.HashMapReplacer(games.cake.breaked, p.getName(), 1, false, false);
	}
	@EventHandler
	public static void thrown(PlayerEggThrowEvent e){
    	e.setHatching(false);
    }
	@EventHandler
	public void projectile(ProjectileHitEvent e){
		if(main.game.equals("Петушиные бои"))egg.spawnChick(e);
	}
}
