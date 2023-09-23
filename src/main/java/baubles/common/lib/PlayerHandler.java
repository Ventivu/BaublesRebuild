package baubles.common.lib;

import baubles.common.Baubles;
import baubles.common.Configuration.Configuration;
import baubles.common.container.InventoryBaubles;
import com.google.common.io.Files;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class PlayerHandler {

    private static final HashMap<String, InventoryBaubles> playerBaubles = new HashMap<>();

    public static void clearPlayerBaubles(EntityPlayer player) {
        playerBaubles.remove(player.getCommandSenderName());
    }

    public static InventoryBaubles getPlayerBaubles(EntityPlayer player) {
        int size = Configuration.getList().size();
        if (!playerBaubles.containsKey(player.getCommandSenderName())) {
            playerBaubles.put(player.getCommandSenderName(), new InventoryBaubles(player, size));
        }
        InventoryBaubles inv = playerBaubles.get(player.getCommandSenderName());
        if (inv.stackList.length != size) inv.stackList = Arrays.copyOf(inv.stackList, size);
        return inv;
    }

    public static void setPlayerBaubles(EntityPlayer player, @Nonnull InventoryBaubles inventory) {
        playerBaubles.put(player.getCommandSenderName(), Objects.requireNonNull(inventory));
    }

    public static void loadPlayerBaubles(EntityPlayer player, File file1, File file2) {
        if (player != null && !player.worldObj.isRemote) {
            try {
                NBTTagCompound data = null;
                boolean save = false;
                if (file1 != null && file1.exists()) {
                    try {
                        FileInputStream fileinputstream = new FileInputStream(file1);
                        data = CompressedStreamTools.readCompressed(fileinputstream);
                        fileinputstream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (file1 == null || !file1.exists() || data == null || data.hasNoTags()) {
                    Baubles.log.warn(StatCollector.translateToLocalFormatted("baubles.message.loadbackup", player.getCommandSenderName()));
                    if (file2 != null && file2.exists()) {
                        try {
                            FileInputStream fileinputstream = new FileInputStream(file2);
                            data = CompressedStreamTools.readCompressed(fileinputstream);
                            fileinputstream.close();
                            save = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (data != null) {
                    InventoryBaubles inventory = new InventoryBaubles(player);
                    inventory.readNBT(data);
                    playerBaubles.put(player.getCommandSenderName(), inventory);
                    if (save) savePlayerBaubles(player, file1, file2);
                }
            } catch (Exception exception1) {
                Baubles.log.fatal(I18n.format("baubles.message.loaderror", player.getCommandSenderName()));
                exception1.printStackTrace();
            }
        }
    }

    @SuppressWarnings("all")
    public static void savePlayerBaubles(EntityPlayer player, File file1, File file2) {
        if (player != null && !player.worldObj.isRemote) {
            try {
                if (file1 != null && file1.exists()) {
                    try {
                        Files.copy(file1, file2);
                    } catch (Exception e) {
                        Baubles.log.error(I18n.format("baubles.message.savebackuperror", player.getCommandSenderName()));
                    }
                }

                try {
                    if (file1 != null) {
                        InventoryBaubles inventory = getPlayerBaubles(player);
                        NBTTagCompound data = new NBTTagCompound();
                        inventory.saveNBT(data);

                        FileOutputStream fileoutputstream = new FileOutputStream(file1);
                        CompressedStreamTools.writeCompressed(data, fileoutputstream);
                        fileoutputstream.close();

                    }
                } catch (Exception e) {
                    Baubles.log.error(I18n.format("baubles.message.saveerror", player.getCommandSenderName()));
                    e.printStackTrace();
                    if (file1.exists()) {
                        try {
                            file1.delete();
                        } catch (Exception ignored) {
                        }
                    }
                }
            } catch (Exception exception1) {
                Baubles.log.fatal("Error saving baubles inventory");
                exception1.printStackTrace();
            }
        }
    }

    public static void refreshPlayerBaubles() {
        for (String name : playerBaubles.keySet()) {
            InventoryBaubles inv = playerBaubles.get(name);
            inv.refresh(Configuration.getList().size());
        }
    }
}
