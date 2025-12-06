package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

public class Main {

	public static void main(String[] args) {
		// Entry-point: cria GUI, regista engine como observer e arranca o loop
		ImageGUI gui = ImageGUI.getInstance();
		GameEngine engine = new GameEngine();
		gui.registerObserver(engine);
		gui.go();
	}
	
}
