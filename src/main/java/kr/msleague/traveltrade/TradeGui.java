package kr.msleague.traveltrade;

import kr.msleague.bgmlib.ItemBuild;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    public void onInventory(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        Inventory clickInv = e.getClickedInventory();
        if (e.getCurrentItem() == null) return;
        if(clickInv.getName() == null) return;
        if(!clickInv.getName().equals("거래창")) return;
        ItemStack clickItem = e.getCurrentItem();
        if(clickInv.getType() == InventoryType.PLAYER) {
            if(e.getClick().isShiftClick()) e.setCancelled(true);
        } else {
            List<Integer> canClick = Arrays.asList(Cons.canClick);
            if (!canClick.contains(e.getRawSlot())) e.setCancelled(true);
            if(e.getCurrentItem().getType() != Material.AIR) {
                if (!clickItem.hasItemMeta()) return;
                if (clickItem.getItemMeta().hasDisplayName() && clickItem.getItemMeta().getDisplayName().equals("§f "))
                    return;
            }
        }
        String s = clickInv.getItem(6).getItemMeta().getDisplayName();
        Player other = Bukkit.getPlayer(s.replace("§f", ""));
        refresh((Player) e.getWhoClicked(), other);

    }
    private void refresh(Player player, Player other) {
        int i = 0;
        Inventory inv = player.getOpenInventory().getTopInventory();
        Inventory otherInv = other.getOpenInventory().getTopInventory();
        if(otherInv.getName() == null) return;
        if(!otherInv.getName().equals("거래창")) return;
        Trade var = null;
        boolean isSender = false;
        for(Trade trade : TravelTrade.tradeSet) {
            if(trade.getTarget().equals(player.getUniqueId())) {
                var = trade;
                break;
            }else if(trade.getSender().equals(player.getUniqueId())) {
                var = trade;
                isSender = true;
            }
        }
        /*for(Integer slot : Cons.canClick) {
            ItemStack item = inv.getItem(slot);
            Bukkit.broadcastMessage(item.getType()+"");
            Integer slot2 = Cons.xClick[i];
            otherInv.setItem(slot2, item);
            i++;
        }*/
    }
}
