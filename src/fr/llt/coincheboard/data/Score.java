package fr.llt.coincheboard.data;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class will store the total score for each team as well as the result of
 * each turn.
 */
public class Score implements Parcelable {
	public static final Creator<Score> CREATOR = new Creator<Score>() {

		@Override
		public Score[] newArray(int size) {
			return new Score[size];
		}

		@Override
		public Score createFromParcel(Parcel source) {
			return new Score(source);
		}
	};

	/**
	 * The score of each turn.
	 */
	private final List<ScoreTurn> scores;

	/**
	 * The total score for each teams.
	 */
	private final int[] totalScore;

	public Score(int numberOfTeam) {
		this.scores = new ArrayList<ScoreTurn>();
		this.totalScore = new int[numberOfTeam];
	}

	public Score(Parcel source) {
		this.scores = new ArrayList<ScoreTurn>();
		source.readList(this.scores, Score.class.getClassLoader());
		this.totalScore = Util.readArray(source);
	}

	public int getNumberOfTurn() {
		return this.scores.size();
	}
	
	public void addTurn(ScoreTurn scoreTurn) {
		this.scores.add(scoreTurn);
		for (int i = 0; i < this.totalScore.length; i++) {
			this.totalScore[i] += scoreTurn.getScores(i);
		}
	}

	public ScoreTurn getTurn(int index) {
		return this.scores.get(index);
	}

	public int getTotal(int team) {
		return this.totalScore[team];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.scores);
		Util.writeArray(dest, this.totalScore);
	}
}
