package org.spout.engine.geo.world;

import org.spout.engine.geo.region.SpoutRegion;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.flowpowered.commons.bit.ShortBitMask;
import com.flowpowered.commons.bit.ShortBitSet;
import com.flowpowered.events.Cause;

import org.spout.api.component.BaseComponentOwner;
import org.spout.api.component.Component;
import org.spout.api.entity.Entity;
import org.spout.api.entity.EntityPrefab;
import org.spout.api.entity.Player;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.material.BlockMaterial;
import org.spout.api.scheduler.TaskManager;
import org.spout.api.scheduler.TickStage;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;
import org.spout.engine.SpoutEngine;
import org.spout.engine.entity.EntityManager;
import org.spout.engine.entity.SpoutEntity;
import org.spout.engine.geo.region.RegionSource;
import org.spout.engine.geo.SpoutBlock;
import org.spout.engine.geo.chunk.SpoutChunk;
import org.spout.engine.geo.snapshot.WorldSnapshot;
import org.spout.engine.util.thread.CopySnapshotManager;
import org.spout.engine.util.thread.snapshotable.SnapshotManager;
import org.spout.engine.util.thread.snapshotable.SnapshotableLong;
import com.flowpowered.math.GenericMath;
import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.vector.Vector3f;

public class SpoutWorld extends BaseComponentOwner implements World, CopySnapshotManager {
    private final SpoutEngine engine;
    private final String name;
    private final UUID uid;
    private final SnapshotManager snapshotManager;
    private final SnapshotableLong age;
    private final RegionSource regionSource;
    private final WorldSnapshot snapshot;

    public SpoutWorld(SpoutEngine engine, String name, UUID uid, long age) {
        this.engine = engine;
        this.name = name;
        this.uid = uid;
        this.snapshotManager = new SnapshotManager();
        this.age = new SnapshotableLong(snapshotManager, age);
        this.regionSource = new RegionSource(engine, (SpoutServerWorld) this);
        this.snapshot = new WorldSnapshot(this);
    }

