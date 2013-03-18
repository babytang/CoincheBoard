package fr.llt.coincheboard;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import fr.llt.coincheboard.MiseFragment.MiseFragmentListener;
import fr.llt.coincheboard.PlayFragment.PlayFragmentListener;
import fr.llt.coincheboard.data.Game;

public class ScoreBoard extends FragmentActivity implements
		MiseFragmentListener, PlayFragmentListener {
	private ScoreAdapter scoreAdapter;
	private Game game;

	public Game getGame() {
		return this.game;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setupActionBar();

		this.game = (Game) this.getLastCustomNonConfigurationInstance();
		if (this.game == null) {
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			this.game = (Game) extras.get(NewGameActivity.GAME);
		}

		setContentView(R.layout.activity_score_board);
		this.updateCurrentDealer();

		GridView scoreBoard = (GridView) findViewById(R.id.scoreBoard);
		scoreBoard.setNumColumns(this.game.getTeams().getNumberOfTeam());

		this.scoreAdapter = new ScoreAdapter(this);
		scoreBoard.setAdapter(this.scoreAdapter);

		findViewById(R.id.playFragment).setVisibility(View.INVISIBLE);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			View playView = this.findViewById(R.id.playFragment);
			if (playView.getVisibility() == View.VISIBLE) {
				// In this case return to the bet activity.
				playView.setVisibility(View.INVISIBLE);
				this.previousDealer();
				this.onPlayDone();

			} else {
				this.showQuitDialog();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showQuitDialog() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					ScoreBoard.this.finish();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.quitMessage)
				.setPositiveButton(R.string.yesMessage, dialogClickListener)
				.setNegativeButton(R.string.noMessage, dialogClickListener)
				.show();
	}

	private void previousDealer() {
		int currentDealer = this.game.getDealer() - 1;
		if (currentDealer < 0) {
			currentDealer = this.game.getTeams().getNumberOfPlayer() - 1;
		}
		this.game.setDealer(currentDealer);
	}

	private void nextDealer() {
		int currentDealer = this.game.getDealer() + 1;
		if (currentDealer >= this.game.getTeams().getNumberOfPlayer()) {
			currentDealer = 0;
		}
		this.game.setDealer(currentDealer);
		this.updateCurrentDealer();
	}

	private void updateCurrentDealer() {
		TextView textView = (TextView) findViewById(R.id.dealerTextView);
		textView.setText(this.game.getTeams().getName(this.game.getDealer()));
	}

	public void updateScore(View view) {
	}

	@Override
	public void onSkip() {
		this.nextDealer();
	}

	@Override
	public void onMiseDone() {
		PlayFragment playFragement = (PlayFragment) getSupportFragmentManager()
				.findFragmentById(R.id.playFragment);
		playFragement.initBeloteButtonVisibility();

		findViewById(R.id.playFragment).setVisibility(View.VISIBLE);
	}

	@Override
	public void onPlayDone() {
		findViewById(R.id.miseFragment).setVisibility(View.VISIBLE);
		this.scoreAdapter.notifyDataSetChanged();

		MiseFragment miseFragment = (MiseFragment) getSupportFragmentManager()
				.findFragmentById(R.id.miseFragment);
		miseFragment.skip(null);
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return this.game;
	}

}
