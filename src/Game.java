import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Game extends JPanel implements Runnable {

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 750;

	public boolean running = false;
	private Thread thread;
	public boolean pause = true;

	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	SnakeBoard snakeboard;

	public Game() {
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);
		snakeboard = new SnakeBoard();
	}

	public static void addKeyBindings(JComponent comp, int keyCode, String id, ActionListener lambda) {
		InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke(keyCode, 0, false), id);
		ActionMap ap = comp.getActionMap();
		ap.put(id, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lambda.actionPerformed(e);
			}
		});
	}

	public void run() {
		double oneSecond = 1000000000.0;
		double frames = 15;
		long start, end, time;
		double fpsTimer = oneSecond / frames;
		start = System.nanoTime();
		boolean render = false;
		long process = 0;
		while (running) {
			end = System.nanoTime();
			time = end - start;
			process += time;
			start = end;
			addKeyBindings(this, KeyEvent.VK_SPACE, "Space", (evt) -> {
				if (pause) {
					pause = false;
					if (snakeboard.dead) {
						snakeboard.reset();
					}
				} else if (!pause) {
					pause = true;
				}
			});
			if (process >= fpsTimer) {
				if (!pause) {
					update();
				}
				render();
				process = 0;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void AI() {
		pause = false;
		int x = snakeboard.point.x;
		int y = snakeboard.point.y;
		if (snakeboard.headSnake.dir == Direction.DOWN) {
			if (snakeboard.headSnake.getY() > y) {
				snakeboard.headSnake.setDirection(Direction.RIGHT);
			}
		}
	}

	private void drawStartScreen() {
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.black);
		g.setFont(new Font("Consolas", Font.PLAIN, 28));
		g.drawString("Use arrow keys to move. Press Space to start.", 175, 300);
		g.dispose();
	}

	private void render() {
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setFont(new Font("Consolas", Font.PLAIN, 20));
		g.setColor(Color.black);
		g.drawString("Score: " + String.valueOf(snakeboard.score), 50, 710);
		g.drawString("HighScore: " + String.valueOf(snakeboard.highscore), 300, 710);
		g.drawString("Press Space to Pause", 675, 710);
		snakeboard.render(g);
		if (pause && !snakeboard.dead) {
			drawStartScreen();
		}
		g.dispose();
		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.drawImage(image, 0, 0, null);
	}

	private void update() {
		if (snakeboard.dead) {
			pause = true;
		} else {
			addKeyBindings(this, KeyEvent.VK_LEFT, "Left", (evt) -> {
				if (snakeboard.headSnake.moveBack
						|| (!snakeboard.headSnake.moveBack && snakeboard.headSnake.dir != Direction.RIGHT)) {
					snakeboard.headSnake.setDirection(Direction.LEFT);
					snakeboard.headSnake.setDirChange(true);
				}

			});
			addKeyBindings(this, KeyEvent.VK_RIGHT, "Right", (evt) -> {
				if (snakeboard.headSnake.moveBack
						|| (!snakeboard.headSnake.moveBack && snakeboard.headSnake.dir != Direction.LEFT)) {
					snakeboard.headSnake.setDirection(Direction.RIGHT);
					snakeboard.headSnake.setDirChange(true);
				}

			});
			addKeyBindings(this, KeyEvent.VK_UP, "Up", (evt) -> {
				if (snakeboard.headSnake.moveBack
						|| (!snakeboard.headSnake.moveBack && snakeboard.headSnake.dir != Direction.DOWN)) {
					snakeboard.headSnake.setDirection(Direction.UP);
					snakeboard.headSnake.setDirChange(true);
				}

			});
			addKeyBindings(this, KeyEvent.VK_DOWN, "Down", (evt) -> {
				if (snakeboard.headSnake.moveBack
						|| (!snakeboard.headSnake.moveBack && snakeboard.headSnake.dir != Direction.UP)) {
					snakeboard.headSnake.setDirection(Direction.DOWN);
					snakeboard.headSnake.setDirChange(true);
				}

			});
			snakeboard.update();
		}

	}

	public synchronized void start() {
		running = true;
		snakeboard.reset();
		thread = new Thread(this, "game");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		System.exit(0);
	}

}
