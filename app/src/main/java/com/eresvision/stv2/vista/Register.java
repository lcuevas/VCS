package com.eresvision.stv2.vista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.EmailValidator;
import com.andreabaccega.formedittextvalidator.OrValidator;
import com.andreabaccega.widget.FormEditText;
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

public class Register extends AppCompatActivity {

    FormEditText emailv;
    private EditText email, password, username;
    private Button sign_in_register;
    private RequestQueue requestQueue;
    private static final String URL = "http://todoit.com.ve/dbma/index.php";
    private StringRequest request;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        password = (EditText) findViewById(R.id.editTextPassword);
        username = (EditText) findViewById(R.id.editTextUsername);
        sign_in_register = (Button) findViewById(R.id.buttonRegister);
        emailv = (FormEditText) findViewById(R.id.editTextEmail);
        Log.d("AVISO", "Inicializadas las variables");

        EditTextValidator();


    }

    public void EditTextValidator() {
// set the validation rule on the input filed for email
        emailv.addValidator(
                new OrValidator(
                        "This is not a valid email",
                        new EmailValidator(null)
                )
        );


    }

    public void onClickValidate(final View v) {

        // set all field in the array list
        FormEditText[] allFields = {emailv};

        boolean allValid = true;
        for (FormEditText field : allFields) {

            // validate the input filed through testValidity method
            allValid = field.testValidity() && allValid;
        }

        if (allValid) {

            Toast.makeText(this, "valid", Toast.LENGTH_LONG).show();

            Log.d("AVISO", "Es valido el email");


            Log.d("AVISO", "Iniciando el Volley");
            requestQueue = Volley.newRequestQueue(this);


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
                    hashMap.put("email", emailv.getText().toString());
                    hashMap.put("password", password.getText().toString());
                    Log.d("AVISO", "Enviando datos");
                    return hashMap;

                }
            };

            requestQueue.add(request);
        } else {
            // EditText are going to appear with an exclamation mark and an explicative message.
            Log.d("AVISO", "No es valido el email");
        }


    }
}