package com.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.model.entity.pc.Attribute;
import com.model.entity.pc.Player;

/**
 * The attributes and stats panel, responsible for showing the player's
 * attribute values, including health and mana values.
 *
 * @author Christopher
 *
 */
public class StatsPanel extends JPanel {

	private static final long serialVersionUID = 2065813871632233743L;

	/* The player whose stats are displayed. */
	private Player player;

	/* Bar displaying player health. */
	private JProgressBar hpBar;
	/* Bar displaying player mana. */
	private JProgressBar manaBar;
	/* Label to indicate the level of the player. */
	private JLabel levelLabel;
	/* Bar showing xp until next level. */
	private JProgressBar xpBar;
	/* Labels to show attribute values. */
	private Map<Attribute, JLabel> attributes;

	/**
	 * Constructs a new Stats Panel, showing unknown values.
	 */
	public StatsPanel() {
		// Set up the panel itself.
		setPreferredSize(new Dimension());
		setBackground(new Color(150, 150, 150));

		// Construct progress bars
		hpBar = new JProgressBar(0, 10000);
		manaBar = new JProgressBar(0, 10000);
		xpBar = new JProgressBar(0, 10000);

		// Set up progress bars.
		for (JProgressBar bar : new JProgressBar[] { hpBar, manaBar, xpBar }) {
			bar.setValue(10000);
			bar.setString("?");
			bar.setStringPainted(true);
		}

		// Recolour bars
		hpBar.setForeground(new Color(150, 30, 30));
		xpBar.setForeground(new Color(30, 150, 30));

		// Construct and adjust labels.
		levelLabel = new JLabel("Level: ?");
		levelLabel.setFont(levelLabel.getFont().deriveFont((float) (16)));

		add(hpBar);
		add(manaBar);
		add(levelLabel);
		add(xpBar);

		// Set up attribute labels.
		attributes = new HashMap<Attribute, JLabel>();
		for (Attribute att : Attribute.values()) {
			JLabel attLabel = new JLabel(" " + att.getShortening() + ": ?  ");
			attLabel.setFont(attLabel.getFont().deriveFont((float) (16)));
			attributes.put(att, attLabel);
			add(attLabel);
		}
	}

	/**
	 * Sets the player whose stats are displayed on the stats panel.
	 *
	 * @param player player whose stats should be displayed.
	 */
	public void setPlayer(Player player) {
		this.player = player;
		repaint();
	}

	/**
	 * Update health and mana values to accurate values.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int barWidth = (int) (getWidth() * 0.46);
		int barHeight = (int) (getHeight() * 0.15);

		float fontSize = Math.min(barHeight, barWidth / 10);

		Font barFont = hpBar.getFont().deriveFont((float) (fontSize * 0.8));
		// Set bar fonts and sizes.
		hpBar.setFont(barFont);
		hpBar.setPreferredSize(new Dimension(barWidth, barHeight));
		manaBar.setPreferredSize(new Dimension(barWidth, barHeight));
		manaBar.setFont(barFont);
		xpBar.setPreferredSize(new Dimension((int) (getWidth() * 0.932),
				barHeight));
		xpBar.setFont(barFont);

		// Adjust label fonts.
		Font largeLabelFont = barFont.deriveFont((float) (fontSize * 1.4));
		Font labelFont = barFont.deriveFont(fontSize);
		levelLabel.setFont(largeLabelFont);
		for (Attribute att : Attribute.values())
			attributes.get(att).setFont(labelFont);

		if (player != null) {
			// Update bar texts and values.
			hpBar.setValue(new BigDecimal(player.getHP())
			.multiply(BigDecimal.valueOf(10000))
			.divide(new BigDecimal(player.getMaxHP()),
					BigDecimal.ROUND_HALF_UP).intValue());
			hpBar.setString(player.getHP() + "/" + player.getMaxHP());

			manaBar.setValue(new BigDecimal(player.getMana())
			.multiply(BigDecimal.valueOf(10000))
			.divide(new BigDecimal(player.getMaxMana()),
					BigDecimal.ROUND_HALF_UP).intValue());
			manaBar.setString(player.getMana() + "/" + player.getMaxMana());

			xpBar.setValue(new BigDecimal(player.getCurrentXP())
			.multiply(BigDecimal.valueOf(10000))
			.divide(new BigDecimal(player.getXPToNextLevel()),
					BigDecimal.ROUND_HALF_UP).intValue());
			xpBar.setString(player.getCurrentXP() + "/"
					+ player.getXPToNextLevel());

			// Update HP label.
			levelLabel.setText("Level: " + player.getLevel());
			// Update attribute labels.
			for (Attribute att : Attribute.values()) {
				JLabel label = attributes.get(att);
				label.setText("  " + att.getShortening() + ": "
						+ player.getAttributeValue(att) + " ");
			}
		}
	}
}
