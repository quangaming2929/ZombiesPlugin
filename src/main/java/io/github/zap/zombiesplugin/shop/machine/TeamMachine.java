package io.github.zap.zombiesplugin.shop.machine;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class TeamMachine {
    protected final GameManager manager;
    protected final HashMap<Integer, TeamMachineTask> tasks = new HashMap<>();
    protected final Hashtable<User, Inventory> storedGuiInstances = new Hashtable<>();

    private static final String GUI_NAME = "Team Machine";
    private int guiSize = -1;

    public TeamMachine(GameManager manager, List<TeamMachineTask> tasks) {
        this.manager = manager;

        prepareInv(tasks.size(), tasks);
    }

    public void openTeamMachineGUI(User user) {
        Inventory ci = storedGuiInstances.get(user);
        if (ci == null) {
            ci = Bukkit.createInventory(null, guiSize, GUI_NAME);
            storedGuiInstances.put(user, ci);
        }

        for (Map.Entry<Integer, TeamMachineTask> entry : tasks.entrySet()) {
            ci.setItem(entry.getKey(), entry.getValue().getVisual(user));
        }

        user.getPlayer().openInventory(ci);
    }


    private void prepareInv(int num, List<TeamMachineTask> tasks) {
        int width = (int) Math.ceil(Math.sqrt(num));
        int height = (int) Math.ceil(num / (float)width);
        int remainderLine = (int) Math.floor(Math.min(6, height) / 2);
        // this is the first line offset
        int offset = height + 2 <= 6 ? 1 : 0;
        // If the height go higher than 6 we need to change our calculation
        if(height > 6) {
            width = (int) Math.ceil(num / (float)6);
        }
        int finalLine = num % width;
        if (finalLine == 0) {
            finalLine = width;
        }

        guiSize = 9 * Math.min(6, height + 2);

        int i = 0;

        for (int h = 0; h < height; h++) {
            int lineCount = h == remainderLine ? finalLine : width;
            for (int w = 0; w < lineCount; w++) {
                if (i >= num) {
                    return;
                }
                // If we are at the last line

                int slotID = alignItem(9, lineCount, w + 1);
                int pos = (h + offset) * 9 + slotID;
                TeamMachineTask task = tasks.get(i);
                this.tasks.put(pos, task);
                i++;
            }
        }
    }

    /**
     * This method get the location of an item so that all items in a line will have an even spacing
     * Well Idk is there a better solution, but I gonna use my own algo.
     */
    private int alignItem (float width, float count, float index) {
        return (int)Math.floor((2 * width * index - width) / (2 * count));
    }


    public boolean processClick(InventoryClickEvent event, User user) {
        TeamMachineTask clickedTask = tasks.get(event.getSlot());
        if (clickedTask != null) {
            clickedTask.tryPurchase(user);
            user.getPlayer().closeInventory();
        }

        return true;
    }

    public boolean contains (Inventory inventory) {
        return inventory != null && storedGuiInstances.containsValue(inventory);
    }
}
