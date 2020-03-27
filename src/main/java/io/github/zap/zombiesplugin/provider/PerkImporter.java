package io.github.zap.zombiesplugin.provider;

import io.github.zap.zombiesplugin.data.EquipmentData;

import java.nio.file.Path;

public class PerkImporter extends Importer {

    public PerkImporter() {


        createTestSpeed();
    }

    private void createTestSpeed() {
        EquipmentData perkData = new EquipmentData();
        perkData.id = "perk_speed_test";
        perkData.name = "Test speed";

    }

    @Override
    public void registerValue(String name, Object value) {

    }

    @Override
    public void processConfigFile(Path file, String contents) {

    }

    @Override
    public String getConfigExtension() {
        return "perkData";
    }



}
