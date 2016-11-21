package net.sradonia.bukkit.alphachest;

import org.bukkit.command.*;
import org.bukkit.craftbukkit.Main;
import org.bukkit.*;

public class Teller
{
    public static void tell(final CommandSender sender, final Type type, final String message) {
        ChatColor color = ChatColor.WHITE;
        switch (type) {
            case Info: {
                color = ChatColor.YELLOW;
                break;
            }
            case Success: {
                color = ChatColor.DARK_AQUA;
                break;
            }
            case Warning: {
                color = ChatColor.RED;
                break;
            }
            case Error: {
                color = ChatColor.RED;
                break;
            }
            case Misc: {
                color = ChatColor.DARK_BLUE;
                break;
            }
        }
        sender.sendMessage(ChatColor.YELLOW + "§e[Bau] §4» " + color + message);
    }
    
    public enum Type
    {
        Info("Info", 0, "Info", 0, "Info", 0), 
        Success("Success", 1, "Success", 1, "Success", 1), 
        Warning("Warning", 2, "Warning", 2, "Warning", 2), 
        Error("Error", 3, "Error", 3, "Error", 3), 
        Misc("Misc", 4, "Misc", 4, "Misc", 4);
        
        private Type(final String s3, final int n3, final String s2, final int n2, final String s, final int n) {
        }
    }
}
