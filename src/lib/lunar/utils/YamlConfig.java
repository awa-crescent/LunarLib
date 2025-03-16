package lib.lunar.utils;

import java.io.File;

public class YamlConfig {
	protected Class<?> entry_clazz = null;
	protected String config_path = null;
	protected YamlStruct yml_entries = null;

	protected YamlConfig(Class<?> entry_clazz) {
		this.entry_clazz = entry_clazz;
	}

	protected YamlConfig(Class<?> entry_clazz, String config_path) {
		this.entry_clazz = entry_clazz;
		this.config_path = config_path;
		loadConfigFile(entry_clazz, config_path);
	}

	/**
	 * 加载配置文件，优先加载本地文件夹的配置文件，如果没有则加载jar包内置本地化文件jar:/config_path.yml
	 * 
	 * @return 返回是否加载成功
	 */
	public boolean loadConfigFile(Class<?> entry_clazz, String config_path) {
		boolean load_complete = false;
		File locale_file = new File(config_path);
		// 本地目录下存在语言文件
		if (locale_file.exists()) {
			try {
				yml_entries = YamlStruct.fromFile(config_path);
				load_complete = true;
			} catch (Exception ex) {
				System.err.println("Cannot load config file " + config_path);
				ex.printStackTrace();
			}
		} else {
			try {
				if (entry_clazz != null) {
					yml_entries = YamlStruct.fromJarFile(entry_clazz, config_path);
					load_complete = true;
				} else
					System.err.println("No config file exists in local filesystem, please specify jar entry clazz for finding embeded config file " + config_path);
			} catch (Exception ex) {
				System.err.println("No config file exists in local filesystem. Cannot load embeded config file " + config_path + " in jar of entry class " + entry_clazz.getName());
				ex.printStackTrace();
			}
		}
		return load_complete;
	}

	public boolean loadConfigFile(String config_path) {
		return loadConfigFile(entry_clazz, config_path);
	}

	public boolean reload() {
		return loadConfigFile(entry_clazz, config_path);
	}
}
