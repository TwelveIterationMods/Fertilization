package net.blay09.mods.fertilization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumHand;

public class TempUtils {
    public static EnumHand getItemUseHand(ItemUseContext useContext) {
        EntityPlayer player = useContext.getPlayer();
        if (player == null) {
            return EnumHand.MAIN_HAND;
        }

        return player.getHeldItemMainhand() == useContext.getItem() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
    }
}
