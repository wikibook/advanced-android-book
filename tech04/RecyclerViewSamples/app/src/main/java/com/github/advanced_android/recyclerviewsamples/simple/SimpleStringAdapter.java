package com.github.advanced_android.recyclerviewsamples.simple;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.advanced_android.recyclerviewsamples.R;

import java.util.List;

public class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringAdapter.ViewHolder> {
    protected List<String> dataset;
    private View.OnClickListener onItemViewClickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.simple_text_view);
        }
    }

    // 이번에는 생성자로 데이터를 넘겨준다
    public SimpleStringAdapter(List<String> myDataset) {
        dataset = myDataset;
    }

    public void setOnItemViewClickListener(View.OnClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    // 새로운 ViewHolder를 작성한다(LayoutManager에서 호출한다)
    @Override
    public SimpleStringAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // 새로 View를 만든다
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_row, parent, false);
        // View에 클릭 리스너를 붙인다
        if (onItemViewClickListener != null) {
            v.setOnClickListener(onItemViewClickListener);
        }
        // 데이터와 관련이 없는 레이아웃 조정은 여기서 한다(여기서 만든 레이아웃을 돌려쓰기 위해)
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // View 안의 데이터를 변경한다(LayoutManager에서 호출한다)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 설정할 데이터를 가져온다
        String text = dataset.get(position);
        // ViewHolder의 View 안의 데이터를 변경한다
        holder.textView.setText(text);

    }

    // 데이터 수를 반환한다(LayoutManager에서 호출한다)
    @Override
    public int getItemCount() {
        return dataset.size();
    }

}