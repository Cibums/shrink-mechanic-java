package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public class Rock extends WorldObject {

	public Rock() {
		super(Renderable.getTextureFromPath("/rock.png"));
		this.setSpriteAlignment(SpriteAlignment.CENTER);
		this.setSize(new Vector2(12, 8));
	}

	@Override
	public void onDestroy() {
	}

}
