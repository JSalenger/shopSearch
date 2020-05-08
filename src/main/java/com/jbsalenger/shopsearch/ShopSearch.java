package com.jbsalenger.shopsearch;

import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.slf4j.Logger;
import java.util.logging.Level;

public class ShopSearch extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("[ShopSearch] Starting onEnable() method");

        // Register Command
        this.getCommand("addshop").setExecutor(new CommandAdd());
        this.getCommand("myshops").setExecutor(new MyShopsCommand());
        this.getCommand("shops").setExecutor(new SearchShopByItemCommand());
        this.getCommand("deleteshop").setExecutor(new DeleteShopCommand());

        getLogger().info("[ShopSearch] Finished onEnable() method");
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}