package baubles.gui;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import com.ventivu.core.GuiFactory.Widget.BackGroundSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BaubleSlot extends BackGroundSlot {

    BaubleType type;

    public BaubleSlot(IInventory inv, int index, int x, int y, BaubleType type) {
        super(inv, index, x, y, type.getUv(), type.getTexture());
        this.type = type;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.getItem() instanceof IBauble && (this.type == BaubleType.ANY || ((IBauble) stack.getItem()).getBaubleType(stack) == this.type) && ((IBauble) stack.getItem()).canEquip(stack, ((InventoryBaubles) this.inventory).player.get());
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return this.getStack() != null && ((IBauble) this.getStack().getItem()).canUnequip(this.getStack(), player);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

}
