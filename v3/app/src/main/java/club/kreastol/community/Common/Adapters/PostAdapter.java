package club.kreastol.community.Common.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import club.kreastol.community.Common.Items.PostItem;
import club.kreastol.community.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final List<PostItem> postItemList;

    public PostAdapter(List<PostItem> postItemList){
        this.postItemList = postItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

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
