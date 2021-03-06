package alex.lopez.vega.launcher;

import javax.swing.SwingUtilities;

import alex.lopez.vega.util.FileUtils;
import alex.lopez.vega.view.View;

public class Launcher {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FileUtils.setupWorkingDirectory();
				new View();
			}
		});
	}
}
