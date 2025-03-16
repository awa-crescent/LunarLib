package lib.lunar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lib.lunar.jvm.VMEntry;
import lib.lunar.nativemc.Version;
import net.fabricmc.api.ModInitializer;

public class LunarLibFabric implements ModInitializer {
	public static final String MOD_ID = "plenilune-lib";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("LunarLib loaded.");
		LOGGER.info("Running on " + VMEntry.NATIVE_JVM_BIT_VERSION + "-bit " + (VMEntry.NATIVE_JVM_HOTSPOT ? "HotSpot JVM" : "JVM") + " -UseCompressedOops = " + VMEntry.NATIVE_JVM_COMPRESSED_OOPS);
		LOGGER.info("NMS version: " + Version.this_version);
	}

}
