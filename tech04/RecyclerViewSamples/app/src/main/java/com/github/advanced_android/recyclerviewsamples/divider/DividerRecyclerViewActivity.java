package com.github.advanced_android.recyclerviewsamples.divider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.github.advanced_android.recyclerviewsamples.DummyDataGenerator;
import com.github.advanced_android.recyclerviewsamples.R;
import com.github.advanced_android.recyclerviewsamples.simple.SimpleStringAdapter;

public class DividerRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SimpleStringAdapter simpleStringAdapter;

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

        // Adapter를 설정
        // 최초의 리스트 표시에서 만든 것을 이용
        simpleStringAdapter = new SimpleStringAdapter(DummyDataGenerator.generateStringListData());
        recyclerView.setAdapter(simpleStringAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, DividerRecyclerViewActivity.class);
    }

}
