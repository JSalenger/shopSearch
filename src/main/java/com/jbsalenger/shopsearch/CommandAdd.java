package com.jbsalenger.shopsearch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandAdd implements CommandExecutor {
    // Adds shop to list of shops, obviously
    Mongo mongo = new Mongo();

    public String convertItemStackToString(ItemStack item) {
        String amount = String.valueOf(item.getAmount());
        String name = item.getType().toString();
        return amount + " " + name;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args == null) {
            return false;
        }

        if(sender instanceof Player) {
            // Add thing

            Player player = (Player) sender;

            String playerName = player.getDisplayName();
            ItemStack itemForSale = player.getInventory().getItemInOffHand();
            ItemStack price = player.getInventory().getItemInMainHand();
            String shopName;
            String shopLocation;
            try {
                shopName = args[0].toString();
                shopLocation = args[1].toString();


            } catch(NullPointerException e) {
                // They forgot to include arguments so the args are null

                Bukkit.getPlayer(player.getUniqueId()).sendMessage("Make sure to include arguments!");

                return false;
            }

            try {
                // If the shopLocation entered is a number than this will run fine
                // ( if its a number than its a stall number so i don't need to set the location to the play pos )
                int stallNumber = Integer.parseInt(shopLocation);
            } catch(NumberFormatException e) {
                // otherwise

                String xValue = String.valueOf(player.getLocation().getBlockX());
                String yValue = String.valueOf(player.getLocation().getBlockY());
                String zValue = String.valueOf(player.getLocation().getBlockZ());

                shopLocation = "X: " + xValue + " Y: " + yValue + " Z: " + zValue;
            }


            Bukkit.getPlayer(player.getUniqueId()).sendMessage("[ShopSearch]");
            Bukkit.getPlayer(player.getUniqueId()).sendMessage(
                    ChatColor.RED + "Owner: " + ChatColor.RESET + playerName + ChatColor.BOLD
                            + " | " + ChatColor.RESET + ChatColor.RED + "Sale: " + ChatColor.RESET + itemForSale.getAmount()
                            + " " + itemForSale.getType() + ChatColor.BOLD + " | " + ChatColor.RESET + ChatColor.RED + "Price: "
                            + ChatColor.RESET +  price.getAmount()
                            + " " + price.getType() + ChatColor.BOLD + " | " + ChatColor.RESET + ChatColor.RED
                            + "Name ( Only visible to you ): " + ChatColor.RESET
                            + shopName + ChatColor.BOLD + " | " + ChatColor.RESET + ChatColor.RED +
                            "Location: " + ChatColor.RESET + shopLocation
            );



            mongo.storePlayerShop(player.getUniqueId(), shopName, itemForSale, price, shopLocation);

            Bukkit.getPlayer(player.getUniqueId()).sendMessage("Data successfully uploaded!");

        } else {
            // it's the console
            // bruh
            // main.sendMessage("You must be a player to perform this action!");
        }
        return true;
    }
}
