package club.kreastol.community.User.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import club.kreastol.community.R;
import club.kreastol.community.Volley.LinkHolder;


public class AddNewEvent extends DialogFragment {

    RequestQueue requestQueue;

    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    LinkHolder links = new LinkHolder();

    Button cancel, upload;
    TextInputLayout eventTitle;
    NumberPicker lowestAge, highestAge;
    TimePicker startTimePicker, endTimePicker;
    String formattedLowestDate, formattedHighestDate, eventName;
    String startTimeString, endTimeString;

    String day;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_new_event, container, false);

        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Spinner daySpinner =  view.findViewById(R.id.event_day);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        daySpinner.setAdapter(adapter);
        cancel = view.findViewById(R.id.add_new_event_cancel);
        upload = view.findViewById(R.id.save_new_event);

        eventTitle = view.findViewById(R.id.new_event_name);
        lowestAge = view.findViewById(R.id.lowest_age);
        highestAge = view.findViewById(R.id.highest_age);
        lowestAge.setMaxValue(50);
        lowestAge.setMinValue(2);
        highestAge.setMaxValue(120);
        highestAge.setMinValue(2);

        startTimePicker = view.findViewById(R.id.start_time);
        endTimePicker = view.findViewById(R.id.end_time);
        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                day = daySpinner.getSelectedItem().toString();
                ageCalculating();

                eventName = Objects.requireNonNull(eventTitle.getEditText().getText().toString());
                int startTimeHour = startTimePicker.getCurrentHour();
                int startTimeMinute = startTimePicker.getCurrentMinute();
                int endTimeHour = endTimePicker.getCurrentHour();
                int endTimeMinute = endTimePicker.getCurrentMinute();
                startTimeString = startTimeHour + ":" + startTimeMinute + ":00";
                endTimeString = endTimeHour + ":" + endTimeMinute + ":00";

                uploadEvent();

            }
        });

        return view;
    }

    private void uploadEvent(){
        StringRequest request = new StringRequest(Request.Method.POST, links.ADD_EVENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                if(response.equals("yes")) {
                    Log.d("Response","It went through");
                    getDialog().dismiss();
                }
                else{
                    Toast.makeText(mContext, getResources().getString(R.string.add_post_dialog_post_error),Toast.LENGTH_SHORT);
                    getDialog().dismiss();
                }
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put("event_name", eventName);
                params.put("day", day);
                params.put("start_time", startTimeString);
                params.put("end_time", endTimeString);
                params.put("highest_age", formattedHighestDate);
                params.put("lowest_age", formattedLowestDate);
                return params;
            }
        };

        Volley.newRequestQueue(mContext).add(request);
    }

    private void ageCalculating(){
        //Current date
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = df.format(calendar);
        Log.d("Current Date: ","Current time => " + formattedDate);

        Calendar highestAgeCalendar = Calendar.getInstance();
        highestAgeCalendar.add(Calendar.YEAR, (highestAge.getValue() * -1));
        highestAgeCalendar.set(Calendar.MONTH, 11);
        highestAgeCalendar.set(Calendar.DAY_OF_MONTH, 31);

        Calendar lowestAgeCalendar = Calendar.getInstance();
        lowestAgeCalendar.add(Calendar.YEAR, (lowestAge.getValue() * -1));
        lowestAgeCalendar.set(Calendar.MONTH, 1);
        lowestAgeCalendar.set(Calendar.DAY_OF_MONTH, 1);

        Date highestAgeCalendarDate = highestAgeCalendar.getTime();
        SimpleDateFormat highestDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formattedHighestDate = highestDateFormat.format(highestAgeCalendarDate);
        Log.d("Age Date: ","Simple Highest age => " + formattedHighestDate);

        Date lowestAgeCalendarDate = lowestAgeCalendar.getTime();
        SimpleDateFormat lowestDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formattedLowestDate = lowestDateFormat.format(lowestAgeCalendarDate);
        Log.d("Age Date: ","Simple Lowest age => " + formattedLowestDate);
    }
}