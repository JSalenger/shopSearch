package com.jbsalenger.shopsearch;


import com.mongodb.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import org.bson.Document;

import java.util.ArrayList;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.bukkit.inventory.ItemStack.*;



public class Mongo {

    /*
    ConnectionString connString = new ConnectionString(
            "mongodb+srv://admin:Jqk6bvCDbTvWSU6imLxEr5krT8tA@<cluster-address>/test?w=majority"
    );
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .retryWrites(true)
            .build();

     */
    // if there's an error check here, intellij insisted on casting to MongoClient
     MongoClient mongoClient = com.mongodb.client.MongoClients.create(
             "mongodb+srv://admin:Jqk6bvCDbTvWSU6imLxEr5krT8tA@cluster0-1olrf.mongodb.net/test?retryWrites=true&w=majority");
     MongoDatabase database = mongoClient.getDatabase("mcserver");

    public Mongo() {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.OFF);
    }

    public String formatItemStackString(String unformattedItemStack) {
        unformattedItemStack.substring(9);
        char[] formattedItemStack = unformattedItemStack.toCharArray();
        return formattedItemStack.toString();
    }

    Block<Document> printBlockAsOwnerOfShop = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            UUID uuid = (UUID) document.get("uuid");

            boolean locationIsStallNumber = false;

            try {
                // If the shopLocation entered is a number than this will run fine
                // ( if its a number than its a stall number )
                int stallNumber = Integer.parseInt((String) document.get("location"));

                locationIsStallNumber = true;
            } catch(NumberFormatException e) {
                // otherwise

                locationIsStallNumber = false;
            }

            if(locationIsStallNumber) {
                Bukkit.getPlayer(uuid).sendMessage(
                        ChatColor.RED + " " + ChatColor.BOLD + "Shop at stall " + ChatColor.RESET + document.get("location")
                                + " is selling " + document.get("sale_quantity") + " " + document.get("sale_item")
                                + " for " + document.get("price_item") + " " + document.get("price_quantity")
                                + ". ( With name ) " + document.get("shop_name")
                );
            } else {
                Bukkit.getPlayer(uuid).sendMessage(
                        ChatColor.RED + " " + ChatColor.BOLD + "Shop at coords " + ChatColor.RESET + document.get("location")
                                + " is selling " + document.get("sale_quantity") + " " + document.get("sale_item")
                                + " for " + document.get("price_item") + " " + document.get("price_quantity")
                                + ". ( With name ) " + document.get("shop_name")
                );
            }

        }
    };

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            UUID uuid = (UUID) document.get("uuid");

            boolean locationIsStallNumber = false;

            try {
                // If the shopLocation entered is a number than this will run fine
                // ( if its a number than its a stall number )
                int stallNumber = Integer.parseInt((String) document.get("location"));

                locationIsStallNumber = true;
            } catch(NumberFormatException e) {
                // otherwise

                locationIsStallNumber = false;
            }

            if(locationIsStallNumber) {
                Bukkit.getPlayer(uuid).sendMessage(
                        ChatColor.RED + " " + ChatColor.BOLD + "Shop at stall " + ChatColor.RESET + document.get("location")
                                + " is selling " + document.get("sale_quantity") + " " + document.get("sale_item")
                                + " for " + document.get("price_item") + " " + document.get("price_quantity")
                                + "."
                );
            } else {
                Bukkit.getPlayer(uuid).sendMessage(
                        ChatColor.RED + " " + ChatColor.BOLD + "Shop at coords " + ChatColor.RESET + document.get("location")
                                + " is selling " + document.get("sale_quantity") + " " + document.get("sale_item")
                                + " for " + document.get("price_item") + " " + document.get("price_quantity")
                                + ". "
                );
            }

        }
    };

    public void storePlayerShop(UUID uuid, String shopName, ItemStack sale, ItemStack price, String location){
         MongoCollection<Document> collection = database.getCollection("shops");

         String formattedSaleItem = sale.getType().toString();
         formattedSaleItem.replaceAll("_", " ").toLowerCase();

        String formattedPriceItem = price.getType().toString();
        formattedPriceItem.replaceAll("_", " ").toLowerCase();

        // _id field auto-magically created
        Document document = new Document("uuid", uuid)
                .append("shop_name", shopName)
                .append("sale_item", formattedSaleItem)
                .append("sale_quantity", String.valueOf(sale.getAmount()))
                .append("price_item", formattedPriceItem)
                .append("price_quantity", String.valueOf(price.getAmount()))
                .append("location", location);

         collection.insertOne(document);
    }

    // For a player to check what shops they own
    public void listOwnedShops(UUID uuid) {
        MongoCollection<Document> collection = database.getCollection("shops");
        // Its deprecated but its what the docs say to do so idk
        collection.find(eq("uuid", uuid)).forEach(printBlockAsOwnerOfShop);
    }

    public void listShopsByItem(String sale) {
        MongoCollection<Document> collection = database.getCollection("shops");
        // Its deprecated but its what the docs say to do so idk
        collection.find(eq("sale_item", sale)).forEach(printBlock);
    }



    // You could get the second variable by getting their hand item
    // This would be quickest but they might always have the item
    // Once they want to delete the shop...
    public void deletePlayerShop(UUID uuid, ItemStack sale) {}

    // ... So they could delete by id
    public void deletePlayerShop(UUID uuid, String shopName) {
         MongoCollection<Document> collection = database.getCollection("shops");
        // Should work, fingers crossed
        collection.deleteMany(and(eq("shop_name", shopName), eq("uuid", uuid)));
    }
}
