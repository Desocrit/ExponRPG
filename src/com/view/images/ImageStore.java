package com.view.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class ImageStore {

	private String[] imageNames;
	private BufferedImage[] images;

	/**
	 * Stores a set of images, to be lazily initialised whenever one is needed.
	 * To be used by Image enumerations.
	 *
	 * @param image list of images to be stored
	 */
	protected ImageStore(String... image) {
		imageNames = new String[image.length];
		for (int i = 0; i < image.length; i++)
			imageNames[i] = image[i];
		images = new BufferedImage[image.length];
	}

	/**
	 * Randomly selects an available image and displays it.
	 *
	 * @return a randomly chosen image for the relevant background.
	 * @throws IOException if the image could not be read from the file.
	 */
	public BufferedImage getImage() throws IOException {
		if (imageNames.length == 0)
			throw new UnsupportedOperationException("No images stored.");
		int imageChosen;
		if (imageNames.length == 1)
			imageChosen = 0;
		else
			imageChosen = (int) (Math.random() * imageNames.length);
		if (images[imageChosen] == null)
			images[imageChosen] = ImageIO
					.read(new File(imageNames[imageChosen]));
		return images[imageChosen];
	}

}
