package club.kreastol.community.util;

import android.content.Context;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

public class EditTextPlus extends AppCompatEditText {
    //This is the custom listener you will use
    public OnFocusChangeListener customListener;

    public EditTextPlus(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (customListener != null) {
            customListener.onFocusChange(this, focused);
        }
    }
}
