/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wireless;

import dan200.computercraft.api.network.IPacketNetwork;
import dan200.computercraft.api.network.IPacketReceiver;
import dan200.computercraft.api.network.IPacketSender;
import dan200.computercraft.api.network.Packet;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WirelessNetwork implements IPacketNetwork
{
    private static WirelessNetwork universalNetwork = null;

    public static WirelessNetwork getUniversal()
    {
        if( universalNetwork == null ) universalNetwork = new WirelessNetwork();
        return universalNetwork;
    }

    public static void resetNetworks()
    {
        universalNetwork = null;
    }

    private final Set<IPacketReceiver> receivers = Collections.newSetFromMap( new ConcurrentHashMap<>() );

    @Override
    public void addReceiver( @Nonnull IPacketReceiver receiver )
    {
        Objects.requireNonNull( receiver, "device cannot be null" );
        receivers.add( receiver );
    }

    @Override
    public void removeReceiver( @Nonnull IPacketReceiver receiver )
    {
        Objects.requireNonNull( receiver, "device cannot be null" );
        receivers.remove( receiver );
    }

    @Override
    public void transmitSameDimension( @Nonnull Packet packet )
    {
        Objects.requireNonNull( packet, "packet cannot be null" );
        for( IPacketReceiver device : receivers ) tryTransmit( device, packet, false );
    }

    @Override
    public void transmitInterdimensional( @Nonnull Packet packet )
    {
        Objects.requireNonNull( packet, "packet cannot be null" );
        for( IPacketReceiver device : receivers ) tryTransmit( device, packet, true );
    }

    private static void tryTransmit( IPacketReceiver receiver, Packet packet, boolean interdimensional )
    {
        IPacketSender sender = packet.sender();
        double receiveRange = sender.getRangeAtLevel( receiver.getLevel() );
        double distanceSq = receiver.getPosition().distanceToSqr( sender.getPosition() );

        if( distanceSq <= receiveRange * receiveRange )
        {
            if( sender.getLevel() == receiver.getLevel() )
            {
                receiver.receiveSameDimension( packet, Math.sqrt( distanceSq ) );
            }
            else
            {
                receiver.receiveDifferentDimension( packet );
            }
        }
    }

    @Override
    public boolean isWireless()
    {
        return true;
    }
}
