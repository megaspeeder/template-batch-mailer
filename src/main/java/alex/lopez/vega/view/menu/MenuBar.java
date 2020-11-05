package alex.lopez.vega.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.view.View;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private Controller controller;

	public MenuBar(View view, Controller controller) {
		super();
		this.controller = controller;

		init(view);
	}

	private void init(View view) {
		// Create file menu
		JMenu fileMenu = new JMenu("File");

		JMenu viewMenu = new JMenu("View");
		JMenu viewGeneralMenu = new JMenu("General");
		
		// Create menu items
		JMenuItem loadRecipientsMI = new LoadRecipientsMenuItem(controller);
		JMenuItem loadTemplateMI = new LoadTemplateMenuItem(controller);

		JMenuItem mailQueueMI = new MailQueueMenuItem(view);

		// Add menu items
		fileMenu.add(loadRecipientsMI);
		fileMenu.add(loadTemplateMI);

		viewGeneralMenu.add(mailQueueMI);
		viewMenu.add(viewGeneralMenu);
		
		add(fileMenu);
		add(viewMenu);
	}

}
