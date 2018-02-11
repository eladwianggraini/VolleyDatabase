package com.example.eladw.volley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ImageView imghp;
    private TextView txtmerk, txttipe, txtketerangan;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    public String baseUrl = "http://192.168.8.104";

    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = baseUrl + "/app_blogvolley/getdata.php";

        imghp = (ImageView)findViewById(R.id.imghp);
        txtmerk = (TextView)findViewById(R.id.txtmerk);
        txttipe = (TextView)findViewById(R.id.txttipe);
        txtketerangan = (TextView)findViewById(R.id.txtketerangan);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        list_data = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("handphone");
                    for (int a = 0; a < jsonArray.length(); a ++){
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map  = new HashMap<String, String>();
                        map.put("id", json.getString("idhp"));
                        map.put("merk", json.getString("merk"));
                        map.put("tipe", json.getString("tipe"));
                        map.put("gambar", json.getString("gambar"));
                        map.put("keterangan", json.getString("keterangan"));
                        list_data.add(map);
                    }
                    Glide.with(getApplicationContext())
                            .load(baseUrl + "/app_blogvolley/img/" + list_data.get(0).get("gambar"))
                            .crossFade()
                            .placeholder(R.mipmap.ic_launcher)
                            .into(imghp);
                    txtmerk.setText(list_data.get(0).get("merk"));
                    txttipe.setText(list_data.get(0).get("tipe"));
                    txtketerangan.setText(list_data.get(0).get("keterangan"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}
