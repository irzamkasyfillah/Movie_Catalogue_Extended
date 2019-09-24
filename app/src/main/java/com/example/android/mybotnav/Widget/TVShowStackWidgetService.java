package com.example.android.mybotnav.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TVShowStackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TVShowStackRemoteViewsFactory(this.getApplicationContext());
    }
}
