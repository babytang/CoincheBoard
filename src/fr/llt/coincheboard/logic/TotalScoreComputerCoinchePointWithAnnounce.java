package fr.llt.coincheboard.logic;

/**
 * This class compute the total score
 */
public class TotalScoreComputerCoinchePointWithAnnounce extends
		TotalScoreComputer {
	public TotalScoreComputerCoinchePointWithAnnounce(int coincheCoeff,
			int surCoincheCoeff) {
		super(coincheCoeff, surCoincheCoeff);
	}

	@Override
	protected int computeCoinchedTotal(int baseScore, int bonusScore,
			int belote, int bet, int coeff) {
		return coeff * (baseScore + bonusScore) + belote + bet;
	}
}
