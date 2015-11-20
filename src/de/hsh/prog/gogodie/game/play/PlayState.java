package de.hsh.prog.gogodie.game.play;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import de.hsh.prog.gogodie.game.GameState;
import de.hsh.prog.gogodie.game.actor.Player;
import de.hsh.prog.gogodie.game.gfx.SpriteSheet;

@SuppressWarnings("serial")
public class PlayState extends GameState implements Runnable{

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	
	private PlayBoard board;
	private Player player;
	private SpriteSheet sprite;
	
	public boolean running = false;
	
	public PlayState() {
		sprite = new SpriteSheet("/res/sprite_sheet.png", 16, 16, 10);
		player = new Player(sprite,new Rectangle(16,16,16,16));
		board = new Level1(player);
		addKeyListener(player);
		this.startGame();
	}
	
	@Override
	public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        
        while(running){
        	long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                update();
                delta -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
            	System.out.println(ticks+" "+frames);
                lastTimer += 1000;
                frames = 0;
                ticks = 0;
            }
        }
	}

	public synchronized void startGame(){
		running = true;
		new Thread(this).start();
	}
	
	public synchronized void stopGame(){
		running = false;
	}
	
	private void update(){
		board.update();
	}
	
	private void render(){
		Graphics g = image.getGraphics();
		g.drawImage(board.getBuffer(), 0, 0, this);
		g.dispose();
		this.getGraphics().drawImage(image, 0, 0, this);
	}
}
