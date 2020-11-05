package alex.lopez.vega.model;

import java.util.LinkedList;
import java.util.List;

import alex.lopez.vega.view.listeners.ModelListener;

public class Model {

	private List<ModelListener> listenerList;

	private List<String> keys;
	private List<Recipient> recipientList;
	private Template template;

	public Model() {
		this(new LinkedList<Message>(), new LinkedList<String>(), new LinkedList<Recipient>());
	}

	public Model(LinkedList<Message> scheduledMessageList, List<String> keys, List<Recipient> recipientList) {
		listenerList = new LinkedList<ModelListener>();

		this.keys = keys;
		this.recipientList = recipientList;
		template = new Template();
	}

	public void addListener(ModelListener listener) {
		if (listener != null)
			listenerList.add(listener);
	}

	public void removeListener(ModelListener listener) {
		if (listener != null)
			listenerList.remove(listener);
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;

		for (ModelListener listener : listenerList)
			listener.onKeysChanged(keys);
	}

	public List<Recipient> getRecipients() {
		return recipientList;
	}

	public void setRecipients(List<Recipient> recipientList) {
		this.recipientList = recipientList;

		for (ModelListener listener : listenerList)
			listener.onRecipientsChanged(recipientList);
	}

	public void removeRecipients(List<Recipient> rows) {
		recipientList.removeAll(rows);

		for (ModelListener listener : listenerList)
			listener.onRecipientsChanged(recipientList);
	}

	public void insertColumnVariable(int selectedColumn) {
		template.insertContent(keys.get(selectedColumn));

		for (ModelListener listener : listenerList)
			listener.onTemplateChanged(template);
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;

		for (ModelListener modelListener : listenerList)
			modelListener.onTemplateChanged(template);
	}

	public void setSubject(String subject) {
		template.setSubject(subject);
	}

	public void setContent(String content) {
		template.setContent(content);
	}
}
