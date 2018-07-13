package com.vit.trafficvolumemap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import timber.log.Timber;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    // ---------------------------------------------------------------------------------------------
    // FIELDS
    // ---------------------------------------------------------------------------------------------
    private static final int MY_REQUEST_INT = 177;

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private LatLng mCurrentLocation;

    private LatLng mLatLngSearchPosition;


    // ---------------------------------------------------------------------------------------------
    // OVERRIDE METHODS
    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            initView();
        } catch (Exception e) {
            Timber.e(e);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.setBuildingsEnabled(true);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_INT);
                }
                return;
            } else {
                mMap.setMyLocationEnabled(true);
            }

            onClickMap();
        } catch (Exception e) {
            Timber.e(e);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                final Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (lastLocation == null) {
                    return;
                }
                mCurrentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                if (mLatLngSearchPosition == null) {
                    showCameraToPosition(mCurrentLocation, Constant.LEVEL_ZOOM_DEFAULT);
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    // ---------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ---------------------------------------------------------------------------------------------

    /**
     * init view
     */
    private void initView() {
        try {
            setContentView(R.layout.activity_maps);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Timber.e(e);
        }

    }

    /**
     * show marker and circle around marker when touch map
     */
    private void onClickMap() {
        try {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    showMarkerToGoogleMap(latLng);
                    showCircleToGoogleMap(latLng, 0.5f);
                }
            });
        } catch (Exception e) {
            Timber.e(e);
        }

    }

    /**
     * show marker when user click a position on map
     *
     * @param position position's marker
     */
    public void showMarkerToGoogleMap(LatLng position) {
        try {
            mMap.clear();
            MarkerOptions markerOptions = new MarkerOptions().position(position);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_active));
            mMap.addMarker(markerOptions);
        } catch (Exception e) {
            Timber.e(e);
        }

    }


    /**
     * move camera to a position on map
     *
     * @param position position is LatLng
     * @param zoomLevel zoom level on screen
     */
    public void showCameraToPosition(LatLng position, float zoomLevel) {
        try {
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(position)
                    .zoom(zoomLevel)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();

            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
            }
        } catch (Exception e) {
            Timber.e(e);
        }

    }

    /**
     * draw circle on positon of map
     *
     * @param position position is LatLng
     * @param radius radius of circle
     */
    public void showCircleToGoogleMap(LatLng position, float radius) {
        try {
            if (position == null) {
                return;
            }
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(position);
            //Radius in meters
            circleOptions.radius(radius * 1000);
            circleOptions.fillColor(getResources().getColor(R.color.circle_on_map));
            circleOptions.strokeColor(getResources().getColor(R.color.circle_on_map));
            circleOptions.strokeWidth(0);
            if (mMap != null) {
                mMap.addCircle(circleOptions);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
