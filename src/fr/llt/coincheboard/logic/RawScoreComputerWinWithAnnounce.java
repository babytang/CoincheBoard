package fr.llt.coincheboard.logic;

public class RawScoreComputerWinWithAnnounce implements RawScoreComputer {
	@Override
	public void compute(ComputerData data) {
		for (int i = 0; i < data.getNumberOfTeam(); i++) {
			int rawScore = data.getBaseScore(i) + data.getBonusScore(i)
					+ data.getBelote(i);
			data.setScore(i, rawScore);
		}
	}
}