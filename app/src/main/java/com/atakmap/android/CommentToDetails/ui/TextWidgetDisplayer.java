package com.atakmap.android.CommentToDetails.ui;

import static android.text.TextUtils.isEmpty;
import static com.atakmap.android.maps.MapView.getMapView;

import android.util.Log;

import com.atakmap.android.widgets.LinearLayoutWidget;
import com.atakmap.android.widgets.RootLayoutWidget;

import gov.tak.api.commons.graphics.Font;
import gov.tak.api.commons.graphics.TextFormat;
import gov.tak.platform.widgets.TextWidget;

public class TextWidgetDisplayer {

    private static final TextWidget commentTextWidget  = new TextWidget("", 2);;
    private static final String TAG = "CommentToDetailsWidget";

    public static void registerWidget() {
        RootLayoutWidget root = (RootLayoutWidget) getMapView().getComponentExtra("rootLayoutWidget");
        final LinearLayoutWidget trLayout = root.getLayout(RootLayoutWidget.TOP_RIGHT);
        trLayout.addChildWidgetAt(trLayout.getChildCount(), commentTextWidget);

        commentTextWidget.setName("CommentText");
        commentTextWidget.setMargins(0, 8f, 0, 0);
        String familyName = commentTextWidget.getWidgetTextFormat().getFont().getFamilyName();
        Font.Style style = commentTextWidget.getWidgetTextFormat().getFont().getStyle();
        commentTextWidget.setWidgetTextFormat(new TextFormat(new Font(familyName, style, 22f ), 0));
    }
    public static void displayTextWidget(String comment) {
        if (isEmpty(comment)) {
            commentTextWidget.setVisible(false);
            return;
        }
        commentTextWidget.setText(comment.replace("<BR>", "\n"));
        commentTextWidget.setVisible(true);
    }

    public static void unregisterWidget() {
        ((RootLayoutWidget) getMapView()
                .getComponentExtra("rootLayoutWidget"))
                .getLayout(RootLayoutWidget.TOP_RIGHT)
                .removeChildWidget(commentTextWidget);
    }


}
