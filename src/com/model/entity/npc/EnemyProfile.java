package com.model.entity.npc;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A profile for a generated enemy, with statistics, an ability list etc.
 *
 * This is effectively just an intermediary to keep the Enemy class clean.
 *
 * @author Christopher
 *
 */
public class EnemyProfile {

	/**
	 * Enemy name, as displayed to the player.
	 */
	public String name;
	/**
	 * The amount of xp gained from slaying this foe.
	 */
	public BigInteger xpValue;
	/**
	 * Enemy's attack damage dealt per hit.
	 */
	public BigInteger attackDamage;

	/**
	 * Enemy initial health percentage as a decimal. Default is 1.0 (100%).
	 */
	public double initialHPPct = 1.0;
	/**
	 * Enemy's maximum health value.
	 */
	public BigInteger maxHP;
	/**
	 * Enemy initial mana percentage as a decimal. Default is 1.0 (100%).
	 */
	public double initialManaPct = 1.0;
	/**
	 * Enemy's maximum mana value.
	 */
	public BigInteger maxMana;

	/**
	 * Calculates the initial mana value based on the mana percentage stored.
	 *
	 * @return the initial mana value as BigInteger
	 */
	public BigInteger calculateInitialMana() {
		// Convert to BigDecimal, multiply, and convert back.
		BigDecimal max = new BigDecimal(maxMana);
		max = max.multiply(new BigDecimal(initialManaPct));
		return max.toBigInteger();
	}

	/**
	 * Calculates the initial health value based on the mana percentage stored.
	 *
	 * @return the initial health value as BigInteger
	 */
	public BigInteger calculateInitialHP() {
		// As above, so below.
		BigDecimal max = new BigDecimal(maxHP);
		max = max.multiply(new BigDecimal(initialHPPct));
		return max.toBigInteger();
	}

}
