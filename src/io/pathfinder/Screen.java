package io.pathfinder;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import io.pathfinder.input.KeyInputEvent;
import io.pathfinder.math.Vector3d;
import io.pathfinder.menu.CustomMenuBar;
import io.pathfinder.input.InputHandler;
import io.pathfinder.input.InputManager;

/**
 * 
 * Singleton Java AWT Canvas for 3D Pathfinder. Displays everything for the 3D
 * pathfinder, acting as the main container Calls all the renders and updates.
 * 
 * TODO: Decouple JFrame and follow more closely to MVC design
 * 
 * @author Justin Scott
 *
 */
public class Screen extends Canvas implements Runnable {

	private ScreenConfiguration screenConfiguration;

	private boolean rendering = false;
	private BufferStrategy bufferStrategy;
	private BufferedImage backgroundImage;

	private JFrame frame;
	private Camera camera;

	private Timer timerFrameDelay, timerSecond;

	double frames = 0;
	double actualFPS = 0;

	private static int GRID_SIZE = 6;
	private final static int MIN_GRID_SIZE = 2;
	private final static int MAX_GRID_SIZE = 16;

	private InputHandler inputHandler;
	private Crosshair crosshair;
	private CubicGridSaveManager cubicGridSaveManager;
	private Cube testCube;

	private boolean leftMouseDown = false;

	private boolean editMode = false;
	private int editY = 0;

	private CustomMenuBar menuBar;

	private TextRenderer textRenderer;
	private static final Font DEBUG_FONT = new Font("verdana", Font.BOLD, 16);

	private static Screen screen;
	private boolean update;

	private long frameTick = 0;

	private Screen() {
	}

	public int getScreenWidth() {
		return frame.getWidth();
	}

	public int getScreenHeight() {
		return frame.getHeight();
	}

	private void initText() {
		textRenderer = new TextRenderer(32);
		int startY = 84;
		for (int i = 0; i < 5; i++) {
			Text text = new Text(DEBUG_FONT, null, Color.RED, 8, startY - (i * 16));
			textRenderer.add(text);
		}
	}

	private void updateText() {
		textRenderer.getTextObject(0).setText("Grid Size: " + String.valueOf(GRID_SIZE));
		textRenderer.getTextObject(1).setText("Edit Mode: " + String.valueOf(this.editMode));
		textRenderer.getTextObject(2).setText("Edit Level: " + String.valueOf(this.editY));
		textRenderer.getTextObject(3).setText("FPS: " + String.valueOf(this.actualFPS));
		textRenderer.getTextObject(4).setText("Camera Pos: " + camera.getPositionAsString());

		if (editMode) {
			textRenderer.setAllX((getWidth() / 2) + 8);
		} else {

			textRenderer.setAllX(8);
		}

	}

	public void createDisplay() {

		frame = new JFrame();

		screenConfiguration = ScreenConfiguration.getDefaultConfiguration();
		frame.setPreferredSize(new Dimension(screenConfiguration.getWidth(), screenConfiguration.getHeight()));
		inputHandler = new InputHandler();

		InputManager inputManager = inputHandler.getManager();
		addKeyListener(inputManager);
		addMouseListener(inputManager);
		addMouseMotionListener(inputManager);
		addMouseWheelListener(inputManager);

		setCursor(getToolkit().createCustomCursor(new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
				"null"));
		frame.add(this);
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar = new CustomMenuBar();
		frame.setJMenuBar(menuBar);
		frame.validate();

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setTitle(screenConfiguration.getTitle());

	}

