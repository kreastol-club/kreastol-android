package club.kreastol.kreastolcommunity.User;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import club.kreastol.kreastolcommunity.HelperClasses.util.SessionManager;
import club.kreastol.kreastolcommunity.R;

import static android.app.Activity.RESULT_OK;

public class
ImageDialogFragment extends DialogFragment {

    private Button uploadBtn, selectBtn;
    private TextView mActionCancel;
    private TextInputLayout title, group;
    private LinearLayout spinnerLayout;
    private SessionManager session;
    RequestQueue requestQueue;

    private Spinner categorySpinner;
    ArrayList<String> categories = new ArrayList <String>();
    ArrayAdapter<String> categoryAdapter;
    private ImageView mImageView;
    private Bitmap bitmap;
    String encodedImage;

    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri filepath = data.getData();
            try {
                InputStream inputStream = mContext.getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                mImageView.setImageBitmap(bitmap);
                setVisible();
                getCategory();
                imageStore(bitmap);

            } catch (FileNotFoundException e) {
                Log.d("InputStream",e.toString());
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageStore(Bitmap bitmap) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

    byte[] imageBytes = stream.toByteArray();

    encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_image_dialog, container, false);

        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        session = new SessionManager(mContext);

        uploadBtn = (Button) view.findViewById(R.id.image_dialog_upload);
        selectBtn = (Button) view.findViewById(R.id.image_dialog_select);
        mActionCancel = (TextView) view.findViewById(R.id.image_dialog_cancel);
        mImageView = (ImageView) view.findViewById(R.id.image_dialog_preview);

        spinnerLayout = (LinearLayout) view.findViewById(R.id.image_dialog_spinner);
        title = (TextInputLayout) view.findViewById(R.id.image_dialog_title);
        group = (TextInputLayout) view.findViewById(R.id.image_dialog_image_group);

        categorySpinner = view.findViewById(R.id.category_spinner);

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(mContext);

                if(!validateTitle()){
                    return;
                }

                Log.d("PostDialog", "Posted");
                String titleS = title.getEditText().getText().toString();
                String groupS = group.getEditText().getText().toString();
                String categoryS = categorySpinner.getSelectedItem().toString();
                Log.d("Category", categoryS);

                StringRequest request = new StringRequest(Request.Method.POST, "https://community.kreastol.club/android/img_upload.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("IMGResponse", response);
                        Objects.requireNonNull(getDialog()).dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("image", encodedImage);
                        params.put("title", titleS);
                        if(checkGroup()) params.put("gruop", groupS);
                        params.put("category", categoryS);
                        params.put("username", session.getUsername());
                        return params;

                    }
                };

                requestQueue.add(request);

            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(mContext)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.add_image_select_image)), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        return view;
    }

    private void getCategory(){

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void setVisible(){
        uploadBtn.setVisibility(View.VISIBLE);
        spinnerLayout.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        group.setVisibility(View.VISIBLE);
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
    private boolean checkGroup() {
        String val = group.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            //title.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else {
            /*title.setError(null);
            title.setErrorEnabled(false);*/
            return true;
        }
    }

}
