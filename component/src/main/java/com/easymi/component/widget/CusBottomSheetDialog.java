package com.easymi.component.widget;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.easymi.component.R;
import com.easymi.component.utils.Log;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.lang.reflect.Field;

/**
 * Base class for {@link android.app.Dialog}s styled as a bottom sheet.
 */
public class CusBottomSheetDialog extends AppCompatDialog {

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean cancelAble = true;

    public CusBottomSheetDialog(@NonNull Context context) {
        this(context, 0);
    }

    public CusBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, getThemeResId(context, theme));
        // We hide the title bar for any style configuration. Otherwise, there will be a gap
        // above the bottom sheet when it is expanded.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected CusBottomSheetDialog(@NonNull Context context, boolean cancelable,
                                   OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(wrapInBottomSheet(layoutResId, null, null));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(wrapInBottomSheet(0, view, null));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(wrapInBottomSheet(0, view, params));
    }

    @Override
    public void setCancelable(boolean flag) {
        cancelAble = flag;
        super.setCancelable(flag);
    }

    private View wrapInBottomSheet(int layoutResId, View view, ViewGroup.LayoutParams params) {
        final CoordinatorLayout coordinator = (CoordinatorLayout) View.inflate(getContext(),
                R.layout.my_layout_bottom, null);
        if (layoutResId != 0 && view == null) {
            view = getLayoutInflater().inflate(layoutResId, coordinator, false);
        }
        FrameLayout bottomSheet = (FrameLayout) coordinator.findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
        if (params == null) {
            bottomSheet.addView(view);
        } else {
            bottomSheet.addView(view, params);
        }
        // We treat the CoordinatorLayout as outside the dialog though it is technically inside
        if (shouldWindowCloseOnTouchOutside() && cancelAble) {
            coordinator.findViewById(R.id.touch_outside).setOnClickListener(
                    view1 -> {
                        if (isShowing()) {
                            cancel();
                        }
                    });
        }
        return coordinator;
    }

    private boolean shouldWindowCloseOnTouchOutside() {
        if (Build.VERSION.SDK_INT < 11) {
            return true;
        }
        TypedValue value = new TypedValue();
        //noinspection SimplifiableIfStatement
        if (getContext().getTheme()
                .resolveAttribute(android.R.attr.windowCloseOnTouchOutside, value, true)) {
            return value.data != 0;
        }
        return false;
    }

    private static int getThemeResId(Context context, int themeId) {
        if (themeId == 0) {
            // If the provided theme is 0, then retrieve the dialogTheme from our theme
            TypedValue outValue = new TypedValue();
            if (context.getTheme().resolveAttribute(
                    R.attr.bottomSheetDialogTheme, outValue, true)) {
                themeId = outValue.resourceId;
            } else {
                // bottomSheetDialogTheme is not provided; we default to our light theme
                themeId = R.style.Theme_Design_Light_BottomSheetDialog;
            }
        }
        return themeId;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (slideOffset > bottomSheet.getMeasuredHeight() / 2) {
                dismiss();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
//        会有异常
//        if (bottomSheetBehavior != null)
// bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        //暂时解决方法
        try {
            Field field = BottomSheetBehavior.class.getDeclaredField("mState");
            field.setAccessible(true);
            field.setInt(bottomSheetBehavior, BottomSheetBehavior.STATE_EXPANDED);
        } catch (NoSuchFieldException e) {
            Log.e("BottomSheetBehavior", "Error setting BottomSheetBehavior initial state (1)", e);
        } catch (IllegalAccessException e) {
            Log.e("BottomSheetBehavior", "Error setting BottomSheetBehavior initial state (2)", e);
        }
    }
}