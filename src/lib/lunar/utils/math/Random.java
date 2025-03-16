package lib.lunar.utils.math;

public class Random {
	private static final java.util.Random random = new java.util.Random();

	public static boolean check(double chance) {
		return random.nextDouble() < chance;
	}
}
