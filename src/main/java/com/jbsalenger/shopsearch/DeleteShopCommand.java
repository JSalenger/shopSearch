package com.jbsalenger.shopsearch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteShopCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args == null) {
            return false;
        }

        if(sender instanceof Player) {
            Player player = (Player) sender;

            Mongo mongo = new Mongo();

            mongo.deletePlayerShop(player.getUniqueId(), args[0]);
        }

        return true;
    }
}
