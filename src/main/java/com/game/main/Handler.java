package com.game.main;

import java.awt.Font;
import java.awt.Graphics2D;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.game.enums.Control;
import com.game.enums.ID;
import com.game.item.Item;
import com.game.item.ItemShip;
import com.game.item.ItemSuper;
import com.game.item.ItemUtility;
import com.game.item.ItemWeapon;
import com.game.object.Fighter;
import com.game.object.GameObject;
import com.game.object.Player;
import com.game.stage.Stage;
import com.game.ui.HUD;
import com.game.ui.Menu;

public class Handler {
	/**
	 * Internal object list.
	 * Limit:  2000
	 */
	LinkedList<GameObject> object = new LinkedList<GameObject>();
	/**
	 * Internal background object list.
	 * Limit: N/A
	 */
	LinkedList<GameObject> bgobj = new LinkedList<GameObject>();
	/**
	 * List of all registered menus, and their names.
	 */
	public Map<String, Menu> menus = new HashMap<String, Menu>();
	
	public Map<String, Item> items = new HashMap<String, Item>();
	
	public Map<ID, GameObject> enemies = new HashMap<ID, GameObject>();
	
	LinkedList<Class<? extends Stage>> stages = new LinkedList<Class<? extends Stage>>();
	
	
	Player player;
	
	/**
	 * List of object IDs to ignore for quasi layering.
	 */
	List ignore = new ArrayList();
	
	public String[] invShips = new String[]{
			"prospect", "testship1", "testship2", "testship3"
	};
	public String[] invWeapons = new String[]{
			"plasmacannon", "testweapon1", "testweapon2", "testweapon3"
	};
	public String[] invUtilities = new String[]{
			"phaseengine", "testutility1", "testutility2", "testutility3"
	};
	public String[] invSupers = new String[]{
			"hightenergyprism", "testsuper1", "testsuper2", "testsuper3"
	};
	
	public static HUD hud;
	
	/**
	 * Sprite handler.
	 */
	public static SpriteSheet sprite;

//	public static KeyInput keys;
//
//	public static MouseInput mouse;
	
	public static Input input;
	
	public static Font font;
	
	public Game game;

