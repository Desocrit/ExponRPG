package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This panel displays a console, explaining the in-game events to the player.
 * Events can be displayed by calling the displayMessage method. This should be
 * called via the GUI facade.
 *
 * @author Christopher
 *
 */
public class ConsolePanel extends JPanel {

	private static final long serialVersionUID = -5388168484830040518L;

	private JTextArea textArea;

	/*
	 * Constructs the console panel. Should only be called from the GUI class.
	 */
	protected ConsolePanel() {
		super(new BorderLayout());
		setPreferredSize(new Dimension());

		// Set up text area
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(10);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane);
	}

	/**
	 * Displays the requested message on a new line in the text area. A newline
	 * character is automatically added to the end of the line.
	 *
	 * @param message message to be displayed
	 */
	public void displayMessage(String message) {
		textArea.setText(textArea.getText() + message + "\n");
	}

	/**
	 * Clears the text from the text area.
	 */
	public void clear() {
		textArea.setText("");
	}

}
