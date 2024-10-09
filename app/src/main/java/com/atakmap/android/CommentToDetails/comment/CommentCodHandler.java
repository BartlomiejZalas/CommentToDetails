package com.atakmap.android.CommentToDetails.comment;

import static com.atakmap.coremap.filesystem.FileSystemUtils.isEmpty;

import android.util.Log;

import com.atakmap.android.cot.detail.CotDetailHandler;
import com.atakmap.android.maps.MapItem;
import com.atakmap.comms.CommsMapComponent;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;

public class CommentCodHandler extends CotDetailHandler {
    public static final String TAG = "CommentToDetailsCotHandler";

    public static final String COMMENT_COD_KEY = "COMMENT_COD_KEY";
    public static final String DETAILS_META_KEY_COMMENT = "com.atakmap.coremap.filesystem.FileSystemUtils.DETAILS_META_KEY_COMMENT";

    public CommentCodHandler() {
        super(COMMENT_COD_KEY);
    }

    @Override
    public CommsMapComponent.ImportResult toItemMetadata(MapItem item, CotEvent event, CotDetail detail) {
        String commentString = detail.getAttribute(DETAILS_META_KEY_COMMENT);

        if (isEmpty(commentString)) {
            return CommsMapComponent.ImportResult.FAILURE;
        }

        item.setMetaString(DETAILS_META_KEY_COMMENT, commentString);
        return CommsMapComponent.ImportResult.SUCCESS;
    }

    @Override
    public boolean toCotDetail(MapItem item, CotEvent event,
                               CotDetail root) {
        return true;
    }
}
