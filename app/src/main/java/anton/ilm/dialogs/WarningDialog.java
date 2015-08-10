package anton.ilm.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import anton.ilm.R;

/**
 * Created by anton on 28.05.15.
 */
public class WarningDialog extends DialogFragment {
    private static String message;

    public static WarningDialog newInstance(String msg) {
        WarningDialog f = new WarningDialog();
        message = msg;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pick a style
        int style = DialogFragment.STYLE_NO_TITLE, theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);
        if (savedInstanceState != null) {
            this.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.no_connection, container, false);
        TextView text = (TextView) v.findViewById(R.id.message);
        text.setText(message);

        return v;
    }
}
