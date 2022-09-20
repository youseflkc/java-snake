import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		Game game=new Game();
		JFrame mainFrame=new JFrame("Snake");
		mainFrame.setResizable(false);
		mainFrame.add(game);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		game.start();
	}
}
