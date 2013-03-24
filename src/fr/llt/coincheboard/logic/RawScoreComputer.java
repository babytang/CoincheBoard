package fr.llt.coincheboard.logic;

/**
 * This interface will compute the raw score for each team. The raw score is the
 * score that will be used to determine the winners.
 */
public interface RawScoreComputer {
	void compute(ComputerData data);
}
