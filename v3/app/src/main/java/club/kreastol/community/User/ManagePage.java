package club.kreastol.community.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import club.kreastol.community.Common.Adapters.HereUserAdapter;
import club.kreastol.community.Common.Items.UserIsHereItem;
import club.kreastol.community.Common.SpalshScreen;
import club.kreastol.community.R;
import club.kreastol.community.User.Dialogs.AddNewEvent;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.Volley.VolleyResponse;


public class ManagePage extends AppCompatActivity {

    VolleyResponse volley = new VolleyResponse();
    // implementation of ResponseListener
    public void gotResponse(JSONObject jo) {
        // eventually do more with this data
        Log.d("Response: ", jo.toString());

        try {
            if(jo.getString("result").equals("no"))
            {
                eventName.setText(getString(R.string.manage_event_name_empty));
                eventTime.setVisibility(View.GONE);
                eventAge.setVisibility(View.GONE);
                return;
            }
            String name = jo.getString("event_name");
            String startTime = jo.getString("starts_at");
            String endTime = jo.getString("ends_at");
            String fromAge = jo.getString("age_from");
            String toAge = jo.getString("age_to");
            Log.d("EventId", String.valueOf(jo.getInt("id")));
            eventId = jo.getInt("id");
            eventName.setText(getString(R.string.manage_event_name, name));
            eventTime.setText(getString(R.string.manage_event_time, startTime, endTime));
            eventAge.setText(getString(R.string.manage_event_age_range, fromAge, toAge));
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private static final String URL_DATA_RECEIVE = "https://community.kreastol.club/android/list_users_event.php";
    private static final String URL_DATA_SEND = "https://community.kreastol.club/android/list_users_event.php";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    final String TAG = this.getClass().getSimpleName();
    private List<UserIsHereItem> userList = new ArrayList<>();

    LinkHolder links = new LinkHolder();

    Button eventAddNew, eventRecordNew, submitUsers;
    Context mContext = this;

    TextView eventName, eventAge, eventTime;
    public int eventId;

    private JSONObject eventDetails;
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_page);

        eventAddNew = findViewById(R.id.add_new_event);
        eventRecordNew = findViewById(R.id.record_new_event);
        eventName = findViewById(R.id.event_name);
        eventAge = findViewById(R.id.event_age);
        eventTime = findViewById(R.id.event_time);

        recyclerView = findViewById(R.id.event_users);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        submitUsers = findViewById(R.id.users_submit);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, links.GET_CURRENT_EVENT, null, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        gotResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mContext, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


        eventAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewEvent newEventDialog = new AddNewEvent();
                newEventDialog.show(getSupportFragmentManager(), "AddNewEvent");
                Log.d("Dialog:","AddNewEWvent");
            }
        });

        eventRecordNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUserData(eventId);
            }
        });

        submitUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBackUsers();
            }
        });
    }


    private void loadUserData(int event) {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL_DATA_RECEIVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response Arrived");
                progressDialog.dismiss();
                Log.d("Response: ", response);

                try {
                    userList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray array = jsonObject.getJSONArray("users");
                    Log.d(TAG, "Json array length: " + array.length());
                    for (int i = 0; i < array.length(); i++){

                        JSONObject jo = array.getJSONObject(i);

                        UserIsHereItem user = new UserIsHereItem(jo.getString("first_name"), jo.getString("last_name"),
                                jo.getInt("id"), false);
                        userList.add(user);
                        Log.d(TAG, i + "th post");
                    }
                    adapter = new HereUserAdapter(userList, mContext);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("event_id", String.valueOf(event));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void sendBackUsers(){

        JSONArray userAttendace = new JSONArray();
        JSONObject jsonObject;
        for(int i = 0; i < userList.size(); i++){
            jsonObject = new JSONObject();
            try {
                jsonObject.put("id", userList.get(i).getUserId());
                jsonObject.put("is_here", userList.get(i).isUserIsHere());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            userAttendace.put(jsonObject);
        }
        JSONObject usersAttended = new JSONObject();
        try {
            usersAttended.put("users_list", userAttendace);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("User attendence: ", usersAttended.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,  links.MANAGE_POINTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Add_Points", "Response Arrived");
                Log.d("Add_Points: ", response);
                userList = new ArrayList<>();
                adapter = new HereUserAdapter(userList, mContext);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("json", usersAttended.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


}