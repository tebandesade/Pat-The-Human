package co.fresa.pat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.fresa.pat.mundo.Clan;
import co.fresa.pat.mundo.MisionesClanLogic;
import co.fresa.pat.mundo.UserLogic;

/**
 * Created by teban on 4/8/2018.
 */

public class CreateClanDialogFragment  extends DialogFragment
{
    private static final int REQUEST_PLACE_PICKER =1 ;
    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    private DatabaseReference mDatabase;

    public EditText getMyview() {
        return myview;
    }

    public void setMyview(EditText myview) {
        this.myview = myview;
    }

    private  EditText myview;

    public EditText getUsernameview() {
        return usernameview;
    }

    public void setUsernameview(EditText usernameview) {
        this.usernameview = usernameview;
    }

    private  EditText usernameview;
    public String getPlaceIdSelected() {
        return placeIdSelected;
    }
   // private EditText temp;
    //private EditText username ;
    public void setPlaceIdSelected(String placeIdSelected, String name) {
        this.placeIdSelected = placeIdSelected;

        getMyview().setText(name);

    }


    private String placeName;

    private String placeIdSelected ;
    private FirebaseAuth mAuth;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Construct a GeoDataClient.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        placeIdSelected = new String();
        mAuth= FirebaseAuth.getInstance();
        mGeoDataClient = Places.getGeoDataClient(this.getActivity(), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this.getActivity(), null);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create a Clan!");
        LayoutInflater inflater = getActivity().getLayoutInflater();

        myview = inflater.inflate(R.layout.dialogfragment_create_clan, null).getRootView().findViewById(R.id.password);
        usernameview = inflater.inflate(R.layout.dialogfragment_create_clan, null).getRootView().findViewById(R.id.username);

        myview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPickButtonClick(view);
                    }
                }
        );
        if (placeIdSelected!=null )
        {
            myview.setText(placeIdSelected);
        }
        builder.setView(inflater.inflate(R.layout.dialogfragment_create_clan, null)).setMessage("Ready to be part of AWESOME!")
                .setPositiveButton("Clan UP!", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        //Toca get attributes  y crear un objecto clan y pasarlo a la base de datos.
                        //

                        if(verificarCampos(getUsernameview(),getMyview())){

                            System.out.println("LOOK OR CLAN BUG") ;
                            System.out.println(mDatabase.child("clans").child(getPlaceIdSelected()));
                            if(mDatabase.child("clans").child(getPlaceIdSelected())==null)
                            {
                                Toast.makeText(getContext(), "Clan already exists",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                               //String debuguser=  getDialog()z.findViewById(R.id.username).getRootView().gette
                                Clan newClan = new Clan(getPlaceName(), getPlaceIdSelected());


                                String uId = mAuth.getCurrentUser().getUid();
                                    System.out.println(uId);
                                if(uId.isEmpty()==false)
                                {
                                    newClan.addMember();
                                    mDatabase.child("users").child(uId).child("idClan").setValue(getPlaceIdSelected());
                                    MisionesClanLogic.getInstance().cargarMisiones();
                                    UserLogic.getInstance().getUser().setIdClan(getPlaceIdSelected());
                                    mDatabase.child("clans").child(getPlaceIdSelected()).setValue(newClan);
                                    ((CreateClanActivity) getActivity()).addObjToClans(newClan);
                                    ((CreateClanActivity) getActivity()).displayClans();
                                    Intent intent = new Intent(getActivity(),Bottom_Nav_Activity.class);
                                    startActivity(intent);
                                    //ClanFragment cf = new ClanFragment();
                                    //cf.getUserClan();
                                }

                            }
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        setPlaceIdSelected(null,null);
                        setPlaceName(null);
                    }
                });

        builder.setView(myview.getRootView());
        //builder.setView(myview);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private boolean verificarCampos(EditText nom, EditText plcid) {
            boolean valid = false;

            String name =  nom.getText().toString();
            if (TextUtils.isEmpty(name)) {
                nom.setError("Required.");
                valid = true;
            } else {
                nom.setError(null);
            }

            String place = plcid.getText().toString();
            if (TextUtils.isEmpty(place)) {
                plcid.setError("Required.");
                valid = true;
            } else {
                plcid.setError(null);
            }

            return valid;

    }

    public void onPickButtonClick(View v) {
        // Construct an intent for the place picker
        int PLACE_PICKER_REQUEST = 1;

        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this.getActivity());
            /*
            Start the intent by requesting a result,
            identified by a request code.
            */

            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == -1) {
                Place place = PlacePicker.getPlace(data, this.getActivity());
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                String toastMsg = String.format("Place: %s", place.getName());
                //String txtName = getPlaceName();
                setPlaceIdSelected(place.getId(),place.getName().toString());
                if (getPlaceName()==null || getPlaceName().isEmpty()){
                    setPlaceName((String) place.getName());
                }
                else
                {
                    setPlaceName("input");
                }

                Toast.makeText(this.getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        if (placeName==null)
        {

        }
            else
        {
            if(placeName.equalsIgnoreCase("input"))
            {

                this.placeName =  getUsernameview().getText().toString();
            }
            else
            {
                this.placeName = placeName;
            }

        }


    }
}
