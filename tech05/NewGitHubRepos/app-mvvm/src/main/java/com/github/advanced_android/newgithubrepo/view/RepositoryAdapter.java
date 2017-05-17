package com.github.advanced_android.newgithubrepo.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.advanced_android.newgithubrepo.R;
import com.github.advanced_android.newgithubrepo.contract.RepositoryListViewContract;
import com.github.advanced_android.newgithubrepo.databinding.RepoItemBinding;
import com.github.advanced_android.newgithubrepo.model.GitHubService;
import com.github.advanced_android.newgithubrepo.viewmodel.RepositoryItemViewModel;

import java.util.List;

/**
 * RecyclerView에서 리포지토리 목록을 표시하기 위한 Adapter 클래스
 * 이 클래스에 의해 RecyclerView 아이템의 뷰를 생성하고 뷰에 데이터를 넣는다
 */
public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepoViewHolder> {
  private final RepositoryListViewContract view;
  private final Context context;
  private List<GitHubService.RepositoryItem> items;

  public RepositoryAdapter(Context context, RepositoryListViewContract view) {
    this.context = context;
    this.view = view;
  }

  /**
   * 리포지토리의 데이터를 설정해서 갱신한다
   * @param items
   */
  public void setItemsAndRefresh(List<GitHubService.RepositoryItem> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  public GitHubService.RepositoryItem getItemAt(int position) {
    return items.get(position);
  }

  /**
   * RecyclerView의 아이템의 뷰 작성과 뷰를 보존할 ViewHolder를 생성
   */
  @Override
  public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RepoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.repo_item, parent, false);
    binding.setViewModel(new RepositoryItemViewModel(view));
    return new RepoViewHolder(binding.getRoot(), binding.getViewModel());
  }

  /**
   * onCreateViewHolder에서 만든 ViewHolder의 뷰에
   * setItemsAndRefresh(items)에서 설정된 데이터를 넣는다
   */
  @Override
  public void onBindViewHolder(final RepoViewHolder holder, final int position) {
    final GitHubService.RepositoryItem item = getItemAt(position);
    holder.loadItem(item);

  }

  @Override
  public int getItemCount() {
    if (items == null) {
      return 0;
    }
    return items.size();
  }

  /**
   * 뷰를 보존해 둘 클래스
   * 여기서는 ViewModel을 가진다
   */
  static class RepoViewHolder extends RecyclerView.ViewHolder {
    private final RepositoryItemViewModel viewModel;

    public RepoViewHolder(View itemView, RepositoryItemViewModel viewModel) {
      super(itemView);
      this.viewModel = viewModel;
    }

    public void loadItem(GitHubService.RepositoryItem item) {
      viewModel.loadItem(item);
    }
  }


}
