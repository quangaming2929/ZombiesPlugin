package io.github.zap.zombiesplugin.navmesh;

import org.bukkit.util.Vector;

import java.util.HashMap;

public class Navmesh {
    private HashMap<Vector,MeshBlock> meshBlocks;

    public Navmesh() {
        this.meshBlocks = new HashMap<Vector,MeshBlock>();
    }

    public void toggleNodesAt(Vector vector) {
        MeshBlock meshBlock = meshBlocks.get(vector);
        if(meshBlock == null) return;

        meshBlock.getNode().toggle();
    }

    public HashMap<Vector,MeshBlock> getMeshBlocks() { return meshBlocks; }
}
