package szu.wifichat.android.activity.maintabs;

import szu.wifichat.android.R;
import szu.wifichat.android.view.HeaderLayout;
import szu.wifichat.android.view.HeaderLayout.HeaderStyle;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;

public class NewsListActivity extends TabItemActivity {

	private HeaderLayout mHeaderLayout;
	private NewsFragment mNewsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newslist);
		initViews();
		init();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 隐藏数字提示(未读短消息数)
		MainTabActivity.sendEmptyMessage();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 隐藏数字提示(未读短消息数)
		MainTabActivity.sendEmptyMessage();
		mNewsFragment.refreshAdapter();
	}

	@Override
	protected void initViews() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.session_header);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(getString(R.string.maintab_text_news), null);
	}

	@Override
	protected void initEvents() {

	}

	@Override
	protected void init() {
		mNewsFragment = new NewsFragment(mApplication, this, this);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.session_layout_content, mNewsFragment).commit();
	}

	@Override
	public void processMessage(Message msg) {
		mNewsFragment.refreshAdapter();
	}

}
