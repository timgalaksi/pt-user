package bleizing.punyatemenuser;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ItemOneFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "ItemOneFragment";

    GoogleMap mMap;
//    SupportMapFragment mapFrag;
//    LocationRequest mLocationRequest;
//    GoogleApiClient mGoogleApiClient;
//    Location mLastLocation;
//    Marker mCurrLocationMarker;

    private FloatingActionButton fab;

    private RequestQueue requestQueue;

    private CalonPenyewa calonPenyewa;

    private PermintaanBarang permintaanBarang;
    private BarangSewa barangSewa;

    private ArrayList<BarangSewa> barangSewaArrayList;
    private ArrayList<PermintaanBarang> permintaanBarangArrayList;

    private Double lat, lng;

//    private LocationManager locationManager;

    public static ItemOneFragment newInstance() {
        ItemOneFragment fragment = new ItemOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        calonPenyewa = Model.getCalonPenyewa();

        // Request Queue Volley Network Connection
        requestQueue = Volley.newRequestQueue(getActivity());
        barangSewaArrayList = new ArrayList<>();
        permintaanBarangArrayList = new ArrayList<>();

        lat = Model.getLat();
        lng = Model.getLng();
//
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        getCurrentLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_one, container, false);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();
        fragment.getMapAsync(this);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbClicked();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        permintaanBarang = null;
        barangSewa = null;
        fab.setImageResource(android.R.drawable.ic_input_add);
        if (mMap != null) {
            Log.d(TAG,"mMap != null");
            mMap.clear();
//            getCurrentLocation();
            setCenterPoint();
            if (barangSewaArrayList != null) {
                if (barangSewaArrayList.size() != 0) {
                    barangSewaArrayList.clear();
                }
            } else {
                barangSewaArrayList = new ArrayList<>();
            }

            if (permintaanBarangArrayList != null) {
                if (permintaanBarangArrayList.size() != 0) {
                    permintaanBarangArrayList.clear();
                }
            } else {
                permintaanBarangArrayList = new ArrayList<>();
            }
            getBarangSewa();
            getPermintaanBarang();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,"onMapReady()");
        mMap = googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        setCenterPoint();

        getBarangSewa();
        getPermintaanBarang();

//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);

//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng b1 = new LatLng(-6.179158, 106.876802);
//        mMap.addMarker(new MarkerOptions().position(b1).title("Jual Baju Second").icon(icon));
//
//        LatLng b2 = new LatLng(-6.180150, 106.875021);
//        mMap.addMarker(new MarkerOptions().position(b2).title("Mobil").icon(icon));
//
//        LatLng b3 = new LatLng(-6.176712, 106.873415);
//        mMap.addMarker(new MarkerOptions().position(b3).title("Motor").icon(icon));
//
//        LatLng b4 = new LatLng(-6.175871, 106.876963);
//        mMap.addMarker(new MarkerOptions().position(b4).title("Kerete Bayi").icon(icon));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(b1));
//        updateBarangLokasi();

        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(20.0f);

    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
