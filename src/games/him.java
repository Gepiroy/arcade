package games;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import utils.ItemUtil;

public class him {
	
	public static ItemStack himCreate(Material mat, String name, int i, int h, int s){
		return ItemUtil.create(mat, 1, name, new String[]{
				ChatColor.BLUE+"���������: "+ChatColor.AQUA+i,
				ChatColor.LIGHT_PURPLE+"��������: "+ChatColor.GREEN+i,
				ChatColor.DARK_RED+"����: "+ChatColor.RED+i,
		});
	}
}
