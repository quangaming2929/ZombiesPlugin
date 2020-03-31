package io.github.zap.zombiesplugin.utils;

import net.minecraft.server.v1_15_R1.*;

public class NMSUtils {
    public boolean isSlab(Block block) {
        return block instanceof BlockStepAbstract;
    }
}
