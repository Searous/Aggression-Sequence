package com.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

import com.game.main.Game;
import com.game.main.Handler;

/**
 * Default button class. Should only be used for menus.
 * @author Searous
 */
public class Button {
	protected String text;
	protected int sizeX;
	protected int sizeY;
	protected int posX,posY;
	protected Image idleSprite;
	protected Image hoverSprite;
	protected Image disableSprite;
	public boolean enabled;
	protected String toolTip;
	public boolean flag;
	public int selected = 0;
	public List<String> option;
	public int selectedStarting = 0;
	public boolean doTextScale = true;
	/**
	 * True if the mouse is over this button, false if not.
	 */
	public boolean isHovered;
	
	protected Handler handler;
	public boolean firstUpdate;
	public boolean noHover;
	/**
	 * Default button.
	 * @param text The text to be displayed on the button.
	 * @param idleSprite Sprite for when the button isn't hovered, or disabled; the default look of the button.
	 * @param hoverSprite Sprite for when the button is hovered.
	 * @param disableSprite Sprite for when the button is disabled.
	 * @param sizeX Horizontal size of the button. NOTE sprites WILL be stretched.
	 * @param sizeY Vertical size of the button.  NOTE sprites WILL be stretched.
	 */
	public Button(String text, Image idleSprite, Image hoverSprite, Image disableSprite, int sizeX, int sizeY, Handler handler) {
		this.text = text;
		this.idleSprite = idleSprite;
		this.hoverSprite = hoverSprite;
		this.disableSprite = disableSprite;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.handler = handler;
	}
	public Button(String text, Image idleSprite, Image hoverSprite, Image disableSprite, int posX, int posY, int sizeX, int sizeY, Handler handler) {
		this.text = text;
		this.idleSprite = idleSprite;
		this.hoverSprite = hoverSprite;
		this.disableSprite = disableSprite;
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.handler = handler;
	}
	
	public void render(Graphics2D g, @SuppressWarnings("unused") int buttonID, int offsetX, int offsetY) {	
		//this.posX = 100;
		if (enabled) { //When enabled
			//When hovered
			if (handler.input.mouseX >= offsetX * Game.world.ppu && handler.input.mouseX <=  (sizeX + offsetX) * Game.world.ppu && handler.input.mouseY >= offsetY * Game.world.ppu && handler.input.mouseY <= (sizeY + offsetY) * Game.world.ppu && !noHover) {
				if (!isHovered) isHovered = true;
				Game.world.drawScaledImage(g, hoverSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				g.setColor(Color.white);
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.07)), (int)(offsetY + (sizeY * 0.68)), doTextScale ? (int)(sizeX / 7) : 31);
				if (toolTip != null) {
					//TODO Implament tooltips for buttons
				}
			} else { //When idle.
				if (isHovered) isHovered = false;
				Game.world.drawScaledImage(g, idleSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
				g.setColor(Color.white);
				Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.07)), (int)(offsetY + (sizeY * 0.68)), doTextScale ? (int)(sizeX / 7) : 31);
				if (toolTip != null) {
					
				}
			}
		} else { //When disabled
			if (isHovered) isHovered = false;
			Game.world.drawScaledImage(g, disableSprite, (int)(offsetX * Game.world.ppu), (int)(offsetY * Game.world.ppu), (int)(sizeX * Game.world.ppu), (int)(sizeY * Game.world.ppu));
			g.setColor(Color.white);
			Game.world.drawScaledText(g, text, (int)(offsetX + (sizeX * 0.07)), (int)(offsetY + (sizeY * 0.68)), doTextScale ? (int)(sizeX / 7) : 31);
			if (toolTip != null) {
				
			}
		}
	}
	
	public void setIdalSprite(String name) {
		this.idleSprite = Handler.sprite.getSprite(name);
	}
	public void setHoverSprite(String name) {
		this.hoverSprite = Handler.sprite.getSprite(name);
	}
	public void setDisableSprite(String name) {
		this.disableSprite = Handler.sprite.getSprite(name);
	}
	public Button setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getSizeX() {
		return this.sizeX;
	}
	public int getSizeY() {
		return this.sizeY;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	public boolean isEnabled() {
		return this.enabled;
	}
	public String getText() {
		return this.text;
	}

	public void update() {
		
	}
	public Button setDoTextScale(boolean b) {
		this.doTextScale = b;
		return this;
	}

}
