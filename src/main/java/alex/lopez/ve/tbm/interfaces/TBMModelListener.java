package alex.lopez.ve.tbm.interfaces;

import java.util.List;

import alex.lopez.ve.tbm.model.Recipient;
import alex.lopez.ve.tbm.model.Template;

public interface TBMModelListener {

	void onRecipientListLoaded(List<String> columnNames, List<Recipient> recipientList);

	void onTemplateLoaded(Template template);

	void onRecipientsDeleted(List<String> columnNames, List<TBMModelListener> modelListenerList);

	void onTemplateDeleted(Template template);
	
}
