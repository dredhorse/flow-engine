/*
 * This file is part of Flow Engine, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.api.entity;

import java.util.List;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandSender;

import com.flowpowered.api.geo.discrete.Transform;

public interface Player extends CommandSender {
    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    @Override
    public String getName();

    /**
     * Gets the player's display name.
     *
     * @return the player's display name
     */
    public String getDisplayName();

    /**
     * Sets the player's display name.
     *
     * @param name the player's new display name
     */
    public void setDisplayName(String name);

    /**
     * Gets if the player is online
     *
     * @return true if online
     */
    public boolean isOnline();

    /**
     * Gets if the player has joined before
     *
     * @return true if joined before
     */
    public boolean hasJoinedBefore();

    /**
     * Kicks the player without giving a reason, or forcing it.
     */
    public void kick();

    /**
     * Kicks the player for the given reason.
     *
     * @param reason the message to send to the player.
     */
    public void kick(String reason);

    /**
     * Bans the player without giving a reason.
     */
    public void ban();

    /**
     * Bans the player for the given reason.
     *
     * @param kick whether to kick or not
     */
    public void ban(boolean kick);

    /**
     * Bans the player for the given reason.
     *
     * @param kick whether to kick or not
     * @param reason for ban
     */
    public void ban(boolean kick, String reason);

    /**
     * Immediately saves the players state to disk
     *
     * @return true if successful
     */
    public boolean save();

    /**
     * If an entity is set as invisible, it will not be sent to the client.
     */
    public void setVisible(Entity entity, boolean visible);

    /**
     * Retrieves a list of all invisible {@link Entity}'s to the player
     *
     * @return {@link List<{@link Entity}>} of invisible {@link Entity}'s
     */
    public List<Entity> getInvisibleEntities();

    /**
     * Returns true if the {@link Entity} provided is invisible this this {@link Player}
     *
     * @param entity Entity to check if invisible to the {@link Player}
     * @return true if the {@link Entity} is invisible
     */
    public boolean isInvisible(Entity entity);

    /**
     * Sends a command to be processed on the opposite Platform. This is basically a shortcut method to prevent the need to register a command locally with a {@link Command.NetworkSendType} of {@code
     * SEND}.
     *
     * @param command to send
     * @param args to send
     */
    public void sendCommand(String command, String... args);

    /**
     * @return the location that player's camera is at
     */
    public Transform getCameraLocation();

    public PlayerSnapshot snapshot();
}
