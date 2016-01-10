package com.model.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.view.GUI;

/**
 * Any character in the game, including the player. This stores their health and
 * mana values, and tracks their death and kills.
 *
 * @author Christopher
 *
 */

public abstract class GameCharacter extends Entity {

	/* Current amount of HP. */
	protected BigInteger currHP;
	/* Maximum HP. */
	protected BigInteger maxHP;
	/* Current amount of mana. */
	protected BigInteger currMana;
	/* Maximum mana count */
	protected BigInteger maxMana;

	/* Damage dealt per attack. */
	private BigInteger attackDamage;

	/**
	 * Creates a character. This only sets their name, and will throw errors if
	 * used before setting current and max HP. Notifications will not be sent.
	 *
	 * @param name name of the character.
	 * @param view the GUI to send messages to.
	 */
	protected GameCharacter(String name) {
		super(name);
	}

	/**
	 * Creates a character. This only sets their name, and will throw errors if
	 * used before setting current and max HP. Notifications will be sent to the
	 * specified View object.
	 *
	 * @param name name of the character.
	 * @param view the GUI to send messages to.
	 */
	protected GameCharacter(String name, GUI view) {
		super(name, view);
	}

	/**
	 * Creates a character, setting their maximum health and mana to the given
	 * values. Current health and mana will also be set to these values. No
	 * notifications will be sent.
	 *
	 * @param name name of the character.
	 * @param maxHP current and maximum health to be set.
	 * @param maxMana current and maximum mana to be set.
	 * @param attackDamage damage dealt per hit.
	 */
	protected GameCharacter(String name, BigInteger maxHP, BigInteger maxMana,
			BigInteger attackDamage) {
		super(name);
		init(maxHP, maxMana, attackDamage);
	}

	/**
	 * Creates a character, setting their maximum health and mana to the given
	 * values. Current health and mana will also be set to these values.
	 * Notifications will be sent to the specificed View object.
	 *
	 * @param name name of the character.
	 * @param view the GUI to send relevant messages to.
	 * @param maxHP current and maximum health to be set.
	 * @param maxMana current and maximum mana to be set.
	 * @param attackDamage damage dealt per hit.
	 */
	protected GameCharacter(String name, GUI view, BigInteger maxHP,
			BigInteger maxMana, BigInteger attackDamage) {
		super(name, view);
		init(maxHP, maxMana, attackDamage);
	}

	private void init(BigInteger maxHP, BigInteger maxMana,
			BigInteger attackDamage) {
		this.maxHP = maxHP;
		this.maxMana = maxMana;
		this.attackDamage = attackDamage;

		currHP = maxHP;
		currMana = maxMana;
	}

	// Actions

	/**
	 * Attacks the target, dealing their AttackDamage on average per hit. This
	 * is modified by 25% random variance either side. The takeDamage method of
	 * the target is then called with this damage as a parameter. Returns the
	 * amount of damage dealt by the attack.
	 *
	 * @param target Target to be attacked.
	 * @return the amount of damage dealt by the attack.
	 */
	public BigInteger attack(GameCharacter target) {
		// Update the view.
		if (!silent)
			view.displayMessage("The " + this + " attacks the " + target + ".");
		// Calculate base damage dealt
		BigDecimal damageDecimal = new BigDecimal(getAttackDamage());
		// Calculate variance (+-25%)
		double modifier = 0.75 + Math.random() / 2;
		// Multiply the two together.
		damageDecimal = damageDecimal.multiply(BigDecimal.valueOf(modifier));
		// Convert back to BigInteger.
		BigInteger damageDealt = damageDecimal.toBigInteger();
		target.takeDamage(damageDealt, this);
		return damageDealt;
	}

	// Health/mana based functions.

	/**
	 * Restores the given amount of health. If this would put the character
	 * above their maxHP, it will instead set them to maxHP.
	 *
	 * @param amount amount of health to restore.
	 * @param source entity that restored the health.
	 */
	public void restoreHealth(BigInteger amount, Entity source) {
		// Make sure the healing does not go above the max HP.
		if (currHP.add(amount).compareTo(maxHP) == 1)
			amount = maxHP.subtract(currHP);
		// Update the view.
		if (!silent)
			view.displayMessage("The " + source + " gains " + amount
					+ " health.");
		// Add the health on to current health.
		currHP = currHP.add(amount);

	}

	/**
	 * Causes the player to take the given amount of damage. If this would kill
	 * the character, the 'die' function is called.
	 *
	 * @param amount amount of damage to take.
	 * @param source entity that caused the damage.
	 */
	public void takeDamage(BigInteger amount, Entity source) {
		// Take the damage.
		currHP = currHP.subtract(amount);
		if (!silent)
			view.displayMessage("The " + this + " takes " + amount + " damage.");
		// Check for death.
		if (currHP.compareTo(BigInteger.ZERO) != 1)
			die(source);
	}

	/**
	 * Restores the given amount of mana. If this would put the character above
	 * their maxMana, it will instead set them to maxMana.
	 *
	 * @param amount amount of mana to restore.
	 * @param source entity that restored the mana.
	 */
	public void restoreMana(BigInteger amount, Entity source) {
		// Make sure the healing does not go above the max HP.
		if (currMana.add(amount).compareTo(maxMana) == 1)
			amount = maxMana.subtract(currMana);
		// Update the view.
		if (!silent)
			view.displayMessage("The " + source + " gains " + amount + " mana.");
		// Add the health on to current health.
		currMana = currMana.add(amount);

	}

	/**
	 * Reduces the amount of mana available to the character by a given amount.
	 * If this mana is unavailable, an exception will be thrown.
	 *
	 * @param amount amount of mana to spend
	 * @param source entity that caused the reduction in mana.
	 *
	 */
	public void spendMana(BigInteger amount, Entity source) {
		// Check that the mana is available.
		if (currMana.compareTo(amount) == -1)
			throw new IllegalArgumentException(toString()
					+ " attempted to spend " + amount + " mana when only "
					+ currMana + " was available.");
		// Update the view.
		if (!silent)
			view.displayMessage("The " + source + " loses " + amount + " mana.");
		// Spend the mana.
		currMana = currMana.subtract(amount);
	}

	// Accessor functions.

	/**
	 * @return Current HP.
	 */
	public BigInteger getHP() {
		return currHP;
	}

	/**
	 * @return maximum HP.
	 */
	public BigInteger getMaxHP() {
		return maxHP;
	}

	/**
	 * @return current Mana.
	 */
	public BigInteger getMana() {
		return currMana;
	}

	/**
	 * @return maximum mana.
	 */
	public BigInteger getMaxMana() {
		return maxMana;
	}

	/**
	 * @return the attackDamage
	 */
	public BigInteger getAttackDamage() {
		return attackDamage;
	}

}
