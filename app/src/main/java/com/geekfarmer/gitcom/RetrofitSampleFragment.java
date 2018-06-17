package com.geekfarmer.gitcom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geekfarmer.gitcom.adapter.ReposCommitAdapter;
import com.geekfarmer.gitcom.model.Commits;
import com.geekfarmer.gitcom.model.GitHubService;
import com.geekfarmer.gitcom.model.Repos;
import com.geekfarmer.gitcom.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by geekfarmer
 */
public class RetrofitSampleFragment extends AppCompatActivity {
    
    private static String TAG = RetrofitSampleFragment.class.getSimpleName();

    EditText mUserName;

    ExpandableListView mReposCommitList;
    Button getuserinfo, getrepo;

    TextView mUserInfo;

    private ReposCommitAdapter mReposCommitAdapter;

    private GitHubService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit_sample);
        Unbinder mUnbinder = ButterKnife.bind(this);
        mUserName = (EditText)findViewById(R.id.github_account_name);
        mReposCommitList = (ExpandableListView)findViewById(R.id.repos_commit_list);
        mUserInfo = (TextView)findViewById(R.id.user_info);

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(GitHubService.class);

        getuserinfo = (Button)findViewById(R.id.get_user_info);

        getuserinfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Please wait few second...",
                        Toast.LENGTH_SHORT).show();
                onGetUserInfoClicked();

            }

        });

        getrepo = (Button)findViewById(R.id.get_repository);
        getrepo.setEnabled(false);

        getrepo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            public void onClick(View vv) {
                getrepo.setEnabled(false);
                Toast.makeText(getApplicationContext(),
                        "Please wait few second...",
                        Toast.LENGTH_SHORT).show();
                onGetRepositoryClicked();

            }
        });
    }
    


    public void onGetUserInfoClicked() {
        String userName = mUserName.getText().toString();
        // String userName = "rails";

        Call<User> userCall = mService.getUser(userName);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();

                if(user != null){
                    String sb = ("User: " + user.getName() + "\n") +
                            "Company: " + user.getCompany() + "\n" +
                            "Email: " + user.getEmail() + "\n" +
                            "Bio: " + user.getBio() + "\n" +
                            "Created at: " + user.getCreatedAt() + "\n" +
                            "Updated at: " + user.getUpdatedAt() + "\n";

                    mUserInfo.setText(sb);

                    mUserInfo.setVisibility(View.VISIBLE);

                    mReposCommitList.setVisibility(View.GONE);
                    getrepo.setEnabled(true);

                }
                else {
                    getrepo.setEnabled(false);

                    mUserInfo.setText("");
                    Toast.makeText(getApplicationContext(),
                            "Http fetch error, Please restart app.",
                            Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mUserInfo.setText("");
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    public void onGetRepositoryClicked() {
        final List<Repos> reposList = new ArrayList<>();
        final List<List<Commits>> commitsList = new ArrayList<>();

        mReposCommitAdapter = new ReposCommitAdapter(this, reposList, commitsList);
        mReposCommitList.setAdapter(mReposCommitAdapter);

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {

                String userName = params[0];

                try {
                    Call<List<Repos>> reposCall = mService.listRepos(userName);
                    reposList.addAll(reposCall.execute().body());

                    for (Repos repos : reposList) {
                        String reposName = repos.getName();
                        Call<List<Commits>> commitsCall = mService.listCommit(userName, reposName);
                        commitsList.add(commitsCall.execute().body());
                    }


                } catch (IOException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Http fetch error, Please restart app.",
                            Toast.LENGTH_SHORT).show();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                mReposCommitAdapter.notifyDataSetChanged();

                mReposCommitList.setVisibility(View.VISIBLE);

                mUserInfo.setVisibility(View.GONE);
            }
        }.execute(mUserName.getText().toString());

    }
}