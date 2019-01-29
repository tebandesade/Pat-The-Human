package co.fresa.pat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import co.fresa.pat.mundo.Clan;
import co.fresa.pat.mundo.MisionesClanLogic;
import co.fresa.pat.mundo.UserLogic;

/**
 * Created by teban on 4/29/2018.
 */

@SuppressLint("ValidFragment")
class ExistingClanDialogFragment extends DialogFragment {

    private DatabaseReference databaseReference;
    private Clan cSeleccionado;
    private FirebaseAuth mAu;





    public ExistingClanDialogFragment(Clan clanSelected, FirebaseAuth mAuth, DatabaseReference databaseReference) {
        this.cSeleccionado = clanSelected;
        this.databaseReference = databaseReference;
        this.mAu = mAuth;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you wish to join ")
                .setPositiveButton("Accept clan", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                       databaseReference.child("users").child(mAu.getCurrentUser().getUid()).child("idClan").setValue(cSeleccionado.getPlaceID());
                        UserLogic.getInstance().getUser().setIdClan(cSeleccionado.getPlaceID());
                        updateMemberSelectedClan();
                        MisionesClanLogic.getInstance().cargarMisiones();
                        Intent intent = new Intent(getActivity(),Bottom_Nav_Activity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Look for other options", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void updateMemberSelectedClan() {
        cSeleccionado.addMember();
        databaseReference.child("clans").child(cSeleccionado.getPlaceID()).setValue(cSeleccionado);
        Toast.makeText(this.getActivity(), "Clan "+cSeleccionado.getName()+ " seleccionado!", Toast.LENGTH_LONG).show();

    }

    public void setFireBase(DatabaseReference db)
    {
        this.databaseReference =db;
    }

}
