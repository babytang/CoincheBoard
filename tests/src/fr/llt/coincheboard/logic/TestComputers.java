package fr.llt.coincheboard.logic;

import java.util.ArrayList;
import java.util.Arrays;

import fr.llt.coincheboard.data.Bet;
import fr.llt.coincheboard.logic.TestComputers.Data.B;
import junit.framework.TestCase;

public class TestComputers extends TestCase {
	static class Data {
		Bet bet;
		int[] score;
		int[] bonus;
		int[] belote;
		int[] expectedResult;

		Data(Bet bet) {
			this.bet = bet;
		}

		@Override
		public String toString() {
			return "Data [bet=" + bet + ", score=" + Arrays.toString(score)
					+ ", bonus=" + Arrays.toString(bonus) + ", belote="
					+ Arrays.toString(belote) + ", expectedResult="
					+ Arrays.toString(expectedResult) + "]";
		}

		static interface AddScore {
			AddBonus s(int... score);
		}

		static interface AddBonus {
			AddBelote bo(int... bonus);
		}

		static interface AddBelote {
			AddCoinche b(int... belote);
		}

		static interface AddCoinche {
			AddExpected nc();
			AddExpected c(int coinched);
			AddExpected oc(int coinched);
		}
		
		static interface AddExpected {
			Data e(int... expected);
		}

		static class B implements AddScore, AddBonus, AddBelote, AddExpected, AddCoinche {
			private Data data;

			public B(Data data) {
				this.data = data;
			}

			static AddScore b(int declarer, int bet) {
				Data data = new Data(new Bet(declarer, bet, 0));
				return new B(data);
			}

			@Override
			public Data e(int... expected) {
				this.data.expectedResult = expected;
				return this.data;
			}

			@Override
			public AddCoinche b(int... belote) {
				this.data.belote = belote;
				return this;
			}

			@Override
			public AddBelote bo(int... bonus) {
				this.data.bonus = bonus;
				return this;
			}

			@Override
			public AddBonus s(int... score) {
				this.data.score = score;
				return this;
			}

			@Override
			public AddExpected nc() {
				return this;
			}

			@Override
			public AddExpected c(int coinched) {
				this.data.bet.setCoinched(coinched);
				return this;
			}

			@Override
			public AddExpected oc(int coinched) {
				this.data.bet.setCoinched(coinched);
				this.data.bet.setOverCoinched(true);
				return this;
			}
		}
	}

