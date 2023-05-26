package baubles.common.container;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeProxy;
import baubles.api.IBauble;
import ventivu.core.GuiFactory.Widget.BackGroundSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotBauble extends BackGroundSlot {

    BaubleType type;

    public SlotBauble(IInventory inv, BaubleType type, int index, int x, int y) {
        super(inv, index, x, y, BaubleTypeProxy.getType(type).getUv(), BaubleTypeProxy.getType(type).getTexture());
        this.type = type;
       //super.setRgba(new int[]{0,0,0,170});
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !(inventory instanceof InventoryBaubles)||stack != null && stack.getItem() != null && stack.getItem() instanceof IBauble && ( BaubleTypeProxy.getType(this.type)== BaubleTypeProxy.ANY || ((IBauble) stack.getItem()).getBaubleType(stack) == this.type) && ((IBauble) stack.getItem()).canEquip(stack, ((InventoryBaubles) this.inventory).player.get());
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
