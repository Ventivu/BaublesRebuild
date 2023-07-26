package baubles.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ventivu.core.Utils.IFunctions;
import ventivu.core.WindowFrame.GuiWindow;
import ventivu.core.WindowFrame.Widgets.TextWidget;
import ventivu.core.WindowFrame.Widgets.WidgetContainer;
import ventivu.core.WindowFrame.Widgets.base.ButtonWidget;
import ventivu.core.WindowFrame.Widgets.base.SlotWidget;
import ventivu.core.WindowFrame.Widgets.interfaces.IAlignment;
import ventivu.core.WindowFrame.Widgets.interfaces.IWidget;
import ventivu.core.WindowFrame.WindowContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 特殊用途：用于需要在有限空间展示超过限定空间数量的格子数<br>
 * 属性：内部关联的上下翻页按钮，页码显示以及分页处理的大量物品格<br>
 * 需要提前实例化，本身不参与逻辑工作，仅部署
 */
public class FlippableSlotsPack {
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
     * 用于{@link ventivu.core.WindowFrame.Window#serverSideConfigs(WindowContainer, EntityPlayer, World, int, int, int) serverSideConfigs}中向容器加入物品格
     *
     * @param container 将要加入该系列组的容器
     * @param func      返回下一个将被加入的物品格, 参数:X坐标,Y坐标,结束返回null
     */
    public void genSlotsWithLimit(WindowContainer container, IFunctions.IFunctionValues<SlotWidget> func) {
        SlotWidget widget;
        boolean show = true;
        int x = 0, y = 0;
        int count = 0;
        container.tag.setInteger("page", 1);
        while ((widget = func.run(x * 18, y * 18 + 19)) != null) {
            widget.setGroupID(groupID);
            widget.setState(show);
            container.addSlotToContainer(widget);
            count++;
            y++;
            if (y >= height) {
                x++;
                y = 0;
                if (x >= width) {
                    x = 0;
                    show = false;
                }
            }
        }
        container.tag.setInteger("maxPages", (int) Math.ceil((double) count / width / height));
    }

    @SuppressWarnings({"rawtype", "unchecked"})
    @SideOnly(Side.CLIENT)
    public WidgetContainer genPackedContainer(String name, int x, int y, GuiWindow window) {
        int width = this.width * 18;
        int height = this.height * 18 + 19;
        int size = this.width * this.height;
        WidgetContainer container = new WidgetContainer(name, x, y, width, height);
        List<SlotWidget> slots = new ArrayList<>();
        window.inventorySlots.inventorySlots.stream().filter(slot -> slot instanceof SlotWidget && ((SlotWidget) slot).isSameGroup(groupID)).forEach(slot -> {
            ((SlotWidget) slot).setOrginalPoint(window.getGuiLeft(), window.getGuiTop());
            slots.add((SlotWidget) slot);
        });

        slots.forEach(container::addWidget);
        int max = (int) Math.ceil(container.get().size() / (double) size);
        if (max > 1) {
            int wid = (int) (width * 0.15);
            IWidget widget = new ButtonWidget(499, 0, 0, wid, 15, "<");
            ((ButtonWidget) widget).setEnable(false);
            ((ButtonWidget) widget).setClientOnly(me -> ((ButtonWidget) me[0]).setEnable(((WindowContainer) window.inventorySlots).tag.getInteger("page") > 1));

            container.addWidget(widget);
            widget = new ButtonWidget(501, width - wid, 0, wid, 15, ">");
            ((ButtonWidget) widget).setClientOnly(me -> ((ButtonWidget) me[0]).setEnable(((WindowContainer) window.inventorySlots).tag.getInteger("page") < max));

            container.addWidget(widget);
            widget = new TextWidget("page", width / 2, 10, "baubles.gui.page");
            ((TextWidget) widget).setAlignment(IAlignment.Alignments.CENTER);
            container.addWidget(widget);
        }

        return container;
    }

    @SideOnly(Side.CLIENT)
    public void addTextFunc(Map<String, GuiWindow.UpdateFunc> map, WidgetContainer root, String name) {
        map.put("page", (window, widget) -> {
            NBTTagCompound tag = ((WindowContainer) window.inventorySlots).tag;
            widget.putData(tag.getInteger("page"), tag.getInteger("maxPages"));
        });
    }

    public void addButtonFuncs(Map<Integer, IFunctions.IFunctionValueVoid> funcMap) {
        int size = this.width * this.height;

        funcMap.put(499, data -> {
            CustomizableBaublesContainer container = (CustomizableBaublesContainer) data[0];
            List<SlotBauble> slots = new ArrayList<>();
            container.inventorySlots.stream().filter(slot -> slot instanceof SlotBauble && ((SlotBauble) slot).isSameGroup(groupID)).forEach(slot -> slots.add((SlotBauble) slot));
            int page = container.tag.getInteger("page");
            for (int start = (page - 1) * size, end = Math.min(page * size, slots.size()); start < end; start++)
                slots.get(start).setState(false);
            page--;
            for (int start = (page - 1) * size, end = page * size; start < end; start++)
                slots.get(start).setState(true);
            container.tag.setInteger("page", page);
            container.detectAndSendChanges();
        });

        funcMap.put(501, data -> {
            CustomizableBaublesContainer container = (CustomizableBaublesContainer) data[0];
            List<SlotBauble> slots = new ArrayList<>();
            container.inventorySlots.stream().filter(slot -> slot instanceof SlotBauble && ((SlotBauble) slot).isSameGroup(groupID)).forEach(slot -> slots.add((SlotBauble) slot));
            int page = container.tag.getInteger("page");
            for (int start = (page - 1) * size, end = page * size; start < end; start++)
                slots.get(start).setState(false);
            page++;
            for (int start = (page - 1) * size, end = Math.min(page * size, slots.size()); start < end; start++)
                slots.get(start).setState(true);
            container.tag.setInteger("page", page);
            container.detectAndSendChanges();
        });
    }
}
