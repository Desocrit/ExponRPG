package com.view.images;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An interface to signify an image as able to be accessed by a getImage()
 * method, as per all of the image enumerations in this package.
 *
 * @author Christopher
 *
 */
public interface DisplayableImage {

	/**
	 * @return the attached image.
	 * @throws IOException if the image cannot be accessed.
	 */
	public BufferedImage getImage() throws IOException;

}
