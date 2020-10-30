package alex.lopez.ve.tbm.launcher;

import javax.swing.SwingUtilities;

import alex.lopez.ve.tbm.view.TBMWindow;

public class Launcher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new TBMWindow();
		});
	}

}
