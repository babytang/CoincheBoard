package fr.llt.coincheboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import fr.llt.coincheboard.MiseFragment.MiseFragmentListener;
import fr.llt.coincheboard.PlayFragment.PlayFragmentListener;

public class ScoreBoard extends FragmentActivity implements
		MiseFragmentListener, PlayFragmentListener {

	private ScoreAdapter scoreAdapter;
	private int currentDealer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		boolean fourPlayersEnabled = extras
				.getBoolean(NewGameActivity.IS_4_PLAYERS);
		CharSequence[] playersName = new CharSequence[fourPlayersEnabled ? 4
				: 3];
		for (int i = 0; i < NewGameActivity.PLAYERS_NAME.length; i++) {
			if (i != 3 || fourPlayersEnabled) {
				playersName[i] = extras
						.getCharSequence(NewGameActivity.PLAYERS_NAME[i]);
			}
		}
		Game.setPlayersName(playersName);
		this.currentDealer = extras.getInt(NewGameActivity.DEALER);

		setContentView(R.layout.activity_score_board);

		GridView scoreBoard = (GridView) findViewById(R.id.scoreBoard);

		this.updateCurrentDealer();

		scoreBoard.setNumColumns(Game.getNbOfTeam());

		scoreAdapter = new ScoreAdapter(this);
		scoreBoard.setAdapter(scoreAdapter);
		
		findViewById(R.id.playFragment).setVisibility(View.INVISIBLE);
	}

	private void nextDealer() {
		this.currentDealer += 1;
		if (this.currentDealer >= Game.getPlayersName().length) {
			this.currentDealer = 0;
		}
		this.updateCurrentDealer();
	}

	private void updateCurrentDealer() {
		TextView textView = (TextView) findViewById(R.id.dealerTextView);
		textView.setText(Game.getPlayersName()[this.currentDealer]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_board, menu);
		return true;
	}

	public void updateScore(View view) {
	}

	@Override
	public void onSkip() {
		this.nextDealer();
	}

	@Override
	public void onMiseDone() {
		findViewById(R.id.playFragment).setVisibility(View.VISIBLE);
	}

	@Override
	public void onPlayDone() {
		findViewById(R.id.miseFragment).setVisibility(View.VISIBLE);
		this.scoreAdapter.notifyDataSetChanged();
		
		MiseFragment miseFragment = (MiseFragment) getSupportFragmentManager().findFragmentById(R.id.miseFragment);
		miseFragment.skip(null);
	}
}
