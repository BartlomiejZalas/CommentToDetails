package com.atakmap.android.CommentToDetails.plugin;

import static com.atakmap.android.CommentToDetails.CommentToDetailsDropDownReceiver.SHOW_LAYOUT;

import android.content.Context;

import com.atak.plugins.impl.AbstractPluginTool;

public class PluginTool extends AbstractPluginTool {

    public PluginTool(final Context context) {
        super(context, context.getString(R.string.app_name), context.getString(R.string.app_name), context.getResources().getDrawable(R.drawable.logo), SHOW_LAYOUT);
    }
}