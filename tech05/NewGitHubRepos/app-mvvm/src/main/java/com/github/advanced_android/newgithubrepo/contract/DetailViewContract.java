package com.github.advanced_android.newgithubrepo.contract;

/**
 * 상세 화면의 뷰가 가진 Contract(계약)을 정의해 두는 인터페이스
 * <p>
 * ViewModel이 직접 Activity를 참조하지 않도록 인터페이스로 명확히 나눈다
 */
public interface DetailViewContract {

  String getFullRepositoryName();

  void startBrowser(String url);

  void showError(String message);
}


