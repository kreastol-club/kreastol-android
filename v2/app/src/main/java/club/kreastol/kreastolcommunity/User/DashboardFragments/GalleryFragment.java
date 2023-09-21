package club.kreastol.kreastolcommunity.User.DashboardFragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import club.kreastol.kreastolcommunity.HelperClasses.Adapters.GalleryAdapter;
import club.kreastol.kreastolcommunity.HelperClasses.Models.GalleryItem;
import club.kreastol.kreastolcommunity.R;

public class GalleryFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    //GalleryAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    GalleryItem item;
    List<GalleryItem> imgList = new ArrayList<>();

    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.gallery_recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fetchImages();
    }

    private final String IMG_GET_URL = "https://community.kreastol.club/android/images.php";
    private final String IMG_URL = "https://community.kreastol.club/";

    public void fetchImages(){
        StringRequest request = new StringRequest(Request.Method.POST, IMG_GET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("Gallery:","Response Arrived");
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")){
                        Log.d("Gallery:","Response Success");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("id");
                            String imgUrl = object.getString("image_dir");
                            String title = object.getString("title");

                            String url = IMG_URL + imgUrl;
                            //Log.d("Gallery Img:",item.getId());
                            //Log.d("Gallery Img:",item.getImgUrl());
                            //Log.d("Gallery Img:",item.getTitle());
                            item = new GalleryItem(id, url, title);
                            imgList.add(item);
                        }
                        adapter = new GalleryAdapter(mContext, imgList);
                        recyclerView.setAdapter(adapter);
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);
    }
}