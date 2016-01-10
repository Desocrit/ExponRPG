package com.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.swing.JPanel;

import com.model.entity.pc.Player;

/**
 * The equipment panel contains a representation of the player's current
 * equipment layout. Items can be equipped by dragging them to equipment slots.
 *
 * @author Christopher
 *
 */
public class EquipmentPanel extends JPanel {

	private static final long serialVersionUID = -6557349610331389379L;

	// The player currently being drawn.
	private Player player;

	/**
	 * Constructs a new, empty, EquipmentPanel. This will display nothing until
	 * the player is specified, and then will display the player's image.
	 */
	public EquipmentPanel() {
		this(null);
	}

	/**
	 * Constructs a equipment panel displaying the specified player..
	 *
	 * @param player the player to have the image of displayed.
	 */
	public EquipmentPanel(Player player) {
		// Update frame.
		setPreferredSize(new Dimension());
		setBackground(Color.BLACK);
		// Update variables.
		this.player = player;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (player == null)
			return;
		// Get the Graphics2D object.
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
		// Prepare a few variables;
		int w = getWidth();
		int h = getHeight();
		// Draw the player image.
		try {
			g2.drawImage(player.getImage().getImage(), (int) (w * 0.2),
					(int) (h * 0.1), (int) (w * 0.6), (int) (h * 0.8), this);
		} catch (IOException e) {
			return;
		}
	}

	/**
	 * Sets which player has their details displayed.
	 *
	 * @param player player to be displayed.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}
