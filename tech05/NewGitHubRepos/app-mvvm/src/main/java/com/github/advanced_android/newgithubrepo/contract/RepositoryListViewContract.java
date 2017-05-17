package com.github.advanced_android.newgithubrepo.contract;

import com.github.advanced_android.newgithubrepo.model.GitHubService;

/**
 * 리포지터리 목록 화면이 가진 Contract(계약)를 정의해두는 인터페이스
 * <p>
 * ViewModel이 직접 Activity를 참조하지 않도록 인터페이스로 명확히 나눈다.
 */
public interface RepositoryListViewContract {
  void showRepositories(GitHubService.Repositories repositories);

  void showError();

  void startDetailActivity(String fullRepositoryName);
}