    public SpoutWorld(SpoutEngine engine, String name) {
        this(engine, name, UUID.randomUUID(), 0);
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getAge() {
        return age.get();
    }

    @Override
    public UUID getUID() {
        return uid;
    }

    @Override
    public Entity getEntity(UUID uid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity spawnEntity(Vector3f point, LoadOption option, EntityPrefab prefab) {
        return spawnEntity(point, option, prefab.getComponents().toArray(new Class[0]));
    }

    @Override
    public Entity spawnEntity(Vector3f point, LoadOption option, Class<? extends Component>... classes) {
        SpoutRegion region = (SpoutRegion) getRegionFromBlock(point, option);
        if (region == null) {
            return null;
        }

        SpoutEntity entity = EntityManager.createEntity(new Transform(new Point(point, this), Quaternionf.fromAxesAnglesDeg(0, 0, 0), Vector3f.ONE));
		region.getEntityManager().addEntity(entity);
        return entity;
    }

    @Override
    public Entity[] spawnEntities(Vector3f[] points, LoadOption option, Class<? extends Component>... classes) {
        Entity[] entities = new Entity[points.length];
		for (int i = 0; i < points.length; i++) {
			entities[i] = spawnEntity(points[i], option, classes);
		}
		return entities;
    }

    @Override
    public SpoutEngine getEngine() {
        return engine;
    }

    @Override
    public List<Entity> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity getEntity(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TaskManager getParallelTaskManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TaskManager getTaskManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getNearbyEntities(Point position, Entity ignore, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getNearbyEntities(Point position, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getNearbyEntities(Entity entity, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity getNearestEntity(Point position, Entity ignore, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity getNearestEntity(Point position, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity getNearestEntity(Entity entity, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Player> getNearbyPlayers(Point position, Player ignore, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Player> getNearbyPlayers(Point position, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Player> getNearbyPlayers(Entity entity, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Player getNearestPlayer(Point position, Player ignore, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Player getNearestPlayer(Point position, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Player getNearestPlayer(Entity entity, int range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCuboid(CuboidBlockMaterialBuffer buffer, Cause<?> cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCuboid(int x, int y, int z, CuboidBlockMaterialBuffer buffer, Cause<?> cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CuboidBlockMaterialBuffer getCuboid(int bx, int by, int bz, int sx, int sy, int sz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getCuboid(int bx, int by, int bz, CuboidBlockMaterialBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getCuboid(CuboidBlockMaterialBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Player> getPlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Region> getRegions() {
        return regionSource.getRegions();
    }

    public Collection<SpoutRegion> getSpoutRegions() {
        return (Collection) getRegions();
    }

    @Override
    public SpoutRegion getRegion(int x, int y, int z, LoadOption loadopt) {
        return regionSource.getRegion(x, y, z, loadopt);
    }

    @Override
    public SpoutRegion getRegionFromChunk(int x, int y, int z, LoadOption loadopt) {
        return getRegion(x >> Region.CHUNKS.BITS, y >> Region.CHUNKS.BITS, z >> Region.CHUNKS.BITS, loadopt);
    }

    @Override
    public SpoutRegion getRegionFromBlock(int x, int y, int z, LoadOption loadopt) {
        return getRegion(x >> Region.BLOCKS.BITS, y >> Region.BLOCKS.BITS, z >> Region.BLOCKS.BITS, loadopt);
    }

    @Override
    public SpoutRegion getRegionFromBlock(Vector3f position, LoadOption loadopt) {
        return getRegionFromBlock(position.getFloorX(), position.getFloorY(), position.getFloorZ(), loadopt);
    }

    @Override
    public boolean containsChunk(int x, int y, int z) {
        return true;
    }

    @Override
    public SpoutChunk getChunk(int x, int y, int z, LoadOption loadopt) {
        SpoutRegion region = getRegionFromChunk(x, y, z, loadopt);
        if (region == null) {
            return null;
        }
        return region.getChunk(x, y, z, loadopt);
    }

    @Override
    public Chunk getChunkFromBlock(int x, int y, int z, LoadOption loadopt) {
        Region region = getRegionFromBlock(x, y, z, loadopt);
        if (region == null) {
            return null;
        }
        return region.getChunkFromBlock(x, y, z, loadopt);
    }

    @Override
    public Chunk getChunkFromBlock(Vector3f position, LoadOption loadopt) {
        Region region = getRegionFromBlock(position, loadopt);
        if (region == null) {
            return null;
        }
        return region.getChunkFromBlock(position, loadopt);
    }

    @Override
    public void saveChunk(int x, int y, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unloadChunk(int x, int y, int z, boolean save) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumLoadedChunks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setBlockData(int x, int y, int z, short data, Cause<?> cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addBlockData(int x, int y, int z, short data, Cause<?> cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setBlockMaterial(int x, int y, int z, BlockMaterial material, short data, Cause<?> cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean compareAndSetData(int x, int y, int z, int expect, short data, Cause<?> cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short setBlockDataBits(int x, int y, int z, int bits, Cause<?> cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short setBlockDataBits(int x, int y, int z, int bits, boolean set, Cause<?> source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short clearBlockDataBits(int x, int y, int z, int bits, Cause<?> source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getBlockDataField(int x, int y, int z, int bits) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBlockDataBitSet(int x, int y, int z, int bits) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int setBlockDataField(int x, int y, int z, int bits, int value, Cause<?> source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int addBlockDataField(int x, int y, int z, int bits, int value, Cause<?> source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsBlock(int x, int y, int z) {
        return true;
    }

	@Override
	public SpoutBlock getBlock(int x, int y, int z) {
		return new SpoutBlock(this, x, y, z);
	}

	@Override
	public SpoutBlock getBlock(float x, float y, float z) {
		return this.getBlock(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
	}

	@Override
	public SpoutBlock getBlock(Vector3f position) {
		return this.getBlock(position.getX(), position.getY(), position.getZ());
	}

    @Override
    public boolean commitCuboid(CuboidBlockMaterialBuffer buffer, Cause<?> cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CuboidBlockMaterialBuffer getCuboid(boolean backBuffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CuboidBlockMaterialBuffer getCuboid(int bx, int by, int bz, int sx, int sy, int sz, boolean backBuffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockMaterial getBlockMaterial(int x, int y, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getBlockFullState(int x, int y, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short getBlockData(int x, int y, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public WorldSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public void copySnapshotRun(int sequence) {
        // TODO: modified status
        snapshot.update(this);
    }

    @Override
    public boolean checkSequence(TickStage stage, int sequence) {
        if (stage == TickStage.SNAPSHOT) {
            return sequence == 0;
        }
        return sequence == -1;
    }

    @Override
    public Thread getExecutionThread() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setExecutionThread(Thread t) {
    }

    private static ShortBitMask STAGES = TickStage.allOf(TickStage.SNAPSHOT);
    @Override
    public ShortBitMask getTickStages() {
        return STAGES;
    }
}
