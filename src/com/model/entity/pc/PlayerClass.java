package com.model.entity.pc;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration of classes available to the class. Also includes a
 * getBaseAttributes function that will return a map of the base attributes for
 * that class. Future versions should include more comprehensive levelling
 * systems.
 *
 * @author Christopher
 *
 */
public enum PlayerClass {

	/**
	 * The fighter class has high attack damage and health.
	 */
	FIGHTER(15, 10, 10, 12),

	/**
	 * The mage class has high spell damage and mana.
	 */
	MAGE(10, 12, 15, 10),

	/**
	 * The rogue has strong all-round stats but no major speciality.
	 */
	ROGUE(12, 12, 12, 12);

	private Map<Attribute, Integer> baseAttributes;

	private PlayerClass(int strength, int intellect, int wisdom,
			int constitution) {
		baseAttributes = new HashMap<Attribute, Integer>();
		baseAttributes.put(Attribute.STRENGTH, strength);
		baseAttributes.put(Attribute.INTELLIGENCE, intellect);
		baseAttributes.put(Attribute.WISDOM, wisdom);
		baseAttributes.put(Attribute.CONSTITUTION, constitution);
	}

	/**
	 * Gets a map of the base attributes of the chosen class.
	 *
	 * @return A map of Attribute to Integer for each of the base Attributes.
	 */
	public Map<Attribute, Integer> getBaseAttributes() {
		return baseAttributes;
	}

}