	private static Data[] getDataToTest() {
		ArrayList<Data> result = new ArrayList<Data>();
		
		// Simple win two players.
		result.add(B.b(0, 80).s(120, 40).bo(5, 5).b(1, 1).nc().e(206, 46));
		result.add(B.b(1, 80).s(40, 120).bo(5, 5).b(1, 1).nc().e(46, 206));
		
		// Simple win three players.
		result.add(B.b(0, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).nc().e(166, 46, 36));
		result.add(B.b(1, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).nc().e(36, 166, 46));
		result.add(B.b(2, 80).s(40, 30, 80).bo(5, 5, 5).b(1, 1, 1).nc().e(46, 36, 166));
		
		// Coinched win two players.
		result.add(B.b(0, 80).s(120, 40).bo(5, 5).b(1, 1).c(1).e(406, 1));
		result.add(B.b(1, 80).s(40, 120).bo(5, 5).b(1, 1).c(0).e(1, 406));

		// Coinched win three players.
		result.add(B.b(0, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).c(1).e(326, 1, 36));
		result.add(B.b(0, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).c(2).e(306, 46, 1));
		result.add(B.b(1, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).c(0).e(1, 306, 46));
		result.add(B.b(1, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).c(2).e(36, 326, 1));
		result.add(B.b(2, 80).s(40, 30, 80).bo(5, 5, 5).b(1, 1, 1).c(0).e(1, 36, 326));
		result.add(B.b(2, 80).s(40, 30, 80).bo(5, 5, 5).b(1, 1, 1).c(1).e(46, 1, 306));
		
		// overcoinched win two players.
		result.add(B.b(0, 80).s(120, 40).bo(5, 5).b(1, 1).oc(1).e(566, 1));
		result.add(B.b(1, 80).s(40, 120).bo(5, 5).b(1, 1).oc(0).e(1, 566));

		// overcoinched win three players.
		result.add(B.b(0, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).oc(1).e(446, 1, 36));
		result.add(B.b(0, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).oc(2).e(416, 46, 1));
		result.add(B.b(1, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).oc(0).e(1, 416, 46));
		result.add(B.b(1, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).oc(2).e(36, 446, 1));
		result.add(B.b(2, 80).s(40, 30, 80).bo(5, 5, 5).b(1, 1, 1).oc(0).e(1, 36, 446));
		result.add(B.b(2, 80).s(40, 30, 80).bo(5, 5, 5).b(1, 1, 1).oc(1).e(46, 1, 416));
		
		// Simple lose two players.
		result.add(B.b(0, 80).s(40, 120).bo(5, 5).b(1, 1).nc().e(1, 251));
		result.add(B.b(1, 80).s(120, 40).bo(5, 5).b(1, 1).nc().e(251, 1));
		
		// Simple lose three players.
		result.add(B.b(0, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).nc().e(1, 201, 46));
		result.add(B.b(1, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).nc().e(211, 1, 36));
		result.add(B.b(2, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).nc().e(201, 46, 1));
		
		// Simple lose three players but same score.
		result.add(B.b(0, 80).s(30, 60, 60).bo(5, 5, 5).b(1, 1, 1).nc().e(1, 163, 163));
		result.add(B.b(1, 80).s(60, 30, 60).bo(5, 5, 5).b(1, 1, 1).nc().e(163, 1, 163));
		result.add(B.b(2, 80).s(60, 60, 30).bo(5, 5, 5).b(1, 1, 1).nc().e(163, 163, 1));
		
		// Coinched lose two players.
		result.add(B.b(0, 80).s(40, 120).bo(5, 5).b(1, 1).c(1).e(1, 411));
		result.add(B.b(1, 80).s(120, 40).bo(5, 5).b(1, 1).c(0).e(411, 1));

		// Coinched lose three players.
		result.add(B.b(0, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).c(1).e(1, 311, 46));
		result.add(B.b(0, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).c(2).e(1, 396, 1));
		result.add(B.b(1, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).c(0).e(331, 1, 36));
		result.add(B.b(1, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).c(2).e(396, 1, 1));
		result.add(B.b(2, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).c(0).e(1, 396, 1));
		result.add(B.b(2, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).c(1).e(36, 331, 1));
		
		// Coinched lose three players but same score.
		result.add(B.b(0, 80).s(30, 60, 60).bo(5, 5, 5).b(1, 1, 1).c(2).e(1, 396, 1));
		result.add(B.b(1, 80).s(60, 30, 60).bo(5, 5, 5).b(1, 1, 1).c(2).e(396, 1, 1));
		result.add(B.b(2, 80).s(60, 60, 30).bo(5, 5, 5).b(1, 1, 1).c(0).e(1, 396, 1));
		
		// overcoinched lose two players.
		result.add(B.b(0, 80).s(40, 120).bo(5, 5).b(1, 1).oc(1).e(1, 571));
		result.add(B.b(1, 80).s(120, 40).bo(5, 5).b(1, 1).oc(0).e(571, 1));

		// overcoinched lose three players.
		result.add(B.b(0, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).oc(1).e(1, 421, 46));
		result.add(B.b(0, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).oc(2).e(1, 546, 1));
		result.add(B.b(1, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).oc(0).e(451, 1, 36));
		result.add(B.b(1, 80).s(80, 40, 30).bo(5, 5, 5).b(1, 1, 1).oc(2).e(546, 1, 1));
		result.add(B.b(2, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).oc(0).e(1, 546, 1));
		result.add(B.b(2, 80).s(30, 80, 40).bo(5, 5, 5).b(1, 1, 1).oc(1).e(36, 451, 1));
		
		// Capot test case
		result.add(B.b(0, 80).s(250, 0).bo(5, 5).b(1, 1).nc().e(336, 1));
		result.add(B.b(1, 80).s(250, 0).bo(5, 5).b(1, 1).nc().e(341, 1));
		result.add(B.b(0, 80).s(100, 0, 20).bo(5, 5, 5).b(1, 1, 1).nc().e(186, 1, 26));
		result.add(B.b(0, 80).s(100, 0, 0).bo(5, 5, 5).b(1, 1, 1).nc().e(186, 1, 1));
		result.add(B.b(2, 80).s(100, 0, 20).bo(5, 5, 5).b(1, 1, 1).nc().e(211, 1, 1));
		
		return result.toArray(new Data[result.size()]);
	}

	private ScoreComputer scoreComputer;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.scoreComputer = new ScoreComputer(new RoundComputer() {
			@Override
			public void compute(ComputerData data) {
				// No round
			}
		});
	}

	public void testAllData() {
		Data[] allData = getDataToTest();
		for (int i = 0; i < allData.length; i++) {
			Data data = allData[i];
			ComputerData computerData = new ComputerData(data.bet, data.score,
					data.bonus, data.belote);
			this.scoreComputer.compute(computerData);

			for (int j = 0; j < data.expectedResult.length; j++) {
				assertEquals(
						"Score of team " + j + " failed for " + data.toString(),
						data.expectedResult[j], computerData.getScore(j));
			}
		}
	}
}
