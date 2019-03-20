package com.luta.semesterproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class SensorsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseFirestore db;
    private SensorAdapter sensorAdapter;
    private RecyclerView sensorsRecyclerView;
    private List<Sensor> listViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_id);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true); //persist highlight
                        drawerLayout.closeDrawers(); // close drawer when item selected


                        return false;
                    }
                }
        );

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);

        db = FirebaseFirestore.getInstance(); // CLOUD FIRESTORE INSTANCE
        sensorsRecyclerView = findViewById(R.id.sensors_recyclerview);

        listViewData = new ArrayList<>(); // initialize arraylist for listview
        sensorAdapter = new SensorAdapter(this, listViewData);

        RecyclerView recyclerView = findViewById(R.id.sensors_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sensorAdapter); // set adapter...
    }

    public void buttonPressed1(View view){
        readDatabase();
        sensorAdapter.notifyDataSetChanged();
    }
    public void buttonPressed2(View view){
        listViewData.clear();
        sensorAdapter.notifyDataSetChanged();
    }

    public void readDatabase(){

        //db = FirebaseFirestore.getInstance(); // CLOUD FIRESTORE INSTANCE
        db.collection("Temporary").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            listViewData.clear(); // JEIGU NORIU KAD NEDETU PAPILDOMAI O TIK ATNAUJINTU SENUS !!!!!!!!!!!!!!!!!!!!!
                            for(DocumentSnapshot d : list)
                            {
                                Sensor s = d.toObject(Sensor.class);
                                listViewData.add(s);
                            }
                            sensorAdapter.notifyDataSetChanged();
                        }
                    }
                });
        /*db.collection("Sensors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> list =
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("dbinfo", document.getId() + " => " + document.getData());
                                temp.add(document.getId() + " => " + document.getData());
                            }
                            Log.i("tempinfo",String.valueOf(temp.size()));
                            SingletonClass s = SingletonClass.getInstance();
                            s.setData(temp);
                        } else {
                            Log.w("dbinfo", "Error getting documents.", task.getException());
                        }

                    }
                });*/
    }

    public void readDatabase1()
    {
        CollectionReference usersCollectionRef = db.collection("users");
        List<DocumentReference> documents = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(SensorsActivity.this, SignInActivity.class);
                startActivity(i);
                return true;

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
