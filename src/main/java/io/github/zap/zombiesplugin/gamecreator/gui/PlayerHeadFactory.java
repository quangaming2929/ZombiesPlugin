package io.github.zap.zombiesplugin.gamecreator.gui;

import io.github.zap.zombiesplugin.utils.EncoderUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * SB class to store custom head data
 */
public class PlayerHeadFactory {
    public static ItemStack getArrowRight() {
        return EncoderUtil.decodeElement(
 "rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGph\n" +
         "dmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAk\n" +
         "U2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAG\n" +
         "dmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAR0AAI9\n" +
         "PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9yeS5JdGVt\n" +
         "U3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFu\n" +
         "Zy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAC1BMQVlFUl9IRUFEc3EAfgAAc3EAfgADdXEAfgAG\n" +
         "AAAABHEAfgAIdAAJbWV0YS10eXBldAAMZGlzcGxheS1uYW1ldAAIaW50ZXJuYWx1cQB+AAYAAAAE\n" +
         "dAAISXRlbU1ldGF0AAVTS1VMTHQAFE9hayBXb29kIEFycm93IFJpZ2h0dAFgSDRzSUFBQUFBQUFB\n" +
         "QUUyT3kwNkRRQmhHZjAxTWtQZ1lia200eUJTV1JtdzdwRE5JTzNMYmNabktaYWdOaGNyMHFYeEVX\n" +
         "YnI4Y3M1SlBoVkFoYWRETndueE1Yd2ZHOEVWdU1jVlBGZTJZVlVJSVUxM1hWTjdNY3VWNXVZNTBy\n" +
         "aHRPTGxqY2Iyd0hCWFVKVHJ6WVd6NDVSR1VrYy9qTlBDTENnQjNDanhFdVpnNC9ITHA2MWxTNjFY\n" +
         "aWkxSml0R3gyMEVXQTIvTUtueUpadkdHRSs0VnZYOUZPdXY5Y2U4eGpXNlNXWDJlbmNDcjZTTjla\n" +
         "ZThHM2U2UHNQNitFaVNacmlReVlMd2pEWnNyZWYyaU1EYnFKbXBTVk16SFhOWW54VEpuZlVSYk9L\n" +
         "VnQzZ2RmZEF1OUxCakV4YUV0TXVnbG5lcXRGNW1HWjltbHpUQXgzZVE5L3dpOFBRaGdCQUFBPQ==\n");
    }

    public static ItemStack getInfoHead() {
        return CommonVisual.createVisualItem(Material.BARRIER, 1, "Please implement", Arrays.asList(ChatColor.GRAY + "SB!"));
    }
}
