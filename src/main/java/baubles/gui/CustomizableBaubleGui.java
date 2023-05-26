package baubles.gui;

import baubles.common.Baubles;
import ventivu.core.GuiFactory.AbstractContainer;
import ventivu.core.GuiFactory.CustomizableGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.Nullable;

public class CustomizableBaubleGui extends CustomizableGui {
    public CustomizableBaubleGui(AbstractContainer Container, @Nullable TileEntity entity, EntityLivingBase user) {
        super(Container, entity, user);
        this.allowUserInput = true;
    }


    @Override
    public void updateScreen() {
        super.updateScreen();
        try {
            ((CustomizableBaublesContainer) inventorySlots).baubles.blockEvents = false;
        } catch (Exception ignored) {
        }
    }

    @Override
    public void initGui() {
        buttonList.clear();
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
        }

        if (button.id == 1) {
            mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
        }
    }

    @Override
    protected void keyTyped(char charter, int keycode) {
        if (keycode == Baubles.proxy.keyHandler.key.getKeyCode()) {
            mc.thePlayer.closeScreen();
        } else super.keyTyped(charter, keycode);
    }
}
