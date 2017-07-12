package com.philong.wifichua.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philong.wifichua.R;
import com.philong.wifichua.adapter.WifiAdapter;
import com.philong.wifichua.model.Wifi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.philong.wifichua.R.layout.dialog;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener{

    //toolbar
    private Toolbar mToolbar;
    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    //recyclerview
    private RecyclerView mRecyclerView;
    private WifiAdapter mWifiAdapter;
    private List<Wifi> mWifiList;
    private LinearLayoutManager mLinearLayoutManager;
    //Geocoder
    private Geocoder mGeocoder;
    //Location
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;
    //back to stop
    private boolean isBack = false;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getSupportActionBar() == null){
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(R.string.app_name);
        }
        //recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mWifiList = new ArrayList<>();
        //Geocoder
        mGeocoder = new Geocoder(this, Locale.getDefault());
        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("wifi");
        //get database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!mWifiList.isEmpty()){
                    mWifiList.clear();
                }
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Wifi wifi = snapshot.getValue(Wifi.class);
                    float khoangCach = tinhKhoangCach(mLatitude, mLongitude, wifi.getLattitude(), wifi.getLongitude());
                    if(khoangCach <= 50){
                        String diaChi = getAdrdess(wifi.getLattitude(), wifi.getLongitude());
                        wifi.setDiachi(diaChi);
                        mWifiList.add(wifi);

                    }
                }
                mWifiAdapter = new WifiAdapter(MainActivity.this, mWifiList);
                mRecyclerView.setAdapter(mWifiAdapter);
                mWifiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Google api
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();


    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        if(isBack){
            super.onBackPressed();
        }
        isBack = true;
        Toast.makeText(this, "Bấm thêm 1 lần nữa để thoát", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBack = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.add:
                final Dialog dialog = createDialog();
                TextView txtSSID = (TextView) dialog.findViewById(R.id.txtSSID);
                final EditText edtMatKhauWifi = (EditText) dialog.findViewById(R.id.edtMatKhauWifi);
                Button btnGui = (Button) dialog.findViewById(R.id.btnGui);
                txtSSID.setText(getCurrentWifi());
                btnGui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String matKhauWifi = edtMatKhauWifi.getText().toString();
                        Wifi wifi = new Wifi(getCurrentWifi(), mLatitude, mLongitude, matKhauWifi, getAdrdess(mLatitude, mLongitude));
                        DatabaseReference databaseReference = mDatabaseReference.push();
                        databaseReference.setValue(wifi, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError != null){
                                    Toast.makeText(MainActivity.this, "Có lổi xảy ra", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "Gửi thành công", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            }
                        });

                    }
                });
                dialog.show();
                break;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(DangNhapActivity.newIntent(MainActivity.this));
                finish();
                break;
        }
        return true;
    }

    private Dialog createDialog(){
        View view = LayoutInflater.from(this).inflate(dialog, null);
        Dialog alBuilder = new Dialog(this);
        alBuilder.setTitle("Gửi thông tin Wifi");
        alBuilder.setContentView(view);
        return alBuilder;
    }

    private String getAdrdess(double latitude, double longitude){
        String diaChi = "";
        try {
            List<Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);
            if(addresses.isEmpty()){
                diaChi = "Error!!!";
            }else{
                if(addresses.size() > 0){
                    diaChi = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diaChi;
    }

    public String getCurrentWifi(){
        String ssid = "Cần kết nối wifi!";
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){
            if(networkInfo.isAvailable() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if(wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getSSID())){
                    ssid = wifiInfo.getSSID();
                }
            }
        }
        return ssid;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation != null){
            mLatitude = mLocation.getLatitude();
            mLongitude = mLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(mLocation != null){
            mLatitude = mLocation.getLatitude();
            mLongitude = mLocation.getLongitude();
        }
    }

    private float tinhKhoangCach(double lat1, double long1, double lat2, double long2){
        float[] khoangCach = new float[1];
        Location.distanceBetween(lat1, long1, lat2, long2, khoangCach);
        return khoangCach[0];
    }

}
