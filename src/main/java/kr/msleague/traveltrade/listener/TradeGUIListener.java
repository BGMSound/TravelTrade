package kr.msleague.traveltrade.listener;

import kr.msleague.traveltrade.Constants;
import kr.msleague.traveltrade.trade.Trade;
import kr.msleague.traveltrade.trade.TradeManager;
import kr.msleague.traveltrade.TravelTrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick1(InventoryDragEvent e) {
        Trade var = TradeManager.getTrade((Player) e.getWhoClicked());
        if(var != null && e.getInventory() != null && (var.getSenderInv().equals(e.getInventory()) || var.getTargetInv().equals(e.getInventory()))) {
            refresh(var, var.getSenderInv().equals(e.getInventory()));
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory clickInv = e.getClickedInventory();
        Player player = (Player) e.getWhoClicked();
        Trade var = TradeManager.getTrade(player);
        if(e.getClickedInventory() == null || e.getCurrentItem() == null || clickInv.getName() == null || var == null) return;
        boolean isSender = false;
        if (var.getSenderInv() != null) {
            isSender = var.getSenderInv().equals(clickInv);
        }
        ItemStack clickItem = e.getCurrentItem();
        if(clickInv.getType() == InventoryType.PLAYER) {
            if(e.getClick().isShiftClick()) e.setCancelled(true);
        } else {

            if (!Constants.isClickablePane(e.getRawSlot())) e.setCancelled(true);
            if(e.getCurrentItem().getType() != Material.AIR) {
                if (clickItem.getItemMeta().hasDisplayName() && clickItem.getItemMeta().getDisplayName().equals("§f "))
                    return;
            }
            if(clickItem.isSimilar(Constants.ITEM_LOCK_TRADE)) {
                clickInv.setItem(e.getRawSlot(), Constants.ITEM_TRADE_COMPLETE);
            }
            else if(clickItem.isSimilar(Constants.ITEM_TRADE_COMPLETE)) {
                clickInv.setItem(e.getRawSlot(), Constants.ITEM_TRADE_COMPLETE_CANCEL);
            }
        }
        refresh(var, isSender);
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        Trade trade = TradeManager.getTrade(player);
        if(trade != null) {
            Player target = trade.getTargetPlayer();
            Player sender = trade.getSenderPlayer();
            TradeManager.removeTrade(trade);
            sender.sendMessage("거래가 취소되었습니다.");
            target.sendMessage("거래가 취소되었습니다.");
            TradeManager.getTradeItem(trade.getSenderInv(), sender, true);
            TradeManager.getTradeItem(trade.getTargetInv(), target, true);
            sender.closeInventory();
            target.closeInventory();
        }
    }
    private void refresh(Trade trade, boolean isSender) {
        Inventory inv = isSender?trade.getSenderInv():trade.getTargetInv();
        Inventory otherInv = isSender?trade.getTargetInv():trade.getSenderInv();
        Bukkit.getScheduler().runTaskLater(TravelTrade.instance, ()-> {
            for (int i = 0; i < Constants.CLICKABLE_PANES.length; i++) {
                int slot = Constants.CLICKABLE_PANES[i];
                ItemStack item = inv.getItem(slot);
                int oSlot = slot+4;
                if(item == null || item.getType() == null) {
                    otherInv.setItem(oSlot, Constants.GUI_EMPTY_PANE);
                    continue;
                }
                otherInv.setItem(oSlot, item.getType() != Material.AIR ? item : Constants.GUI_EMPTY_PANE);
            }
            ItemStack item2 = inv.getItem(47);
            if(item2 == null)
                return;
            if(item2.isSimilar(Constants.ITEM_TRADE_COMPLETE))
                otherInv.setItem(51, Constants.ITEM_FAKE_TRADE_COMPLETE);
            else if (item2.isSimilar(Constants.ITEM_TRADE_COMPLETE_CANCEL))
                otherInv.setItem(51, Constants.ITEM_FAKE_TRADE_COMPLETE_CANCEL);
            else if(item2.isSimilar(Constants.ITEM_TRADE_COMPLETE_CANCEL) && inv.getItem(51).isSimilar(Constants.ITEM_FAKE_TRADE_COMPLETE_CANCEL))
                endTrade(trade, trade.getSenderInv(), trade.getTargetInv());
        }, 1L);
    }
    private void endTrade(Trade trade, Inventory senderInv, Inventory targetInv) {
        Player sender = trade.getSenderPlayer();
        Player target = trade.getTargetPlayer();
        TravelTrade.tradeSet.remove(trade);
        sender.sendMessage("거래가 완료되었습니다.");
        target.sendMessage("거래가 완료되었습니다.");
        TradeManager.getTradeItem(senderInv, sender, false);
        TradeManager.getTradeItem(targetInv, target, false);
        sender.closeInventory();
        target.closeInventory();
    }
}
