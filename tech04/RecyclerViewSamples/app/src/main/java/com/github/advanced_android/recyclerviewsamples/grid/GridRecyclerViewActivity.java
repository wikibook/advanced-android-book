package com.github.advanced_android.recyclerviewsamples.grid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.advanced_android.recyclerviewsamples.DummyDataGenerator;
import com.github.advanced_android.recyclerviewsamples.R;

public class GridRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_view);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.simple_recycler_view);
        // RecyclerView 자체의 크기가 변하지 않는 것을 알고 있을 때
        // 이 옵션을 지정하면 성능이 개선된다
        recyclerView.setHasFixedSize(true);

        // Adapter를 설정한다
        // 최초의 리스트 표시에서 만든 것을 이용
        final RichAdapter adapter = new RichAdapter(DummyDataGenerator.generateStringListData());
        recyclerView.setAdapter(adapter);

        // 열을 3으로 설정한 GridLayoutManager의 인스턴를 생성하고 설정
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        // SpanSizeLookup으로 위치별로 차지할 폭을 결정한다
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == RichAdapter.HEADER_VIEW_TYPE) {
                    // 헤더는 3열을 차지해서 표시한다(표시되는 것은 1열)
                    return gridLayoutManager.getSpanCount();
                }
                // 나머지는 1열을 사용한다(표시되는 것은 3열)
                return 1;
            }
        };
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, GridRecyclerViewActivity.class);
    }

}
