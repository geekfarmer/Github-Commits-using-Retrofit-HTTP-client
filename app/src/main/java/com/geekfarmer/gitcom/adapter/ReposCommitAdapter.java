package com.geekfarmer.gitcom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.geekfarmer.gitcom.R;
import com.geekfarmer.gitcom.RetrofitSampleFragment;
import com.geekfarmer.gitcom.model.Commits;
import com.geekfarmer.gitcom.model.Repos;

import java.util.List;

/**
 * Created by geekfarmer
 */
public class ReposCommitAdapter extends BaseExpandableListAdapter {
    
    private Context mContext;
    private List<Repos> mReposList;
    private List<List<Commits>> mCommitList;
    
    public ReposCommitAdapter(RetrofitSampleFragment context, List<Repos> reposList, List<List<Commits>> commitList) {
        mContext = (Context) context;
        mReposList = reposList;
        mCommitList = commitList;
    }
    
    @Override
    public int getGroupCount() {
        return mReposList.size();
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        return mCommitList.get(groupPosition).size();
    }
    
    @Override
    public Repos getGroup(int groupPosition) {
        return mReposList.get(groupPosition);
    }
    
    @Override
    public Commits getChild(int groupPosition, int childPosition) {
        return mCommitList.get(groupPosition).get(childPosition);
    }
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    @Override
    public boolean hasStableIds() {
        return true;
    }
    

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        
        View v = LayoutInflater
                .from(mContext)
                .inflate(R.layout.repository_list_cell, null);
        

        Repos repos = mReposList.get(groupPosition);
        

        ((TextView) v.findViewById(R.id.repository_name)).setText(repos.getName());
        

        ((TextView) v.findViewById(R.id.repository_description)).setText(repos.getDescription());
        

        ((TextView) v.findViewById(R.id.repository_url)).setText(repos.getUrl());
        
        return v;
    }
    

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        
        View v = LayoutInflater
                .from(mContext)
                .inflate(R.layout.commit_list_cell, null);
        

        Commits commit = mCommitList.get(groupPosition).get(childPosition);
        

        ((TextView) v.findViewById(R.id.commit_id)).setText(commit.getSha());
        

        ((TextView) v.findViewById(R.id.committer_name)).setText(commit.getCommiterName());
        

        ((TextView) v.findViewById(R.id.committer_email)).setText("(" + commit.getCommiterEmail() + ")");
        

        ((TextView) v.findViewById(R.id.commit_date)).setText(commit.getCommitDate());
        

        ((TextView) v.findViewById(R.id.commit_description)).setText(commit.getMessage());
        
        return v;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}