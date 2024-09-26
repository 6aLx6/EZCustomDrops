package org.example.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.example.customdrops;

public class MainCommand implements CommandExecutor {

    private final customdrops plugin;

    public MainCommand(customdrops plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("customdrops")) {

            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Uso: /customdrops reload");
                return false;
            }


            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "La configuración se ha recargado correctamente.");
                return true;
            } else {

                sender.sendMessage(ChatColor.RED + "Comando desconocido. Uso: /customdrops reload");
                return false;
            }
        }
        return false;
    }
}
