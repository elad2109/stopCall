package com.example.stopcall.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.example.stopcall.app.activities.PopupActivity;
import com.example.stopcall.app.dal.CommentDal;
import com.example.stopcall.app.dal.PhoneDal;
import com.example.stopcall.app.dal.dto.Phone;
import com.google.inject.Inject;
import roboguice.fragment.RoboDialogFragment;

import java.util.List;
import java.util.Random;

public class YesNoDialogFragment extends RoboDialogFragment {

	private YesNoDialogFragmentListener mListener;
	@Inject private CommentDal commentDal;
	@Inject private PhoneDal phoneDal;
	private Phone phone;

	public YesNoDialogFragment() {

	}

	public static final String TAG = "YesNoDialogFragment";

	public interface YesNoDialogFragmentListener {
		void onDialogPositiveClick();
		void onDialogNegativeClick();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);


		phone = ((PopupActivity)activity).phone;
		
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (YesNoDialogFragmentListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		List<String> comments = commentDal.getAllItems(phone.id);
		
		Bundle args = new Bundle();
		String title = args.getString("title", "בטוח שאתה רוצה להתקשר?");
		String message = args.getString("message", getRandomComment(comments));

		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Send the positive button event back to the
								// host activity
								mListener.onDialogPositiveClick();
								dismiss();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Send the negative button event back to the
								// host activity
								mListener.onDialogNegativeClick();
								dismiss();
							}
						}).create();
	}

	private String getRandomComment(List<String> comments) {
		int length = comments.size();
		Random random = new Random(length);
		return comments.get(random.nextInt());
	}
}