package co.fresa.pat.mundo;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserLogic {
    private static UserLogic instance = null;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String userKey;
    User user;

    protected UserLogic() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void cargarUsuario() {
        userKey = mAuth.getCurrentUser().getUid();
        user = new User();

        mDatabase.child("users").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                MisionesClanLogic.getInstance().cargarMisiones();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public static UserLogic getInstance() {
        if(instance == null) instance = new UserLogic();
        return instance;
    }

    public void sumarMonedas(final int reward) {
        user.sumarMonedas(reward);
        mDatabase.child("users").child(userKey).setValue(user);
    }

    public void sumarMonedasClan(final int reward, final String idMission)
    {
        final String idClan = user.getIdClan();
        if (idClan==null|| idClan.equalsIgnoreCase("none"))
        {

        }
        else
        {
            final Query users = mDatabase.child("clans").child(idClan);
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap testDebug = (HashMap) dataSnapshot.getValue();

                    Clan temp = new Clan(testDebug);
                    if (temp!=null)
                    {
                        try
                        {

                            if(temp.getMisiones().get(idMission)==null)
                            {

                            }
                            else
                            {
                                temp.addAmountMoney(reward);
                                users.getRef().setValue(temp);
                            }

                        }
                        catch(Exception e)
                        {
                            System.out.println("Clan doesnt have mission");
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }



    public User getUser() {
        return user;
    }

    public void setAvatarInfo(int posicion, int valor) {
        user.setAvatarInfo(posicion,valor);
        mDatabase.child("users").child(userKey).setValue(user);
    }
}