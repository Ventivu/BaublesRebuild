package baubles.gui;

import baubles.api.BaubleType;
import baubles.common.Baubles;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ventivu.core.Utils.jsonHelper.*;

public class UIController {
    Gson gson = new Gson();
    public GuiStorage storage;

    public UIController() {
        String inf = JsonReads(Baubles.MODID + "Gui");
        if (inf != null) {
            storage = gson.fromJson(inf, GuiStorage.class);
        } else {
            creatFile(Baubles.MODID + "Gui", gson.toJson(example()));
            storage = example();
        }
    }

    public BaubleType getType(int i){
        return storage.baubleSlots.get(i);
    }

    public static void apply(GuiStorage storage,CustomizableBaublesContainer container) {
        double a=4;
        int count = storage.baubleSlots.size();
        for (int i = 0; i < Math.ceil(count / a); i++)
            for (int j = 0; j < count-a*i&&j<a; j++) {
                BaubleType type = storage.baubleSlots.get((int) (i * a + j));
                container.addBaubleSlot(48+BaublesGui.playerX+i*18, BaublesGui.playerY+1+j*18, type);
            }
    }

    public static GuiStorage example(){
        GuiStorage storage = new GuiStorage();
        storage.defaults();
        return storage;
    }

    public static class GuiStorage {
        public ArrayList<BaubleType> baubleSlots = new ArrayList<>();

        public GuiStorage(){}
        public void defaults(){
            this.baubleSlots.addAll(Arrays.asList(BaubleType.values()));
        }
    }
}
