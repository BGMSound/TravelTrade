package kr.msleague.traveltrade.trade;

import kr.msleague.bgmlib.ItemBuild;
import kr.msleague.bgmlib.WarningMessage;
import kr.msleague.traveltrade.Constants;
import kr.msleague.traveltrade.TravelTrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Trade {
    private final UUID senderUniqueId, targetUniqueId;
    private final Player sender, target;
    private BukkitTask inviteTask;
    private Inventory senderInv, targetInv;
    public Trade(Player sender, Player target) {
        this.sender = sender;
        this.target = target;
        this.senderUniqueId = sender.getUniqueId();
        this.targetUniqueId = target.getUniqueId();
    }
    public UUID getSenderUniqueId() {
        return senderUniqueId;
    }
    public UUID getTargetUniqueId() {
        return targetUniqueId;
    }
    public Inventory getSenderInv() { return senderInv; }
    public Inventory getTargetInv() {
        return targetInv;
    }
    public Player getSenderPlayer() {
        return sender;
    }
    public Player getTargetPlayer() {
        return target;
    }
    public void setSenderInv(Inventory senderInv) {
        this.senderInv = senderInv;
    }
    public void setTargetInv(Inventory targetInv) {
        this.targetInv = targetInv;
    }

    public void tradeInvite() {
        Trade trade = this;
        TradeManager.addTrade(trade);
        inviteTask = Bukkit.getScheduler().runTaskLater(TravelTrade.instance, () -> {
            sender.sendMessage("15초가 지나 거래가 취소되었습니다!");
            target.sendMessage("15초가 지나 거래가 취소되었습니다!");
            TradeManager.remove(trade);
        }, Constants.WAIT_TIME);
    }
    public void inviteAccept() {
        if(!inviteTask.isCancelled())
            inviteTask.cancel();
        if(!sender.isOnline()) {
            WarningMessage.sendWarningMsg(this.getTargetPlayer(), "해당 플레이어가 오프라인입니다.");
            return;
        }
        target.sendMessage(Constants.MESSAGE_PREFIX +"거래 신청이 수락되었습니다.");
        sender.sendMessage(Constants.MESSAGE_PREFIX +"거래 신청이 수락되었습니다.");
        this.openGUI(sender);
        this.openGUI(target);
    }
    public void inviteDeny() {
        if(!inviteTask.isCancelled())
            inviteTask.cancel();
        if(!sender.isOnline()) {
            WarningMessage.sendWarningMsg(this.getTargetPlayer(), "해당 플레이어가 오프라인입니다.");
            return;
        }
        TradeManager.removeTrade(this);
        target.sendMessage(Constants.MESSAGE_PREFIX +"거래 신청이 취소되었습니다.");
        sender.sendMessage(Constants.MESSAGE_PREFIX +"거래 신청이 취소되었습니다.");
    }
    private void openGUI(Player player) {
        Player other;
        Inventory thisInv = Bukkit.createInventory(null, 54, "거래창");
        if(this.getSenderUniqueId().equals(player.getUniqueId())) {
            other = this.getTargetPlayer();
            this.setSenderInv(thisInv);
        }
        else {
            other = this.getSenderPlayer();
            this.setTargetInv(thisInv);
        }
        ItemStack playerSkull = new ItemBuild(Material.SKULL_ITEM).displayname("&f"+player.getName()).build();
        ItemStack targetSkull = new ItemBuild(Material.SKULL_ITEM).displayname("&f"+other.getName()).build();
        Bukkit.getScheduler().runTaskAsynchronously(TravelTrade.instance, ()->{
            SkullMeta playerSkullMeta = (SkullMeta) playerSkull.getItemMeta();
            playerSkullMeta.setOwningPlayer(player);
            SkullMeta targetSkullMeta = (SkullMeta) targetSkull.getItemMeta();
            targetSkullMeta.setOwningPlayer(other);
        });
        for(int i=0;i<9;i++) {
            thisInv.setItem(i, Constants.GUI_BORDER_PANE);
        }
        for(int i=45;i<54;i++) {
            thisInv.setItem(i, Constants.GUI_BORDER_PANE);
        }

        for(int i : Constants.BORDER_PANES) {
            thisInv.setItem(i, Constants.GUI_BORDER_PANE);
        }
        for(int i : Constants.CLICKABLE_PANES) {
            thisInv.setItem(i + 4, Constants.GUI_EMPTY_PANE);
        }
        thisInv.setItem(2, playerSkull);
        thisInv.setItem(6, targetSkull);
        thisInv.setItem(47, Constants.ITEM_LOCK_TRADE);
        thisInv.setItem(51, Constants.ITEM_FAKE_TRADE);

        player.openInventory(thisInv);
    }
}
