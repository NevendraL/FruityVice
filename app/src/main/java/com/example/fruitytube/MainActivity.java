package com.example.fruitytube;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private String url = "https://www.fruityvice.com/api/fruit/";
    private String imageUrl = "http://fruityvice.com/images/";
    private  ImageView errorImage;
    private ImageView fruitImage;
    private Button searchFruitButton;
    private EditText fruitNameInput;
    private TextView fruitNameTv, caloriesTv, proteinTv, carbsTv, fatTv;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorImage = findViewById(R.id.errorImage);
        fruitNameTv = findViewById(R.id.fruitNameTv);
        caloriesTv = findViewById(R.id.caloriesTv);
        proteinTv = findViewById(R.id.proteinTv);
        carbsTv = findViewById(R.id.carbTv);
        fatTv = findViewById(R.id.fatTv);
        fruitImage = findViewById(R.id.fruitSplashImage);
        searchFruitButton = findViewById(R.id.searchFruitButton);
        fruitNameInput = findViewById(R.id.fruitNameInput);
        requestQueue = Volley.newRequestQueue(this);
        handleSSLHandshake();


        searchFruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);

                updateUrl();


            }
        });


    }

    //Downloads json data based on a url
    public void downloadJson(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                String fruitName = response.getString("name");
                                JSONObject jsonObject = response.getJSONObject("nutritions");

                                double calories = jsonObject.getDouble("calories");
                                double protein = jsonObject.getDouble("protein");
                                double carbs = jsonObject.getDouble("carbohydrates");
                                double fat = jsonObject.getDouble("fat");

                                updateUi(fruitName, calories, protein, carbs, fat);


                            } catch (JSONException e) {

                            }

                        }
                    }
                }, new Response.ErrorListener() {

                    //Handles the error if the app fails to load data due to no wifi, or if the fruit is not in the database
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       handleError();


                    }
                });
        requestQueue.add(jsonObjectRequest);


    }


    public void updateUi(String fruitName, double calories, double protein, double carbs, double fat) {
        //creates a FruitDataModel based on parameters given from update UI method
        FruitDataModel fruitDataModel = new FruitDataModel(fruitName, calories, protein, carbs, fat);
        fruitNameTv.setText(fruitDataModel.getFruitName());
        caloriesTv.setText("Calories:" + fruitDataModel.getCalories() + "");
        proteinTv.setText("Protein:" + fruitDataModel.getProtein() + "");
        carbsTv.setText("Carbs:" + fruitDataModel.getCarbs() + "");
        fatTv.setText("Fat:" + fruitDataModel.getFat() + "");
        progressDialog.dismiss();



    }

    //loads the fruit image into image view
    public void loadImages(String imageUrl) {

        Glide.with(this)
                .load(imageUrl) // Remote URL of image.
                .into(fruitImage); //ImageView to set.






    }

    //updates the url based on users search criteria
    public void updateUrl() {
            String finalUrl = url + fruitNameInput.getText().toString().toLowerCase();
            downloadJson(finalUrl);
            String finalImage = imageUrl + fruitNameInput.getText().toString().toLowerCase() + ".png";
            loadImages(finalImage);
            fruitNameInput.setText("");
    }
    //Handles error if nothing is displayed
    public void handleError(){
        progressDialog.dismiss();
        fruitNameTv.setText("No data available");
        caloriesTv.setText("No data available");
        proteinTv.setText("No data available");
        carbsTv.setText("No data available");
        fatTv.setText("No data available");
    }


    /**
     * Enables https connections
     */
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

}
