package com.atakmap.android.CommentToDetails;

import static android.widget.Toast.makeText;
import static com.atakmap.android.CommentToDetails.comment.CommentCodHandler.DETAILS_META_KEY_COMMENT;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferencesResolver.resolveComment;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferencesResolver.updateCommentPreferences;
import static com.atakmap.android.CommentToDetails.services.CommentDetailsUpdater.updateSelfMarkerCommentDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.CommentToDetails.comment.CommentCodHandler;
import com.atakmap.android.CommentToDetails.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.maps.MapView;
import com.atakmap.coremap.log.Log;

public class CommentToDetailsDropDownReceiver extends DropDownReceiver implements OnStateListener {

    public static final String SHOW_LAYOUT = "com.atakmap.android.CommentToDetails.SHOW_LAYOUT";
    private static final String TAG = "CommentToDetailsDDR";
    private final View view;
    private final CommentCodHandler cotHandler;
    private final EditText commentField;


    protected CommentToDetailsDropDownReceiver(MapView mapView, Context context, CommentCodHandler cotHandler) {
        super(mapView);
        this.view = PluginLayoutInflater.inflate(context, R.layout.layout, null);
        this.cotHandler = cotHandler;

        commentField = view.findViewById(R.id.commentField);
        updateCommentField();

        ImageButton editButton = view.findViewById(R.id.editCommentBtn);
        editButton.setOnClickListener(v -> promptEditText(mapView));
    }

    protected void promptEditText(MapView mapView) {
        String currentComment = resolveComment();

        final EditText input = new EditText(mapView.getContext());
        input.setText(currentComment);

        AlertDialog.Builder b = new AlertDialog.Builder(mapView.getContext());

        b.setView(input);
        b.setPositiveButton("Save", (dialog, which) -> {
            String newComment = input.getText().toString();
            updateSelfMarkerCommentDetails(cotHandler, newComment);
            updateCommentPreferences(newComment);
            commentField.setText(newComment);
        });
        b.setNegativeButton("Cancel", null);
        final AlertDialog d = b.create();

        Window w = d.getWindow();
        if (w != null) {
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        d.show();
        input.requestFocus();
    }

    @Override
    protected void disposeImpl() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if (action == null)
            return;

        // Show drop-down
        if (action.equals(SHOW_LAYOUT)) {
            showDropDown(view, 0.4, FULL_HEIGHT, 0.5, HALF_HEIGHT, false, this);
            updateCommentField();
        }
    }

    private void updateCommentField() {
        if (commentField != null) {
            commentField.setText(resolveComment());
        }
    }

    @Override
    public void onDropDownSelectionRemoved() {

    }

    @Override
    public void onDropDownClose() {

    }

    @Override
    public void onDropDownSizeChanged(double v, double v1) {

    }

    @Override
    public void onDropDownVisible(boolean b) {

    }

}
