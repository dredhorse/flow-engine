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

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.flowpowered.api.Platform;
import com.flowpowered.api.Singleplayer;
import com.flowpowered.api.entity.Entity;
import com.flowpowered.api.entity.Player;
import com.flowpowered.api.geo.LoadOption;
import com.flowpowered.api.geo.World;
import com.flowpowered.api.generator.FlatWorldGenerator;
import com.flowpowered.api.material.BlockMaterial;
import com.flowpowered.engine.entity.FlowPlayer;
import com.flowpowered.engine.geo.world.FlowWorld;
import com.flowpowered.engine.render.DeployNatives;
import com.flowpowered.engine.render.FlowRenderer;

import com.flowpowered.math.vector.Vector3f;

public class FlowSingleplayer extends FlowServer implements Singleplayer {
    private final AtomicReference<FlowPlayer> player = new AtomicReference<>();
    private final AtomicReference<FlowWorld> activeWorld = new AtomicReference<>();

    // TEST CODE
    private Entity testEntity;

    public FlowSingleplayer(FlowApplication args) {
        super(args);
    }

    @Override
    public void init() {
        try {
            DeployNatives.deploy();
        } catch (Exception ex) {
            Logger.getLogger(FlowSingleplayer.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        super.init();
        FlowWorld loadedWorld = getWorldManager().loadWorld("fallback", new FlatWorldGenerator(BlockMaterial.SOLID_BLUE));
        activeWorld.set(loadedWorld);
        FlowPlayer player = new FlowPlayer("Flowy");
        this.player.set(player);
        players.put(player.getName(), player);
        Entity entity = loadedWorld.spawnEntity(Vector3f.ZERO, LoadOption.LOAD_GEN);
        this.testEntity = entity;
    }

    public Entity getTestEntity() {
        return testEntity;
    }

    @Override
    public void start() {
        getScheduler().startClientThreads();
        super.start();
    }

    @Override
    public boolean stop() {
        return super.stop();
        
    }

    @Override
    public Platform getPlatform() {
        return Platform.SINGLEPLAYER;
    }

    @Override
    public Player getPlayer() {
        return player.get();
    }

    @Override
    public World getWorld() {
        return activeWorld.get();
    }

    @Override
    public FlowRenderer getRenderer() {
        return getScheduler().getRenderThread().getRenderer();
    }

}
