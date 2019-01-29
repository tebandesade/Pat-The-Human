package co.fresa.pat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import co.fresa.pat.mundo.Clan;
import co.fresa.pat.mundo.MisionesClanLogic;
import co.fresa.pat.mundo.Mission;



@SuppressLint("ValidFragment")
public class DialogSelectMissionClan extends DialogFragment {


    private  Clan clanActual;
    private DatabaseReference databaseReference;
    private Mission mSeleccionado;
    private HashMap idMisSelected;
    //private FirebaseAuth mAu;
    @SuppressLint("ValidFragment")
    public DialogSelectMissionClan(Mission misionSelected, DatabaseReference databaseReference, Clan actualclan) {
        this.mSeleccionado = misionSelected;
        this.databaseReference = databaseReference;
        this.clanActual = actualclan;
        //this.idMisSelected =idSelec;
        //this.mAu = mAuth;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you wish to add this mission?")
                .setPositiveButton("Add mission!", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        addMission();
                        //databaseReference.child("clans").child(clanActual.getPlaceID());
                        //Intent intent = new Intent(getActivity(),Bottom_Nav_Activity.class);
                        //startActivity(intent);
                    }
                })
                .setNegativeButton("Mission too easy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void addMission()
    {
        if(clanActual.getMisiones()==null){
            clanActual.inicializarMisiones();
            clanActual.addMission(mSeleccionado.getId(),mSeleccionado);
           ((ClanMissionsActivity) getActivity()).addMisionesClan(mSeleccionado);
            databaseReference.child("clans").child(clanActual.getPlaceID()).setValue(clanActual);
            ((ClanMissionsActivity) getActivity()).reSetArrayMisionesAdapter();
        }
        else
        {

            if(clanActual.getMisiones().containsKey(mSeleccionado.getId()))
            {
                Toast.makeText(this.getActivity(), "Mission already in clan!", Toast.LENGTH_LONG).show();

            }
            else
            {
                clanActual.addMission(mSeleccionado.getId(),mSeleccionado);
                ((ClanMissionsActivity) getActivity()).addMisionesClan(mSeleccionado);

                databaseReference.child("clans").child(clanActual.getPlaceID()).setValue(clanActual);
            }

            ((ClanMissionsActivity) getActivity()).setFlagStateAdding(false);
            ((ClanMissionsActivity) getActivity()).updateArrayAdapter();

        }
        MisionesClanLogic.getInstance().cargarMisiones();
    }

}
