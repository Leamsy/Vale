package com.bluedot.bluedot_vale;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class AgregarPeticion extends AppCompatActivity {


    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_peticion);
    }

    public void volver(android.view.View V){
        finish();
    }

    public void enviar(android.view.View V){

        Toast.makeText(this, "La sugerencia ha sido enviada.", Toast.LENGTH_SHORT).show();

        /*
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://vale-web.000webhostapp.com/wp-json/vale/v1/peticion/nueva";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("titulo", "Peti");
            jsonBody.put("descripcion", "Cion");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        Log.i("asdf",requestBody);
        StringRequest objectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("asdf",":)");
                Log.i("asdf",response);
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asdf","mal");
                Log.i("asdf",error.getMessage());
            }}){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = Global.user + ":" + Global.pass;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "multipart/form-data");
                headers.put("Authorization", auth);
                Log.i("asdf", headers.toString());
                return headers;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(objectRequest);*/

        finish();
    }
}
