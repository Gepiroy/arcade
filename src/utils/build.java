package utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class build {
	List<Block> blocks = new ArrayList<>();
	public void setblock(Location loc, Material mat){
		loc.getBlock().setType(mat);
		blocks.add(loc.getBlock());
	}
	public void remblocks(){
		for(Block b:blocks){
			remblock(b);
		}
	}
	public void remblock(Block b){
		b.setType(Material.AIR);
		blocks.remove(b);
	}
}
