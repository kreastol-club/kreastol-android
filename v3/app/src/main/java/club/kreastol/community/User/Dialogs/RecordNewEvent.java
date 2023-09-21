package club.kreastol.community.User.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.android.volley.RequestQueue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import club.kreastol.community.Common.Items.UserIsHereItem;
import club.kreastol.community.R;

public class RecordNewEvent extends DialogFragment {

    RequestQueue requestQueue;
    private static final String URL_DATA_RECEIVE = "https://community.kreastol.club/android/list_users_event.php";
    private static final String URL_DATA_SEND = "https://community.kreastol.club/android/list_users_event.php";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    final String TAG = this.getClass().getSimpleName();
    private List<UserIsHereItem> userList = new ArrayList<>();
    private int eventId;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_record_new_event, null);

        Bundle bundle = getArguments();
        eventId = bundle.getInt("event_id",0);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_record_new_event, container, false);

        recyclerView = view.findViewById(R.id.event_users);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("EventId: ",eventId + " Ues");

//        loadUserData(eventId);

        return view;
    }


}