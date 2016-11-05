package ag.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class AggressionSequence extends BasicGame {

	public AggressionSequence(String title) {
		super(title);
	}
	
	public GraphicsHandler gh;
	public SoundHandler sh;
	
	
	@Override
	public void init(GameContainer container) throws SlickException {
		this.gh = new GraphicsHandler();
		this.sh = new SoundHandler();
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		
		
	}
	
	public static void main(String[] args) {
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new AggressionSequence("Aggression Sequence"));
			appgc.setShowFPS(false);
			appgc.setDisplayMode(640, 480, false);
			appgc.setMinimumLogicUpdateInterval(50);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(AggressionSequence.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
