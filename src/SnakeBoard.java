import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SnakeBoard {
	private static final int WIDTH = 960;
	private static final int HEIGHT = 660;
	public static final int border = 30;
	private static final int ROWS = 50;
	private static final int COLS = 80;
	public static int[][] board;
	public static final int squareSize = 15;
	public boolean dead = false;
	public boolean dirChange = false;

	public Point point;
	private BufferedImage snakeBoard;
	private BufferedImage finalBoard;

	public int score;
	public int highscore=0;
	public Snake headSnake;
	public LinkedList<Snake> snakeList;

	public SnakeBoard() {
		snakeBoard = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		finalBoard = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		snakeList = new LinkedList<Snake>();
	}

	private void drawBoard() {
		Graphics2D g = (Graphics2D) snakeBoard.getGraphics();
		g.setColor(Color.green);
		g.fillRect(border, border, WIDTH, HEIGHT);
	}

	private void drawEndGame() {
		Graphics2D g = (Graphics2D) snakeBoard.getGraphics();
		g.setColor(Color.BLACK);
		String endString="GAME OVER";
		Font font = new Font("Impact", Font.BOLD, 50);
		g.setFont(font);
		int x = WIDTH / 2 - DrawUtils.getMesageWidth(endString, font, g) / 2;
		int y = HEIGHT / 2 - DrawUtils.getMessageHeight(endString, font, g) / 2;
		g.drawString(endString, x, y);
		g.setFont(new Font("Impact",Font.PLAIN,24));
		g.drawString("Press Space to Restart", x+10, y+50);
		g.dispose();
	}

	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
		g2d.drawImage(snakeBoard, 0, 0, null);
		g.drawImage(finalBoard, 0, 0, null);
		if (dead) {
			drawEndGame();
		} else {
			point.render(g);
			for (int a = 0; a < snakeList.size(); a++) {
				snakeList.get(a).render(g);
			}
		}
	}

	public boolean turn(int xturn, int yturn, int a) {
		Snake snake = snakeList.get(a);
		Snake infront = snakeList.get(a - 1);

		return true;
	}

	public void move() {
		if (headSnake.dir == Direction.UP) {
			headSnake.setY(headSnake.getY() - headSnake.size);
		} else if (headSnake.dir == Direction.DOWN) {
			headSnake.setY(headSnake.getY() + headSnake.size);
		} else if (headSnake.dir == Direction.RIGHT) {
			headSnake.setX(headSnake.getX() + headSnake.size);
		} else if (headSnake.dir == Direction.LEFT) {
			headSnake.setX(headSnake.getX() - headSnake.size);
		}

		for (int a = snakeList.size() - 1; a > 0; a--) {
			Snake snake = snakeList.get(a);
			Snake infront = snakeList.get(a - 1);

			if (snake.dirChange) {
				snake.dirChange = false;
				snake.dir = snake.turnDir;

			}
			if (a != 1 && infront.dirChange) {
				snake.dirChange = true;
				snake.turnDir = infront.turnDir;
			} else if (a == 1 && infront.dirChange) {
				snake.dirChange = true;
				snake.turnDir = infront.dir;
				infront.dirChange = false;
			}

			if (snake.dir == Direction.UP) {
				snake.setY(snake.getY() - snake.size);
			} else if (snake.dir == Direction.DOWN) {
				snake.setY(snake.getY() + snake.size);
			} else if (snake.dir == Direction.RIGHT) {
				snake.setX(snake.getX() + snake.size);
			} else if (snake.dir == Direction.LEFT) {
				snake.setX(snake.getX() - snake.size);
			}

			snakeList.set(a, snake);
			snakeList.set(a - 1, infront);

		}

	}

	public Point randPoint() {
		boolean found = false;
		int x, y;
		Point point = new Point(0, 0);
		while (!found) {
			x = (int) ((Math.random() * 64) + 1) * squareSize - border;
			y = (int) ((Math.random() * 44) + 1) * squareSize - border;
			for (int a = 0; a < snakeList.size(); a++) {
				Snake snake=snakeList.get(a);
				if (x !=snake.getX() && y != snake.getY() && x > 0 && y > 0 && x < WIDTH - border
						&& y < HEIGHT - border) {
					found = true;
					point = new Point(x, y);
				}
			}

		}
		return point;
	}

	public void detectSides() {
		if (headSnake.getX() > WIDTH - border - headSnake.size || headSnake.getX() < 0) {
			dead = true;
		} else if (headSnake.getY() > HEIGHT - border - headSnake.size || headSnake.getY() < 0) {
			dead = true;
		} else {
			dead = false;
		}
	}

	public void detectPoint() {
		if (headSnake.getX() == point.getX() && headSnake.getY() == point.getY()) {
			addSnake();
			addSnake();
			addSnake();
			score+=3;
		}
	}

	private void detectSnake() {
		for (int a = 1; a < snakeList.size(); a++) {
			Snake snake = snakeList.get(a);
			if (headSnake.getX() == snake.getX() && headSnake.getY() == snake.getY()) {
				dead = true;
			}
		}
	}

	private void addSnake() {
		point = randPoint();
		Snake snake = snakeList.getLast();
		headSnake.moveBack = false;
		Snake newSnake;
		if (snake.dir == Direction.DOWN) {
			newSnake = new Snake(snake.count + 1, snake.getX(), snake.getY() - snake.size, snake.dir);
			snakeList.add(newSnake);
		} else if (snake.dir == Direction.UP) {
			newSnake = new Snake(snake.count + 1, snake.getX(), snake.getY() + snake.size, snake.dir);
			snakeList.add(newSnake);
		} else if (snake.dir == Direction.LEFT) {
			newSnake = new Snake(snake.count + 1, snake.getX() + snake.size, snake.getY(), snake.dir);
			snakeList.add(newSnake);
		} else if (snake.dir == Direction.RIGHT) {
			newSnake = new Snake(snake.count + 1, snake.getX() - snake.size, snake.getY(), snake.dir);
			snakeList.add(newSnake);
		}

	}

	public void update() {
		move();
		detectSides();
		detectSnake();
		detectPoint();
	}

	public void reset() {
		dead = false;
		drawBoard();
		if(score>highscore) {
			highscore=score;
		}
		score=0;
		headSnake = new Snake(1, 0, 0, Direction.DOWN);
		snakeList=new LinkedList<Snake>();
		snakeBoard.getGraphics().dispose();
		snakeList.add(headSnake);
		point = randPoint();
	}

}
