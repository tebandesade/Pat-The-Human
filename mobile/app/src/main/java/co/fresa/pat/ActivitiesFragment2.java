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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.fresa.pat.mundo.Mission;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivitiesFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivitiesFragment2 extends Fragment implements View.OnClickListener {
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
    private boolean hayListView;

    // Mantenemos una lista de las misiones en la pantalla para acceder a su información
    private List<Mission> missions;
    // y un hash de su key en la base de datos
    private ArrayList<Integer> missionIdHashes;


    public ActivitiesFragment2() {
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
    public static ActivitiesFragment2 newInstance(String param1, String param2) {
        ActivitiesFragment2 fragment = new ActivitiesFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("ONCREATE");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        missions = new ArrayList<Mission>();
        mapMissionId = new HashMap();
        missionIdHashes = new ArrayList<Integer>();
    }

    private void cargarMisiones(View view) {
        // getMisiones()
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("missions").orderByChild("dateAdded").endAt(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("missions", "missions");
                Iterable<DataSnapshot> missionsData = dataSnapshot.getChildren();

                for (DataSnapshot mission : missionsData) {
                    String missionId = mission.getKey();
                    Mission objmission = mission.getValue(Mission.class);

                    if (!objmission.getCriteria().equals("LOCATION")) {
                        //añadir esta mision a la lista
                        objmission.setId(mission.getKey());
                        missions.add(objmission);
                        missionIdHashes.add(mission.getKey().hashCode());
                        mapMissionId.put(mission.getKey().hashCode(), missionId);
                    }
                }


                    final ListView listView = getActivity().findViewById(R.id.listMissions);
                    listView.setDividerHeight(10);

                    // ponemos las misiones en listView
                    listView.setAdapter(new CustomListAdapter(getActivity(), missions));
                    // When the user clicks on the ListItem
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                            Object o = listView.getItemAtPosition(position);
                            Mission clicked = (Mission) o;

                            if (clicked.getCriteria().equals("CUSTOM")) {
                                Intent intent = new Intent(getActivity(), CustomMissionActivity.class);
                                intent.putExtra("mission", clicked);
                                intent.putExtra("idMision", clicked.getId());
                                startActivity(intent);
                            } else if (clicked.getCriteria().equals("SHAKE")) {
                                Intent intent = new Intent(getActivity(), ShakeitMissionActivity.class);
                                intent.putExtra("level", new Integer(clicked.getLevel()));
                                intent.putExtra("reward", new Integer(clicked.getReward()));
                                intent.putExtra("idMision", clicked.getId());
                                startActivity(intent);
                            } else if (clicked.getCriteria().equals("SCREAM")) {
                                Intent intent = new Intent(getActivity(), ScreamMissionActivity.class);
                                intent.putExtra("level", new Integer(clicked.getLevel()));
                                intent.putExtra("reward", new Integer(clicked.getReward()));
                                intent.putExtra("idMision", clicked.getId());
                                startActivity(intent);
                            } else if (clicked.getCriteria().equals("DARK")) {
                                Intent intent = new Intent(getActivity(), DarkMissionActivity.class);
                                intent.putExtra("reward", new Integer(clicked.getReward()));
                                intent.putExtra("idMision", clicked.getId());
                                startActivity(intent);
                            }
                        }
                    });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activities2, container, false);
        cargarMisiones(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onPause() {
        hayListView = false;
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hayListView = false;
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
