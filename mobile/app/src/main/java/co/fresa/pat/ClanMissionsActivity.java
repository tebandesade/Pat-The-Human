package co.fresa.pat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import co.fresa.pat.mundo.Clan;
import co.fresa.pat.mundo.Mission;

public class ClanMissionsActivity extends AppCompatActivity implements Serializable{

    private TextView triggerAvailMissions;
   // private HashMap misionesActuales;
    private DatabaseReference databaseReference;
    private Button btnAddMissions;
    private Mission selectedItem;

    public ArrayList<Mission> getMisionesDelCan() {
        return misionesDelCan;
    }

    public void setMisionesDelCan(ArrayList<Mission> misionesDelCan) {
        this.misionesDelCan = misionesDelCan;
    }

    private ArrayList<Mission> misionesDelCan;
    private DialogSelectMissionClan dSM;
    public boolean isFlagStateAdding() {
        return flagStateAdding;
    }

    public void setFlagStateAdding(boolean flagStateAdding) {
        this.flagStateAdding = flagStateAdding;
    }

    private boolean flagStateAdding;
    //private int IdMisSelected;

    public ArrayList<Mission> getMisiones() {
        return misiones;
    }

    public void setMisiones(ArrayList<Mission> misiones) {
        this.misiones = misiones;
    }

    private ArrayList<Mission> misiones;
    private HashMap<Mission,Integer> idMissionMaping;
    private FirebaseAuth auth;
    private Clan selectedClan;
    public ListView getListaMisionees() {
        return listaMisionees;
    }

    public void setListaMisionees(ListView listaMisionees) {
        this.listaMisionees = listaMisionees;
    }

