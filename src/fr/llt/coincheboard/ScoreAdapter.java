package fr.llt.coincheboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreAdapter extends BaseAdapter {
	private final Context context;

	public ScoreAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		int headerRow = getNbOfRowInHeader();// + (this.playPart ? 1 : 0); 
		return (Game.getScoresSize() + 1 + headerRow) * getNbOfColumn();
	}

	private int getNbOfColumn() {
		return Game.getNbOfTeam();
	}

	private int getNbOfRowInHeader() {
		int headerRow = getNbOfColumn() == 2 ? 2 : 1;
		return headerRow;
	}

	@Override
	public Object getItem(int position) {
		// Not used.
		return null;
	}

	@Override
	public long getItemId(int position) {
		// Not used.
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView result;
		if (convertView == null) {
			result = new TextView(this.context);
		} else {
			result = (TextView) convertView;
		}
	
		int col = position % this.getNbOfColumn();
		int row = position / this.getNbOfColumn();
		
		if (row < this.getNbOfRowInHeader()) {
			result.setText(Game.getPlayersName()[col + row * Game.getNbOfTeam()]);
		} else if (row >= this.getNbOfRowInHeader() + Game.getScoresSize()) {
			result.setText(String.valueOf(Game.getTotalScores()[col]));
		} else {
			row -= this.getNbOfRowInHeader();
			result.setText(String.valueOf(Game.getScores(row)[col]));
		}
		
		return result;
	}
}
