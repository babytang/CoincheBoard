package fr.llt.coincheboard.logic;

import fr.llt.coincheboard.data.Bet;

/**
 * This class stores the datas needed during the computation of the score.
 */
public class ComputerData {
	/**
	 * The bet on this turn.
	 */
	private final Bet bet;

	/**
	 * The score that each teams have done with cards.
	 */
	private final int[] baseScore;

	/**
	 * The announce that each teams have.
	 */
	private final int[] bonusScore;

	/**
	 * The belote is a special kinds of annouce so put it away from the bonus
	 * score.
	 */
	private final int[] belote;

	/**
	 * This array is used for intermediate computations.
	 */
	private final int[] score;

	public ComputerData(Bet bet, int[] baseScore, int[] bonusScore, int[] belote) {
		this.bet = bet;
		this.baseScore = baseScore;
		this.bonusScore = bonusScore;
		this.belote = belote;
		this.score = new int[this.bonusScore.length];
	}

	public Bet getBet() {
		return bet;
	}

	public int getBaseScore(int index) {
		return this.baseScore[index];
	}

	public int getBonusScore(int index) {
		return this.bonusScore[index];
	}

	public int getBelote(int index) {
		return this.belote[index];
	}

	public int getScore(int index) {
		return this.score[index];
	}

	public int[] getScore() {
		return this.score;
	}

	public void setScore(int index, int score) {
		this.score[index] = score;
	}

	public int getNumberOfTeam() {
		return this.score.length;
	}
}
