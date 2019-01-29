package co.fresa.pat;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import co.fresa.pat.mundo.User;
import co.fresa.pat.mundo.UserLogic;

public class ShakeitMissionActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    // nivel de dificultad de la mision
    Integer level;
    // beneficio economico
    Integer reward;
    // Id key de la mision
    String idMission;

    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakeit_mission);

        // Recibimos el nivel del reto mediante el intent
        level = (Integer) getIntent().getSerializableExtra("level");
        if (level == null) level = 1;

        reward = (Integer) getIntent().getSerializableExtra("reward");
        if (reward == null) reward = 100;
        idMission = getIntent().getStringExtra("idMision");

        TextView description = findViewById(R.id.shakeit_mission_description);
        description.setText("Sh-sh-sh-SHAKE IT SHAKE IT SHAKE IT!!.\n\nShake your device to complete this mission.");

        TextView title = findViewById(R.id.shakeit_mission_name);
        title.setText("Shake it! Level "+level);

        ImageButton close = findViewById(R.id.close_shakeit);
        close.setOnClickListener(this);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    protected void onResume() {
        super.onResume();
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        //if(event.sensor.getName().contains("Accelerometer")) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;

            if (Math.abs(delta) > 20*level) {
                System.out.println("SHOOK");

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userKey = mAuth.getCurrentUser().getUid();

                UserLogic.getInstance().sumarMonedas(reward);
                UserLogic.getInstance().sumarMonedasClan(reward,idMission);
                System.out.println("Transaccion completa: +"+reward+" monedas");

                // crucial: terminamos la actividad
                finish();
            }
        //}
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}