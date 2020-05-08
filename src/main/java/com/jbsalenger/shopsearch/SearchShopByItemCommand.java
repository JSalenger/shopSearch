package com.jbsalenger.shopsearch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SearchShopByItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args == null) {
            return false;
        }

        if(sender instanceof Player) {
            Player player = (Player) sender;

            Mongo mongo = new Mongo();

            UUID uuid = player.getUniqueId();

            String item = args[0];

            mongo.listShopsByItem(item.toUpperCase());

            return true;
        }

        return true;
    }
}
