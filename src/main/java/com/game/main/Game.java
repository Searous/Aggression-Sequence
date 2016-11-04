package com.game.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;

import com.game.action.ActionAHighEnergyPrismD;
import com.game.action.ActionAPhaseEngineD;
import com.game.action.ActionBPPlasmaCannonD;
import com.game.enums.Control;
import com.game.enums.ID;
import com.game.enums.State;
import com.game.item.ItemShip;
import com.game.item.ItemSuper;
import com.game.item.ItemUtility;
import com.game.item.ItemWeapon;
import com.game.object.Fighter;
import com.game.object.Player;
import com.game.object.background.Star;
import com.game.stage.Stage;
import com.game.stage.StageTest;
import com.game.ui.Button;
import com.game.ui.ButtonCharacter;
import com.game.ui.ButtonCharacterEquip;
import com.game.ui.ButtonCharacterUpgrade;
import com.game.ui.ButtonOption;
import com.game.ui.ButtonOptionControl;
import com.game.ui.ButtonPageSelect;
import com.game.ui.ButtonStage;
import com.game.ui.ButtonStart;
import com.game.ui.Cursor;
import com.game.ui.Menu;
import com.game.ui.MenuAdvanced;

/**
 * The main game class.  This where the game is managed.
 * @author Searous
 */
public class Game extends Canvas implements Runnable {
	/**
	 * The version of the game.  Currently only used for the version number in the window title.
	 */
	public static final String VERSION = "6.5.10"; //6.6 - Storry map finished.  6.7 - Puase menu finished.  6.8 - Code cleanup completed.  7.0 - Stages implemented, and the other things before this are done as well.
	/**
	 * Sets the width and height of the game window.
	 */
	public static int WIDTH = 1280, HEIGHT = WIDTH / 12 * 9; 
	
	/**
	 * The name of the game.
	 */
	public static String NAME;
	
	private Thread thread;
	private boolean running = false;
	/**
	 * The game's handler.  Manages all game stuff.
	 */
	public static Handler handler; 
	/**
	 * The game's window object.
	 */
	public static Window window; 
	public static Random r;
	
	
	/**
	 * Game's difficulty setting.
	 */
	@Deprecated
	public static byte difficulty = 3; 
	/**
	 * The game's state.  See @State
	 */
	public static State state = State.None;
	/**
	 * The world object.  Not really named correctly.
	 */
	public static World world; 
	/**
	 * Currently not really used.
	 */
	public static boolean fullscreen;
	public static int renderOffset = 0;
	private boolean updateWindow = false;; 
	
	/**
	 * Stores XP required to reach next level values.
	 * Index 0 is 0 to offset the array, so the level value can be passed as the index
	 * to get the XP to next level value for that level.
	 * Index 15 is the total XP gained at level 15
	 */
	public static int[] xpVals = new int[]{
		0,
		100, 125, 158, 199,
		251, 316, 398, 501,
		632, 795, 1002, 1261,
		1589, 2000, 
		16568
	};
	
	/**
	 * Range: 0-3
	 * Used to know which set of items to display on the right-side of the character menu.
	 */
	public static int equipSelected = 0;
	public static int modeSelected = 0;
	public static int areaSelected = 0; 
	public static int stageSelected = 0;
	
	public static float xpBoost = 1.0f;
	
	/**
	 * Quality of the game's graphics.
	 * 0 - Low
	 * 1 - Medium
	 * 2 - High
	 */
	public static int quality = 2;
	
	//Debug Options
	public static boolean drawEnemyHealthBars = true;
	@Deprecated
	public static boolean dissableMouseCursor = true;
	public static boolean enableEnemySpawner = true;
	public static boolean debugHud = true;
	public static boolean godMode = false;
	public static boolean drawHitBoxes = false;
	
	public static Stage currentStage;
	public static int currentWave,currentEnemy,waveKills = 0;
	
	public static int health,energy;
	public static boolean isUtilReady = true, isSuperReady = true;
	public static Ticker utilTimter,superTimer;
	
	public Game() {
		
		handler = new Handler(this);
		
		NAME = Localization.get("game.name");
		
		if ((int)handler.save.get("fullscreen") == 0) {
			fullscreen = false;
		} else {
			fullscreen = true;
		}
		
		world = new World(handler);
		//robot = new Robot();
//		this.addKeyListener(Handler.keys);
//		this.addMouseListener(Handler.mouse);
//		this.addMouseMotionListener(Handler.mouse);
		
		this.window = new Window(WIDTH, HEIGHT, NAME+" Alpha "+VERSION, this);
		
	    r = new Random();
	    
	    handler.addTicker("waveTimer", 0, 0);
	    handler.setTickerActive("waveTimer", false);
		handler.addTicker("spawnTimer", 0, 0);
		handler.setTickerActive("spawnTimer", false);
	    
//--------------------Menu---------------------------------------------------------------------------------------------------------------------------
	    //registerMenus();
	    load();
	    
	    handler.menus.get("options").options[0].selected = (int)handler.save.get("resolution");
	    handler.menus.get("options").options[1].selected = (int)handler.save.get("fullscreen");
	    handler.menus.get("options").options[2].selected = (int)handler.save.get("musicvol");
	    handler.menus.get("options").options[3].selected = (int)handler.save.get("soundvol");
	    handler.menus.get("options").options[4].selected = (int)handler.save.get("enableCursor");
	    handler.menus.get("options").options[5].selected = (int)handler.save.get("cursorStyle");
	    handler.menus.get("options").options[6].selected = (int)handler.save.get("cursorSize");
	    handler.menus.get("options").options[7].selected = (int)handler.save.get("hudScale");
	    handler.menus.get("options").options[8].selected = (int)handler.save.get("enemyHealthBars");
	    handler.menus.get("options").options[9].selected = (int)handler.save.get("damageNumbers");
	    handler.menus.get("options").options[10].selected = (int)handler.save.get("showfps");
	    
	    handler.menus.get("optionsControls").options[0].selected = (int)handler.save.get("control_moveUp");
	    handler.menus.get("optionsControls").options[1].selected = (int)handler.save.get("control_moveLeft");
	    handler.menus.get("optionsControls").options[2].selected = (int)handler.save.get("control_moveDown");
	    handler.menus.get("optionsControls").options[3].selected = (int)handler.save.get("control_moveRight");
	    handler.menus.get("optionsControls").options[4].selected = (int)handler.save.get("control_useWeapon");
	    handler.menus.get("optionsControls").options[5].selected = (int)handler.save.get("control_useUtility");
	    handler.menus.get("optionsControls").options[6].selected = (int)handler.save.get("control_useSuper");
	    
	    handler.menus.get("options").options[0].selectedStarting = (int)handler.save.get("resolution");
	    handler.menus.get("options").options[1].selectedStarting = (int)handler.save.get("fullscreen");
	    handler.menus.get("options").options[2].selectedStarting = (int)handler.save.get("musicvol");
	    handler.menus.get("options").options[3].selectedStarting = (int)handler.save.get("soundvol");
	    handler.menus.get("options").options[4].selectedStarting = (int)handler.save.get("enableCursor");
	    handler.menus.get("options").options[5].selectedStarting = (int)handler.save.get("cursorStyle");
	    handler.menus.get("options").options[6].selectedStarting = (int)handler.save.get("cursorSize");
	    handler.menus.get("options").options[7].selectedStarting = (int)handler.save.get("hudScale");
	    handler.menus.get("options").options[8].selectedStarting = (int)handler.save.get("enemyHealthBars");
	    handler.menus.get("options").options[9].selectedStarting = (int)handler.save.get("damageNumbers");
	    handler.menus.get("options").options[10].selectedStarting = (int)handler.save.get("showfps");
	    
	    handler.menus.get("optionsControls").options[0].selectedStarting = (int)handler.save.get("control_moveUp");
	    handler.menus.get("optionsControls").options[1].selectedStarting = (int)handler.save.get("control_moveLeft");
	    handler.menus.get("optionsControls").options[2].selectedStarting = (int)handler.save.get("control_moveDown");
	    handler.menus.get("optionsControls").options[3].selectedStarting = (int)handler.save.get("control_moveRight");
	    handler.menus.get("optionsControls").options[4].selectedStarting = (int)handler.save.get("control_useWeapon");
	    handler.menus.get("optionsControls").options[5].selectedStarting = (int)handler.save.get("control_useUtility");
	    handler.menus.get("optionsControls").options[6].selectedStarting = (int)handler.save.get("control_useSuper");

	    setStages();
	    //Audio.load();
	    //Audio.setMusic("temp");
	    //--------------------Menu---------------------------------------------------------------------------------------------------------------------------
	   
	    //Spawns the player's ship.  Will be moved when stages are implemented.
	    handler.setPlayer(new Player((float)world.WORLD_SCALE_X / 2 - 32, (float)world.WORLD_SCALE_Y / 2 - 32, 0, 0, ID.Player, handler));
	    //handler.addObject(new Fighter((float)world.WORLD_SCALE_X / 2 - 20, (float)world.WORLD_SCALE_Y / 2 - 200, ID.Fighter, handler));
	    //handler.addObject(new Seaker(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), 16, 16, ID.Seaker, handler));
	    
	    //Spawns the star objects into the background.  Will be moved when stages are implemented.
	    for (int i = 0; i < 80; i++) {
	    	float posX = (float)r.nextInt(WIDTH - 20) + 10;
	    	float posY = (float)r.nextInt(HEIGHT - 20) + 10;
	    	
	    	handler.addBackgroundObject(new Star(posX, posY, 0, 0, ID.Star, handler));
	    }
	    
	    applyOptions(true);
	    state = State.Main_Menu;
	    Log.write("Game initialized", "Info");
	}
	
