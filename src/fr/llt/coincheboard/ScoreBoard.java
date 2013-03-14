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
		PlayFragment playFragement = (PlayFragment) getSupportFragmentManager()
				.findFragmentById(R.id.playFragment);
		playFragement.initCoincheButtonVisibility();

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
