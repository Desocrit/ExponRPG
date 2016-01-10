package com.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.model.entity.GameCharacter;
import com.view.images.BackgroundImage;
import com.view.images.DisplayableImage;

/**
 * The main panel of the interface, containing an image of the current area,
 * possibly with certain hints or enemy status bars. This covers the majority of
 * the screen, and is intended to provide scenery to the player.
 *
 * @author Christopher
 *
 */
public class MainPanel extends JPanel {

	private static final long serialVersionUID = 348666572757602850L;

	/* Background image to be displayed */
	private BufferedImage background;
	/* Characters to be displayed */
	private GameCharacter[] characters;
	/* Images to be displayed */
	private BufferedImage[] images;

	// Core methods.

	/**
	 * Constructs a new blank mainPanel.
	 */
	public MainPanel() {
		characters = new GameCharacter[5];
		images = new BufferedImage[5];
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw the background.
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		// Enumerate foreground images. TODO: Arrange from center outwards.
		List<BufferedImage> displayedImages = new ArrayList<BufferedImage>();
		for (BufferedImage image : images)
			if (image != null)
				displayedImages.add(image);
		// Display images evenly spaced.
		for (int i = 0; i < displayedImages.size(); i++)
			g.drawImage(
					displayedImages.get(i),
					(int) (getWidth() * ((double) ((i * 2) + 1) / (displayedImages
							.size() * 2 + 1))), (int) (getHeight() * 0.2),
							(int) (getWidth() * 0.2), (int) (getHeight() * 0.6), this);
	}

	// Foreground related.

	/**
	 * Clears all images and characters from the foreground of the panel.
	 */
	public void clearForeground() {
		for (int i = 0; i < 5; i++) {
			images[i] = null;
			characters[i] = null;
		}
		repaint();
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
		int displayedChars = 0;
		for (int i = 0; i < 5 && displayedChars < character.length; i++)
			if (characters[i] == null)
				characters[i] = character[displayedChars++];
		repaint();
		return (displayedChars == character.length);
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
		int placedImages = 0;
		for (int i = 0; i < 5 && placedImages < image.length; i++)
			if (images[i] == null)
				try {
					images[i] = image[placedImages++].getImage();
				} catch (IOException e) {
					// Image is skipped.
					placedImages++;
					i--;
				}
		repaint();
		return (placedImages == image.length);
	}

	// Background related.

	/**
	 * Sets the displayed background image to the specified BackgroundImage
	 * enumerated value.
	 *
	 * @param image background to be displayed.
	 */
	public void setBackgroundImage(BackgroundImage image) {
		try {
			this.background = image.getImage();
			repaint();
		} catch (IOException e) {
			// Don't change background.
		}
	}
}
