package com.atakmap.android.CommentToDetails.plugin;


import static com.atakmap.android.util.ATAKConstants.getVersionCode;

import com.atak.plugins.impl.AbstractPlugin;
import com.atak.plugins.impl.PluginContextProvider;
import com.atakmap.android.CommentToDetails.CommentToDetailsComponent;
import com.atakmap.android.CommentToDetails.ImproveRemarksComponent;

import gov.tak.api.plugin.IServiceController;

public class PluginTemplateLifecycle extends AbstractPlugin {

    public PluginTemplateLifecycle(IServiceController serviceController) {
        super(serviceController, getVersionCode() >= 1736526545 ? new ImproveRemarksComponent() : new CommentToDetailsComponent());
        PluginNativeLoader.init(serviceController.getService(PluginContextProvider.class).getPluginContext());
    }
}

