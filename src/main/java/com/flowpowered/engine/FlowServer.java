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
package com.flowpowered.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.flowpowered.commons.StringUtil;

import com.flowpowered.api.Platform;
import com.flowpowered.api.Server;
import com.flowpowered.api.entity.Player;
import com.flowpowered.engine.entity.FlowPlayer;
import com.flowpowered.engine.geo.world.FlowServerWorldManager;
import com.flowpowered.engine.util.thread.snapshotable.SnapshotableLinkedHashMap;

public class FlowServer extends FlowEngine implements Server {
    protected final SnapshotableLinkedHashMap<String, FlowPlayer> players;
    private final FlowServerWorldManager worldManager;

    public FlowServer(FlowApplication args) {
        super(args);
        players = new SnapshotableLinkedHashMap<>(snapshotManager);
        worldManager = new FlowServerWorldManager(this);
    }

    @Override
    public Platform getPlatform() {
        return Platform.SERVER;
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
		Map<String, FlowPlayer> playerList = players.get();
		ArrayList<Player> onlinePlayers = new ArrayList<>(playerList.size());
		for (FlowPlayer player : playerList.values()) {
			if (player.isOnline()) {
				onlinePlayers.add(player);
			}
		}
		return onlinePlayers;
    }

    @Override
    public int getMaxPlayers() {
        // TODO: config
        return 5;
    }

    @Override
    public void broadcastMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void broadcastMessage(String permission, String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Player getPlayer(String name, boolean exact) {
		name = name.toLowerCase();
		if (exact) {
			for (Player player : players.getValues()) {
				if (player.getName().equalsIgnoreCase(name)) {
					return player;
				}
			}
			return null;
		} else {
			return StringUtil.getShortest(StringUtil.matchName(players.getValues(), name));
		}
    }

    @Override
    public FlowServerWorldManager getWorldManager() {
        return worldManager;
    }
}
