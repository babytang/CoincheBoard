package fr.llt.coincheboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import fr.llt.coincheboard.data.Bet;
import fr.llt.coincheboard.data.Game;
import fr.llt.coincheboard.data.ScoreTurn;

public class PlayFragment extends Fragment {
	// 0-2 are team
	// 3 is dog
	private final int[] score = new int[4];
	private int lastUpdatedIndex = -1;

	private static final int[] scoreId = new int[] { R.id.teamOneScore,
			R.id.teamTwoScore, R.id.teamThreeScore, R.id.dogScore };

	private static final int[] bonusId = new int[] { R.id.teamOneBonus,
			R.id.teamTwoBonus, R.id.teamThreeBonus };

	private static final int[] coincheId = new int[] { R.id.teamOneCoinche,
			R.id.teamTwoCoinche, R.id.teamThreeCoinche };

	private PlayFragmentListener listener;

	public static interface PlayFragmentListener {
		void onPlayDone();

		Game getGame();
	}

	private class MyTextWatcher implements TextWatcher {
		private final int index;

		public MyTextWatcher(int index) {
			this.index = index;
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// Nothing to do.
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// Nothing to do.
		}

		@Override
		public void afterTextChanged(Editable editable) {
			if (editable.length() != 0 && this.index != lastUpdatedIndex) {
				scoreUpdated(this.index, Integer.parseInt(editable.toString()));
			} else if (editable.length() == 0) {
				score[this.index] = -1;
				enableValidButton(false);
			}
		}
	}

	private class CoincheButtonListener implements OnClickListener {
		private int team;

		public CoincheButtonListener(int team) {
			this.team = team;
		}

		@Override
		public void onClick(View v) {
			coinched(this.team);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (PlayFragmentListener) activity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_play_fragment,
				container, false);

		if (this.listener.getGame().getTeams().getNumberOfTeam() == 2) {
			view.findViewById(R.id.teamThreePlay).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.teamThreeBonus)
					.setVisibility(View.INVISIBLE);
			view.findViewById(R.id.dogScore).setVisibility(View.INVISIBLE);
		}

