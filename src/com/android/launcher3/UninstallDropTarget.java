package com.android.launcher3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Toast;

import com.android.launcher3.compat.LauncherAppsCompat;
import com.android.launcher3.dragndrop.DragOptions;
import com.lambda.common.utils.CommonUtil;

import java.util.ArrayList;

public class UninstallDropTarget extends ButtonDropTarget {

    public UninstallDropTarget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UninstallDropTarget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // Get the hover color
        mHoverColor = getResources().getColor(R.color.uninstall_target_hover_tint);

        setDrawable(R.drawable.ic_uninstall_launcher);
    }

    @Override
    public void onDragStart(DragObject dragObject, DragOptions options) {
        super.onDragStart(dragObject, options);
        // 推荐移除逻辑
        if (dragObject.dragInfo != null) {
            if (dragObject.dragInfo instanceof AppInfo info) {
                if (info.intent != null && info.intent.getAction() != null && info.intent.getAction().contains(RecommendAppManager.getActionHost())) {
                    setTextBasedOnDragSource(false);
                    return;
                }
            }
        }
        setTextBasedOnDragSource(true);
    }

    public void setTextBasedOnDragSource(Boolean isUninstall) {
        if (!TextUtils.isEmpty(getText())) {
            setText(isUninstall ? R.string.uninstall_drop_target_label
                    : R.string.remove_drop_target_label);
        }
    }

    @Override
    protected boolean supportsDrop(DragSource source, ItemInfo info) {
        return supportsDrop(getContext(), info);
    }

    public static boolean supportsDrop(Context context, Object info) {
        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        Bundle restrictions = userManager.getUserRestrictions();
        if (restrictions.getBoolean(UserManager.DISALLOW_APPS_CONTROL, false)
                || restrictions.getBoolean(UserManager.DISALLOW_UNINSTALL_APPS, false)) {
            return false;
        }

        return getUninstallTarget(context, info) != null;
    }

    /**
     * @return the component name that should be uninstalled or null.
     */
    private static ComponentName getUninstallTarget(Context context, Object item) {
        Intent intent = null;
        UserHandle user = null;
        if (item instanceof AppInfo) {
            AppInfo info = (AppInfo) item;
            intent = info.intent;
            user = info.user;
        } else if (item instanceof ShortcutInfo) {
            ShortcutInfo info = (ShortcutInfo) item;
            if (info.itemType == LauncherSettings.BaseLauncherColumns.ITEM_TYPE_APPLICATION) {
                // Do not use restore/target intent here as we cannot uninstall an app which is
                // being installed/restored.
                intent = info.intent;
                user = info.user;
            }
        }
        if (intent != null) {

            // 推荐移除逻辑
            if (intent.getAction() != null && intent.getAction().contains(RecommendAppManager.getActionHost())) {
                if (RecommendAppManager.isCanRemove(intent.getAction())) {
                    return intent.getComponent();
                } else {
                    return null;
                }
            }

            LauncherActivityInfo info = LauncherAppsCompat.getInstance(context)
                    .resolveActivity(intent, user);

            if (info != null &&
                    info.getComponentName().getPackageName().equals(CommonUtil.getAppContext().getPackageName())) {
                return null;
            }

            if (info != null
                    && (info.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                return info.getComponentName();
            }
        }
        return null;
    }

    @Override
    public void onDrop(DragObject d) {
        // Differ item deletion
        if (d.dragSource instanceof DropTargetSource) {
            ((DropTargetSource) d.dragSource).deferCompleteDropAfterUninstallActivity();
        }
        super.onDrop(d);
    }

    @Override
    public void completeDrop(final DragObject d) {
        DropTargetResultCallback callback = d.dragSource instanceof DropTargetResultCallback
                ? (DropTargetResultCallback) d.dragSource : null;

        // 推荐移除逻辑
        if (d.dragInfo instanceof AppInfo appInfo) {
            if (appInfo.intent != null && appInfo.intent.getAction() != null && appInfo.intent.getAction().contains(RecommendAppManager.getActionHost())) {
                ArrayList<AppInfo> appInfos = new ArrayList<>();
                appInfos.add(appInfo);
                mLauncher.removeAppInfoFormAppView(appInfos);
                RecommendAppManager.remove(appInfo);
                return;
            }
        }

        startUninstallActivity(mLauncher, d.dragInfo, callback);
    }

    public static boolean startUninstallActivity(Launcher launcher, ItemInfo info) {
        return startUninstallActivity(launcher, info, null);
    }

    public static boolean startUninstallActivity(
            final Launcher launcher, ItemInfo info, DropTargetResultCallback callback) {
        final ComponentName cn = getUninstallTarget(launcher, info);

        final boolean isUninstallable;
        if (cn == null) {
            // System applications cannot be installed. For now, show a toast explaining that.
            // We may give them the option of disabling apps this way.
            Toast.makeText(launcher, R.string.uninstall_system_app_text, Toast.LENGTH_SHORT).show();
            isUninstallable = false;
        } else {
            Intent intent = new Intent(Intent.ACTION_DELETE,
                    Uri.fromParts("package", cn.getPackageName(), cn.getClassName()))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.putExtra(Intent.EXTRA_USER, info.user);
            launcher.startActivity(intent);
            isUninstallable = true;
        }
        if (callback != null) {
            sendUninstallResult(launcher, isUninstallable, cn, info.user, callback);
        }
        return isUninstallable;
    }

    /**
     * Notifies the {@param callback} whether the uninstall was successful or not.
     * <p>
     * Since there is no direct callback for an uninstall request, we check the package existence
     * when the launch resumes next time. This assumes that the uninstall activity will finish only
     * after the task is completed
     */
    protected static void sendUninstallResult(
            final Launcher launcher, boolean activityStarted,
            final ComponentName cn, final UserHandle user,
            final DropTargetResultCallback callback) {
        if (activityStarted) {
            final Runnable checkIfUninstallWasSuccess = new Runnable() {
                @Override
                public void run() {
                    // We use MATCH_UNINSTALLED_PACKAGES as the app can be on SD card as well.
                    boolean uninstallSuccessful = LauncherAppsCompat.getInstance(launcher)
                            .getApplicationInfo(cn.getPackageName(),
                                    PackageManager.MATCH_UNINSTALLED_PACKAGES, user) == null;
                    callback.onDragObjectRemoved(uninstallSuccessful);
                }
            };
            launcher.addOnResumeCallback(checkIfUninstallWasSuccess);
        } else {
            callback.onDragObjectRemoved(false);
        }
    }

    public interface DropTargetResultCallback {
        /**
         * A drag operation was complete.
         *
         * @param isRemoved true if the drag object should be removed, false otherwise.
         */
        void onDragObjectRemoved(boolean isRemoved);
    }

    /**
     * Interface defining an object that can provide uninstallable drag objects.
     */
    public interface DropTargetSource extends DropTargetResultCallback {

        /**
         * Indicates that an uninstall request are made and the actual result may come
         * after some time.
         */
        void deferCompleteDropAfterUninstallActivity();
    }
}
