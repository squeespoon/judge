package util;



public class Util {

	

	public static int Random(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		int delta = max - min;
		int rnd = new java.util.Random().nextInt(delta);
		return min + rnd;
	}
}
