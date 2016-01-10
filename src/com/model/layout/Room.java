package com.model.layout;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.model.entity.Entity;
import com.model.entity.npc.Enemy;
import com.model.entity.npc.EnemyProfile;
import com.view.GUI;

/**
 * A representation of a room in the game. This can contain various objects,
 * enemies, and room specific effects.
 *
 * @author Christopher
 *
 */
public class Room {

	/* Whether or not the room has been searched. */
	private boolean searched;
	/* Whether or not the player has been next to this room. */
	private boolean discovered;
	/* Whether or not the player has entered this room. */
	private boolean entered;

	/* Enemies within the room. */
	private List<Enemy> enemies;

	/* Objects found by searching this room. */
	private List<Entity> containedObjects;

	/* View to be notified by room events. */
	private GUI view;

	/**
	 * Constructs a new empty room with no special effects.
	 */
	public Room() {
		this(null);
	}

	/**
	 * Creates a new room that will be notified by the given GUI.
	 *
	 * @param view view to be notified.
	 */
	public Room(GUI view) {
		enemies = new ArrayList<Enemy>();
		containedObjects = new ArrayList<Entity>();
		searched = false;

		// TODO: Replace with actual enemy generation.

		EnemyProfile enemyStats = new EnemyProfile();
		enemyStats.name = "goblin";
		enemyStats.maxHP = BigInteger.valueOf(100);
		enemyStats.maxMana = BigInteger.valueOf(100);
		enemyStats.attackDamage = BigInteger.valueOf(20);
		enemyStats.xpValue = BigInteger.valueOf(5);

		this.view = view;
		addEnemy(new Enemy(enemyStats, this.view));
		hideEntity(new Enemy(enemyStats, this.view));
	}

	// Enemies and entities.

	/**
	 * Adds an enemy to the room. This will be ignored if the enemy is already
	 * in the room.
	 *
	 * @param enemy enemy to be added.
	 */
	public void addEnemy(Enemy enemy) {
		enemies.add(enemy);
	}

	/**
	 * Adds a list of enemies to the room.
	 *
	 * @param enemies enemies to be added.
	 */
	public void addEnemies(Collection<Enemy> enemies) {
		enemies.addAll(enemies);
	}

	/**
	 * Adds an entity that will be found when the room is searched.
	 *
	 * @param entity entity to be hidden.
	 */
	public void hideEntity(Entity entity) {
		containedObjects.add(entity);
	}

	/**
	 * Adds multiple entities that will be found when the room is searched.
	 *
	 * @param entities entities to be added.
	 */
	public void hideEntities(Collection<Entity> entities) {
		containedObjects.addAll(entities);
	}

	/**
	 * @return a list of enemies in the room.
	 */
	public List<Enemy> getEnemies() {
		Iterator<Enemy> enemyIterator = enemies.iterator();
		while (enemyIterator.hasNext())
			if (enemyIterator.next().isDead())
				enemyIterator.remove();
		return enemies;
	}

	// Flags

	/**
	 * Set the 'entered' flag as true, marking this room as having been entered
	 * at least once by the player.
	 */
	public void enter() {
		entered = true;
		discover();
	}

	/**
	 * @return true if the player has entered this room at least once.
	 */
	public boolean hasBeenEntered() {
		return entered;
	}

	/**
	 * Set the 'discovered' flag to true, marking this room as having been seen
	 * by the player.
	 */
	public void discover() {
		discovered = true;
	}

	/**
	 * @return true if the room has been discovered.
	 */
	public boolean hasBeenDiscovered() {
		return discovered;
	}

	/**
	 * Attempts to search the room, returning a set of all objects found.
	 *
	 * @return entities found by the search.
	 */
	public List<Entity> search() {
		searched = true;
		if (containedObjects.isEmpty()) {
			if (view != null)
				view.displayMessage("Nothing was found.");
		} else
			for (Entity entity : containedObjects)
				if (entity instanceof Enemy) {
					enemies.add((Enemy) entity);
					if (view != null)
						view.displayMessage("A " + entity + " attacked!");
				}
		return containedObjects;
	}

	/**
	 * @return true if the room has been searched, otherwise false.
	 */
	public boolean hasBeenSearched() {
		return searched;
	}

	/**
	 * Sets the view notified by events caused by this rooms and members.
	 *
	 * @param view gui to be notified.
	 */
	public void setView(GUI view) {
		this.view = view;
		for (Enemy enemy : enemies)
			enemy.setView(view);
		for (Entity entity : containedObjects)
			entity.setView(view);
	}
}
