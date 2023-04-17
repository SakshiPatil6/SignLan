package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinCreate extends AppCompatActivity {
    final String sampleToken="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJiYzZjN2M4MC02ZGIzLTRiMWYtYWJmMC00ZmRjNGFiY2ZjNzEiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTY3NzI1MTI4NywiZXhwIjoxODM1MDM5Mjg3fQ.EwRhrrDk-fv-Q5SGE1S2mjgSRagpjnwRLk38sKUfPk8";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_create);
        final Button btnCreate = findViewById(R.id.create);
        final Button btnJoin = findViewById(R.id.join);
        final EditText etMeetingId = findViewById(R.id.code);
        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID);
        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID);

        btnCreate.setOnClickListener(v -> {
            // we will explore this method in the next step
            createMeeting(sampleToken);
        });

        btnJoin.setOnClickListener(v -> {
            Intent intent = new Intent(JoinCreate.this, MeetingActivity.class);
            intent.putExtra("token", sampleToken);
            intent.putExtra("meetingId", etMeetingId.getText().toString());
            startActivity(intent);
        });
    }

    private void createMeeting(String token) {
        // we will make an API call to VideoSDK Server to get a roomId
        AndroidNetworking.post("https://api.videosdk.live/v2/rooms")
                .addHeaders("Authorization", token) //we will pass the token in the Headers
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response will contain `roomId`
                            final String meetingId = response.getString("roomId");

                            // starting the MeetingActivity with received roomId and our sampleToken
                            Intent intent = new Intent(JoinCreate.this, MeetingActivity.class);
                            intent.putExtra("token", sampleToken);
                            intent.putExtra("meetingId", meetingId);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(JoinCreate.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
//            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

}
