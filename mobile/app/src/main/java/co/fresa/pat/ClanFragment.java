package co.fresa.pat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;

import co.fresa.pat.mundo.Clan;
import co.fresa.pat.mundo.User;
import co.fresa.pat.mundo.UserLogic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClanFragment  extends Fragment implements Serializable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String EXTRA_MESSAGE = "Changing activy";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static TextView mListView;
    //private final ArrayList<> clanMissions;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    private DatabaseReference mDatabase;
    private OnFragmentInteractionListener mListener;
    private Button btnclan;
    private ImageView coinImg ;
    private TextView plataClan;
    private Clan actual;

    public Button getBtnerrase() {
        return btnerrase;
    }

    public void setBtnerrase(Button btnerrase) {
        this.btnerrase = btnerrase;
    }

    private Button btnerrase;
 //   private String[] listItems;
    public ClanFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClanFragment newInstance(String param1, String param2) {
        ClanFragment fragment = new ClanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        //mListView = getid

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_clan, container, false);
        mListView =  rootView.findViewById(R.id.clanlist_view);
        btnclan = rootView.findViewById(R.id.btncreateclan);
        coinImg = rootView.findViewById(R.id.coinimage);
        plataClan = rootView.findViewById(R.id.plataClan);
        btnerrase = rootView.findViewById(R.id.btnRemoveClan);
        System.out.println("onCreateviewClan");
        getUserClan();
    return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public Clan getClanActual() {
        return actual;
    }

    public void setClanActual(Clan actual) {
        this.actual = actual;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getUserClan()
    {
        //dos outcomes si esta en un clan si no
        System.out.println("getuserclan");
        System.out.println(actual);
        if(actual==null)
        {
            Query users = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());//orderByKey().equalTo(mAuth.getCurrentUser().getUid());

            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //prints "Do you have data? You'll love Firebase."

                    // if (listItems[0]==null){
                    //    listItems[0] = snapshot.getValue(User.class).getIdClan();//ystem.out.print(ret.length);
                    //}
                    String clanId = snapshot.getValue(User.class).getIdClan();

                    //ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listItems);
                    //mListView.setAdapter(adapter);
                    verificarClan(clanId);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
        else
        {
            System.out.println("Actual no es null");
            verificarClan(actual.getPlaceID());
        }

        //    mDatabase.child("users").child(newKey).setValue(newUser);

      //  clanMissions = Recipe.getRecipesFromFile("recipes.json", this);

    }

    @SuppressLint("WrongConstant")
    public void verificarClan(String clan)
    {
        if(clan.equalsIgnoreCase("none"))
        {

            if(btnclan.getVisibility()==View.VISIBLE && btnclan.getText().equals("See Missions"))
            {
                    btnclan.setText("Join a clan!");
                    mListView.setText(clan);
                    activateImages();

            }
            else
            {
                btnclan.setVisibility(0);
                mListView.setText(clan);
                btnclan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeToCreateClan();
                    }
                });
            }

        }
        else
        {
            System.out.println("Clan is : "+ clan);
            inicializarClan(clan);

        }


    }



    public void inicializarClan(String id) {
      DatabaseReference r = getmDatabase().child("clans").child(id);
      System.out.println("DEBUGINGINICIALIZARCLAN");
      System.out.println(r);
      r.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              //System.out.println(dataSnapshot);
              //GenericTypeIndicator<HashMap> t = new GenericTypeIndicator<HashMap>() {};
              HashMap testDebug = (HashMap) dataSnapshot.getValue();//dataSnapshot.getValue(t);
              System.out.println("OLAOLAOLAOLAOALA");
              System.out.println(testDebug);


              //Clan dos = new Clan(testDebug);
              //System.out.println(dos);
              Clan temp = new Clan(testDebug);
              if (temp!=null)
              {
                  setClanActual(temp);
                  updateViewsClan();

              }

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {}
      });
    }

    private void activateImages() {
        if (coinImg.getVisibility() == View.VISIBLE && btnerrase.getVisibility() == View.VISIBLE && plataClan.getVisibility() == View.VISIBLE)
        {
            coinImg.setVisibility(View.INVISIBLE);
            btnerrase.setVisibility(View.INVISIBLE);
            plataClan.setVisibility(View.INVISIBLE);
        }
        else
        {

            coinImg.setVisibility(View.VISIBLE);
            btnerrase.setVisibility(View.VISIBLE);
            plataClan.setVisibility(View.VISIBLE);

        }
    }

    private void updateViewsClan() {

        mListView.setText(actual.getName());
        coinImg.setVisibility(View.VISIBLE);
        btnerrase.setVisibility(View.VISIBLE);
        plataClan.setVisibility(View.VISIBLE);
        btnclan.setText("See Missions");
        btnclan.setVisibility(View.VISIBLE);
        plataClan.setText(String.valueOf(actual.getMoney()));
        btnclan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClanMissionsActivity.class);
                intent.putExtra("clanReallySelected", (Serializable) actual);
                //intent.putExtra("clanSelected", actual.getMisiones());
                startActivity(intent);
            }
        });
        btnerrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeClan();
            }
        });

    }

   //public void updateUserClanId(String idPlace)
  //  {
      //  mDatabase.child("users").child(mAuth.getUid()).child("idClan").setValue(idPlace);
    //}

    public void removeClan()
    {
       mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("idClan").setValue("none");
       actual.removeMember();
       mDatabase.child("clans").child(actual.getPlaceID()).setValue(actual);
       actual= null;
       UserLogic.getInstance().getUser().setIdClan("none");
       getUserClan();
       btnclan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               changeToCreateClan();
           }
       });
       //verificarClan();
       Toast.makeText(this.getActivity(), "Clan removed", Toast.LENGTH_LONG).show();
    }

    public void changeToCreateClan()
    {
        Intent intent = new Intent(getActivity(), CreateClanActivity.class);
        intent.putExtra(EXTRA_MESSAGE  , "hello test");
        startActivity(intent);
    }

//  public void onResume() {

//        super.onResume();

  //  }

public void onStart() {

    super.onStart();
    if(actual!=null)
    {
        verificarClan(actual.getPlaceID());
    }
}

}
