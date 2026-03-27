package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.List;
import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import dev.lucasfransson.shrinkmechanic.items.ItemDrop;
import dev.lucasfransson.shrinkmechanic.items.RockItem;

public class Rock extends WorldObject implements IRandomizable {

	public Rock() {
		super(new Sprite(Sprite.getTextureFromPath("/rock.png")));
		getMainSprite().setSpriteAlignment(SpriteAlignment.CENTER);
		this.setSize(new Vector2(0.375, 0.20));
	}

	@Override
	public void randomize(Random rnd) {
		applyPositionRandomization(rnd, 0.2);
		applySizeRandomization(rnd, 0.9, 1.1);
	}

	@Override
	public List<ItemDrop> getDrops() {
		return List.of(ItemDrop.of(new RockItem(), 1, 3));
	}
}