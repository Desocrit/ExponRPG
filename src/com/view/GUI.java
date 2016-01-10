package com.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.controller.GameController;
import com.model.entity.GameCharacter;
import com.model.entity.pc.Player;
import com.model.layout.FloorLayout;
import com.view.images.BackgroundImage;
import com.view.images.DisplayableImage;

/**
 * Facade class to handle the main interface functions and control the other GUI
 * elements. All commands to the GUI should go through this facade. This handles
 * the main frames, scene switching, and all interface events that occur.
 *
 * @author Christopher
 *
 */
public class GUI implements Runnable {

	/**
	 * The layout of the GUI, governing the positions of each panel, as well as
	 * which panels are visible.
	 *
	 * @author Christopher
	 *
	 */
	public enum Layout {
		/**
		 * Only the main pannel and button panels are visible.
		 */
		SIMPLE,
		/**
		 * All panels are displayed.
		 */
		FULL
	}

	// The associated GameController, to be informed of button presses.
	private GameController controller;

	// The current layout to be used.
	private Layout layout;

	// The frame that contains the interface.
	private JFrame frame;
	// The background JPanel containing all other panels.
	private JPanel bgPanel;
	// GridBagConstraints used for the panels. Stored for ease of reuse.
	private GridBagConstraints gc;

	// The upper left panel, which contains an image.
	private MainPanel mainPanel;
	// The central left panel, which contains the console.
	private ConsolePanel consolePanel;
	// The lower left panel, which contains the buttons.
	private ButtonPanel buttonPanel;

	// The upper right panel, which contains the map.
	private MapPanel mapPanel;
	// The upper central right hand panel, which displays the inventory.
	private InventoryPanel inventoryPanel;
	// The lower central right hand panel, which displays equipment.
	private EquipmentPanel equipmentPanel;
	// The lower right panel, showing player stats and attributes.
	private StatsPanel statsPanel;

	/**
	 * Constructs and initialises the main game window. This functionality may
	 * be changed at a later date.
	 *
	 * @param controller GameController to control events occurring on the GUI.
	 */
	public GUI(GameController controller) {
		this.controller = controller;
		// Set the GUI up.
		run();
		// TODO: This should be used frim controller once everything is up and
		// running. For the moment, run can be used directly.
		// SwingUtilities.invokeLater(this);
	}

	/**
	 * Initialises the UI, setting up all of the frames and sub panels. Should
	 * only be called internally by the constructor. The access level is only
	 * for polymorphism reasons.
	 */
	@Override
	public void run() {
		// Set up the frame.
		frame = new JFrame("exponRPG");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setMinimumSize(new Dimension(720, 600));
		frame.setLocation(0, 0);

		// Create a main panel to hold the sub panels.
		// Card layout is used to easily switch interface formats.
		bgPanel = new JPanel(new GridBagLayout());
		bgPanel.setPreferredSize(new Dimension(1280, 960));
		frame.add(bgPanel);

		// Create the panels.
		mainPanel = new MainPanel();
		consolePanel = new ConsolePanel();
		buttonPanel = new ButtonPanel(controller);
		mapPanel = new MapPanel();
		equipmentPanel = new EquipmentPanel();
		inventoryPanel = new InventoryPanel();
		statsPanel = new StatsPanel();

		// Set up the constraints with universal values.
		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;

		// Set the layout.
		setLayout(Layout.SIMPLE);
		frame.setVisible(true);
	}

	// ButtonPanel functions.

	/**
	 * Sets the displayed buttons to be the passed array of labels. Labels will
	 * be automatically positioned based on the length of the passed array. This
	 * will be the simplest way of organising the buttons with the grid width
	 * equal to or greater than the height, except in the case of a 5-length
	 * array (in which case a plus shape will be used) or a 7 length array,
	 * which will be similar to the 6-length but with a single triple-length
	 * button below. Action listeners will be generated to pass a message with
	 * the button name to the button press controller.
	 *
	 * @param labels labels for the buttons to be used.
	 */
	public void setButtons(String... labels) {
		buttonPanel.setButtons(labels);
	}

	/**
	 * Enables or disabled all of the buttons on the panel.
	 *
	 * @param enabled whether buttons should be enabled.
	 */
	public void setButtonsEnabled(boolean enabled) {
		buttonPanel.setButtonsEnabled(enabled);
	}

	/**
	 * Enables or disables a button with the specified label.
	 *
	 * @param label button label to be searched for.
	 *
	 * @param enabled whether buttons should be enabled.
	 */
	public void setButtonEnabled(String label, boolean enabled) {
		buttonPanel.setButtonEnabled(label, enabled);
	}

