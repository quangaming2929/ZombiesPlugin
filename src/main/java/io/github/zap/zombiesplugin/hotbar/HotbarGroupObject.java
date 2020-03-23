package io.github.zap.zombiesplugin.hotbar;

public class HotbarGroupObject {
    public HotbarObject object;
    public int slotId;
    public int itemGroupId;

    public HotbarGroupObject(int itemGroupId, int slotId) {
        this.itemGroupId = itemGroupId;
        this.slotId = slotId;
    }
}
