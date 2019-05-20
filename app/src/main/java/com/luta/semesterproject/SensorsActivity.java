package com.luta.semesterproject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SensorsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    //private FirebaseFirestore db;
    private static SensorAdapter sensorAdapter;
    private RecyclerView sensorsRecyclerView;
    private static List<Sensor> listViewData;
    private static List<DocumentSnapshot> sensors;
    private static List<String> floors;
    private static ArrayAdapter<String> spinnerAdapter;
    private static int selectedFloor = 0;
    private static boolean autoUpdate = false;
    private static int delayForUpdate = 4000;
    private static Handler handler; // handler for time
    private static Runnable runnable;
    private FloatingActionButton fab;
    private static String databaseCollection = "Sensors";

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
                        int id = menuItem.getItemId();
                        //menuItem.setChecked(true); //persist highlight
                        //drawerLayout.closeDrawers(); // close drawer when item selected
                        if(id == R.id.drawer_switch)
                        {
                            return false;
                        }
                        else if(id == R.id.logout)
                        {
                            FirebaseAuth.getInstance().signOut();
                            Intent i = new Intent(SensorsActivity.this, SignInActivity.class);
                            startActivity(i);
                        }
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

        floors = new ArrayList<>(); // floors list initialization
        floors.add("All floors");
        Spinner spinner = (Spinner) findViewById(R.id.menu_spinner); // floors adapter
        spinnerAdapter = new ArrayAdapter(this,R.layout.spinner_item,floors);
        //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // suknisa isvaizda nors neturetu
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new SpinnerActivity());

        //db = FirebaseFirestore.getInstance(); // CLOUD FIRESTORE INSTANCE
        sensorsRecyclerView = findViewById(R.id.sensors_recyclerview);

        listViewData = new ArrayList<>(); // initialize arraylist for listview
        sensors = new ArrayList<>(); // initialize arraylist for ids of sensors
        sensorAdapter = new SensorAdapter(this, listViewData);

        RecyclerView recyclerView = findViewById(R.id.sensors_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sensorAdapter); // set adapter...

        Switch toggleAuto = (Switch) navigationView.getMenu().findItem(R.id.switch_auto_update).getActionView();
        toggleAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    delayForUpdate = 1500;
                    autoUpdate = true;
                    Log.i("SENSORS","autoUpdate = true");
                }
                else {
                    delayForUpdate = 4000;
                    autoUpdate = false;
                    Log.i("SENSORS","autoUpdate = false");
                }
            }
        });
        handler = new Handler();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClick(v);
            }
        });

        readDatabase();
    }

    public void fabClick(View view){
        if(selectedFloor == 0)
            readDatabase();
        else readDatabase(selectedFloor);
        sensorAdapter.notifyDataSetChanged();
    }

    public static void readDatabase(){

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // CLOUD FIRESTORE INSTANCE
        db.collection(databaseCollection).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            listViewData.clear(); // JEIGU NORIU KAD NEDETU PAPILDOMAI O TIK ATNAUJINTU SENUS !!!!!!!!!!!!!!!!!!!!!
                            sensors.clear();
                            floors.clear();
                            floors.add("All floors");
                            selectedFloor = 0;
                            for(DocumentSnapshot d : list)
                            {
                                Sensor s = d.toObject(Sensor.class);
                                Log.i("SENSORS", d.toString());
                                listViewData.add(s);
                                sensors.add(d);
                                if(!floors.contains("Floor " + (int)s.getFloor()))
                                    floors.add("Floor " + (int)s.getFloor());

                            }
                            Log.i("SENSORS", "FLOORS COUNT "+ floors.size());
                            sensorAdapter.notifyDataSetChanged();
                            Collections.sort(floors);
                            spinnerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    public static void readDatabase(final int floor){

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // CLOUD FIRESTORE INSTANCE
        db.collection(databaseCollection).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            listViewData.clear(); // JEIGU NORIU KAD NEDETU PAPILDOMAI O TIK ATNAUJINTU SENUS !!!!!!!!!!!!!!!!!!!!!
                            sensors.clear();
                            floors.clear();
                            floors.add("All floors");
                            selectedFloor = floor;
                            for(DocumentSnapshot d : list)
                            {
                                Sensor s = d.toObject(Sensor.class);
                                Log.i("SENSORS", d.toString());
                                if(s.getFloor() == floor) {
                                    listViewData.add(s);
                                    sensors.add(d);
                                }
                                if(!floors.contains("Floor " + (int)s.getFloor()))
                                    floors.add("Floor " + (int)s.getFloor());

                            }
                            Log.i("SENSORS", "FLOORS COUNT "+ floors.size());
                            sensorAdapter.notifyDataSetChanged();
                            Collections.sort(floors);
                            spinnerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public static void updateStatus(int id, String newStatus){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("status", newStatus);
        db.collection(databaseCollection).document(sensors.get(id).getId()).set(data, SetOptions.merge());
        if(selectedFloor == 0)
            readDatabase();
        else readDatabase(selectedFloor);
    }

    @Override
    protected void onResume() {

        //start handler as activity become visible
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                Log.i("SENSORS", "onResume");
                if (autoUpdate) {
                    Log.i("SENSORS", "AutoUpdate end");
                    if (selectedFloor == 0)
                        readDatabase();
                    else readDatabase(selectedFloor);
                }
                handler.postDelayed(runnable, delayForUpdate);
            }
        }, delayForUpdate);
        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
