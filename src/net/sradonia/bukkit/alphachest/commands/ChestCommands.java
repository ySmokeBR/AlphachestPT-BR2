package net.sradonia.bukkit.alphachest.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import net.sradonia.bukkit.alphachest.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class ChestCommands implements CommandExecutor
{
    private final VirtualChestManager chestManager;
    
    public ChestCommands(final VirtualChestManager chestManager) {
        this.chestManager = chestManager;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final String name = command.getName();
        if (name.equals("bau")) {
            return this.performChestCommand(sender, args);
        }
        if (name.equalsIgnoreCase("limparbau")) {
            return this.performClearChestCommand(sender, args);
        }
        return name.equalsIgnoreCase("salvarbaus") && this.performSaveChestsCommand(sender, args);
    }
    
    private boolean performChestCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            Teller.tell(sender, Teller.Type.Error, "Apenas os jogadores s\u00e3o capazes de abrir ba\u00fas.");
            return true;
        }
        final Player player = (Player)sender;
        if (player.getGameMode().equals((Object)GameMode.CREATIVE) && !player.hasPermission("alphachest.chest.creativeMode")) {
            Teller.tell(sender, Teller.Type.Error, "Voc\u00ea n\u00e3o pode abrir o bau no modo criativo!");
            return true;
        }
        if (args.length == 0) {
            if (sender.hasPermission("alphachest.bau")) {
                final Inventory chest = this.chestManager.getChest(sender.getName());
                player.openInventory(chest);
            }
            else {
                Teller.tell(sender, Teller.Type.Error, "Voc\u00ea n\u00e3o tem permiss\u00e3o para usar este comando.");
            }
            return true;
        }
        if (args.length == 1) {
            if (sender.hasPermission("alphachest.admin")) {
                final Inventory chest = this.chestManager.getChest(args[0]);
                player.openInventory(chest);
            }
            else {
                Teller.tell(sender, Teller.Type.Error, "Voc\u00ea n\u00e3o tem permiss\u00e3o para usar este comando.");
            }
            return true;
        }
        return false;
    }
    
    private boolean performClearChestCommand(final CommandSender sender, final String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            if (sender.hasPermission("alphachest.limpar")) {
                this.chestManager.removeChest(sender.getName());
                Teller.tell(sender, Teller.Type.Success, "Seu ba\u00fa foi limpado com sucesso");
            }
            else {
                Teller.tell(sender, Teller.Type.Error, "Voc\u00ea n\u00e3o tem permiss\u00e3o para usar este comando.");
            }
            return true;
        }
        if (args.length == 1) {
            if (sender.hasPermission("alphachest.admin")) {
                this.chestManager.removeChest(args[0]);
                Teller.tell(sender, Teller.Type.Success, "Bau do " + args[0] + " foi limpado.");
            }
            else {
                Teller.tell(sender, Teller.Type.Error, "Voc\u00ea n\u00e3o tem permiss\u00e3o para usar este comando.");
            }
            return true;
        }
        return false;
    }
    
    private boolean performSaveChestsCommand(final CommandSender sender, final String[] args) {
        if (sender.hasPermission("alphachest.save")) {
            final int savedChests = this.chestManager.save();
            Teller.tell(sender, Teller.Type.Success, "Foram salvos " + savedChests + " baus.");
        }
        else {
            Teller.tell(sender, Teller.Type.Error, "Voc\u00ea n\u00e3o tem permiss\u00e3o para usar este comando.");
        }
        return true;
    }
}
