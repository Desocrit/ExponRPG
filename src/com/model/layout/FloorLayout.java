package com.model.layout;

import java.awt.Dimension;
import java.security.InvalidParameterException;
import java.util.List;

import com.model.entity.npc.Enemy;
import com.utils.StringUtils;
import com.view.GUI;
import com.view.images.BackgroundImage;

/**
 * Represents the layout of the floor, including all of the positions of the
 * rooms. Also tracks the position of the player for ease of use.
 *
 * @author Christopher
 *
 */
public class FloorLayout {

	/* Floor number of this floor */
	private int floorNum;
	/* Player's current location as a co-ordinate, starting from (0,0) */
	private Dimension playerLocation;

	/* 2d array of rooms in each location */
	private Room[][] rooms;
	/* 2d array of whether the north door of each room is locked. */
	private boolean[][] northDoorLocked;
	/* 2d array of whether the east door of each room is locked */
	private boolean[][] eastDoorLocked;

	/* View to be notified by room events. */
	private GUI view;

	/**
	 * Generates a new floor layout of the designated floor number.
	 *
	 * @param floorNumber the floor number to be created. Floor width and height
	 *            will be set to this value plus three.
	 */
	public FloorLayout(int floorNumber) {
		this(floorNumber, null);
	}

	/**
	 * Generates a new floor layout of the designated floor number.
	 *
	 * @param floorNumber the floor number to be created. Floor width and height
	 *            will be set to this value plus three.
	 * @param playerLocation the initial location of the player.
	 */
	public FloorLayout(int floorNumber, Dimension playerLocation) {
		this(floorNumber, playerLocation, null);
	}

	/**
	 * Generates a new floor layout of the designated floor number.
	 *
	 * @param floorNumber the floor number to be created. Floor width and height
	 *            will be set to this value plus three.
	 * @param playerLocation the initial location of the player.
	 * @param view view to be notified
	 */
	public FloorLayout(int floorNumber, Dimension playerLocation, GUI view) {
		this.floorNum = floorNumber;
		this.view = view;
		rooms = new Room[floorNumber + 3][floorNumber + 3];
		northDoorLocked = new boolean[floorNumber + 3][floorNumber + 2];
		eastDoorLocked = new boolean[floorNumber + 2][floorNumber + 3];
		generateRooms();
		setPlayerLocation(playerLocation);
	}

	// Generates the rooms for the layout.
	private void generateRooms() {
		for (int i = 0; i < floorNum + 3; i++)
			for (int j = 0; j < floorNum + 3; j++) {
				if (i != floorNum + 2)
					eastDoorLocked[i][j] = false;
				if (j != floorNum + 2)
					northDoorLocked[i][j] = false;
				rooms[i][j] = new Room(view);
			}
	}

	/**
	 * Returns true if there is a room in the selected direction next to the
	 * room currently inhabited by the player, or otherwise false.
	 *
	 * @param dir direction to be checked.
	 * @return whether a room exists.
	 */
	public boolean roomExists(Direction dir) {
		return roomExists(dir, playerLocation);
	}

	/**
	 * Returns true if there is a room in the selected direction next to the
	 * room at the passed location, or otherwise false.
	 *
	 * @param dir direction to be checked.
	 * @param location location to be checked.
	 * @return whether a room exists.
	 */
	public boolean roomExists(Direction dir, Dimension location) {
		try {
			// Find offset location.
			Dimension targetPosition = offsetLocation(location,
					dir.getOffset(), floorNum + 2, floorNum + 2);
			// Check room at location.
			return rooms[targetPosition.width][targetPosition.height] != null;
		} catch (InvalidParameterException e) {
			// Location was out of bounds.
			return false;
		}
	}

	/**
	 * Returns true if the room in the specified direction next to the room at
	 * the passed location both exists and has been discovered, else false.
	 *
	 * @param dir direction to be travelled.
	 * @param location location to be checked.
	 * @return whether the specified room exists and has been discovered.
	 */
	public boolean roomDiscovered(Direction dir, Dimension location) {
		if (!roomExists(dir, location))
			return false;
		return (getRoom(offsetLocation(location, dir.getOffset())))
				.hasBeenDiscovered();
	}

	/**
	 * Returns true if the room in the specified direction next to the room at
	 * the passed location both exists and has been entered, else false.
	 *
	 * @param dir direction to be travelled.
	 * @param location location to be checked.
	 * @return whether the specified room exists and has been entered.
	 */
	public boolean roomEntered(Direction dir, Dimension location) {
		if (!roomExists(dir, location))
			return false;
		return (getRoom(offsetLocation(location, dir.getOffset())))
				.hasBeenEntered();
	}

	/**
	 * Returns true if there is a locked door in the selected direction next to
	 * the room currently inhabited by the player, or otherwise false.
	 *
	 * @param dir direction to be checked.
	 * @return whether a locked door exists.
	 */
	public boolean doorLocked(Direction dir) {
		return roomExists(dir, playerLocation);
	}

	/**
	 * Returns true if there is a locked door in the selected direction next to
	 * the room at the passed location, or otherwise false.
	 *
	 * @param dir direction to be checked.
	 * @param location location to be checked.
	 * @return whether a locked door exists.
	 */
	public boolean doorLocked(Direction dir, Dimension location) {
		try {
			// Find offset location.
			Dimension targetPosition = offsetLocation(location,
					dir.getOffset(), floorNum + 2, floorNum + 2);
			// Check room at location.
			if (rooms[targetPosition.width][targetPosition.height] == null)
				return false;
			// Make sure correct door is being checked.
			if (dir == Direction.NORTH || dir == Direction.EAST)
				targetPosition = location;
			// Check array for locked door.
			if (dir == Direction.NORTH || dir == Direction.SOUTH)
				return northDoorLocked[targetPosition.width][targetPosition.height];
			else
				return eastDoorLocked[targetPosition.width][targetPosition.height];
		} catch (InvalidParameterException e) {
			// Location was out of bounds.
			return false;
		}
	}

