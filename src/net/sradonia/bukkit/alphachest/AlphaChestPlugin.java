package net.sradonia.bukkit.alphachest;

import org.bukkit.plugin.java.*;
import java.util.logging.*;
import java.io.*;
import org.bukkit.command.*;
import net.sradonia.bukkit.alphachest.commands.*;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

public class AlphaChestPlugin extends JavaPlugin implements Listener
{
    public static AlphaChestPlugin instance;
    private Logger logger;
    private VirtualChestManager chestManager;
    private boolean clearOnDeath;
    private boolean dropOnDeath;
    
    public static AlphaChestPlugin getInstance() {
        return AlphaChestPlugin.instance;
    }
    
    public void onEnable() {
        this.logger = this.getLogger();
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
        final File chestFolder = new File(this.getDataFolder(), "chests");
        this.chestManager = new VirtualChestManager(chestFolder, this.getLogger());
        this.clearOnDeath = this.getConfig().getBoolean("clearOnDeath");
        this.dropOnDeath = this.getConfig().getBoolean("dropOnDeath");
        final ChestCommands chestCommands = new ChestCommands(this.chestManager);
        this.getCommand("bau").setExecutor((CommandExecutor)chestCommands);
        this.getCommand("limparbau").setExecutor((CommandExecutor)chestCommands);
        this.getCommand("salvarbaus").setExecutor((CommandExecutor)chestCommands);
        this.getCommand("craftar").setExecutor((CommandExecutor)new WorkbenchCommand());
        this.getCommand("encantar").setExecutor((CommandExecutor)new EnchantCommand());
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        final int autosaveInterval = this.getConfig().getInt("autosave") * 1200;
        if (autosaveInterval > 0) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    final int savedChests = AlphaChestPlugin.this.chestManager.save();
                    if (savedChests > 0 && !AlphaChestPlugin.this.getConfig().getBoolean("silentAutosave")) {
                        AlphaChestPlugin.this.logger.info("auto-saved " + savedChests + " chests");
                    }
                }
            }, (long)autosaveInterval, (long)autosaveInterval);
        }
    }
    
    public void onDisable() {
        final int savedChests = this.chestManager.save();
        this.logger.info("saved " + savedChests + " chests");
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        boolean drop = this.dropOnDeath;
        boolean clear = this.dropOnDeath || this.clearOnDeath;
        if (player.hasPermission("alphachest.keepOnDeath")) {
            drop = false;
            clear = false;
        }
        else if (player.hasPermission("alphachest.dropOnDeath")) {
            drop = true;
            clear = true;
        }
        else if (player.hasPermission("alphachest.clearOnDeath")) {
            drop = false;
            clear = true;
        }
        if (drop) {
            final List<ItemStack> drops = (List<ItemStack>)event.getDrops();
            final Inventory chest = this.chestManager.getChest(player.getName());
            for (int i = 0; i < chest.getSize(); ++i) {
                drops.add(chest.getItem(i));
            }
        }
        if (clear) {
            this.chestManager.removeChest(player.getName());
        }
    }
}
