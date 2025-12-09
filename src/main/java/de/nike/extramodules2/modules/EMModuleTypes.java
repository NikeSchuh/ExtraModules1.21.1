package de.nike.extramodules2.modules;

import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleType;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.types.ModuleTypeImpl;
import de.nike.extramodules2.modules.data.*;
import de.nike.extramodules2.modules.entities.defensebrain.DefenseBrainEntity;
import de.nike.extramodules2.modules.entities.OxygenEntity;
import de.nike.extramodules2.modules.entities.PotionCurerEntity;

import java.util.function.Function;

public class EMModuleTypes {

    public static final ModuleType<OxygenStorageData> OXYGEN_STORAGE = (ModuleType<OxygenStorageData>) (new ModuleTypeImpl("oxygen_storage", 1, 1, (Function<Module, ModuleEntity<?>>) OxygenEntity::new, OxygenEntity.CODEC, OxygenEntity.STREAM_CODEC, ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR)).setMaxInstallable(1);
    public static final ModuleType<PotionCurerData> POTION_CURER = (ModuleType<PotionCurerData>) (new ModuleTypeImpl("potion_curer", 1, 2, (Function<Module, ModuleEntity<?>>) PotionCurerEntity::new, PotionCurerEntity.CODEC, PotionCurerEntity.STREAM_CODEC, ModuleCategory.CHESTPIECE)).setMaxInstallable(1);
    public static final ModuleType<DefenseBrainData> DEFENSE_BRAIN = (ModuleType<DefenseBrainData>) (new ModuleTypeImpl("defense_brain", 2, 2, (Function<Module, ModuleEntity<?>>) DefenseBrainEntity::new, DefenseBrainEntity.CODEC, DefenseBrainEntity.STREAM_CODEC, ModuleCategory.CHESTPIECE)).setMaxInstallable(1);
    public static final ModuleType<DefenseData> DEFENSE_MODULE = (ModuleType<DefenseData>)new ModuleTypeImpl<>("defense_module", 1, 1, ModuleCategory.CHESTPIECE);
    public static final ModuleType<EffectData> EFFECT = new ModuleTypeImpl<>("effect", 1, 2, new ModuleCategory[] {EMModuleCategories.EFFECT});

    public static final ModuleType<PistolData> PISTOL = new ModuleTypeImpl<>("pistol", 1, 1, new ModuleCategory[] {EMModuleCategories.PISTOL});
    public static final ModuleType<PistolHomingData> PISTOL_HOMING = new ModuleTypeImpl<>("pistol_homing", 2, 2, new ModuleCategory[] {EMModuleCategories.PISTOL}).setMaxInstallable(1);


}
