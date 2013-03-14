package fr.llt.coincheboard.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class will store the bet used for this turn.
 */
public class Bet implements Parcelable {
	public static final Creator<Bet> CREATOR = new Creator<Bet>() {
		@Override
		public Bet[] newArray(int size) {
			return new Bet[size];
		}
		
		@Override
		public Bet createFromParcel(Parcel source) {
			return new Bet(source);
		}
	};
	
	/**
	 * The team that win the bidding.
	 */
	private final int declarer;

	/**
	 * The value that the declarer should reach.
	 */
	private final int bet;

	/**
	 * The color of the trump. 0: spades, 1: hearts, 2: clubs and 3: diamonds
	 */
	private final int trumpSuit;
	
	/**
	 * -1 if not coinched, if not it's the team number that coinched.
	 */
	private int coinched;
	
	/**
	 * true if the declarer has over-coinched.
	 */
	private boolean overCoinched;

	public Bet(int declarer, int bet, int trumpSuit) {
		this.declarer = declarer;
		this.bet = bet;
		this.trumpSuit = trumpSuit;
		this.coinched = -1;
	}
	
	private Bet(Parcel source) {
		this.coinched = source.readInt();
		this.bet = source.readInt();
		this.declarer = source.readInt();
		this.overCoinched = source.readByte() == 1;
		this.trumpSuit = source.readInt();
	}

	public int getDeclarer() {
		return this.declarer;
	}

	public int getBet() {
		return this.bet;
	}

	public int getTrumpSuit() {
		return this.trumpSuit;
	}
	
	public int getCoinched() {
		return this.coinched;
	}
	
	public boolean isCoinched() {
		return this.coinched != -1;
	}

	public void setCoinched(int coinched) {
		this.coinched = coinched;
	}

	public boolean isOverCoinched() {
		return this.overCoinched;
	}

	public void setOverCoinched(boolean overCoinched) {
		this.overCoinched = overCoinched;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.bet);
		dest.writeInt(this.coinched);
		dest.writeInt(this.declarer);
		dest.writeByte((byte) (this.overCoinched ? 1 : 0));
		dest.writeInt(this.trumpSuit);
	}
}