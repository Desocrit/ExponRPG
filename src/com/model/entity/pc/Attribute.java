package com.model.entity.pc;

/**
 * An enumeration of attributes that affect the player.
 *
 * @author Christopher
 *
 */
public enum Attribute {
	/**
	 * Strength affects attack damage.
	 */
	STRENGTH("STR"),
	/**
	 * Intelligence affects spell damage.
	 */
	INTELLIGENCE("INT"),
	/**
	 * Wisdom affects maximum mana.
	 */
	WISDOM("WIS"),
	/**
	 * Constitution affects maximum HP.
	 */
	CONSTITUTION("CON");

	private String shortening;

	private Attribute(String shortening) {
		this.shortening = shortening;
	}

	/**
	 * @return the shortened name of the attribute.
	 */
	public String getShortening() {
		return shortening;
	}
}
