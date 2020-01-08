package quarris.incstu.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import quarris.incstu.mechanics.soul.SoulType;
import quarris.incstu.util.ColorHelper;

import java.util.*;

public class SoulRegistry {

    public static final Map<String, SoulType> SOUL_TYPE_REGISTRY = new HashMap<>();
    public static final Multimap<String, Pair<SoulType, Integer>> ENTITY_TYPE_REGISTRY = HashMultimap.create();

    // Soul Types
    public static final SoulType CLEAR = new SoulType("clear", 0xffffff).register();
    public static final SoulType GROUND = new SoulType("ground", 0x8c4a17).register();
    public static final SoulType HUMAN = new SoulType("human", 0xe8cf9d).register();

    // Cosmetic Types
    public static final SoulType RAINBOLLPECK = new SoulType("rainbollpeck") {
        @SideOnly(Side.CLIENT)
        @Override
        public int getColor() {
            World world = Minecraft.getMinecraft().world;
            return ColorHelper.rainbow((int)(world.getTotalWorldTime() % 255));
        }
    }.setCosmetic().register();

    // Init
    public static void init() {
        addTypeToEntity("minecraft:sheep", GROUND, 10);
        addTypeToEntity("minecraft:sheep", CLEAR, 5);
        addTypeToEntity("minecraft:pig", GROUND, 25);
        addTypeToEntity("minecraft:villager", HUMAN, 50);
        addTypeToEntity("player", HUMAN, 100);
    }


    public static void addTypeToEntity(String name, SoulType type, int amount) {
        ENTITY_TYPE_REGISTRY.put(name, Pair.of(type, amount));
    }

    public static Collection<Pair<SoulType, Integer>> getTypesForEntity(Entity e) {
        if (e != null) {
            if (e instanceof EntityPlayer) {
                return ENTITY_TYPE_REGISTRY.get("player");
            }
            ResourceLocation entry = EntityList.getKey(e);
            if (entry != null) {
                return ENTITY_TYPE_REGISTRY.get(EntityList.getKey(e.getClass()).toString());
            }
        }
        return Collections.emptyList();
    }

    public static SoulType getCosmeticForEntity(Entity e) {
        if (e instanceof EntityPlayer) {
			if (e.getUniqueID().equals(UUID.fromString("3f9f4a94-95e3-40fe-8895-e8e3e84d1468"))) {  //Ellpeck
				return RAINBOLLPECK;
			}
            else if (e.getUniqueID().equals(UUID.fromString("314b59c7-413f-4357-a074-f059ebe5aaae"))) {
                return RAINBOLLPECK;
            }
        }
        else if (e instanceof EntitySheep) {
			if ("Jeb_".equals(e.getName())) {
                return RAINBOLLPECK;
            }
        }
        return null;
    }
}
