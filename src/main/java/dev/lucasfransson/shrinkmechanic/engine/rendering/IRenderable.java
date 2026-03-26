package dev.lucasfransson.shrinkmechanic.engine.rendering;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.IPositioned;

public interface IRenderable extends IPositioned {
	List<Sprite> getSprites();
}