////        lat = location.getLatitude();
////        lng = location.getLongitude();
//    }
//
//    private void getCurrentLocation() {
//        Log.d(TAG, "getCurrentLocation()");
//        if (locationManager != null) {
//            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                lat = location.getLatitude();
//                lng = location.getLongitude();
//
////                getAddress();
//            }
//        }
//        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
//    }
//
//    private android.location.LocationListener locationListener = new android.location.LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            lat = location.getLatitude();
//            lng = location.getLongitude();
//
////            getAddress();
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }
//    };

    private void fbClicked() {
        Intent intent;

        if (permintaanBarang != null) {
            intent = new Intent(getActivity(), DetailItemInput.class);
            intent.putExtra("permintaan_barang_id", permintaanBarang.getId());
        } else {
            intent = new Intent(getActivity(), DetailItemInput.class);
        }

        if (barangSewa != null) {
            intent = new Intent(getActivity(), DetailBarangSewaActivity.class);
            intent.putExtra("barang_sewa_id", barangSewa.getId());
        }
        startActivity(intent);
    }

    private void getBarangSewa() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetAPI.GET_BARANG_SEWA, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "getBarangSewa response : " + response);

                try {
                    JSONArray data = response.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.getString("id"));
                        String nama = jsonObject.getString("nama");
                        String deskripsi = jsonObject.getString("deskripsi");
                        String tgl_mulai = jsonObject.getString("tanggal_mulai");
                        String tgl_berakhir = jsonObject.getString("tanggal_berakhir");
                        String foto = jsonObject.getString("foto");
                        String lat = jsonObject.getString("lat");
                        String lng = jsonObject.getString("lng");
                        String kategori = jsonObject.getString("kategori");
                        Double harga = Double.parseDouble(jsonObject.getString("harga"));
                        int user_penyewa_id = Integer.parseInt(jsonObject.getString("user_penyewa_id"));
                        String user_penyewa_nama = jsonObject.getString("user_penyewa_nama");
                        String user_penyewa_no_hp = jsonObject.getString("user_penyewa_no_hp");

                        BarangSewa barangSewa = new BarangSewa(id, nama, deskripsi, tgl_mulai, tgl_berakhir, foto, lat, lng, harga, user_penyewa_id, user_penyewa_nama, user_penyewa_no_hp, kategori);
                        barangSewaArrayList.add(barangSewa);
                    }
                    Model.setBarangSewaArrayList(barangSewaArrayList);
                    updateBarangLokasi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error getBarangSewa : " + error);
            }
        });
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private void getPermintaanBarang() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetAPI.GET_PERMINTAAN_BARANG_BY_CALON_PENYEWA_ID + calonPenyewa.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "getPermintaanBarang response : " + response);

                try {
                    JSONArray data = response.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.getString("id"));
                        String nama = jsonObject.getString("nama");
                        String deskripsi = jsonObject.getString("deskripsi");
                        String tgl_mulai = jsonObject.getString("tanggal_mulai");
                        String tgl_berakhir = jsonObject.getString("tanggal_berakhir");
                        String lat = jsonObject.getString("lat");
                        String lng = jsonObject.getString("lng");
                        int calon_penyewa_id = Integer.parseInt(jsonObject.getString("calon_penyewa_id"));

                        PermintaanBarang permintaanBarang = new PermintaanBarang(id, nama, deskripsi, tgl_mulai, tgl_berakhir, lat, lng, calon_penyewa_id);
                        permintaanBarangArrayList.add(permintaanBarang);
                    }
                    Model.setPermintaanBarangArrayList(permintaanBarangArrayList);
                    updatePermintaanBarangLokasi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error getPermintaanBarang : " + error);
            }
        });
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private void updateBarangLokasi() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        if (barangSewaArrayList.size() != 0) {
            if (mMap != null) {
                for (BarangSewa barangSewa : barangSewaArrayList) {
                    LatLng lokasiBarang = new LatLng(Double.parseDouble(barangSewa.getLat()), Double.parseDouble(barangSewa.getLng()));
                    mMap.addMarker(new MarkerOptions().position(lokasiBarang).title(barangSewa.getNama()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiBarang));
                }
            }
        } else {
            // LatLng Monumen Nasional as Default
//            LatLng lokasiBarang = new LatLng(-6.175206, 106.827131);
//            mMap.addMarker(new MarkerOptions().position(lokasiBarang).title("Jual Baju Second").icon(icon));

//            mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiBarang));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                fab.setImageResource(android.R.drawable.ic_input_add);
                permintaanBarang = null;
                barangSewa = null;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "mMapBarangSewa Clicked");

                checkTitle(marker.getTitle());

//                String title = marker.getTitle();
//
//                for (BarangSewa bs : barangSewaArrayList) {
//                    if (title.equals(bs.getNama())) {
//                        barangSewa = bs;
//                    }
//                }
//
//                if (barangSewa != null) {
//                    fab.setImageResource(R.drawable.ic_action_view);
//                }
                return false;
            }
        });
    }

    private void updatePermintaanBarangLokasi() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        if (permintaanBarangArrayList.size() != 0) {
            if (mMap != null) {
                for (PermintaanBarang permintaanBarang : permintaanBarangArrayList) {
                    LatLng lokasiBarang = new LatLng(Double.parseDouble(permintaanBarang.getLat()), Double.parseDouble(permintaanBarang.getLng()));
                    mMap.addMarker(new MarkerOptions().position(lokasiBarang).title(permintaanBarang.getNama()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiBarang));
                }
            }
        } else {
            // LatLng Monumen Nasional as Default
//            LatLng lokasiBarang = new LatLng(-6.175206, 106.827131);
//            mMap.addMarker(new MarkerOptions().position(lokasiBarang).title("Jual Baju Second").icon(icon));

//            mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiBarang));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                fab.setImageResource(android.R.drawable.ic_input_add);
                permintaanBarang = null;
                barangSewa = null;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "mMapPermintaanBarang Clicked");
                checkTitle(marker.getTitle());

//                String title = marker.getTitle();
//
//                for (PermintaanBarang pb : permintaanBarangArrayList) {
//                    if (title.equals(pb.getNama())) {
//                        permintaanBarang = pb;
//
//                        break;
//                    }
//                }
//
//                if (permintaanBarang != null) {
//                    fab.setImageResource(R.drawable.ic_action_edit);
//                }

                return false;
            }
        });
    }

    private void checkTitle(String title) {
        for (PermintaanBarang pb : permintaanBarangArrayList) {
            if (title.equals(pb.getNama())) {
//                        title = permintaanBarang.getNama();

                permintaanBarang = pb;
                break;
            } else {
                permintaanBarang = null;
            }
        }

        for (BarangSewa bs : barangSewaArrayList) {
            if (title.equals(bs.getNama())) {
//                        title = permintaanBarang.getNama();

                barangSewa = bs;
                break;
            } else {
                barangSewa = null;
            }
        }

        if (permintaanBarang != null) {
            fab.setImageResource(R.drawable.ic_action_edit);
        } else {
            if (barangSewa != null) {
                fab.setImageResource(R.drawable.ic_action_view);
            }
        }
    }

    private void setCenterPoint() {
        if (mMap != null) {
            if (lat != 0.0 && lng != 0.0) {
                LatLng latLng = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );
            }
        }
    }
}