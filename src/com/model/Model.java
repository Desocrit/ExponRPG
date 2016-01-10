package com.model;

import java.util.ArrayList;
import java.util.List;

import com.model.entity.npc.Enemy;
import com.model.entity.pc.Player;
import com.model.entity.pc.PlayerClass;
import com.model.layout.Direction;
import com.model.layout.FloorLayout;
import com.utils.StringUtils;
import com.view.GUI;
import com.view.images.BackgroundImage;
import com.view.images.MiscImage;

/**
 * The complete game model, containing the locations of all enemies and objects,
 * as well as the current statistics of the player, as well as the room layout.
 * This is automatically generated
 *
 * @author Christopher
 *
 */
public class Model {

	/* The associated view to be outputted to. */
	private GUI view;

	/* The layout of the current floor. */
	private FloorLayout layout;

	/* A reference to the current player of the game. */
	private Player player;

	/* Enemies that the player is currently in combat with. */
	private List<Enemy> currentEnemies;

	// Constructors

	/**
	 * Constructs a fresh game model, with no data yet stored.
	 */
	public Model() {
		currentEnemies = new ArrayList<Enemy>();
	}

	// Controls setup.

	/**
	 * Sets up the related View with the appropriate options for the player to
	 * select a class. This will construct a button for each currently available
	 * class, and provide some dialogue.
	 */
	public void showCharacterSelection() {
		if (view == null)
			throw new IllegalStateException(
					"A view must be provided in order to initiate player class selection.");
		view.displayMessage("Select a class.");
		// Create an array of classes.
		int numClasses = PlayerClass.values().length;
		String[] classes = new String[numClasses];
		for (int i = 0; i < numClasses; i++)
			classes[i] = StringUtils.titleCase(PlayerClass.values()[i].name());
		// Display images.
		view.setBackgroundImage(BackgroundImage.FIELD);
		view.displayImages(MiscImage.FIGHTER, MiscImage.MAGE, MiscImage.ROGUE);
		// Adjust buttons
		view.setButtons(classes);
		view.setButtonsEnabled(true);
	}

	/**
	 * Sets the view to display controls for the player to engage in combat. If
	 * the view is set to null, does nothing.
	 */
	public void showCombatControls() {
		if (view == null)
			return;
		// Set the view layout to display all relevant panels.
		view.setLayout(GUI.Layout.FULL);
		// Set buttons to standard directional options.
		view.setButtons("Attack", "Flee");
	}

	/**
	 * Sets the view to display controls for the player to move between
	 * available rooms. If the view is set to null, does nothing.
	 */
	public void showMovementControls() {
		if (view == null)
			return;
		// Set the view layout to display all relevant panels.
		view.setLayout(GUI.Layout.FULL);
		// Set buttons to standard directional options.
		view.setButtons("North", "West", "Search", "East", "South");
		// Disable any directions that would lead to a wall.
		List<String> walls = new ArrayList<String>();
		for (Direction dir : Direction.values())
			if (!layout.roomExists(dir))
				walls.add(StringUtils.titleCase(dir.toString()));
		for (String dir : walls)
			view.setButtonEnabled(dir, false);
		// Search button is disabled if room has already been searched.
		view.setButtonEnabled("Search", !layout.getRoom().hasBeenSearched());
	}

	// Accessors.

	/**
	 * Sets the current active player.
	 *
	 * @param player player object to be set at the current active player.
	 */
	public void setPlayer(Player player) {
		this.player = player;

		if (view != null) {
			view.displayMessage("Player class is now "
					+ StringUtils.titleCase(player.getPlayerClass().toString()));

			// Inform interested parties. TODO: Remove circular reference.
			view.setPlayer(player);
			player.setView(view);
		}
	}

	/**
	 * @param layout the layout for the currently active floor.
	 */
	public void setLayout(FloorLayout layout) {
		this.layout = layout;

		if (view != null) {
			// Inform interested parties. TODO: Remove circular reference.
			view.setFloorLayout(layout);
			layout.setView(view);
		}
	}

	/**
	 * Causes the model, and all sub-elements of the model, to begin sending
	 * messages and notifications to the specified view object.
	 *
	 * @param view GUI to be sent notifications.
	 */
	public void setView(GUI view) {
		this.view = view;
		if (player != null)
			player.setView(view);
		if (currentEnemies != null)
			for (Enemy enemy : currentEnemies)
				enemy.setView(view);
	}

}
