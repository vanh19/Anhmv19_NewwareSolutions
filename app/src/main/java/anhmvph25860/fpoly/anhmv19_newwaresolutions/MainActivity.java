package anhmvph25860.fpoly.anhmv19_newwaresolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import       android.widget.Toast;
import     androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import      com.android.volley.Request;
import      com.android.volley.RequestQueue;
import      com.android.volley.Response;
import      com.android.volley.VolleyError;
import      com.android.volley.toolbox.JsonObjectRequest;
import      com.android.volley.toolbox.Volley;
import      org.json.JSONArray;
import      org.json.JSONException;
import      org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtAddress;
    private TextView tvResult;
    private Button btnSearch;
    private Button btnGGmap;
    private RecyclerView recyclerView;
    private  AddressAdapter adapter;
    private List<String> addressList;
    private List<Double> latList;
    private List<Double> lngList;
    private RequestQueue requestQueue;

    private final String API_KEy = "h91KH-ca6Csal4pxuqwa_B20TDxpF7cWRmOXHKeQgIc";
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAddress = findViewById(R.id.edtAddress);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerView);

        requestQueue = Volley.newRequestQueue(this);
        addressList = new ArrayList<>();
        latList = new ArrayList<>();
        lngList = new ArrayList<>();
        adapter = new AddressAdapter(this, addressList, latList, lngList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = edtAddress.getText().toString();
                if (!address.isEmpty()) {
                    fetchCoordinates(address);
                } else {
                    Toast.makeText(MainActivity.this, "vui long nhap dia chi", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void fetchCoordinates(String address) {
        //call api
        String url = "https://geocode.search.hereapi.com/v1/geocode?q=" + address + "&apiKey=" + API_KEy;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            if (items.length() > 0) {
                                JSONObject position = items.getJSONObject(0).getJSONObject("position");
                                double lat = position.getDouble("lat");
                                double lng = position.getDouble("lng");
                                JSONObject addressDetails = items.getJSONObject(0).getJSONObject("address");
                                String label = addressDetails.getString("label");

                                // Thêm địa chỉ vào danh sách và cập nhật RecyclerView
                                addressList.add(label);
                                latList.add(lat);
                                lngList.add(lng);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MainActivity.this, "Không thể tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Lỗi khi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

}