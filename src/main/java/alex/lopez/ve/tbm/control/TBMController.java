package alex.lopez.ve.tbm.control;

import java.io.File;
import java.util.List;

import alex.lopez.ve.tbm.interfaces.IControllerAccess;
import alex.lopez.ve.tbm.interfaces.TBMModelListener;
import alex.lopez.ve.tbm.model.Recipient;
import alex.lopez.ve.tbm.model.TBMModel;
import alex.lopez.ve.tbm.model.Template;

public class TBMController implements IControllerAccess {
	private transient TBMModel model;
	private transient Mailer mailer;

	public TBMController() {
		model = new TBMModel();
		mailer = new Mailer(this);
	}

	@Override
	public void loadTemplate(File file) {
		model.loadTemplate(file);
	}

	@Override
	public void loadRecipients(File file) {
		model.loadRecipients(file);
	}

	public void addTBMModelListener(TBMModelListener listener) {
		model.addTBMModelListener(listener);
	}

	@Override
	public void deleteRecipients() {
		model.deleteRecipients();
	}

	@Override
	public void deleteTemplate() {
		model.deleteTemplate();
	}

	@Override
	public void sendMail() {
		if (!model.hasRecipients())
			throw new IllegalStateException("Missing recipients");
		if (!model.hasTemplate())
			throw new IllegalStateException("Missing template");
		if (!model.canSendMessage())
			throw new IllegalStateException("Cannot send mail yet");

		mailer.scheduleSendMessage();

		while (mailer.canSendWholeBatch())
			mailer.sendWholeBatch();

		while (model.canSendMessage())
			mailer.sendOne();
		
		System.out.println("Sent!");
	}

	@Override
	public List<Recipient> getRecipients() {
		return model.getRecipients();
	}

	@Override
	public Template getTemplate() {
		return model.getTemplate();
	}

	@Override
	public void setTemplate(Template template) {
		model.setTemplate(template);
	}

	@Override
	public boolean canSendMessage() {
		return model.canSendMessage();
	}

	@Override
	public int getNumberAvailableMails() {
		return model.getNumberAvailableMails();
	}

	@Override
	public int getMailLimit() {
		return model.getMailLimit();
	}

	@Override
	public Mailer getMailer() {
		return mailer;
	}

	@Override
	public void removeRecipients(List<Recipient> batchRecipients) {
		for (Recipient recipient : batchRecipients)
			model.removeRecipient(recipient);
	}

}
