package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.guns.data.SoundFx;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Door extends Shop {

    private Room room;

    private Block[] doorBlocks;

    protected Door(GameManager gameManager, boolean requiresPower, int cost, SoundFx purchaseSuccessSound, Location hologramLocation, Room room, Block[] doorBlocks) {
        super(gameManager, requiresPower, cost, purchaseSuccessSound, hologramLocation, room.getRoomName(), String.valueOf(cost));
        this.doorBlocks = doorBlocks;
        this.room = room;
    }

    @Override
    public void onPurchaseAttempt(PlayerEvent event) {
      if (event instanceof PlayerInteractEvent) {
          PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;

          if (interactEvent.getClickedBlock() != null && interactEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
              Block block = interactEvent.getClickedBlock();

              for (Block doorBlock : doorBlocks) {
                      checkCostBeforePurchase(event);
                  if (doorBlock.equals(block)) {
                      break;
                  }
              }
          }
      }
    }

    @Override
    protected boolean purchase(User user) {
        room.open(user.getPlayer());
        return true;
    }

    // TODO: Method that restores the broken
}
