/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher3;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.View.AccessibilityDelegate;

import androidx.fragment.app.FragmentActivity;

import com.android.launcher3.logging.UserEventDispatcher;
import com.lambda.common.base.BaseDialog;
import com.lambda.common.base.DialogManager;


public abstract class BaseActivity extends FragmentActivity {

    protected DeviceProfile mDeviceProfile;
    protected UserEventDispatcher mUserEventDispatcher;

    private DialogManager dialogManager = new DialogManager();

    public DeviceProfile getDeviceProfile() {
        return mDeviceProfile;
    }

    public AccessibilityDelegate getAccessibilityDelegate() {
        return null;
    }

    public final UserEventDispatcher getUserEventDispatcher() {
        if (mUserEventDispatcher == null) {
            mUserEventDispatcher = UserEventDispatcher.newInstance(this,
                    mDeviceProfile.isLandscape, isInMultiWindowModeCompat());
        }
        return mUserEventDispatcher;
    }

    public boolean isInMultiWindowModeCompat() {
        return Utilities.ATLEAST_NOUGAT && isInMultiWindowMode();
    }

    public static BaseActivity fromContext(Context context) {
        if (context instanceof BaseActivity) {
            return (BaseActivity) context;
        }
        return ((BaseActivity) ((ContextWrapper) context).getBaseContext());
    }

    public void showDialogOnQueue(BaseDialog dialog) {
        dialogManager.showDialogOnQueue(dialog);
    }
}
