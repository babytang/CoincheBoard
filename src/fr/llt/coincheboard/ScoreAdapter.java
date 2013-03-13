package fr.llt.coincheboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.llt.coincheboard.data.Game;
import fr.llt.coincheboard.data.Score;
import fr.llt.coincheboard.data.Teams;

public class ScoreAdapter extends BaseAdapter {
	private final Context context;
	private final Game game;

	public ScoreAdapter(ScoreBoard context) {
		this.context = context;
		this.game = context.getGame();
	}

	@Override
	public int getCount() {
		int headerRow = getNbOfRowInHeader();// + (this.playPart ? 1 : 0);
		return (this.game.getScore().getNumberOfTurn() + 1 + headerRow)
				* getNbOfColumn();
	}

	private int getNbOfColumn() {
		return this.game.getTeams().getNumberOfTeam();
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

		Teams teams = this.game.getTeams();
		Score score = this.game.getScore();

		if (row < this.getNbOfRowInHeader()) {
			result.setText(teams.getName(col + row * teams.getNumberOfTeam()));
		} else if (row >= this.getNbOfRowInHeader() + score.getNumberOfTurn()) {
			result.setText(String.valueOf(score.getTotal(col)));
		} else {
			row -= this.getNbOfRowInHeader();
			result.setText(String.valueOf(score.getTurn(row).getScores(col)));
		}

		return result;
	}
}
