package com.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.model.layout.Direction;
import com.model.layout.FloorLayout;
import com.model.layout.Room;

/**
 * The map panel, which contains a map demonstrating the player's current
 * location, as well as the layout of discovered rooms, and whether rooms have
 * been searched, as well as special room layouts.
 *
 * @author Christopher
 *
 */
public class MapPanel extends JPanel {

	private static final long serialVersionUID = 7263337683976203060L;

	/* Floor layout to be displayed. */
	private FloorLayout layout;

	/**
	 * Constructs a map panel that displays the placeholder image until a floor
	 * layout to be displayed is specified.
	 */
	public MapPanel() {
		this(null);
	}

	/**
	 * Constructs a map panel that displays the specified floor layout.
	 *
	 * @param layout layout to be displayed.
	 */
	public MapPanel(FloorLayout layout) {
		this.layout = layout;
		setPreferredSize(new Dimension());
		setBackground(Color.WHITE);
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

		// Set up dimensions
		float w = getWidth();
		float h = getHeight();
		// setPreferredSize(new Dimension((int) w, (int) w));

		if (layout == null) {
			g2.setStroke(new BasicStroke(h / 20));
			// Main arc at the top.
			g2.draw(new Arc2D.Float((float) (w * 0.35), (float) (h * 0.3),
					(float) (w * 0.3), (float) (h * 0.2), 270, 270, Arc2D.OPEN));
			// Lower line
			g2.draw(new Line2D.Float(w / 2, h / 2, w / 2, (float) (h * 0.6)));
			// Dot
			g2.draw(new Line2D.Float(w / 2, (float) (h * 0.75), w / 2,
					(float) (h * 0.75)));
		} else {
			int rooms = layout.getFloorNumber() + 3;
			double rw = getWidth() / (rooms + 2); // Room width
			double rh = getHeight() / (rooms + 2); // Room height.

			// Create a buffered image for foreground objects.
			BufferedImage fg = new BufferedImage((int) w, (int) h,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D fgg2 = fg.createGraphics();

			for (int i = 0; i < rooms; i++)
				for (int j = 0; j < rooms; j++) {
					Dimension roomLoc = new Dimension(i, rooms - j - 1);
					Room room = layout.getRoom(roomLoc);
					if (room != null && room.hasBeenDiscovered()) {
						// Prepare some values.
						double left = rw * (i + 1);
						double top = rh * (j + 1);

						if (room.hasBeenEntered()) {
							// Draw the outline of the room.
							g2.setColor(Color.BLACK);
							g2.draw(new Rectangle2D.Double(left, top, rw, rh));
						}
						// Determine fill colour based on room characteristics.

						if (room.hasBeenEntered())
							if (room.getEnemies().size() != 0)
								g2.setColor(new Color(255, 100, 100));
							else if (room.hasBeenSearched())
								g2.setColor(new Color(200, 255, 150));
							else
								g2.setColor(Color.WHITE);
						else
							g2.setColor(Color.LIGHT_GRAY);

						// Fill the outline in.
						g2.fill(new Rectangle2D.Double(left + 1, top + 1,
								rw - 1, rh - 1));

						// Fix the doors
						if (layout.roomEntered(Direction.WEST, roomLoc))
							// Set colour based on whether door is locked.
							if (layout.doorLocked(Direction.WEST, roomLoc)) {
								fgg2.setColor(Color.BLACK);
								fgg2.fill(new Rectangle2D.Double(left - 1, top
										+ rh * 0.35, 3, rh * 0.3));
							} else {
								fgg2.setColor(Color.WHITE);
								fgg2.fill(new Rectangle2D.Double(left, top + rh
										* 0.35, 1, rh * 0.3));
							}

						if (layout.roomEntered(Direction.SOUTH, roomLoc))
							if (layout.doorLocked(Direction.WEST, roomLoc)) {
								fgg2.setColor(Color.BLACK);
								fgg2.fill(new Rectangle2D.Double(left + rw
										* 0.35, top + rh, rw * 0.3, 3));
							} else {
								fgg2.setColor(Color.WHITE);
								fgg2.fill(new Rectangle2D.Double(left + rw
										* 0.35, top + rh, rw * 0.3, 1));
							}

						// Add various icons.

						// X marks the player.
						if (layout.getPlayerLocation().width == i
								&& layout.getPlayerLocation().height == rooms
								- j - 1) {
							fgg2.setColor(Color.BLACK);
							fgg2.setStroke(new BasicStroke(3));
							// Draw an X.
							fgg2.draw(new Line2D.Double(left + rw * 0.2, top
									+ rh * 0.2, left + rw * 0.8, top + rh * 0.8));
							fgg2.draw(new Line2D.Double(left + rw * 0.2, top
									+ rh * 0.8, left + rw * 0.8, top + rh * 0.2));
						}
					}
				}
			g2.drawImage(fg, 0, 0, this);
		}
	}

	/**
	 * Sets the displayed floor layout to the specified FloorLayout object.
	 *
	 * @param layout layout to be displayed.
	 */
	public void setFloorLayout(FloorLayout layout) {
		this.layout = layout;
		repaint();
	}
}
