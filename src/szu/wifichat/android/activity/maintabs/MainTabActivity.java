package szu.wifichat.android.activity.maintabs;

import szu.wifichat.android.BaseApplication;
import szu.wifichat.android.view.HandyTextView;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import szu.wifichat.android.R;

/**
 * 主页面
 * 
 * @date 2015-3-12
 */
@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity implements OnTabChangeListener {
	protected static boolean isTabActive;
	private TabHost mTabHost;

	/**
	 * 未读消息数量
	 */
	private static HandyTextView mHtvSessionNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintabs_menu);
		initTabs();
		initViews();
		initEvents();
	}

	@Override
	public void onResume() {
		super.onResume();
		isTabActive = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		isTabActive = false;
	}

	private void initTabs() {
		mTabHost = getTabHost(); // 从TabActivity上面获取放置Tab的TabHost
		LayoutInflater inflater = LayoutInflater.from(MainTabActivity.this);

		// 附近
		// common_bottombar_tab_nearby存放该Tab布局，inflate可将xml实例化成View
		View nearbyView = inflater.inflate(R.layout.common_bottombar_tab_nearby, null);

		// 创建TabHost.TabSpec的对象，并设置该对象的tag，最后关联该Tab的View
		TabHost.TabSpec nearbyTabSpec = mTabHost.newTabSpec(NearByActivity.class.getName()).setIndicator(nearbyView);
		// 设置跳转activity
		nearbyTabSpec.setContent(new Intent(MainTabActivity.this, NearByActivity.class));
		mTabHost.addTab(nearbyTabSpec); // 添加该Tab

		// 消息
		View sessionListView = inflater.inflate(R.layout.common_bottombar_tab_chat, null);
		TabHost.TabSpec sessionListTabSpec = mTabHost.newTabSpec(SessionListActivity.class.getName()).setIndicator(sessionListView);
		sessionListTabSpec.setContent(new Intent(MainTabActivity.this, SessionListActivity.class));
		mTabHost.addTab(sessionListTabSpec);

		// 新闻
		View newsListView = inflater.inflate(R.layout.common_bottombar_tab_news, null);
		TabHost.TabSpec newsListTabSpec = mTabHost.newTabSpec(NewsListActivity.class.getName()).setIndicator(newsListView);
		newsListTabSpec.setContent(new Intent(MainTabActivity.this, NewsListActivity.class));
		mTabHost.addTab(newsListTabSpec);

		// 设置
		View userSettingView = inflater.inflate(R.layout.common_bottombar_tab_profile, null);
		TabHost.TabSpec userSettingTabSpec = mTabHost.newTabSpec(SettingActivity.class.getName()).setIndicator(userSettingView);
		userSettingTabSpec.setContent(new Intent(MainTabActivity.this, SettingActivity.class));
		mTabHost.addTab(userSettingTabSpec);
	}

	private void initEvents() {
	}

	private void initViews() {
		mHtvSessionNumber = (HandyTextView) findViewById(R.id.tab_chat_number);
	}

	public static boolean getIsTabActive() {
		return isTabActive;
	}

	/**
	 * 隐藏显示未读数量显示控件
	 */
	public static void sendEmptyMessage() {
		if (isTabActive)
			handler.sendEmptyMessage(0);
	}

	/**
	 * 用于显示和隐藏未读短信数量显示控件
	 */
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int unReadPeopleSize = BaseApplication.getInstance().getUnReadPeopleSize();
			switch (unReadPeopleSize) { // 判断人数作不同处理
			case 0: // 为0，隐藏数字提示
				mHtvSessionNumber.setVisibility(View.GONE);
				break;

			default: // 不为0，则显示未读数
				mHtvSessionNumber.setText(String.valueOf(unReadPeopleSize));
				mHtvSessionNumber.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	@Override
	public void onTabChanged(String tabId) {

	}
}
