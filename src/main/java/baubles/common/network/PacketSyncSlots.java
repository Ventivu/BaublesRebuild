package baubles.common.network;

import baubles.api.BaubleTypeProxy;
import baubles.common.Configuration.Configuration;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncSlots implements IMessage, IMessageHandler<PacketSyncSlots, IMessage> {
    List<BaubleTypeProxy> list = new ArrayList<>();

    @Override
    public void toBytes(ByteBuf buffer) {
        list = Configuration.getList();
        buffer.writeInt(list.size());
        buffer.writeBytes(praseTo(list));
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        int length = buffer.readInt();
        byte[] code = new byte[(int) Math.ceil(length / 4d)];
        buffer.getBytes(buffer.readerIndex(), code);
        list = readFrom(code, length);
    }


    /**
     * 这个方法是一个他妈的静态方法，不要内部调用！！！！！！！！！
     */
    @Override
    public IMessage onMessage(PacketSyncSlots message, MessageContext ctx) {
        Configuration.ServerSlots.clear();
        Configuration.ServerSlots.addAll(message.list);
        return null;
    }

    /**
     *将服务端的物品栏信息保存为字节流，每个byte装入4个格位(分辨类型需要2字的数据)
     * @param list 将被打包的格位列表
     * @return 打包好的字节组
     */
    private byte[] praseTo(List<BaubleTypeProxy> list) {
        byte[] code = new byte[(int) Math.ceil(list.size() / 4d)];
        for (int i = 0; i < list.size(); i++)
            code[i / 4] |= (list.get(i).ordinal() << ((i % 4) * 2));
        return code;
    }


    /**
     * 从字节组中读取打包的格位信息，并在最后删减无效空间带来的错误
     * @param code 被打包的字节组
     * @param length 有效的组长度
     * @return 解包后的格位信息表
     */
    private List<BaubleTypeProxy> readFrom(byte[] code, int length) {
        List<BaubleTypeProxy> out = new ArrayList<>();
        for (byte b : code)
            for (int i = 0; i < 8; i += 2) out.add(BaubleTypeProxy.values()[b >> i & 3]);
        if (out.size() > length) out.subList(length, out.size()).clear();
        return out;
    }
}