	/**
	 * Runs on startup during game init.
	 * Loads all the game's assets and resources, along with registration.
	 */
	private void load() {
		handler.sprite.register("cursor", "ui/cursor");
	    handler.sprite.register("fighter_red", "mob/fighter/fighter_0");
	    handler.sprite.register("fighter_white", "mob/fighter/fighter_1");
	    handler.sprite.register("button", "ui/button");
	    handler.sprite.register("button_hover", "ui/button_hover");
	    handler.sprite.register("button_disabled", "ui/button_disabled");
	    handler.sprite.register("button_character_idle_0", "ui/button_character_idle_0");
	    handler.sprite.register("button_character_idle_1", "ui/button_character_idle_1");
	    handler.sprite.register("button_character_hovered_0", "ui/button_character_hovered_0");
	    handler.sprite.register("button_character_hovered_1", "ui/button_character_hovered_1");
	    handler.sprite.register("button_character_disabled_0", "ui/button_character_disabled_0");
	    handler.sprite.register("button_character_disabled_1", "ui/button_character_disabled_1");
	    handler.sprite.register("button_character_disabled_2", "ui/button_character_disabled_2");
	    handler.sprite.register("button_small", "ui/button_small");
	    handler.sprite.register("button_small_hovered", "ui/button_small_hovered");
	    handler.sprite.register("button_small_disabled", "ui/button_small_disabled");
	    handler.sprite.register("bullet", "bullet/plasma_canon/white");
	    handler.sprite.register("bullet_orange", "bullet/plasma_canon/orange");
	    handler.sprite.register("bullet_light_pink", "bullet/plasma_canon/light_pink");
	    handler.sprite.register("player", "mob/player");
	    handler.sprite.register("character_bg", "ui/character_bg");
	    handler.sprite.register("connector", "ui/button_character_connector");
	    handler.sprite.register("character_xpbar_0", "ui/character_xpvar_0");
	    handler.sprite.register("character_xpbar_1", "ui/character_xpvar_1");
	    handler.sprite.register("character_infobg", "ui/character_infobg");
	    handler.sprite.register("start_bg", "ui/start_bg");
	    handler.sprite.register("hud_background", "ui/hud/hud_background");
	    handler.sprite.register("hud_health", "ui/hud/hud_health");
	    handler.sprite.register("hud_energy", "ui/hud/hud_energy");
	    handler.sprite.register("hud_cooldown_0", "ui/hud/hud_cooldown_0");
	    handler.sprite.register("hud_cooldown_1", "ui/hud/hud_cooldown_1");
	    handler.sprite.register("energy_orb_0", "mob/energy_orb_0");
	    handler.sprite.register("energy_orb_1", "mob/energy_orb_1");
	    handler.sprite.register("aoe_green", "bullet/green_aoe");
	      
	    this.xpbar = (BufferedImage)handler.sprite.getSprite("character_xpbar_1");
	    
	    handler.registerItem("prospect", new ItemShip("Prospect", handler){
	    	public void setItem() {
	    		this.saveName = "prospect";
	    		this.upgradeNames = new String[]{
    				"Upgrade 1", "Upgrade 2",
    				"Upgrade 3", "Upgrade 4",
    				"Upgrade 5"
    			};
    			
    			this.subText = new String[]{
    				"+10% Energy pickup"
    			};
    			
    			this.hp = 100;
    			this.energy = 100;
    			this.energyPickup = 1.1f;
    			this.speed = 5;
    			this.immuneDur = 6;
    			
    			//this.level = 15;
	    	}
	    });
	    handler.registerItem("testship1", new ItemShip("Another Ship", handler){
	    	public void setItem(){
	    		this.saveName = "testship1";
	    		this.upgradeNames = new String[]{
    				"Penis Extender", "Vagina Tightener",
    				"Loli Jiuce", "Generic Waffle Iron",
    				"Master"
    			};
    				
    			this.subText = new String[]{
    				"Grants a protective shield"
    			};
    				
    			this.hp = 100;
    			this.energy = 100;
    			this.energyPickup = 1.0f;
	    	}
	    });
	    handler.registerItem("testship2", new ItemShip("Test Ship 2", handler){
	    	public void setItem() {
	    		this.saveName = "testship2";
	    		this.upgradeNames = new String[]{
    				"Upgrade 1", "Upgrade 2",
    				"Upgrade 3", "Upgrade 4",
    				"Upgrade 5"
    			};
    			
    			this.subText = new String[]{
    				"Immune to Stasus",
    				"Grants protective shield",
    				"Filled with lolis",
    				"...and anime"
    			};
    			
    			this.hp = 100;
    			this.energy = 100;
    			this.energyPickup = 1.1f;
	    	}
	    });
	    handler.registerItem("testship3", new ItemShip("Test Ship 3", handler){
	    	public void setItem() {
	    		this.saveName = "testship3";
	    		this.upgradeNames = new String[]{
    				"Upgrade 1", "Upgrade 2",
    				"Upgrade 3", "Upgrade 4",
    				"Upgrade 5"
    			};
    			
    			this.subText = new String[]{
    				"Immune to Stasus",
    				"Grants protective shield",
    				"Filled with lolis",
    				"...and anime"
    			};
    			
    			this.hp = 100;
    			this.energy = 100;
    			this.energyPickup = 1.1f;
	    	}
	    });  
	    
	    handler.registerItem("plasmacannon", new ItemWeapon("Plasma Cannon", handler){
	    	public void setItem() {
	    		this.saveName = "plasmacannon";
	    		this.action = new ActionBPPlasmaCannonD(0, 0, handler);
	    		
	    		this.upgradeNames = new String[]{
	    			"Upgrade 1", "Upgrade 2",
	    			"Upgrade 3", "Upgrade 4",
	    			"Upgrade 5"
	    		};
	    		
	    		this.damage = 2f;
	    		this.speed = 7;
	    		this.numProjectile = 1;
	    		this.fireRate = 12;
	    	}
	    });
	    handler.registerItem("testweapon1", new ItemWeapon("Test Weapon 1", handler){
	    	public void setItem() {
	    		this.saveName = "testweapon1";
	    		this.action = new ActionBPPlasmaCannonD(0, 0, handler);
	    		
	    		this.upgradeNames = new String[]{
	    			"Upgrade 1", "Upgrade 2",
	    			"Upgrade 3", "Upgrade 4",
	    			"Upgrade 5"
	    		};
	    		
	    		this.damage = 2f;
	    		this.speed = 7;
	    		this.numProjectile = 1;
	    		this.fireRate = 10;
	    	}
	    });
	    handler.registerItem("testweapon2", new ItemWeapon("Test Weapon 2", handler){
	    	public void setItem() {
	    		this.saveName = "testweapon2";
	    		this.action = new ActionBPPlasmaCannonD(0, 0, handler);
	    		
	    		this.upgradeNames = new String[]{
	    			"Upgrade 1", "Upgrade 2",
	    			"Upgrade 3", "Upgrade 4",
	    			"Upgrade 5"
	    		};
	    		
	    		this.damage = 2f;
	    		this.speed = 7;
	    		this.numProjectile = 1;
	    		this.fireRate = 10;
	    	}
	    });
	    handler.registerItem("testweapon3", new ItemWeapon("Test Weapon 3", handler){
	    	public void setItem() {
	    		this.saveName = "testweapon3";
	    		this.action = new ActionBPPlasmaCannonD(0, 0, handler);
	    		
	    		this.upgradeNames = new String[]{
	    			"Upgrade 1", "Upgrade 2",
	    			"Upgrade 3", "Upgrade 4",
	    			"Upgrade 5"
	    		};
	    		
	    		this.damage = 2f;
	    		this.speed = 7;
	    		this.numProjectile = 1;
	    		this.fireRate = 10;
	    	}
	    });
	    
	    handler.registerItem("phaseengine", new ItemUtility("Phase Engine", handler){
	    	public void setItem() {
	    		this.saveName = "phaseengine";
	    		
	    		this.action = new ActionAPhaseEngineD(0, 0, handler);
	    		
	    		this.upgradeNames = new String[]{
	    			"Upgrade 1", "Upgrade 2",
	    			"Upgrade 3", "Upgrade 4",
	    			"Upgrade 5"
	    		};
	    	}
	    });
	    handler.registerItem("testutility1", new ItemUtility("Test Utility 1", handler){
	    	public void setItem() {
	    		this.saveName = "testutility1";
	    		
	    		this.upgradeNames = new String[]{
	    			"Upgrade 1", "Upgrade 2",
	    			"Upgrade 3", "Upgrade 4",
	    			"Upgrade 5"
	    		};
	    	}
	    });
	    handler.registerItem("testutility2", new ItemUtility("Test Utility 2", handler){
	    	public void setItem() {
	    		this.saveName = "testutility2";
	    		
	    		this.upgradeNames = new String[]{
	    			"Upgrade 1", "Upgrade 2",
	    			"Upgrade 3", "Upgrade 4",
	    			"Upgrade 5"
	    		};
	    	}
	    });
	    handler.registerItem("testutility3", new ItemUtility("Test Utility 3", handler){
	    	public void setItem() {
	    		this.saveName = "testutility3";
	    		
	    		this.upgradeNames = new String[]{
	    			"Upgrade 1", "Upgrade 2",
	    			"Upgrade 3", "Upgrade 4",
	    			"Upgrade 5"
	    		};
	    	}
	    });
	    
	    handler.registerItem("hightenergyprism", new ItemSuper("High-Energy Prism", handler){
	    	public void setItem() {
	    		this.saveName = "hightenergyprism";
	    		this.action = new ActionAHighEnergyPrismD(0, 0, handler);
	    		
	    		this.upgradeNames = new String[]{
    				"Upgrade 1", "Upgrade 2",
    				"Upgrade 3", "Upgrade 4",
    				"Upgrade 5"
    			};
	    		
	    		this.size = 80;
	    		this.duration = 264;
	    		this.deployDelay = 45;
	    		this.damage = 3;
	    	}
	    });
	    handler.registerItem("testsuper1", new ItemSuper("Test Super 1", handler){
	    	public void setItem() {
	    		this.saveName = "testsuper1";
	    		
	    		this.upgradeNames = new String[]{
    				"Upgrade 1", "Upgrade 2",
    				"Upgrade 3", "Upgrade 4",
    				"Upgrade 5"
    			};
	    	}
	    });
	    handler.registerItem("testsuper2", new ItemSuper("Test Super 2", handler){
	    	public void setItem() {
	    		this.saveName = "testsuper2";
	    		
	    		this.upgradeNames = new String[]{
    				"Upgrade 1", "Upgrade 2",
    				"Upgrade 3", "Upgrade 4",
    				"Upgrade 5"
    			};
	    	}
	    });
	    handler.registerItem("testsuper3", new ItemSuper("Test Super 3", handler){
	    	public void setItem() {
	    		this.saveName = "testsuper3";
	    		
	    		this.upgradeNames = new String[]{
    				"Upgrade 1", "Upgrade 2",
    				"Upgrade 3", "Upgrade 4",
    				"Upgrade 5"
    			};
	    	}
	    });
	    
	    int buttonHeight = 55;
	    int buttonWidth = buttonHeight * 4;
	    
		Button[] buttons = new Button[4];
	    for (int i = 0; i < 4; i++) {
	    	Button b = new Button(
	    			i == 0 ? Localization.get("menu.main.start") : i == 1 ? Localization.get("menu.main.character") : i == 2 ? Localization.get("menu.main.options") : Localization.get("menu.main.exit"),
	    			Handler.sprite.getSprite("button"),
	    			Handler.sprite.getSprite("button_hover"),
	    			Handler.sprite.getSprite("button"),
	    			buttonWidth, buttonHeight,
	    			handler); //192, 48
	    	b.setEnabled(true);
		    buttons[i] = b;
	    }
	    
	    handler.registerMenu("mainmenu", new Menu(NAME, 4, 15, 70, 0, 15, handler, buttons){
	    	public void action(int pressed) {
	    		if (pressed == 0) {
	    			state = State.Start_Menu;
	    		} else if (pressed == 1) {
	    			state = State.Character_Menu;
	    			
	    			if (handler.getItem(handler.getInv(equipSelected)[0]).level != 15) {
		    			xpbarWidth = ((256.0f / (float)xpVals[handler.getItem(handler.getInv(equipSelected)[0]).level]) * (float)handler.getItem(handler.getInv(equipSelected)[0]).xp);
						if (xpbarWidth <= 0) xpbarWidth = 1;
						xpbar = ((BufferedImage) handler.sprite.getSprite("character_xpbar_1")).getSubimage(0, 0, (int)xpbarWidth, handler.sprite.getSprite("character_xpbar_1").getHeight(null));
						xpbarDrawWidth = (458.0f / (256.0f / xpbarWidth)) * world.ppu;
	    			}
	    		} else if (pressed == 2) {
	    			state = State.Options;
	    		} else if (pressed == 3) {
	    			System.exit(0);
	    		} else if (pressed == 4) {
	    			
	    		}
	    	}
	    });

	    //Start Menu
	    buttons = new Button[5];
	    for (int i = 0; i < 5; i++) {
	    	Button b = new ButtonStart(
	    			i == 0 ? Localization.get("menu.start.story") : i == 1 ? Localization.get("menu.start.survival") : i == 2 ? Localization.get("menu.start.gauntlet") : i == 3 ? Localization.get("menu.start.sandbox") : Localization.get("menu.back"),
	    			Handler.sprite.getSprite("button"),
	    			Handler.sprite.getSprite("button_hover"),
	    			Handler.sprite.getSprite("button_hover"),
	    			buttonWidth, buttonHeight,
	    			handler); //192, 48
	    	b.setEnabled(i == 0 ? false : true);
		    buttons[i] = b;
	    }

	    handler.registerMenu("startmenu", new Menu(Localization.get("menu.start.name"), 5, 15, 70, 0, 15, handler, buttons){
	    	public void action(int pressed) {
	    		if (pressed == 0) {
	    			title = "Start - Story";
	    			modeSelected = 0;
	    			
	    			options[0].setEnabled(false);
	    			options[1].setEnabled(true);
	    			options[2].setEnabled(true);
	    			options[3].setEnabled(true);
	    		} else if (pressed == 1) {
	    			title = "Start - Survival";
	    			modeSelected = 1;
	    			
	    			options[0].setEnabled(true);
	    			options[1].setEnabled(false);
	    			options[2].setEnabled(true);
	    			options[3].setEnabled(true);
	    		} else if (pressed == 2) {
	    			title = "Start - Gauntlet";
	    			modeSelected = 2;
	    			
	    			options[0].setEnabled(true);
	    			options[1].setEnabled(true);
	    			options[2].setEnabled(false);
	    			options[3].setEnabled(true);
	    		} else if (pressed == 3) {
	    			title = "Start - Sandbox";
	    			modeSelected = 3;
	    			
	    			options[0].setEnabled(true);
	    			options[1].setEnabled(true);
	    			options[2].setEnabled(true);
	    			options[3].setEnabled(false);
	    		} else if (pressed == 4) {
	    			state = State.Main_Menu;
	    		}
	    	}
	    });
	    
	    buttons = new Button[10];
	    for (int i = 0; i < 5; i++) {
	    	Button b = new ButtonStage(
	    			Localization.get("menu.start.stage"),
	    			Handler.sprite.getSprite("button"),
	    			Handler.sprite.getSprite("button_hover"),
	    			Handler.sprite.getSprite("button_hover"),
	    			10,
	    			i == 0 ? 62 : i == 1 ? 129 : i == 2 ? 196 : i == 3 ? 263 : 330,
	    			buttonWidth, buttonHeight,
	    			handler); //192, 48 //255, 171
	    	b.setEnabled(true);
		    buttons[i] = b;
	    }
	    buttons[5] = new Button( //Reset
	    		Localization.get("menu.start.reset"),
    			Handler.sprite.getSprite("button"),
    			Handler.sprite.getSprite("button_hover"),
    			Handler.sprite.getSprite("button_hover"),
    			311, 8,
    			140, 35,
    	handler).setEnabled(true); //192, 48
	    buttons[6] = new Button( //Tutorial
	    		Localization.get("menu.start.reset"),
    			Handler.sprite.getSprite("button"),
    			Handler.sprite.getSprite("button_hover"),
    			Handler.sprite.getSprite("button_disabled"),
    			159, 8,
    			140, 35,
    	handler).setEnabled(false); //192, 48
	    buttons[7] = new Button( //Launch
	    		Localization.get("menu.start.launch"),
    			Handler.sprite.getSprite("button"),
    			Handler.sprite.getSprite("button_hover"),
    			Handler.sprite.getSprite("button_disabled"),
    			7, 8,
    			140, 35,
    	handler).setEnabled(false); //192, 48
	    buttons[8] = new ButtonPageSelect( //Previous Area
    			"<",
    			Handler.sprite.getSprite("button_small"),
    			Handler.sprite.getSprite("button_small_hovered"),
    			Handler.sprite.getSprite("button_small_disabled"),
    			20, 404,
    			35, 35,
    	handler).setEnabled(true).setDoTextScale(false); //192, 48
	    buttons[9] = new ButtonPageSelect( //Next Area
    			">",
    			Handler.sprite.getSprite("button_small"),
    			Handler.sprite.getSprite("button_small_hovered"),
    			Handler.sprite.getSprite("button_small_disabled"),
    			404, 404,
    			35, 35,
    	handler).setEnabled(true).setDoTextScale(false); //192, 48
	    
	    handler.registerMenu("storymap", new MenuAdvanced(" ", 10, 243, 73, handler, buttons){
	    	public void action(int pressed, int button) {
	    		if (pressed < 5) {
	    			stageSelected = pressed;
	    			options[0].setEnabled(true);
	    			options[1].setEnabled(true);
	    			options[2].setEnabled(true);
	    			options[3].setEnabled(true);
	    			options[4].setEnabled(true);
	    			options[pressed].setEnabled(false);
	    			options[7].setEnabled(true);
	    		} else if (pressed == 5) { //Reset
	    			
	    		} else if (pressed == 6) { //Tutorial
	    			
	    		} else if (pressed == 7) { //Launch
	    			try {
						currentStage = handler.getStage((areaSelected * 5) + stageSelected).newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
	    			
	    			//handler.setPlayer(new Player(x, y, sizeX, sizeY, id, handler));
	    			
	    			health = handler.getPlayer().ship.hp;
	    			energy = 0;
	    			
	    			handler.object.clear();
	    			//handler.bgobj.clear();
	    			
	    			handler.removeTicker("waveTimer");  //Time between waves
	    			handler.removeTicker("spawnTimer"); //Time between enemy spawns
	    			
	    			currentWave = 0;
	    			currentEnemy = 0;
	    			
	    			currentStage.start();
	    			
	    			state = State.Game;
	    		} else if (pressed == 8) {
	    			options[0].setEnabled(true);
	    			options[1].setEnabled(true);
	    			options[2].setEnabled(true);
	    			options[3].setEnabled(true);
	    			options[4].setEnabled(true);
	    			options[7].setEnabled(false);
	    			
	    			areaSelected--;
	    		} else if (pressed == 9) {
	    			options[0].setEnabled(true);
	    			options[1].setEnabled(true);
	    			options[2].setEnabled(true);
	    			options[3].setEnabled(true);
	    			options[4].setEnabled(true);
	    			options[7].setEnabled(false);
	    			
	    			areaSelected++;
	    		}
	    	}
	    	public void tick() {
	    		super.tick();
	    		if (areaSelected == 0 && options[8].enabled) {
	    			options[8].setEnabled(false);
	    		} else if (areaSelected > 0 && !options[8].enabled) {
	    			options[8].setEnabled(true);
	    		}
	    		if (areaSelected == ((handler.stages.size() / 5) - 1) && options[9].enabled) {
	    			options[9].setEnabled(false);
	    		} else if (areaSelected < ((handler.stages.size() / 5) - 1) && !options[9].enabled) {
	    			options[9].setEnabled(true);
	    		}
	    	}
	    });
	    
	    //Character Menu
	    buttons = new Button[5];
	    for (int i = 0; i < 4; i++) {
	    	ButtonCharacter b = new ButtonCharacter(
	    			i == 0 ? Localization.get("menu.character.ship") : i == 1 ? Localization.get("menu.character.weapon") : i == 2 ? Localization.get("menu.character.utility") : Localization.get("menu.character.super"),
	    			handler.sprite.getSprite("button_character_idle_0"),
	    			handler.sprite.getSprite("button_character_hovered_0"),
	    			handler.sprite.getSprite("button_character_disabled_0"),
	    			buttonWidth, buttonHeight,
	    			handler); //192, 48
	    	if (i == 0) b.setEnabled(false); else b.setEnabled(true);
		    buttons[i] = b;
	    }
	    buttons[4] = new Button(
	    		Localization.get("menu.back"),
    			Handler.sprite.getSprite("button"),
    			Handler.sprite.getSprite("button_hover"),
    			Handler.sprite.getSprite("button"),
    			buttonWidth, buttonHeight,
    			handler); //192, 48
	    buttons[4].setEnabled(true);
	    
	    handler.registerMenu("charactermenu", new Menu(Localization.get("menu.character.name"), 5, 15, 70, 0, 15, handler, buttons){
	    	public void action(int pressed) {
	    		if (pressed == 0) {
	    			equipSelected = 0;
	    			this.options[0].setEnabled(false);
	    			this.options[1].setEnabled(true);
	    			this.options[2].setEnabled(true);
	    			this.options[3].setEnabled(true);
	    			
	    			if (handler.getItem(handler.getInv(equipSelected)[0]).level != 15) {
						xpbarWidth = ((256.0f / (float)xpVals[handler.getItem(handler.getInv(equipSelected)[0]).level]) * (float)handler.getItem(handler.getInv(equipSelected)[0]).xp);
						if (xpbarWidth <= 0) xpbarWidth = 1;
						xpbar = ((BufferedImage) handler.sprite.getSprite("character_xpbar_1")).getSubimage(0, 0, (int)xpbarWidth, handler.sprite.getSprite("character_xpbar_1").getHeight(null));
						xpbarDrawWidth = (458.0f / (256.0f / xpbarWidth)) * world.ppu;
	    			}
	    		} else if (pressed == 1) {
	    			equipSelected = 1;
	    			this.options[0].setEnabled(true);
	    			this.options[1].setEnabled(false);
	    			this.options[2].setEnabled(true);
	    			this.options[3].setEnabled(true);
	    			
	    			if (handler.getItem(handler.getInv(equipSelected)[0]).level != 15) { 
		    			xpbarWidth = ((256.0f / (float)xpVals[handler.getItem(handler.getInv(equipSelected)[0]).level]) * (float)handler.getItem(handler.getInv(equipSelected)[0]).xp);
						if (xpbarWidth <= 0) xpbarWidth = 1;
						xpbar = ((BufferedImage) handler.sprite.getSprite("character_xpbar_1")).getSubimage(0, 0, (int)xpbarWidth, handler.sprite.getSprite("character_xpbar_1").getHeight(null));
						xpbarDrawWidth = (458.0f / (256.0f / xpbarWidth)) * world.ppu;
	    			}
	    		} else if (pressed == 2) {
	    			equipSelected = 2;
	    			this.options[0].setEnabled(true);
	    			this.options[1].setEnabled(true);
	    			this.options[2].setEnabled(false);
	    			this.options[3].setEnabled(true);
	    			
	    			if (handler.getItem(handler.getInv(equipSelected)[0]).level != 15) {
		    			xpbarWidth = ((256.0f / (float)xpVals[handler.getItem(handler.getInv(equipSelected)[0]).level]) * (float)handler.getItem(handler.getInv(equipSelected)[0]).xp);
						if (xpbarWidth <= 0) xpbarWidth = 1;
						xpbar = ((BufferedImage) handler.sprite.getSprite("character_xpbar_1")).getSubimage(0, 0, (int)xpbarWidth, handler.sprite.getSprite("character_xpbar_1").getHeight(null));
						xpbarDrawWidth = (458.0f / (256.0f / xpbarWidth)) * world.ppu;
	    			}
	    		} else if (pressed == 3) {
	    			equipSelected = 3;
	    			this.options[0].setEnabled(true);
	    			this.options[1].setEnabled(true);
	    			this.options[2].setEnabled(true);
	    			this.options[3].setEnabled(false);
	    			
	    			if (handler.getItem(handler.getInv(equipSelected)[0]).level != 15) {
		    			xpbarWidth = ((256.0f / (float)xpVals[handler.getItem(handler.getInv(equipSelected)[0]).level]) * (float)handler.getItem(handler.getInv(equipSelected)[0]).xp);
						if (xpbarWidth <= 0) xpbarWidth = 1;
						xpbar = ((BufferedImage) handler.sprite.getSprite("character_xpbar_1")).getSubimage(0, 0, (int)xpbarWidth, handler.sprite.getSprite("character_xpbar_1").getHeight(null));
						xpbarDrawWidth = (458.0f / (256.0f / xpbarWidth)) * world.ppu;
	    			}
	    		} else if (pressed == 4) {
	    			state = State.Main_Menu;
	    			
	    			for (int i = 0; i < 4; i++) {
	    				for (int j = 0; j < 4; j++) {
	    					handler.getItem(handler.getInv(i)[j]).update();
	    				}	
	    			}
	    		}
	    	}
	    });
	   
	    int h = 35;
	    int w = h * 4;
	    buttons = new Button[3];
	    //player = handler.getPlayer();
	    for (int i = 0; i < 3; i++) {
	    	ButtonCharacterEquip b = new ButtonCharacterEquip(
	    			" ",
	    			Handler.sprite.getSprite("button_character_idle_0"),
	    			Handler.sprite.getSprite("button_character_hovered_0"),
	    			Handler.sprite.getSprite("button"),
	    			w, h,
	    			handler); //192, 48
	    	b.setEnabled(true);
		    buttons[i] = b;
	    }
	    
	    handler.registerMenu("equips", new Menu(" ", 3, 250, 81, 12, 0, handler, buttons){
	    	public void action(int pressed) {
	    		if (pressed == 0) {
	    			handler.invMove(equipSelected, 0, pressed + 1);
	    			
	    			if (handler.getItem(handler.getInv(equipSelected)[0]).level != 15) {
		    			xpbarWidth = ((256.0f / (float)xpVals[handler.getItem(handler.getInv(equipSelected)[0]).level]) * (float)handler.getItem(handler.getInv(equipSelected)[0]).xp);
						if (xpbarWidth <= 0) xpbarWidth = 1;
						xpbar = ((BufferedImage) handler.sprite.getSprite("character_xpbar_1")).getSubimage(0, 0, (int)xpbarWidth, handler.sprite.getSprite("character_xpbar_1").getHeight(null));
						xpbarDrawWidth = (458.0f / (256.0f / xpbarWidth)) * world.ppu;
	    			}
	    		} else if (pressed == 1) {
	    			handler.invMove(equipSelected, 0, pressed + 1);
	    			
	    			if (handler.getItem(handler.getInv(equipSelected)[0]).level != 15) {
		    			xpbarWidth = ((256.0f / (float)xpVals[handler.getItem(handler.getInv(equipSelected)[0]).level]) * (float)handler.getItem(handler.getInv(equipSelected)[0]).xp);
						if (xpbarWidth <= 0) xpbarWidth = 1;
						xpbar = ((BufferedImage) handler.sprite.getSprite("character_xpbar_1")).getSubimage(0, 0, (int)xpbarWidth, handler.sprite.getSprite("character_xpbar_1").getHeight(null));
						xpbarDrawWidth = (458.0f / (256.0f / xpbarWidth)) * world.ppu;
	    			}
	    		} else if (pressed == 2) {
	    			handler.invMove(equipSelected, 0, pressed + 1);
	    			
	    			if (handler.getItem(handler.getInv(equipSelected)[0]).level != 15) {
		    			xpbarWidth = ((256.0f / (float)xpVals[handler.getItem(handler.getInv(equipSelected)[0]).level]) * (float)handler.getItem(handler.getInv(equipSelected)[0]).xp);
						if (xpbarWidth <= 0) xpbarWidth = 1;
						xpbar = ((BufferedImage) handler.sprite.getSprite("character_xpbar_1")).getSubimage(0, 0, (int)xpbarWidth, handler.sprite.getSprite("character_xpbar_1").getHeight(null));
						xpbarDrawWidth = (458.0f / (256.0f / xpbarWidth)) * world.ppu;
	    			}
	    		}
	    	}
	    });
	    
	    h = 52;
	    w = h * 4;
	    buttons = new Button[5];
	    for (int i = 0; i < 5; i++) {
	    	ButtonCharacterUpgrade b = new ButtonCharacterUpgrade(
	    			Localization.get("menu.character.upgrade"),
	    			Handler.sprite.getSprite("button"),
	    			Handler.sprite.getSprite("button_hover"),
	    			Handler.sprite.getSprite("button"),
	    			i == 0 || i == 2 ? 10 : i == 1 || i == 3 ? 241 : 125, 
	    			i == 0 || i == 1 ? 10 : i == 2 || i == 3 ? 71 : 133,
	    			w, h,
	    			handler); //192, 48
	    	b.setEnabled(true);
		    buttons[i] = b;
	    }
	    
	    handler.registerMenu("upgrades", new MenuAdvanced(" ", 5, 243, 225, handler, buttons){
	    	public void action(int pressed, int button) {
	    		if (pressed == 0) {
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[0] = !handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[0];
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[1] = false;
	    			if (handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[1]) {
	    				xpBoost += 0.1f;
	    			} else {
	    				xpBoost += 0.1f;
	    			}
	    		} else if (pressed == 1) {
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[1] = !handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[1];
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[0] = false;
	    			if (handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[1]) {
	    				xpBoost += 0.1f;
	    			} else {
	    				xpBoost += 0.1f;
	    			}
	    		} else if (pressed == 2) {
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[2] = !handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[2];
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[3] = false;
	    		} else if (pressed == 3) {
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[3] = !handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[3];
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[2] = false;
	    		} else if (pressed == 4) {
	    			handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[4] = !handler.getItem(handler.getInv(Game.equipSelected)[0]).upgrades[4];
	    		}
	    		handler.getItem(handler.getInv(equipSelected)[0]).update();
	    	}
	    });
	    
	    //Options Menu
	    buttons = new Button[15];
	    for (int i = 0; i < 12; i++) {
	    	//System.out.println(i);
	    	ButtonOption b = new ButtonOption(
	    			i == 0 ? Localization.get("menu.options.resolution") : i == 1 ? Localization.get("menu.options.fullscreen") : i == 2 ? Localization.get("menu.options.musicVol") : i == 3 ? Localization.get("menu.options.soundVol") : i == 4 ? Localization.get("menu.options.customCursor") : i == 5 ? Localization.get("menu.options.cursorStyle") : i == 6 ? Localization.get("menu.options.cursorSize") : i == 7 ? Localization.get("menu.options.hudScale") : i == 8 ? Localization.get("menu.options.enemyHPBars") : i == 9 ? Localization.get("menu.options.dmgPopoff") : Localization.get("menu.options.showFPS"),
	    					i == 0 ? new String[]{"1280x960","1440x1080","640x477","960x720"} : i == 1 ? new String[]{Localization.get("menu.false"),Localization.get("menu.true")} : i == 2 ? new String[]{"10 / 10","0 / 10","1 / 10","2 / 10","3 / 10","4 / 10","5 / 10","6 / 10","7 / 10","8 / 10","9 / 10"} : i == 3 ? new String[]{"10 / 10","0 / 10","1 / 10","2 / 10","3 / 10","4 / 10","5 / 10","6 / 10","7 / 10","8 / 10","9 / 10"} : i == 4 ? new String[]{Localization.get("menu.true"),Localization.get("menu.false")} : i == 5 ? new String[]{"Default"} : i == 6 ? new String[]{"5 / 10","6 / 10","7 / 10","8 / 10","9 / 10","10 / 10","1 / 10","2 / 10","3 / 10","4 / 10"} : i == 7 ? new String[]{"5 / 10","6 / 10","7 / 10","8 / 10","9 / 10","10 / 10","1 / 10","2 / 10","3 / 10","4 / 10"} : i == 8 ? new String[]{Localization.get("menu.false"),Localization.get("menu.true")} : i == 9 ? new String[]{Localization.get("menu.false"),Localization.get("menu.true")} : new String[]{Localization.get("menu.false"),Localization.get("menu.true")},
	    					Handler.sprite.getSprite("button"),
	    					Handler.sprite.getSprite("button_hover"),
	    					Handler.sprite.getSprite("button"),
	    					i == 0 ? 0 : i == 1 ? 0 : i == 2 ? 0 : i == 3 ? 0 : i == 4 ? 235 : i == 5 ? 235 : i == 6 ? 235 : i == 7 ? 235 : i == 8 ? 470 : i == 9 ? 470 : 470,
	    					i == 0 ? 0 : i == 1 ? 70 : i == 2 ? 140 : i == 3 ? 210 : i == 4 ? 0 : i == 5 ? 70 : i == 6 ? 140 : i == 7 ? 210 : i == 8 ? 0 : i == 9 ? 70 : 140,
	    			buttonWidth, buttonHeight,
	    			handler); //192, 48
	    	b.setEnabled(true);
		    buttons[i] = b;
	    }
	    buttons[11] = new Button(
	    		Localization.get("menu.back"),
	    		Handler.sprite.getSprite("button"),
				Handler.sprite.getSprite("button_hover"),
				Handler.sprite.getSprite("button"),
				0, 370,
				buttonWidth, buttonHeight,
				handler
	    ).setEnabled(true);
	    buttons[12] = new Button(
	    		Localization.get("menu.apply"),
	    		Handler.sprite.getSprite("button"),
				Handler.sprite.getSprite("button_hover"),
				Handler.sprite.getSprite("button"),
				235, 370,
				buttonWidth, buttonHeight,
				handler
	    ).setEnabled(true);
	    buttons[13] = new Button(
	    		Localization.get("menu.defaults"),
	    		Handler.sprite.getSprite("button"),
				Handler.sprite.getSprite("button_hover"),
				Handler.sprite.getSprite("button"),
				470, 370,
				buttonWidth, buttonHeight,
				handler
	    ).setEnabled(true);
	    buttons[14] = new Button( // 470,210
	    		Localization.get("menu.options.controls"),
	    		Handler.sprite.getSprite("button"),
				Handler.sprite.getSprite("button_hover"),
				Handler.sprite.getSprite("button"),
				470, 280,
				buttonWidth, buttonHeight,
				handler
	    ).setEnabled(true);
	    
	    handler.registerMenu("options", new MenuAdvanced(Localization.get("menu.options.name"), 15, 15, 70, handler, buttons){
	    	public void action(int pressed, int button) {
	    		if (pressed < 11) {
					if (button == 1) {
						if (options[pressed].selected + 1 == options[pressed].option.size()) {
							options[pressed].selected = 0;
						} else options[pressed].selected++;
					} else if (button == 3) {
						if (options[pressed].selected == 0) {
							options[pressed].selected = options[pressed].option.size() - 1;
						} else options[pressed].selected--;
					}
		    		if (options[pressed].selected == options[pressed].selectedStarting) options[pressed].flag = false; else options[pressed].flag = true;
	    		} else if (pressed == 11) {
	    			state = State.Main_Menu;
	    			for (int i = 0; i < options.length - 1; i++){
	    				options[i].flag = false;
		    			options[i].selected = options[i].selectedStarting;
	    			}
	    		} else if (pressed == 12) {
	    			applyOptions(false);
	    			
	    			handler.save.put("resolution", options[0].selected);
	    			options[0].selectedStarting = (int)handler.save.get("resolution");
	    			options[0].flag = false;
	    		   
	    			handler.save.put("fullscreen", options[1].selected);
	    		    options[1].selectedStarting = (int)handler.save.get("fullscreen");
	    		    options[1].flag = false;
	    		  
	    		    handler.save.put("musicvol", options[2].selected);
	    		    options[2].selectedStarting = (int)handler.save.get("musicvol");
	    		    options[2].flag = false;

	    		    handler.save.put("soundvol", options[3].selected);
	    		    options[3].selectedStarting = (int)handler.save.get("soundvol");
	    		    options[3].flag = false;

	    		    handler.save.put("enableCursor", options[4].selected);
	    		    options[4].selectedStarting = (int)handler.save.get("enableCursor");
	    		    options[4].flag = false;

	    		    handler.save.put("cursorStyle", options[5].selected);
	    		    options[5].selectedStarting = (int)handler.save.get("cursorStyle");
	    		    options[5].flag = false;

	    		    handler.save.put("cursorSize", options[6].selected);
	    		    options[6].selectedStarting = (int)handler.save.get("cursorSize");
	    		    options[6].flag = false;

	    		    handler.save.put("hudScale", options[7].selected);
	    		    options[7].selectedStarting = (int)handler.save.get("hudScale");
	    		    options[7].flag = false;

	    		    handler.save.put("enemyHealthBars", options[8].selected);
	    		    options[8].selectedStarting = (int)handler.save.get("enemyHealthBars");
	    		    options[8].flag = false;

	    		    handler.save.put("damageNumbers", options[9].selected);
	    		    options[9].selectedStarting = (int)handler.save.get("damageNumbers");
	    		    options[9].flag = false;

	    		    handler.save.put("showfps", options[10].selected);
	    		    options[10].selectedStarting = (int)handler.save.get("showfps");
	    		    options[10].flag = false;
		
	    			handler.saveSave();
	    		} else if (pressed == 13) {
	    			for (int i = 0; i < options.length - 1; i++){
		    			options[i].selected = 0;
			    		if (options[i].selected == options[i].selectedStarting) options[i].flag = false; else options[i].flag = true;
	    			}
	    		} else if (pressed == 14) {
	    			state = State.Options_Controls;
	    			
	    		}
	    	}
	    });
	    
	    buttons = new Button[10];
	    for (int i = 0; i < 7; i++) {
	    	Button b = new ButtonOptionControl(
	    			i == 0 ? Localization.get("menu.controls.moveUp") : i == 1 ? Localization.get("menu.controls.moveLeft") : i == 2 ? Localization.get("menu.controls.moveDown") : i == 3 ? Localization.get("menu.controls.moveRight") : i == 4 ? Localization.get("menu.controls.useWeapon") : i == 5 ? Localization.get("menu.controls.useUtility") : Localization.get("menu.controls.useSuper"),
	    			Handler.sprite.getSprite("button"),
	    			Handler.sprite.getSprite("button_hover"),
	    			Handler.sprite.getSprite("button"),
	    			i == 0 ? 0 : i == 1 ? 0 : i == 2 ? 0 : i == 3 ? 0 : i == 4 ? 235 : i == 5 ? 235 : 235,
	    			i == 0 ? 0 : i == 1 ? 70 : i == 2 ? 140 : i == 3 ? 210 : i == 4 ? 0 : i == 5 ? 70 :  140,
	    			buttonWidth, buttonHeight,
	    			handler); //192, 48
	    	b.setEnabled(true);
	    	//b.selected = i == 0 ? handler.getControl(Control.MOVE_UP) : i == 1 ? handler.getControl(Control.MOVE_LEFT) : i == 2 ? handler.getControl(Control.MOVE_DOWN) : i == 3 ? handler.getControl(Control.MOVE_RIGHT) : i == 4 ? handler.getControl(Control.USE_WEAPON) : i == 5 ? handler.getControl(Control.USE_UTILITY) : handler.getControl(Control.USE_SUPER);
		    buttons[i] = b;
	    }
	    buttons[7] = new Button(
	    		Localization.get("menu.back"),
	    		Handler.sprite.getSprite("button"),
				Handler.sprite.getSprite("button_hover"),
				Handler.sprite.getSprite("button_disabled"),
				0, 370,
				buttonWidth, buttonHeight,
				handler
	    ).setEnabled(true);
	    buttons[8] = new Button(
	    		Localization.get("menu.apply"),
	    		Handler.sprite.getSprite("button"),
				Handler.sprite.getSprite("button_hover"),
				Handler.sprite.getSprite("button_disabled"),
				235, 370,
				buttonWidth, buttonHeight,
				handler
	    ).setEnabled(true);
	    buttons[9] = new Button(
	    		Localization.get("menu.defaults"),
	    		Handler.sprite.getSprite("button"),
				Handler.sprite.getSprite("button_hover"),
				Handler.sprite.getSprite("button_disabled"),
				470, 370,
				buttonWidth, buttonHeight,
				handler
	    ).setEnabled(true);

	    handler.registerMenu("optionsControls", new MenuAdvanced(Localization.get("menu.controls.name"), 10, 15, 70, handler, buttons){
	    	public boolean flag = false;
	    	public int selectID = 0;
	    	public int oldS = 0;
	    	public void action(int pressed, int button) {
	    		if (pressed < 7) {
	    			this.flag = true;
	    			this.selectID = pressed;
	    			this.oldS = options[pressed].selected;
	    			options[pressed].selected = -1;
	    			this.setNoHover(true);
	    			options[pressed].setEnabled(false);
	    			options[pressed].flag = false;
	    			options[pressed].firstUpdate = false; //Used here to know if two options are conflicting
	    		} else if (pressed == 7) {
	    			state = State.Options;
	    			options[8].setEnabled(true);
	    			for (int i = 0; i < options.length; i++) {
	    				options[i].selected = options[i].selectedStarting;
	    				options[i].flag = false;
	    				options[i].firstUpdate = false;
	    			}
	    		} else if (pressed == 8) {		
	    			handler.save.put("control_moveUp", options[0].selected);
	    			handler.save.put("control_moveLeft", options[1].selected);
	    			handler.save.put("control_moveDown", options[2].selected);
	    			handler.save.put("control_moveRight", options[3].selected);
	    			handler.save.put("control_useWeapon", options[4].selected);
	    			handler.save.put("control_useUtility", options[5].selected);
	    			handler.save.put("control_useSuper", options[6].selected);
	    			
	    			handler.saveSave();
	    			
	    			for (int i = 0; i < options.length; i++) {
	    				options[i].selectedStarting = options[i].selected;
	    				options[i].flag = false;
	    			}
	    		} else if (pressed == 9) {
	    			options[0].selected = 92;
	    			options[1].selected = 70;
	    			options[2].selected = 88;
	    			options[3].selected = 73;
	    			
	    			options[4].selected = 1;
	    			options[5].selected = 3;
	    			options[6].selected = 37;
	    			
	    			for (int i = 0; i < options.length - 3; i++) 
	    				if (options[i].selectedStarting == options[i].selected) 
	    					options[i].flag = false;
	    				else
	    					options[i].flag = true;
	    		}
	    	}
	    	public void tick() {
	    		if (!flag) super.tick(); else {
    				if (handler.input.getLastPressed() != -1 && handler.input.getLastPressed() != 0 && canClick) {
    					if (handler.isPressed(Control.PAUSE)) { flag = false; this.setNoHover(false); options[selectID].setEnabled(true); options[selectID].selected = this.oldS; } else {
							options[this.selectID].selected = handler.input.getLastPressed();
							
							flag = false;
							this.setNoHover(false);
			    			options[this.selectID].setEnabled(true);
			    			if (options[this.selectID].selected == options[this.selectID].selectedStarting) options[this.selectID].flag = false; else options[this.selectID].flag = true;
			    			
			    			options[8].setEnabled(true);
			    			for (int i = 0; i < options.length - 3; i++) {
			    				options[i].firstUpdate = false; //Used here to know if two options are conflicting
			    				for (int j = 0; j < options.length - 3; j++) {
			    					if (i != j) {
				    					if (options[i].selected == options[j].selected) {
				    						options[i].firstUpdate = true;
				    						options[j].firstUpdate = true;
				    						options[8].setEnabled(false);
				    					}
			    					}
			    				}
			    			}
    					}
    				}
    				if (handler.input.isButtonDown(1) || handler.input.isButtonDown(3))
    					canClick = false;
    				else
    					canClick = true;
	    		}
	    	}
	    	private void setNoHover(boolean setTo) {
	    		for (int i = 0; i < options.length; i++) {
	    			options[i].noHover = setTo; 
	    		}
	    	}
	    });
	    
	    //Fullscreen Notify
	    buttons = new Button[2];
	    for (int i = 0; i < 2; i++) {
	    	Button b = new Button(
	    			i == 0 ? Localization.get("menu.main.exit") : Localization.get("menu.back"),
	    			Handler.sprite.getSprite("button"),
	    			Handler.sprite.getSprite("button_hover"),
	    			Handler.sprite.getSprite("button"),
	    			buttonWidth, buttonHeight,
	    			handler); //192, 48
	    	b.setEnabled(true);
		    buttons[i] = b;
	    }
	    
	    handler.registerMenu("fullscreennotify", new Menu("", 2, 15, 120, 0, 15, handler, buttons){
	    	public void action(int pressed) {
	    		if (pressed == 0) {
	    			handler.saveSave();
	    			System.exit(0);
	    		} else if (pressed == 1) {
	    			if (fullscreen)  {
	    				handler.menus.get("options").options[1].selected = 0;
	    				handler.menus.get("options").options[1].selectedStarting = 0;
	    				handler.save.put("fullscreen", 0);
	    			} else {
	    				handler.menus.get("options").options[1].selected = 1;
	    				handler.menus.get("options").options[1].selectedStarting = 1;
	    				handler.save.put("fullscreen", 1);
	    			}
	    			state = State.Options;
	    		}
	    	}
	    });
	}
	
	/**
	 * Runs on startup during game init.
	 * The order of stage registration here, determins the order of the stages in-game.
	 * i.e. ever five stages listed will be a sector.
	 */
	private void setStages() {
		handler.registerStage(StageTest.class);
		handler.registerStage(StageTest.class);
		handler.registerStage(StageTest.class);
		handler.registerStage(StageTest.class);
		handler.registerStage(StageTest.class);
	}
	
	public void applyOptions(boolean forceApply) {
		MenuAdvanced menu = (MenuAdvanced)handler.menus.get("options");
		
		if (menu.options[1].selected == 0) {
			fullscreen = false;
		} else {
			fullscreen = true;
		}
		
		for (int i = 0; i < menu.options.length; i++) {
			//System.out.println("tities");
			
			if (i == 0 && (forceApply ? true : menu.options[i].selected != menu.options[i].selectedStarting)) { //Resolution
				if (!fullscreen) {
					System.out.println(fullscreen);
					if (menu.options[i].selected == 0) {
						WIDTH = 1280; HEIGHT = WIDTH / 12 * 9;
						this.setSize(WIDTH, HEIGHT);
						window.frame.pack();
						window.frame.setLocationRelativeTo(null);
						world.ppu = (float)Game.WIDTH / world.WORLD_SCALE_X;
					} else if (menu.options[i].selected == 1) {
						WIDTH = 1440; HEIGHT = WIDTH / 12 * 9;
						this.setSize(WIDTH, HEIGHT);
						window.frame.pack();
						window.frame.setLocationRelativeTo(null);
						world.ppu = (float)Game.WIDTH / world.WORLD_SCALE_X;
					} else if (menu.options[i].selected == 2) {
						WIDTH = 640; HEIGHT = WIDTH / 12 * 9;
						this.setSize(WIDTH, HEIGHT);
						window.frame.pack();
						window.frame.setLocationRelativeTo(null);
						world.ppu = (float)Game.WIDTH / world.WORLD_SCALE_X;
					} else if (menu.options[i].selected == 3) {
						WIDTH = 960; HEIGHT = WIDTH / 12 * 9;
						this.setSize(WIDTH, HEIGHT);
						window.frame.pack();
						window.frame.setLocationRelativeTo(null);
						world.ppu = (float)Game.WIDTH / world.WORLD_SCALE_X;
					}
				}
			} else if (i == 1 && (forceApply ? true : menu.options[i].selected != menu.options[i].selectedStarting)) { //Fullscreen
				state = State.FullscreenNotify;
			} else if (i == 2) { //Music
				
			} else if (i == 3) { //Sound
				
			} else if (i == 4 && (forceApply ? true : menu.options[i].selected != menu.options[i].selectedStarting)) {
				if (menu.options[i].selected == 0) {
					window.frame.getContentPane().setCursor(window.newc);
					Cursor.enabled = true;
				} else {
					window.frame.getContentPane().setCursor(window.oldc);
					Cursor.enabled = false;
				}
			} else if (i == 5) { //Cursor Style
				
			} else if (i == 6 && (forceApply ? true : menu.options[i].selected != menu.options[i].selectedStarting)) {
				if (menu.options[i].selected >= 0 && menu.options[i].selected <= 5) {
					Cursor.size = menu.options[i].selected + 5 + 1;
				} else {
					Cursor.size = menu.options[i].selected - 5 + 1;
				}
			} else if (i == 7) { //HUD Scale
				
			} else if (i == 8) { //HUD Fade
				
			} else if (i == 9 && (forceApply ? true : menu.options[i].selected != menu.options[i].selectedStarting)) {
				if (menu.options[i].selected == 0) {
					drawEnemyHealthBars = false;
				} else {
					drawEnemyHealthBars = true;
				}
			} else if (i == 10) {

			}
		}
	}
	
	/**
	 * Starts the java program by calling this class's constructor.  Don't remove the Game line.  The game won't work if you do.
	 * @param arg
	 */
	@SuppressWarnings("unused")
	public static void main(String[] arg) {
		new Game();
		
		Scanner scanner = new Scanner(System.in);

        while (true) {
            String[] command = scanner.nextLine().split("\\ ", -1);
            
            if (command[0] != null) {
	            if ("state".equals(command[0])) {
	                if (command[1] != null) {
	                	if ("set".equals(command[1])) {
	                		if (command[2] != null) {
	                			try {
	                				synchronized(state) { state = State.valueOf(command[2]); }
	                			} catch(Exception e) {
	                				System.out.println("Invalid state");
	                			}
	                		}
	                	} else if ("list".equals(command[1])) {
	                		for (int i = 0; i < State.values().length; i++) {
								System.out.println(State.values()[i]);
							}
	                	} else if ("get".equals(command[1])) {
	                		if (command[1] != null) {
	                			System.out.println(state.name());
	                		}
	                	} else {
	                		System.out.println("Usage:  state <get/set> <state>");
	                	}
	                }
	            } else {
	            	System.out.println("Unknown command");
	            }
            }
        }
	}
	
	public static int fps;
	/**
	 * Currently unused.
	 */
	@Deprecated
	public int frameLimit = 60; 
	/**
	 * DO NOT TOUCH THIS METHOD!!!!!!  FUCKING WITH THIS WILL MESS UP THE ENTIRE GAME!!!!!!
	 * This is LITERALY the core of the game.  Every bit of code that runs, starts here.
	 * This method calls tick() and render() here, at different times, to make the game work.
	 * tick() runs 60 times be second, render() runs whenever it can, no limit.
	 * The number of times render() is called every second, is the FPS.
	 */
	public void run() { 
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			
			render();
			frames++;
			
			if (window.frame.isFocused() && (int)handler.save.get("lockMouse") == 1 || window.frame.isFocused() && fullscreen) {
				if (MouseInfo.getPointerInfo().getLocation().x < this.getLocationOnScreen().x) {
					robot.mouseMove(this.getLocationOnScreen().x, MouseInfo.getPointerInfo().getLocation().y);
				} else if (MouseInfo.getPointerInfo().getLocation().x > this.getLocationOnScreen().x + WIDTH - 1) {
					robot.mouseMove(this.getLocationOnScreen().x + WIDTH - 1, MouseInfo.getPointerInfo().getLocation().y);
				} else if (MouseInfo.getPointerInfo().getLocation().y < this.getLocationOnScreen().y) {
					robot.mouseMove(MouseInfo.getPointerInfo().getLocation().x, this.getLocationOnScreen().y);
				} else if (MouseInfo.getPointerInfo().getLocation().y > this.getLocationOnScreen().y + HEIGHT - 1) {
					robot.mouseMove(MouseInfo.getPointerInfo().getLocation().x, this.getLocationOnScreen().y + HEIGHT - 1);
				}
			}
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = frames;
				frames = 0;
			}
		}
		stop();
	}
	
	/**
	 * Tick counter.  These are everywhere XD
	 */
	private int ticks = 0; 
	/**
 	* Currently unused.
 	*/
	@Deprecated
	private boolean b = true; //Currently unused.
	
	/**
	 * Will be removed when stages are implemented.
	 */
	@Deprecated
	public static boolean win = false; 
	private boolean flag = true;
	public Robot robot; 
	/**
	 * Main tick method.  DO NOT CALL THIS EVER!  Runs 60 times per second.
	 * Inside each "state == [A STATE]" if statement, is what happens when the game is in that state.
	 */
	private void tick() {
		if (robot == null) {
			try {
				robot = new Robot();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//handler.getPlayer().ship.level = 15;
		handler.tickTickers();
		switch(state) {
			default: break;
			case FullscreenNotify:
				handler.menus.get("fullscreennotify").tick();
				break;
			case Game:
				handler.setTickerActive("fireInterval", true);
				//System.out.println(handler.getTicker("fireInterval"));
				if (handler.hud.health != 0 && !win) {
					handler.tick();
					handler.hud.tick();
					//spawn.tick();
				} else ticks++;
				
				if (ticks >= 300) {
					System.exit(1);
				}
				
				if (enableEnemySpawner && flag) {
					if (handler.input.isButtonDown(2)) handler.addObject(new Fighter(handler.input.mouseX / Game.world.ppu, handler.input.mouseY / Game.world.ppu, 0, handler)); 
					flag = false;
				}
				
				if (!handler.input.isButtonDown(2)) {
					flag = true;
				}
				
				if (handler.isPressed(Control.PAUSE)) {
					state = State.Main_Menu;
				}
			
				currentStage.tick();
				Cursor.tick();
				break;
			case Main_Menu:
				handler.tickMenu("mainmenu");
				Cursor.tick();
				break;
			case Start_Menu:
				handler.tickMenu("startmenu");
				switch(modeSelected) {
				case 0:
					handler.tickMenu("storymap");
					break;
				}
				Cursor.tick();
				break;
			case Character_Menu:
				//handler.sprite.register("char", "charmenutest");
				handler.tickMenu("charactermenu");
				handler.tickMenu("equips");
				handler.tickMenu("upgrades");
				Cursor.tick();	
				break;
			case Options:
				handler.tickMenu("options");
				Cursor.tick();
				break;
			case Options_Controls:
				handler.tickMenu("optionsControls");
				Cursor.tick();
				break;
			case Pause:
				handler.hud.tick();
				Cursor.tick();
				break;
		}
	}
	
	static int loaded = 0;
	static int toLoad = 13;
	static String step = "Testing loading screen";
	boolean setSize = true;
	float xpbarWidth;
	float xpbarDrawWidth;
	BufferedImage xpbar;
	/**
	 * Main render method.  DO NOT CALL EVER!  Runs every frame.  ONLY use for drawing graphics.
	 */
	
	private boolean optionsApplied = false;
	
	protected void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics b = bs.getDrawGraphics();
		Graphics2D g = (Graphics2D)b;
		
//		if (setSize) {
//			//window.frame.setSize(g., );
//			
//			setSize = false;
//		}
		
		g.setColor(Color.black);
		g.fillRect(0, 0, window.frame.getWidth(), window.frame.getHeight());
		
		g.setColor(Color.yellow);
		
		switch(state) { //Unused loading screen.  Visuals work, but does not function properly due to the nature of the game's engine.
		default: break;
		case FullscreenNotify:
			handler.menus.get("fullscreennotify").render(g);
			world.drawScaledText(g, Localization.get("menu.fullscreenNotify.dialog.0"), 15, 35, 35);
			world.drawScaledText(g, Localization.get("menu.fullscreenNotify.dialog.1"), 15, 70, 35);
			world.drawScaledText(g, Localization.get("menu.fullscreenNotify.dialog.2"), 15, 105, 35);
			
			Cursor.draw(g, handler.input.mouseX, handler.input.mouseY, handler);
			break;
		case Loading:
			g.setColor(Color.white);
			world.drawScaledText(g, NAME, 95, 75, 50);
			world.drawScaledText(g, "LOADING", 300, 350, 20);
			g.setColor(Color.darkGray);
			world.fillScaledRect(g, 95, 360, 518, 20);
			g.setColor(Color.white);
			world.fillScaledRect(g, 95, 360, loaded == toLoad ? 518 : loaded * (518 / toLoad), 20);
			world.drawScaledText(g, step, 95, 395, 15);
			break;
		case Game:
			if (win) {
				g.setColor(Color.green);
				g.drawString("You Win", WIDTH / 2 - 20, HEIGHT / 2);
			} else if (handler.hud.health == 0) {
				g.setColor(Color.red);
				g.drawString("Game Over", WIDTH / 2 - 35, HEIGHT / 2);
			}
			
			handler.render(g);
			handler.hud.render(g);
			
//			g.setColor(Color.green);
//			for (int i = 0; i < handler.object.size(); i++) {
//				GameObject obj = handler.object.get(i);
//				if (obj.getID() == ID.Player) {
//					g.drawLine((int)obj.getX(), (int)obj.getY(), mouse.getX(), mouse.getY());
//				}
//			}
			if (drawHitBoxes) {
//				for (int i = 0; i < handler.object.size(); i++) {
//					GameObject obj = handler.object.get(i);
//					g.setColor(Color.blue);
//					Graphics2D g2 = (Graphics2D) g;
//					
//					
//					
//					g2.draw(obj.getBounds());
//				}
//				Graphics2D g2 = (Graphics2D) g;
//				
//				g.setColor(Color.blue);
//				g2.draw(handler.player.getBounds());
			}
			
			Cursor.draw(g, handler.input.mouseX, handler.input.mouseY, handler);
			break;
		case Main_Menu:
			handler.renderMenu(g, "mainmenu");
			Cursor.draw(g, handler.input.mouseX, handler.input.mouseY, handler);
			break;
		case Start_Menu:
			handler.renderMenu(g, "startmenu");
			world.drawScaledImage(g, handler.sprite.getSprite("start_bg"), (int)(240 * Game.world.ppu), (int)(70 * Game.world.ppu), (int)(465 * Game.world.ppu), (int)(465 * Game.world.ppu));
			world.drawScaledText(g, "Sector "+(areaSelected + 1)+" / "+(handler.stages.size() / 5), 365, 502, 25);
			
			switch(modeSelected) {
			case 0:
				handler.renderMenu(g, "storymap");
				if (handler.menus.get("storymap").options[7].isEnabled())
				
				try {	
					for (int i = 0; i < handler.getStage((areaSelected * 5) + stageSelected).newInstance().description.length; i++) {
						world.drawScaledText(g, handler.getStage((areaSelected * 5) + stageSelected).newInstance().description[i], 480, (i * 13) + 145, 15);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			Cursor.draw(g, handler.input.mouseX, handler.input.mouseY, handler);
			break;
		case Character_Menu:
			world.drawScaledImage(g, handler.sprite.getSprite("character_xpbar_0"), (int)((243.0f) * world.ppu), (int)((205.0f) * world.ppu), (int)((458.0f) * world.ppu), (int)((16) * world.ppu));
			if (handler.getItem(handler.getInv(equipSelected)[0]).level == 15) world.drawScaledImage(g, handler.sprite.getSprite("character_xpbar_1"), (int)((243.0f) * world.ppu), (int)((205.0f) * world.ppu), (int)((458.0f) * world.ppu), (int)((16) * world.ppu));
			else if (xpbarWidth != 1)  world.drawScaledImage(g, xpbar, (int)((243.0f) * world.ppu), (int)((205.0f) * world.ppu), (int)xpbarDrawWidth, (int)((16) * world.ppu)); 
			 
			handler.renderMenu(g, "charactermenu");
			Game.world.drawScaledImage(g, handler.sprite.getSprite("character_bg"), (int)(240 * Game.world.ppu), (int)(70 * Game.world.ppu), (int)(465 * Game.world.ppu), (int)(465 * Game.world.ppu));
			handler.renderMenu(g, "equips");
			handler.renderMenu(g, "upgrades");
			
			g.setColor(Color.white);
			world.drawScaledImage(g, handler.sprite.getSprite("character_infobg"), (int)(15 * world.ppu), (int)(417.5f * world.ppu), (int)(220 * world.ppu), (int)(110 * world.ppu));
			world.drawScaledText(g, "HP", 30, 447, 25);
			world.drawScaledText(g, "EN", 30, 467, 25);
			world.drawScaledText(g, "ENP", 30, 487, 25);
			world.drawScaledText(g, "XPB", 30, 507, 25);
			
			g.setColor(Color.lightGray);
			world.drawScaledText(g, ""+handler.getPlayer().ship.hp, 158, 447, 25);
			world.drawScaledText(g, ""+handler.getPlayer().ship.energy, 158, 467, 25);
			world.drawScaledText(g, ""+(int)(handler.getPlayer().ship.energyPickup * 100)+"%", 158, 487, 25);
			world.drawScaledText(g, ""+handler.getXPBonus()+"%", 158, 507, 25);
			
			int offset = 5;
			if (equipSelected == 0) {
				for (float i = 0.0f; i < handler.getPlayer().ship.stats.size(); i++) {
					if (i >= 0 && i <= 2) {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, handler.getPlayer().ship.stats.get((int) i)[0], 250 + offset, (int)((20.0f * i) + 151.0f), 25);
						g.setColor(Color.lightGray);
						Game.world.drawScaledText(g, handler.getPlayer().ship.stats.get((int) i)[1], 410 + offset, (int)((20.0f * i) + 151.0f), 25);
					} else {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, handler.getPlayer().ship.stats.get((int) i)[0], 475 + offset, (int)((20.0f * (i - 3.0f)) + 151.0f), 25);
						g.setColor(Color.lightGray);
						Game.world.drawScaledText(g, handler.getPlayer().ship.stats.get((int) i)[1], 640 + offset, (int)((20.0f * (i - 3.0f)) + 151.0f), 25);					
					}
				}
				
				g.setColor(Color.white);
				world.drawScaledText(g, "Experience", (int)(255.0f), 219, 20);
				//g.setColor(Color.lightGray);
				
				if (handler.getItem(handler.getInv(equipSelected)[0]).level == 15) world.drawScaledText(g, ""+(handler.getPlayer().ship.xp+xpVals[15]), 415, 219, 20);
				else world.drawScaledText(g, handler.getPlayer().ship.xp+" / "+xpVals[handler.getPlayer().ship.level], 415, 219, 20);
				g.setColor(Color.white);
				
				if (handler.getPlayer().ship.subText != null) { 
					for (float i = 0.0f; i < handler.getPlayer().ship.subText.length; i++) {
						Game.world.drawScaledText(g, "- "+handler.getPlayer().ship.subText[(int)i], 255, (int)((i * 20.0f) + 447.5f), 25);
					}
				}
			} else if (equipSelected == 1) {
				for (float i = 0.0f; i < handler.getPlayer().weapon.stats.size(); i++) {
					if (i >= 0 && i <= 2) {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, handler.getPlayer().weapon.stats.get((int) i)[0], 250 + offset, (int)((20.0f * i) + 151.0f), 25);
						g.setColor(Color.lightGray);
						Game.world.drawScaledText(g, handler.getPlayer().weapon.stats.get((int) i)[1], 410 + offset, (int)((20.0f * i) + 151.0f), 25);
					} else {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, handler.getPlayer().weapon.stats.get((int) i)[0], 475 + offset, (int)((20.0f * (i - 3.0f)) + 151.0f), 25);
						g.setColor(Color.lightGray);
						Game.world.drawScaledText(g, handler.getPlayer().weapon.stats.get((int) i)[1], 640 + offset, (int)((20.0f * (i - 3.0f)) + 151.0f), 25);					
					}
				}
				
				g.setColor(Color.white);
				world.drawScaledText(g, "Experience", (int)(255.0f), 219, 20);
				//g.setColor(Color.lightGray);
				if (handler.getItem(handler.getInv(equipSelected)[0]).level == 15) world.drawScaledText(g, ""+(handler.getPlayer().weapon.xp+xpVals[15]), 415, 219, 20);
				else world.drawScaledText(g, handler.getPlayer().weapon.xp+" / "+xpVals[handler.getPlayer().weapon.level], 415, 219, 20);
				g.setColor(Color.white);
				
				if (handler.getPlayer().weapon.subText != null) { 
					for (float i = 0.0f; i < handler.getPlayer().ship.subText.length; i++) {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, "- "+handler.getPlayer().weapon.subText[(int)i], 255, (int)((i * 20.0f) + 447.5f), 25);
					}
				}
			} else if (equipSelected == 2) {
				for (float i = 0.0f; i < handler.getPlayer().utility.stats.size(); i++) {
					if (i >= 0 && i <= 2) {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, handler.getPlayer().utility.stats.get((int) i)[0], 250 + offset, (int)((20.0f * i) + 151.0f), 25);
						g.setColor(Color.lightGray);
						Game.world.drawScaledText(g, handler.getPlayer().utility.stats.get((int) i)[1], 410 + offset, (int)((20.0f * i) + 151.0f), 25);
					} else {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, handler.getPlayer().utility.stats.get((int) i)[0], 475 + offset, (int)((20.0f * (i - 3.0f)) + 151.0f), 25);
						g.setColor(Color.lightGray);
						Game.world.drawScaledText(g, handler.getPlayer().utility.stats.get((int) i)[1], 640 + offset, (int)((20.0f * (i - 3.0f)) + 151.0f), 25);					
					}
				}
				
				g.setColor(Color.white);
				world.drawScaledText(g, "Experience", (int)(255.0f), 219, 20);
				//g.setColor(Color.lightGray);
				if (handler.getItem(handler.getInv(equipSelected)[0]).level == 15) world.drawScaledText(g, ""+(handler.getPlayer().utility.xp+xpVals[15]), 415, 219, 20);
				else world.drawScaledText(g, handler.getPlayer().utility.xp+" / "+xpVals[handler.getPlayer().utility.level], 415, 219, 20);
				g.setColor(Color.white);
				
				if (handler.getPlayer().utility.subText != null) { 
					for (float i = 0.0f; i < handler.getPlayer().ship.subText.length; i++) {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, "- "+handler.getPlayer().utility.subText[(int)i], 255, (int)((i * 20.0f) + 447.5f), 25);
					}
				}
			} else if (equipSelected == 3) {
				for (float i = 0.0f; i < handler.getPlayer().sup.stats.size(); i++) {
					if (i >= 0 && i <= 2) {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, handler.getPlayer().sup.stats.get((int) i)[0], 250 + offset, (int)((20.0f * i) + 151.0f), 25);
						g.setColor(Color.lightGray);
						Game.world.drawScaledText(g, handler.getPlayer().sup.stats.get((int) i)[1], 410 + offset, (int)((20.0f * i) + 151.0f), 25);
					} else {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, handler.getPlayer().sup.stats.get((int) i)[0], 475 + offset, (int)((20.0f * (i - 3.0f)) + 151.0f), 25);
						g.setColor(Color.lightGray);
						Game.world.drawScaledText(g, handler.getPlayer().sup.stats.get((int) i)[1], 640 + offset, (int)((20.0f * (i - 3.0f)) + 151.0f), 25);					
					}
				}
				
				g.setColor(Color.white);
				world.drawScaledText(g, "Experience", (int)(255.0f), 219, 20);
				//g.setColor(Color.lightGray);
				if (handler.getItem(handler.getInv(equipSelected)[0]).level == 15) world.drawScaledText(g, ""+(handler.getPlayer().sup.xp+xpVals[15]), 415, 219, 20);
				else world.drawScaledText(g, handler.getPlayer().sup.xp+" / "+xpVals[handler.getPlayer().sup.level], 415, 219, 20);
				g.setColor(Color.white);
				
				if (handler.getPlayer().sup.subText != null) { 
					for (float i = 0.0f; i < handler.getPlayer().ship.subText.length; i++) {
						g.setColor(Color.white);
						Game.world.drawScaledText(g, "- "+handler.getPlayer().sup.subText[(int)i], 255, (int)((i * 20.0f) + 447.5f), 25);
					}
				}
			}
			
			Cursor.draw(g, handler.input.mouseX, handler.input.mouseY, handler);
			break;
		case Options:
			handler.renderMenu(g, "options");
			Cursor.draw(g, handler.input.mouseX, handler.input.mouseY, handler);
			break;
		case Options_Controls:
			handler.renderMenu(g, "optionsControls");
			Cursor.draw(g, handler.input.mouseX, handler.input.mouseY, handler);
			break;
		case Pause:
			g.setColor(Color.blue);
			g.drawString("Pause", WIDTH / 2 - 35, HEIGHT / 2);
			
			handler.render(g);
			handler.hud.render(g);
			
			Cursor.draw(g, handler.input.mouseX, handler.input.mouseY, handler);
			break;
		case Level_Select:
			break;
		}
		
