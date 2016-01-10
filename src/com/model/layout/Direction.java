package com.model.layout;

import java.awt.Dimension;

/**
 * A compass direction.
 *
 * @author Christopher
 *
 */
public enum Direction {
	/** North or Up */
	NORTH(0, 1),
	/** East or Right */
	EAST(1, 0),
	/** South or Down */
	SOUTH(0, -1),
	/** West or Left */
	WEST(-1, 0);

	private Dimension offset;

	/**
	 *
	 */
	private Direction(int xDiff, int yDiff) {
		offset = new Dimension(xDiff, yDiff);
	}

	/**
	 * @return a dimension representing the change in position caused by
	 *         travelling one cell in this direction.
	 */
	public Dimension getOffset() {
		return offset;
	}
}
