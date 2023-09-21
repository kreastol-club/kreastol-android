package club.kreastol.community.Common.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import club.kreastol.community.Common.Items.GalleryItem;
import club.kreastol.community.Common.Items.PostItem;
import club.kreastol.community.R;

public class HomeContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Object> items;
    Context _context;

    private final int POST = 0, IMAGE = 1;

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeContentAdapter(List<Object> items, Context context) {
        this.items = items;
        _context = context;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof PostItem) {
            return POST;
        } else if (items.get(position) instanceof GalleryItem) {
            return IMAGE;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case POST:
                View v1 = inflater.inflate(R.layout.item_post, viewGroup, false);
                viewHolder = new PostHolder(v1);
                break;
            case IMAGE:
                View v2 = inflater.inflate(R.layout.item_gallery, viewGroup, false);
                viewHolder = new ImageHolder(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder viewHolder,int position){
        switch (viewHolder.getItemViewType()) {
            case POST:
                PostHolder vh1 = (PostHolder) viewHolder;
                configurePost(vh1, position);
                break;
            case IMAGE:
                ImageHolder vh2 = (ImageHolder) viewHolder;
                configureImage(vh2, position);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;

        }
    }

    private void configureDefaultViewHolder (RecyclerViewSimpleTextViewHolder vh,int position){
        vh.getLabel().setText((CharSequence) items.get(position));
    }

    private void configurePost (PostHolder vh1,int position){
        PostItem post = (PostItem) items.get(position);
        if (post != null) {
            vh1.getTitle().setText(post.getTitle());
            vh1.getBody().setText(post.getBody());
            vh1.getCategory().setText(post.getCategory());
            vh1.getDate().setText(post.getDate());
            vh1.getPoster().setText(post.getPoster());
            vh1.getPoster().setText(post.getPoster());

        }
    }

    private void configureImage (ImageHolder vh1, int position){
        GalleryItem image = (GalleryItem) items.get(position);
        if (image != null) {
            Glide.with(_context).load(image.getImgUrl()).into(vh1.imageView);
            vh1.imageTitle.setText(image.getTitle());

        }
    }

    public class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder{
        TextView label;
        public RecyclerViewSimpleTextViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public TextView getLabel() {
            return label;
        }

        public void setLabel(TextView label) {
            this.label = label;
        }
    }
    public class PostHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView body;
        public TextView date;
        public TextView category;
        public TextView poster;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.post_title);
            body = (TextView) itemView.findViewById(R.id.post_body);
            date = (TextView) itemView.findViewById(R.id.post_date);
            category = (TextView) itemView.findViewById(R.id.post_category);
            poster = (TextView) itemView.findViewById(R.id.post_poster);
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getBody() {
            return body;
        }

        public void setBody(TextView body) {
            this.body = body;
        }

        public TextView getDate() {
            return date;
        }

        public void setDate(TextView date) {
            this.date = date;
        }

        public TextView getCategory() {
            return category;
        }

        public void setCategory(TextView category) {
            this.category = category;
        }

        public TextView getPoster() {
            return poster;
        }

        public void setPoster(TextView poster) {
            this.poster = poster;
        }
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public LinearLayout linearLayout;
        TextView imageTitle;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageTitle = itemView.findViewById(R.id.gallery_image_title);
            imageView = (ImageView) itemView.findViewById(R.id.galler_image_holder);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.gallery_recycler_view);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

        public void setLinearLayout(LinearLayout linearLayout) {
            this.linearLayout = linearLayout;
        }

        public TextView getImageTitle() {
            return imageTitle;
        }

        public void setImageTitle(TextView imageTitle) {
            this.imageTitle = imageTitle;
        }
    }

}




