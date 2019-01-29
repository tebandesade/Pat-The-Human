package co.fresa.pat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.w3c.dom.Text;

import co.fresa.pat.mundo.Mission;
import co.fresa.pat.mundo.User;
import co.fresa.pat.mundo.UserLogic;

public class CustomMissionActivity extends AppCompatActivity implements View.OnClickListener {

    Mission mission;
    private String idMission;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_mission);

        // La misión que recibimos como extra en el Intent
        mission = (Mission) getIntent().getSerializableExtra("mission");
        idMission = getIntent().getStringExtra("idMision");
        // ponemos la Activity como listener para los botones de cerrar y terminar mision
        ImageButton close = findViewById(R.id.close_custom);
        close.setOnClickListener(this);

        ImageButton check = findViewById(R.id.check_custom_mission);
        check.setOnClickListener(this);

        TextView missionName = findViewById(R.id.custom_mission_name);
        missionName.setText(mission.getName());

        System.out.println(mission.getDescription());
        TextView missionDescription = findViewById(R.id.custom_mission_description);
        missionDescription.setText(mission.getDescription());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.close_custom) finish();
        else if (view.getId() == R.id.check_custom_mission) {
            onCheckClicked(mission.getReward());
            UserLogic.getInstance().sumarMonedasClan(mission.getReward(),idMission);
            finish();
        }
        //TODO reportar contenido inapropiado
        //tambien agregar accion si el usuario espicha back
    }

    private void onCheckClicked(final int reward) {
        UserLogic.getInstance().sumarMonedas(reward);
        System.out.println("Transaccion completa: +"+reward+" monedas");
    }
}