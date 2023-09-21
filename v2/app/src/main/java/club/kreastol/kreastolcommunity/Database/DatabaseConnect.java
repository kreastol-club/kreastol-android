package club.kreastol.kreastolcommunity.Database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class DatabaseConnect {
    Context context;
    RequestQueue queue;

    public DatabaseConnect(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void get(String[] data, final VolleyCallBack callBack) {
        String url = "https://community.kreastol.club/android/getUserData.php";



        // executing request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("get", "Response arrived: " + response.toString());
                        // calling callback function
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void get(final VolleyCallBack callBack) {
        get( null, callBack);
    }


}