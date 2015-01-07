package com.example.stopcall.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.stopcall.app.activities.PopupActivity;
import com.example.stopcall.app.dal.CommentDal;
import com.example.stopcall.app.dal.PhoneDal;
import com.example.stopcall.app.dal.dto.Phone;
import com.google.inject.Inject;
import roboguice.receiver.RoboBroadcastReceiver;

public class OutgoingCallReceiver extends RoboBroadcastReceiver {

	public static final String ABORT_PHONE_NUMBER = "0055";

	private static final String OUTGOING_CALL_ACTION = "android.intent.action.NEW_OUTGOING_CALL";
	private static final String INTENT_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";

	@Inject private CommentDal commentDal;
	@Inject private PhoneDal phoneDal;

	@Override
	protected void handleReceive(Context context, Intent intent) {
		super.handleReceive(context, intent);
		Log.v(Constants.LOGGER_TAG, "OutgoingCallReceiver onReceive");
		if (intent.getAction()
				.equals(OutgoingCallReceiver.OUTGOING_CALL_ACTION)) {

			Log.v(Constants.LOGGER_TAG,
					"OutgoingCallReceiver NEW_OUTGOING_CALL received");

			String phoneNumber = intent.getExtras().getString(
					OutgoingCallReceiver.INTENT_PHONE_NUMBER);

			if (isBlocked(phoneNumber))
			{
				Intent i = new Intent(context, PopupActivity.class);
				i.putExtra(Constants.DIALED_PHONE, phoneDal.getItem(phoneNumber));
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
				setResultData(null);
			}


//
//			if (isNumberBlocked(phoneNumber)) {
//				Toast.makeText(
//						context,
//						"NEW_OUTGOING_CALL intercepted to number 123-123-1234 - aborting call",
//						Toast.LENGTH_LONG).show();
//
//				Intent i = new Intent(context, PopupActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(i);
//
//				SharedPreferences sharedPreferences = context
//						.getSharedPreferences(Constants.SHARED_PREF_NAME,
//								Context.MODE_PRIVATE);
//
//				boolean isBloacked = sharedPreferences.getBoolean(
//						Constants.IS_NUMBER_BLOCKED, true);
//
//				if (isBloacked) {
//					// dialog and then:
//					setResultData(null);
//				}
//			}
		}
	}

	public OutgoingCallReceiver() {
		super();
	}

	public void onReceive1(final Context context, final Intent intent) {
		Log.v(Constants.LOGGER_TAG, "OutgoingCallReceiver onReceive");
		if (intent.getAction()
				.equals(OutgoingCallReceiver.OUTGOING_CALL_ACTION)) {

			Log.v(Constants.LOGGER_TAG,
					"OutgoingCallReceiver NEW_OUTGOING_CALL received");

			String phoneNumber = intent.getExtras().getString(
					OutgoingCallReceiver.INTENT_PHONE_NUMBER);

			if (isBlocked(phoneNumber))
			{
				setResultData(null);
				Intent i = new Intent(context, PopupActivity.class);
				i.putExtra(Constants.DIALED_PHONE, phoneDal.getItem(phoneNumber));
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}


//
//			if (isNumberBlocked(phoneNumber)) {
//				Toast.makeText(
//						context,
//						"NEW_OUTGOING_CALL intercepted to number 123-123-1234 - aborting call",
//						Toast.LENGTH_LONG).show();
//
//				Intent i = new Intent(context, PopupActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(i);
//
//				SharedPreferences sharedPreferences = context
//						.getSharedPreferences(Constants.SHARED_PREF_NAME,
//								Context.MODE_PRIVATE);
//
//				boolean isBloacked = sharedPreferences.getBoolean(
//						Constants.IS_NUMBER_BLOCKED, true);
//
//				if (isBloacked) {
//					// dialog and then:
//					setResultData(null);
//				}
//			}
		}
	}

	private boolean isBlocked(String phoneNumber) {
		Phone phone = phoneDal.getItem(phoneNumber, true);
		boolean ans;
		if (phone != null && phone.isBlocked)
		{
			ans = true;
		}
		else
		{
			ans = false;
		}

		return ans;
	}

//	private boolean isNumberBlocked(String phoneNumber) {
//		return (phoneNumber != null)
//				&& phoneNumber.equals(OutgoingCallReceiver.ABORT_PHONE_NUMBER);
//	}
}