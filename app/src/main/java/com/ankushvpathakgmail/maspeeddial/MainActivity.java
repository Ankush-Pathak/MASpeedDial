package com.ankushvpathakgmail.maspeeddial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int[] buttonIds = {R.id.buttonCall, R.id.buttonSD1, R.id.buttonSD2, R.id.buttonSD3,
            R.id.buttonSD4, R.id.buttonSD5, R.id.buttonSD6,
            R.id.buttonSD7, R.id.buttonSD8, R.id.buttonSD9, R.id.buttonContactPicker};
    EditText editTextNo;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    HashMap<String, String> speedDialMap;
    String forJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.buttonCall:
                if(editTextNo.getText().length() == 10)
                {
                    Intent callintent = new Intent(Intent.ACTION_CALL);
                    callintent.setData(Uri.parse("tel:" + editTextNo.getText().toString()));
                    try
                    {
                        startActivity(callintent);
                    }
                    catch(android.content.ActivityNotFoundException e)
                    {
                        Toast.makeText(MainActivity.this, "Unable to make call", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "Invalid number.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.buttonSD1:
                speedDialOps(1 + "");
                break;
            case R.id.buttonSD2:
                speedDialOps(2 + "");
                break;
            case R.id.buttonSD3:
                speedDialOps(3 + "");
                break;
            case R.id.buttonSD4:
                speedDialOps(4 + "");
                break;
            case R.id.buttonSD5:
                speedDialOps(5 + "");
                break;
            case R.id.buttonSD6:
                speedDialOps(6 + "");
                break;
            case R.id.buttonSD7:
                speedDialOps(7 + "");
                break;
            case R.id.buttonSD8:
                speedDialOps(8 + "");
                break;
            case R.id.buttonSD9:
                speedDialOps(9 + "");
                break;

            case R.id.buttonContactPicker:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 2);
                break;

        }
    }

    void init()
    {
        speedDialMap = new HashMap<>();
        editTextNo = findViewById(R.id.editTextNo);

        for(int i = 0;i < 11;i++)
        {
            findViewById(buttonIds[i]).setOnClickListener(this);
        }

        sharedPreferences = getSharedPreferences("SD",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        forJson = sharedPreferences.getString("SD_HM",null);
        Log.e("JSON","Retireived JSON: " + forJson);
        if(forJson != null)
            try {
                speedDialMap = new Gson().fromJson(forJson, speedDialMap.getClass());
                Log.e("JSON","Set to HM size: " + speedDialMap.size() + " HM: " + speedDialMap.toString() + " speed1: " + speedDialMap.get(speedDialMap.keySet().toArray()[0]) + " key: " + speedDialMap.keySet().toArray()[0] + " type: " + speedDialMap.keySet().toArray()[0].getClass());
            }
            catch (Exception e){
                e.printStackTrace();
            }

    }

    void speedDialOps(String no)
    {
        if(editTextNo.getText() != null && editTextNo.getText().toString().length() == 10) {
            speedDialMap.put(no, editTextNo.getText().toString());
            Toast.makeText(MainActivity.this, "Speed dial set for " + no, Toast.LENGTH_SHORT).show();
            forJson = new Gson().toJson(speedDialMap);
            editor.putString("SD_HM",forJson);
            editor.commit();
            Log.e("JSON","Put JSON: " + forJson);
        }
        else
        if(speedDialMap.get(no) != null)
            editTextNo.setText(speedDialMap.get(no));
        else
            Toast.makeText(MainActivity.this, "Speed dial not set.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.e("PHONE", "reqCode: " + requestCode + " resCode: " + resultCode + " data: " + data.getData().toString());
        if(resultCode == Activity.RESULT_OK && resultCode == 2)
        {
            String phoneNo = null;
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor.moveToFirst()) {
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNo = cursor.getString(phoneIndex);
                editTextNo.setText(phoneNo);
            }

            cursor.close();
        }
    }
}
