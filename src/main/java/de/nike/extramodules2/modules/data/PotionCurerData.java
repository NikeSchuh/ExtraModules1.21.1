package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import javax.annotation.Nullable;
import java.util.Map;

public class PotionCurerData implements ModuleData<PotionCurerData> {

    @Override
    public PotionCurerData combine(PotionCurerData other) {
        return new PotionCurerData();
    }

}
