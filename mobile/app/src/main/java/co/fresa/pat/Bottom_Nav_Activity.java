package co.fresa.pat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import co.fresa.pat.mundo.Clan;

//TODO cambiar por ActivitiesFragment2
public class Bottom_Nav_Activity extends AppCompatActivity   implements ClanFragment.OnFragmentInteractionListener, PatFragment.OnFragmentInteractionListener, ActivitiesFragment2.OnFragmentInteractionListener {

    private TextView mTextMessage;

    private FirebaseAuth emailPasswordActivity;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("ResourceAsColor")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_social:
                    //mTextMessage.setText(R.string.title_social);
                    selectedFragment = ClanFragment.newInstance("Testparam1","testparam2");
                    loadFragment(selectedFragment);
                   // return true;
                    return true;
                case R.id.navigation_pat:
                    //mTextMessage.setText(R.string.title_pat);
                    //return true;
                    selectedFragment = PatFragment.newInstance("Testparam1","testparam2");
                    loadFragment(selectedFragment);
                    return true;
                case R.id.navigation_activities:
                    //mTextMessage.setText(R.string.title_activities);
                    //return true;
                    //Color lila de actividades
                    //View someView = findViewById(R.id.container);
                    //View root = someView.getRootView();
                    //root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.azulbasico));
                    //TODO cambiar por ActivitiesFragment
                    selectedFragment = ActivitiesFragment2.newInstance("Testparam1","testparam2");
                    loadFragment(selectedFragment);
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_activity);
        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new ClanFragment());

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar4);
        //setSupportActionBar(myToolbar);
    }

    private void loadFragment(Fragment selectedFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }
}
