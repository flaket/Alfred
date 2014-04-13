package view;

import java.applet.Applet;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

@SuppressWarnings("serial")
public class ConsoleView extends Applet {

	public static void main(String[] args) {
		Frame frame;
		try {
			frame = new Frame();
			frame.setSize(800,600);
			frame.setLocationRelativeTo(null);
			frame.add(new ConsoleView());
			frame.setVisible(true);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
