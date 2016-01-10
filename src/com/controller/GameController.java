package com.controller;

import java.awt.Dimension;
import java.util.List;

import com.model.Model;
import com.model.entity.GameCharacter;
import com.model.entity.npc.Enemy;
import com.model.entity.pc.Player;
import com.model.entity.pc.PlayerClass;
import com.model.layout.Direction;
import com.model.layout.FloorLayout;
import com.view.GUI;
import com.view.images.MiscImage;

/**
 * Controls the main elements of the game itself (i.e. starting the game, and
 * activating the other controllers to construct and play the game itself. Also
 * contains the main method.
 *
 * @author Christopher
 *
 */
public class GameController {

	// The game model used
	private Model model;

	// The current state of the game (i.e. what the player is currently doing).
	private GameState state;

	// The currently active player.
	private Player player;
	// The layout of the current floor.
	private FloorLayout layout;

	/**
	 * Constructs a new GameController, generating the necessary listeners and
	 * setting up initial values for the game to function.
	 *
	 * @param model the model to be manipulated.
	 */
	public GameController(Model model) {
		this.model = model;
		state = GameState.INACTIVE;
	}

	/**
	 * Starts a new game, creating a fresh player entity and dungeon, and
	 * setting all stats to their base value.
	 */
	public void startNewGame() {
		model.showCharacterSelection();
		state = GameState.SELECTING_CLASS;
	}

	/**
	 * Handles the player pressing a button with a speficied label. This will
	 * cause actions to occur within the game, if the game is currently
	 * expecting input.
	 *
	 * @param buttonLabel label of the pressed button.
	 */
	public void acceptButtonPress(String buttonLabel) {
		switch (state) {
		case SELECTING_CLASS:
			setPlayerClass(buttonLabel);
			break;
		case EXPLORING:
			// Handle searching the room.
			if (buttonLabel == "Search")
				layout.getRoom().search();
			else
				try {
					Direction dir = Direction
							.valueOf(buttonLabel.toUpperCase());
					layout.movePlayer(dir);
				} catch (IllegalArgumentException e) {
					return;
				}
			if (layout.inCombat()) {
				state = GameState.COMBAT;
				model.showCombatControls();
			} else
				model.showMovementControls(); // An update may be needed.
			break;
		case COMBAT:
			if (buttonLabel == "Flee") {
				for (Enemy enemy : layout.getRoom().getEnemies())
					enemy.attack(player);
				model.showMovementControls();
				state = GameState.EXPLORING;
			}
			// Get updated current list of enemies.
			List<Enemy> currentEnemies = layout.getRoom().getEnemies();
			// Interpret attack command.
			// if (currentEnemies.size() == 1) // TODO: Specific targeting.
			interpretAttack(buttonLabel, currentEnemies.get(0));
			break;
		default:
		case INACTIVE:
			break;

		}
	}

	// Button input subfunctions.

	/*
	 * Sets the player class to the given class name. Does nothing if the given
	 * class name is invalid.
	 * 
	 * @param className
	 */
	private void setPlayerClass(String className) {
		try {
			// Determine player class, and construct player.
			PlayerClass pc = PlayerClass.valueOf(className.toUpperCase());
			player = new Player(pc);
			player.setImage(MiscImage.valueOf(className.toUpperCase()));
		} catch (IllegalArgumentException e) {
			// Player class invalid. Failed.
			return;
		}
		// Also create a new floor layout (this may be refactored later).
		layout = new FloorLayout(1, new Dimension(2, 0));
		// Update the model.
		model.setPlayer(player);
		model.setLayout(layout);
		// Update state. TODO: Change this to Exploring after layout is done.
		model.showCombatControls();
		state = GameState.COMBAT;
	}

	private void interpretAttack(String attack, GameCharacter target) {
		if (attack == "Attack")
			player.attack(target);
		// Get updated current enemies list.
		List<Enemy> currentEnemies = layout.getRoom().getEnemies();
		// Check for target death.
		if (currentEnemies.isEmpty()) {
			model.showMovementControls();
			state = GameState.EXPLORING;
			return;
		}
		// Give the target a chance to hit back.
		for (Enemy enemy : currentEnemies)
			enemy.attack(player);
	}

	/**
	 * @param vagina Arguments to be passed to the main method.
	 */
	public static void main(String[] vagina) {
		Model model = new Model();
		GameController controller = new GameController(model);
		GUI mainGUI = new GUI(controller);
		model.setView(mainGUI);

		controller.startNewGame();

	}

}