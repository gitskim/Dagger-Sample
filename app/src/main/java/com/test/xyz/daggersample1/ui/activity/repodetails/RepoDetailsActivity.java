package com.test.xyz.daggersample1.ui.activity.repodetails;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.test.xyz.daggersample1.R;
import com.test.xyz.daggersample1.di.DaggerApplication;
import com.test.xyz.daggersample1.ui.activity.base.BaseActivity;
import com.test.xyz.daggersample1.ui.presenter.details.RepoDetailsPresenter;
import com.test.xyz.daggersample1.ui.util.CommonConstants;
import com.test.xyz.daggersample1.ui.util.CommonUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepoDetailsActivity extends BaseActivity implements RepoDetailsView {
    static String TAG = RepoDetailsActivity.class.getName();

    @Inject
    RepoDetailsPresenter presenter;

    @InjectView(R.id.repoDetails)
    TextView repoDetails;

    @InjectView(R.id.repoTitle)
    TextView repoTitle;

    String repoItemTitle = "";

    @Override
    protected void onCreateActivity() {
        setContentView(R.layout.activity_repodetails);

        ButterKnife.inject(this);

        Log.i(TAG, "Getting custom component inside RepoDetailsActivity");

        DaggerApplication.get(this).getAppComponent()
                                   .plus(new RepoDetailsActivityModule(this))
                                   .inject(this);

        startProgress();

        Intent intent = getIntent();
        repoItemTitle = intent.getStringExtra(CommonConstants.REPO_DESC);

        presenter.requestRepoDetails(CommonConstants.DEFAULT_USER_NAME, repoItemTitle);
    }

    @Override
    public void showRepoDetails(final String description) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                endProgress();
                repoTitle.setText(repoItemTitle);
                repoDetails.setText(description);
            }
        });
    }

    @Override
    public void showError(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                endProgress();
                CommonUtils.showToastMessage(RepoDetailsActivity.this, errorMessage);
                repoDetails.setText(R.string.repo_details_ret_error);
            }
        });
    }

    @Override
    protected void onPause() {
        // All dialogs must be closed when activity is paused to avoid leakage ...
        // Reference: http://stackoverflow.com/questions/2850573/activity-has-leaked-window-that-was-originally-added
        closeAllDialogs();
        super.onPause();
    }

    private void closeAllDialogs() {
        endProgress();
    }
}
