package com.controller;

/**
 * The current state of the game. This determines actions currently available,
 * and the result of certain button presses.
 *
 * @author Christopher
 *
 */
enum GameState {
	/**
	 * The game controller is currently inactive.
	 */
	INACTIVE,
	/**
	 * The player is creating a new character, and selecting which class to use.
	 */
	SELECTING_CLASS,
	/**
	 * The player is moving around the game map.
	 */
	EXPLORING,
	/**
	 * The player is in combat.
	 */
	COMBAT
}
