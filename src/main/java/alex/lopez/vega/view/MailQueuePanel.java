package alex.lopez.vega.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import alex.lopez.vega.controller.Controller;
import alex.lopez.vega.model.Message;

public class MailQueuePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JList<Message> messageList;
	private MessageTemplateDisplay messageTemplateDisplay;

	public MailQueuePanel(Controller controller) {
		init(controller);
	}

	private void init(Controller controller) {
		setLayout(new BorderLayout());

		JSplitPane contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JPanel messageQueuePanel = new JPanel();

		JPanel buttonPanel = new JPanel();

		JButton deleteAllButton = new JButton("Delete all messages");

		messageQueuePanel.setLayout(new BorderLayout());

		messageList = new JList<Message>();
		messageTemplateDisplay = new MessageTemplateDisplay(messageList, controller);

		messageList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Message message = messageList.getSelectedValue();
				messageTemplateDisplay.setSelectedMessage(message);
			}
		});

		deleteAllButton.addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == deleteAllButton) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete all scheduled messages?", "Confirm", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					if (controller.getMessageQueue().size() == 0) {
						Dialog.showErrorMessage("No scheduled messages to delete.");
						return;
					}

					controller.removeAllMessages();
					messageList = new JList<Message>(controller.getMessageQueue().toArray(new Message[0]));
					messageList.repaint();
					Dialog.showInfoMessage("All scheduled messages deleted");
				}
			}
		});

		buttonPanel.add(deleteAllButton);

		JLabel label = new JLabel("Queued messages");
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		messageQueuePanel.add(label, BorderLayout.NORTH);
		messageQueuePanel.add(new JScrollPane(messageList), BorderLayout.CENTER);
		messageQueuePanel.add(buttonPanel, BorderLayout.SOUTH);

		messageQueuePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		contentPane.add(messageQueuePanel);
		contentPane.add(messageTemplateDisplay);

		add(contentPane, BorderLayout.CENTER);
	}

	public void updateMailQueue(Controller controller) {
		DefaultListModel<Message> messageListModel = new DefaultListModel<Message>();
		List<Message> messageQueue = controller.getMessageQueue();
		messageListModel.addAll(messageQueue);
		messageList.setModel(messageListModel);
	}
}
