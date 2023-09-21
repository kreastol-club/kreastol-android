package club.kreastol.community.User.Dialogs;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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

import club.kreastol.community.R;
import club.kreastol.community.User.DashboardFragments.PostsFragment;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;
import club.kreastol.community.util.UserProfilSession;


public class PostDialogFragment extends DialogFragment {

    LinkHolder links = new LinkHolder();


    private TextInputLayout title, body, tags;
    private TextView mActionPost, mActionCancel;
    private UserProfilSession userProfile;
    private AppRunSession appRun;

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
    PostDialogFragment dialog;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_post, container, false);

        userProfile = new UserProfilSession(mContext);
        dialog = this;
        title = view.findViewById(R.id.post_dialog_title);
        body = view.findViewById(R.id.post_dialog_body);
        dialog.setCancelable(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);



        categorySpinner = view.findViewById(R.id.category_spinner);

        mActionCancel = view.findViewById(R.id.cancel_post);
        mActionPost = view.findViewById(R.id.send_post);

        getCategory();

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

                StringRequest request = new StringRequest(Request.Method.POST, links.ADD_POST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        if(response.equals("yes")) {
                            Log.d("Response","It went through");
                            Toast.makeText(mContext, getResources().getString(R.string.add_post_dialog_post_done),Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(mContext, getResources().getString(R.string.add_post_dialog_post_error),Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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
                        params.put("username", userProfile.getUsername());
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, links.FETCH_CATEGORY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String category = jsonObject.optString("category_name");
                        categories.add(category);
                        categoryAdapter = new ArrayAdapter<>(mContext, R.layout.item_category_spinner, categories);
                        categoryAdapter.setDropDownViewResource(R.layout.item_category_spinner);
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
