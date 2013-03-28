package fr.llt.coincheboard.logic;

/**
 * This class will compute the score for each teams with the help of various
 * strategies than can be configured by the user.
 */
public class ScoreComputer {
	private RawScoreComputer rawScoreComputer;
	private TotalScoreComputer totalScoreComputer;
	private RoundComputer roundComputer;

	public ScoreComputer(RoundComputer roundComputer) {
		this.rawScoreComputer = new RawScoreComputerWinWithAnnounce();
		this.totalScoreComputer = new TotalScoreComputerCoinchePointWithoutAnnounce(
				2, 3);
		this.roundComputer = roundComputer;
	}

	public ScoreComputer(RawScoreComputer rawScoreComputer,
			RoundComputer roundComputer, TotalScoreComputer totalScoreComputer) {
		this.rawScoreComputer = rawScoreComputer;
		this.totalScoreComputer = totalScoreComputer;
		this.roundComputer = roundComputer;
	}

	/**
	 * Compute the score for each team. The result will be available in
	 * data.getScore().
	 * 
	 * @param data
	 *            The score data for each team.
	 */
	public void compute(ComputerData data) {
		this.rawScoreComputer.compute(data);
		this.totalScoreComputer.compute(data);
		this.roundComputer.compute(data);
	}
}
