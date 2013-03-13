package fr.llt.coincheboard.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class will store the result of each turns.
 */
public class ScoreTurn implements Parcelable {
	public static final Creator<ScoreTurn> CREATOR = new Creator<ScoreTurn>() {
		@Override
		public ScoreTurn[] newArray(int size) {
			return new ScoreTurn[size];
		}
		
		@Override
		public ScoreTurn createFromParcel(Parcel source) {
			return new ScoreTurn(source);
		}
	};
	
	/**
	 * The bet used this turn. XXX this is not used yet but may be useful when
	 * game statistics will be added.
	 */
	private final Bet bet;

	/**
	 * The score done by each teams (With announce).
	 */
	private final int[] scores;

	public ScoreTurn(Bet bet, int[] scores) {
		this.bet = bet;
		this.scores = scores;
	}

	private ScoreTurn(Parcel source) {
		this.bet = source.readParcelable(Bet.class.getClassLoader());
		this.scores = Util.readArray(source);
	}

	public Bet getBet() {
		return this.bet;
	}

	public int getScores(int team) {
		return this.scores[team];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.bet, flags);
		Util.writeArray(dest, this.scores);
	}
}
