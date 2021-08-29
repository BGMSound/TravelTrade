package kr.msleague.traveltrade;

import kr.msleague.traveltrade.listener.TradeCommandListener;
import kr.msleague.traveltrade.listener.TradeGUIListener;
import kr.msleague.traveltrade.trade.Trade;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class TravelTrade extends JavaPlugin {
    public static TravelTrade instance;
    public static final Set<Trade> tradeSet = new HashSet<>();
    @Override
    public void onEnable() {
        instance = this;
        getCommand("거래").setExecutor(new TradeCommandListener());
        Bukkit.getServer().getPluginManager().registerEvents(new TradeGUIListener(), TravelTrade.instance);
    }
}