		Button button = (Button) view.findViewById(R.id.validButton);
		button.setEnabled(false);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				validButtonClicked();
			}
		});

		for (int i = 0; i < this.score.length; i++) {
			EditText editText = (EditText) view.findViewById(scoreId[i]);
			editText.addTextChangedListener(new MyTextWatcher(i));
		}

		this.reset();

		for (int i = 0; i < coincheId.length; i++) {
			Button coincheButton = (Button) view.findViewById(coincheId[i]);
			coincheButton.setOnClickListener(new CoincheButtonListener(i));
		}

		return view;
	}

	private Button enableValidButton(boolean enable) {
		Button button = (Button) getView().findViewById(R.id.validButton);
		button.setEnabled(enable);
		return button;
	}

	private void reset() {
		for (int i = 0; i < this.score.length; i++) {
			this.score[i] = -1;
		}
		this.lastUpdatedIndex = -1;
	}

	private void scoreUpdated(int index, int score) {
		if (score < 0) {
			Button button = (Button) this.getActivity().findViewById(
					R.id.validButton);
			button.setEnabled(false);
			return;
		}

		if (this.lastUpdatedIndex != -1) {
			this.score[this.lastUpdatedIndex] = -1;
		}

		this.score[index] = score;
		int size = this.listener.getGame().getTeams().getNumberOfTeam() == 2 ? 2
				: 4;
		int total = 0;
		boolean onlyOneNotSetted = false;
		int indexNotSetted = -1;
		for (int i = 0; i < size; i++) {
			if (this.score[i] == -1) {
				onlyOneNotSetted = indexNotSetted == -1;
				indexNotSetted = i;
			} else {
				total += this.score[i];
			}
		}
		if (onlyOneNotSetted) {
			this.lastUpdatedIndex = indexNotSetted;
		}

		if (onlyOneNotSetted) {
			int newScore = total == 0 ? 252 : 162 - total;
			if (newScore >= 0) {
				total += newScore;
				this.score[indexNotSetted] = newScore;
				EditText editText = (EditText) this.getActivity().findViewById(
						scoreId[indexNotSetted]);
				editText.setText(String.valueOf(newScore));
			}
		}

		boolean enableValid = total == 252 || total == 162;
		Button button = (Button) this.getView().findViewById(R.id.validButton);
		Log.w("ScoreBoard", "" + button);
		button.setEnabled(enableValid);
	}

	private void validButtonClicked() {
		Game game = this.listener.getGame();
		Bet bet = game.getBet();

		int[] scores = new int[game.getTeams().getNumberOfTeam()];
		for (int i = 0; i < scores.length; i++) {
			EditText editText = (EditText) this.getActivity().findViewById(
					bonusId[i]);
			if (editText.getText().length() != 0) {
				scores[i] = Integer.parseInt(editText.getText().toString());
			}
		}

		int totalTeam = scores[bet.getDeclarer()]
				+ this.score[bet.getDeclarer()];
		int bestScore = -1;
		boolean win = totalTeam >= bet.getBet();
		int winner = bet.getDeclarer();
		boolean specialCase = false;
		for (int i = 0; i < scores.length; i++) {
			if (i == bet.getDeclarer()) {
				continue;
			}
			int otherScore = scores[i] + this.score[i];
			if (totalTeam <= otherScore || !win) {
				win = false;
				if (otherScore > bestScore) {
					bestScore = otherScore;
					winner = i;
				} else if (otherScore == bestScore) {
					specialCase = true;
				}
			}
		}

		// TODO very ugly should be simplified.
		if (bet.isCoinched()) {
			int multiplier = 2;
			if (bet.isOverCoinched()) {
				multiplier = 3;
			}
			for (int i = 0; i < scores.length; i++) {
				if (i == bet.getDeclarer()) {
					if (win) {
						scores[i] += bet.getBet()
								+ (this.score[i] + this.score[bet.getCoinched()])
								* multiplier;
					}
				} else {
					if (i == bet.getCoinched()) {
						if (winner == i && !specialCase) {
							scores[i] += bet.getBet()
									+ (this.score[i] + this.score[bet
											.getDeclarer()]) * multiplier;
						}
					} else {
						if (winner == i || specialCase) {
							scores[i] += bet.getBet()
									+ (this.score[i]
											+ this.score[bet.getDeclarer()] + this.score[bet
												.getCoinched()]) * multiplier;
						} else {
							scores[i] += this.score[i];
						}
					}
				}
			}
		} else {
			for (int i = 0; i < scores.length; i++) {
				if (i == bet.getDeclarer()) {
					if (win) {
						scores[i] += bet.getBet() + this.score[i];
					}
				} else {
					if (specialCase || winner == i) {
						scores[i] += bet.getBet() + this.score[i]
								+ this.score[bet.getDeclarer()];
					} else {
						scores[i] += this.score[i];
					}
				}
			}
		}

		this.roundScore(scores);
		ScoreTurn turn = new ScoreTurn(bet, scores);
		game.getScore().addTurn(turn);
		View miseFragment = getActivity().findViewById(R.id.playFragment);
		miseFragment.setVisibility(View.INVISIBLE);

		this.reset();
		this.resetAll(bonusId);
		this.resetAll(scoreId);
		this.resetAll(R.id.dogScore);
		Button button = (Button) getView().findViewById(R.id.validButton);
		button.setEnabled(false);

		listener.onPlayDone();
	}

	private void roundScore(int[] scores) {
		for (int i = 0; i < scores.length; i++) {
			int modulo = scores[i] % 10;
			scores[i] = scores[i] - modulo;
			if (modulo > 5) {
				scores[i] += 10;
			}
		}
	}
	
	private void resetAll(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			EditText editText = (EditText) getActivity().findViewById(ids[i]);
			editText.setText("");
		}
	}

	public void initCoincheButtonVisibility() {
		Bet bet = this.listener.getGame().getBet();
		for (int i = 0; i < coincheId.length; i++) {
			Button button = (Button) getView().findViewById(coincheId[i]);
			button.setText(R.string.coinche);
			if (bet.getDeclarer() == i) {
				button.setVisibility(View.INVISIBLE);
			} else {
				button.setVisibility(View.VISIBLE);
			}
		}
	}

	private void coinched(int team) {
		Bet bet = this.listener.getGame().getBet();
		if (bet.isCoinched()) {
			bet.setOverCoinched(true);
			Button button = (Button) getView().findViewById(coincheId[team]);
			button.setVisibility(View.INVISIBLE);
		} else {
			bet.setCoinched(team);
			for (int i = 0; i < coincheId.length; i++) {
				Button button = (Button) getView().findViewById(coincheId[i]);
				if (bet.getDeclarer() == i) {
					button.setText(R.string.surCoinche);
					button.setVisibility(View.VISIBLE);
				} else {
					button.setVisibility(View.INVISIBLE);
				}
			}
		}
	}
}
