package com.github.advanced_android.newgithubrepo.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.github.advanced_android.newgithubrepo.R;
import com.github.advanced_android.newgithubrepo.contract.RepositoryListContract;
import com.github.advanced_android.newgithubrepo.model.GitHubService;
import com.github.advanced_android.newgithubrepo.presenter.RepositoryListPresenter;

/**
 * 리포지토리 목록을 표시하는 Activity
 * MVP의 View 역할을 가진다
 */
public class RepositoryListActivity extends AppCompatActivity implements RepositoryAdapter.OnRepositoryItemClickListener,
    RepositoryListContract.View {

  private Spinner languageSpinner;
  private ProgressBar progressBar;
  private CoordinatorLayout coordinatorLayout;

  private RepositoryAdapter repositoryAdapter;

  private RepositoryListContract.UserActions repositoryListPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_repository_list);

    // View를 설정
    setupViews();

    // ① Presenter의 인스턴스를 생성
    final GitHubService gitHubService = ((NewGitHubReposApplication) getApplication()).getGitHubService();
    repositoryListPresenter = new RepositoryListPresenter((RepositoryListContract.View) this, gitHubService);
  }

  /**
   * 목록 등의 화면 요소를 만든다
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

    // SnackBar 표시로 이용한다
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
        //  스피너의 선택 내용이 바뀌면 호출된다
        String language = (String) languageSpinner.getItemAtPosition(position);
        // ② Presenter에 프로그래밍 언어를 선택했다고 알린다
        repositoryListPresenter.selectLanguage(language);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }

    });
  }

  /**
   * RecyclerView에서 클릭됐다
   * @see RepositoryAdapter.OnRepositoryItemClickListener#onRepositoryItemClickListener
   */
  @Override
  public void onRepositoryItemClick(GitHubService.RepositoryItem item) {
    repositoryListPresenter.selectRepositoryItem(item);
  }


  // =====RepositoryListContract.View 구현=====
  // 이곳에서 Presenter로부터 지시를 받아 View의 변경 등을 한다

  @Override
  public void startDetailActivity(String full_name) {
    DetailActivity.start(this, full_name);
  }


  @Override
  public String getSelectedLanguage() {
    return (String) languageSpinner.getSelectedItem();
  }


  @Override
  public void showProgress() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgress() {
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void showRepositories(GitHubService.Repositories repositories) {
    // ③ 리포지토리 목록을 Adapter에 설정한다
    repositoryAdapter.setItemsAndRefresh(repositories.items);
  }

  @Override
  public void showError() {
    Snackbar.make(coordinatorLayout, "읽을 수 없습니다", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
  }

}
