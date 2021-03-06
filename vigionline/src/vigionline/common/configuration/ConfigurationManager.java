package vigionline.common.configuration;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {

	private static ConfigurationManager _instance = new ConfigurationManager();

	public static ConfigurationManager getInstance() {
		return _instance;
	}

	private ConfigurationManager() {
		_properties = new Properties();
		try {
			_properties.loadFromXML(this.getClass().getResourceAsStream("vigionline.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Properties _properties;

	public String getString(String key) {
		return _properties.getProperty(key);
	}

	public int getInt(String key) throws NumberFormatException {
		return Integer.parseInt(_properties.getProperty(key));
	}

	public double getDouble(String key)throws NumberFormatException {
		return Double.parseDouble(_properties.getProperty(key));
	}
	
	/************ Program Settings **************/

	public boolean hasProxy() {
		String proxyHost = null;
		int proxyPort = -1;
		try
		{
			proxyHost = getString("proxyHost");
			proxyPort = getInt("proxyPort");
		}catch(NumberFormatException nfe)
		{
			return false;
		}
		return proxyHost != null && !proxyHost.isEmpty() && proxyPort > -1;
	}

	public String getProxyHost() {
		return getString("proxyHost");
	}

	public int getProxyPort() {
		return getInt("proxyPort");
	}
	
	public String getImageDirectory() {
		return getString("imageDirectory");
	}
	
	public int getMaxConnectionsPerHost() {
		return getInt("http.connection-manager.max-per-host");
	}

	public double getFreeDiskSpacePercentageThreshold() {
		return getDouble("diskTreshold");
	}
}
