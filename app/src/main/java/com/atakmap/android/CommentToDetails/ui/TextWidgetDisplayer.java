package com.atakmap.android.CommentToDetails.ui;

import static android.text.TextUtils.isEmpty;
import static com.atakmap.android.CommentToDetails.comment.CommentCodHandler.DETAILS_META_KEY_COMMENT;
import static com.atakmap.android.maps.MapView.getMapView;

import android.graphics.Typeface;

import com.atakmap.android.maps.MapItem;
import com.atakmap.android.widgets.LinearLayoutWidget;
import com.atakmap.android.widgets.RootLayoutWidget;

import gov.tak.api.commons.graphics.Font;
import gov.tak.api.commons.graphics.TextFormat;
import gov.tak.platform.widgets.TextWidget;

public class TextWidgetDisplayer {

    private static final TextWidget commentTextWidget  = new TextWidget("", 2);;

    public static void registerWidget() {
        RootLayoutWidget root = (RootLayoutWidget) getMapView().getComponentExtra("rootLayoutWidget");
        final LinearLayoutWidget trLayout = root.getLayout(RootLayoutWidget.TOP_RIGHT);
        trLayout.addChildWidgetAt(0, commentTextWidget);

        commentTextWidget.setName("CommentText");
        commentTextWidget.setMargins(0, 8f, 0, 0);
        String familyName = commentTextWidget.getWidgetTextFormat().getFont().getFamilyName();
        Font.Style style = commentTextWidget.getWidgetTextFormat().getFont().getStyle();
        commentTextWidget.setWidgetTextFormat(new TextFormat(new Font(familyName, style, 22f ), 0));
    }
    public static void displayTextWidget(MapItem item) {
        String comment = item.getMetaString(DETAILS_META_KEY_COMMENT, null);
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
