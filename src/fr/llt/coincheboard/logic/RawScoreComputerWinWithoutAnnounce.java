package fr.llt.coincheboard.logic;

public class RawScoreComputerWinWithoutAnnounce implements RawScoreComputer {
	@Override
	public void compute(ComputerData data) {
		for (int i = 0; i < data.getNumberOfTeam(); i++) {
			int rawScore = data.getBaseScore(i);
			data.setScore(i, rawScore);
		}
	}
}