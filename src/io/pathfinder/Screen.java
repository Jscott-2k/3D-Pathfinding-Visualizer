package io.pathfinder;
import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import io.pathfinder.input.KeyInputEvent;
import io.pathfinder.input.InputHandler;
import io.pathfinder.input.InputManager;

public class Screen extends Canvas implements Runnable {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int TARGET_FPS = 160; 
	
	public static final String TITLE = "3D Pathfinding Visualizer";
	
	private boolean rendering = false;
	private BufferStrategy bufferStrategy;
	private JFrame frame;
	
	private ArrayList<CubeGroup> endCrystals;
	
	private Camera camera;

	private Timer timerFrameDelay, timerSecond;
	
	double frames = 0;
	double actualFPS = 0;
	
	private InputHandler inputHandler;
	private Crosshair crosshair;
	
	public void createDisplay() {
		
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		inputHandler = new InputHandler();
		
		InputManager inputManager = inputHandler.getManager();
		addKeyListener(inputManager);
		addMouseListener(inputManager);
		addMouseMotionListener(inputManager);
		addMouseWheelListener(inputManager);
		setCursor(frame.getToolkit().createCustomCursor(
	            new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
	            "null"));
		frame.add(this);
		frame.pack();
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setTitle(TITLE);
		
		
	}
	
	
	private void loadInputEvents() throws AWTException {
		
		camera.addInputEvents(inputHandler);
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_ESCAPE) {

			@Override
			public void onKeyDown() {
				frame.dispose();
				System.exit(0);
				
			}
			
		});
		inputHandler.getManager().setOnMove((e) -> {			
		});
	}
	
	@Override
	public void run() {

		
		createDisplay();
		int screenCenterX = (int) (frame.getX() + WIDTH / 2.0);
		int screenCenterY = (int) (frame.getY() + HEIGHT / 2.0);

		camera = new Camera();
		crosshair = new Crosshair(screenCenterX, screenCenterY);
		try {
			loadInputEvents();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		crosshair.setSize(10);
		rendering = true;
		endCrystals = new ArrayList<CubeGroup>();		

		for(int i=0;i<16;i++) {
			for(int j=0;j<16;j++) {
				for(int k=0;k<16;k++) {
					endCrystals.add(new CubeGroup(i*12, k*12, -60 - (j * 12)));
				}
			}
		}
		
		timerFrameDelay = new Timer();
		timerSecond = new Timer();
						
		double targetFPSdelay = 1.0 / (double) TARGET_FPS;
			
		long sysTime = Timer.getSystemTime();
		timerFrameDelay.setBeginTime(sysTime);
		timerSecond.setBeginTime(sysTime);
		
		while(rendering) {
			sysTime = Timer.getSystemTime();
			timerFrameDelay.calcDeltaTime();
			timerSecond.calcDeltaTime();
			if(timerFrameDelay.getDeltaTime() > targetFPSdelay) {
				update();
				render();
				frames++;
				timerFrameDelay.setBeginTime(sysTime);	
			}
			updateFramesPerSecond(sysTime);
		}
	}
	
	private void updateFramesPerSecond(long currentSysTime) {
		if(timerSecond.getDeltaTime() >= 1) {
			actualFPS = frames;
			frame.setTitle(TITLE + " - FPS - " + actualFPS);			
			frames = 0;
			timerSecond.setBeginTime(currentSysTime);
		}
	}
	
	private void update() {
		
		for(CubeGroup endCrystal : endCrystals) {
			endCrystal.update();
		}
		
		int screenCenterX = (int) (frame.getX() + WIDTH / 2.0);
		int screenCenterY = (int) (frame.getY() + HEIGHT / 2.0);
		
		crosshair.update(screenCenterX, screenCenterY, camera);
	
		inputHandler.handle();
		
	}

	public void render() {
	
		bufferStrategy = getBufferStrategy();
		if(bufferStrategy == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics graphics = bufferStrategy.getDrawGraphics();
		
		
		graphics.setColor(Color.LIGHT_GRAY);		
		graphics.fillRect(0, 0, WIDTH, HEIGHT);

		camera.createView();
		
		for(CubeGroup endCrystal : endCrystals) {
			endCrystal.render(graphics, camera);
		}
	
		crosshair.render(graphics, WIDTH / 2, HEIGHT / 2);
		
		Font f = new Font("verdana", Font.BOLD, 18);
		graphics.setFont(f);
		graphics.setColor(Color.RED);
		graphics.drawString(camera.getPosition(), 0, HEIGHT - 48);
		
		graphics.dispose();
		bufferStrategy.show();
	}

}