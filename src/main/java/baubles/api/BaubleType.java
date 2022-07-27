package baubles.api;

import com.ventivu.core.GuiFactory.PointSet;
import net.minecraft.util.ResourceLocation;

public enum BaubleType {
    AMULET(new ResourceLocation("baubles", "textures/gui/BaublesWidget.png"), new PointSet(16, 0)),
    RING(new ResourceLocation("baubles", "textures/gui/BaublesWidget.png"), new PointSet(32, 0)),
    BELT(new ResourceLocation("baubles", "textures/gui/BaublesWidget.png"), new PointSet(0, 0)),
    ANY(new ResourceLocation("baubles", "textures/gui/BaublesWidget.png"), new PointSet(0, 16));

    public static final ResourceLocation widgetTexture = new ResourceLocation("baubles", "textures/gui/BaublesWidget.png");
    transient final ResourceLocation texture;
    transient final PointSet uv;

    BaubleType(ResourceLocation texture, PointSet uv) {
        this.texture = texture;
        this.uv = uv;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public PointSet getUv() {
        return uv;
    }
}
