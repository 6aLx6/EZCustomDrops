package org.example;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Entity;
import org.example.commands.MainCommand;

import java.util.ArrayList;
import java.util.List;

public class customdrops extends JavaPlugin implements Listener {

    private String prefix;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        prefix = getDescription().getPrefix();
        saveDefaultConfig();
        config = getConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        registerCommands();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &bCustomDrops activado"));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &bCustomDrops desactivado"));
    }

    public void registerCommands() {
        this.getCommand("customdrops").setExecutor(new MainCommand(this));
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        config = getConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &bLa configuración se ha recargado."));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        String entityName = entity.getType().toString().toLowerCase();

        if (config.contains("drops." + entityName)) {
            String itemName = ColorUtils.hex(config.getString("drops." + entityName + ".name"));
            List<String> lore = config.getStringList("drops." + entityName + ".lore");
            lore = translateColors(lore);
            double dropChance = config.getDouble("drops." + entityName + ".drop-chance");
            String itemTypeString = config.getString("drops." + entityName + ".item");

            Material itemType = Material.matchMaterial(itemTypeString);
            if (itemType == null) {
                getLogger().warning("El ítem " + itemTypeString + " no es válido para " + entityName + ".");
                return;
            }

            if (Math.random() <= dropChance) {
                ItemStack item = new ItemStack(itemType, 1);
                ItemMeta meta = item.getItemMeta();

                if (meta != null) {
                    meta.setDisplayName(itemName);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }

                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
    }


    private List<String> translateColors(List<String> lore) {
        List<String> translatedLore = new ArrayList<>();
        for (String line : lore) {
            translatedLore.add(ColorUtils.hex(line));
        }
        return translatedLore;
    }
}
