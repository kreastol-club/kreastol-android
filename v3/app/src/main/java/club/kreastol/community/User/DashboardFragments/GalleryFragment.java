package club.kreastol.community.User.DashboardFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import club.kreastol.community.Common.Adapters.GalleryAdapter;
import club.kreastol.community.Common.Items.GalleryItem;
import club.kreastol.community.R;
import club.kreastol.community.User.Dialogs.ImageDialogFragment;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.util.UserProfilSession;


public class GalleryFragment extends Fragment {

    LinkHolder links = new LinkHolder();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    //GalleryAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    GalleryItem item;
    List<GalleryItem> imgList = new ArrayList<>();

    UserProfilSession userProfile;

    FloatingActionButton imageButton;
    SwipeRefreshLayout refresh;

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
        return inflater.inflate(R.layout.fragment_dashboard_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.gallery_recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        userProfile = new UserProfilSession(mContext);
        imageButton = (FloatingActionButton) view.findViewById(R.id.add_image_floating_button);
        refresh = view.findViewById(R.id.img_refresh);
        if(!userProfile.isLoggedIn())
            imageButton.setVisibility(View.GONE);

        imageButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Image Button");
                ImageDialogFragment dialog = new ImageDialogFragment();
                FragmentManager fm = getFragmentManager();
                assert fm != null;
                dialog.show(fm,"ImageDialog");

                fm.executePendingTransactions();
                Objects.requireNonNull(dialog.getDialog()).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        fetchImages();
                    }
                });
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchImages();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
        fetchImages();
    }

    private final String IMG_URL = "https://community.kreastol.club/";

    public void fetchImages(){
        StringRequest request = new StringRequest(Request.Method.POST, links.FETCH_IMAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("Gallery:","Response Arrived");
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")){
                        Log.d("Gallery:","Response Success");
                        imgList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("id");
                            String imgUrl = object.getString("image_dir");
                            String title = object.getString("title");

                            String url = IMG_URL + imgUrl;

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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", userProfile.getUsername());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);
    }
}