package kr.msleague.traveltrade.trade;

import kr.msleague.traveltrade.Constants;
import kr.msleague.traveltrade.TravelTrade;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeManager {
    public static void getTradeItem(Inventory inv, Player player, boolean isLeftInv) {
        for (Integer slot : Constants.CLICKABLE_PANES) {
            if (!isLeftInv) slot += 4;
            ItemStack item = inv.getItem(slot);
            if(item == null) continue;
            if(!item.isSimilar(Constants.GUI_EMPTY_PANE)) {
                player.getInventory().addItem(item);
            }
        }
    }
    public static Trade getTradeByTarget(Player player) {
        for(Trade trade : TravelTrade.tradeSet)
            if(trade.getTargetUniqueId().equals(player.getUniqueId()))
                return trade;
        return null;
    }
    public static Trade getTradeBySender(Player player) {
        for(Trade trade : TravelTrade.tradeSet)
            if(trade.getSenderUniqueId().equals(player.getUniqueId()))
                return trade;
        return null;
    }
    public static Trade getTrade(Player player) {
        for(Trade trade : TravelTrade.tradeSet)
            if(trade.getTargetUniqueId().equals(player.getUniqueId()) || trade.getSenderUniqueId().equals(player.getUniqueId()))
                return trade;
        return null;
    }
    public static void removeTrade(Trade trade){
        TravelTrade.tradeSet.remove(trade);
    }
    public static void addTrade(Trade trade){
        TravelTrade.tradeSet.add(trade);
    }
}
