package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DefenseData implements ModuleData<DefenseData> {

    private final float damage;

    public DefenseData(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }


    @Override
    public void addInformation(Map<Component, Component> map, @Nullable ModuleContext context) {
        map.put(Component.translatable("module.extramodules2.defense.defense_damage"), Component.literal(damage + ""));
    }

    @Override
    public DefenseData combine(DefenseData other) {
        return new DefenseData(this.damage + other.damage);
    }
}
