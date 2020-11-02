package alex.lopez.ve.tbm.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import alex.lopez.ve.tbm.control.MailBatch;
import alex.lopez.ve.tbm.control.MailLimiter;
import alex.lopez.ve.tbm.interfaces.MailBatchListener;
import alex.lopez.ve.tbm.interfaces.TBMModelListener;

public class TBMModel implements MailBatchListener {
	private List<TBMModelListener> modelListenerList;

	private List<Recipient> recipientList;
	private Template template;
	private MailLimiter limiter;
	private List<String> columnNames;

	public TBMModel() {
		template = new Template();
		modelListenerList = new LinkedList<TBMModelListener>();
		recipientList = new LinkedList<Recipient>();
		limiter = new MailLimiter();
		columnNames = new ArrayList<String>();

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				limiter.decreaseSentMails();
			}
		}, 0, (long) ((3600 / (float) limiter.getMailLimit()) * 1000));
	}

	public void addRecipient(Recipient r) {
		if (r != null)
			recipientList.add(r);
	}

	public void removeRecipient(Recipient r) {
		if (r != null)
			recipientList.remove(r);
	}

	public void loadTemplate(File file) {
		preprocessFile(file, "\\.", "txt");

		try {
			Scanner s = new Scanner(new FileInputStream(file));

			StringBuilder sb = new StringBuilder();

			while (s.hasNext())
				sb.append(s.nextLine());

			template.setContent(sb.toString());

			for (TBMModelListener listner : modelListenerList)
				listner.onTemplateLoaded(template);

			s.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void loadRecipients(File file) {
		preprocessFile(file, "\\.", "xls", "xlsx");

		try {
			Workbook workbook;
			FileInputStream fis = new FileInputStream(file);

			workbook = WorkbookFactory.create(fis);

			Sheet sheet = workbook.getSheetAt(0);

			getColumnNames(sheet);

			getRecipients(sheet);
			
			System.out.println(recipientList.size());

			workbook.close();
			fis.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void getColumnNames(Sheet sheet) {
		DataFormatter df = new DataFormatter();
		Row row = sheet.getRow(0);

		columnNames = new LinkedList<String>();
		
		Iterator<Cell> cellIterator = row.cellIterator();

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();

			String columnName = df.formatCellValue(cell);
			columnNames.add(columnName);
		}

		columnNames.add("Active");
	}

	private void getRecipients(Sheet sheet) {
		recipientList = new LinkedList<Recipient>();

		Iterator<Row> rowIterator = sheet.rowIterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() == 0)
				continue;

			recipientList.add(getRecipient(row));
		}
		
		for (TBMModelListener listener : modelListenerList)
			listener.onRecipientListLoaded(columnNames, recipientList);
	}

	private Recipient getRecipient(Row row) {
		DataFormatter df = new DataFormatter();
		Recipient r = new Recipient();

		for (int i = 0; i < columnNames.size(); i++) {
			String value = df.formatCellValue(row.getCell(i));

			r.addData(columnNames.get(i), value);
		}

		return r;
	}

	private void preprocessFile(File file, String regex, String... acceptedExtensions) {
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

	public boolean hasTemplate() {
		return template.getContent() != "";
	}

	public boolean hasRecipients() {
		return !recipientList.isEmpty();
	}

	public void addTBMModelListener(TBMModelListener listener) {
		if (listener != null)
			modelListenerList.add(listener);
	}

	public void removeTBMModelListener(TBMModelListener listener) {
		if (listener != null)
			modelListenerList.remove(listener);
	}

	public void deleteRecipients() {
		recipientList.clear();

		for (TBMModelListener listener : modelListenerList) {
			listener.onRecipientsDeleted(columnNames, recipientList);
		}
	}

	public void deleteTemplate() {
		template.setContent("");

		for (TBMModelListener listener : modelListenerList) {
			listener.onTemplateDeleted(template);
		}
	}

	public List<Recipient> getRecipients() {
		return recipientList;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public boolean canSendMessage() {
		return !limiter.hasExceededHourlyMails();
	}

	public int getNumberAvailableMails() {
		return limiter.getNumberAvailableMails();
	}

	public int getMailLimit() {
		return limiter.getMailLimit();
	}

	public MailLimiter getMailLimiter() {
		return limiter;
	}

	public boolean hasRemainingMessages() {
		return recipientList.size() > 0;
	}

	@Override
	public void onOneMailSent(MailBatch mailBatch, List<Recipient> sentRecipients) {
		recipientList.removeAll(sentRecipients);
		
		for (TBMModelListener listener : modelListenerList) 
			listener.onRecipientsDeleted(columnNames, sentRecipients);
	}

	@Override
	public void onAllMailSent(MailBatch mailBatch, List<Recipient> sentRecipients) {
		recipientList.removeAll(sentRecipients);
		
		for (TBMModelListener listener : modelListenerList) 
			listener.onRecipientsDeleted(columnNames, sentRecipients);
	}
}
