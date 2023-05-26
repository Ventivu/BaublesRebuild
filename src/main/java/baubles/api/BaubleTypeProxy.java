package baubles.api;

import net.minecraft.util.ResourceLocation;
import ventivu.core.GuiFactory.PointSet;

public enum BaubleTypeProxy {
    AMULET(new ResourceLocation("baubles", "textures/gui/BaublesWidget.png"), new PointSet(16, 0), BaubleType.AMULET), RING(new ResourceLocation("baubles", "textures/gui/BaublesWidget.png"), new PointSet(32, 0), BaubleType.RING), BELT(new ResourceLocation("baubles", "textures/gui/BaublesWidget.png"), new PointSet(0, 0), BaubleType.BELT), ANY(new ResourceLocation("baubles", "textures/gui/BaublesWidget.png"), new PointSet(0, 16), null);

    public static final ResourceLocation widgetTexture = new ResourceLocation("baubles", "textures/gui/BaublesWidget.png");
    transient final ResourceLocation texture;
    transient final PointSet uv;
    transient final BaubleType oldtype;

    BaubleTypeProxy(ResourceLocation texture, PointSet uv, BaubleType oldtype) {
        this.texture = texture;
        this.uv = uv;
        this.oldtype = oldtype;
    }

    public static BaubleTypeProxy getType(BaubleType oldtype) {
        try {
            return BaubleTypeProxy.valueOf(oldtype.name());
        } catch (Exception e) {
            return ANY;
        }
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public PointSet getUv() {
        return uv;
    }

    public BaubleType getOldtype() {
        return oldtype;
    }
}
