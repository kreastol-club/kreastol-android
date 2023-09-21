package club.kreastol.kreastolcommunity.User.DashboardFragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import club.kreastol.kreastolcommunity.R;
import club.kreastol.kreastolcommunity.User.ImageDialogFragment;
import club.kreastol.kreastolcommunity.User.PostDialogFragment;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class HomeFragment extends Fragment {

    private Context mContext;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    private boolean isClicked = false;

    FloatingActionButton addButton;
    FloatingActionButton imageButton;
    FloatingActionButton postButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
//        getDialog().getWindow().setLayout(width, height);

        rotateOpen = AnimationUtils.loadAnimation(mContext, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(mContext, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(mContext, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(mContext, R.anim.to_bottom_anim);

        addButton = (FloatingActionButton) view.findViewById(R.id.add_post_and_image_floating_button);
        imageButton = (FloatingActionButton) view.findViewById(R.id.add_image_floating_button);
        postButton = (FloatingActionButton) view.findViewById(R.id.add_post_floating_button);
        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });

        imageButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Image Button");
                ImageDialogFragment imageDialogFragment = new ImageDialogFragment();
                imageDialogFragment.show(getFragmentManager(),"ImageDialog");
            }
        });

        postButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Post Button");
                PostDialogFragment postDialogFragment = new PostDialogFragment();
                postDialogFragment.show(getFragmentManager(), "PostDialog");
            }
        });

    }

    private void onAddButtonClicked() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}