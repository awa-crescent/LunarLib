package lib.lunar.utils.locale;

import java.util.HashMap;
import java.util.Map;

import lib.lunar.utils.YamlConfig;

//语言系统支持
public class Locale extends YamlConfig {
	public static String locale_folder = "/assets/lang";
	protected static Map<String, Locale> locale_entries = new HashMap<>();

	protected String locale;

	protected Locale(Class<?> entry_clazz, String locale) {
		super(entry_clazz);
		loadLocale(locale);
	}

	protected Locale(Class<?> entry_clazz) {
		super(entry_clazz);
	}

	protected String getLocalePath() {
		return locale_folder + '/' + locale + ".yml";
	}

	/**
	 * 加载语言文件，如果已经加载则重新加载一次
	 * 
	 * @param entry_clazz
	 * @param locale_entry_key
	 * @param locale
	 */
	public static void loadLocale(Class<?> entry_clazz, String locale_entry_key, String locale) {
		Locale l;
		if (!locale_entries.containsKey(locale_entry_key)) {
			l = new Locale(entry_clazz);
			locale_entries.put(locale_entry_key, l);
		} else
			l = locale_entries.get(locale_entry_key);
		l.loadLocale(locale);
	}

	public boolean loadLocale(String locale) {
		this.config_path = getLocalePath();
		return super.loadConfigFile(entry_clazz, config_path);
	}

	public String getLocale() {
		return locale;
	}

	public static String getLocale(String locale_entry_key) {
		if (!locale_entries.containsKey(locale_entry_key))
			return null;
		return locale_entries.get(locale_entry_key).getLocale();
	}

	/**
	 * 根据locale得到本地化文本，不存在则返回key值
	 * 
	 * @param key 文本对应的key值
	 * @return 本地化文本
	 */
	public String getLocalizedValue(String key) {
		String value = yml_entries.getString(key);
		return value == null ? key : value;
	}

	/**
	 * 指定插件名称，根据locale得到本地化文本
	 * 
	 * @param plugin_name 插件名称
	 * @param key         文本对应的key值
	 * @return 本地化文本
	 */
	public static String getLocalizedValue(String plugin_name, String key) {
		return locale_entries.get(plugin_name).getLocalizedValue(key);
	}
}
