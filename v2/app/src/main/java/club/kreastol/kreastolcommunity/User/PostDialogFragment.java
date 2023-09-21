package club.kreastol.kreastolcommunity.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import club.kreastol.kreastolcommunity.HelperClasses.util.SessionManager;
import club.kreastol.kreastolcommunity.R;


public class PostDialogFragment extends DialogFragment /*implements AdapterView.OnItemSelectedListener*/ {



    private TextInputLayout title, body, tags;
    private TextView mActionPost, mActionCancel;
    private SessionManager session;

    private Spinner categorySpinner;
    ArrayList<String> categories = new ArrayList <String>();
    ArrayAdapter<String> categoryAdapter;
    RequestQueue requestQueue;

    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_post_dialog, container, false);

        session = new SessionManager(mContext);

        title = view.findViewById(R.id.post_dialog_title);
        body = view.findViewById(R.id.post_dialog_body);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        categorySpinner = view.findViewById(R.id.category_spinner);

        mActionCancel = view.findViewById(R.id.cancel_post);
        mActionPost = view.findViewById(R.id.send_post);

        getCategory();

      /*  String url = "https://community.kreastol.club/android/category.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String category = jsonObject.optString("category_name");
                        categories.add(category);
                        categoryAdapter = new ArrayAdapter<>(mContext, R.layout.category_spinner_item, categories);
                        categoryAdapter.setDropDownViewResource(R.layout.category_spinner_item);
                        categorySpinner.setAdapter(categoryAdapter);
                    }
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        //categorySpinner.setOnItemSelectedListener(this);*/

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        mActionPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateBody() | !validateTitle()){
                    return;
                }

                Log.d("PostDialog", "Posted");
                String titleS = title.getEditText().getText().toString();
                String bodyS = body.getEditText().getText().toString();
                String categoryS = categorySpinner.getSelectedItem().toString();
                Log.d("Category", categoryS);

                StringRequest request = new StringRequest(Request.Method.POST, "https://community.kreastol.club/android/post_upload.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        if(response.equals("yes")) {
                            Log.d("Response","It went through");
                            getDialog().dismiss();
                        }
                        else{
                            Toast.makeText(mContext, getResources().getString(R.string.add_post_dialog_post_error),Toast.LENGTH_SHORT);
                            getDialog().dismiss();
                        }
                        Log.d("Response", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> params = new HashMap<>();
                        params.put("title", titleS);
                        params.put("category", categoryS);
                        params.put("body", bodyS);
                        params.put("username", session.getUsername());
                        return params;
                    }
                };

                Volley.newRequestQueue(mContext).add(request);
            }
        });

        return view;
    }



    private boolean validateTitle() {
        String val = title.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            title.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else {
            title.setError(null);
            title.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateBody() {
        String val = body.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            body.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else {
            body.setError(null);
            body.setErrorEnabled(false);
            return true;
        }
    }

    private void getCategory(){
        requestQueue = Volley.newRequestQueue(mContext);

        String url = "https://community.kreastol.club/android/category.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String category = jsonObject.optString("category_name");
                        categories.add(category);
                        categoryAdapter = new ArrayAdapter<>(mContext, R.layout.category_spinner_item, categories);
                        categoryAdapter.setDropDownViewResource(R.layout.category_spinner_item);
                        categorySpinner.setAdapter(categoryAdapter);
                    }
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if(parent.getId() == R.id.category_spinner){
//
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
