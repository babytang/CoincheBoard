package fr.llt.coincheboard.data;

import android.os.Parcel;

class Util {
	private Util() {
		throw new AssertionError("Utility class do not instanciate");
	}
	
	static void writeArray(Parcel dest, int[] array) {
		dest.writeInt(array.length);
		for (int i = 0; i < array.length; i++) {
			dest.writeInt(array[i]);
		}
	}
	
	static int[] readArray(Parcel source) {
		int size = source.readInt();
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = source.readInt();
		}
		return result;
	}
}
