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
import team.unnamed.hephaestus.animation.ModelAnimation;
import team.unnamed.hephaestus.partial.ModelAsset;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a {@link Bone} compound which also holds
 * the animations and the resource-pack information
 *
 * @since 1.0.0
 */
public class Model {

    private final String name;
    private final Map<String, Bone> bones;
    private ModelAsset asset;
    private final Map<String, ModelAnimation> animations;

    public Model(
            String name,
            Map<String, Bone> bones,
            ModelAsset asset
    ) {
        this.name = name;
        this.bones = bones;
        this.asset = asset;
        // data from 'asset' that will persist after calling
        // discardResourcePackData()
        this.animations = asset.animations();
    }

    /**
     * Returns this model's name
     *
     * @return The model name
     */
    public String name() {
        return name;
    }

    /**
     * Returns a collection of the {@link Bone}
     * instances that compose this {@link Model}
     *
     * @return The model bones
     */
    public Collection<Bone> bones() {
        return bones.values();
    }

    /**
     * Returns a map of the {@link Bone} instances
     * that compose this {@link Model}, the keys
     * in this map are the bone names
     *
     * @return The model bone map
     */
    public Map<String, Bone> boneMap() {
        return bones;
    }

    /**
     * Returns a map of the registered animations
     * for this model, keys are the animation names
     *
     * @return The model animations
     */
    public Map<String, ModelAnimation> animations() {
        return animations;
    }

    @Nullable
    public ModelAsset asset() {
        return asset;
    }

    /**
     * Discards the information used only in
     * the resource pack generation in this
     * model instance
     */
    public void discardResourcePackData() {
        this.asset = null;
    }

}