package baubles.gui;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ventivu.core.WindowFrame.GuiWindow;
import ventivu.core.WindowFrame.WidgetBorder;
import ventivu.core.WindowFrame.Widgets.base.SlotWidget;

import static baubles.api.BaubleType.ANY;
import static ventivu.core.WindowFrame.GuiRenderUtils.*;
import static ventivu.core.WindowFrame.Widgets.base.WindowWidget.widgetTexture;

public class SlotBauble extends SlotWidget {
    public static final ResourceLocation texture = new ResourceLocation("baubles", "textures/gui/BaublesWidget.png");
    BaubleType type;

    public SlotBauble(IInventory inv, BaubleType type, int index, int x, int y) {
        super(inv, index, x, y);
        this.type = type;
        //super.setRgba(new int[]{0,0,0,170});
    }

    @Override
    public void doDraw(GuiWindow container, FontRenderer fontRenderer, int mouseX, int mouseY, float partialTicks) {
        if (!visible || (ioConfig & NOBG) == NOBG) return;
        int renderX = container.getGuiLeft() + xDisplayPosition - 1;
        int renderY = container.getGuiTop() + yDisplayPosition - 1;
        bindTexture(widgetTexture);
        drawTexturedModalRect(renderX, renderY, 0, 0, 18, 18, 0);
        bindTexture(texture);
        if (!getHasStack() && type != null) {
            WidgetBorder border = borderSet.get(type.name());
            drawColoredModalRect(renderX, renderY, border.getU(), border.getV(), 18, 18, border.getWidth(), border.getHeight(), 0, 0x78787846);
        }
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !(inventory instanceof InventoryBaubles) || stack != null && stack.getItem() != null && stack.getItem() instanceof IBauble && (this.type == ANY || ((IBauble) stack.getItem()).getBaubleType(stack) == this.type) && ((IBauble) stack.getItem()).canEquip(stack, ((InventoryBaubles) this.inventory).player.get());
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        if (this.getStack() != null) {
            if (this.getStack().getItem() instanceof IBauble)
                return ((IBauble) this.getStack().getItem()).canUnequip(this.getStack(), player);
        }
        return true;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
