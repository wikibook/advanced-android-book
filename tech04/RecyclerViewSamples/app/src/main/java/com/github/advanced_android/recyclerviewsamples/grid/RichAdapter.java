package com.github.advanced_android.recyclerviewsamples.grid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.advanced_android.recyclerviewsamples.R;

import java.util.List;

public class RichAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_VIEW_TYPE = 0;
    public static final int HEADER_VIEW_TYPE = 1;
    private List<String> dataset;

    @Override
    public int getItemViewType(int position) {
        // ■부터 시작되면 헤더로 판정
        if (dataset.get(position).startsWith("■")) {
            return HEADER_VIEW_TYPE;
        }
        return ITEM_VIEW_TYPE;
    }

    // 이번에는 생성자로 데이터를 넘겨준다
    public RichAdapter(List<String> myDataset) {
        dataset = myDataset;
    }

    // 아이템용 ViewHolder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ItemViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.simple_text_view);
        }
    }

    // 헤더용 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleTextView;
        public final TextView detailTextView;

        public HeaderViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.title_text_view);
            detailTextView = (TextView) v.findViewById(R.id.detail_text_view);
        }
    }

    // 새로운 ViewHolder를 만든다(LayoutManager에서 호출한다)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view;
        // 새로 View를 만든다
        switch (viewType) {
            // 컨텐츠용 ViewHolder 작성
            case ITEM_VIEW_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.simple_row, parent, false);
                return new ItemViewHolder(view);
            }
            // 헤더용 ViewHolder 작성
            case HEADER_VIEW_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_row, parent, false);
                return new HeaderViewHolder(view);
            }
            default:
                throw new RuntimeException("예측되지 않는 ViewType입니다");
        }
    }

    // View 안의 데이터를 변경한다(LayoutManager에서 호출한다)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 설정할 데이터를 가져온다
        String text = dataset.get(position);
        // ViewHolder의 View 안의 데이터를 변경한다
        switch (holder.getItemViewType()) {
            case ITEM_VIEW_TYPE: {
                // 아이템용으로 그대로 문자열을 설정한다
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.textView.setText(text);
                break;
            }
            case HEADER_VIEW_TYPE: {
                // 헤더라면 타이틀용 문자열을 설정한다
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.titleTextView.setText("시리즈:" + text);
                headerViewHolder.detailTextView.setText(text + " 시리즈입니다");
                break;
            }
        }
    }

    // 데이터 개수를 반환한다(LayoutManager에서 호출된다)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}