	// Console Panel functions.

	/**
	 * Displays the requested message on a new line in the console panel. A
	 * newline character is automatically added to the end of the line.
	 *
	 * @param message message to be displayed
	 */
	public void displayMessage(String message) {
		consolePanel.displayMessage(message);
		update();
	}

	/**
	 * Clears the text from the text area in the console panel.
	 */
	public void clear() {
		consolePanel.clear();
	}

	// MapPanel functions.

	/**
	 * Sets the floor layout displayed on the map to the specified FloorLayout
	 * object. If null is passed, this will display an empty map with a question
	 * mark instead.
	 *
	 * @param layout floor layout to be set.
	 */
	public void setFloorLayout(FloorLayout layout) {
		mapPanel.setFloorLayout(layout);
	}

	/**
	 * Sets the player whose stats should be displayed on the UI. If null is
	 * passed, this will display an indicator for unknown stats.
	 *
	 * @param player player whose stats should be displayed.
	 */
	public void setPlayer(Player player) {
		statsPanel.setPlayer(player);
		equipmentPanel.setPlayer(player);
	}

	// MainPanel functions.

	/**
	 * Sets the displayed background image to the specified BackgroundImage
	 * enumerated value.
	 *
	 * @param image background to be displayed.
	 */
	public void setBackgroundImage(BackgroundImage image) {
		mainPanel.setBackgroundImage(image);
	}

	/**
	 * Clears all images and characters from the foreground of the central
	 * panel.
	 */
	public void clearForeground() {
		mainPanel.clearForeground();
	}

	/**
	 * Queues the passed images to be displayed on the screen. The related
	 * image, health bars and status bars will all be displayed. These will be
	 * displayed before any other images, up to a maximum of five characters. If
	 * more than five characters are queued, the first five will be displayed,
	 * the remainders will be lost and this function will return false.
	 *
	 * @param character characters to be displayed.
	 * @return true if all characters can be displayed, or else false.
	 */
	public boolean displayCharacters(GameCharacter... character) {
		return mainPanel.displayCharacters(character);
	}

	/**
	 * Queues the passed images to be displayed on the screen. These will be
	 * displayed after the GameCharacters, up to a maximum of five images. If
	 * more than five images are queued, the first five will be displayed, the
	 * remainders will be lost and this function will return false.
	 *
	 * @param image images to be displayed.
	 * @return true if all images can be displayed, or else false.
	 */
	public boolean displayImages(DisplayableImage... image) {
		return mainPanel.displayImages(image);
	}

	// Global functions

	/**
	 * Sets the UI layout to the specified Layout enum.
	 *
	 * @param layout layout to be used.
	 */
	public void setLayout(Layout layout) {
		// Only change layout if needed.
		if (this.layout == layout)
			return;
		this.layout = layout;

		// Begin shifting buttons around.
		bgPanel.removeAll();
		// Set constraints for the panels and add them to the frame.
		switch (layout) {
		case FULL:
			addPanel(mainPanel, 0, 0, 2, 2, 0.77, 0.65);
			addPanel(consolePanel, 0, 2, 1, 2, 0.77, 0.2);
			addPanel(buttonPanel, 0, 3, 1, 2, 0.77, 0.15);
			addPanel(mapPanel, 2, 0, 1, 1, 0.23, 0.15);
			addPanel(equipmentPanel, 2, 1, 1, 1, 0.23, 0.35);
			addPanel(inventoryPanel, 2, 2, 1, 1, 0.23, 0.2);
			addPanel(statsPanel, 2, 3, 1, 1, 0.23, 0.15);
			break;
		case SIMPLE:
			addPanel(mainPanel, 0, 0, 2, 3, 1., 0.8);
			addPanel(buttonPanel, 0, 3, 1, 3, 1., 0.2);
			break;
		default:
			break;

		}
		frame.pack();
	}

	// Adds a panel with specified GridBagComponent values.
	private void addPanel(Component panel, int gridx, int gridy,
			int gridheight, int gridwidth, double weightx, double weighty) {
		gc.gridx = gridx;
		gc.gridy = gridy;
		gc.gridheight = gridheight;
		gc.gridwidth = gridwidth;
		gc.weightx = weightx;
		gc.weighty = weighty;
		bgPanel.add(panel, gc);
	}

	/**
	 * Updates all panels on the interface to accurately represent the model.
	 */
	public void update() {
		if (layout == Layout.SIMPLE)
			return;
		// TODO: Find a better way to do this.
		mapPanel.repaint();
		statsPanel.repaint();
		inventoryPanel.repaint();
		equipmentPanel.repaint();
	}
}
