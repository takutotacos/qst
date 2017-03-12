package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class LocationActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private String TAG = LocationActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private LocationRequest locationRequest;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        // creating LocationRequest instance and setting its accuracy and interval
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(16);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location currentLocation = fusedLocationProviderApi.getLastLocation(mGoogleApiClient);
        if(currentLocation != null) {
            intent = new Intent();
            bundle = new Bundle();
            bundle.putDouble("latitude", currentLocation.getLatitude());
            bundle.putDouble("longitude", currentLocation.getLongitude());
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            //バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest,  this);
                // Schedule a Thread to unregister location listeners
                Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                    @Override
                    public void run() {
                        fusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient, LocationActivity.this);
                    }
                }, 60000, TimeUnit.MILLISECONDS);
                // @TODO can we use something other than exception??? more specific one?
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Toast toast = Toast.makeText(this, "例外が発生、位置情報のPermissionを許可していますか。", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }
    }

    //位置情報が更新されると呼び出される
    @Override
    public void onLocationChanged(Location location) {
        Log.d("debug","update");
        intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", location.getLatitude());
        bundle.putDouble("longitude", location.getLongitude());
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            Log.d(TAG, "Already attempting to resolve an error");
            return;
        } else if (connectionResult.hasResolution()) {

        } else {
            mResolvingError = true;
        }
    }

}
