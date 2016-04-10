package szu.wifichat.android.activity.maintabs;

import szu.wifichat.android.R;
import szu.wifichat.android.util.xml.UiUtil;
import szu.wifichat.android.view.HandyTextView;
import szu.wifichat.android.view.ScrollingTextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class NewsDetailActivity extends Activity {

	private WebView wv;

	private String title;
	private String description;

	private ImageButton iButton;
	private LinearLayout layoutHeader;
	private int headerWidth;

	private View newsHead;

	private Handler handler;

	/**
	 * 当前是否为夜黑
	 */
	private boolean isNight;

	private Runnable displayMenuRunnable = new Runnable() {
		public void run() {
			AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
			TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -headerWidth);
			AnimationSet set = new AnimationSet(true);
			set.setFillAfter(true);
			set.addAnimation(alphaAnimation);
			set.addAnimation(translateAnimation);
			newsHead.startAnimation(set);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_detail);

		initData();
		initView();
		initEvent();
		setHeaderTitle(getResources().getString(R.string.news_reading), title);
		disPlayNews();

//		handler.postDelayed(displayMenuRunnable, 3 * 1000);

	}

	private void initEvent() {
		iButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isNight = !isNight;
				disPlayNews();
				setButtonBg();
			}
		});

//		wv.setOnTouchListener(new View.OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_UP:
//					
//					AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//					TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -headerWidth, 0);
//					AnimationSet set = new AnimationSet(true);
//					set.setFillAfter(true);
//					set.addAnimation(alphaAnimation);
//					set.addAnimation(translateAnimation);
//					newsHead.startAnimation(set);
//					handler.postDelayed(displayMenuRunnable, 2 * 1000);
//					
//					break;
//				}
//				return false;
//			}
//		});
	}

	private void initData() {
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");
		handler = new Handler();
	}

	private void initView() {
		wv = (WebView) findViewById(R.id.web_detail_wv);
		iButton = (ImageButton) findViewById(R.id.news_ib_style_change);
		layoutHeader = (LinearLayout) findViewById(R.id.header_layout_title);
		newsHead = findViewById(R.id.news_detail_header);
		headerWidth = newsHead.getHeight();
		wv = (WebView) findViewById(R.id.web_detail_wv);
	}

	/**
	 * 在webView中显示新闻
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void disPlayNews() {
		String data = "<h1>" + title + "</h1><body>" + description + "</body>";

		// 如果有css样式表，可以将其字符串添加在data的开头即可
		if (isNight) {
			data = UiUtil.NEWS_STYLE_NIGHT + data;
		} else {
			data = UiUtil.NEWS_STYLE + data;
		}
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setAppCacheEnabled(true);

		wv.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
	}

	/**
	 * 给标题栏设置标题
	 * 
	 * @param title
	 * @param subTitle
	 */
	private void setHeaderTitle(String title, String subTitle) {
		layoutHeader.setVisibility(View.VISIBLE);
		ScrollingTextView tvTitle = (ScrollingTextView) layoutHeader.getChildAt(0);
		HandyTextView tvSubTitle = (HandyTextView) layoutHeader.getChildAt(1);

		tvTitle.setText(title);
		tvSubTitle.setText(subTitle);

	}

	/**
	 * 更改日夜间模式的背景图
	 */
	private void setButtonBg() {
		if (isNight) {
			iButton.setImageResource(R.drawable.img_2);
		} else {
			iButton.setImageResource(R.drawable.img_1);
		}
	}

}
