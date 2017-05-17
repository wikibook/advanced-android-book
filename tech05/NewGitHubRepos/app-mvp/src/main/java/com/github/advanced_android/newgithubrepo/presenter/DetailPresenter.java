package com.github.advanced_android.newgithubrepo.presenter;

import com.github.advanced_android.newgithubrepo.contract.DetailContract;
import com.github.advanced_android.newgithubrepo.model.GitHubService;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DetailPresenter implements DetailContract.UserActions {
  final DetailContract.View detailView;
  private final GitHubService gitHubService;
  private GitHubService.RepositoryItem repositoryItem;

  public DetailPresenter(DetailContract.View detailView, GitHubService gitHubService) {
    this.detailView = detailView;
    this.gitHubService = gitHubService;
  }

  @Override
  public void prepare() {
    loadRepositories();
  }

  /**
   * 하나의 리포지토리에 관한 정보를 가져온다
   * 기본적으로 API 액세스 방법은 RepositoryListActivity#loadRepositories(String)와 같다
   */
  private void loadRepositories() {
    String fullRepoName = detailView.getFullRepositoryName();
    // 리포지토리 이름을 /로 분할한다
    final String[] repoData = fullRepoName.split("/");
    final String owner = repoData[0];
    final String repoName = repoData[1];
    gitHubService
        .detailRepo(owner, repoName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<GitHubService.RepositoryItem>() {
          @Override
          public void call(GitHubService.RepositoryItem response) {
            repositoryItem = response;
            detailView.showRepositoryInfo(response);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            detailView.showError("읽을 수 없습니다.");
          }
        });
  }

  @Override
  public void titleClick() {
    try {
      detailView.startBrowser(repositoryItem.html_url);
    } catch (Exception e) {
      detailView.showError("링크를 열수 없습니다.");
    }
  }

}
