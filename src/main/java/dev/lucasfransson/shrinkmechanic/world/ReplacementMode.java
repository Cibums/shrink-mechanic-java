package dev.lucasfransson.shrinkmechanic.world;

public enum ReplacementMode {
	/**
	 * If an object already exists at the target position, abort placement.
	 */
	KEEP,

	/**
	 * If an object already exists, call onDestroy() before replacing it. Use
	 * this when effects, drops, or cleanup logic should trigger.
	 */
	DESTROY,

	/**
	 * If an object already exists, silently remove it before placing the new
	 * one. Use this for world generation and programmatic placement where
	 * destroy effects are not desired.
	 */
	REPLACE
}