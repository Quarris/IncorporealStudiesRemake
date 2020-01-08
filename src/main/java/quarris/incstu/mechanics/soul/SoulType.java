package quarris.incstu.mechanics.soul;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quarris.incstu.registry.SoulRegistry;

import java.util.Objects;

public class SoulType {

    private int color;
    private boolean cosmetic;
    public final String name;

    public SoulType(String name, int color) {
        this.color = color;
        this.name = name;
    }

    public SoulType register() {
        SoulRegistry.SOUL_TYPE_REGISTRY.put(this.name, this);
        return this;
    }

    public SoulType(String name) {
        this(name, 0);
    }

    public SoulType setCosmetic() {
        this.cosmetic = true;
        return this;
    }

    public boolean isCosmetic() {
        return cosmetic;
    }

    @SideOnly(Side.CLIENT)
    public int getColor() {
        return this.color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SoulType soulType = (SoulType) o;
        return color == soulType.color &&
                cosmetic == soulType.cosmetic &&
                Objects.equals(name, soulType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, cosmetic, name);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("name=").append(name).append(", ")
                .append("cosmetic=").append(cosmetic).toString();
    }
}
