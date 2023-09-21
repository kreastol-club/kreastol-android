package club.kreastol.kreastolcommunity.HelperClasses.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import club.kreastol.kreastolcommunity.HelperClasses.Models.PostItem;
import club.kreastol.kreastolcommunity.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DATE = "date";
    public static final String KEY_POSTER = "username";

    private List<PostItem> postItemList = new ArrayList<>();
    private Context context;

    public PostAdapter(List<PostItem> postItemList, Context context){
        this.postItemList = postItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final PostItem postItem = postItemList.get(position);

        holder.title.setText(postItem.getTitle());
        holder.body.setText(postItem.getBody());
        holder.date.setText(postItem.getDate());
        holder.category.setText(postItem.getCategory());
        holder.poster.setText(postItem.getPoster());


    }

    @Override
    public int getItemCount() {
        return postItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView body;
        public TextView date;
        public TextView category;
        public TextView poster;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.post_title);
            body = (TextView) itemView.findViewById(R.id.post_body);
            date = (TextView) itemView.findViewById(R.id.post_date);
            category = (TextView) itemView.findViewById(R.id.post_category);
            poster = (TextView) itemView.findViewById(R.id.post_poster);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.post_recycler_view);
        }
    }
}
