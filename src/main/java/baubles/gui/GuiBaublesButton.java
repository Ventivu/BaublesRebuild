package baubles.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import ventivu.core.WindowFrame.GuiRenderUtils;

public class GuiBaublesButton extends GuiButton {

    public GuiBaublesButton(int buttonID, int x, int y, String text) {
        super(buttonID, x, y, 10, 10, text);
    }

    public void drawButton(Minecraft mc, int xx, int yy) {
        if (this.visible) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = xx >= this.xPosition && yy >= this.yPosition && xx < this.xPosition + this.width && yy < this.yPosition + this.height;
            int k = getHoverState(this.field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GuiRenderUtils.bindTexture(SlotBauble.texture);
            if (k == 1) GuiRenderUtils.drawTextureScaled(xPosition, yPosition, 10, 10, "BUTTON_O", 0);
            else {
                GuiRenderUtils.drawTextureScaled(xPosition, yPosition, 10, 10, "BUTTON_I", 0);
                drawCenteredString(mc.fontRenderer, this.displayString, this.xPosition + 5, this.yPosition + this.height, 0xffffff);
            }
        }
    }
}
