package baubles.common.network;

import baubles.common.Baubles;
import baubles.common.CommonProxy;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketOpenBaublesInventory implements IMessage, IMessageHandler<PacketOpenBaublesInventory, IMessage> {

    public PacketOpenBaublesInventory() {
    }

    public PacketOpenBaublesInventory(EntityPlayer player) {
    }

    @Override
    public void toBytes(ByteBuf buffer) {
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
    }

    @Override
    public IMessage onMessage(PacketOpenBaublesInventory message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        player.openGui(Baubles.instance, CommonProxy.bgui.id, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        return null;
    }


}
