package com.model.entity.pc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.model.entity.Entity;
import com.model.entity.npc.Enemy;
import com.utils.StringUtils;
import com.view.GUI;
import com.view.images.MiscImage;

/**
 * The character sheet of the player of the game. This tracks the player's
 * health, mana, experience value, level, and attributes.
 *
 * @author Christopher
 *
 */
public class Player extends com.model.entity.GameCharacter {

	/* Current amount of xp. */
	private BigInteger xp;
	/* xp until next level */
	private BigInteger xpToLevel;

	/* Current power level. Increases by 1 whenever xp reaches the cap. */
	private BigInteger level;
	/* A map of stats to their values. */
	private Map<Attribute, BigInteger> attributes;
	/* The class that the player is playing as. */
	private PlayerClass playerClass;

	private MiscImage image;

	/**
	 * Initialises a player based on a specified player class.
	 *
	 * Initial stats will be set to 10* modifier value.
	 *
	 * @param playerClass RPG Class that the player has selected. Influences
	 *            initial attributes and spell selection.
	 */
	public Player(PlayerClass playerClass) {
		super("player");
		initPlayer(playerClass);
	}

	/**
	 * Initialises a player based on a specified player class, that sends
	 * notifications to a specified GUI.
	 *
	 * Initial stats will be set to 10* modifier value.
	 *
	 * @param playerClass RPG Class that the player has selected. Influences
	 *            initial attributes and spell selection.
	 * @param view a reference to the interface, to be passed messages whenever
	 *            the player's status changes in any way.
	 */
	public Player(PlayerClass playerClass, GUI view) {
		super("player", view);
		initPlayer(playerClass);
	}

	/* Sets up starting stats and attributes. */
	private void initPlayer(PlayerClass playerClass) {
		// Set the player information.
		this.playerClass = playerClass;

		// Get initial stats from PlayerClass..
		Map<Attribute, Integer> classAttributes = playerClass
				.getBaseAttributes();
		attributes = new HashMap<Attribute, BigInteger>();
		for (Attribute stat : classAttributes.keySet())
			attributes.put(stat, BigInteger.valueOf(classAttributes.get(stat)));
		updateHealthAndMana();

		// Set XP, XP to level, and level.
		xp = BigInteger.ZERO;
		level = BigInteger.ONE;
		xpToLevel = BigInteger.TEN;
	}

	// Killing and dying based functions.

	/**
	 * Function to be called when the player has no remaining health. Currently
	 * does nothing, but will be updated to reset the game later.
	 *
	 * @param source entity which killed the player.
	 */
	@Override
	public void die(Entity source) {
		super.die(source);
		if (!silent)
			view.displayMessage("THE PLAYER IS DEAD. LONG LIVE THE PLAYER");
		currHP = maxHP; // Flavour. TODO: Implement death.
	}

	/**
	 * Registers killing off a target. If this target is an enemy, experience is
	 * awarded based on the xp value of the target.
	 *
	 * @param target target killed off.
	 */
	@Override
	public void registerKill(Entity target) {
		// Check to see if an enemy was killed.
		if (target instanceof Enemy) {
			// If so, grant xp and notify the view.
			Enemy enemy = (Enemy) target;
			if (!silent)
				view.displayMessage("The " + enemy + " was killed.");
			gainXp(enemy.getXPValue());
		}
	}

	// Experience/level based functions.

	/**
	 * Increases the player's current xp score by the amount specified.
	 *
	 * @param amount Amount of xp to be added.
	 */
	public void gainXp(BigInteger amount) {
		// Increase the xp value.
		xp = xp.add(amount);
		// Notify the view.
		if (!silent)
			view.displayMessage("You gain " + amount + "xp.");
		// Check for level up.
		if (xp.compareTo(xpToLevel) != -1)
			levelUp();
	}

	/**
	 * Increases the player's level by 1, updating player attributes by 15-35%
	 * at random. Also resets the player's xp to 0, and increases xpToLevel by
	 * 25%.
	 */
	public void levelUp() {
		// Notify the View.
		if (!silent)
			view.displayMessage("\nYou have reached level "
					+ level.add(BigInteger.ONE) + "!");
		// Increase stats pseudorandomly by 15-35% each.
		for (Attribute stat : attributes.keySet()) {
			// Stat multiplier is between 0.15 and 0.35.
			double multiplier = 0.15 + Math.random() * 0.2;
			// Get the stat value from the map.
			BigDecimal increase = new BigDecimal(attributes.get(stat));
			// Multiply it by the multiplier.
			increase = increase.multiply(BigDecimal.valueOf(multiplier));
			// Add 0.5 to ensure stat rounds correctly.
			increase = increase.add(BigDecimal.valueOf(0.5));
			// Convert to BigInteger.
			BigInteger intIncrease = increase.toBigInteger();
			// Update the view.
			if (!silent)
				view.displayMessage(StringUtils.titleCase(stat.toString())
						+ " was increased by " + intIncrease);
			// Update the map.
			attributes.put(stat, attributes.get(stat).add(intIncrease));
		}

		// Update health, mana and xp to level.
		updateHealthAndMana();
		xpToLevel = xpToLevel.add(xpToLevel.divide(BigInteger.valueOf(4)));

		// Done, set level as increased.
		level = level.add(BigInteger.ONE);
		xp = BigInteger.ZERO;

		// Seperate the level up text from standard text with a line break.
		if (!silent)
			view.displayMessage("");
	}

	/*
	 * Sets current and maximum HP to 10x constitution, and current and maximum
	 * mana to 10x Wisdom
	 */
	private void updateHealthAndMana() {
		maxHP = attributes.get(Attribute.CONSTITUTION).multiply(BigInteger.TEN);
		currHP = maxHP;
		maxMana = attributes.get(Attribute.WISDOM).multiply(BigInteger.TEN);
		currMana = maxMana;
	}

	// Accessor functions.

	/**
	 * @return The player class of the player.
	 */
	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	/**
	 * Gets the current value of the specified attribute.
	 *
	 * @param att attribute to be got.
	 * @return the attribute value of the specified attribute.
	 */
	public BigInteger getAttributeValue(Attribute att) {
		return attributes.get(att);
	}

	/**
	 * @return the attack damage of the player. This is equal to twice their
	 *         Strength, plus any modifiers currently active.
	 */
	@Override
	public BigInteger getAttackDamage() {
		return attributes.get(Attribute.STRENGTH).multiply(
				BigInteger.valueOf(2));
	}

	/**
	 * @return the player's current level.
	 */
	public BigInteger getLevel() {
		return level;
	}

	/**
	 * @return the current amount of xp gained since the previous level.
	 */
	public BigInteger getCurrentXP() {
		return xp;
	}

	/**
	 * @return the total xp needed to level up from 0 xp.
	 */
	public BigInteger getXPToNextLevel() {
		return xpToLevel;
	}

	/**
	 * @return the image used to represent the player.
	 */
	public MiscImage getImage() {
		return image;
	}

	/**
	 * @param image the image to be used to represent the player.
	 */
	public void setImage(MiscImage image) {
		this.image = image;
	}

}
