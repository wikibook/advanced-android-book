package com.github.advanced_android.newgithubrepo.contract;

import com.github.advanced_android.newgithubrepo.model.GitHubService;

/**
 * 각자의 역할이 가진 Contract(계약)를 정의해 둘 인터페이스
 */
public interface RepositoryListContract {

  /**
   * MVP의 View가 구현할 인터페이스
   * Presenter가 View를 조작할 때 이용한다
   */
  interface View {
    String getSelectedLanguage();
    void showProgress();
    void hideProgress();
    void showRepositories(GitHubService.Repositories repositories);
    void showError();
    void startDetailActivity(String fullRepositoryName);
  }

  /**
   * MVP의 Presenter가 구현할 인터페이스
   * View를 클릭했을 때 등 View가 Presenter에 알릴 때 이용한다
   */
  interface UserActions {
    void selectLanguage(String language);
    void selectRepositoryItem(GitHubService.RepositoryItem item);
  }

}


