package quarris.incstu.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quarris.incstu.mechanics.soul.SoulType;

public interface ISoulCapability extends ICapabilitySerializable<NBTTagCompound> {

    Entity getVessel();

    int getSoulAmount(SoulType type);

    float getSoulPerc(SoulType type);

    void addSoul(SoulType type, int amount, boolean calcCol);

    void removeSoul(SoulType type);

    int removeSoul(SoulType type, int amount, boolean simulate);

    boolean isVacuum();

    @SideOnly(Side.CLIENT)
    int getColor();

}
