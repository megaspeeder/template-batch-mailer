package alex.lopez.vega.model;

import java.util.HashMap;
import java.util.Map;

public class Recipient {

	private Map<String, String> dataMap;

	public Recipient() {
		dataMap = new HashMap<String, String>();
	}

	public void put(String k, String v) {
		if (k != null && v != null)
			dataMap.put(k, v);
	}

	public String get(String k) {
		if (k != null)
			return dataMap.get(k);

		return null;
	}
}
