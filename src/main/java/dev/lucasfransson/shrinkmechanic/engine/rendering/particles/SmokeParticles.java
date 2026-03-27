package dev.lucasfransson.shrinkmechanic.engine.rendering.particles;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Animation;

public class SmokeParticles extends ParticleSystem {

	private static final Animation ANIMATION = new Animation("/smoke", .25);

	public SmokeParticles(Vector2 position) {
		super(position, ANIMATION);
	}

}
