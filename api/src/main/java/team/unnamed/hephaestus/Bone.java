/*
 * This file is part of hephaestus-engine, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.hephaestus;

import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Vector3Float;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a {@link Model} movable part, in the
 * game, it has its own Creative's {@link team.unnamed.creative.model.Model}
 * and its own armor_stand entity
 *
 * @since 1.0.0
 */
public class Bone {

    @Nullable private final Bone parent;
    private final String name;
    private final Vector3Float rotation;

    private final Map<String, Bone> bones;
    private final Vector3Float offset;

    private final boolean small;
    private final int customModelData;

    public Bone(
            @Nullable Bone parent,
            String name,
            Vector3Float rotation,
            Map<String, Bone> bones,
            Vector3Float offset,
            boolean small,
            int customModelData
    ) {
        this.parent = parent;
        this.name = name;
        this.rotation = rotation;
        this.bones = bones;
        this.offset = offset;
        this.small = small;
        this.customModelData = customModelData;
    }

    /**
     * Returns this bone parent bone
     *
     * @return The bone parent
     */
    public @Nullable Bone parent() {
        return parent;
    }

    /**
     * Returns this bone unique name, bone names
     * are unique in the {@link Model} scope
     *
     * @return The bone name
     */
    public String name() {
        return name;
    }

    /**
     * Returns this bone offset, which is, in
     * other words, the position of this bone
     * (relative to parent's position)
     *
     * @return The bone offset
     */
    public Vector3Float offset() {
        return offset;
    }

    /**
     * Returns this bone custom model data,
     * which must be applied to the creative
     * Model representing this bone
     *
     * @return The bone custom model data
     */
    public int customModelData() {
        return customModelData;
    }

    /**
     * Returns this bone initial rotation
     *
     * @return The bone initial rotation
     */
    public Vector3Float rotation() {
        return rotation;
    }

    /**
     * Determines whether to use small armor stands
     * for this bone
     *
     * @return True to use small armor stands
     */
    public boolean small() {
        return small;
    }

    /**
     * Returns this bone child bones
     *
     * @return The child bones
     */
    public Collection<Bone> bones() {
        return bones.values();
    }

    /**
     * Returns a map of this bone children
     * bones, keys are bone names
     *
     * @return The child bone map
     */
    public Map<String, Bone> boneMap() {
        return bones;
    }

}