package alex.lopez.ve.tbm.interfaces;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;

import alex.lopez.ve.tbm.control.Mailer;
import alex.lopez.ve.tbm.model.Recipient;
import alex.lopez.ve.tbm.model.TBMModel;
import alex.lopez.ve.tbm.model.Template;

public interface IControllerAccess {

	void loadTemplate(File file);

	void loadRecipients(File file);

	void deleteRecipients();

	void deleteTemplate();

	void sendMail() throws MessagingException;

	List<Recipient> getRecipients();

	Template getTemplate();

	void setTemplate(Template template);

	boolean canSendMessage();

	int getNumberAvailableMails();

	int getMailLimit();

	Mailer getMailer();

	void removeRecipients(List<Recipient> batchRecipients);

	TBMModel getModel();
}
