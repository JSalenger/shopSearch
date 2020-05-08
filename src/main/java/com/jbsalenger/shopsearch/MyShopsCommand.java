package com.jbsalenger.shopsearch;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.net.UnknownHostException;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class MyShopsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if(sender instanceof Player) {
            Player player = (Player) sender;

            Mongo mongo = new Mongo();

            UUID uuid = player.getUniqueId();

            mongo.listOwnedShops(uuid);
        } else {

        }

        return true;
    }
}
