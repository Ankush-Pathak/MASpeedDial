package com.ankushvpathakgmail.maspeeddial;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class EmergencyCallActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    HashMap<String, String> speedDialMap;
    String forJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);

        speedDialMap = new HashMap<>();

        sharedPreferences = getSharedPreferences("SD", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        forJson = sharedPreferences.getString("SD_HM", null);
        Log.e("JSON", "Retireived JSON: " + forJson);
        if (forJson != null)
            try {
                speedDialMap = new Gson().fromJson(forJson, speedDialMap.getClass());
                Log.e("JSON", "Set to HM size: " + speedDialMap.size() + " HM: " + speedDialMap.toString() + " speed1: " + speedDialMap.get(speedDialMap.keySet().toArray()[0]) + " key: " + speedDialMap.keySet().toArray()[0] + " type: " + speedDialMap.keySet().toArray()[0].getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }


        showDialog();

    }

    void showDialog() {

        final Dialog dialog = new Dialog(EmergencyCallActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.setContentView(R.layout.dialog_emergency_call);

        dialog.setTitle("Call emergency number: ");
        dialog.show();
        Button buttonEmerCall = dialog.findViewById(R.id.buttonDialogEmerCall);
        ((RadioButton) dialog.findViewById(R.id.radioButtonEmerCall1)).setText(speedDialMap.get("1"));
        ((RadioButton) dialog.findViewById(R.id.radioButtonEmerCall2)).setText(speedDialMap.get("2"));
        ((RadioButton) dialog.findViewById(R.id.radioButtonEmerCall3)).setText(speedDialMap.get("3"));


        buttonEmerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String call = "";
                if (((RadioButton) dialog.findViewById(R.id.radioButtonEmerCall1)).isChecked())
                    call = "1";
                else if (((RadioButton) dialog.findViewById(R.id.radioButtonEmerCall2)).isChecked())
                    call = "2";
                else if (((RadioButton) dialog.findViewById(R.id.radioButtonEmerCall3)).isChecked())
                    call = "3";
                Log.e("CALL", "CALL: " + call);
                if (call.length() > 0) {
                    Intent callintent = new Intent(Intent.ACTION_CALL);
                    callintent.setData(Uri.parse("tel:" + speedDialMap.get(call)));
                    try {
                        dialog.dismiss();
                        finish();
                        startActivity(callintent);


                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(EmergencyCallActivity.this, "Unable to make call", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}


