package alex.lopez.vega.view.menu;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import alex.lopez.vega.view.MailQueuePanel;
import alex.lopez.vega.view.View;

public class MailQueueMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	private JPanel mailQueuePanel;
	
	public MailQueueMenuItem(View view) {
		super("Mail Queue");

		mailQueuePanel = new MailQueuePanel();
		
		init(view);
	}

	private void init(View view) {		
		addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == this) {
				final JDialog frame = new JDialog(view, "Mail Queue", true);
				
				frame.getContentPane().add(mailQueuePanel);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
