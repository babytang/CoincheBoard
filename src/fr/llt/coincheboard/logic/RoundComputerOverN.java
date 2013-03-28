package fr.llt.coincheboard.logic;

public class RoundComputerOverN implements RoundComputer {
	private final int n;
	
	public RoundComputerOverN(int n) {
		this.n = n;
	}

	@Override
	public void compute(ComputerData data) {
		for (int i = 0; i < data.getNumberOfTeam(); i++) {
			int modulo = data.getScore(i) % 10;
			int result = data.getScore(i) - modulo;
			if (modulo >= n) {
				result += 10;
			}
			data.setScore(i, result);
		}
	}
}
