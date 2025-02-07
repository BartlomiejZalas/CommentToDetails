package com.atakmap.android.CommentToDetails.ui;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.atakmap.android.contact.ContactLocationView;
import com.atakmap.android.contact.ContactProfileView;
import com.atakmap.android.cotdetails.ExtendedInfoView;
import com.atakmap.android.hashtags.view.RemarksLayout;
import com.atakmap.android.maps.PointMapItem;
import com.atakmap.android.util.AfterTextChangedWatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ExtendedUserDetailsForNativeRemarks implements ContactLocationView.ExtendedSelfInfoFactory {

    private static final String TAG = "CommentToDetailsUD";
    private final Context viewContext;

    public ExtendedUserDetailsForNativeRemarks(Context viewContext) {
        this.viewContext = viewContext;
    }

    @Override
    public ExtendedInfoView createView() {
        return new ExtendedInfoView(viewContext) {
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
                    RemarksLayout remarksLayout = (RemarksLayout) field.get(contactProfileView);

                    Method method2 = remarksLayout.getClass().getDeclaredMethod("setEditable", boolean.class);
                    method2.setAccessible(true);
                    method2.invoke(remarksLayout, true);
                    remarksLayout.setEnabled(true);
                    remarksLayout.addTextChangedListener(new AfterTextChangedWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            Log.d(TAG, "text changed " + s.toString());
                            // TODO add prefs save
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Cannot override remarks widget", e);
                }
            }
        };
    }

}
