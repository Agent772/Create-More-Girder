package com.agent772.createmoregirder.content.strut.geometry;

import org.joml.Vector3f;

/**
 * Girder Strut Block implementation
 * 
 * Adapted from Bits-n-Bobs by Industrialists-Of-Create
 * Original: https://github.com/Industrialists-Of-Create/Bits-n-Bobs
 * Licensed under MIT License
 * 
 * Modifications:
 * - Adapted for Create: More Girder mod structure
 */

public record GirderVertex(Vector3f position, Vector3f normal, float u, float v, int color, int light) {
}
