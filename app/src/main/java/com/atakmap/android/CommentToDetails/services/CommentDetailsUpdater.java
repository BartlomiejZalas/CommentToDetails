package com.atakmap.android.CommentToDetails.services;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.atakmap.android.CommentToDetails.comment.CommentCodHandler.COMMENT_COD_KEY;
import static com.atakmap.android.CommentToDetails.comment.CommentCodHandler.DETAILS_META_KEY_COMMENT;
import static com.atakmap.android.maps.MapView.getMapView;

import android.content.Intent;

import com.atakmap.android.CommentToDetails.comment.CommentCodHandler;
import com.atakmap.android.cot.CotMapComponent;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.comms.ReportingRate;
import com.atakmap.coremap.cot.event.CotDetail;

public class CommentDetailsUpdater {
    public static void updateSelfMarkerCommentDetails(CommentCodHandler cotHandler, String comment) {
        CotDetail cd = new CotDetail(COMMENT_COD_KEY);
        cd.setAttribute(DETAILS_META_KEY_COMMENT, comment.replace("\n", "<BR>"));
        cotHandler.toItemMetadata(getMapView().getSelfMarker(), null, cd);
        CotMapComponent.getInstance().addAdditionalDetail(cd.getElementName(), cd);

        AtakBroadcast.getInstance().sendBroadcast(new Intent(ReportingRate.REPORT_LOCATION).putExtra("reason", "detail update for heart rate"));
        makeText(getMapView().getContext(), "Comment saved", LENGTH_SHORT).show();
    }
}
