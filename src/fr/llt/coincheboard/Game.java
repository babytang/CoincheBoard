package fr.llt.coincheboard;

import java.util.ArrayList;
import java.util.List;

public class Game {
	public static final Game singleton = new Game();

	private static CharSequence[] playersName;
	private static List<int[]> scores;
	private static int[] totalScores;

	private static int mise;

	private static int team;

	public static void setPlayersName(CharSequence[] _playersName) {
		playersName = _playersName;
		scores = new ArrayList<int[]>();
		totalScores = new int[getNbOfTeam()];
	}

	public static CharSequence[] getPlayersName() {
		return playersName;
	}

	public static int getNbOfTeam() {
		return playersName.length == 4 ? 2 : 3;
	}

	public static int[] getTotalScores() {
		return totalScores;
	}

	public static int[] getScores(int index) {
		return scores.get(index);
	}

	public static int getScoresSize() {
		return scores.size();
	}

	public static void setMise(int mise, int team) {
		Game.mise = mise;
		Game.team = team;
	}

	public static List<int[]> getScores() {
		return scores;
	}

	public static void setScores(List<int[]> scores) {
		Game.scores = scores;
	}

	public static int getMise() {
		return mise;
	}

	public static int getTeam() {
		return team;
	}

	public static void setScore(int... scores) {
		Game.scores.add(scores);
		for (int i = 0; i < scores.length; i++) {
			totalScores[i] += scores[i];
		}
	}
}
