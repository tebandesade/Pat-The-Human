package co.fresa.pat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
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

import co.fresa.pat.mundo.Clan;
import co.fresa.pat.mundo.User;
import co.fresa.pat.mundo.UserLogic;

public class DarkMissionActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    // beneficio economico
    private Integer reward;
    //
    private long tUltimoOscuro;
    private boolean estaOscuro;

    private SensorManager mSensorManager;
    private Sensor lightSensor;
    DarkCompletedTask darkTask;
    private String idMission;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        idMission = getIntent().getStringExtra("idMision");
        setContentView(R.layout.activity_dark_mission);

        reward = (Integer) getIntent().getSerializableExtra("reward");
        tUltimoOscuro = Long.MAX_VALUE;
        estaOscuro = false;

        ImageButton close = findViewById(R.id.close_dark);
        close.setOnClickListener(this);

        darkTask = new DarkCompletedTask();
        darkTask.execute();
    }


    public void onSensorChanged(SensorEvent event) {
        //if(event.sensor.getName().contains("ALS") || event.sensor.getName().contains("Light")) {
            if (event.values[0] < 4) {
                if (!estaOscuro) {
                    System.out.println("Oscurece...");
                    tUltimoOscuro = System.currentTimeMillis();
                }
            }
            else {
                System.out.println("Mucha luz.");
                tUltimoOscuro = Long.MAX_VALUE;
            }
        //}
    }

    @Override
    public void onBackPressed()
    {
        //mSensorManager.unregisterListener(this);
        super.onBackPressed();  // optional depending on your needs
        finish();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public void onClick(View view) { finish(); }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        darkTask.cancel(true);
        System.out.println("Unregistered listener: Light");
        super.onDestroy();
    }

    private class DarkCompletedTask extends AsyncTask<Object, Integer, Boolean> {
        protected Boolean doInBackground(Object... objs) {
            while (!isCancelled()) {
                try { Thread.sleep(800); } catch (InterruptedException e) {
                    return false;
                }
                System.out.println(System.currentTimeMillis()-tUltimoOscuro);
                if (System.currentTimeMillis() - tUltimoOscuro> 10*1000) {
                    UserLogic.getInstance().sumarMonedas(reward);
                    UserLogic.getInstance().sumarMonedasClan(reward,idMission);
                    System.out.println("Transaccion completa: +"+reward+" monedas");
                    return true;
                }
            }
            return false;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                System.out.println("Transaccion completa: +"+reward+" monedas");
            }
            if (!isCancelled()) finish();
        }
    }
}