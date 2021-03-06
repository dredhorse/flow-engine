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

import com.flowpowered.events.EventManager;
import com.flowpowered.events.SimpleEventManager;

import com.flowpowered.api.Engine;
import com.flowpowered.api.material.MaterialRegistry;
import com.flowpowered.api.util.SyncedStringMap;
import com.flowpowered.engine.filesystem.FlowFileSystem;
import com.flowpowered.engine.scheduler.FlowScheduler;
import com.flowpowered.engine.util.thread.snapshotable.SnapshotManager;

public abstract class FlowEngine implements Engine {
    private final FlowApplication args;
    private final EventManager eventManager;
    private final FlowFileSystem fileSystem;

    private FlowScheduler scheduler;
    protected final SnapshotManager snapshotManager = new SnapshotManager();
    private SyncedStringMap itemMap;


    public FlowEngine(FlowApplication args) {
        this.args = args;
        this.eventManager = new SimpleEventManager();
        this.fileSystem = new FlowFileSystem();
    }

	@Override
	public String getVersion() {
		return getClass().getPackage().getImplementationVersion();
	}

    public void init() {
        itemMap = MaterialRegistry.setupRegistry();
        scheduler = new FlowScheduler(this);
    }

    public void start() {
        scheduler.startMainThread();
        System.out.println("Engine started.");
    }

    @Override
    public boolean stop() {
        scheduler.stop();
        System.out.println("Engine stopped");
        return true;
    }

    @Override
    public boolean stop(String reason) {
        return stop();
    }

    @Override
    public boolean debugMode() {
        return args.debug;
    }

    @Override
    public FlowScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public FlowFileSystem getFileSystem() {
        return fileSystem;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public String getName() {
        return "Flow Engine";
    }

    public SnapshotManager getSnapshotManager() {
        return snapshotManager;
    }
}
