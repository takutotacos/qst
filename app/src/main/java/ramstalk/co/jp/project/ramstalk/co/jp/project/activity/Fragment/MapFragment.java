package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.ShowImageActivity;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Posting;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Postings;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private String TAG = MapFragment.class.getSimpleName();
    private final static String CATEGORY_ID = "category";
    private SharedPreferences sharedPreferences;
    private GoogleMap mMap;
    private MapView mMapView;
    private Toast toast;

    public MapFragment() {
    }

    public static MapFragment newInstance(String categoryId) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String categoryId = getArguments().getString(CATEGORY_ID);
        sharedPreferences = getActivity().getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("auth_token", "");
        String latitude = sharedPreferences.getString("latitude", "");
        String longitude = sharedPreferences.getString("longitude", "");

        ApiService apiService = ApiManager.getApiService();
        Observable<Postings> postings = apiService.getPostingsWithinCertainDistance(
                authToken, categoryId, latitude, longitude);
        postings.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Postings>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR : " + e.toString());
                    }

                    @Override
                    public void onNext(Postings postings) {
                        if (postings.getPostings() != null) {
                            for (Posting posting : postings.getPostings()) {
                                String comment = posting.getContent();
                                String id = posting.getId();
                                String lat = posting.getLatitude();
                                String lng = posting.getLongitude();
                                // A posting data has to have location data
                                if (lat != null & lng != null) {
                                    LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                    mMap.addMarker(new MarkerOptions().position(latlng).title(comment)).setTag(id);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            String postingId = String.valueOf(marker.getTag());
                                            Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                                            intent.putExtra("postingId", postingId);
                                            startActivity(intent);
                                            return true;
                                        }
                                    });
                                }
                            }
                        } else {
                            toast("近くに閲覧可能なデータがありません。");
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void toast(String message) {
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}