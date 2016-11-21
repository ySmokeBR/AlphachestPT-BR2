package net.sradonia.bukkit.alphachest;

import org.bukkit.*;
import org.bukkit.inventory.*;
import java.io.*;
import org.bukkit.configuration.file.*;
import org.bukkit.configuration.*;

public class InventoryIO
{
    public static Inventory loadFromTextfile(final File file) throws IOException {
        final Inventory inventory = Bukkit.getServer().createInventory((InventoryHolder)null, 54);
        final BufferedReader in = new BufferedReader(new FileReader(file));
        int slot = 0;
        String line;
        while ((line = in.readLine()) != null) {
            if (!line.equals("")) {
                final String[] parts = line.split(":");
                try {
                    final int type = Integer.parseInt(parts[0]);
                    final int amount = Integer.parseInt(parts[1]);
                    final short damage = Short.parseShort(parts[2]);
                    if (type != 0) {
                        inventory.setItem(slot, new ItemStack(type, amount, damage));
                    }
                }
                catch (NumberFormatException ex) {}
                ++slot;
            }
        }
        in.close();
        return inventory;
    }
    
    public static Inventory loadFromYaml(final File file) throws IOException, InvalidConfigurationException {
        final YamlConfiguration yaml = new Utf8YamlConfiguration();
        yaml.load(file);
        final int inventorySize = yaml.getInt("size", 54);
        final Inventory inventory = Bukkit.getServer().createInventory((InventoryHolder)null, inventorySize);
        final ConfigurationSection items = yaml.getConfigurationSection("items");
        for (int slot = 0; slot < inventorySize; ++slot) {
            final String slotString = String.valueOf(slot);
            if (items.isItemStack(slotString)) {
                final ItemStack itemStack = items.getItemStack(slotString);
                inventory.setItem(slot, itemStack);
            }
        }
        return inventory;
    }
    
    public static void saveToYaml(final Inventory inventory, final File file) throws IOException {
        final YamlConfiguration yaml = new Utf8YamlConfiguration();
        final int inventorySize = inventory.getSize();
        yaml.set("size", (Object)inventorySize);
        final ConfigurationSection items = yaml.createSection("items");
        for (int slot = 0; slot < inventorySize; ++slot) {
            final ItemStack stack = inventory.getItem(slot);
            if (stack != null) {
                items.set(String.valueOf(slot), (Object)stack);
            }
        }
        yaml.save(file);
    }
}
