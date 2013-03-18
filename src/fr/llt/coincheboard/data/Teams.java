package fr.llt.coincheboard.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class will store the data related to the available teams.
 */
public class Teams implements Parcelable {
	public static final Creator<Teams> CREATOR = new Creator<Teams>() {
		@Override
		public Teams createFromParcel(Parcel source) {
			return new Teams(source);
		}

		@Override
		public Teams[] newArray(int size) {
			return new Teams[size];
		}
	};

	/**
	 * The players name should be in this order: If 4 players, 0: north, 1:
	 * west, 2: south, 3: east => two teams (0, 2) and (1, 3). If 3 players, 0:
	 * north, 1: west, 2: south => three teams (0), (1) and (2).
	 */
	private final String[] playersName;

	public Teams(String[] playersName) {
		this.playersName = playersName;
	}

	private Teams(Parcel source) {
		int size = source.readInt();
		this.playersName = new String[size];
		for (int i = 0; i < size; i++) {
			this.playersName[i] = source.readString();
		}
	}

	public String getName(int index) {
		return this.playersName[index];
	}

	public int getNumberOfTeam() {
		if (this.playersName.length == 4) {
			return 2;
		}
		return 3;
	}

	public int getNumberOfPlayer() {
		return this.playersName.length;
	}

	public String getTeamName(int i) {
		String result;
		if (this.playersName.length == 4) {
			int index = i % 2;
			result = String.format("%1$s & %2$s", this.playersName[index],
					this.playersName[index + 2]);
		} else {
			result = this.playersName[i];
		}

		return result;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.playersName.length);
		for (int i = 0; i < this.playersName.length; i++) {
			dest.writeString(this.playersName[i]);
		}
	}
}
