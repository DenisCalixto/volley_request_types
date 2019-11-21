package edu.wmdd.volleyexamples;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btString;
    Button btObject;
    Button btArray;
    TextView textResult;
    private List<Property> propertyList;
    private RecyclerView.Adapter adapter;
    private RecyclerView mList;
    EditText addressEdit;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzQzNzA5NTksInVzZXJfaWQiOjEsImp0aSI6IjRiMjFhMTMyNTQxMzQ4OTA4ODY3MDIzYjkwN2Y3MTVhIiwidG9rZW5fdHlwZSI6ImFjY2VzcyJ9.XG8BiMh7djXjLb42yve1MW_txiFiXdOY4vos2b79e3I";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResult = findViewById(R.id.textResult);
        mList = findViewById(R.id.property_list);
        addressEdit = findViewById(R.id.addressEdit);
        propertyList = new ArrayList<>();

        adapter = new PropertyAdapter(getApplicationContext(), propertyList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        Button btString = findViewById(R.id.btString);
        btString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObject();
            }
        });

        Button btObject = findViewById(R.id.btObject);
        btObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchObject();
            }
        });

        Button btArray = findViewById(R.id.btArray);
        btArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchArray();
            }
        });

    }
    private void saveObject() {

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("address", addressEdit.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Integer method;
        String url;
        method = Request.Method.POST;
        url = "http://10.0.2.2:8000/api/property/";

        // Volley post request with parameters
        JsonObjectRequest request = new JsonObjectRequest(method, url, postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = response;
                        propertyList.clear();
                        Toast.makeText(MainActivity.this, "Object saved", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", error.toString());
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Authorization", "Bearer "+ token);
                return headers;
            };
        };

        // Volley request policy, only one time request to avoid duplicate transaction
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchObject() {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:8000/api/property/9", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("Volley", response.toString());
                        textResult.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("Volley", error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }

    private void fetchArray() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://10.0.2.2:8000/api/property/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Property property = new Property();
                        property.setAddress(jsonObject.getString("address"));

                        propertyList.add(property);
                    } catch (JSONException e) {
                        Log.e("Volley", e.toString());
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}
