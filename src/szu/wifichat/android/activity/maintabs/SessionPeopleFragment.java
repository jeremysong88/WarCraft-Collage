package szu.wifichat.android.activity.maintabs;

import java.util.List;

import szu.wifichat.android.BaseApplication;
import szu.wifichat.android.BaseFragment;
import szu.wifichat.android.activity.message.ChatActivity;
import szu.wifichat.android.adapter.NearByPeopleAdapter;
import szu.wifichat.android.entity.Users;
import szu.wifichat.android.view.MoMoRefreshListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import szu.wifichat.android.R;

@SuppressLint("ValidFragment")
public class SessionPeopleFragment extends BaseFragment implements OnItemClickListener {

	/**
	 * 未读消息用户列表
	 */
	private static List<Users> mSessionPeoples;

	/**
	 * 玩家listView（未读短消息的玩家）
	 */
	private MoMoRefreshListView mMmrlvList;
	private NearByPeopleAdapter mAdapter;
	private TextView mTvListEmpty;

	public SessionPeopleFragment() {
		super();
	}

	public SessionPeopleFragment(BaseApplication application, Activity activity, Context context) {
		super(application, activity, context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_nearbypeople, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		mMmrlvList = (MoMoRefreshListView) findViewById(R.id.nearby_people_mmrlv_list);
		mTvListEmpty = (TextView) findViewById(R.id.nearby_people_mmrlv_empty);
	}

	@Override
	protected void initEvents() {
		mMmrlvList.setOnItemClickListener(this);
		// listView为空时显示的view
		mMmrlvList.setEmptyView(mTvListEmpty);
	}

	@Override
	protected void init() {
		// 获取未读消息用户队列
		mSessionPeoples = mApplication.getUnReadPeopleList();
		mAdapter = new NearByPeopleAdapter(mApplication, mContext, mSessionPeoples);
		mMmrlvList.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int position = (int) arg3;
		Users people = mSessionPeoples.get(position);
		mApplication.removeUnReadPeople(people); // 移除未读用户
		Intent intent = new Intent(mContext, ChatActivity.class);
		intent.putExtra(Users.ENTITY_PEOPLE, people);
		startActivity(intent);
	}

	/** 刷新用户在线列表UI **/
	public void refreshAdapter() {
		mSessionPeoples = mApplication.getUnReadPeopleList();
		mAdapter.setData(mSessionPeoples);
		mAdapter.notifyDataSetChanged();
		mMmrlvList.setSelection(0);
	}

	/** 设置显示起始位置 **/
	public void setLvSelection(int position) {
		mMmrlvList.setSelection(position);
	}
}
