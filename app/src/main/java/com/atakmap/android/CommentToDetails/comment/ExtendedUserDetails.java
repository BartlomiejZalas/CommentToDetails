package com.atakmap.android.CommentToDetails.comment;

import static android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
import static com.atakmap.android.CommentToDetails.comment.CommentCodHandler.DETAILS_META_KEY_COMMENT;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atakmap.android.contact.ContactLocationView;
import com.atakmap.android.cotdetails.ExtendedInfoView;
import com.atakmap.android.maps.PointMapItem;

public class ExtendedUserDetails implements ContactLocationView.ExtendedSelfInfoFactory {

    private static final String TAG = "CommentToDetailsUD";
    private final Context pluginContext;
    private final Context viewContext;

    public ExtendedUserDetails(Context pluginContext, Context viewContext) {
        this.pluginContext = pluginContext;
        this.viewContext = viewContext;
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

                TextView hrValue = new TextView(viewContext);
                hrValue.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                hrValue.setTextColor(Color.WHITE);

                LinearLayout hrParent = new LinearLayout(pluginContext);
                hrParent.setOrientation(VERTICAL);
                hrParent.addView(hrLabel);
                hrParent.addView(hrValue);

                LinearLayout container = new LinearLayout(pluginContext);
                container.setOrientation(HORIZONTAL);
                container.addView(hrParent);
                this.addView(container);

                String comment = m.getMetaString(DETAILS_META_KEY_COMMENT, null);

                if (comment != null) {
                    Log.d(TAG, "Comment from meta:" + comment);
                    String convertedCommentString = comment.replace("<BR>", "\n");
                    Log.d(TAG, "Comment to display:" + convertedCommentString);
                    hrValue.setText(convertedCommentString);
                }
            }
        };
    }
}
