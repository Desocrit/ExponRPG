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
public enum BackgroundImage implements DisplayableImage {
	/**
	 * An outdoor field, for use for character selection for now.
	 */
	FIELD("img/Field.jpg"),
	/**
	 * A standard room with any number of doors.
	 */
	ROOM("img/Room.jpg");

	private ImageStore store;

	private BackgroundImage(String... image) {
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