	/**
	 * Returns the passed location offset by the passed offset, after checking
	 * that the resultant location is within the bounds of this floor. If not,
	 * throws an InvalidParameterException.
	 *
	 * @param location location to be offset.
	 * @param offset amount to offset location by.
	 * @return offset location.
	 * @throws InvalidParameterException if combined location is not within the
	 *             floor bounds.
	 */
	public Dimension offsetLocation(Dimension location, Dimension offset) {
		return offsetLocation(location, offset, floorNum + 2, floorNum + 2);
	}

	/**
	 * Returns the passed location offset by the passed offset, after checking
	 * that the resultant location is within the bounds given. If not, throws an
	 * InvalidParameterException.
	 *
	 * @param location location to be offset.
	 * @param offset amount to offset location by.
	 * @param floorWidth maximum value for resultant location width.
	 * @param floorHeight maximum value for resultant location height.
	 * @return offset location.
	 * @throws InvalidParameterException if combined location is not within the
	 *             given bounds.
	 */
	public Dimension offsetLocation(Dimension location, Dimension offset,
			int floorWidth, int floorHeight) {
		Dimension targetPosition = new Dimension(location);
		targetPosition.width += offset.getWidth();
		targetPosition.height += offset.getHeight();
		// Check if the location is within the array bounds.
		if (targetPosition.getWidth() < 0 || targetPosition.getHeight() < 0
				|| targetPosition.getHeight() > floorHeight
				|| targetPosition.getWidth() > floorWidth)
			throw new InvalidParameterException("Location out of bounds.");
		return targetPosition;
	}

	/**
	 * Attempts to move the player in the specified direction. Will return false
	 * if this would leave the player out of bounds, or in a gap, or otherwise
	 * will return true.
	 *
	 * @param dir direction to move the player.
	 * @return true if the room exists, else false.
	 */
	public boolean movePlayer(Direction dir) {
		if (view != null)
			view.displayMessage("Moved "
					+ StringUtils.titleCase(dir.toString()) + ".");
		return setPlayerLocation(offsetLocation(playerLocation, dir.getOffset()));
	}

	// Accessors

	/**
	 * Attempts to set the player's location to the specified location. Will
	 * return false if the location is not a room on the floor, otherwise will
	 * return true and move the player accordingly.
	 *
	 * @param location location for player to be moved to.
	 * @return true if the player could be moved otherwise false.
	 */
	public boolean setPlayerLocation(Dimension location) {
		if (location.getWidth() < 0 || location.getHeight() < 0
				|| location.getHeight() > floorNum + 2
				|| location.getWidth() > floorNum + 2)
			return false;
		if (rooms[location.width][location.height] == null)
			return false;

		// Set the player location
		playerLocation = location;

		// Update the background image. New background types go here.
		if (view != null) {
			view.setBackgroundImage(BackgroundImage.ROOM);
			view.clearForeground();
		}

		// Check for enemies.
		List<Enemy> enemies = getRoom().getEnemies();
		// Inform the player about these enemies.
		if (view != null && !enemies.isEmpty()) {
			String message = "Encountered a ";
			for (int i = 0; i < enemies.size(); i++) {
				message += StringUtils.titleCase(enemies.get(i).toString()
						.toLowerCase());
				if (i == enemies.size() - 2)
					message += " and a ";
				else if (i == enemies.size() - 1)
					message += ".";
				else
					message += ", ";
			}
			view.displayMessage(message);
		}

		// Discover nearby rooms.
		for (Direction dir : Direction.values())
			try {
				getRoom(offsetLocation(location, dir.getOffset())).discover();
			} catch (InvalidParameterException e) {
				// Room does not exist, do not discover it.
			}

		getRoom().enter();
		return true;
	}

	/**
	 * @return the room at the player's current location.
	 */
	public Room getRoom() {
		return getRoom(playerLocation.width, playerLocation.height);
	}

	/**
	 * @param x x coordinate of the desired room
	 * @param y y coordinate of the desired room
	 * @return the room at the specified location, or null no room is found.
	 */
	public Room getRoom(int x, int y) {
		return rooms[x][y];
	}

	/**
	 * @param location location of the desired room.
	 * @return the room at the specified location, or null no room is found.
	 */
	public Room getRoom(Dimension location) {
		return rooms[location.width][location.height];
	}

	/**
	 * @return the current location of the player.
	 */
	public Dimension getPlayerLocation() {
		return playerLocation;
	}

	/**
	 * @return the floor number of this floor.
	 */
	public int getFloorNumber() {
		return floorNum;
	}

	/**
	 * Sets the view to be updated when events occur.
	 *
	 * @param view view to be updated.
	 */
	public void setView(GUI view) {
		// Set the view.
		this.view = view;
		for (Room[] roomRow : rooms)
			for (Room room : roomRow)
				room.setView(view);

		// Update the view.
		view.setBackgroundImage(BackgroundImage.ROOM);
		view.clearForeground();
	}

	/**
	 * @return true if the player is in combat, or else false.
	 */
	public boolean inCombat() {
		return !getRoom().getEnemies().isEmpty();
	}

}
