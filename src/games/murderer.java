package games;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import arcade.main;

public class murderer {
	public static Player murderer = null;
	public static void base(){
		if(main.timer==16){
			murderer.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
		}
		if(!main.players.contains(murderer.getName())){
			main.title(ChatColor.YELLOW+"���?",ChatColor.RED+"������ ����!",10,30,20, Sound.ENTITY_VILLAGER_DEATH);
			main.stage="won";
			main.timer=3;
			return;
		}
		if(main.players.size()<=1){
			main.title(ChatColor.YELLOW+"������ �������!",ChatColor.RED+"��� ������ ������.",10,30,20, Sound.ENTITY_VILLAGER_YES);
			main.stage="won";
			main.timer=3;
		}
		else if(main.timer<=0){
			main.title(ChatColor.GREEN+"������ ��������.",ChatColor.RED+"������ �� ����� ��������� �� ����.",10,30,20, Sound.ENTITY_VILLAGER_NO);
			main.stage="won";
			main.timer=3;
		}
	}
}
