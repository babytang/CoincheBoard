package fr.llt.coincheboard;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import fr.llt.coincheboard.data.Game;
import fr.llt.coincheboard.data.Teams;

public class NewGameActivity extends Activity implements
		OnCheckedChangeListener {
	public static final String GAME = "fr.llt.coincheboard.game";

	private static final int[] ID_EDIT_TEXT = new int[] { R.id.playerNorthName,
			R.id.playerWestName, R.id.playerSouthName, R.id.playerEastName };;

	private static final int[] ID_TEXT_VIEW = new int[] { R.id.playerNorth,
			R.id.playerWest, R.id.playerSouth, R.id.playerEast };;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		// Show the Up button in the action bar.
		setupActionBar();

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.groupRadioPlayers);
		// radioGroup.check(R.id.radio4Players);
		radioGroup.setOnCheckedChangeListener(this);

		View view = findViewById(R.id.LinearLayout1);
		view.requestFocus();

		Spinner spinner = (Spinner) findViewById(R.id.dealerSpinner);
		// CursorAdapter adapter = new DealerAdapter(this, null);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item, findPlayerNames());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(0);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			startActivityForResult(new Intent(this, SettingsActivity.class), 1);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		boolean enabledEastPlayer = checkedId == R.id.radio4Players;
		View view = findViewById(R.id.playerEastName);
		view.setEnabled(enabledEastPlayer);
	}

	private List<CharSequence> findPlayerNames() {
		List<CharSequence> result = new ArrayList<CharSequence>();

		RadioButton radio = (RadioButton) findViewById(R.id.radio4Players);
		boolean fourPlayerEnabled = radio.isChecked();

		for (int i = 0; i < ID_EDIT_TEXT.length; i++) {
			if (i != 3 || fourPlayerEnabled) {
				TextView textView = (TextView) findViewById(ID_EDIT_TEXT[i]);
				CharSequence text = textView.getText();
				if (text.length() == 0) {
					result.add(textView.getHint().toString());
				} else {
					result.add(text.toString());
				}
			}
		}

		return result;
	}

	public void startGame(View view) {
		RadioButton radio4 = (RadioButton) findViewById(R.id.radio4Players);
		boolean fourPlayerEnabled = radio4.isChecked();
		String[] playersName = new String[fourPlayerEnabled ? 4 : 3];

		for (int i = 0; i < ID_EDIT_TEXT.length; i++) {
			if (i != 3 || fourPlayerEnabled) {
				TextView editText = (TextView) findViewById(ID_EDIT_TEXT[i]);
				CharSequence text = editText.getText();
				if (text.length() == 0) {
					TextView textView = (TextView) findViewById(ID_TEXT_VIEW[i]);
					playersName[i] = textView.getText().toString();
				} else {
					playersName[i] = text.toString();
				}
			}
		}
		Teams teams = new Teams(playersName);

		Spinner spinner = (Spinner) findViewById(R.id.dealerSpinner);
		Game game = new Game(teams, spinner.getSelectedItemPosition());

		Intent intent = new Intent(this, ScoreBoard.class);
		intent.putExtra(GAME, game);

		startActivity(intent);
	}
}
