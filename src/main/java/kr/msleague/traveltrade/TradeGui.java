package kr.msleague.traveltrade;

import kr.msleague.bgmlib.ItemBuild;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TradeGui implements Listener {
    public static void openGUI(Trade trade, Player player) {
        Player other;
        UUID playerID = player.getUniqueId();
        Inventory tradeInv = Bukkit.createInventory(null, 54, "거래창");
        if(trade.getSender().equals(playerID)) {
            other = trade.getTargetPlayer();
            trade.setSenderInv(tradeInv);
        }
        else {
            other = trade.getSenderPlayer();
            trade.setTargetInv(tradeInv);
        }
        ItemStack glass = new ItemBuild(Material.STAINED_GLASS_PANE).displayname("§f").durability((byte) DyeColor.GRAY.ordinal()).build();
        ItemStack glass2 = new ItemBuild(Material.STAINED_GLASS_PANE).displayname("§f").durability((byte) DyeColor.SILVER.ordinal()).build();
        ItemStack playerSkull = new ItemBuild(Material.SKULL_ITEM).displayname("&f"+player.getName()).build();
        ItemStack targetSkull = new ItemBuild(Material.SKULL_ITEM).displayname("&f"+other.getName()).build();
        for(int i=0;i<9;i++) {
            tradeInv.setItem(i, glass);
        }
        for(int i=45;i<54;i++) {
            tradeInv.setItem(i, glass);
        }

        for(int i : Cons.gslot) {
            tradeInv.setItem(i, glass);
        }
        for(int i : Cons.xClick) {
            tradeInv.setItem(i, glass2);
        }
        tradeInv.setItem(2, playerSkull);
        tradeInv.setItem(6, targetSkull);
        tradeInv.setItem(47, Cons.tradeLock);
        tradeInv.setItem(51, Cons.tradeLock2);

        player.openInventory(tradeInv);

    }
    @EventHandler
    public void onInventoryClick1(InventoryDragEvent e) {
        Trade var = Trade.getTrade((Player) e.getWhoClicked());
        if(var != null) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        Inventory clickInv = e.getClickedInventory();
        if (e.getCurrentItem() == null) return;
        if(clickInv.getName() == null) return;
        Player player = (Player) e.getWhoClicked();
        Trade var = Trade.getTrade(player);
        boolean isSender = false;
        if (var.getSenderInv() != null) {
            isSender = var.getSenderInv().equals(clickInv);
        }
        ItemStack clickItem = e.getCurrentItem();
        if(clickInv.getType() == InventoryType.PLAYER) {
            if(e.getClick().isShiftClick()) e.setCancelled(true);
        } else {
            List<Integer> canClick = Arrays.asList(Cons.canClick);
            if (!canClick.contains(e.getRawSlot())) e.setCancelled(true);
            if(e.getCurrentItem().getType() != Material.AIR) {
                if (clickItem.getItemMeta().hasDisplayName() && clickItem.getItemMeta().getDisplayName().equals("§f "))
                    return;
            }
            if(clickItem.isSimilar(Cons.tradeLock)) {
                clickInv.setItem(e.getRawSlot(), Cons.tradeNotOk);
            }
            if(clickItem.isSimilar(Cons.tradeNotOk)) {
                clickInv.setItem(e.getRawSlot(), Cons.tradeOk);
            }
        }
        refresh((Player) e.getWhoClicked(), var, isSender);
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if(Trade.getTrade(player) != null) {
            Trade trade = Trade.getTrade(player);
            Player sender = trade.getSenderPlayer();
            Player target = trade.getTargetPlayer();
            Inventory senderInv = trade.getSenderInv();
            Inventory targetInv = trade.getTargetInv();
            TravelTrade.tradeSet.remove(trade);
            sender.sendMessage("거래가 취소되었습니다.");
            target.sendMessage("거래가 취소되었습니다.");
            Trade.getTradeItem(senderInv, sender, true);
            Trade.getTradeItem(targetInv, target, true);
            sender.closeInventory();
            target.closeInventory();
        }
    }
    private void refresh(Player player, Trade trade, boolean isSender) {
        //Inventory inv = player.getOpenInventory().getTopInventory();
        //Inventory otherInv = other.getOpenInventory().getTopInventory();
        Inventory inv = isSender?trade.getSenderInv():trade.getTargetInv();
        Inventory otherInv = isSender?trade.getTargetInv():trade.getSenderInv();
        Bukkit.getScheduler().runTaskLater(TravelTrade.tradeMain, ()-> {
            int i = 0;
            for (Integer slot : Cons.canClick) {
                ItemStack item = inv.getItem(slot);
                int oSlot = slot+4;
                i++;
                if(item == null) {
                    otherInv.setItem(oSlot, Cons.glass);
                    continue;
                }
                if(item.getType() == null) {
                    otherInv.setItem(oSlot, Cons.glass);
                    continue;
                }
                if ((item.getType() != Material.AIR)) {
                    otherInv.setItem(oSlot, item);
                } else {
                    otherInv.setItem(oSlot, Cons.glass);
                }
            }
            ItemStack item2 = inv.getItem(47);
            if(item2.isSimilar(Cons.tradeNotOk)) otherInv.setItem(51, Cons.tradeNotOk2);
            else if (item2.isSimilar(Cons.tradeOk)) otherInv.setItem(51, Cons.tradeOk2);
            if(item2.isSimilar(Cons.tradeOk) && inv.getItem(51).isSimilar(Cons.tradeOk2)) {
                endTrade(trade, trade.getSenderInv(), trade.getTargetInv());
            }
        }, 1);
     }
     private void endTrade(Trade trade, Inventory senderInv, Inventory targetInv) {
        Player sender = trade.getSenderPlayer();
        Player target = trade.getTargetPlayer();
        TravelTrade.tradeSet.remove(trade);
        sender.sendMessage("거래가 완료되었습니다.");
        target.sendMessage("거래가 완료되었습니다.");
        Trade.getTradeItem(senderInv, sender, false);
        Trade.getTradeItem(targetInv, target, false);
        sender.closeInventory();
        target.closeInventory();
     }

}
