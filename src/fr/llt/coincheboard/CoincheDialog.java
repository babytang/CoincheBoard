package fr.llt.coincheboard;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

public class CoincheDialog extends DialogFragment implements OnClickListener {
	public static interface CoincheDialogListener {
		void onCloseListener(int selectedTeam);
	}

	private static final int[] radioButtonId = new int[] {
			R.id.radioCoincheOne, R.id.radioCoincheTwo, R.id.radioCoincheThree };

	public static CoincheDialog newInstance() {
		CoincheDialog result = new CoincheDialog();
		return result;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setStyle(DialogFragment.STYLE_NO_TITLE,
		// android.R.style.Theme_Holo_Dialog);
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_coinche_dialog,
				container, false);

		for (int i = 0; i < radioButtonId.length; i++) {
			Button button = (Button) view.findViewById(radioButtonId[i]);
			button.setOnClickListener(this);
		}

		return view;
	}

	@Override
	public void onClick(View view) {
		// No good, how can we find the listener more cleanly ?
		CoincheDialogListener listener = (CoincheDialogListener) getFragmentManager()
				.findFragmentById(R.id.miseFragment);

		RadioGroup group = (RadioGroup) this.getView().findViewById(
				R.id.groupCoinche);
		int checkedId = group.getCheckedRadioButtonId();

		int selectedTeam = -1;
		for (int i = 0; i < radioButtonId.length; i++) {
			if (radioButtonId[i] == checkedId) {
				selectedTeam = i;
			}
		}

		listener.onCloseListener(selectedTeam);
	}

}
