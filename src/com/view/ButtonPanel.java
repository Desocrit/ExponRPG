package com.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.controller.GameController;

/**
 * This panel contains all of the buttons that can be pressed on any of the
 * screen. The button layout and labels can be customised using the setButtons
 * method, which takes labels for the buttons and will automatically lay out the
 * buttons. Details of the exact layout can be found in the comment for that
 * method.
 *
 * @see #setButtons(String... labels)
 *
 * @author Christopher
 *
 */
public class ButtonPanel extends JPanel {

	private static final long serialVersionUID = 7367672605082114217L;

	// Actual active panel. This class serves as a facade, to allow padding.
	private JPanel buttonPanel;

	// A List of JButtons, for easy reuse.
	private List<JButton> buttons;

	// The listener that listens for button presses.
	private ActionListener listener;

	/*
	 * Constructs a new button panel. This should only be used by the GUI class.
	 */
	protected ButtonPanel(GameController controller) {
		super();

		// Set up the padding.
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(new Color(100, 100, 100));

		// Set up the actual active panel.

		buttonPanel = new JPanel();

		// Avoid overriding grid bag constants.
		buttonPanel.setPreferredSize(new Dimension());
		buttonPanel.setBackground(new Color(100, 100, 100));

		// Create a listener.
		listener = e -> controller.acceptButtonPress(e.getActionCommand());
		// Setup layout.
		buttonPanel.setLayout(new GridBagLayout());
		buttons = new ArrayList<JButton>();
		setButtons("North", "West", "Search", "East", "South");

		add(buttonPanel);

	}

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
		// Clear the current buttons. Garbage collection will clean up.
		buttonPanel.removeAll();
		buttons.clear();

		// Set up the basic constraints.
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		// TODO: improve padding.
		gc.insets = new Insets(5, 5, 5, 5);

		gc.weightx = 1;
		gc.weighty = 1;

		// Determine the grid width to use. Height is automated.
		int width = 3;
		if (labels.length == 8)
			width = 4;
		else if (labels.length == 2 || labels.length == 4)
			width = 2;

		// Generate and place the buttons.
		for (int i = 0; buttons.size() < labels.length; i++) {
			// 5 buttons uses a directional layout.
			if (labels.length == 5 && i / width != 1 && i % width != 1)
				continue;
			// Button 7 is stretched in size-7 layouts.
			if (labels.length == 7 && buttons.size() == 6)
				gc.gridwidth = 3;

			// Create the button.
			JButton button = new JButton(labels[buttons.size()]);
			button.addActionListener(listener);
			buttons.add(button);

			// Add it to the grid.
			gc.gridy = i / width;
			gc.gridx = i % width;
			buttonPanel.add(button, gc);
		}
		buttonPanel.revalidate();
		buttonPanel.repaint();
	}

	/**
	 * Enables or disables all of the buttons on the panel.
	 *
	 * @param enabled whether buttons should be enabled.
	 */
	public void setButtonsEnabled(boolean enabled) {
		for (JButton button : buttons)
			button.setEnabled(enabled);
	}

	/**
	 * Enables or disables a button with the specified label.
	 *
	 * @param label button label to be searched for.
	 *
	 * @param enabled whether buttons should be enabled.
	 */
	public void setButtonEnabled(String label, boolean enabled) {
		for (JButton button : buttons)
			if (button.getText().toLowerCase().equals(label.toLowerCase()))
				button.setEnabled(enabled);
	}
}
