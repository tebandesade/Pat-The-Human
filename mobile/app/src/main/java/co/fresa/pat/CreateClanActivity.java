package co.fresa.pat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.fresa.pat.mundo.Clan;
import co.fresa.pat.mundo.Mission;

import static com.google.android.gms.location.LocationServices.*;

public class CreateClanActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FusedLocationProviderClient mFusedLocationClient;
   // private ClanFragment myclanfragment;
    private FirebaseAuth mAuth;
    private ExistingClanDialogFragment ECDF;
    private CreateClanDialogFragment cDF;
    public ListView getClansList() {
        return clansList;
    }

    public void setClansList(ListView clansList) {
        this.clansList = clansList;
    }

    private ListView clansList;

    public ArrayList<Clan> getClans() {
        return clans;
    }

    public void setClans(ArrayList<Clan> clans) {
        this.clans = clans;
    }

    private ArrayList<Clan> clans;

    public Clan getClanSelected() {
        return clanSelected;
    }

    public void setClanSelected(Clan clanSelected) {
        this.clanSelected = clanSelected;
    }

    private Clan clanSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        clans = new ArrayList<Clan>();
        mAuth= FirebaseAuth.getInstance() ;
        getDataFromServer();
        setContentView(R.layout.activity_create_clan);
        mFusedLocationClient = getFusedLocationProviderClient(this);
        clansList = (ListView) findViewById(R.id.listClans);

    }

    public void getDataFromServer() {
        Query clans = databaseReference.child("clans");
        clans.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        System.out.println("Helelo boyaca");
                        System.out.println(postSnapShot.getValue());
                        HashMap temp = (HashMap) postSnapShot.getValue();
                        Clan temp_cl = new Clan(temp);
                        //System.out.println(temp.keySet());
                        addObjToClans(temp_cl);
                    }
                  //  getUserLocation();
                    displayClans();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Clan transformClan(HashMap actualtemp )
    {
        String nom = (String) actualtemp.get("name");
        String crit = (String) actualtemp.get("placeID");
        System.out.println(actualtemp.get("misiones"));
        Object descr =  actualtemp.get("misiones");
        Clan new_clan = new Clan(nom,crit);
        System.out.println("teoalla");
        System.out.println(descr);
         if (descr==null)
        {

        }
        else
        {
            if (descr.getClass()==ArrayList.class)
            {
                ArrayList temp_array = (ArrayList) descr;
                int i;
                for(i=0;i<temp_array.size();i++)
                {

                    System.out.println(temp_array.get(i));
                }
            }
            else
            {
                HashMap temp_hash = (HashMap) actualtemp.get("misiones");
                new_clan.setMisiones(temp_hash);
            }
        }
        long mems = (long) actualtemp.get("cantidadMiembros");
        long level = (long) actualtemp.get("money");
        int money = (int)level;
        //new_clan.setMisiones(descr);
        new_clan.setMoney(money);
        new_clan.setCantidadMiembros(mems);
        return new_clan;
    }


    public void addObjToClans(Clan c) {
        //Un clan tiene que tener un placeid

        clans.add(c);
    }

    public void getUserLocation() {
        System.out.println("beeeeforre sucess get userLocation");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        System.out.println("Getting last location");
                        if (location != null) {
                            // Logic to handle location object
                            System.out.println(location);
                        }
                    }
                });
    }

    public void displayClans()
    {
       //String[] listItems = new String[3];
        //listItems[0] = "4444";
        //listItems[1] = "prprpr";
        //listItems[2] = "olaoalao";
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getClans());
        //System.out.println(adapter.getItem(0));
        clansList.setAdapter(adapter);
        clansList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Clan selectedItem = (Clan) parent.getItemAtPosition(position);
                setClanSelected(selectedItem);
                getExistingClanDialogFragment();

                // Display the selected item text on TextView
                System.out.println(selectedItem);
            }
        });
    }

    public void getCreateDialogFragment(View view)
    {
        cDF = new CreateClanDialogFragment();

        cDF.show(getFragmentManager(),"test");

        if(cDF.isDetached())
        {
            System.out.println("It is IS detached");
        }
        else
        {
            System.out.println("It is not detached");
        }
    }

   

    public void getExistingClanDialogFragment() {

        ECDF = new ExistingClanDialogFragment(clanSelected,mAuth,databaseReference);
        ECDF.show(getFragmentManager(), "test");


    }

    public void emptyAllClans(){
        getClans().clear();
    }


    }
