package io.github.zap.zombiesplugin.player;

import net.zswca.zombie.enums.PerkType;
import net.zswca.zombie.enums.UserStatus;
import net.zswca.zombie.utils.Constants;
import org.bukkit.entity.Player;

/**
 * Represents the player that is in game
 */
public class User {

    private GunUser gun;

    private Player player;
    private int gold;
    private UserStatus status;
    private PerkType[] perks;

    /**
     * Constructor to initialize player defaults
     *
     * @param player the Player to represent the User object
     */
    public User(Player player){
        setPlayer(player);
        setGold(0);
        perks = new PerkType[Constants.MAX_PERK_COUNT];
    }

    /**
     * Get the Player representing the User object
     *
     * @return the Player associated with the User object
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Set the Player to the User object
     *
     * @param player the Player to represent the User object
     */
    private void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Get the gold of the User
     *
     * @return int representing User's gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * Sets the gold of the User
     *
     * @param gold the int to set the User's gold to
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Gets the status of the User, i.e, alive, knocked, or dead.
     *
     * @return UserStatus of the User
     */
    public UserStatus getStatus(){
        return status;
    }

    /**
     * Sets the status of the User.
     *
     * @param userStatus the UserStatus to set
     */
    public void setStatus(UserStatus userStatus){
        this.status = userStatus;
    }

    /**
     * Get the perks of the User. Each index corresponds to the perk slot.
     *
     * @return an array of perks that the player has
     */
    public PerkType[] getPerks() {
        return perks;
    }

    /**
     * Checks if the User has a certain perk.
     *
     * @param perk the perk to check
     * @return true if the User has the perk, otherwise false
     */
    public boolean hasPerk(PerkType perk){
        for(PerkType p : getPerks()){
            if(perk.equals(p)){
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the perk on a designated slot.
     *
     * @param perk the perk to set
     * @param slot the slot to set
     */
    public void setPerk(PerkType perk, int slot){
        this.perks[slot] = perk;
    }

    /**
     * Sets all slots to designated perks.
     *
     * @param perks the perks to set the slots to
     */
    public void setPerk(PerkType... perks){
        if(perks.length == Constants.MAX_PERK_COUNT)
            this.perks = perks;
    }

    public GunUser getGun() {
        return gun;
    }

    public void setGun(GunUser gun) {
        this.gun = gun;
    }
}
