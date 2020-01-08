package quarris.incstu.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.mutable.MutableInt;
import quarris.incstu.capability.Soul;
import quarris.incstu.mechanics.soul.SoulType;
import quarris.incstu.registry.SoulRegistry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketSyncSoulsToClient implements IMessageHandler<PacketSyncSoulsToClient, IMessage>, IMessage {

    public UUID playerUUID;
    public Map<SoulType, MutableInt> soulTypes;
    public SoulType cosmetic;

    public PacketSyncSoulsToClient() {
        this.soulTypes = new HashMap<>();
    }

    public PacketSyncSoulsToClient(UUID playerUUID, Soul soul) {
        this.playerUUID = playerUUID;
        this.soulTypes = soul.getSoulTypes();
        this.cosmetic = soul.cosmeticType;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer b = new PacketBuffer(buf);
        b.writeLong(playerUUID.getLeastSignificantBits());
        b.writeLong(playerUUID.getMostSignificantBits());
        b.writeString(this.cosmetic.name);
        NBTTagList soulList = new NBTTagList();
        NBTTagCompound tag;
        for (Map.Entry<SoulType, MutableInt> entry : soulTypes.entrySet()) {
            tag = new NBTTagCompound();
            tag.setString("name", entry.getKey().name);
            tag.setInteger("amount", entry.getValue().getValue());
            soulList.appendTag(tag);
        }
        tag = new NBTTagCompound();
        tag.setTag("list", soulList);
        b.writeCompoundTag(tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer b = new PacketBuffer(buf);
        this.playerUUID = new UUID(b.readLong(), b.readLong());
        this.cosmetic = SoulRegistry.SOUL_TYPE_REGISTRY.get(b.readString(Integer.MAX_VALUE/4));
        NBTTagCompound tag;
        NBTTagList soulList = new NBTTagList();
        try {
            tag = b.readCompoundTag();
            soulList = tag.getTagList("list", Constants.NBT.TAG_COMPOUND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (NBTBase base : soulList) {
            tag = (NBTTagCompound) base;
            SoulType type = SoulRegistry.SOUL_TYPE_REGISTRY.get(tag.getString("name"));
            MutableInt amount = new MutableInt(tag.getInteger("amount"));
            this.soulTypes.put(type, amount);
        }
    }

    @Override
    public IMessage onMessage(PacketSyncSoulsToClient message, MessageContext ctx) {
        //TODO
        return null;
    }
}
