package com.atakmap.android.CommentToDetails.preferences;

import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferences.MDM_COMMENT;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferences.USER_COMMENT;
import static com.atakmap.android.CommentToDetails.services.CommentDetailsUpdater.updateSelfMarkerCommentDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.atakmap.android.CommentToDetails.comment.CommentCodHandler;
import com.atakmap.android.CommentToDetails.plugin.R;
import com.atakmap.android.gui.PanEditTextPreference;
import com.atakmap.android.preference.PluginPreferenceFragment;

public class CommentToDetailPreferenceFragment extends PluginPreferenceFragment {

    public static final String TAG = "CommentToDetailsPreferenceFr";
    private static Context staticPluginContext;

    /**
     * Only will be called after this has been instantiated with the 1-arg constructor.
     * Fragments must has a zero arg constructor.
     */
    public CommentToDetailPreferenceFragment() {
        super(staticPluginContext, R.xml.preferences);
    }

    @SuppressLint("ValidFragment")
    public CommentToDetailPreferenceFragment(final Context pluginContext) {
        super(pluginContext, R.xml.preferences);
        staticPluginContext = pluginContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PanEditTextPreference userComment = (PanEditTextPreference) findPreference(USER_COMMENT);
        PanEditTextPreference mdmComment = (PanEditTextPreference) findPreference(MDM_COMMENT);

        userComment.setOnPreferenceChangeListener((preference, newValue) -> updateComment((String) newValue));
        mdmComment.setOnPreferenceChangeListener((preference, newValue) -> updateComment((String) newValue));
    }

    private boolean updateComment(String newComment) {
        updateSelfMarkerCommentDetails(new CommentCodHandler(), newComment);
        return true;
    }
}
