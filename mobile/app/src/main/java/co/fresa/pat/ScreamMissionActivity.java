package co.fresa.pat;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import co.fresa.pat.mundo.User;
import co.fresa.pat.mundo.UserLogic;

public class ScreamMissionActivity extends AppCompatActivity implements View.OnClickListener  {

    // nivel de dificultad de la mision
    private Integer level;
    // beneficio economico
    private Integer reward;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String [] permissions = {android.Manifest.permission.RECORD_AUDIO};

    private Listen4ScreamTask listen;

    boolean permissionToRecordAccepted;
    private String idMission;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scream_mission);

        // PRIMERO INICIALIZAMOS LOS COMPONENTES GRAFICOS DE LA VENTANA
        // Recibimos el nivel del reto mediante el intent
        level = (Integer) getIntent().getSerializableExtra("level");
        if (level == null) level = 1;

        reward = (Integer) getIntent().getSerializableExtra("reward");
        if (level == null) reward = 100;
        idMission = getIntent().getStringExtra("idMision");
        TextView description = findViewById(R.id.scream_mission_description);
        description.setText("Since ancient times, warriors scream at the top of their lungs to tell the world they will not be defeated. What battle are you fighting?\n\nScream, yell, shout or make any LOUD noise to complete this mission.");

        TextView title = findViewById(R.id.scream_mission_name);
        title.setText("!!%^=!AAAAAAAARRRKKGHHH!%$!*!: Level "+level);

        ImageButton close = findViewById(R.id.close_scream);
        close.setOnClickListener(this);

        //Antes de comenzar pedimos permiso para grabar audio
        permissionToRecordAccepted = false;
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        System.out.println("Ejecución de onCreate termina");

    }

    protected void onResume() {
        if (permissionToRecordAccepted && listen == null) {
            listen = new Listen4ScreamTask();
            listen.execute();
        }
        super.onResume();
    }

    protected void onPause() {
        System.out.println("onPause: ScreamMissionActivity");
        if (listen != null) {
            System.out.println("Listen not null");
            listen.stopRecorder();
        }
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (listen != null) listen.stopRecorder();
        finish();
    }

    public class Listen4ScreamTask extends AsyncTask<Object, Object, Boolean> {
        boolean listening;
        MediaRecorder mRecorder;

        @Override
        protected Boolean doInBackground(Object... params) {
            listening = false;
            startRecorder();

            while (listening) {
                if (mRecorder != null) {
                    int amplitude = -1;
                    try {amplitude = mRecorder.getMaxAmplitude(); }
                    catch (Exception e) { System.out.println("getAmplitude() failed: "+e.getMessage()); }

                    System.out.println("Ruido: "+ amplitude);

                    if (amplitude > 5000 * level) {
                        UserLogic.getInstance().sumarMonedas(reward);
                        UserLogic.getInstance().sumarMonedasClan(reward,idMission);
                        System.out.println("Transaccion completa: +"+reward+" monedas");
                        return true;
                    }
                } else {
                    System.out.println("Not recording.");
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            //TODO avisarle al usuario que gano el oro si es así
            System.out.println("FINAL PACIFICO. Jugador grito?: "+bool);
            stopRecorder();
            if (bool) {
                System.out.println("Transaccion completa: +"+reward+" monedas");
                finish();
            }
        }

        public void startRecorder(){

            System.out.println("START RECORDING");
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                try {
                    mRecorder.prepare();
                } catch (java.io.IOException ioe) {
                    android.util.Log.e("[Monkey]", "IOException: " +
                            android.util.Log.getStackTraceString(ioe));
                } catch (java.lang.SecurityException e) {
                    android.util.Log.e("[Monkey]", "SecurityException: " +
                            android.util.Log.getStackTraceString(e));
                }
                try {
                    mRecorder.start();
                } catch (java.lang.SecurityException e) {
                    android.util.Log.e("[Monkey]", "SecurityException: " +
                            android.util.Log.getStackTraceString(e));
                }
            }
            if (mRecorder != null) listening = true;
        }

        public void stopRecorder() {
            System.out.println("STOP RECORDING");
            listening = false;

            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            }
            cancel(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (permissionToRecordAccepted) {
            System.out.println("Record audio permission granted");
            //begin recording
            listen = new Listen4ScreamTask();
            listen.execute();
        }
        else {
            System.out.println("Record audio permission denied");
            //TODO informar al usuario que no puede realizar la misión
            finish();
        }
    }
}