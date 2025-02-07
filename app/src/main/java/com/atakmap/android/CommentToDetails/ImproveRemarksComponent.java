package com.atakmap.android.CommentToDetails;

import static com.atakmap.android.CommentToDetails.TextWidgetActionReceiver.TextResolver;
import static com.atakmap.android.imagecapture.CapturePrefs.getPrefs;
import static com.atakmap.android.maps.MapView.getMapView;
import static com.atakmap.android.util.ATAKUtilities.isSelf;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.atakmap.android.CommentToDetails.comment.CommentCodHandler;
import com.atakmap.android.CommentToDetails.plugin.R;
import com.atakmap.android.CommentToDetails.ui.ExtendedUserDetailsForNativeRemarks;
import com.atakmap.android.CommentToDetails.ui.TextWidgetDisplayer;
import com.atakmap.android.contact.ContactLocationView;
import com.atakmap.android.cot.detail.CotDetailManager;
import com.atakmap.android.dropdown.DropDownMapComponent;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.maps.PointMapItem;


public class ImproveRemarksComponent extends DropDownMapComponent {

    private static final String TAG = "CommentToDetails";
    private CommentCodHandler cotHandler;
    private ExtendedUserDetailsForNativeRemarks extendedUserDetails;
    private TextWidgetActionReceiver textWidgetActionReceiver;

    @Override
    public void onResume(Context context, MapView view) {
        super.onResume(context, view);
    }

    public void onCreate(final Context context, Intent intent, final MapView view) {
        context.setTheme(R.style.ATAKPluginTheme);
        registerCotHandler();
        registerExtendedDetails(view);
        registerTextWidgetActions();
    }


    private void registerExtendedDetails(MapView view) {
        extendedUserDetails = new ExtendedUserDetailsForNativeRemarks(view.getContext());
        ContactLocationView.register(extendedUserDetails);
    }

    private void registerCotHandler() {
        cotHandler = new CommentCodHandler();
        CotDetailManager.getInstance().registerHandler(cotHandler);
    }


    private void registerTextWidgetActions() {
        DocumentedIntentFilter showFilter = new DocumentedIntentFilter();
        showFilter.addAction("com.atakmap.android.maps.SHOW_DETAILS");
        TextResolver resolveRemarks = i -> isSelf(getMapView(), (PointMapItem) i) ? getPrefs().getString("userRemarks", "").trim() : i.getRemarks();
        textWidgetActionReceiver = new TextWidgetActionReceiver(resolveRemarks);
        AtakBroadcast.getInstance().registerReceiver(textWidgetActionReceiver, showFilter);
        TextWidgetDisplayer.registerWidget();
    }

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        Log.d(TAG, "onDestroyImpl");
        CotDetailManager.getInstance().unregisterHandler(cotHandler);
        ContactLocationView.unregister(extendedUserDetails);
        AtakBroadcast.getInstance().unregisterReceiver(textWidgetActionReceiver);
        TextWidgetDisplayer.unregisterWidget();
    }
}
