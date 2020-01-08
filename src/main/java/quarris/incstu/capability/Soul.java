package quarris.incstu.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;
import quarris.incstu.ModRef;
import quarris.incstu.mechanics.soul.SoulType;
import quarris.incstu.registry.SoulRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class Soul implements ISoulCapability {

    public static final ResourceLocation SOUL_TEX = ModRef.createRes("textures/soul/soul.png");
    public static final ResourceLocation SOUL_VACUUM_TEX = ModRef.createRes("textures/soul/soul_vacuum.png");
    private Entity entity;
    private Map<SoulType, MutableInt> soulTypes;
    public SoulType cosmeticType = null;

    @SideOnly(Side.CLIENT)
    public int color;

    public Soul() {
        this(null);
    }

    public Soul(Entity entity) {
        if (entity != null) {
            this.entity = entity;
            this.soulTypes = new HashMap<>();
            Collection<Pair<SoulType, Integer>> souls = SoulRegistry.getTypesForEntity(entity);
            for (Pair<SoulType, Integer> pair : souls) {
                this.addSoul(pair.getLeft(), pair.getRight(), false);
            }
            this.calcColor();
            //TODO Fix cosmetic soul types. this.cosmeticType = SoulRegistry.getCosmeticForEntity(entity);
        }
    }

    @Override
    public Entity getVessel() {
        return this.entity;
    }

    @Override
    public void addSoul(SoulType type, int amount, boolean calcCol) {
        if (type.isCosmetic()) {
            this.cosmeticType = type;
        }
        else if (this.soulTypes.containsKey(type)) {
            this.soulTypes.get(type).add(amount);
        }
        else {
            this.soulTypes.put(type, new MutableInt(amount));
        }
        if (calcCol) {
            this.calcColor();
        }
    }

    @Override
    public void removeSoul(SoulType type) {
        this.soulTypes.remove(type);
        this.calcColor();
    }

    public Map<SoulType, MutableInt> getSoulTypes() {
        return this.soulTypes;
    }

    /**
     * @param type
     * @param amount
     * @param simulate
     * @return The amount of soul left after the removal.
     */
    @Override
    public int removeSoul(SoulType type, int amount, boolean simulate) {
        if (amount > 0) {
            if (this.soulTypes.containsKey(type)) {
                if (!simulate) {
                    this.calcColor();
                }
                MutableInt val = this.soulTypes.get(type);
                if (val.getValue() > amount) {
                    int ret = val.getValue() - amount;
                    if (!simulate) {
                        val.subtract(amount);
                    }
                    return ret;
                }
                else {
                    int ret = val.getValue() - amount;
                    if (!simulate) {
                        this.soulTypes.remove(type);
                    }
                    return ret;
                }
            }
            return 0;
        }
        return this.soulTypes.containsKey(type) ? this.soulTypes.get(type).getValue() : 0;
    }

    @Override
    public int getSoulAmount(SoulType type) {
        return this.soulTypes.containsKey(type) ? soulTypes.get(type).getValue() : 0;
    }

    @Override
    public float getSoulPerc(SoulType type) {
        return this.soulTypes.get(type).getValue() / (float) getAllSoulAmount();
    }

    private int getAllSoulAmount() {
        int ret = 0;
        for (MutableInt mut : this.soulTypes.values()) {
            ret += mut.getValue();
        }
        return ret;
    }

    @Override
    public boolean isVacuum() {
        return this.soulTypes.isEmpty();
    }

    @Override
    public int getColor() {

        if (this.cosmeticType != null) {
            return this.cosmeticType.getColor();
        }
        if (this.isVacuum()) {
            return 0xffffff;
        }
        return this.color;
    }

    private void calcColor() {
        ModRef.proxy.calcSoulColor(this);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ModCaps.capabilitySoul;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == ModCaps.capabilitySoul ? (T) this : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList typeList = new NBTTagList();
        NBTTagCompound type;
        for (Map.Entry<SoulType, MutableInt> entry : this.soulTypes.entrySet()) {
            type = new NBTTagCompound();
            type.setString("name", entry.getKey().name);
            type.setInteger("amount", entry.getValue().getValue());
            typeList.appendTag(type);
        }
        nbt.setTag("types", typeList);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.soulTypes.clear();
        NBTTagList typeList = nbt.getTagList("types", Constants.NBT.TAG_COMPOUND);
        for (NBTBase base : typeList) {
            NBTTagCompound tag = (NBTTagCompound) base;
            SoulType type = SoulRegistry.SOUL_TYPE_REGISTRY.get(tag.getString("name"));
            int amount = tag.getInteger("amount");
            this.addSoul(type, amount, false);
        }
        this.calcColor();
    }
}
