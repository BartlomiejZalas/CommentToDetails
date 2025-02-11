package com.atakmap.android.CommentToDetails.ui;

import static com.atakmap.android.imagecapture.CapturePrefs.getPrefs;
import static com.atakmap.android.maps.MapView.getMapView;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.atakmap.android.contact.ContactLocationView;
import com.atakmap.android.contact.ContactProfileView;
import com.atakmap.android.cotdetails.ExtendedInfoView;
import com.atakmap.android.hashtags.HashtagManager;
import com.atakmap.android.hashtags.util.HashtagUtils;
import com.atakmap.android.hashtags.view.RemarksLayout;
import com.atakmap.android.maps.Marker;
import com.atakmap.android.maps.PointMapItem;
import com.atakmap.android.util.AfterTextChangedWatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ExtendedUserDetailsForNativeRemarks implements ContactLocationView.ExtendedSelfInfoFactory {

    private static final String TAG = "CommentToDetailsUD";
    private final AfterTextChangedWatcher watcher = new AfterTextChangedWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            String newRemarks = s.toString().trim();
            getPrefs().edit().putString("userRemarks", newRemarks).apply();
            Log.d(TAG, "userRemarks prefs saved");

            Marker selfMarker = getMapView().getSelfMarker();
            List<String> newTags = HashtagUtils.extractTags(newRemarks);
            HashtagManager.getInstance().updateContent(selfMarker, newTags);
            selfMarker.setHashtags(newTags);
            Log.d(TAG, "userRemarks hashtags saved");
        }
    };
    private RemarksLayout remarksLayout;
    private final ExtendedInfoView extendedInfoView;

    public ExtendedUserDetailsForNativeRemarks(Context viewContext) {
        extendedInfoView = new ExtendedInfoView(viewContext) {
            @Override
            public void setMarker(PointMapItem m) {
                ViewPager tabsParent = (ViewPager) this.getParent().getParent().getParent().getParent().getParent();
                PagerAdapter adapter = tabsParent.getAdapter();

                try {
                    Method method = adapter.getClass().getDeclaredMethod("getItem", int.class);
                    method.setAccessible(true);
                    ContactProfileView contactProfileView = (ContactProfileView) method.invoke(adapter, 0);

                    Field field = ContactProfileView.class.getDeclaredField("t");
                    field.setAccessible(true);
                    remarksLayout = (RemarksLayout) field.get(contactProfileView);

                    Method method2 = remarksLayout.getClass().getDeclaredMethod("setEditable", boolean.class);
                    method2.setAccessible(true);
                    method2.invoke(remarksLayout, true);
                    remarksLayout.setEnabled(true);

                    remarksLayout.removeTextChangedListener(watcher);
                    remarksLayout.addTextChangedListener(watcher);
                } catch (Exception e) {
                    Log.e(TAG, "Cannot override remarks widget", e);
                }
            }
        };
    }

    @Override
    public ExtendedInfoView createView() {
        return extendedInfoView;
    }

    public void cleanup() {
        remarksLayout.removeTextChangedListener(watcher);
    }
}
