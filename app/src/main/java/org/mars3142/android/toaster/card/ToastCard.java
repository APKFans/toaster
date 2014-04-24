package org.mars3142.android.toaster.card;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mars3142.android.toaster.R;
import org.mars3142.android.toaster.helper.PackageHelper;

import it.gmariotti.cardslib.library.internal.Card;

public class ToastCard extends Card {

    private final static String TAG = ToastCard.class.getSimpleName();

    public String message;
    public String appName;
    public String packageName;
    public String timestamp;
    public Drawable packageIcon;

    public ToastCard(Context context) {
        super(context, R.layout.toaster_card);
    }

    public void loadData() {
        if (packageName != null && packageName != "") {
            appName = PackageHelper.getAppName(super.getContext(), packageName);
            appName = (appName == null) ? packageName : appName;
            packageIcon = PackageHelper.getIconFromPackageName(super.getContext(), packageName);
        }
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView messageTextView = (TextView) parent.findViewById(R.id.message);
        TextView packageNameTextView = (TextView) parent.findViewById(R.id.packageName);
        ImageView packageIconView = (ImageView) parent.findViewById(R.id.packageIcon);

        if (messageTextView != null) {
            messageTextView.setText(message);
        }

        if (packageNameTextView != null) {
            packageNameTextView.setText(appName == null ? packageName : appName);
        }

        if (packageIconView != null) {
            packageIconView.setImageDrawable(packageIcon);
        }
    }
}