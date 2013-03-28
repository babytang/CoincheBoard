package fr.llt.coincheboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void newGame(View view) {
		Intent intent = new Intent(this, NewGameActivity.class);
		startActivity(intent);
	}

	public void settings(View view) {
		startActivityForResult(new Intent(this, SettingsActivity.class), 1);	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_settings) {
			this.settings(null);
		}
		return super.onOptionsItemSelected(item);
	}
}
