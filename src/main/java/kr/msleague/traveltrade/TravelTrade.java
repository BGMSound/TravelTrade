package kr.msleague.traveltrade;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class TravelTrade extends JavaPlugin {
    //public static final String sdfasdf = "fd";
    public static TravelTrade tradeMain;
    public static final Set<Trade> tradeSet = new HashSet<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        tradeMain = this;
        getCommand("거래").setExecutor(new TradeCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new TradeGui(), TravelTrade.tradeMain);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
