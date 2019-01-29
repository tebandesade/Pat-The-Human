package co.fresa.pat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import co.fresa.pat.mundo.UserLogic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ImageButton imageButtonShirt;
    ImageButton imageButtonSkin;
    ImageButton imageButtonPants;
    ImageButton imageButtonHair;
    TextView coins;

    public PatFragment() {
        // Required empty public constructor
    }
    public static PatFragment newInstance(String param1, String param2) {
        PatFragment fragment = new PatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView nameTV = getActivity().findViewById(R.id.avatarName);
        nameTV.setText("Pat "+UserLogic.getInstance().getUser().getPatName());

        coins = getActivity().findViewById(R.id.textViewCoins);

        imageButtonHair = getActivity().findViewById(R.id.imageButtonHair);
        imageButtonHair.setId((int) 4);
        imageButtonHair.setOnClickListener(this);

        imageButtonPants = getActivity().findViewById(R.id.imageButtonPants);
        imageButtonPants.setId((int) 3);
        imageButtonPants.setOnClickListener(this);

        imageButtonShirt = getActivity().findViewById(R.id.imageButtonShirt);
        imageButtonShirt.setId((int) 2);
        imageButtonShirt.setOnClickListener(this);

        imageButtonSkin = getActivity().findViewById(R.id.imageButtonSkin);
        imageButtonSkin.setId((int) 1);
        imageButtonSkin.setOnClickListener(this);

        ImageButton boton = getActivity().findViewById(R.id.logOutButton);
        boton.setId((int) 5);
        boton.setOnClickListener(this);

        updateAvatar();
    }

    public void updateAvatar() {
        String packageName = getActivity().getPackageName();
        List<Integer> avatarInfo = UserLogic.getInstance().getUser().getAvatarInfo();

        int[] layersIds = new int[4];
        Drawable[] layers = new Drawable[4];
        for (int i = 0; i < layers.length; i++) {
            layersIds[i] = getResources().getIdentifier("layer"+(i+1)+"basesprite"+avatarInfo.get(i),"drawable",packageName);
            layers[i] = getResources().getDrawable(layersIds[i]);
        }
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        ((ImageView) getActivity().findViewById(R.id.avatarImageView)).setImageDrawable(layerDrawable);

        imageButtonSkin.setImageDrawable(layers[0]);
        imageButtonShirt.setImageDrawable(layers[1]);

        Bitmap pants = BitmapFactory.decodeResource(getResources(), layersIds[2]);
        pants = Bitmap.createBitmap(pants, 0,pants.getHeight()/2, pants.getWidth(), pants.getHeight()-pants.getHeight()/2);
        imageButtonPants.setImageBitmap(pants);
        Bitmap hair = BitmapFactory.decodeResource(getResources(), layersIds[3]);
        hair = Bitmap.createBitmap(hair, 0,0, hair.getWidth(), hair.getHeight()/4);
        imageButtonHair.setImageBitmap(hair);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != (int) 5) {
            createDialog(view.getId());
        }
        else {
            // sign out
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), EmailPasswordActivity.class);
            startActivity(intent);
        }
    }

    private void createDialog (int layer) {
        Bundle args = new Bundle();
        args.putInt("layer", layer);
        SelectorDialogFragment dfragment = new SelectorDialogFragment();
        dfragment.setArguments(args);
        dfragment.setTargetFragment(this, 322);
        dfragment.show(getFragmentManager(), "dialFragment");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 322) updateAvatar();
    }

    @Override
    public void onStart() {
        super.onStart();
        coins.setText(""+UserLogic.getInstance().getUser().getMonedas());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pat, container, false);
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
}