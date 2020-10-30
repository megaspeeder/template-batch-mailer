package alex.lopez.ve.tbm.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Recipient implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> dataMap;
	private boolean isActive;

	public Recipient() {
		this(new HashMap<String, String>(), true);
	}

	public Recipient(Map<String, String> dataMap, boolean isActive) {
		this.dataMap = dataMap;
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void addData(String key, String value) {
		dataMap.put(key.toLowerCase(), value);
	}

	public String getData(String key) {
		return dataMap.get(key.toLowerCase());
	}
}