	private void loadInputEvents() throws AWTException {
		Screen myScreen = this;
		camera.addInputEvents(inputHandler);
		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_ESCAPE) {
			@Override
			public void onKeyDown() {
				frame.dispose();
				System.exit(0);
			}

			@Override
			public void onKeyRelease() {

			}
		});

		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_C) {
			@Override
			public void onKeyDown() {

			}

			@Override
			public void onKeyRelease() {
				crosshair.toggle(myScreen);
			}
		});

		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_Q) {

			@Override
			public void onKeyDown() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onKeyRelease() {

				int tempSize = GRID_SIZE - 1;

				if (tempSize < MIN_GRID_SIZE) {
					return;
				}

				if (editY >= GRID_SIZE - 1) {
					editY = GRID_SIZE - 1;
				}

				GRID_SIZE = tempSize;
				cubicGridSaveManager.setCubicGrid(new CubicGrid(GRID_SIZE, 12, myScreen));
				System.out.println("Set Grid Size: " + GRID_SIZE);

			}

		});

		inputHandler.addEvent(new KeyInputEvent(KeyEvent.VK_E) {

			@Override
			public void onKeyDown() {

			}

			@Override
			public void onKeyRelease() {
				int tempSize = GRID_SIZE + 1;

				if (tempSize > MAX_GRID_SIZE) {
					return;
				}

				GRID_SIZE = tempSize;

				if (editY >= GRID_SIZE - 1) {
					editY = GRID_SIZE - 1;
				}

				cubicGridSaveManager.setCubicGrid(new CubicGrid(GRID_SIZE, 12, myScreen));
				System.out.println("Set Grid Size: " + GRID_SIZE);
			}
		});

		InputManager inputManager = inputHandler.getManager();

		inputManager.setOnMove((e) -> {

			if (editMode) {
				SquareGrid squareGrid = cubicGridSaveManager.getCubicGrid().getSquareGrid(editY);
				squareGrid.onMouseMove(getMousePosition());
			}

		});
		inputManager.setOnRightPress((e) -> {
		});
		inputManager.setOnLeftPress((e) -> {
			leftMouseDown = true;
		});
		inputManager.setOnLeftRelease((e) -> {

			leftMouseDown = false;

			if (editMode) {
				SquareGrid squareGrid = cubicGridSaveManager.getCubicGrid().getSquareGrid(editY);
				squareGrid.onLeftRelease(getMousePosition());
			}
		});
		inputManager.setOnRightRelease(e -> {
			if (editMode) {
				SquareGrid squareGrid = cubicGridSaveManager.getCubicGrid().getSquareGrid(editY);
				squareGrid.onRightRelease(getMousePosition());
			}
		});

		inputManager.setOnMouseWheelMoveDown(e -> {
			if (editMode) {
				int tempZ = editY + 1;
				editY = tempZ > GRID_SIZE - 1 ? GRID_SIZE - 1 : tempZ;
			}
		});
		inputManager.setOnMouseWheelMoveUp(e -> {

			if (editMode) {
				int tempZ = editY - 1;
				editY = tempZ < 0 ? 0 : tempZ;
			}
		});

	}

	private int getScreenCenterX() {
		return (int) (frame.getLocation().getX() + (double) (getWidth() / 2.0));
	}

	private int getScreenCenterY() {
		return (int) (frame.getLocation().getY()
				+ (double) ((getHeight() / 2.0) + ((getScreenHeight()) - getHeight())));
	}

	@Override
	public void run() {

		createDisplay();
		System.out.println("Display created!");
		initText();

		int screenCenterX = getScreenCenterX();
		int screenCenterY = getScreenCenterY();

		camera = new Camera(1, 1, 300, 80, screenConfiguration);

		crosshair = new Crosshair(screenCenterX, screenCenterY, frame.getToolkit());

		try {
			loadInputEvents();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		try {
			backgroundImage = ImageIO.read(new File("back.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rendering = true;
		update = true;
		cubicGridSaveManager = CubicGridSaveManager.getCubicGridSaver();
		cubicGridSaveManager.setCubicGrid(new CubicGrid(GRID_SIZE, 12, this));

		testCube = new Cube(Color.RED, 8, new Vector3d(), new Vector3d());
		testCube.translate(0, 0, 30);

		timerFrameDelay = new Timer();
		timerSecond = new Timer();

		double targetFPSdelay = 1.0 / (double) screenConfiguration.getTargetFPS();

		long sysTime = Timer.getSystemTime();
		timerFrameDelay.setBeginTime(sysTime);
		timerSecond.setBeginTime(sysTime);

		while (rendering) {
			sysTime = Timer.getSystemTime();
			timerFrameDelay.calcDeltaTime();
			timerSecond.calcDeltaTime();
			if (timerFrameDelay.getDeltaTime() > targetFPSdelay) {

				if (update) {
					update();
					render();
					frameTick++;
				}
				frames++;
				timerFrameDelay.setBeginTime(sysTime);
			}
			updateFramesPerSecond(sysTime);
		}
	}

	public long getFrameTick() {
		return frameTick;
	}

	private void updateFramesPerSecond(long currentSysTime) {
		if (timerSecond.getDeltaTime() >= 1) {
			actualFPS = frames;
			frame.setTitle(screenConfiguration.getTitle());
			frames = 0;
			timerSecond.setBeginTime(currentSysTime);
		}
	}

	private void update() {

		editMode = menuBar.getEditMenu().getEdit().isSelected();

		if (editMode) {
			if (!camera.isSplitScreen()) {
				camera.setSplitScreen(true);
			}
		} else {
			if (camera.isSplitScreen()) {
				camera.setSplitScreen(false);
			}
		}

		int screenCenterX = getScreenCenterX();
		int screenCenterY = getScreenCenterY();

		if (camera.isSplitScreen()) {
			screenCenterX += (getWidth() / 4.0);
		}

		crosshair.update(screenCenterX, screenCenterY, camera);
		CubicGrid grid = cubicGridSaveManager.getCubicGrid();
		if (grid != null) {
			grid.update();
		}

		inputHandler.handle();
		updateText();

	}

	public void render() {

		bufferStrategy = getBufferStrategy();
		if (bufferStrategy == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics graphics = bufferStrategy.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) graphics;
		g2d.setStroke(new BasicStroke(1));

		if (backgroundImage != null) {
			int w = backgroundImage.getWidth(null);
			int h = backgroundImage.getHeight(null);
			g2d.drawImage(backgroundImage, 0, 0, null);
		} else {

			graphics.setColor(new Color(0, 4, 26));
			graphics.fillRect(0, 0, getScreenWidth(), getScreenHeight());
		}
		// testCube.render(graphics, camera);

		if (editMode) {
			SquareGrid squareGrid = cubicGridSaveManager.getCubicGrid().getSquareGrid(editY);

			g2d = (Graphics2D) graphics;
			squareGrid.render(g2d);
		}
		camera.createView();
		CubicGrid cubicGrid = cubicGridSaveManager.getCubicGrid();
		if (cubicGrid != null) {
			cubicGrid.render(graphics, camera);
		} else {
			System.out.println("NULL GRID!");
		}

		crosshair.render(graphics, camera.isSplitScreen() ? getWidth() / 2 + (getWidth() / 4) : getWidth() / 2,
				getHeight() / 2);

		textRenderer.render(graphics);

		if (editMode) {

			g2d = (Graphics2D) graphics;
			g2d.setColor(Color.BLUE);
			g2d.setStroke(new BasicStroke(3));
			graphics.drawLine((int) (getWidth() / 2.0), 0, (int) (getWidth() / 2.0), getHeight());
		}

		graphics.dispose();
		bufferStrategy.show();
	}

	public static Screen getScreen() {
		if (screen == null) {
			screen = new Screen();
		}
		return screen;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public void setGridSize(int gridSize) {

		if (gridSize <= MAX_GRID_SIZE && gridSize >= MIN_GRID_SIZE) {
			System.out.println("Setting Size: " + gridSize);
			GRID_SIZE = gridSize;
		}

	}

}