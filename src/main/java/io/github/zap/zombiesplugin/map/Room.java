package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.shop.Shop;
import io.github.zap.zombiesplugin.shop.weaponschest.WeaponsChest;
import org.bukkit.entity.Player;

public class Room {

    private WeaponsChest weaponsChest; // TODO: Weapons chest must be entirely refactored, make sure this is only one double chest

    private String roomName;

    private Shop[] shops;

    private boolean opened = false;

    public Room(WeaponsChest weaponsChest /* Window[] windows*/, String roomName, Shop[] shops) {
        this.weaponsChest = weaponsChest;
        this.roomName = roomName;
        this.shops = shops;
    }

    public void open(Player opener) {
        if (!opened) {
            // TODO: Send message to everyone saying the room was opened
            for (Shop shop : shops) {
                shop.display();
            }

            opened = true;
        }
    }

    public String getRoomName() {
        return roomName;
    }
}