    private ListView listaMisionees ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_missions);
        triggerAvailMissions = (TextView) findViewById(R.id.triggerAvailableMissions);
        misionesDelCan = new ArrayList<Mission>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        btnAddMissions = (Button) findViewById(R.id.btnAddClanMissions);
        //misionesActuales = (HashMap) getIntent().getSerializableExtra("clanSelected");
        selectedClan  = (Clan) getIntent().getSerializableExtra("clanReallySelected");
        misiones = new ArrayList<Mission>();
        listaMisionees = (ListView) findViewById(R.id.listAvailableMissions);
        auth = FirebaseAuth.getInstance();
        System.out.println("ONCREATE");
        System.out.println(selectedClan);
        listaMisionees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                selectedItem = (Mission) parent.getItemAtPosition(position);
                // Display the selected item text on TextView

                if(flagStateAdding==true) {
                    createConfirmMissionSelectDialog();
                }
                else
                {

                    System.out.println("DEBUGINGCLICK");
                    System.out.println(selectedItem);

                    String idDeMisionSelected=selectedItem.getId(); //getIdMissionsFromSelected();
                    System.out.println(idDeMisionSelected);
                    System.out.println(selectedItem.getClass());
                    if (selectedItem.getCriteria().equals("CUSTOM")) {

                        Intent intent = new Intent(listaMisionees.getContext(), CustomMissionActivity.class);
                        intent.putExtra("mission", selectedItem);
                        intent.putExtra("idMision", idDeMisionSelected);
                        startActivity(intent);
                    }
                    // TODO implementar misiones con cada uno de los siguientes cuatro sensores
                    else if (selectedItem.getCriteria().equals("CAMERA")) {}
                    else if (selectedItem.getCriteria().equals("LOCATION")) {}
                    else if (selectedItem.getCriteria().equals("SHAKE")) {
                        Intent intent = new Intent(listaMisionees.getContext(), ShakeitMissionActivity.class);
                        intent.putExtra("level", new Integer(selectedItem.getLevel()));
                        intent.putExtra("reward", new Integer(selectedItem.getReward()));
                        intent.putExtra("idMision", idDeMisionSelected);
                        startActivity(intent);
                    }
                    else if (selectedItem.getCriteria().equals("SCREAM")) {
                        Intent intent = new Intent(listaMisionees.getContext(), ScreamMissionActivity.class);
                        intent.putExtra("level", new Integer(selectedItem.getLevel()));
                        intent.putExtra("reward", new Integer(selectedItem.getReward()));
                        intent.putExtra("idMision", idDeMisionSelected);
                        startActivity(intent);
                    }
                    else if (selectedItem.getCriteria().equals("DARK")) {
                        Intent intent = new Intent(listaMisionees.getContext(), DarkMissionActivity.class);
                        intent.putExtra("reward", new Integer(selectedItem.getReward()));
                        intent.putExtra("idMision", idDeMisionSelected);
                        startActivity(intent);
                    }
                }
            }
        });
        checkMissions();
    }

    private String getIdMissionsFromSelected()
    {
        String ret = "";
        if (idMissionMaping==null)
        {
            HashMap misiones = selectedClan.getMisiones();

            Iterator llaves = misiones.keySet().iterator();

            while(llaves.hasNext())
            {
                String llaveIdMision = (String) llaves.next();

                HashMap tempMis = (HashMap) misiones.get(llaveIdMision);
                if ( tempMis.get("name").toString().equalsIgnoreCase(selectedItem.getName()))
                {
                    ret = llaveIdMision;
                }

            }
        }
        else
        {
            ret =  idMissionMaping.get(selectedItem).toString();
        }

        return ret;

    }

    private void checkMissions()
    {
        System.out.println("Check mission");
        System.out.println(selectedClan);
        if (selectedClan.getMisiones()==null)
        {
            System.out.println("NO MISSIONS");
            System.out.println(selectedClan);
            triggerAvailMissions.setVisibility(View.VISIBLE);
            triggerAvailMissions.setText("No available missions! :(");
            triggerAvailMissions.setTextColor(Color.RED);
            btnAddMissions.setVisibility(View.VISIBLE);
            btnAddMissions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flagStateAdding =true;
                    getMissionsFromServer();
                }
            });

        }
        else
        {
            traerMisionesClan();
            updateArrayAdapter();
        }
    }

    private void traerMisionesClan()
    {
       HashMap misiones = selectedClan.getMisiones();
       Iterator llaves = misiones.keySet().iterator();
       while(llaves.hasNext())
       {
           String sup = (String) llaves.next();
          // HashMap actualtemp = (HashMap) misiones.get(sup);
           //System.out.println(actualtemp);
           try
           {
               Mission newTemp = (Mission) misiones.get(sup);
               addMisionesClan(newTemp);
           }
           catch(Exception e)
           { HashMap actualtemp = (HashMap) misiones.get(sup);
               System.out.println("Not casting to mission");
               Mission newTemp =  transformMission(actualtemp);
               addMisionesClan(newTemp);
           }
          //Mission newTemp = (Mission) misiones.get(sup);//transformMission(actualtemp);
          //addMisionesClan(newTemp);
       }
       triggerAvailMissions.setVisibility(View.VISIBLE);
        triggerAvailMissions.setTextColor(Color.GREEN);
        triggerAvailMissions.setText("Available clan missions!");
        btnAddMissions.setVisibility(View.VISIBLE);
        btnAddMissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagStateAdding =true;
                getMissionsFromServer();
            }
        });

    }
    private Mission transformMission(HashMap actualtemp )
    {
        String nom = (String) actualtemp.get("name");
        String crit = (String) actualtemp.get("criteria");
        String idMis = (String) actualtemp.get("id");
        String descr = (String) actualtemp.get("description");
        long rwardd = (long) actualtemp.get("reward");
        int rward = (int) rwardd;
        long level = (long) actualtemp.get("level");
        int lvl = (int)level;
        return new Mission(nom,crit,descr,rward,lvl,idMis);
    }

    private void getMissionsFromServer() {
        if (misiones.isEmpty())
        {
            Query clans = databaseReference.child("missions");
            //idMissionMaping = new HashMap<Mission,Integer>();
            clans.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            String idM =  String.valueOf(postSnapShot.getKey());
                            Mission sup= postSnapShot.getValue(Mission.class);
                            //idMissionMaping.put(sup,idM);
                            sup.setId(idM);
                            misiones.add(sup);
                            //System.out.println(temp.keySet());
                            // addObjToClans(new Clan(temp));
                        }
                        // getUserLocation();
                        displayAvailableMissions();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            updateArrayAdapter();
        }

    }

    private void displayAvailableMissions() {
        updateArrayAdapter();
        if(misionesDelCan.isEmpty())
        {
            triggerAvailMissions.setTextColor(Color.YELLOW);
            triggerAvailMissions.setText("Choose a mission!");
            btnAddMissions.setVisibility(View.INVISIBLE);
        }


    }
    public void reSetArrayMisionesAdapter()
    {
        flagStateAdding = false;
        triggerAvailMissions.setTextColor(Color.GREEN);
        triggerAvailMissions.setText("Available clan missions!");
        btnAddMissions.setVisibility(View.VISIBLE);
        updateArrayAdapter();

    }

    public void updateArrayAdapter()
    {

        if(isFlagStateAdding()==true)
        {
            CustomListAdapter adapter = new  CustomListAdapter(this, getMisiones());
            //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getMisiones()) ;
            listaMisionees.setAdapter(adapter);
        }
        else
        {
           //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getMisionesDelCan());
            CustomListAdapter adapter = new  CustomListAdapter(this, getMisionesDelCan());
            listaMisionees.setAdapter(adapter);

        }
    }

    private void createConfirmMissionSelectDialog()
    {

            DialogSelectMissionClan dSM=  new DialogSelectMissionClan(selectedItem,databaseReference,selectedClan);
            dSM.show(getFragmentManager(),"test");





    }

    public void addMisionesClan(Mission mis)
    {
     misionesDelCan.add(mis);
    }

    //public ArrayList<Mission> getMisionesArray()
    //{
     //   Iterator iter = selectedClan.getMisiones().entrySet().iterator();
      //  ArrayList<Mission> misionesArray = new ArrayList<Mission>();
        //while(iter.hasNext())
        //{
         //   Mission actualMis = (Mission) iter.next();

           // misionesArray.add(actualMis);
        //}
        //return misionesArray;
    //}
        //public void onResume() {

          //  super.onResume();
            //updateArrayAdapter();
        //}
    //public void onResume() {
//
  //      super.onResume();
    //    updateArrayAdapter();
    //}
}
