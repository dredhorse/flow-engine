/*
 * This file is part of Spout.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spout is licensed under the Spout License Version 1.
 *
 * Spout is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Spout is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package com.flowpowered.api.generator;

import com.flowpowered.commons.Named;

import com.flowpowered.api.geo.World;
import com.flowpowered.api.util.cuboid.CuboidBlockMaterialBuffer;

/**
 * Represents a World generator.
 *
 * WorldGenerators are used to generate {@link World}s (surprise surprise)
 */
public interface WorldGenerator extends Named {
	/**
	 * Gets the block structure for a Chunk.
	 *
	 * The CuboidBuffer will always be chunk-aligned, and could be of a variable (chunk) size.<br><br> Use {@link CuboidBlockMaterialBuffer#getBase()} and {@link CuboidBlockMaterialBuffer#getTop()} to
	 * obtain the Block bounds in which can be generated.
	 *
	 * It is recommended that seeded random number generators from WorldGeneratorUtils are used.
	 *
	 * @param blockData a zeroed CuboidBuffer which has to be fully generated
	 * @param world in which is generated
	 */
	public void generate(CuboidBlockMaterialBuffer blockData, World world);

	/**
	 * Gets an array of Populators for the world generator
	 *
	 * @return the Populator array
	 */
	public Populator[] getPopulators();

	/**
	 * Gets the name of the generator. This name should be unique to prevent two generators overwriting the same world
	 */
	@Override
	public String getName();
}