//		g.setColor(Color.blue);
//		for (int i = 0; i < world.WORLD_SCALE_X / 5; i++) {
//			g.drawLine((int)(i * world.ppu * 5), 1, (int)(i * world.ppu * 5), HEIGHT);
//		}
//		for (int i = 0; i < world.WORLD_SCALE_Y / 5; i++) {
//			g.drawLine(1, (int)(i * world.ppu * 5), WIDTH, (int)(i * world.ppu * 5));
//		}
		
		g.setColor(Color.white);
		world.drawScaledText(g, "Alpha "+VERSION, 5, 532, 15);
		
		//Clears the render cache, and shows this frame.
		g.dispose();
		bs.show();
	}
	
	//DO NOT TOUCH!!!!
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
		Log.write("Game thread initialized with ID \""+thread.getId()+"\"", "Info");
	}
	
	//DO NOT TOUCH!!!
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
			Log.write("Game thread stopped", "Info");
		} catch(Exception e) {
			Log.write("Failed to stop Game thread "+e.toString(), "Error");
			e.printStackTrace();
		}
	}
	
	//Game Util --------------------------------------------
	
	/**
	 * Toggles between two gives states.
	 * @param state1 The first state.
	 * @param state2 The second state.
	 */
	public static void toggleState(State state1, State state2) {
		if (state == state1) {
			state = state2;
		} else if (state == state2) {
			state = state1;
		}
	}
	
	/**
	 * Updates the mouse cursor's state.  Only used for dissableMouseCursor that i know of.
	 */
	@Deprecated
	public static void updateCursor() {
		if (dissableMouseCursor) {
			window.frame.getContentPane().setCursor(window.newc);
		} else {
			window.frame.getContentPane().setCursor(window.oldc);
		}
	}
	
	public static void setState(State s) {
		state = s;
	}
}
