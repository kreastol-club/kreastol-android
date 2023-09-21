package club.kreastol.community.Volley;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import club.kreastol.community.R;

public class VolleyResponse {

    public JSONObject responseObj;

    public void gotResponse(JSONObject jo) {
        // eventually do more with this data
        //Log.d("Response: ", jo.toString());
        responseObj = jo;
    }

    public JSONObject setResponseJsonObject(JSONObject jo) {
        // eventually do more with this data
        Log.d("Response: ", jo.toString());
        return jo;
    }

    public void jsonRequest(Context context, String url) {


    }
}
