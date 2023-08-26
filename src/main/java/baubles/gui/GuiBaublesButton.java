package baubles.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import ventivu.core.WindowFrame.GuiRenderUtils;
import ventivu.core.WindowFrame.Widgets.base.ButtonWidget;

public class GuiBaublesButton extends GuiButton {

    public GuiBaublesButton(int buttonID, int x, int y, int width, int height, String text) {
        super(buttonID, x, y, width, height, text);
    }

    public void drawButton(Minecraft mc, int xx, int yy) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = xx >= this.xPosition && yy >= this.yPosition && xx < this.xPosition + this.width && yy < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GuiRenderUtils.bindTexture(SlotBauble.texture);
            if (k == 1) {
                GuiRenderUtils.drawChangeableTexture(xPosition, yPosition, width, height, 0, "BUTTON_O");
            } else {
                GuiRenderUtils.drawChangeableTexture(xPosition, yPosition, width, height, 0, "BUTTON_I");
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + 5, this.yPosition + this.height, 0xffffff);
            }
            this.mouseDragged(mc, xx, yy);
        }
    }
}
