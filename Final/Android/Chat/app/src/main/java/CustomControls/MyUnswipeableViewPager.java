package CustomControls;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyUnswipeableViewPager extends ViewPager {
    public MyUnswipeableViewPager(@NonNull Context context) {
        super(context);
    }

    public MyUnswipeableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return performClick();
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return false;
    }
}
