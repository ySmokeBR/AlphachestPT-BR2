package net.sradonia.bukkit.alphachest.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import net.sradonia.bukkit.alphachest.*;

public class EnchantCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("alphachest.encantar")) {
                ((Player)sender).openEnchanting((Location)null, true);
            }
            else {
                Teller.tell(sender, Teller.Type.Error, "Voc\u00ea n\u00e3o tem permiss\u00e3o para usar este comando.");
            }
            return true;
        }
        return false;
    }
}
