package com.github.advanced_android.newgithubrepo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.Calendar;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 리포지토리 목록을 표시하는 Activity
 */
public class RepositoryListActivity extends AppCompatActivity implements RepositoryAdapter.OnRepositoryItemClickListener {

  private Spinner languageSpinner;
  private ProgressBar progressBar;
  private CoordinatorLayout coordinatorLayout;

  private RepositoryAdapter repositoryAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_repository_list);

    // View를 설정한다
    setupViews();
  }

  /**
   * 목록 등 화면 요소를 만든다
   */
  private void setupViews() {
    // 툴바 설정
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Recycler View
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_repos);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    repositoryAdapter = new RepositoryAdapter((Context) this, (RepositoryAdapter.OnRepositoryItemClickListener) this);
    recyclerView.setAdapter(repositoryAdapter);

    // ProgressBar
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);

    // SnackBar 표시에 이용한다
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

    // Spinner
    languageSpinner = (Spinner) findViewById(R.id.language_spinner);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    adapter.addAll("java", "objective-c", "swift", "groovy", "python", "ruby", "c");
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    languageSpinner.setAdapter(adapter);
    languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 선택시 뿐만 아니라 처음에도 호출된다
        String language = (String) languageSpinner.getItemAtPosition(position);
        loadRepositories(language);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }

    });
  }


  /**
   * 지난 1주일간 만들어진 라이브러리의 인기순으로 가져온다
   * @param language 가져올 프로그래밍 언어
   */
  private void loadRepositories(String language) {
    // 로딩 중이므로 진행바를 표시한다
    progressBar.setVisibility(View.VISIBLE);

    // 일주일전 날짜의 문자열 지금이 2016-10-27이면 2016-10-20 이라는 문자열을 얻는다
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -7);
    String text = DateFormat.format("yyyy-MM-dd", calendar).toString();

    // Retrofit를 이용해 서버에 액세스한다
    final NewGitHubReposApplication application = (NewGitHubReposApplication) getApplication();
    // 지난 일주일간 만들어지고 언어가 language인 것을 요청으로 전달한다
    Observable<GitHubService.Repositories> observable = application.getGitHubService().listRepos("language:" + language + " " + "created:>" + text);
    // 입출력(IO)용 스레드로 통신하고, 메인스레드에서 결과를 수신하게 한다
    observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GitHubService.Repositories>() {
      @Override
      public void onNext(GitHubService.Repositories repositories) {
        // 로딩이 끝났으므로 진행바를 표시하지 않는다
        progressBar.setVisibility(View.GONE);
        // 가져온 아이템을 표시하고자 RecyclerView에 아이템을 설정하고 갱신한다
        repositoryAdapter.setItemsAndRefresh(repositories.items);
      }

      @Override
      public void onError(Throwable e) {
        // 통신 실패 시에 호출된다
        // 여기서는 스낵바를 표시한다(아래에 표시되는 바)
        Snackbar.make(coordinatorLayout, "읽어올 수 없습니다.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
      }

      @Override
      public void onCompleted() {
        // 아무것도 하지 않는다
      }
    });
  }

  /**
   * 상세화면을 표시한다
   * @see RepositoryAdapter.OnRepositoryItemClickListener#onRepositoryItemClickListener
   */
  @Override
  public void onRepositoryItemClick(GitHubService.RepositoryItem item) {
    DetailActivity.start(this, item.full_name);
  }
}
