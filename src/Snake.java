import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Snake {
	
	public int x;
	public int y;
	public final int size=15;
	public int length;
	public int count;
	private BufferedImage snakeImage;
	public Direction dir;
	public boolean dirChange;


	public Direction turnDir;
	public boolean moveBack;
	
	public Snake(int count,int x,int y,Direction dir) {
		this.count=count;
		snakeImage=new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		this.x=x;
		this.y=y;
		this.dir=dir;
		dirChange=false;
		moveBack=true;
		drawSnake();
	}
	
	private void drawSnake() {
		Graphics2D g=(Graphics2D) snakeImage.getGraphics();
		g.setColor(Color.blue);
		g.fillRect(0,0, size, size);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, size, size);
		g.dispose();
	}
	
	public void render(Graphics2D g) {
		g.drawImage(snakeImage, x+SnakeBoard.border, y+SnakeBoard.border, null);
	}
	
	public void setDirection(Direction dir) {
		this.dir=dir;
	}
	public void setX(int x) {
		this.x=x;
	}
	public int getX() {
		return this.x;
	}
	
	public void setY(int y) {
		this.y=y;
	}
	
	public int getY() {
		return this.y;
	}

	public boolean isDirChange() {
		return dirChange;
	}

	public void setDirChange(boolean dirChange) {
		this.dirChange = dirChange;
	}

	public Direction getTurnDir() {
		return turnDir;
	}

	public void setTurnDir(Direction turnDir) {
		this.turnDir = turnDir;
	}

}
