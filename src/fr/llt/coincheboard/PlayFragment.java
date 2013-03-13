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

public class PlayFragment extends Fragment {
	// 0-2 are team
	// 3 is dog
	private final int[] score = new int[4];
	private int lastUpdatedIndex = -1;

	private static final int[] scoreId = new int[] { R.id.teamOneScore,
			R.id.teamTwoScore, R.id.teamThreeScore, R.id.dogScore };

	private static final int[] bonusId = new int[] { R.id.teamOneBonus,
			R.id.teamTwoBonus, R.id.teamThreeBonus };

	private PlayFragmentListener listener;

	public static interface PlayFragmentListener {
		void onPlayDone();
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (PlayFragmentListener) activity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_play_fragment,
				container, false);

		if (Game.getNbOfTeam() == 2) {
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
		int size = Game.getNbOfTeam() == 2 ? 2 : 4;
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
		this.lastUpdatedIndex = indexNotSetted;

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
		int[] scores = new int[Game.getNbOfTeam()];
		for (int i = 0; i < scores.length; i++) {
			EditText editText = (EditText) this.getActivity().findViewById(
					bonusId[i]);
			if (editText.getText().length() != 0) {
				scores[i] = Integer.parseInt(editText.getText().toString());
			}
		}

		Log.w("ScoreBoard",
				"Mise " + Game.getMise() + " Team " + Game.getTeam());
		int totalTeam = scores[Game.getTeam()] + this.score[Game.getTeam()];
		int bestScore = -1;
		boolean win = totalTeam >= Game.getMise();
		int winner = Game.getTeam();
		boolean specialCase = false;
		for (int i = 0; i < scores.length; i++) {
			if (i == Game.getTeam()) {
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

		for (int i = 0; i < scores.length; i++) {
			if (i == Game.getTeam()) {
				if (win) {
					scores[i] += Game.getMise() + this.score[i];
				}
			} else {
				if (specialCase || winner == i) {
					scores[i] += Game.getMise() + this.score[i]
							+ this.score[Game.getTeam()];
				} else {
					scores[i] += this.score[i];
				}
			}
		}

		Game.setScore(scores);
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

	private void resetAll(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			EditText editText = (EditText) getActivity().findViewById(ids[i]);
			editText.setText("");
		}
	}
}
