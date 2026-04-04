package dev.lucasfransson.shrinkmechanic.items;

import dev.lucasfransson.shrinkmechanic.world.objects.WorldObject;

public interface IPlaceableItem<T extends WorldObject> {
	public T createWorldObject();
}
