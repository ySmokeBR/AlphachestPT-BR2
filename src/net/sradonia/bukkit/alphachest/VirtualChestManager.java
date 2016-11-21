package net.sradonia.bukkit.alphachest;

import java.util.logging.*;
import java.io.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class VirtualChestManager
{
    private static final String YAML_CHEST_EXTENSION = ".chest.yml";
    private final File dataFolder;
    private final Logger logger;
    private final HashMap<String, Inventory> chests;
    
    public VirtualChestManager(final File dataFolder, final Logger logger) {
        this.logger = logger;
        this.dataFolder = dataFolder;
        this.chests = new HashMap<String, Inventory>();
        this.load();
    }
    
    private void load() {
        this.dataFolder.mkdirs();
        final FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".chest.yml");
            }
        };
        File[] listFiles;
        for (int length = (listFiles = this.dataFolder.listFiles(filter)).length, i = 0; i < length; ++i) {
            final File chestFile = listFiles[i];
            final String chestFileName = chestFile.getName();
            try {
                final String playerName = chestFileName.substring(0, chestFileName.length() - 10);
                this.chests.put(playerName.toLowerCase(), InventoryIO.loadFromYaml(chestFile));
            }
            catch (Exception e) {
                this.logger.log(Level.WARNING, "Couldn't load chest file: " + chestFileName, e);
            }
        }
        this.logger.info("loaded " + this.chests.size() + " chests");
    }
    
    public int save() {
        int savedChests = 0;
        this.dataFolder.mkdirs();
        final Iterator<Map.Entry<String, Inventory>> chestIterator = this.chests.entrySet().iterator();
        while (chestIterator.hasNext()) {
            final Map.Entry<String, Inventory> entry = chestIterator.next();
            final String playerName = entry.getKey();
            final Inventory chest = entry.getValue();
            final File chestFile = new File(this.dataFolder, String.valueOf(String.valueOf(String.valueOf(playerName))) + ".chest.yml");
            if (chest == null) {
                chestFile.delete();
                chestIterator.remove();
            }
            else {
                try {
                    InventoryIO.saveToYaml(chest, chestFile);
                    ++savedChests;
                }
                catch (IOException e) {
                    this.logger.log(Level.WARNING, "Couldn't save chest file: " + chestFile.getName(), e);
                }
            }
        }
        return savedChests;
    }
    
    public Inventory getChest(final String playerName) {
        Inventory chest = this.chests.get(playerName.toLowerCase());
        if (chest == null) {
            chest = Bukkit.getServer().createInventory((InventoryHolder)null, 54);
            this.chests.put(playerName.toLowerCase(), chest);
        }
        return chest;
    }
    
    public void removeChest(final String playerName) {
        this.chests.put(playerName.toLowerCase(), null);
    }
    
    public int getChestCount() {
        return this.chests.size();
    }
}
