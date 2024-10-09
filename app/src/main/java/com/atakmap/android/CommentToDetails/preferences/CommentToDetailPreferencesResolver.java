package com.atakmap.android.CommentToDetails.preferences;

import static android.text.TextUtils.isEmpty;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferences.MDM_COMMENT;
import static com.atakmap.android.CommentToDetails.preferences.CommentToDetailPreferences.USER_COMMENT;
import static com.atakmap.android.maps.MapView.getMapView;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CommentToDetailPreferencesResolver {

    public static String resolveComment() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getMapView().getContext());

        String userComment = preferences.getString(USER_COMMENT, null);
        String mdmComment = preferences.getString(MDM_COMMENT, null);

        if (!isEmpty(userComment)) {
            return userComment;
        }

        if (!isEmpty(mdmComment)) {
            return mdmComment;
        }

        return "";
    }

    public static void updateCommentPreferences(String comment) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getMapView().getContext());
        preferences.edit().putString(USER_COMMENT, comment).apply();
    }
}
