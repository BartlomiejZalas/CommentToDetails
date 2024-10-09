package com.atakmap.android.CommentToDetails;

import static com.atakmap.android.maps.MapView.getMapView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.atakmap.android.CommentToDetails.ui.TextWidgetDisplayer;
import com.atakmap.android.maps.MapItem;

public class TextWidgetActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals("com.atakmap.android.maps.SHOW_DETAILS")) {
            if (intent.hasExtra("uid")) {
                String pointUID = intent.getStringExtra("uid");
                if (pointUID != null) {
                    MapItem item = getMapView().getMapItem(pointUID);
                    TextWidgetDisplayer.displayTextWidget(item);
                }
            }
        }


    }
}
