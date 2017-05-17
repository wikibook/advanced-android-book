package com.github.advanced_android.newgithubrepo.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.advanced_android.newgithubrepo.R;
import com.github.advanced_android.newgithubrepo.contract.DetailContract;
import com.github.advanced_android.newgithubrepo.model.GitHubService;
import com.github.advanced_android.newgithubrepo.presenter.DetailPresenter;

/**
 * 상세화면을 표시하는 Activity
 */
public class DetailActivity extends AppCompatActivity implements DetailContract.View {
  private static final String EXTRA_FULL_REPOSITORY_NAME = "EXTRA_FULL_REPOSITORY_NAME";
  private TextView fullNameTextView;
  private TextView detailTextView;
  private TextView repoStarTextView;
  private TextView repoForkTextView;
  private ImageView ownerImage;
  private DetailContract.UserActions detailPresenter;
  private String fullRepoName;

  /**
   * DetailActivity를 시작하는 메소드
   * @param fullRepositoryName 표시하고 싶은 리포지토리 이름(google/iosched 등)
   */
  public static void start(Context context, String fullRepositoryName) {
    final Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(EXTRA_FULL_REPOSITORY_NAME, fullRepositoryName);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    final Intent intent = getIntent();
    fullRepoName = intent.getStringExtra(EXTRA_FULL_REPOSITORY_NAME);

    fullNameTextView = (TextView) DetailActivity.this.findViewById(R.id.fullname);
    detailTextView = (TextView) findViewById(R.id.detail);
    repoStarTextView = (TextView) findViewById(R.id.repo_star);
    repoForkTextView = (TextView) findViewById(R.id.repo_fork);
    ownerImage = (ImageView) findViewById(R.id.owner_image);


    final GitHubService gitHubService = ((NewGitHubReposApplication) getApplication()).getGitHubService();
    detailPresenter = new DetailPresenter((DetailContract.View) this, gitHubService);
    detailPresenter.prepare();
  }

  @Override
  public String getFullRepositoryName() {
    return fullRepoName;
  }

  @Override
  public void showRepositoryInfo(final GitHubService.RepositoryItem response) {
    fullNameTextView.setText(response.full_name);
    detailTextView.setText(response.description);
    repoStarTextView.setText(response.stargazers_count);
    repoForkTextView.setText(response.forks_count);
    // 서버에서 이미지를 가져와 imageView에 넣는다
    Glide.with(DetailActivity.this)
         .load(response.owner.avatar_url)
         .asBitmap().centerCrop().into(new BitmapImageViewTarget(ownerImage) {
      @Override
      protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable circularBitmapDrawable =
            RoundedBitmapDrawableFactory.create(getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        ownerImage.setImageDrawable(circularBitmapDrawable);
      }
    });
    // 로고와 리포지토리 이름을 탭하면, 제작자의 GitHub 페이지를 브라우저로 연다
    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        detailPresenter.titleClick();
      }
    };
    fullNameTextView.setOnClickListener(listener);
    ownerImage.setOnClickListener(listener);
  }

  /**
   * @throws Exception
   */
  @Override
  public void startBrowser(String url) {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }

  @Override
  public void showError(String message) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            .show();
  }

}
