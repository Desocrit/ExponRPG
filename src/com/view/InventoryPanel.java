package com.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * The inventory panel contains a series of squares, which may be filled with
 * icons representing items. These can be dragged to the equipment panel to
 * equip them, or potentially used in combat.
 *
 * @author Christopher
 *
 */
public class InventoryPanel extends JPanel {

	private static final long serialVersionUID = 7894473990746928570L;

	/**
	 * Constructs a new InventoryPanel with an empty inventory displayed.
	 */
	public InventoryPanel() {
		setPreferredSize(new Dimension());
		setBackground(Color.GRAY);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Create a Graphics2D object.
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaintMode();
		g2.setRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		// Store window height.
		int w = getWidth() - 2;
		int h = getHeight() - 2;
		// Set up the stroke.
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.BLACK);
		// Draw the boxes.
		for (int x = 0; x < 4; x++)
			for (int y = 0; y < 3; y++)
				g2.draw(new Rectangle2D.Double((w / 4) * x + 1,
						(h / 3) * y + 1, w / 4, h / 3));

	}

}
