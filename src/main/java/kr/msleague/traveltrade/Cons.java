package kr.msleague.traveltrade;

import kr.msleague.bgmlib.ItemBuild;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Cons {
    public static final String prefix = "§6§l● §f";
    public static final ItemStack tradeLock = new ItemBuild(Material.ANVIL)
            .displayname("&8&l[LOCK] &f거래창 잠그기")
            .addLore("&7 - &f거래창을 잠근 후에는 거래 물품 수정이 불가능합니다&7. &f &f &f ")
            .addLore("&f")
            .addLore("&8클릭 시 거래창을 잠금합니다.")
            .build();
    public static final ItemStack tradeNotOk = new ItemBuild(Material.STAINED_CLAY)
            .displayname("&a&l[OK] &f거래 확정하기")
            .addLore("&7 - &f클릭하여 거래를 확정합니다&7.")
            .addLore("&7 - &f상대방도 확정하면&7, &f거래가 진행됩니다&7. &f &f &f ")
            .addLore("&f")
            .addLore("&8클릭 시 거래를 확정합니다.")
            .durability((byte) DyeColor.RED.ordinal())
            .build();
    public static final ItemStack tradeOk = new ItemBuild(Material.STAINED_CLAY)
            .displayname("&c&l[X] &f거래 확정 취소하기")
            .addLore("&7 - &f클릭하여 거래 확정을 취소합니다&7.")
            .addLore("&f")
            .addLore("&8클릭 시 거래를 확정합니다.")
            .durability((byte) DyeColor.LIME.ordinal())
            .build();
    public static final ItemStack tradeLock2 = new ItemBuild(Material.ANVIL).displayname("&f ").build();
    public static final ItemStack tradeNotOk2 = new ItemBuild(Material.STAINED_CLAY).displayname("&f ").durability((byte) DyeColor.RED.ordinal()).build();
    public static final ItemStack tradeOk2 = new ItemBuild(Material.STAINED_CLAY).displayname("&f ").durability((byte) DyeColor.LIME.ordinal()).build();
    public static final int[] gslot = {9, 18, 27, 36, 13, 22, 31, 40, 17, 26, 35, 44};
    public static final Integer[] xClick = {14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43};
    public static final Integer[] canClick = {10, 11, 12, 19, 20, 21, 28, 29, 30, 37, 38, 39};
    public static ItemStack glass = new ItemBuild(Material.STAINED_GLASS_PANE).displayname("§f").durability((byte) DyeColor.SILVER.ordinal()).build();
}
