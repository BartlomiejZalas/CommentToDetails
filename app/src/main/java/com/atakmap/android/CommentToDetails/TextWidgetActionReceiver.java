package com.atakmap.android.CommentToDetails;

import static com.atakmap.android.imagecapture.CapturePrefs.getPrefs;
import static com.atakmap.android.maps.MapView.getMapView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.atakmap.android.CommentToDetails.ui.TextWidgetDisplayer;
import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.Marker;

import java.util.HashMap;
import java.util.Map;

public class TextWidgetActionReceiver extends BroadcastReceiver {

    private static final String TAG = "CommentToDetailsWR";

    interface TextResolver {
        String resolve(MapItem i);
    }

    private final TextResolver textResolver;

    public TextWidgetActionReceiver(TextResolver textResolver) {
        this.textResolver = textResolver;
    }


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
                    TextWidgetDisplayer.displayTextWidget(textResolver.resolve(item));
                }
            }
        }


    }
}
