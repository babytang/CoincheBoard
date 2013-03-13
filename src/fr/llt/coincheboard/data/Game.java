package fr.llt.coincheboard.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class will store the current state of the game (Teams, Score, the
 * current dealer and the current bet)
 */
public class Game implements Parcelable {
	public static final Creator<Game> CREATOR = new Creator<Game>() {
		
		@Override
		public Game[] newArray(int size) {
			return new Game[size];
		}
		
		@Override
		public Game createFromParcel(Parcel source) {
			return new Game(source);
		}
	};
	
	private final Teams teams;
	
	private final Score score;
	
	private Bet bet;

	/**
	 * 0: north, 1: west, 2: south, 3: east
	 */
	private int dealer;

	public Game(Teams teams, int firstDealer) {
		this.teams = teams;
		this.score = new Score(this.teams.getNumberOfTeam());
		this.dealer = firstDealer;
	}

	private Game(Parcel source) {
		this.bet = source.readParcelable(Bet.class.getClassLoader());
		this.dealer = source.readInt();
		this.score = source.readParcelable(Score.class.getClassLoader());
		this.teams = source.readParcelable(Teams.class.getClassLoader());
	}

	public Bet getBet() {
		return bet;
	}

	public void setBet(Bet bet) {
		this.bet = bet;
	}

	public int getDealer() {
		return dealer;
	}

	public void setDealer(int dealer) {
		this.dealer = dealer;
	}

	public Teams getTeams() {
		return this.teams;
	}

	public Score getScore() {
		return this.score;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.bet, flags);
		dest.writeInt(this.dealer);
		dest.writeParcelable(this.score, flags);
		dest.writeParcelable(this.teams, flags);
	}
}
