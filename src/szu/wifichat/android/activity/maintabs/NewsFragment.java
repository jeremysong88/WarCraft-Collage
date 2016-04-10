package szu.wifichat.android.activity.maintabs;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import szu.wifichat.android.BaseApplication;
import szu.wifichat.android.BaseFragment;
import szu.wifichat.android.R;
import szu.wifichat.android.adapter.NewsAdapter;
import szu.wifichat.android.entity.MyNews;
import szu.wifichat.android.util.xml.XmlUtils;
import szu.wifichat.android.view.MoMoRefreshListView;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class NewsFragment extends BaseFragment implements OnItemClickListener {

	/**
	 * 玩家listView（未读短消息的玩家）
	 */
	private MoMoRefreshListView mMmrlvList;
	private TextView mTvListEmpty;

	private List<MyNews> mNews;

	private NewsAdapter mAdapter;

	public NewsFragment() {
		super();
	}

	public NewsFragment(BaseApplication application, Activity activity, Context context) {
		super(application, activity, context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_news, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {

		initData();

		mMmrlvList = (MoMoRefreshListView) findViewById(R.id.news_mmrlv_list);
		mTvListEmpty = (TextView) findViewById(R.id.news_mmrlv_empty);

		mAdapter = new NewsAdapter(getActivity(), mNews);
		mMmrlvList.setAdapter(mAdapter);

		WindowManager windowManager = (WindowManager) getActivity().getSystemService(Service.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		int screenWidth = outMetrics.widthPixels;

		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1.0f);
		TranslateAnimation translateAnimation = new TranslateAnimation(-screenWidth, 0, 0, 0);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(translateAnimation);
		set.addAnimation(alphaAnimation);
		set.setDuration(1 * 1000);

		LayoutAnimationController lac = new LayoutAnimationController(set);
		mMmrlvList.setLayoutAnimation(lac);

	}

	private void initData() {
		try {
			InputStream is = getActivity().getAssets().open("qqmail_rss.xml");
			XmlUtils mXmlUtils = new XmlUtils();
			mNews = mXmlUtils.parserNews(is);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 点击事件
		MyNews news = mNews.get(position);
		Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
		intent.putExtra("title", news.getTitle());
		intent.putExtra("description", news.getDescription());
		startActivity(intent);
	}

	/** 刷新新闻在线列表UI **/
	public void refreshAdapter() {
	}

	/** 设置显示起始位置 **/
	public void setLvSelection(int position) {
		mMmrlvList.setSelection(position);
	}

	/**
	 * 方法测试，访问web数据的方法。正常数据来源于网络访问
	 */
	@SuppressWarnings("unused")
	private void visitWeb() {

		new Thread() {
			@Override
			public void run() {
				String url = "";
				// Method 1:
				try {
					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
					connection.setReadTimeout(5 * 1000);
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5 * 1000);
					connection.setRequestProperty("name", "Dear Wang");

					InputStream is = connection.getInputStream();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Method 2:
				try {
					HttpRequest request = new DefaultHttpRequestFactory().newHttpRequest("GET", url);

					HttpClient client = new DefaultHttpClient();
					client.execute(null, request);

					HttpGet requestGet = new HttpGet(url);
					client.execute(requestGet);

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
