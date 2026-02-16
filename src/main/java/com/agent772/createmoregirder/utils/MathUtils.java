package com.agent772.createmoregirder.utils;

public class MathUtils {
	private MathUtils() {
		// Private constructor to prevent instantiation
	}

	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}

}
