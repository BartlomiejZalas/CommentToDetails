package com.atakmap.android.CommentToDetails.ui;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
import static com.atakmap.android.CommentToDetails.comment.CommentCodHandler.DETAILS_META_KEY_COMMENT;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferencesResolver.resolveComment;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferencesResolver.updateCommentPreferences;
import static com.atakmap.android.CommentToDetails.services.CommentDetailsUpdater.updateSelfMarkerCommentDetails;
import static com.atakmap.android.util.ATAKConstants.getFullVersionName;
import static com.atakmap.android.util.ATAKConstants.getVersionBrand;
import static com.atakmap.android.util.ATAKConstants.getVersionCode;
import static com.atakmap.android.util.ATAKConstants.getVersionName;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.atakmap.android.CommentToDetails.comment.CommentCodHandler;
import com.atakmap.android.CommentToDetails.plugin.R;
import com.atakmap.android.contact.ContactLocationView;
import com.atakmap.android.cotdetails.ExtendedInfoView;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.maps.PointMapItem;

public class ExtendedUserDetails implements ContactLocationView.ExtendedSelfInfoFactory {

    private static final String TAG = "CommentToDetailsUD";
    private final Context pluginContext;
    private final Context viewContext;
    private final CommentCodHandler cotHandler;
    private TextView hrValue;

    public ExtendedUserDetails(Context pluginContext, Context viewContext, CommentCodHandler cotHandler) {
        this.pluginContext = pluginContext;
        this.viewContext = viewContext;
        this.cotHandler = cotHandler;
    }

    @Override
    public ExtendedInfoView createView() {
        return new ExtendedInfoView(viewContext) {
            @Override
            public void setMarker(PointMapItem m) {
                TextView hrLabel = new TextView(pluginContext);
                hrLabel.setText("Comment");
                hrLabel.setTextColor(Color.parseColor("#d4b246"));
                hrLabel.setTextSize(10);
                hrLabel.setPadding(0, 0, 0, 0);
                hrLabel.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

                hrValue = new TextView(viewContext);
                hrValue.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                hrValue.setTextColor(Color.WHITE);

                LinearLayout hrValueWrapper = new LinearLayout(pluginContext);
                hrValueWrapper.setOrientation(VERTICAL);
                hrValueWrapper.setLayoutParams(new LayoutParams(0, WRAP_CONTENT, 1));
                hrValueWrapper.addView(hrValue);

                ImageButton editButton = new ImageButton(pluginContext);
                int size = (int) pluginContext.getResources().getDimension(R.dimen.image_button_size);
                editButton.setLayoutParams(new LinearLayout.LayoutParams(size, size));
                Drawable icon = ContextCompat.getDrawable(pluginContext, R.drawable.ic_edit);
                editButton.setImageDrawable(icon);
                editButton.setContentDescription("Edit");
                int padding = (int) pluginContext.getResources().getDimension(R.dimen.image_button_padding);
                editButton.setPadding(padding, padding, padding, padding);
                editButton.setBackgroundResource(R.drawable.btn_gray);
                editButton.setOnClickListener(v -> promptEditText(MapView.getMapView()));

                LinearLayout columns = new LinearLayout(pluginContext);
                columns.setOrientation(HORIZONTAL);
                columns.addView(hrValueWrapper);
                columns.addView(editButton);

                LinearLayout container = new LinearLayout(pluginContext);
                container.setOrientation(VERTICAL);
                container.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                container.addView(hrLabel);
                container.addView(columns);

                this.addView(container);

                String comment = m.getMetaString(DETAILS_META_KEY_COMMENT, null);

                if (comment != null) {
                    String convertedCommentString = comment.replace("<BR>", "\n");
                    hrValue.setText(convertedCommentString);
                }
            }
        };
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
            hrValue.setText(newComment);
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
}
