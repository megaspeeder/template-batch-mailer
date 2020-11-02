package alex.lopez.ve.tbm.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import alex.lopez.ve.tbm.interfaces.IControllerAccess;
import alex.lopez.ve.tbm.interfaces.Initialisable;
import alex.lopez.ve.tbm.interfaces.TBMModelListener;
import alex.lopez.ve.tbm.model.Recipient;
import alex.lopez.ve.tbm.model.Template;

public class TBMTemplatePanel extends JPanel implements Initialisable, TBMModelListener {

	private static final long serialVersionUID = 6884330342883394043L;

	private IControllerAccess ica;
	private JTextField header;
	private JTextArea content;

	public TBMTemplatePanel(IControllerAccess ica) {
		this.ica = ica;
		initialise();
	}

	@Override
	public void initialise() {
		setLayout(new BorderLayout());

		header = new JTextField();

		content = new JTextArea();
		content.setWrapStyleWord(true);
		content.setLineWrap(true);

		JLabel headerLabel = new JLabel("Header");
		JLabel contentLabel = new JLabel("Content");

		JPanel headerPanel = new JPanel();
		JPanel contentPanel = new JPanel();

		headerPanel.setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());

		headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		header.setAlignmentX(Component.LEFT_ALIGNMENT);

		contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.setAlignmentX(Component.LEFT_ALIGNMENT);

		headerPanel.add(headerLabel, BorderLayout.NORTH);
		headerPanel.add(header, BorderLayout.CENTER);
		headerPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		contentPanel.add(contentLabel, BorderLayout.NORTH);
		contentPanel.add(new JScrollPane(content), BorderLayout.CENTER);

		JPanel container = new JPanel();

		container.setLayout(new BorderLayout());

		container.add(headerPanel, BorderLayout.NORTH);
		container.add(contentPanel, BorderLayout.CENTER);

		add(container);

		setBorder(BorderFactory.createTitledBorder("Template"));

		FocusListener listener = new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				updateTemplate();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		};

		header.addFocusListener(listener);
		content.addFocusListener(listener);
	}

	public void insertColumnVariable(String columnName) {
		content.insert("{" + columnName + "}", content.getCaretPosition());
	}

	@Override
	public void onRecipientListLoaded(List<String> columnNames, List<Recipient> recipientList) {

	}

	@Override
	public void onTemplateLoaded(Template template) {
		content.setText(template.getContent());
	}

	@Override
	public void onTemplateDeleted(Template template) {
		content.setText("");
	}

	private void updateTemplate() {
		String headerStr = header.getText();
		String contentStr = content.getText();

		Template template = new Template(headerStr, contentStr);

		ica.setTemplate(template);
	}

	@Override
	public void onRecipientsDeleted(List<String> columnNames, List<Recipient> recipientList) {
		// TODO Auto-generated method stub
		
	}

}
