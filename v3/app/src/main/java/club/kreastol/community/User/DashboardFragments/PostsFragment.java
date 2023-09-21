package club.kreastol.community.User.DashboardFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import club.kreastol.community.Common.Adapters.PostAdapter;
import club.kreastol.community.Common.Items.PostItem;
import club.kreastol.community.R;
import club.kreastol.community.User.Dialogs.PostDialogFragment;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.util.UserProfilSession;

public class PostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    LinkHolder links = new LinkHolder();

    private RecyclerView.Adapter adapter;
    final String TAG = this.getClass().getSimpleName();
    private List<PostItem> postItemList = new ArrayList<>();


    FloatingActionButton postButton;
    UserProfilSession userProfile;

    private Context mContext;
    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_posts, container, false);

    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userProfile = new UserProfilSession(mContext);

        recyclerView = view.findViewById(R.id.post_recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.post_swipe);
        postButton = (FloatingActionButton) view.findViewById(R.id.add_post_floating_button);
        if(!userProfile.isLoggedIn())
            postButton.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadUrlData();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        postButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Post Button");
                PostDialogFragment dialog = new PostDialogFragment();
                FragmentManager fm = getFragmentManager();
                assert fm != null;
                dialog.show(fm,"PostDialog");

            }
        });

        loadUrlData();
    }

    public void loadUrlData() {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.d("Link-fetch-post", links.FETCH_POSTS);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,links.FETCH_POSTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response Arrived");
                Log.d("PostRes: ", response);
                progressDialog.dismiss();
                if(response.equals("error"))
                    return;
                try {
                    postItemList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray array = jsonObject.getJSONArray("items");
                    Log.d(TAG, "Json array length: " + array.length());
                    for (int i = 0; i < array.length(); i++){

                        JSONObject jo = array.getJSONObject(i);

                        PostItem posts = new PostItem(jo.getString("title"),
                                jo.getString("body"),
                                jo.getString("category"),
                                jo.getString("date"),
                                jo.getString("username"));
                        postItemList.add(posts);
                        Log.d(TAG, i + "th post");
                    }
                    adapter = new PostAdapter(postItemList);
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
                params.put("username", userProfile.getUsername());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
}