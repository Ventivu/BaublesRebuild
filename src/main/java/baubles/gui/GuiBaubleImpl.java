package baubles.gui;

import net.minecraftforge.client.event.GuiScreenEvent;
import ventivu.core.WindowFrame.GuiWindow;
import ventivu.core.WindowFrame.WindowContainer;

/**
 * 标志位，便于为{@link GuiEvents#guiPostInit(GuiScreenEvent.InitGuiEvent.Post) guiPostInit}提供识别功能
 */
public class GuiBaubleImpl extends GuiWindow {
    public GuiBaubleImpl(WindowContainer container) {
        super(container);
    }
}
