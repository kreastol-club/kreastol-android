package club.kreastol.community.User.DashboardFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import club.kreastol.community.Common.Adapters.HomeContentAdapter;
import club.kreastol.community.Common.Adapters.PostAdapter;
import club.kreastol.community.Common.Items.GalleryItem;
import club.kreastol.community.Common.Items.PostItem;
import club.kreastol.community.R;
import club.kreastol.community.User.Dialogs.ImageDialogFragment;
import club.kreastol.community.User.Dialogs.PostDialogFragment;
import club.kreastol.community.User.UserDashboard;
import club.kreastol.community.Volley.LinkHolder;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class HomeFragment extends Fragment {


     public  interface ResponseListener {
        public void gotResponse(JSONObject jo);
    }

    private Context mContext;

    HomeContentAdapter adapter;
    private final String IMG_URL = "https://community.kreastol.club/gallery/";

    // implementation of ResponseListener
    public void gotResponse(JSONObject jo) {
        // eventually do more with this data
        Log.d("Response-links: ", jo.toString());
        try {
            ArrayList<Object> items = new ArrayList<>();

            JSONArray array = jo.getJSONArray("items");
            Log.d("", "Json array length: " + array.length());
            for (int i = 0; i < array.length(); i++){

                JSONObject obj = array.getJSONObject(i);

                switch (obj.getString("type")){
                    case "post":
                        PostItem post = new PostItem(obj.getString("title"),
                                obj.getString("body"),
                                obj.getString("category"),
                                obj.getString("date"),
                                obj.getString("username"));
                        items.add(post);
                        break;
                    case "image":
                        GalleryItem image = new GalleryItem(
                                obj.getString("id"),
                                IMG_URL + obj.getString("img_dir"),
                                obj.getString("title")
                        );
                        items.add(image);
                        break;
                }
            }
            adapter = new HomeContentAdapter(items, mContext);
            recycler.setAdapter(adapter);

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean isClicked = false;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;

    private LinkHolder links = new LinkHolder();
//    FloatingActionButton addButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rotateOpen = AnimationUtils.loadAnimation(mContext, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(mContext, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(mContext, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(mContext, R.anim.to_bottom_anim);
        recycler  = view.findViewById(R.id.home_contents);
        layoutManager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(layoutManager);

          JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, links.FETCH_FOR_HOME, null, new Response.Listener<JSONObject>(){
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

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjectRequest);

        /*addButton = (FloatingActionButton) view.findViewById(R.id.add_post_and_image_floating_button);

        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onAddButtonClicked();
            }
        });*/
    }

/*    private void onAddButtonClicked() {
        setAnimation(isClicked);
        setVisibility(isClicked);
        setClickable(isClicked);
        if(!isClicked) isClicked = true; else isClicked = false;
    }

    private void setVisibility(boolean isClicked) {
        if(!isClicked){
            postButton.setVisibility(VISIBLE);
            imageButton.setVisibility(VISIBLE);
        }else{
            postButton.setVisibility(INVISIBLE);
            imageButton.setVisibility(INVISIBLE);
        }

    }

    private void setAnimation(boolean isClicked) {
        if(!isClicked){
            postButton.startAnimation(fromBottom);
            imageButton.startAnimation(fromBottom);
            addButton.startAnimation(rotateOpen);
        }else{
            postButton.startAnimation(toBottom);
            imageButton.startAnimation(toBottom);
            addButton.startAnimation(rotateClose);
        }
    }
    private void setClickable(boolean clicked){
        if(clicked){
            imageButton.setClickable(false);
            postButton.setClickable(false);
            imageButton.setFocusable(false);
            postButton.setFocusable(false);
        }
        else {
            imageButton.setClickable(true);
            postButton.setClickable(true);
            imageButton.setFocusable(true);
            postButton.setFocusable(true);
        }
    }*/


}