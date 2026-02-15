/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.network;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

/**
 * An object on a {@link IPacketNetwork}, capable of sending packets.
 */
public interface IPacketSender
{
    /**
     * Get the world in which this packet sender exists.
     *
     * @return The sender's world.
     */
    @Nonnull
    Level getLevel();

    /**
     * Get the position in the world at which this sender exists.
     *
     * @return The sender's position.
     */
    @Nonnull
    Vec3 getPosition();

    /**
     * Get the maximum distance this receiver can send and receive messages.
     *
     * When determining whether a receiver can receive a message, the largest distance of the packet and receiver is
     * used - ensuring it is within range. If the packet or receiver is inter-dimensional, then the packet will always
     * be received.
     *
     * @param level The level receiver is at. This is provided as an argument to allow for senders which have different
     *              ranges in different dimensions.
     * @return The maximum distance this device can send and receive messages.
     * @see #isInterdimensional()
     * @see IPacketNetwork#transmitInterdimensional(Packet)
     */
    double getRangeAtLevel( Level level );

    /**
     * Determine whether this receiver can receive packets from other dimensions.
     *
     * A device will receive an inter-dimensional packet if either it or the sending device is inter-dimensional.
     *
     * @return Whether this receiver receives packets from other dimensions.
     * @see #getRangeAtLevel(Level)
     * @see IPacketNetwork#transmitInterdimensional(Packet)
     */
    boolean isInterdimensional();

    /**
     * Get some sort of identification string for this sender. This does not strictly need to be unique, but you
     * should be able to extract some identifiable information from it.
     *
     * @return This device's id.
     */
    @Nonnull
    String getSenderID();
}
