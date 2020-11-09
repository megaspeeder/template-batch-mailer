package alex.lopez.vega.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import alex.lopez.vega.mail.Mailer;
import alex.lopez.vega.model.Message;
import alex.lopez.vega.model.Model;
import alex.lopez.vega.model.Recipient;
import alex.lopez.vega.model.Template;
import alex.lopez.vega.view.listeners.MailLimiterListener;
import alex.lopez.vega.view.listeners.ModelListener;

public class Controller {

	private Model model;
	private Mailer mailer;

	public Controller() {
		this.model = new Model();
		this.mailer = new Mailer();
	}

	public void addModelListener(ModelListener listener) {
		if (listener != null)
			model.addListener(listener);
	}

	public void removeModelListener(ModelListener listener) {
		if (listener != null)
			model.removeListener(listener);
	}

	public void addMailLimiterListener(MailLimiterListener listener) {
		mailer.addMailLimiterListener(listener);
	}

	public Model getModel() {
		return model;
	}

	public Mailer getMailer() {
		return mailer;
	}

	public void loadRecipients(File file) throws EncryptedDocumentException, IOException {
		checkExtension(file, "\\.", "xls", "xlsx");

		Workbook workbook;
		FileInputStream fis = new FileInputStream(file);

		workbook = WorkbookFactory.create(fis);

		Sheet sheet = workbook.getSheetAt(0);

		loadKeys(sheet);
		loadRecipients(sheet);

		workbook.close();
		fis.close();
	}

	private void loadKeys(Sheet sheet) {
		DataFormatter df = new DataFormatter();
		Iterator<Cell> it = sheet.getRow(0).cellIterator();

		List<String> keys = new LinkedList<String>();

		while (it.hasNext()) {
			Cell cell = it.next();
			String value = df.formatCellValue(cell);

			keys.add(value);
		}

		model.setKeys(keys);
	}

	private void loadRecipients(Sheet sheet) {
		DataFormatter df = new DataFormatter();
		Iterator<Row> rowIt = sheet.rowIterator();
		List<Recipient> recipientList = new LinkedList<Recipient>();

		if (rowIt.hasNext())
			rowIt.next();

		while (rowIt.hasNext()) {
			Row row = rowIt.next();

			List<String> keys = model.getKeys();
			Recipient r = new Recipient();

			for (int i = 0; i < keys.size(); ++i) {
				String value = df.formatCellValue(row.getCell(i));
				r.put(keys.get(i), value);
			}

			recipientList.add(r);
		}

		model.setRecipients(recipientList);
	}

	public void loadTemplate(File file) {

	}

	private void checkExtension(File file, String regex, String... acceptedExtensions) {
		if (file == null)
			throw new IllegalArgumentException("File is null");

		String fileExtension = file.getName().split(regex)[1];
		String errorMsg = "Unrecognised file extension. Accepted file extensions are: ";

		for (String extension : acceptedExtensions) {
			if (fileExtension.contentEquals(extension))
				return;
			else {
				errorMsg += extension + ", ";
			}
		}

		errorMsg = errorMsg.substring(0, errorMsg.length() - 2);

		throw new IllegalArgumentException(errorMsg);
	}

	public void insertColumnVariable(int selectedColumn) {
		model.insertColumnVariable(selectedColumn);
	}

	public void removeRecipients(List<Recipient> rows) {
		model.removeRecipients(rows);
	}

	public void newRecipients() {
		model.setRecipients(new LinkedList<Recipient>());
	}

	public void newKeys() {
		model.setKeys(new LinkedList<String>());
	}

	public void newTemplate() {
		model.setTemplate(new Template());
	}

	public List<Message> addCurrentMessages() {
		return mailer.processMessages(model.getRecipients(), model.getTemplate());
	}

	public int getAvailableMessages() {
		return mailer.getAvailableMessages();
	}

	public void sendMessagesUntilLimit() {
		mailer.sendMessagesUntilLimit();
	}

	public void setSubject(String subject) {
		model.setSubject(subject);
	}

	public void setContent(String content) {
		model.setContent(content);
	}

	public List<Message> getMessageQueue() {
		return mailer.getMessageQueue();
	}

	public void removeMessage(Message message) {
		mailer.removeMessage(message);
	}

	public void removeAllMessages() {
		mailer.removeAllMessages();
	}
}
