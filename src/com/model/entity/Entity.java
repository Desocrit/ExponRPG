package com.model.entity;

import com.view.GUI;

/**
 * An entity in the game world. This includes everything that could be found or
 * placed in a room, but not elements of the dungeon itself.
 *
 * @author Christopher
 *
 */
public abstract class Entity {

	/* The name of the entity. Should be human readable. */
	private String name;

	/* Whether or not this character is considered dead. */
	private boolean isDead;

	/* The game controller. Should only be used by extending classes. */
	protected GUI view;
	/* Whether notifications are sent. */
	protected boolean silent;

	/**
	 * Creates an Entity with the given name that does not send notifications.
	 *
	 * @param name name of the entity. Should be human readable.
	 */
	public Entity(String name) {
		this(name, null);
	}

	/**
	 * Creates an Entity with the given name that will send notifications to the
	 * given GUI.
	 *
	 * @param name name of the entity. Should be human readable.
	 * @param view The GUI to send messages to.
	 */
	public Entity(String name, GUI view) {
		this.name = name;
		this.view = view;
		silent = view == null;
	}

	/**
	 * @return the name of the entity.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Kills off the entity, potentially removing it from the game. The entity
	 * responsible has its registerKill method called. This implementation of
	 * this method only calls registerKill.
	 *
	 * @param source entity responsible for the death.
	 */
	public void die(Entity source) {
		source.registerKill(this);
		isDead = true;
	}

	/**
	 * Registers killing off an entity. Aautomatically called by the die method.
	 * Default implementation of this method is empty.
	 *
	 * @param target entity killed off by this entity.
	 */
	public void registerKill(Entity target) {

	}

	/**
	 * Sets the output view to send notifications to. Notifications and events
	 * will be send to this GUI.
	 *
	 * @param view view to send messages to
	 */
	public void setView(GUI view) {
		silent = false;
		this.view = view;
	}

	/**
	 * @return whether or not this character is considered dead.
	 */
	public boolean isDead() {
		return isDead;
	}
}
