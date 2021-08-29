package kr.msleague.traveltrade;

import kr.msleague.bgmlib.WarningMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Trade {
    private final UUID sender;
    private final UUID target;
    private BukkitTask inviteTask;
    private Inventory senderInv;
    private Inventory targetInv;
    public Trade(Player sender, Player target) {
        this.sender = sender.getUniqueId();
        this.target = target.getUniqueId();
    }

    public UUID getSender() {
        return sender;
    }
    public Inventory getSenderInv() {
        return senderInv;
    }
    public Inventory getTargetInv() {
        return targetInv;
    }
    public UUID getTarget() {
        return target;
    }

    public Player getSenderPlayer() {
        return Bukkit.getPlayer(sender);
    }

    public Player getTargetPlayer() {
        return Bukkit.getPlayer(target);
    }

    public static Trade getTrade(Player player) {
        Trade var = null;
        for(Trade trade : TravelTrade.tradeSet) {
            if(trade.getTarget().equals(player.getUniqueId()) || trade.getSender().equals(player.getUniqueId())) {
                var = trade;
                break;
            }
        }
        return var;
    }
    public static void getTradeItem(Inventory inv, Player player, boolean isLeftInv) {
        for (Integer slot : Cons.canClick) {
            if (!isLeftInv) slot += 4;
            ItemStack item = inv.getItem(slot);
            if(item == null) continue;
            if(!item.isSimilar(Cons.glass)) {
                player.getInventory().addItem(item);
            }
        }
    }
    public static Trade getTradeByTarget(Player player) {
        Trade var = null;
        for(Trade trade : TravelTrade.tradeSet) {
            if(trade.getTarget().equals(player.getUniqueId())) {
                var = trade;
                break;
            }
        }
        return var;
    }
    public static Trade getTradeBySender(Player player) {
        Trade var = null;
        for(Trade trade : TravelTrade.tradeSet) {
            if(trade.getSender().equals(player.getUniqueId())) {
                var = trade;
                break;
            }
        }
        return var;
    }

    public void setSenderInv(Inventory senderInv) {
        this.senderInv = senderInv;
    }

    public void setTargetInv(Inventory targetInv) {
        this.targetInv = targetInv;
    }

    public void tradeInvite() {
        Trade trade = this;
        TravelTrade.tradeSet.add(trade);
        Player sender = this.getSenderPlayer();
        Player target = this.getTargetPlayer();
        inviteTask = Bukkit.getScheduler().runTaskLater(TravelTrade.tradeMain, () -> {
            if (sender.isOnline()) sender.sendMessage("15초가 지나 거래가 취소되었습니다!");
            if (target.isOnline()) target.sendMessage("15초가 지나 거래가 취소되었습니다!");
            TravelTrade.tradeSet.remove(trade);
        }, 300);
    }
    public void inviteAccept() {
        Player sender = this.getSenderPlayer();
        Player target = this.getTargetPlayer();
        if(sender == null) {
            WarningMessage.sendWarningMsg(this.getTargetPlayer(), "해당 플레이어가 오프라인입니다.");
        }
        inviteTask.cancel();
        target.sendMessage(Cons.prefix+"거래 신청이 수락되었습니다.");
        sender.sendMessage(Cons.prefix+"거래 신청이 수락되었습니다.");
        TradeGui.openGUI(this, sender);
        TradeGui.openGUI(this, target);
    }
    public void inviteDeny() {
        Player sender = this.getSenderPlayer();
        Player target = this.getTargetPlayer();
        if(sender == null) {
            WarningMessage.sendWarningMsg(this.getTargetPlayer(), "해당 플레이어가 오프라인입니다.");
        }
        inviteTask.cancel();
        TravelTrade.tradeSet.remove(this);
        target.sendMessage(Cons.prefix+"거래 신청이 취소되었습니다.");
        sender.sendMessage(Cons.prefix+"거래 신청이 취소되었습니다.");
    }
}
