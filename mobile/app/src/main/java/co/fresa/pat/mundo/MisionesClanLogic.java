package co.fresa.pat.mundo;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MisionesClanLogic {

    private static MisionesClanLogic instance = null;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    boolean change;

    public List<String> getMisionIds() {
        return misionIds;
    }

    public void setMisionIds(List<String> misionIds) {
        this.misionIds = misionIds;
    }

    List<String> misionIds;

    public static MisionesClanLogic getInstance() {
        if(instance == null) instance = new MisionesClanLogic();
        return instance;
    }

    protected MisionesClanLogic() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        cargarMisiones();
    }

    public void cargarMisiones() {
        misionIds = new ArrayList<String>();
        String idClan = UserLogic.getInstance().getUser().getIdClan();

        if (idClan != null && !"none".equals(idClan)) {
            mDatabase.child("clans").child(idClan).child("misiones").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    misionIds = new ArrayList<String>();
                    Iterable<DataSnapshot> missionsData = dataSnapshot.getChildren();
                    for (DataSnapshot mission : missionsData) misionIds.add(mission.getValue(Mission.class).getId());

                    System.out.println("Misiones cargadas");
                    System.out.println(misionIds);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else {
            misionIds = new ArrayList<String>();
        }
    }

    public void changeAcknowledged () {
        change = false;
    }

    public void addMission (String id) {
        misionIds.add(id);
    }

    public void removeMission(String id) {
        misionIds.remove(id);
    }

    public boolean hasMissionId(String id) { return misionIds.contains(id); }
}
