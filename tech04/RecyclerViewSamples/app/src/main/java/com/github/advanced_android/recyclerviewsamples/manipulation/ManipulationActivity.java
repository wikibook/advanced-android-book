package com.github.advanced_android.recyclerviewsamples.manipulation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.github.advanced_android.recyclerviewsamples.DummyDataGenerator;
import com.github.advanced_android.recyclerviewsamples.R;

public class ManipulationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManipulationSimpleStringAdapter simpleStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipulation_recycler_view);
        setupRecyclerView();
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleStringAdapter.addAtPosition(3, "새 아이템");
            }
        });
        findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleStringAdapter.removeAtPosition(3);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.simple_recycler_view);

        // RecyclerView 자체의 크기가 변하지 않는 것을 알고 있을 때
        // 이 옵션을 지정해두면 성능이 개선된다
        recyclerView.setHasFixedSize(true);

        // Adapter를 설정한다
        simpleStringAdapter = new ManipulationSimpleStringAdapter(DummyDataGenerator.generateStringListData());
        recyclerView.setAdapter(simpleStringAdapter);

        // ItemTouchHelper클래스를 구현한다
        // 이에 따라 드래그앤드롭이나 스와이프로 삭제 등을 할 수 있게 된다.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 드래그앤드롭 시
                simpleStringAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 아이템 스와이프 시
                simpleStringAdapter.removeAtPosition(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ManipulationActivity.class);
    }

}
