package alex.lopez.vega.view.listeners;

import java.util.List;

import alex.lopez.vega.model.Recipient;
import alex.lopez.vega.model.Template;

public interface ModelListener {

	void onRecipientsChanged(List<Recipient> newRecipientsList);

	void onKeysChanged(List<String> newKeys);

	void onTemplateChanged(Template template);

}
