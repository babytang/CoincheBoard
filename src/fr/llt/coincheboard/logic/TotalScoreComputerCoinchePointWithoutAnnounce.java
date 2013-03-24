package fr.llt.coincheboard.logic;

/**
 * This class compute the total score
 */
public class TotalScoreComputerCoinchePointWithoutAnnounce extends
		TotalScoreComputer {
	public TotalScoreComputerCoinchePointWithoutAnnounce(int coincheCoeff,
			int surCoincheCoeff) {
		super(coincheCoeff, surCoincheCoeff);
	}

	@Override
	protected int computeCoinchedTotal(int baseScore, int bonusScore,
			int belote, int bet, int coeff) {
		return baseScore * coeff + bonusScore + belote + bet;
	}
}
