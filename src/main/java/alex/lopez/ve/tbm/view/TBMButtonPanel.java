package alex.lopez.ve.tbm.view;

import java.awt.BorderLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import alex.lopez.ve.tbm.interfaces.IControllerAccess;
import alex.lopez.ve.tbm.interfaces.Initialisable;

public class TBMButtonPanel extends JPanel implements Initialisable {

	private static final long serialVersionUID = 3115446816541162887L;

	private JLabel infoLabel;
	private IControllerAccess ica;

	public TBMButtonPanel(IControllerAccess ica) {
		this.ica = ica;
		initialise();
	}

	@Override
	public void initialise() {
		setLayout(new BorderLayout());

		JPanel buttonContainer = new JPanel();

		buttonContainer.add(new DeleteButton(ica));
		buttonContainer.add(new SendButton(ica));

		JPanel timeContainer = new JPanel();

		infoLabel = new JLabel();
		updateRemainingTime();

		timeContainer.add(infoLabel);

		add(buttonContainer, BorderLayout.EAST);
		add(timeContainer, BorderLayout.WEST);

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				updateRemainingTime();

			}
		}, 0, 1000);
	}

	private void updateRemainingTime() {
		String message = Integer.toString(ica.getNumberAvailableMails());

		infoLabel.setText(message);
	}
}
