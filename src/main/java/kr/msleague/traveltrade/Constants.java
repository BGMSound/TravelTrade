package kr.msleague.traveltrade;

import kr.msleague.bgmlib.ItemBuild;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Constants {
    public static final String MESSAGE_PREFIX = "§6§l● §f";
    public static final int WAIT_TIME = 300;
    public static final int[] BORDER_PANES = {9, 18, 27, 36, 13, 22, 31, 40, 17, 26, 35, 44};
    public static final int[] CLICKABLE_PANES = {10, 11, 12, 19, 20, 21, 28, 29, 30, 37, 38, 39};
    public static final ItemStack ITEM_FAKE_TRADE = new ItemBuild(Material.ANVIL)
            .displayname("&f ")
            .build();
    public static final ItemStack ITEM_FAKE_TRADE_COMPLETE = new ItemBuild(Material.STAINED_CLAY)
            .displayname("&f ")
            .durability((byte) DyeColor.RED.ordinal())
            .build();
    public static final ItemStack ITEM_FAKE_TRADE_COMPLETE_CANCEL = new ItemBuild(Material.STAINED_CLAY)
            .displayname("&f ")
            .durability((byte) DyeColor.LIME.ordinal())
            .build();
    public static final ItemStack GUI_EMPTY_PANE = new ItemBuild(Material.STAINED_GLASS_PANE)
            .displayname("§f")
            .durability((byte) DyeColor.SILVER.ordinal())
            .build();
    public static final ItemStack GUI_BORDER_PANE = new ItemBuild(Material.STAINED_GLASS_PANE)
            .displayname("§f")
            .durability((byte) DyeColor.GRAY.ordinal())
            .build();
    public static final ItemStack ITEM_LOCK_TRADE = new ItemBuild(Material.ANVIL)
            .displayname("&8&l[LOCK] &f거래창 잠그기")
            .addLore("&7 - &f거래창을 잠근 후에는 거래 물품 수정이 불가능합니다&7. &f &f &f ")
            .addLore("&f")
            .addLore("&8클릭 시 거래창을 잠금합니다.")
            .build();
    public static final ItemStack ITEM_TRADE_COMPLETE = new ItemBuild(Material.STAINED_CLAY)
            .displayname("&a&l[OK] &f거래 확정하기")
            .addLore("&7 - &f클릭하여 거래를 확정합니다&7.")
            .addLore("&7 - &f상대방도 확정하면&7, &f거래가 진행됩니다&7. &f &f &f ")
            .addLore("&f")
            .addLore("&8클릭 시 거래를 확정합니다.")
            .durability((byte) DyeColor.RED.ordinal())
            .build();
    public static final ItemStack ITEM_TRADE_COMPLETE_CANCEL = new ItemBuild(Material.STAINED_CLAY)
            .displayname("&c&l[X] &f거래 확정 취소하기")
            .addLore("&7 - &f클릭하여 거래 확정을 취소합니다&7.")
            .addLore("&f")
            .addLore("&8클릭 시 거래를 확정합니다.")
            .durability((byte) DyeColor.LIME.ordinal())
            .build();
    public static boolean isClickablePane(int slot){
        for(int s : CLICKABLE_PANES){
            if(s == slot)
                return true;
        }
        return false;
    }
}