	public Handler(Game game) {
		ignore.add(ID.Bullet);
		ignore.add(ID.EnemyBullet);
		
		this.game = game;

		hud = new HUD(this);
		input = new Input(this);
		sprite = new SpriteSheet();
		
//		 String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//
//	    for ( int i = 0; i < fonts.length; i++ )
//	    {
//	      System.out.println(fonts[i]);
//	    }
		
		//saveSave();
		//writeSave();
		//Create backup save after successful load
    	try {
			loadSave();
			Localization.setLocalization((String)save.get("localization"));
	    	if (!error) { 
	    		Files.copy(Paths.get("save.cfg"), Paths.get("save.bak"), StandardCopyOption.REPLACE_EXISTING); 
	    		Log.write("Save backup created", "Info");
	    	} else {
	    		Log.write("Save file curoption detected!  Reverting to backup save!", "Warning");
	    		Files.copy(Paths.get("save.bak"), Paths.get("save.cfg"), StandardCopyOption.REPLACE_EXISTING); 
	    		loadSave();
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		
		Log.write("Handler initialized", "Info");
	}
	
	public void registerStage(Class<? extends Stage> stage) {
		this.stages.add(stage);
	}
	public Class<? extends Stage> getStage(int id) {
		return this.stages.get(id);
	}
	
	public Class<? extends GameObject> enemyLookup(ID id) {
		switch(id) {
		case Fighter: 
			return Fighter.class;
		default: return Fighter.class;
		}
	}
	
	/**
	 * Moves an item in an inventory.
	 * @param inv The inventory to move the items in. 0 = ship, 1 = weapon, 2 = utility, 3 = super.
	 * @param index1 Index of item 1
	 * @param index2 Index of item 2
	 */
	public void invMove(int inv, int index1, int index2) {
		String[] copy;
		if (inv == 0) {
			copy = invShips.clone();
			copy[index1] = invShips[index2];
			copy[index2] = invShips[index1];
			invShips = copy;
			player.ship = (ItemShip) getItem(invShips[0]);
			//System.out.println(getItem(invShips[0]).level);
		} else if (inv == 1) {
			copy = invWeapons.clone();
			copy[index1] = invWeapons[index2];
			copy[index2] = invWeapons[index1];
			invWeapons = copy;
			player.weapon = (ItemWeapon) getItem(invWeapons[0]);
		} else if (inv == 2) {
			copy = invUtilities.clone();
			copy[index1] = invUtilities[index2];
			copy[index2] = invUtilities[index1];
			invUtilities = copy;
			player.utility = (ItemUtility) getItem(invUtilities[0]);
		} else if (inv == 3) {
			copy = invSupers.clone();
			copy[index1] = invSupers[index2];
			copy[index2] = invSupers[index1];
			invSupers = copy;
			player.sup = (ItemSuper) getItem(invSupers[0]);
		} else throw new IllegalArgumentException("inv value out of range: "+inv);
	}
	
	public String[] getInv(int inv) {
		return inv == 0 ? invShips : inv == 1 ? invWeapons : inv == 2 ? invUtilities : invSupers;
	}
	public Item getEquiped(int inv) {
		return inv == 0 ? player.ship : inv == 1 ? player.weapon : inv == 2 ? player.utility : player.sup;
	}
	
	public void tick() {
		player.tick();
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			
			tempObject.tick();
		}
	}
	
	private String lastMenu;
	private boolean timer = false;
	private int ticks = 0;
	public void tickMenu(String name) {
		lastMenu = name;
		if (name != lastMenu) {
			timer = true;
		}
		if (timer) {
			ticks++;
			if (ticks >= 100) {
				ticks = 0;
				timer = false;
			}
		} else {
			menus.get(name).tick();
		}
	}
	
	public void render(Graphics2D g) {
		if (bgobj.size() > 0) {
			for(int i = 0; i < bgobj.size(); i++) {
				GameObject tempObject =  bgobj.get(i);
				
				try {
					tempObject.render(g);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		//ID[] ids = new ID[]{ID.Bullet, ID.EnemyBullet};
		
		
		
		for (int i = 0; i < object.size(); i++) {
			GameObject obj = object.get(i);
			
			if (ignore.contains(obj.getID())) {
				obj.render(g);
			}
		}
		
		for (int i = 0; i < object.size(); i++) {
			GameObject obj = object.get(i);
			
			if (!ignore.contains(obj.getID())) {
				obj.render(g);
			}
		}
		
		player.render(g);
	}
	
	/**
	 * Registers a menus object.
	 * @param name The string ID of the menu
	 * @param menu The menu object
	 */
	public void registerMenu(String name, Menu menu) {
		//System.out.println("ddd");
		menus.put(name, menu);
		
		Log.write("Registered menu with the name \""+name+"\"", "Info");
	}
	public void registerItem(String name, Item item) {
		items.put(name, item);
		
		Log.write("Registered item with the name \""+name+"\"", "Info");
	}
	public Item getItem(String name) {
		return this.items.get(name);
	}
	
	Map<String, Ticker> tickers = new HashMap<String, Ticker>();
	public void addTicker(String name, int min, int max) {
		tickers.put(name, new Ticker(min, max, name));
	}
	public void addTicker(Ticker ticker) {
		tickers.put("ticker_"+(tickers.size() + 1), ticker);
	}
	public void removeTicker(String name) {
		tickers.remove(name);
	}
	public int getTicker(String name) {
		return tickers.get(name).ticks;
	}
	public Ticker getTicker(String name, Object o) {
		return tickers.get(name);
	}
	public void setTicker(String name, int value) {
		tickers.get(name).ticks = value;
	}
	public void setTickerActive(String name, boolean active) {
		tickers.get(name).active = active;
	}
	public void tickTickers() {
		Object[] keys = tickers.keySet().toArray();;
		
		for (int i = 0; i < keys.length; i++) {
			Ticker ticker = tickers.get(keys[i]);
			if  (ticker.active) {
				ticker.tick();
			}
		}
	}
	
	public void renderMenu(Graphics2D g, String name) {
		//System.out.println(menus.size());
		menus.get(name).render(g);
	}
	
	/**
	 * Returns the internal object list.
	 */
	public LinkedList<GameObject> getObjectList() {
		return this.object;
	}
	
	/**
	 * Returns the size of the internal object list.
	 */
	public int getSize() {
		return this.object.size();
	}
	/**
	 * Adds an object to the game world.
	 * @param object
	 */
	public void addObject(GameObject object) {
		this.object.add(object);
		
		if (object.getID() != ID.Bullet && object.getID() != ID.EnemyBullet)
			Log.write("Added GameObject \""+object.getID()+"\" at X="+object.getX()+", Y="+object.getY(), "Info");
	}
	
	/**
	 * Removes an object from the game world.
	 * @param object The object to remove.
	 */
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
	
	/**
	 * Returns a game object.
	 * @param index The index of the object.
	 * @return GameObject
	 */
	public GameObject getObject(int index) {
		return object.get(index);
	}
	/**
	 * Adds a background object to the world.
	 * @param object The background object to add.
	 */
	public void addBackgroundObject(GameObject object) {
		this.bgobj.add(object);
	}
	/**
	 * Removes a background object from the world.
	 * @param object The background object to remove.
	 */
	public void removeBackgroundObject(GameObject object) {
		this.bgobj.remove(object);
	}
	/**
	 * Returns a background object.
	 * @param index The index of the background object.
	 * @return GameObject background object
	 */
	public GameObject getBackgroundObject(int index) {
		return bgobj.get(index);
	}
	
	/**
	 * Removes all background object from the world.
	 */
	public void clearBackground() {
		bgobj.clear();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Player getPlayer() {
		return this.player;
	}
	
	public Map<String, Object> save = new HashMap<String, Object>();
	
    static boolean error = false;
    public void loadSave() {
    	try {
	    	if (Files.exists(Paths.get("save.cfg"))) {
	    		//Load from existing save file
	    		List<String> ls = Files.readAllLines(Paths.get("save.cfg"));
	    		
	        	for (int line = 0; line < ls.size(); line++) for (int sep = 0; sep < ls.get(line).length(); sep++) if (ls.get(line).charAt(sep) == '=') {
	        		String name = ls.get(line).substring(0, sep), value = ls.get(line).substring(sep + 1);
	        		
	        		if (value.charAt(0) == '#' || value.charAt(0) == ' ') {
	        			//Is a comment, or empty
	        			
	        		} else if (value.charAt(0) == '0' || value.charAt(0) == '1' || value.charAt(0) == '2' || value.charAt(0) == '3' || value.charAt(0) == '4' || value.charAt(0) == '5' || value.charAt(0) == '6' || value.charAt(0) == '7' || value.charAt(0) == '8' || value.charAt(0) == '9') {
	        			//Value is a number
	        			if (value.contains(".")) {
	        				//Is a double or float
	        				if (value.contains("d") || value.contains("D")) {
	        					//Is a double
	        					save.put(name, Double.parseDouble(value.substring(0, value.length() - 1)));
	        				} else if (value.contains("f") || value.contains("F")) {
	        					//Is a float
	        					save.put(name, Float.parseFloat(value.substring(0, value.length() - 1)));
	        				}
	        			} else {
	        				//Is an integer
	        				save.put(name, Integer.decode(value));
	        			}
	        		} else {
	        			//Is a string or boolean
	        			if (value.contains("true") || value.contains("false")) {
	        				//Is a boolean
	        				save.put(name, Boolean.parseBoolean(value));
	        			} else {
	        				//Is a string
	        				save.put(name, value);
	        			}
	        		}
		        	
		        	Log.write("\t[" + save.get(name).getClass().getName() + "] " + name + " = " + save.get(name), "Info");
	        	}
	    	} else if (Files.exists(Paths.get("save.bak"))) {
	    		//Save file was not found; reverting to backup
	    		Log.write("Save file not found!  Reverting to backup save!", "Warning");
	    		Files.copy(Paths.get("save.bak"), Paths.get("save.cfg"), StandardCopyOption.REPLACE_EXISTING); 
	    		loadSave();
	    	} else {
	    		//Create new save file, and recur
	        	List<String> lines = Arrays.asList(
	        		//Options
	        		"#Options",
	        		"resolution=0",
	        		"fullscreen=0",
	        		"musicvol=0",
	        		"soundvol=0",
	        		"hudScale=0",
	        		"enemyHealthBars=0",
	        		"enableCursor=1",
	        		"cursorStyle=0",
	        		"cursorSize=0",
	        		"showfps=0",
	        		"damageNumbers=0",
	        		"localization=en_US",
	        		"lockMouse=0",
	        		"",
	        		
	        		//Controls
	        		"#Controls",
	        		"control_useWeapon=1",
	        		"control_useUtility=3",
	        		"control_useSuper=37",
	        		"control_moveUp=92",
	        		"control_moveLeft=70",
	        		"control_moveDown=88",
	        		"control_moveRight=73",
	        		"",
	        		
	        		//Character
	        		"#Character",
	        		"ship=prospect",
	        		"weapon=plasmacannon",
	        		"utility=phaseengine",
	        		"super=hightenergyprism",
	        		"",
	        		
	        		//Items
	        		"#Items",
	        		"ship0_level=1",
	        		"ship0_xp=0",
	        		"ship0_t1u1=false",
	        		"ship0_t1u2=false",
	        		"ship0_t2u1=false",
	        		"ship0_t2u2=false",
	        		"ship0_master=false",

	        		"ship1_level=1",
	        		"ship1_xp=0",
	        		"ship1_t1u1=false",
	        		"ship1_t1u2=false",
	        		"ship1_t2u1=false",
	        		"ship1_t2u2=false",
	        		"ship1_master=false",

	        		"ship2_level=1",
	        		"ship2_xp=0",
	        		"ship2_t1u1=false",
	        		"ship2_t1u2=false",
	        		"ship2_t2u1=false",
	        		"ship2_t2u2=false",
	        		"ship2_master=false",

	        		"ship3_level=1",
	        		"ship3_xp=0",
	        		"ship3_t1u1=false",
	        		"ship3_t1u2=false",
	        		"ship3_t2u1=false",
	        		"ship3_t2u2=false",
	        		"ship3_master=false",

	        		"weapon0_level=1",
	        		"weapon0_xp=0",
	        		"weapon0_t1u1=false",
	        		"weapon0_t1u2=false",
	        		"weapon0_t2u1=false",
	        		"weapon0_t2u2=false",
	        		"weapon0_master=false",

	        		"weapon1_level=1",
	        		"weapon1_xp=0",
	        		"weapon1_t1u1=false",
	        		"weapon1_t1u2=false",
	        		"weapon1_t2u1=false",
	        		"weapon1_t2u2=false",
	        		"weapon1_master=false",

	        		"weapon2_level=1",
	        		"weapon2_xp=0",
	        		"weapon2_t1u1=false",
	        		"weapon2_t1u2=false",
	        		"weapon2_t2u1=false",
	        		"weapon2_t2u2=false",
	        		"weapon2_master=false",

	        		"weapon3_level=1",
	        		"weapon3_xp=0",
	        		"weapon3_t1u1=false",
	        		"weapon3_t1u2=false",
	        		"weapon3_t2u1=false",
	        		"weapon3_t2u2=false",
	        		"weapon3_master=false",

	        		"utility0_level=1",
	        		"utility0_xp=0",
	        		"utility0_t1u1=false",
	        		"utility0_t1u2=false",
	        		"utility0_t2u1=false",
	        		"utility0_t2u2=false",
	        		"utility0_master=false",

	        		"utility1_level=1",
	        		"utility1_xp=0",
	        		"utility1_t1u1=false",
	        		"utility1_t1u2=false",
	        		"utility1_t2u1=false",
	        		"utility1_t2u2=false",
	        		"utility1_master=false",

	        		"utility2_level=1",
	        		"utility2_xp=0",
	        		"utility2_t1u1=false",
	        		"utility2_t1u2=false",
	        		"utility2_t2u1=false",
	        		"utility2_t2u2=false",
	        		"utility2_master=false",

	        		"utility3_level=1",
	        		"utility3_xp=0",
	        		"utility3_t1u1=false",
	        		"utility3_t1u2=false",
	        		"utility3_t2u1=false",
	        		"utility3_t2u2=false",
	        		"utility3_master=false",

	        		"super0_level=1",
	        		"super0_xp=0",
	        		"super0_t1u1=false",
	        		"super0_t1u2=false",
	        		"super0_t2u1=false",
	        		"super0_t2u2=false",
	        		"super0_master=false",

	        		"super1_level=1",
	        		"super1_xp=0",
	        		"super1_t1u1=false",
	        		"super1_t1u2=false",
	        		"super1_t2u1=false",
	        		"super1_t2u2=false",
	        		"super1_master=false",

	        		"super2_level=1",
	        		"super2_xp=0",
	        		"super2_t1u1=false",
	        		"super2_t1u2=false",
	        		"super2_t2u1=false",
	        		"super2_t2u2=false",
	        		"super2_master=false",

	        		"super3_level=1",
	        		"super3_xp=0",
	        		"super3_t1u1=false",
	        		"super3_t1u2=false",
	        		"super3_t2u1=false",
	        		"super3_t2u2=false",
	        		"super3_master=false"
	        	);
	    		Files.write(Paths.get("save.cfg"), lines, Charset.forName("UTF-8"));
	    		loadSave();
	    		return;
	    	}
    	} catch (Exception e) {
    		error = true;
    		e.printStackTrace();
    	}
    }
    
    public void saveSave() {
    	try {
    		List<String> lines = new ArrayList();
    		Object[] keys = save.keySet().toArray();
    		for (int i = 0; i < save.size(); i++) {
    			if (save.get(keys[i]) instanceof Double) {
    				lines.add(keys[i] + "=" + save.get(keys[i]) + "d");
    			} else if (save.get(keys[i]) instanceof Float) {
    				lines.add(keys[i] + "=" + save.get(keys[i]) + "f");
    			} else if (save.get(keys[i]) instanceof Integer || save.get(keys[i]) instanceof String || save.get(keys[i]) instanceof Boolean) {
    				lines.add(keys[i] + "=" + save.get(keys[i]));
    			}
    		}
    		Files.write(Paths.get("save.cfg"), lines, Charset.forName("UTF-8"));
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
	
	
//	private Map<String, Tag> savebk;
//	private void loadSave() {
//		Log.write("Load save", "Debug");
//		savebk = save; //Backup save in case an error occurs loading the save
//		save.clear();
//		try {
//			NBTInputStream input = new NBTInputStream(Files.newInputStream(Paths.get("save.nbt"), StandardOpenOption.READ));
//			for (int j = 0; j < 20; j++) {
//				Tag t = input.readTag();
//				save.put(t.getName(), t);
//				if (Log.doOutput)
//				System.out.println(t.getName() + " = "+ t.getValue());
//			}
//			input.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			save = savebk;
//		}
//	}
//	void saveSave() {
//		Log.write("Save save", "Debug");
//		//save = writeSave();
//		try {
//			NBTOutputStream nbt = new NBTOutputStream(Files.newOutputStream(Paths.get("save.nbt"), StandardOpenOption.CREATE));
//			Object[] keys = save.keySet().toArray();
//			
//			for (int i = 0; i < keys.length; i++) {
//				nbt.writeTag(save.get(keys[i]));
//				if (Log.doOutput)
//				System.out.println(save.get(keys[i]).getName()+" = "+save.get(keys[i]).getValue());
//			}
//			nbt.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	private Map<String, Tag> writeSave() {
//		Map<String, Tag> map = new HashMap<String, Tag>();
//		
//		map.put("ship", new StringTag("ship", "prospect"));
//		map.put("weapon", new StringTag("weapon", "plasmacannon"));
//		map.put("utility", new StringTag("utility", "phaseengine"));
//		map.put("sup", new StringTag("sup", "hightenergyprism"));
//		
//		Map<String, Tag> items = new HashMap<String, Tag>();
//			item("prospect", items, 1);
//			item("testship1", items, 2);
//			item("testship2", items, 7);
//			item("testship3", items, 3);
//			
//			item("plasmacannon", items, 1);
//			item("testweapon1", items, 4);
//			item("testweapon2", items, 4);
//			item("testweapon3", items, 9);
//			
//			item("phaseengine", items, 1);
//			item("testutility1", items, 10);
//			item("testutility2", items, 15);
//			item("testutility3", items, 2);
//			
//			item("hightenergyprism", items, 1);
//			item("testsuper1", items, 5);
//			item("testsuper2", items, 6);
//			item("testsuper3", items, 11);
//		map.put("items", new CompoundTag("items", items));
//		
//		map.put("resolution", new IntTag("resolution", 0));
//		map.put("fullscreen", new IntTag("fullscreen", 0));
//		map.put("quality", new IntTag("quality", 0));
//		map.put("musicvol", new IntTag("musicvol", 0));
//		map.put("soundvol", new IntTag("soundvol", 0));
//		map.put("customcursor", new IntTag("customcursor", 0));
//		map.put("cursorstyle", new IntTag("cursorstyle", 0));
//		map.put("cursorsize", new IntTag("cursorsize", 0));
//		map.put("hudscale", new IntTag("hudscale", 0));
//		map.put("hudfade", new IntTag("hudfade", 0));
//		map.put("enemyhealthbars", new IntTag("enemyhealthbars", 0));
//		map.put("hudnumbers", new IntTag("hudnumbers", 0));
//		map.put("damagenumbers", new IntTag("damagenumbers", 0));
//		map.put("showfps", new IntTag("showfps", 0));
//		
//		Map<String, Tag> controls = new HashMap<String, Tag>();
//			controls.put("moveUp", new IntTag("moveUp", KeyEvent.VK_W + 5));
//			controls.put("moveDown", new IntTag("moveDown", KeyEvent.VK_S + 5));
//			controls.put("moveLeft", new IntTag("moveLeft", KeyEvent.VK_A + 5));
//			controls.put("moveRight", new IntTag("moveRight", KeyEvent.VK_D + 5));
//			
//			controls.put("useWeapon", new IntTag("useWeapon", MouseEvent.BUTTON1));
//			controls.put("useUtility", new IntTag("useUtility", MouseEvent.BUTTON2));
//			controls.put("useSuper", new IntTag("useSuper", KeyEvent.VK_SPACE + 5));
//			
//			controls.put("pause", new IntTag("pause", KeyEvent.VK_ESCAPE + 5));
//		map.put("controls", new CompoundTag("controls", controls));
//		
//		return map;
//	}
//	
//	private void item(String name, Map map, int level) {
//		Map<String, Tag> item = new HashMap<String, Tag>();
//		item.put("level", new IntTag("level", level));
//		item.put("xp", new IntTag("xp", 0));
//		item.put("color", new IntTag("color", 0));
//		item.put("t1u1", new IntTag("t1u1", 0));
//		item.put("t1u2", new IntTag("t1u2", 0));
//		item.put("t2u1", new IntTag("t2u1", 0));
//		item.put("t2u2", new IntTag("t2u2", 0));
//		item.put("master", new IntTag("master", 0));
//		map.put(name, new CompoundTag(name, item));
//	}

	public int getXPBonus() {
		double multi = 1.0d;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				//getItem(getInv(i)[j]).level = 15;
				if (getItem(getInv(i)[j]).upgrades[4]) {
					multi += 0.1f;
					//System.out.println("true");
				}
			}
		}
		//System.out.println(multi);
		return (int)(multi * 100.0d);
	}
	
	public int getControl(Control c) {
		if (c == Control.USE_WEAPON) {
			return (int)save.get("control_useWeapon");
		} else if (c == Control.USE_UTILITY) {
			return (int)save.get("control_useUtility");
		} else if (c == Control.USE_SUPER) {
			return (int)save.get("control_useSuper");
		} else if (c == Control.MOVE_UP) {
			return (int)save.get("control_moveUp");
		} else if (c == Control.MOVE_DOWN) {
			return (int)save.get("control_moveDown");
		} else if (c == Control.MOVE_RIGHT) {
			return (int)save.get("control_moveRight");
		} else if (c == Control.MOVE_LEFT) {
			return (int)save.get("control_moveLeft");
		} else if (c == Control.PAUSE) {
			return 32;
		} else return -1;
		
	}
	public boolean isPressed(Control c) {
		return input.isButtonDown(getControl(c));
	}
	
//	public void backupSave() {
//		savebk = save;
//	}
}
