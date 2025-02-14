package baubles.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * This interface should be extended by items that can be worn in bauble slots
 *
 * @author Azanor
 */

public interface IBauble {

    /**
     * This method return the type of bauble this is.
     * Type is used to determine the slots it can go into.
     */
    BaubleType getBaubleType(ItemStack itemstack);

    /**
     * This method is called once per tick if the bauble is being worn by a player
     */
    void onWornTick(ItemStack itemstack, EntityLivingBase player);

    /**
     * This method is called when the bauble is equipped by a player
     */
    void onEquipped(ItemStack itemstack, EntityLivingBase player);

    /**
     * This method is called when the bauble is unequipped by a player
     */
    void onUnequipped(ItemStack itemstack, EntityLivingBase player);

    /**
     * can this bauble be placed in a bauble slot
     */
    boolean canEquip(ItemStack itemstack, EntityLivingBase player);

    /**
     * Can this bauble be removed from a bauble slot
     */
    boolean canUnequip(ItemStack itemstack, EntityLivingBase player);
}
