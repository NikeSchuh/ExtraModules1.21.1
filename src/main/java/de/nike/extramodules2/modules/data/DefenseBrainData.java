package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;

public class DefenseBrainData implements ModuleData<DefenseBrainData> {

    private final int rageTicks;
    private final int opTickCost;
    private final float lightningJumpRange;
    private final int maximumJumpTargets;

    public DefenseBrainData(int rageTicks, int opTickCost, float lightningJumpRange, int maximumJumpTargets) {
        this.rageTicks = rageTicks;
        this.opTickCost = opTickCost;
        this.lightningJumpRange = lightningJumpRange;
        this.maximumJumpTargets = maximumJumpTargets;
    }

    @Override
    public DefenseBrainData combine(DefenseBrainData other) {
        return new DefenseBrainData(rageTicks + other.rageTicks, opTickCost + other.opTickCost, lightningJumpRange + other.lightningJumpRange, maximumJumpTargets + other.maximumJumpTargets);
    }

    public float getLightningJumpRange() {
        return lightningJumpRange;
    }

    public int getMaximumJumpTargets() {
        return maximumJumpTargets;
    }

    public int getOpTickCost() {
        return opTickCost;
    }

    public int getRageTicks() {
        return rageTicks;
    }
}
