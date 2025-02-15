package com.atakmap.android.CommentToDetails;

import static com.atakmap.android.CommentToDetails.comment.CommentCodHandler.DETAILS_META_KEY_COMMENT;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferences.PREFERENCES_KEYS;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferencesResolver.resolveComment;
import static com.atakmap.android.CommentToDetails.services.CommentDetailsUpdater.updateSelfMarkerCommentDetails;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.atakmap.android.CommentToDetails.comment.CommentCodHandler;
import com.atakmap.android.CommentToDetails.ui.ExtendedUserDetails;
import com.atakmap.android.CommentToDetails.plugin.R;
import com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferenceFragment;
import com.atakmap.android.CommentToDetails.ui.TextWidgetDisplayer;
import com.atakmap.android.contact.ContactLocationView;
import com.atakmap.android.cot.detail.CotDetailManager;
import com.atakmap.android.dropdown.DropDownMapComponent;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter;
import com.atakmap.android.maps.MapView;
import com.atakmap.app.preferences.ToolsPreferenceFragment;


public class CommentToDetailsComponent extends DropDownMapComponent {

    private static final String TAG = "CommentToDetails";
    private CommentCodHandler cotHandler;
    private ExtendedUserDetails extendedUserDetails;
    private TextWidgetActionReceiver textWidgetActionReceiver;

    @Override
    public void onResume(Context context, MapView view) {
        super.onResume(context, view);
    }

    public void onCreate(final Context context, Intent intent, final MapView view) {
        context.setTheme(R.style.ATAKPluginTheme);
        registerCotHandler();
        registerExtendedDetails(context, view);
        registerPreferences(context);
        updateCommentFromPreferences();
        registerTextWidgetActions();
        TextWidgetDisplayer.registerWidget();
    }

    private void updateCommentFromPreferences() {
        updateSelfMarkerCommentDetails(cotHandler, resolveComment());
    }

    private void registerPreferences(Context context) {
        ToolsPreferenceFragment.register(
                new ToolsPreferenceFragment.ToolPreference(
                        context.getResources().getString(R.string.preferences_name),
                        context.getResources().getString(R.string.preferences_description),
                        PREFERENCES_KEYS,
                        context.getResources().getDrawable(R.drawable.logo, null),
                        new CommentToDetailPreferenceFragment(context))
        );
    }

    private void registerExtendedDetails(Context context, MapView view) {
        extendedUserDetails = new ExtendedUserDetails(context, view.getContext(), cotHandler);
        ContactLocationView.register(extendedUserDetails);
    }

    private void registerCotHandler() {
        cotHandler = new CommentCodHandler();
        CotDetailManager.getInstance().registerHandler(cotHandler);
    }

    private void registerTextWidgetActions() {
        DocumentedIntentFilter showFilter = new DocumentedIntentFilter();
        showFilter.addAction("com.atakmap.android.maps.SHOW_DETAILS");
        textWidgetActionReceiver = new TextWidgetActionReceiver(i -> i.getMetaString(DETAILS_META_KEY_COMMENT, null));
        AtakBroadcast.getInstance().registerReceiver(textWidgetActionReceiver, showFilter);
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
