package arcade;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.isOp()){
			sender.sendMessage(ChatColor.RED+"Ю нот хейв пермишнс)");
			return true;
		}
		if(args.length<1){
			sender.sendMessage(ChatColor.RED+"/loc name <+>");
			return true;
		}
		Location loc = ((Player) sender).getLocation().getBlock().getLocation();
		if(args.length>1){
			loc.setX(loc.getX()+0.5);
			loc.setZ(loc.getZ()+0.5);
		}
		main.locs.put(args[0], loc);
		sender.sendMessage(ChatColor.YELLOW+"Локация "+args[0]+" добавлена.");
		return true;
	}
}
