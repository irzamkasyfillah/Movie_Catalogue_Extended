package com.example.android.mybotnav.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MovieStackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MovieStackRemoteViewsFactory(this.getApplicationContext());
    }
}
