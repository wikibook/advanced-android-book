package com.github.advanced_android.recyclerviewsamples.manipulation;

import com.github.advanced_android.recyclerviewsamples.cardview.CardViewAdapter;

import java.util.List;

/*
    CardViewAdapter와 하는 일이 거의 같으므로 상속해서 필요한 부분 추가한다
*/
public class ManipulationSimpleStringAdapter extends CardViewAdapter {
    public ManipulationSimpleStringAdapter(List<String> myDataset) {
        super(myDataset);
    }

    // 데이터를 삽입한다
    public void addAtPosition(int position, String text) {
        if (position > dataset.size()) {
            // 현재 존재하는 아이템의 수보다 많은 위치를 지정하므로 마지막 위치에 추가
            position = dataset.size();
        }
        // 데이터를 추가한다
        dataset.add(position, text);
        // 삽입했다고 Adapter에 알린다
        notifyItemInserted(position);
    }

    // 데이터를 삭제한다
    public void removeAtPosition(int position) {
        if (position < dataset.size()) {
            // 데이터를 삭제한다
            dataset.remove(position);
            // 삭제했다고 Adapter 알린다
            notifyItemRemoved(position);
        }
    }


    // 데이터를 이동한다
    public void move(int fromPosition, int toPosition) {
        final String text = dataset.get(fromPosition);
        dataset.remove(fromPosition);
        dataset.add(toPosition, text);
        notifyItemMoved(fromPosition, toPosition);
    }
}
