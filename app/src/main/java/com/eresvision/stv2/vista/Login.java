package com.eresvision.stv2.vista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eresvision.stv2.lcchat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText password, username;
    private Button sign_in_but;
    private TextView registrarse_link;
    private RequestQueue requestQueue;
    private static final String URL = "http://todoit.com.ve/dbma/index.php";
    private StringRequest request;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password = (EditText) findViewById(R.id.editLoginPassword);
        username = (EditText) findViewById(R.id.editLoginUsername);
        sign_in_but = (Button) findViewById(R.id.buttonLogin);
        registrarse_link = (TextView) findViewById(R.id.textViewRegistrarse);

        requestQueue = Volley.newRequestQueue(this);

        sign_in_but.setOnClickListener(this);
        registrarse_link.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:

                authenticate();

                break;

            case R.id.textViewRegistrarse:

                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);


                break;


        }
    }

    private void authenticate(){
        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "Bienvenido " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else if (jsonObject.names().get(0).equals("error")) {
                        Toast.makeText(getApplicationContext(), "Error de registro " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error de servidor ", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de Conexión o Sin Internet", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("username", username.getText().toString());
                hashMap.put("password", password.getText().toString());

                return hashMap;
            }
        };

        requestQueue.add(request);


    }

}



