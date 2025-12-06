package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;

public class OxygenStorageData implements ModuleData<OxygenStorageData> {

    private final int oxygenCapacity;
    private final int oxygenRefillRate;

    public OxygenStorageData(int oxygenStored, int oxygenRefillRate) {
        this.oxygenCapacity = oxygenStored;
        this.oxygenRefillRate = oxygenRefillRate;
    }

    @Override
    public OxygenStorageData combine(OxygenStorageData other) {
        return new OxygenStorageData(this.oxygenCapacity + other.oxygenCapacity, this.oxygenRefillRate + other.oxygenRefillRate);
    }

    public int getOxygenCapacity() {
        return oxygenCapacity;
    }

    public int getOxygenRefillRate() {
        return oxygenRefillRate;
    }
}
