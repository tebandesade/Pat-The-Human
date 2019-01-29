package co.fresa.pat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import co.fresa.pat.mundo.Mission;
import co.fresa.pat.mundo.UserLogic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivitiesFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private LinearLayout linearLayout;
    private HashMap mapMissionId;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Mantenemos una lista de las misiones en la pantalla para acceder a su información
    private ArrayList<Mission> missions;
    // y un hash de su key en la base de datos
    private ArrayList<Integer> missionIdHashes;


    public ActivitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivitiesFragment newInstance(String param1, String param2) {
        ActivitiesFragment fragment = new ActivitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        missions = new ArrayList<Mission>();
        mapMissionId = new HashMap();
        missionIdHashes = new ArrayList<Integer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activities, container, false);
        linearLayout = view.findViewById(R.id.linear);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        //de hecho es el mismo objeto ActivitiesFragment
        final View.OnClickListener buttonListener = this;

        mDatabase.child("missions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("missions", "missions");
                Iterable<DataSnapshot> missionsData = dataSnapshot.getChildren();

                for (DataSnapshot mission : missionsData) {
                    String missionId = mission.getKey();
                    Mission objmission = mission.getValue(Mission.class);

                    if (!objmission.getCriteria().equals("LOCATION")) {
                        //añadir esta mision a la lista
                        missions.add(objmission);
                        missionIdHashes.add(mission.getKey().hashCode());
                        mapMissionId.put(mission.getKey().hashCode(),missionId);
                        //añadir boton al layout
                        Button btnTag = new Button(getContext());
                        btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        btnTag.setText(objmission.getName());
                        // id del boton es (un hash de) la key de la mision en la base de datos
                        btnTag.setId(mission.getKey().hashCode());
                        btnTag.setTextSize(20);
                        btnTag.setOnClickListener(buttonListener);
                        linearLayout.addView(btnTag);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            //mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onClick(View view) {
        int missionId = ((Button)view).getId();
        for (int i=0; i<missionIdHashes.size(); i++) {
            if (missionId == missionIdHashes.get(i)) {
                Mission clicked = missions.get(i);

                if (clicked.getCriteria().equals("CUSTOM")) {
                    Intent intent = new Intent(getActivity(), CustomMissionActivity.class);
                    intent.putExtra("mission", clicked);
                    intent.putExtra("idMision", (String) mapMissionId.get(missionIdHashes.get(i)));
                    startActivity(intent);
                }
                // TODO implementar misiones con cada uno de los siguientes cuatro sensores
                else if (clicked.getCriteria().equals("CAMERA")) {}
                else if (clicked.getCriteria().equals("LOCATION")) {}
                else if (clicked.getCriteria().equals("SHAKE")) {
                    Intent intent = new Intent(getActivity(), ShakeitMissionActivity.class);
                    intent.putExtra("level", new Integer(clicked.getLevel()));
                    intent.putExtra("reward", new Integer(clicked.getReward()));
                    intent.putExtra("idMision", (String) mapMissionId.get(missionIdHashes.get(i)));
                    startActivity(intent);
                }
                else if (clicked.getCriteria().equals("SCREAM")) {
                    Intent intent = new Intent(getActivity(), ScreamMissionActivity.class);
                    intent.putExtra("level", new Integer(clicked.getLevel()));
                    intent.putExtra("reward", new Integer(clicked.getReward()));
                    intent.putExtra("idMision", (String) mapMissionId.get(missionIdHashes.get(i)));
                    startActivity(intent);
                }
                else if (clicked.getCriteria().equals("DARK")) {
                    Intent intent = new Intent(getActivity(), DarkMissionActivity.class);
                    intent.putExtra("reward", new Integer(clicked.getReward()));
                    intent.putExtra("idMision", (String) mapMissionId.get(missionIdHashes.get(i)));
                    startActivity(intent);
                }
            }
        }
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }
}
