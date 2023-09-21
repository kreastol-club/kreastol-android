package club.kreastol.community.Common.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import club.kreastol.community.Common.Items.PostItem;
import club.kreastol.community.Common.Items.UserIsHereItem;
import club.kreastol.community.R;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;

public class HereUserAdapter extends RecyclerView.Adapter<HereUserAdapter.ViewHolder>{

    private final List<UserIsHereItem> userList;
    private SessionManager session;
    private AppRunSession appRun;

    public HereUserAdapter(List<UserIsHereItem> userlist, Context context) {
        Log.d("Called: ","new user");

        this.userList = userlist;
        appRun = new AppRunSession(context);
    }

    @NonNull
    @Override
    public HereUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_user, parent, false);
        return new HereUserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HereUserAdapter.ViewHolder holder, int position) {
        final UserIsHereItem userItem = userList.get(position);
        Log.d("LangInUserAdapter",appRun.getLanguage());
        if(appRun.getLanguage().equals("hu") || appRun.getLanguage().equals("ja")){
            holder.name_1.setText(userItem.getLastName());
            holder.name_2.setText(userItem.getFirstName());
        }
        else{
            holder.name_2.setText(userItem.getLastName());
            holder.name_1.setText(userItem.getFirstName());
        }
        holder.isHere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userItem.setUserIsHere(isChecked);
                Log.d("UserStatusChanged :", isChecked ? userList.get(position).getFirstName() + "is here" : userList.get(position).getFirstName() + "is not here" );

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name_1;
        public TextView name_2;
        public CheckBox isHere;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_1 = itemView.findViewById(R.id.name_1);
            name_2 = itemView.findViewById(R.id.name_2);
            isHere = itemView.findViewById(R.id.user_is_here);
        }
    }
}
