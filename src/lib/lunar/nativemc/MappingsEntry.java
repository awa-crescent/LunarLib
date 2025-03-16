package lib.lunar.nativemc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import lib.lunar.utils.YamlStruct;

/**
 * 
 * 统一使用Mojang Mappings
 *
 */
public abstract class MappingsEntry {
	private static YamlStruct mappings_entries = null;
	private static final int compatible_version_tolerance = 3;

	public static final String mappingsFileDir = "lib/lunar/nativemc/mappings/";

	static {
		InputStream mappings_stream;
		ClassLoader mappings_class_loader = MappingsEntry.class.getClassLoader();
		Version mappings_ver = Version.this_version;
		URL mappings_url = null;
		int try_times = compatible_version_tolerance;
		while (try_times >= 0) {
			--try_times;
			mappings_url = mappings_class_loader.getResource(mappingsFileDir + mappings_ver + ".yml");
			if (mappings_url == null) {
				System.err.println("Cannot find embeded mappings file for NMS version " + mappings_ver);
				mappings_ver = mappings_ver.previous();
				continue;
			} else
				break;
		}
		if (mappings_url == null)
			System.err.println("Cannot find compatible mappings version for current server NMS version " + Version.this_version + " with tolerance " + compatible_version_tolerance);
		else
			System.out.println("Using mappings version " + mappings_ver);
		try {
			mappings_stream = mappings_url.openStream();
			mappings_entries = YamlStruct.fromStream(mappings_stream);
			mappings_stream.close();
		} catch (IOException ex) {
			System.err.println("Parsing mappings file failed");
			ex.printStackTrace();
		}
	}

	public static String getObfuscatedName(String mojang_name) {
		return mappings_entries.getString(mojang_name);
	}

	public static String getClassObfuscatedName(String nms_class_name) {
		return getObfuscatedName(nms_class_name + ".class");
	}
}
