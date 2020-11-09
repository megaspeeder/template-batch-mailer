package alex.lopez.vega.view.menu;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JMenuItem;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.MailQueuePanel;
import alex.lopez.vega.view.View;

public class MailQueueMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	private MailQueuePanel mailQueuePanel;

	public MailQueueMenuItem(View view, Controller controller) {
		super("Mail Queue");

		mailQueuePanel = new MailQueuePanel(controller);

		init(view, controller);
	}

	private void init(View view, Controller controller) {
		addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == this) {
				final JDialog frame = new JDialog(view, "Mail Queue", true);

				mailQueuePanel.updateMailQueue(controller);

				frame.getContentPane().add(mailQueuePanel);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
