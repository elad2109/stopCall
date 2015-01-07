package com.example.stopcall.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.example.stopcall.app.Constants;
import com.example.stopcall.app.dal.PhoneDal;
import com.example.stopcall.app.dal.dto.Phone;
import com.example.stopcall.app.fragments.YesNoDialogFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboFragmentActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class PopupActivity extends RoboFragmentActivity implements
        YesNoDialogFragment.YesNoDialogFragmentListener {

    @Inject PhoneDal phoneDal;
    public Phone phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        //
        // btnEditName = (Button) findViewById(R.id.btn_edit);
        // txtName = (TextView) findViewById(R.id.txt_name);
        //
        // btnEditName.setOnClickListener(this);
        phone = getIntent().getParcelableExtra(Constants.DIALED_PHONE);
        exportDatabase(Constants.DB_NAME);
        showYesNoDialog();
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick() {
        persistNumberIsAllowed();
        SendDialIntent();
        finish();
    }

    private void SendDialIntent() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone.phone));
        startActivity(intent);
    }

    private void persistNumberIsAllowed() {
//		SharedPreferences sharedPreferences = this.getSharedPreferences(
//				Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//
//		Editor editor = sharedPreferences.edit();
//		editor.putBoolean(Constants.IS_NUMBER_BLOCKED, false);
//		editor.commit();
        if (phone != null) {
            phone.isBlocked = false;
            if (phoneDal.getItem(phone.phone) != null) {
                phoneDal.updateItem(phone);
            }
        }
    }

    public void exportDatabase(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //String currentDBPath = context.getDatabasePath(DATABASE_NAME).toString(); //"//data//" + context.getPackageName() + "//databases//" + databaseName + "";

                File file = new File("data/data/com.example.stopcall.app/databases");
                File[] files = file.listFiles();
                String backupDBPath = "dontacllDB.db";
                //File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (files[0].exists()) {
                    FileChannel src = new FileInputStream(files[0]).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            String a = "";
        }
    }

    @Override
    public void onDialogNegativeClick() {
        finish();
    }

    // private void showSetNameDialog() {
    // SetNameDialogFragment dialog = new SetNameDialogFragment();
    // dialog.show(getSupportFragmentManager(), SetNameDialogFragment.TAG);
    // }

    private void showYesNoDialog() {
        YesNoDialogFragment dialog = new YesNoDialogFragment();
        dialog.show(getSupportFragmentManager(), YesNoDialogFragment.TAG);
    }

}
