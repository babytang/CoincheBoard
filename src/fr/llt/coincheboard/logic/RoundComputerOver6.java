package fr.llt.coincheboard.logic;

public class RoundComputerOver6 implements RoundComputer {
	@Override
	public void compute(ComputerData data) {
		for (int i = 0; i < data.getNumberOfTeam(); i++) {
			int modulo = data.getScore(i) % 10;
			int result = data.getScore(i) - modulo;
			if (modulo > 5) {
				result += 10;
			}
			data.setScore(i, result);
		}
	}
}
