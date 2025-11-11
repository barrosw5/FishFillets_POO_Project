package pt.iscte.poo.game;

import static pt.iscte.poo.game.GameEngine.playedLevels;
import pt.iscte.poo.gui.ImageGUI;

public class Main {

	public static void main(String[] args) {
		ImageGUI gui = ImageGUI.getInstance();
		GameEngine engine = new GameEngine();
		gui.setStatusMessage("Level " + playedLevels + ": Good luck!");
		gui.registerObserver(engine);
		gui.go();
	}
	
}
