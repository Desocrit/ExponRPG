package com.view.images;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An enum to store specific background images. Uses lazy initialisation to
 * automatically get relevant images from pre-specified file names. Will
 * generate images randomly if multiple relevant images exist.
 *
 * @author Christopher
 *
 */
public enum MiscImage implements DisplayableImage {
	/**
	 * A picture of a chivalrous knight.
	 */
	FIGHTER("img/Knight.png"),
	/**
	 * A picture of a classic fantasy wizard.
	 */
	MAGE("img/Wizard.png"),
	/**
	 * A picture of an arabian-style rogue.
	 */
	ROGUE("img/Rogue.png");

	private ImageStore store;

	private MiscImage(String... image) {
		store = new ImageStore(image);
	}

	/**
	 * Randomly selects an available image for the chosen background and
	 * displays it.
	 *
	 * @return a randomly chosen image for the relevant background.
	 * @throws IOException if the image could not be read from the file.
	 */
	@Override
	public BufferedImage getImage() throws IOException {
		return store.getImage();
	}
}
