import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Point {
	public int x, y;
	public static final int size=15;
	private BufferedImage pointImage;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		pointImage=new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		drawPoint();
	}
	
	public void drawPoint() {
		Graphics2D g= (Graphics2D)pointImage.getGraphics();
		g.setColor(Color.red);
		g.fillRect(0, 0, size, size);
		g.dispose();
	}
	
	public void render(Graphics2D g) {
		g.drawImage(pointImage,x+SnakeBoard.border,y+SnakeBoard.border,null);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
