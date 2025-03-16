package lib.lunar.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlStruct {
	protected ArrayList<LinkedHashMap<String, Object>> configs = new ArrayList<>();
	protected char delim = '.';// path分隔符，分隔出嵌套命名空间
	public char[] end_path_identifier = { '(', ')', '[', ']', '{', '}', ',', ';' };// 遇到这些字符时停止分隔命名空间，并且从上一个分隔符开始到path结尾均视作key

	public static Yaml getYamlLoader() {
		LoaderOptions yamlLoaderOptions = new LoaderOptions();
		yamlLoaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
		yamlLoaderOptions.setCodePointLimit(Integer.MAX_VALUE);// 设置Yaml文件的最大能解析的字符数
		yamlLoaderOptions.setNestingDepthLimit(Integer.MAX_VALUE);
		return new Yaml(yamlLoaderOptions);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static YamlStruct fromJarFile(Class<?> any_class_in_jar, String path) {
		YamlStruct struct = new YamlStruct();
		Yaml yaml = getYamlLoader();
		Iterable<LinkedHashMap> all_configs = (Iterable<LinkedHashMap>) (Object) yaml.loadAll(new String(JarUtils.getJarResourceAsBytes(any_class_in_jar, path)));
		for (LinkedHashMap map : all_configs)
			struct.configs.add(map);
		return struct;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static YamlStruct fromFile(String path) {
		YamlStruct struct = new YamlStruct();
		Yaml yaml = getYamlLoader();
		Iterable<LinkedHashMap> all_configs = null;
		try (FileInputStream stream = new FileInputStream(path)) {
			all_configs = (Iterable<LinkedHashMap>) (Object) yaml.loadAll(stream);
		} catch (IOException e) {
			System.err.println("Read yaml file " + path + " failed.");
			e.printStackTrace();
		}
		if (all_configs != null)
			for (LinkedHashMap map : all_configs)
				struct.configs.add(map);
		return struct;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static YamlStruct fromStream(InputStream stream) {
		YamlStruct struct = new YamlStruct();
		Yaml yaml = getYamlLoader();
		Iterable<LinkedHashMap> all_configs = null;
		all_configs = (Iterable<LinkedHashMap>) (Object) yaml.loadAll(stream);
		for (LinkedHashMap map : all_configs)
			struct.configs.add(map);
		return struct;
	}

	@Override
	public String toString() {
		return configs.toString();
	}

	public char getDelim() {
		return delim;
	}

	public YamlStruct setDelim(char new_delim) {
		delim = new_delim;
		return this;
	}

	public String[] parsePath(String key) {
		ArrayList<String> result = new ArrayList<>();
		int last_namespace_start_idx = 0;
		int end_idx = key.length();
		FIND_END_IDX: for (int i = 0; i < end_idx; ++i) {
			char ch = key.charAt(i);
			if (ch == delim) {
				result.add(key.substring(last_namespace_start_idx, i));
				last_namespace_start_idx = i + 1;
			} else
				for (int j = 0; j < end_path_identifier.length; ++j)
					if (ch == end_path_identifier[j]) {
						end_idx = i;
						break FIND_END_IDX;
					}
		}
		result.add(key.substring(last_namespace_start_idx));
		return result.toArray(new String[result.size()]);
	}

	@SuppressWarnings("rawtypes")
	public Object getObject(String key, int config_idx) {
		Object value = null;
		String[] namespaces = parsePath(key);
		LinkedHashMap map = (LinkedHashMap) configs.get(config_idx);
		for (int i = 0; i < namespaces.length; ++i) {
			if (i != namespaces.length - 1)
				map = (LinkedHashMap) map.get(namespaces[i]);
			else
				value = map.get(namespaces[i]);
		}
		return value;
	}

	public Object getObject(String key) {
		return getObject(key, 0);
	}

	public String getString(String key, int config_idx) {
		return getObject(key, config_idx).toString();
	}

	public String getString(String key) {
		return getString(key, 0);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Object> getArray(String key, int config_idx) {
		Object obj = getObject(key, config_idx);
		if (obj instanceof ArrayList list)
			return list;
		else {
			ArrayList<Object> list = new ArrayList<>();
			list.add(obj);
			return list;
		}
	}

	public ArrayList<Object> getArray(String key) {
		return getArray(key, 0);
	}
}
