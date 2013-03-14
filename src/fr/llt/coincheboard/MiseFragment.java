package fr.llt.coincheboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;
import fr.llt.coincheboard.data.Bet;
import fr.llt.coincheboard.data.Game;

public class MiseFragment extends Fragment {
	public static interface MiseFragmentListener {
		void onSkip();

		void onMiseDone();
		
		Game getGame();
	}

	private static int[] colorToggleButtonId = new int[] { R.id.button4,
			R.id.button5, R.id.button6, R.id.button7 };

	private static int[] teamToggleButtonId = new int[] { R.id.validButton,
			R.id.teamTwoCoinche, R.id.teamThreeCoinche };

	private static int[] scoreToggleButtonId = new int[] { R.id.toggleButton1,
			R.id.toggleButton2, R.id.toggleButton3, R.id.toggleButton13,
			R.id.toggleButton17, R.id.toggleButton21, R.id.toggleButton4,
			R.id.toggleButton7, R.id.toggleButton10, R.id.toggleButton14,
			R.id.toggleButton18, R.id.toggleButton22, R.id.toggleButton5,
			R.id.toggleButton8, R.id.toggleButton11, R.id.toggleButton15,
			R.id.toggleButton19, R.id.toggleButton23, R.id.toggleButton6,
			R.id.toggleButton9, R.id.toggleButton12, R.id.toggleButton16,
			R.id.toggleButton20 };

	private boolean more;
	private int bet;
	private int trumpSuit;
	private int declarer;

	private MiseFragmentListener listener;

	private void reset() {
		this.more = true;
		this.bet = -1;
		this.trumpSuit = -1;
		this.declarer = -1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_mise_fragment,
				container, false);

		addClickListener(view, new OnClickListener() {
			@Override
			public void onClick(View v) {
				colorChosen(v);
			}
		}, colorToggleButtonId);

		addClickListener(view, new OnClickListener() {
			@Override
			public void onClick(View v) {
				teamChosen(v);
			}
		}, teamToggleButtonId);

		addClickListener(view, new OnClickListener() {
			@Override
			public void onClick(View v) {
				scoreButtonClicked(v);
			}
		}, scoreToggleButtonId);

		addClickListener(view, new OnClickListener() {
			@Override
			public void onClick(View v) {
				skip(v);
			}
		}, R.id.button8);

		addClickListener(view, new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateScoreButton(v);
			}
		}, R.id.button9);

		if (this.listener.getGame().getTeams().getNumberOfTeam() == 2) {
			view.findViewById(R.id.teamThreeCoinche).setVisibility(View.INVISIBLE);
		}
		
		return view;
	}

	private void addClickListener(View view, OnClickListener listener,
			int... ids) {
		for (int i = 0; i < ids.length; i++) {
			Button button = (Button) view.findViewById(ids[i]);
			button.setOnClickListener(listener);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.reset();
		this.updateScoreButton(null);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (MiseFragmentListener) activity;
	}

	public void updateScoreButton(View view) {
		this.more = !this.more;

		int start = getStartScore();

		for (int i = 0; i < scoreToggleButtonId.length; i++) {
			ToggleButton toggleButton = (ToggleButton) getActivity()
					.findViewById(scoreToggleButtonId[i]);
			String score = String.valueOf(start);
			toggleButton.setTextOff(score);
			toggleButton.setTextOn(score);
			toggleButton.setText(score);
			start += 10;
		}

		Button moreButton = (Button) getActivity().findViewById(R.id.button9);
		if (this.more) {
			moreButton.setText(R.string.less);
		} else {
			moreButton.setText(R.string.more);
		}
	}

	public void scoreButtonClicked(View view) {
		this.bet = this.getStartScore() + 10
				* resetOtherToggleButton(view.getId(), scoreToggleButtonId);
		this.checkMiseDone();
	}

	public void colorChosen(View view) {
		this.trumpSuit = resetOtherToggleButton(view.getId(),
				colorToggleButtonId);
		this.checkMiseDone();
	}

	public void teamChosen(View view) {
		this.declarer = resetOtherToggleButton(view.getId(), teamToggleButtonId);
		this.checkMiseDone();
	}

	private int resetOtherToggleButton(int idToFind, int... ids) {
		int result = -1;
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == idToFind) {
				result = i;
			} else {
				ToggleButton toggleButton = (ToggleButton) getActivity()
						.findViewById(ids[i]);
				toggleButton.setChecked(false);
			}
		}
		return result;
	}

	public void skip(View view) {
		this.reset();
		this.updateScoreButton(null);
		this.resetOtherToggleButton(-1, colorToggleButtonId);
		this.resetOtherToggleButton(-1, scoreToggleButtonId);
		this.resetOtherToggleButton(-1, teamToggleButtonId);

		this.listener.onSkip();
	}

	private int getStartScore() {
		int start = this.listener.getGame().getTeams().getNumberOfTeam() == 2 ? 80 : 70;
		start += this.more ? scoreToggleButtonId.length * 10 : 0;
		return start;
	}

	private void checkMiseDone() {
		if (this.bet != -1 && this.trumpSuit != -1 && this.declarer != -1) {
			Bet bet = new Bet(this.declarer, this.bet, this.trumpSuit);
			this.listener.getGame().setBet(bet);

			View miseFragment = getActivity().findViewById(R.id.miseFragment);
			miseFragment.setVisibility(View.INVISIBLE);
			this.listener.onMiseDone();
		}
	}
}
