package quarris.incstu.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class ModCaps {

    @CapabilityInject(ISoulCapability.class)
    public static Capability<ISoulCapability> capabilitySoul;

    public static void initCaps() {
        registerCap(ISoulCapability.class);
    }

    public static <T> void registerCap(Class<T> type) {
        CapabilityManager.INSTANCE.register(type, new IStorage<T>() {

            @Nullable
            @Override
            public NBTBase writeNBT(Capability capability, Object instance, EnumFacing side) {
                return null;
            }

            @Override
            public void readNBT(Capability capability, Object instance, EnumFacing side, NBTBase nbt) {

            }

        }, () -> null);
    }

    public static ISoulCapability getSoul(Entity e) {
        return e.getCapability(capabilitySoul, null);
    }
}
