package club.kreastol.community.Common.Adapters;

import android.content.Context;
import android.util.Log;
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
import club.kreastol.community.R;


public class GalleryAdapter extends RecyclerView.Adapter<ImageViewHolder>{

    private Context context;
    private List<GalleryItem> imgList;

    public GalleryAdapter(Context context, List<GalleryItem> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Log.d("Adapter", "Binding" + position + "th picture");
        Glide.with(context).load(imgList.get(position).getImgUrl()).into(holder.imageView);
        holder.imageTitle.setText(imgList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }
}

class ImageViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public LinearLayout linearLayout;
    TextView imageTitle;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageTitle = itemView.findViewById(R.id.gallery_image_title);
        imageView = (ImageView) itemView.findViewById(R.id.galler_image_holder);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.gallery_recycler_view);
    }
}