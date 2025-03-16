package lib.lunar;

import java.util.logging.Level;
import lib.lunar.jvm.VMEntry;
import lib.lunar.nativemc.Version;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunarLibSpigot extends JavaPlugin {
	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 0;

	@Override
	public void onEnable() {
		Bukkit.getLogger().log(Level.INFO, "LunarLib loaded.");
		Bukkit.getLogger().log(Level.INFO, "Running on " + VMEntry.NATIVE_JVM_BIT_VERSION + "-bit " + (VMEntry.NATIVE_JVM_HOTSPOT ? "HotSpot JVM" : "JVM") + " -UseCompressedOops = " + VMEntry.NATIVE_JVM_COMPRESSED_OOPS);
		Bukkit.getLogger().log(Level.INFO, "NMS version: " + Version.this_version);
	}

	@Override
	public void onDisable() {

	}
}
