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
package com.flowpowered.api.geo.discrete;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;

import com.flowpowered.api.Flow;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.flowpowered.api.geo.World;

import com.flowpowered.commons.StringUtil;
import com.flowpowered.commons.concurrent.SpinLock;

import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.matrix.Matrix3f;
import com.flowpowered.math.matrix.Matrix4f;
import com.flowpowered.math.vector.Vector3f;

public final class Transform implements Serializable {
	private static final long serialVersionUID = 2L;
	private final transient SpinLock lock = new SpinLock();
	private Point position;
	private Quaternionf rotation;
	private Vector3f scale;

	public Transform() {
		this(Point.invalid, Quaternionf.IDENTITY, new Vector3f(1, 1, 1));
	}

	public Transform(Transform transform) {
		set(transform);
	}

	public Transform(Point position, Quaternionf rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Point getPosition() {
		try {
			lock.lock();
			return position;
		} finally {
			lock.unlock();
		}
	}

	public Transform setPosition(Point position) {
		try {
			lock.lock();
			this.position = position;
		} finally {
			lock.unlock();
		}
		return this;
	}

	public Transform translate(float x, float y, float z) {
		return translate(new Vector3f(x, y, z));
	}

	public Transform translate(Vector3f offset) {
		try {
			lock.lock();
			this.position = this.position.add(offset);
		} finally {
			lock.unlock();
		}
		return this;
	}

	public Transform rotate(Quaternionf offset) {
		try {
			lock.lock();
			this.rotation = offset.mul(rotation);
		} finally {
			lock.unlock();
		}
		return this;
	}

	public Transform scale(Vector3f offset) {
		try {
			lock.lock();
			this.scale = this.scale.add(offset);
		} finally {
			lock.unlock();
		}
		return this;
	}

	public Transform translateAndSetRotation(Vector3f offset, Quaternionf rotation) {
		try {
			lock.lock();
			this.position = this.position.add(offset);
			this.rotation = rotation;
		} finally {
			lock.unlock();
		}
		return this;
	}

	public Quaternionf getRotation() {
		try {
			lock.lock();
			return rotation;
		} finally {
			lock.unlock();
		}
	}

	public Transform setRotation(Quaternionf rotation) {
		try {
			lock.lock();
			this.rotation = rotation;
		} finally {
			lock.unlock();
		}
		return this;
	}

	public Vector3f getScale() {
		try {
			lock.lock();
			return scale;
		} finally {
			lock.unlock();
		}
	}

	public Transform setScale(Vector3f scale) {
		try {
			lock.lock();
			this.scale = scale;
		} finally {
			lock.unlock();
		}
		return this;
	}

	/**
	 * Atomically sets the value of this transform to the value of another transform
	 *
	 * @param transform the other transform
	 * @return this transform
	 */
	public Transform set(Transform transform) {
		if (transform == null) {
			throw new NullPointerException("Transform can not be a null argument!");
		}

		try {
			SpinLock.dualLock(lock, transform.lock);
			setUnsafe(transform.position, transform.rotation, transform.scale);
		} finally {
			SpinLock.dualUnlock(lock, transform.lock);
		}
		return this;
	}

	/**
	 * Atomically sets the value of this transform.
	 *
	 * @param world the world
	 * @param px the x coordinate of the position
	 * @param py the y coordinate of the position
	 * @param pz the z coordinate of the position
	 * @param rx the x coordinate of the quaternion
	 * @param ry the y coordinate of the quaternion
	 * @param rz the z coordinate of the quaternion
	 * @param rw the w coordinate of the quaternion
	 * @param sx the x coordinate of the scale
	 * @param sy the y coordinate of the scale
	 * @param sz the z coordinate of the scale
	 * @return this transform
	 */
	public Transform set(World world, float px, float py, float pz, float rx, float ry, float rz, float rw, float sx, float sy, float sz) {
		return this.set(new Point(world, px, py, pz), new Quaternionf(rx, ry, rz, rw), new Vector3f(sx, sy, sz));
	}

	/**
	 * Atomically sets this point to the given components
	 *
	 * @return this transform
	 */
	public Transform set(Point p, Quaternionf r, Vector3f s) {
		try {
			lock.lock();
			setUnsafe(p, r, s);
		} finally {
			lock.unlock();
		}
		return this;
	}

	private void setUnsafe(Point p, Quaternionf r, Vector3f s) {
		this.position = p;
		this.rotation = r;
		this.scale = s;
	}

	/**
	 * Creates a Transform that is a copy of this transform
	 *
	 * @return the snapshot
	 */
	public Transform copy() {
		try {
			lock.lock();
			return new Transform(position, rotation, scale);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Gets a String representation of this transform
	 * <p/>
	 * Note: unsafe, could return torn values
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + StringUtil.toString(position, rotation, scale);
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(41, 63);
		try {
			lock.lock();
			builder.append(position).append(rotation).append(scale);
		} finally {
			lock.unlock();
		}
		return builder.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Transform)) {
			return false;
		}
		Transform t = (Transform) other;
		try {
			SpinLock.dualLock(lock, t.lock);
			return position.equals(t.position) && rotation.equals(t.rotation) && scale.equals(t.scale);
		} finally {
			SpinLock.dualUnlock(lock, t.lock);
		}
	}

	/**
	 * Returns the 4x4 matrix that represents this transform object
	 */
	public Matrix4f toMatrix() {
		Matrix4f translate = Matrix4f.createTranslation(getPosition());
		Matrix4f rotate = Matrix4f.createRotation(getRotation());
		Matrix4f scale = Matrix4f.createScaling(getScale().toVector4(1));
		return scale.mul(rotate).mul(translate);
	}

	/**
	 * Returns a unit vector that points in the forward direction of this transform
	 */
	public Vector3f forwardVector() {
		return Matrix3f.createRotation(getRotation()).transform(Vector3f.FORWARD);
	}

	/**
	 * Returns a unit vector that points right in relation to this transform
	 */
	public Vector3f rightVector() {
		return Matrix3f.createRotation(getRotation()).transform(Vector3f.RIGHT);
	}

	/**
	 * Returns a unit vector that points up in relation to this transform
	 */
	public Vector3f upVector() {
		return Matrix3f.createRotation(getRotation()).transform(Vector3f.UP);
	}

	/**
	 * Returns if this Transform is "empty" <p> Empty is defined by Position, {@link Point}, of the transform equaling {@link Point#invalid}, Rotation, {@link com.flowpowered.math.imaginary.Quaternionf}, of the transform equaling
	 * {@link com.flowpowered.math.imaginary.Quaternionf#IDENTITY}, and Scale, {@link com.flowpowered.math.vector.Vector3f}, equaling {@link com.flowpowered.math.vector.Vector3f#ONE}.
	 *
	 * @return True if empty, false if not
	 */
	public boolean isEmpty() {
		try {
			lock.lock();
			return position.equals(Point.invalid) && rotation.equals(Quaternionf.IDENTITY) && scale.equals(Vector3f.ONE);
		} finally {
			lock.unlock();
		}
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
		try {
			Field field;

			field = Transform.class.getDeclaredField("spinLock");
			field.setAccessible(true);
            field.set(this, new SpinLock());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			if (Flow.debugMode()) {
				e.printStackTrace();
			}
		}
	}
    
}
