package co.fresa.pat.mundo;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;


public class Listen4ScreamTask extends AsyncTask<Integer, Object, Boolean> {
    boolean listening;
    MediaRecorder mRecorder;

    @Override
    protected Boolean doInBackground(Integer... params) {
        listening = false;
        startRecorder();

        final int reward = (int) params[0];
        int level = (int) params[1];

        while (listening) {
            if (mRecorder != null) {
                int amplitude = mRecorder.getMaxAmplitude();
                System.out.println(amplitude);

                if (amplitude > 3000 * level) {
                    System.out.println("SCREAMED");

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String userKey = mAuth.getCurrentUser().getUid();

                    mDatabase.child("users").child(userKey).runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            User user = mutableData.getValue(User.class);
                            if (user == null) {
                                Log.d("Monedas update", "No se encontro la mision.");
                                return Transaction.success(mutableData);
                            }
                            // Le sumamos las monedas al usuario
                            user.sumarMonedas(reward);

                            // Set value and report transaction success
                            mutableData.setValue(user);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            Log.d("Monedas update", "Transaccion completa.");
                        }
                    });

                    stopRecorder();
                    return true;
                }
            } else {
                System.out.println("Not recording.");
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean bool) {

    }

    public void startRecorder(){
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));
            } catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try {
                mRecorder.start();
            } catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
        }
        if (mRecorder != null) listening = true;
    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        listening = false;
    }
}