package fr.llt.coincheboard.logic;

import fr.llt.coincheboard.data.Bet;

/**
 * This class compute the total score for each team.
 */
public abstract class TotalScoreComputer {
	protected final int coincheCoeff;
	protected final int surCoincheCoeff;

	public TotalScoreComputer(int coincheCoeff, int surCoincheCoeff) {
		this.coincheCoeff = coincheCoeff;
		this.surCoincheCoeff = surCoincheCoeff;
	}

	public final void compute(ComputerData data) {
		if (this.checkDeclarerWin(data)) {
			this.declarerWin(data);
		} else {
			this.declarerLose(data);
		}
	}

	private void declarerLose(ComputerData data) {
		Bet bet = data.getBet();
		int declarer = bet.getDeclarer();
		int[] opponent = findOpponent(data, declarer);
		boolean specialCase = specialCase(data, opponent);
		int opponentWinner = findOpponentWiner(data, opponent);
		int coeff = findCoeff(bet);

		int[] totalOpponent = new int[opponent.length];

		int totalDeclarer = data.getBelote(declarer);
		for (int i = 0; i < opponent.length; i++) {
			int adversaryCurrent = opponent[i];
			int adversaryOther = -1;
			if (opponent.length == 2) {
				adversaryOther = opponent[i == 0 ? 1 : 0];
			}

			if (specialCase) {
				// Only same score not coinched !
				totalOpponent[i] = this.computeCoinchedTotal(
						data.getBaseScore(declarer) / 2
								+ data.getBaseScore(adversaryCurrent),
						data.getBonusScore(declarer) / 2
								+ data.getBonusScore(adversaryCurrent),
						data.getBelote(adversaryCurrent), bet.getBet(), coeff);

			} else if (adversaryCurrent == opponentWinner) {
				if (adversaryCurrent == bet.getCoinched()
						|| bet.isCoinched() == false) {
					totalOpponent[i] = this.computeCoinchedTotal(
							data.getBaseScore(declarer)
									+ data.getBaseScore(adversaryCurrent),
							data.getBonusScore(declarer)
									+ data.getBonusScore(adversaryCurrent),
							data.getBelote(adversaryCurrent), bet.getBet(),
							coeff);

				} else {
					totalOpponent[i] = this.computeCoinchedTotal(
							data.getBaseScore(declarer)
									+ data.getBaseScore(adversaryCurrent)
									+ data.getBaseScore(adversaryOther),
							data.getBonusScore(declarer)
									+ data.getBonusScore(adversaryCurrent)
									+ data.getBonusScore(adversaryOther),
							data.getBelote(adversaryCurrent), bet.getBet(),
							coeff);
				}

			} else if (adversaryCurrent == bet.getCoinched()) {
				totalOpponent[i] = data.getBelote(adversaryCurrent);

			} else {
				if (data.getBaseScore(adversaryCurrent) == 0) {
					totalOpponent[i] = data.getBelote(adversaryCurrent);
				} else {
					totalOpponent[i] = data.getBaseScore(adversaryCurrent)
							+ data.getBonusScore(adversaryCurrent)
							+ data.getBelote(adversaryCurrent);
				}
			}
		}

		data.setScore(declarer, totalDeclarer);
		for (int i = 0; i < opponent.length; i++) {
			data.setScore(opponent[i], totalOpponent[i]);
		}
	}

	private void declarerWin(ComputerData data) {
		Bet bet = data.getBet();
		int declarer = bet.getDeclarer();
		int[] opponent = findOpponent(data, declarer);
		int coeff = findCoeff(bet);

		int totalDeclarer;
		int[] totalOpponent = new int[opponent.length];

		if (coeff == 1) {
			totalDeclarer = data.getBaseScore(declarer)
					+ data.getBonusScore(declarer) + data.getBelote(declarer)
					+ bet.getBet();
			for (int i = 0; i < opponent.length; i++) {
				if (data.getBaseScore(opponent[i]) == 0) {
					totalOpponent[i] = data.getBelote(opponent[i]);
				} else {
					totalOpponent[i] = data.getBaseScore(opponent[i])
							+ data.getBonusScore(opponent[i])
							+ data.getBelote(opponent[i]);
				}
			}

		} else {
			totalDeclarer = this.computeCoinchedTotal(
					data.getBaseScore(declarer)
							+ data.getBaseScore(bet.getCoinched()),
					data.getBonusScore(declarer), data.getBelote(declarer),
					bet.getBet(), coeff);
			for (int i = 0; i < opponent.length; i++) {
				int adversary = opponent[i];
				if (adversary == bet.getCoinched()) {
					totalOpponent[i] = data.getBelote(adversary);
				} else {
					totalOpponent[i] = data.getBaseScore(adversary)
							+ data.getBonusScore(adversary)
							+ data.getBelote(adversary);
				}
			}
		}

		data.setScore(declarer, totalDeclarer);
		for (int i = 0; i < opponent.length; i++) {
			data.setScore(opponent[i], totalOpponent[i]);
		}
	}

	private int findCoeff(Bet bet) {
		int coeff = 1;
		if (bet.isOverCoinched()) {
			coeff = this.surCoincheCoeff;
		} else if (bet.isCoinched()) {
			coeff = this.coincheCoeff;
		}
		return coeff;
	}

	private int[] findOpponent(ComputerData data, int declarer) {
		int[] opponent = new int[data.getNumberOfTeam() - 1];
		for (int i = 0; i < opponent.length; i++) {
			if (declarer == i) {
				opponent[i] = i + 1;
			} else if (i == 0) {
				opponent[i] = 0;
			} else {
				opponent[i] = opponent[i - 1] + 1;
			}
		}
		return opponent;
	}

	protected abstract int computeCoinchedTotal(int baseScore, int bonusScore,
			int belote, int bet, int coeff);

	private boolean checkDeclarerWin(ComputerData data) {
		Bet bet = data.getBet();
		boolean declarerWinTheBet = data.getScore(bet.getDeclarer()) >= bet
				.getBet();
		boolean declarerHasTheBestScore = this.findBestTeam(data) == bet
				.getDeclarer();
		return declarerWinTheBet && declarerHasTheBestScore;
	}

	private int findBestTeam(ComputerData data) {
		int bestScore = -1;
		int bestTeam = -1;
		for (int i = 0; i < data.getNumberOfTeam(); i++) {
			if (bestScore < data.getScore(i)) {
				bestScore = data.getScore(i);
				bestTeam = i;
			} else if (bestScore == data.getScore(i)) {
				Bet bet = data.getBet();
				// If the declarer has the same score of an opponent => he
				// should lose !
				if (bet.getDeclarer() != i) {
					bestTeam = i;
				}
			}
		}
		return bestTeam;
	}

	// Special case is when in 3 teams mode, the two opponents get the same
	// score and not coinched.
	private boolean specialCase(ComputerData data, int[] opponent) {
		boolean result = opponent.length == 2;
		return result
				&& data.getScore(opponent[0]) == data.getScore(opponent[1])
				&& data.getBet().isCoinched() == false;
	}

	private int findOpponentWiner(ComputerData data, int[] opponent) {
		int result = opponent[0];
		if (opponent.length == 2) {
			if (data.getScore(opponent[0]) < data.getScore(opponent[1])) {
				result = opponent[1];
			} else if (data.getScore(opponent[0]) == data.getScore(opponent[1])) {
				// If same but coinched => the winner is the other opponent.
				if (data.getBet().getCoinched() == result) {
					result = opponent[1];
				}
			}
		}
		return result;
	}

}
