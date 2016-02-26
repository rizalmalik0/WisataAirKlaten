package malik.wisataairklaten.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import malik.wisataairklaten.DetailWisata;
import malik.wisataairklaten.ListGallery;
import malik.wisataairklaten.ListReview;
import malik.wisataairklaten.R;

/**
 * Created by vihaan on 1/9/15.
 */
public class CustomPager extends ViewPager {

    private View mCurrentView;

    public CustomPager(Context context) {
        super(context);
    }

    public CustomPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int height = 0;
        int h = 0;
        int rvHeight = 0;

        switch (getCurrentItem()) {
            case 0:
                RecyclerView rvGallery = (RecyclerView) mCurrentView.findViewById(R.id.rvGallery);

                if (rvGallery != null) {
                    rvGallery.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                    rvGallery.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    rvHeight = rvGallery.getMeasuredHeight();
                }

                if (rvHeight > getMeasuredWidth())
                    height = rvHeight;
                else
                    height = getMeasuredWidth();
                break;
            case 1:
                RecyclerView rvReview = (RecyclerView) mCurrentView.findViewById(R.id.rvReview);

                if (rvReview != null) {
                    rvReview.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    rvHeight = rvReview.getMeasuredHeight();
                }

                mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                h = mCurrentView.getMeasuredHeight();

                if (h + rvHeight > getMeasuredWidth())
                    height = rvHeight + h;
                else
                    height = getMeasuredWidth();
                break;
            case 2:
                mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                h = mCurrentView.getMeasuredHeight();

                if (h > height)
                    height = h;
                else
                    height = getMeasuredWidth();
                break;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }

    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }
}
