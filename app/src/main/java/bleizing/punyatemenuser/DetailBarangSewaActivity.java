package bleizing.punyatemenuser;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetailBarangSewaActivity extends Activity {
    private static final String TAG = "DetailBarangSewa";

    private BarangSewa barangSewa;

    private EditText editNama, editDeskripsi, editLokasi;
    private TextView tvTglMulai, tvTglBerakhir;
    private NetworkImageView networkImageView;

    private String tglMulai, tglBerakhir;
    private String fotoUrl;

    private String addressLocation;

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang_sewa);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (getIntent().getExtras() !=  null) {
            int barang_sewa_id = getIntent().getExtras().getInt("barang_sewa_id");

            if (Model.getBarangSewaArrayList() != null || Model.getBarangSewaArrayList().size() != 0) {
                for (BarangSewa bs : Model.getBarangSewaArrayList()) {
                    if (barang_sewa_id == bs.getId()) {
                        barangSewa = bs;
                        break;
                    }
                }
            }
        }

        imageLoader = VolleyImageRequest.getInstance(this).getImageLoader();

        addressLocation = "";

        editNama = (EditText) findViewById(R.id.edNama);
        editDeskripsi = (EditText) findViewById(R.id.edDeskripsi);
        editLokasi = (EditText) findViewById(R.id.editLokasi);
        tvTglMulai = (TextView) findViewById(R.id.tvTglMulai);
        tvTglBerakhir = (TextView) findViewById(R.id.tvTglBerakhir);
        networkImageView = (NetworkImageView) findViewById(R.id.img_foto_barang);

        fotoUrl = "http://www.pt.himsigalaksi.com/" + barangSewa.getFoto_barang();

        Button btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String tglMulaiArr[] = barangSewa.getTgl_mulai().split("-");
        tglMulai = tglMulaiArr[0];

        String tglBerakhirArr[] = barangSewa.getTgl_berakhir().split("-");
        tglBerakhir = tglBerakhirArr[0];

        getAddress();

        setData();
    }

    private void getAddress() {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(barangSewa.getLat()), Double.parseDouble(barangSewa.getLng()), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName()).append("\n");
                result.append(address.getAddressLine(0)).append("\n");
                result.append(address.getPostalCode()).append("\n");
                result.append(address.getSubAdminArea()).append("\n");
                result.append(address.getAdminArea()).append("\n");
                result.append(address.getLatitude()).append("\n");
                result.append(address.getLongitude()).append("\n");
                result.append(address.getPhone()).append("\n");
                result.append(address.getPremises()).append("\n");
                result.append(address.getSubLocality()).append("\n");
                result.append(address.getThoroughfare()).append("\n");
                result.append(address.getSubThoroughfare()).append("\n");
                result.append(address.getUrl()).append("\n");
                result.append(address.getMaxAddressLineIndex()).append("\n");

                Log.w(TAG, result.toString());
//                    tv_address.setText(address.getAddressLine(0));
                    addressLocation = address.getAddressLine(0);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void setData() {
        editNama.setText(barangSewa.getNama());
        editDeskripsi.setText(barangSewa.getDeskripsi());
        editLokasi.setText(addressLocation);
        tvTglMulai.setText(tglMulai);
        tvTglBerakhir.setText(tglBerakhir);

        imageLoader.get(fotoUrl, ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        networkImageView.setImageUrl(fotoUrl, imageLoader);

        Log.d(TAG, "fotoUrl = " + fotoUrl);
    }
}