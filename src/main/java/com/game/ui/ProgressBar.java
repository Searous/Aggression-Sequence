package com.game.ui;

import java.awt.Image;

import com.game.main.Handler;

public class ProgressBar {
	
	private int posX,posY,sizeX,sizeY;
	private Image bgSprite,fgSprite;
	private Handler handler;
	
	public ProgressBar(int posX, int posY, int sizeX, int sizeY, Image bgSprite, Image fgSprite, Handler handler) {
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.bgSprite = bgSprite;
		this.fgSprite = fgSprite;
		this.handler = handler;
	}

}
