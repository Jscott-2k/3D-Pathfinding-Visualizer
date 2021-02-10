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
	public static final int TARGET_FPS = 24; 
	
	public static final String TITLE = "3D Pathfinding Visualizer";
	
	private boolean rendering = false;
	private BufferStrategy bufferStrategy;
	private JFrame frame;
	
	private ArrayList<EndCrystal> endCrystals;
	
	private Camera camera;

	private Timer timerFrameDelay, timerSecond;
	
	double frames = 0;
	double actualFPS = 0;
	
	private InputHandler inputHandler;
	private Crosshair crosshair;
	private Robot robot;
	private int lx = 0, ly = 0;
	
	
	
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
	            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
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
		robot = new Robot();


		
		camera.addInputEvents(inputHandler);
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_ESCAPE) {

			@Override
			public void onKeyDown() {
				frame.dispose();
				System.exit(0);
				
			}
			
		});
		inputHandler.getManager().setOnMove((e) -> {
//			int x = e.getXOnScreen();
//			int y = e.getYOnScreen();
//			
//			int dx = x - lx;
//			int dy = y - ly;
//			camera.rotate(dx, dy);
//			System.out.println(dx);
//			lx = x;
//			ly = y;
//
//			if(Math.abs(dx) > 0 && Math.abs(dy) > 0) {
//				//robot.mouseMove(cx, cy);
//			}
			
		});
	}
	
	@Override
	public void run() {

		
		createDisplay();


		camera = new Camera();
		crosshair = new Crosshair();
		try {
			loadInputEvents();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		crosshair.setSize(10);
		rendering = true;
		endCrystals = new ArrayList<EndCrystal>();		

		for(int i=0;i<12;i++) {
			for(int j=0;j<8;j++) {
				endCrystals.add(new EndCrystal(i*12, 0, -60 - (j * 12)));

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
		for(EndCrystal endCrystal : endCrystals) {
			endCrystal.update();
		}
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		

		
			int x = MouseInfo.getPointerInfo().getLocation().x;
			int y = MouseInfo.getPointerInfo().getLocation().y;
			
			int cx = (int) (frame.getX() + WIDTH / 2.0);
			int cy = (int) (frame.getY() + HEIGHT / 2.0);
			
			int dx = x - lx;
			int dy = y - ly;
			
			
			camera.rotate(dx, dy);
			
			System.out.println(dx);
			
			lx = x;
			ly = y;
	
			if(Math.abs(dx) > 1 || Math.abs(dy) > 1) {
				robot.mouseMove(cx, cy);
				lx = cx;
				ly = cy;
			}

		
		
		inputHandler.handle();
		
	}

	public void render() {
	
		bufferStrategy = getBufferStrategy();
		if(bufferStrategy == null) {
			createBufferStrategy(4);
			return;
		}
		Graphics graphics = bufferStrategy.getDrawGraphics();
		
		
		graphics.setColor(Color.BLACK);		
		graphics.fillRect(0, 0, WIDTH, HEIGHT);

		camera.createView();
		
		for(EndCrystal endCrystal : endCrystals) {
			endCrystal.render(graphics, camera);
		}
	
		crosshair.render(graphics, WIDTH / 2, HEIGHT / 2);
		
		Font f = new Font("verdana", Font.PLAIN, 16);
		graphics.setFont(f);
		graphics.setColor(Color.YELLOW);
		graphics.drawString(camera.getPosition(), 0, HEIGHT - 48);
		
		graphics.dispose();
		bufferStrategy.show();
	}
	
	@Override
	public void paint(Graphics g) {


	}
}