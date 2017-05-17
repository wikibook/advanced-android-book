package com.github.advanced_android.newgithubrepo.viewmodel;

import android.databinding.ObservableInt;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;

import com.github.advanced_android.newgithubrepo.contract.RepositoryListViewContract;
import com.github.advanced_android.newgithubrepo.model.GitHubService;

import java.util.Calendar;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * MVVM의 ViewModel 역할을 하는 클래스
 */
public class RepositoryListViewModel {
  public final ObservableInt progressBarVisibility = new ObservableInt(View.VISIBLE);
  private final RepositoryListViewContract repositoryListView;
  private final GitHubService gitHubService;

  public RepositoryListViewModel(RepositoryListViewContract repositoryListView, GitHubService gitHubService) {
    this.repositoryListView = repositoryListView;
    this.gitHubService = gitHubService;
  }

  public void onLanguageSpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {
    //  스피너의 선택 내용이 바뀌면 호출된다
    loadRepositories((String) parent.getItemAtPosition(position));
  }

  /**
   * 지난 일주일간 만들어진 라이브러리를 인기순으로 가져온다
   */
  private void loadRepositories(String langugae) {
    // 로딩 중이므로 진행바를 표시한다
    progressBarVisibility.set(View.VISIBLE);

    // 일주일 전 날짜 문자열 지금이 2016-10-27이라면 2016-10-20이라는 문자열을 얻는다
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -7);
    String text = DateFormat.format("yyyy-MM-dd", calendar).toString();

    // Retrofit을 이용해 서버에 액세스한다

    // 지난 일주일간 만들어졌고 언어가 language인 것을 쿼리로 전달한다
    Observable<GitHubService.Repositories> observable = gitHubService.listRepos("language:" + langugae + " " + "created:>" + text);
    // 입출력(IO)용 스레드로 통신하고, 메인스레드로 결과를 받도록 한다
    observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GitHubService.Repositories>() {
      @Override
      public void onNext(GitHubService.Repositories repositories) {
        // 로딩이 끝났으므로 진행바를 표시하지 않는다
        progressBarVisibility.set(View.GONE);
        // 가져온 아이템을 표시하고자, RecyclerView에 아이템을 설정해 갱신한다
        repositoryListView.showRepositories(repositories);
      }

      @Override
      public void onError(Throwable e) {
        // 통신에 실패하면 호출된다
        // 여기서는 스낵바를 표시한다(아래에 표시되는 바)
        repositoryListView.showError();
      }

      @Override
      public void onCompleted() {
        // 아무것도 하지 않는다
      }
    });
  }
}

