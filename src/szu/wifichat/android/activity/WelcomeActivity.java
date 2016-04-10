package szu.wifichat.android.activity;

import szu.wifichat.android.BaseActivity;
import szu.wifichat.android.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity implements AnimationListener {

	private ImageView logoView;
	private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		initViews();
		initAnim();
		initEvents();
	}

	@Override
	protected void initViews() {
		logoView = (ImageView) findViewById(R.id.welcome_logo_iv);
	}

	private void initAnim() {
		animation = AnimationUtils.loadAnimation(this, R.anim.logo_in_anim);
		animation.setInterpolator(new DecelerateInterpolator());
		logoView.startAnimation(animation);
	}

	@Override
	protected void initEvents() {
		animation.setAnimationListener(this);
	}

	@Override
	public void processMessage(Message msg) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {

		new Handler().postDelayed(new Runnable() {
			public void run() {
				// startActivity(LoginActivity.class);

				SharedPreferences sp = WelcomeActivity.this.getSharedPreferences("LocalUserInfo", MODE_PRIVATE);
				String nickName = sp.getString("Nickname", "");
				if (TextUtils.isEmpty(nickName)) {
					startActivity(WhatsNew.class);
				} else {
					startActivity(LoginActivity.class);
				}

				WelcomeActivity.this.finish();
			}
		}, (1 * 1000));

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}
}
