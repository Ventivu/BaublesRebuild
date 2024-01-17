package baubles.gui;

import baubles.api.BaubleType;
import baubles.common.container.InventoryBaubles;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ventivu.core.WindowFrame.GuiWindow;
import ventivu.core.WindowFrame.Widgets.TextWidget;
import ventivu.core.WindowFrame.Widgets.WidgetContainer;
import ventivu.core.WindowFrame.Widgets.base.ButtonWidget;
import ventivu.core.WindowFrame.Widgets.base.SlotWidget;
import ventivu.core.WindowFrame.Widgets.interfaces.IAlignment;
import ventivu.core.WindowFrame.Widgets.interfaces.IWidget;
import ventivu.core.WindowFrame.WindowContainer;

import java.util.List;

/**
 * 特殊用途：用于需要在有限空间展示超过限定空间数量的格子数<br>
 * 属性：内部关联的上下翻页按钮，页码显示以及分页处理的大量物品格<br>
 * 需要提前实例化，本身不参与逻辑工作，仅部署
 */
public class FlippableSlotsPack {
    private static final String Tag_Page = "page";
    private static final String Tag_MaxPage = "maxPages";
    int width, height, groupID;

    /**
     * @param width   格子横向数量
     * @param height  格子纵向数量
     * @param groupID 要求唯一，无需操作
     */
    public FlippableSlotsPack(int groupID, int width, int height) {
        this.width = width;
        this.height = height;
        this.groupID = groupID;
    }

    /**
     * 用于{@link ventivu.core.WindowFrame.Window#serverConfigs(WindowContainer, EntityPlayer, World, int, int, int) serverSideConfigs}中向容器加入物品格
     *
     * @param container 将要加入该系列组的容器
     * @param types     全部的饰品栏位类型
     */
    public void genSlots(WindowContainer container, List<BaubleType> types, InventoryBaubles baub) {
        SlotWidget widget;
        boolean show = true;
        int x = 0, y = 0, index = 0;
        container.tag.setInteger(Tag_Page, 1);
        for (BaubleType type : types) {
            widget = new SlotBauble(baub, type, index++, x * 18, y++ * 18 + 19);
            widget.setGroupID(groupID).setState(show);
            container.addSlotToContainer(widget);
            if (y >= height) {
                y = 0;
                if (++x >= width) {
                    x = 0;
                    show = false;
                }
            }
        }
        container.tag.setInteger(Tag_MaxPage, (int) Math.ceil((double) index / width / height));
    }

    @SuppressWarnings({"rawtype"})
    @SideOnly(Side.CLIENT)
    public WidgetContainer genPackedContainer(int x, int y, GuiWindow window) {
        int width = this.width * 18;
        int height = this.height * 18 + 19;
        int size = this.width * this.height;
        WidgetContainer container = new WidgetContainer(x, y, width, height);
        container.addSlotGroup(groupID, window, 0, 0);
        List<IWidget> baublesContainer = ((WidgetContainer) container.get().get(container.get().size() - 1)).get();

        int max = (int) Math.ceil(baublesContainer.size() / (double) size);
        if (max > 1) {
            int wid = (int) (width * 0.15);
            ButtonWidget prevPage = new ButtonWidget(499, 0, 0, wid, 15, null);
            prevPage.setEnabled(false);
            prevPage.setClientOnlyFunc(w -> {
                int page = ((WindowContainer) w.inventorySlots).tag.getInteger(Tag_Page);
                prevPage.setEnabled(page > 2);

                for (int start = (page - 1) * size, end = Math.min(page * size, baublesContainer.size()); start < end; start++)
                    baublesContainer.get(start).setState(false);
                for (int start = (--page - 1) * size, end = page * size; start < end; start++)
                    baublesContainer.get(start).setState(true);
                ((WindowContainer) w.inventorySlots).tag.setInteger(Tag_Page, page);
            }).setDisplayFunc(w -> {
                int page = ((WindowContainer) w.inventorySlots).tag.getInteger(Tag_Page);
                prevPage.setEnabled(page > 1);
                return "<";
            });
            container.addWidget(prevPage);


            ButtonWidget nextPage = new ButtonWidget(501, width - wid, 0, wid, 15, null);
            nextPage.setClientOnlyFunc(w -> {
                int page = ((WindowContainer) w.inventorySlots).tag.getInteger(Tag_Page);
                nextPage.setEnabled(page < max - 1);

                for (int start = (page - 1) * size, end = page * size; start < end; start++)
                    baublesContainer.get(start).setState(false);
                for (int start = (++page - 1) * size, end = Math.min(page * size, baublesContainer.size()); start < end; start++)
                    baublesContainer.get(start).setState(true);
                ((WindowContainer) w.inventorySlots).tag.setInteger(Tag_Page, page);
            }).setDisplayFunc(w -> {
                int page = ((WindowContainer) w.inventorySlots).tag.getInteger(Tag_Page);
                nextPage.setEnabled(page < max);
                return ">";
            });

            container.addWidget(nextPage);
            TextWidget text = new TextWidget(width / 2, 10, "baubles.gui.page").func(w -> {
                NBTTagCompound tag = ((WindowContainer) w.inventorySlots).tag;
                return new Object[]{tag.getInteger(Tag_Page), tag.getInteger(Tag_MaxPage)};
            });
            text.setAlignment(IAlignment.Alignments.CENTER);
            container.addWidget(text);
        }

        return container;
    }
}
