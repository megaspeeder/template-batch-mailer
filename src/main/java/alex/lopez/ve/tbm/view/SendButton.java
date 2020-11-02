package alex.lopez.ve.tbm.view;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import alex.lopez.ve.tbm.interfaces.IControllerAccess;
import alex.lopez.ve.tbm.interfaces.Initialisable;

public class SendButton extends JButton implements Initialisable {

	private static final long serialVersionUID = -1514308586285207032L;

	private IControllerAccess ica;

	public SendButton(IControllerAccess ica) {
		super("Send...");
		this.ica = ica;
		initialise();
	}

	@Override
	public void initialise() {
		addActionListener((ActionEvent ae) -> {
			int choice = JOptionPane.showConfirmDialog(null, "Are you sure you would like to send this message?",
					"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

			if (choice == JOptionPane.YES_OPTION) {
				try {
					ica.sendMail();
					JOptionPane.showConfirmDialog(null, "Mail sent!", "Confirm", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane optionPane = new JOptionPane("Could not send mail:" + e.getMessage(),
							JOptionPane.ERROR_MESSAGE);
					JDialog dialog = optionPane.createDialog("Error");
					dialog.setVisible(true);
				}
			}
		});
	}

}
