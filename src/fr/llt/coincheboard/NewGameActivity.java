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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class NewGameActivity extends Activity implements
		OnCheckedChangeListener {
	public static final String DEALER = "fr.llt.coincheboard.dealer";

	public static final String IS_4_PLAYERS = "fr.llt.coincheboard.4Players";

	public static final String[] PLAYERS_NAME = new String[] {
			"fr.llt.coincheboard.playerNorth",
			"fr.llt.coincheboard.playerWest",
			"fr.llt.coincheboard.playerSouth", "fr.llt.coincheboard.playerEast" };

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

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
		linearLayout.requestFocus();

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
		Intent intent = new Intent(this, ScoreBoard.class);

		RadioButton radio4 = (RadioButton) findViewById(R.id.radio4Players);
		boolean fourPlayerEnabled = radio4.isChecked();
		intent.putExtra(IS_4_PLAYERS, fourPlayerEnabled);

		for (int i = 0; i < ID_EDIT_TEXT.length; i++) {
			if (i != 3 || fourPlayerEnabled) {
				TextView editText = (TextView) findViewById(ID_EDIT_TEXT[i]);
				CharSequence text = editText.getText();
				if (text.length() == 0) {
					TextView textView = (TextView) findViewById(ID_TEXT_VIEW[i]);
					intent.putExtra(PLAYERS_NAME[i], textView.getText());
				} else {
					intent.putExtra(PLAYERS_NAME[i], text);
				}
			}
		}

		Spinner spinner = (Spinner) findViewById(R.id.dealerSpinner);
		intent.putExtra(DEALER, spinner.getSelectedItemPosition());

		startActivity(intent);
	}
}
