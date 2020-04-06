package io.github.zap.zombiesplugin.utils;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;

/**
 * A <i>Base64</i> encoding utility for {@link ConfigurationSerializable}s
 */
@SuppressWarnings("unchecked")
public final class EncoderUtil {

    /**
     * Returns a {@code Base64}-encoded string representing the ConfigurationSerializables.
     *
     * @return The encoded kit.
     */
    public static <R extends ConfigurationSerializable> String toBase64(final R[] elements) {
        try {
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(byteStream);
            bukkitStream.writeInt(elements.length);
            for (R element : elements) {
                bukkitStream.writeObject(element);
            }
            bukkitStream.close();
            String encode = Base64Coder.encodeLines(byteStream.toByteArray());
            byteStream.close();
            return encode;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to save ConfigurationSerializables.", ex);
        }
    }

    /**
     * Returns a {@code Base64}-encoded string representing the ConfigurationSerializables.
     *
     * @param element Element to encode.
     * @return The encoded kit.
     */
    public static String toBase64(final ConfigurationSerializable element) {
        try {
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(byteStream);
            bukkitStream.writeObject(element);
            bukkitStream.close();
            String encode = Base64Coder.encodeLines(byteStream.toByteArray());
            byteStream.close();
            return encode;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to save ConfigurationSerializable.", ex);
        }
    }

    /**
     * Returns an array of {@link ConfigurationSerializable} from the specified Base64 string
     *
     * @param base64 String to decode
     * @return The ConfigurationSerializables
     */
    public static <R extends ConfigurationSerializable> R[] fromBase64(final String base64, Class<R> elementClass) {
        try {
            final ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            final BukkitObjectInputStream bukkitStream = new BukkitObjectInputStream(byteStream);
            final R[] elements = (R[]) Array.newInstance(elementClass, bukkitStream.readInt());
            for (int i = 0; i < elements.length; ++i) {
                elements[i] = (R) bukkitStream.readObject();
            }
            bukkitStream.close();
            byteStream.close();
            return elements;
        } catch (ClassNotFoundException | IOException ex) {
            throw new IllegalStateException("Unable to load ConfigurationSerializables.", ex);
        }
    }

    /**
     * Returns an array of {@link ConfigurationSerializable} from the specified Base64 string
     *
     * @param base64 String to decode
     * @return The ConfigurationSerializables
     */
    public static <R extends ConfigurationSerializable> R decodeElement(final String base64) {
        try {
            final ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            final BukkitObjectInputStream bukkitStream = new BukkitObjectInputStream(byteStream);
            R element = (R) bukkitStream.readObject();
            bukkitStream.close();
            byteStream.close();
            return element;
        } catch (ClassNotFoundException | IOException ex) {
            throw new IllegalStateException("Unable to load ConfigurationSerializables.", ex);
        }
    }
}
