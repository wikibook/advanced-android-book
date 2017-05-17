package com.github.advanced_android.recyclerviewsamples.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int dividerHeight;
    private Drawable divider;

    public DividerItemDecoration(Context context) {
        // 기본인 ListView 구분선의 Drawable을 얻는다(구분선을 커스터마이징하고 싶을 때는 여기서 Drawable을 가져온다)
        final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        divider = a.getDrawable(0);

        // 표시할 때마다 높이를 가져오지 않아도 되게 여기서 구해 둔다
        dividerHeight = divider.getIntrinsicHeight();
        a.recycle();
    }

//    View의 아이템보다 위에 그리고 싶을 때는 이쪽 메소드를 사용한다
//    @Override
//    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDrawOver(c, parent, state);
//    }


    // View의 아이템보다 아래에 그리고 싶을 때는 이쪽 메소드를 사용한다
    // 여기서는 RecyclerView의 아이템마다 아래에 선을 그린다
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // 좌우의 padding으로 선의 right과 left를 설정
        final int lineLeft = parent.getPaddingLeft();
        final int lineRight = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            // 애니메이션 등일 때에 제대로 이동하기 위해서
            int childTransitionY = Math.round(ViewCompat.getTranslationY(child));
            final int top = child.getBottom() + params.bottomMargin + childTransitionY;
            final int bottom = top + dividerHeight;

            // View 아래에 선을 그린다
            divider.setBounds(lineLeft, top, lineRight, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // View 아래에 선이 들어가므로 아래에 Offset을 넣는다
        outRect.set(0, 0, 0, dividerHeight);
    }
}