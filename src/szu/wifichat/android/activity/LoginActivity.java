package szu.wifichat.android.activity;

import java.util.Calendar;
import java.util.Date;

import szu.wifichat.android.BaseActivity;
import szu.wifichat.android.R;
import szu.wifichat.android.activity.maintabs.MainTabActivity;
import szu.wifichat.android.adapter.SimpleListDialogAdapter;
import szu.wifichat.android.dialog.SimpleListDialog;
import szu.wifichat.android.dialog.SimpleListDialog.onSimpleListItemClickListener;
import szu.wifichat.android.entity.Users;
import szu.wifichat.android.socket.udp.UDPSocketThread;
import szu.wifichat.android.sql.SqlDBOperate;
import szu.wifichat.android.sql.UserInfo;
import szu.wifichat.android.util.DateUtils;
import szu.wifichat.android.util.ImageUtils;
import szu.wifichat.android.util.SessionUtils;
import szu.wifichat.android.util.TextUtils;
import szu.wifichat.android.util.WifiUtils;
import szu.wifichat.android.view.HandyTextView;
import szu.wifichat.android.view.HeaderLayout;
import szu.wifichat.android.view.HeaderLayout.HeaderStyle;
import szu.wifichat.android.view.PagerScrollView;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 用户登陆类
 * 
 * @date 2015-3-12
 */
public class LoginActivity extends BaseActivity implements OnClickListener, onSimpleListItemClickListener, OnDateChangedListener {

	private static final String TAG = "SZU_loginActivity";
	/**
	 * 登陆年龄限制
	 */
	private static final int MAX_AGE = 80;
	private static final int MIN_AGE = 12;
	private static final String DEFAULT_DATA = "19920101";

	private HeaderLayout mHeaderLayout;
	/**
	 * 首次登陆主界面
	 */
	private PagerScrollView mLlayoutMain;
	private HandyTextView mHtvSelectOnlineState;
	private EditText mEtNickname;

	private HandyTextView mHtvConstellation;
	private HandyTextView mHtvAge;
	private DatePicker mDpBirthday;
	private Calendar mCalendar;
	private Date mMinDate;
	private Date mMaxDate;
	private Date mSelectDate;

	/**
	 * 二次登陆页面
	 */
	private LinearLayout mLlayoutExMain;
	private ImageView mImgExAvatar;
	private TextView mTvExNickmame;
	/**
	 * 性别根布局
	 */
	private LinearLayout mLayoutExGender;
	private ImageView mIvExGender;
	private HandyTextView mHtvExAge;
	/**
	 * 星座
	 */
	private TextView mTvExConstellation;
	/**
	 * 上次登录时间
	 */
	private TextView mTvExLogintime;

	private Button mBtnBack;
	private Button mBtnNext;
	private Button mBtnChangeUser;
	private RadioGroup mRgGender;
	/**
	 * 电话信息获取管理类
	 */
	private TelephonyManager mTelephonyManager;
	private SimpleListDialog mSimpleListDialog;

	private int mAge;
	private int mAvatar;
	private String mBirthday;
	private String mGender;
	private String mIMEI;
	/**
	 * 星座
	 */
	private String mConstellation;
	/**
	 * 上次登录时间
	 */
	private String mLastLogintime;
	private String mNickname = "";
	/**
	 * 默认登录状态
	 */
	private String mOnlineStateStr = "在线";
	/**
	 * 默认登录状态编号
	 */
	private int mOnlineStateInt = 0;
	private String[] mOnlineStateType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mTelephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		initViews();
		initData();
		initEvents();
	}

	@Override
	protected void initViews() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.login_header);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle("登录", null);

		mEtNickname = (EditText) findViewById(R.id.login_et_nickname);
		mHtvSelectOnlineState = (HandyTextView) findViewById(R.id.login_htv_onlinestate);
		mRgGender = (RadioGroup) findViewById(R.id.login_baseinfo_rg_gender);
		mHtvConstellation = (HandyTextView) findViewById(R.id.login_birthday_htv_constellation);
		mHtvAge = (HandyTextView) findViewById(R.id.login_birthday_htv_age);
		mDpBirthday = (DatePicker) findViewById(R.id.login_birthday_dp_birthday);

		mBtnBack = (Button) findViewById(R.id.login_btn_back);
		mBtnNext = (Button) findViewById(R.id.login_btn_next);
		mBtnChangeUser = (Button) findViewById(R.id.login_btn_changeUser);

		SharedPreferences mSharedPreferences = getSharedPreferences(GlobalSharedName, Context.MODE_PRIVATE);
		mNickname = mSharedPreferences.getString(Users.NICKNAME, "");

		// 若mNickname有内容，则读取本地存储的用户信息
		if (mNickname.length() != 0) {
			mTvExNickmame = (TextView) findViewById(R.id.login_tv_existName);
			mImgExAvatar = (ImageView) findViewById(R.id.login_img_existImg);
			mLayoutExGender = (LinearLayout) findViewById(R.id.login_layout_gender);
			mIvExGender = (ImageView) findViewById(R.id.login_iv_gender);
			mHtvExAge = (HandyTextView) findViewById(R.id.login_htv_age);
			mTvExConstellation = (TextView) findViewById(R.id.login_tv_constellation);
			mTvExLogintime = (TextView) findViewById(R.id.login_tv_lastlogintime);
			mLlayoutExMain = (LinearLayout) findViewById(R.id.login_linearlayout_existmain);
			mLlayoutMain = (PagerScrollView) findViewById(R.id.login_linearlayout_main);
			mLlayoutMain.setVisibility(View.GONE);
			mLlayoutExMain.setVisibility(View.VISIBLE);

			mAvatar = mSharedPreferences.getInt(Users.AVATAR, 0);
			mBirthday = mSharedPreferences.getString(Users.BIRTHDAY, "000000");
			mOnlineStateInt = mSharedPreferences.getInt(Users.ONLINESTATEINT, 0);
			mGender = mSharedPreferences.getString(Users.GENDER, "获取失败");
			mAge = mSharedPreferences.getInt(Users.AGE, -1);

			mConstellation = mSharedPreferences.getString(Users.CONSTELLATION, "获取失败");
			mLastLogintime = mSharedPreferences.getString(Users.LOGINTIME, "获取失败");

			mImgExAvatar.setImageBitmap(ImageUtils.getAvatar(mApplication, this, Users.AVATAR + mAvatar));
			mTvExNickmame.setText(mNickname);
			mTvExConstellation.setText(mConstellation);
			mHtvExAge.setText(mAge + "");
			mTvExLogintime.setText(DateUtils.getBetweentime(mLastLogintime));
			if ("女".equals(mGender)) {
				mIvExGender.setBackgroundResource(R.drawable.ic_user_famale);
				mLayoutExGender.setBackgroundResource(R.drawable.bg_gender_famal);
			} else {
				mIvExGender.setBackgroundResource(R.drawable.ic_user_male);
				mLayoutExGender.setBackgroundResource(R.drawable.bg_gender_male);
			}
		}
	}

	@Override
	protected void initEvents() {
		mHtvSelectOnlineState.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mBtnNext.setOnClickListener(this);
		mBtnChangeUser.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_htv_onlinestate:
			mOnlineStateType = getResources().getStringArray(R.array.onlinestate_type);
			mSimpleListDialog = new SimpleListDialog(LoginActivity.this);
			mSimpleListDialog.setTitle("选择在线状态");
			mSimpleListDialog.setTitleLineVisibility(View.GONE);
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(LoginActivity.this, mOnlineStateType));
			mSimpleListDialog.setOnSimpleListItemClickListener(LoginActivity.this);
			mSimpleListDialog.show();
			break;

		// 更换用户,清空数据
		case R.id.login_btn_changeUser:
			mNickname = "";
			mAge = -1;
			mGender = null;
			mIMEI = null;
			mOnlineStateStr = "在线"; // 默认登录状态
			mAvatar = 0;
			mConstellation = null;
			mOnlineStateInt = 0; // 默认登录状态编号
			SessionUtils.clearSession(); // 清空Session数据
			mLlayoutMain.setVisibility(View.VISIBLE);
			mLlayoutExMain.setVisibility(View.GONE);
			break;

		case R.id.login_btn_back:
			finish();
			break;

		case R.id.login_btn_next:
			doLoginNext();
			break;
		}
	}

	@Override
	public void onItemClick(int position) {
		mOnlineStateStr = mOnlineStateType[position];
		mOnlineStateInt = position; // 获取在线状态编号
		mHtvSelectOnlineState.requestFocus();
		mHtvSelectOnlineState.setText(mOnlineStateStr);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		mBirthday = String.valueOf(year) + String.format("%02d", monthOfYear) + String.format("%02d", dayOfMonth);
		mCalendar = Calendar.getInstance();
		mCalendar.set(year, monthOfYear, dayOfMonth);
		if (mCalendar.getTime().after(mMinDate) || mCalendar.getTime().before(mMaxDate)) {
			mCalendar.setTime(mSelectDate);
			mDpBirthday.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
		} else {
			flushBirthday(mCalendar);
		}
	}

	private void flushBirthday(Calendar calendar) {
		String constellation = TextUtils.getConstellation(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		mSelectDate = calendar.getTime();
		mHtvConstellation.setText(constellation);
		int age = TextUtils.getAge(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		mHtvAge.setText(age + "");
	}

	private void initData() {
		if (android.text.TextUtils.isEmpty(mBirthday)) {
			mSelectDate = DateUtils.getDate(DEFAULT_DATA);
			mBirthday = DEFAULT_DATA;
		} else {
			mSelectDate = DateUtils.getDate(mBirthday);
		}

		Calendar mMinCalendar = Calendar.getInstance();
		Calendar mMaxCalendar = Calendar.getInstance();

		mMinCalendar.set(Calendar.YEAR, mMinCalendar.get(Calendar.YEAR) - MIN_AGE);
		mMinDate = mMinCalendar.getTime();
		mMaxCalendar.set(Calendar.YEAR, mMaxCalendar.get(Calendar.YEAR) - MAX_AGE);
		mMaxDate = mMaxCalendar.getTime();

		mCalendar = Calendar.getInstance();
		mCalendar.setTime(mSelectDate);
		flushBirthday(mCalendar);
		mDpBirthday.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
	}

	/**
	 * 登录资料完整性验证，不完整则无法登陆，完整则记录输入的信息。
	 * 
	 * @return boolean 返回是否为完整， 完整(true),不完整(false)
	 */
	private boolean isValidated() {
		mNickname = "";
		mGender = null;
		if (TextUtils.isNull(mEtNickname)) {
			showShortToast(R.string.login_toast_nickname);
			mEtNickname.requestFocus();
			return false;
		}

		switch (mRgGender.getCheckedRadioButtonId()) {
		case R.id.login_baseinfo_rb_female:
			mGender = "女";
			break;
		case R.id.login_baseinfo_rb_male:
			mGender = "男";
			break;
		default:
			showShortToast(R.string.login_toast_sex);
			return false;
		}

		mNickname = mEtNickname.getText().toString().trim(); // 获取昵称
		mAvatar = (int) (Math.random() * 12 + 1); // 获取头像编号
		mConstellation = mHtvConstellation.getText().toString().trim(); // 获取星座
		mAge = Integer.parseInt(mHtvAge.getText().toString().trim()); // 获取年龄
		return true;
	}

	/**
	 * 执行下一步跳转
	 * <p>
	 * 同时获取客户端的IMIE信息
	 */
	private void doLoginNext() {
		if (mNickname.length() == 0) {
			if ((!isValidated())) {
				return;
			}
		}
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog(getString(R.string.login_dialog_saveInfo));
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					mIMEI = mTelephonyManager.getDeviceId(); // 获取IMEI

					// 设置用户Session信息
					SessionUtils.setIMEI(mIMEI);
					SessionUtils.setNickname(mNickname);
					SessionUtils.setAge(mAge);
					SessionUtils.setBirthday(mBirthday);
					SessionUtils.setGender(mGender);
					SessionUtils.setAvatar(mAvatar);
					SessionUtils.setOnlineStateInt(mOnlineStateInt);
					SessionUtils.setConstellation(mConstellation);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				if (result) {
					// startActivity(WifiapActivity.class);
					// finish();
					handler.sendEmptyMessage(0);
				} else {
					showShortToast(R.string.login_toast_loginfailue);
				}
			}
		});
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	// -------------------------------------------------------
	// Del Wifi Mode By FanChenggong add Start
	private UserInfo mUserInfo; // 用户信息类实例
	private SqlDBOperate mSqlDBOperate;// 数据库操作实例,新
	private String mDevice = getPhoneModel(); // 手机品牌型号
	private String localIPaddress; // 本地WifiIP
	private String serverIPaddres; // 热点IP
	private boolean isClient = true; // 客户端标识,默认为true
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				doLogin();
			}
		}
	};

	/** 执行登陆 **/
	private void doLogin() {
		isValidatedIp();
		// localIPaddress = "192.168.1.3";
		// serverIPaddres = "192.168.155.1";
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog(getString(R.string.wifiap_dialog_login_saveInfo));
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					mSqlDBOperate = new SqlDBOperate(mContext);
					String IMEI = SessionUtils.getIMEI();
					String nickname = SessionUtils.getNickname();
					String gender = SessionUtils.getGender();
					String constellation = SessionUtils.getConstellation();
					int age = SessionUtils.getAge();
					int avatar = SessionUtils.getAvatar();
					int onlineStateInt = SessionUtils.getOnlineStateInt();

					String logintime = DateUtils.getNowtime();

					// 录入数据库
					// 若数据库中有IMEI对应的用户记录，则更新此记录; 无则创建新用户
					if ((mUserInfo = mSqlDBOperate.getUserInfoByIMEI(IMEI)) != null) {
						mUserInfo.setIPAddr(localIPaddress);
						mUserInfo.setAvater(avatar);
						mUserInfo.setIsOnline(onlineStateInt);
						mUserInfo.setName(nickname);
						mUserInfo.setSex(gender);
						mUserInfo.setAge(age);
						mUserInfo.setDevice(mDevice);
						mUserInfo.setConstellation(constellation);
						mUserInfo.setLastDate(logintime);
						mSqlDBOperate.updateUserInfo(mUserInfo);
					} else {
						mUserInfo = new UserInfo(nickname, age, gender, IMEI, localIPaddress, onlineStateInt, avatar);
						mUserInfo.setLastDate(logintime);
						mUserInfo.setDevice(mDevice);
						mUserInfo.setConstellation(constellation);
						mSqlDBOperate.addUserInfo(mUserInfo);
					}

					int usserID = mSqlDBOperate.getIDByIMEI(IMEI); // 获取用户id
					// 设置用户Session
					SessionUtils.setLocalUserID(usserID);
					SessionUtils.setDevice(mDevice);
					SessionUtils.setIsClient(isClient);
					SessionUtils.setLocalIPaddress(localIPaddress);
					SessionUtils.setServerIPaddress(serverIPaddres);
					SessionUtils.setLoginTime(logintime);

					// 在SD卡中存储登陆信息
					SharedPreferences.Editor mEditor = getSharedPreferences(GlobalSharedName, Context.MODE_PRIVATE).edit();
					mEditor.putString(Users.IMEI, IMEI).putString(Users.DEVICE, mDevice).putString(Users.NICKNAME, nickname)
							.putString(Users.GENDER, gender).putInt(Users.AVATAR, avatar).putInt(Users.AGE, age)
							.putString(Users.BIRTHDAY, SessionUtils.getBirthday()).putInt(Users.ONLINESTATEINT, onlineStateInt)
							.putString(Users.CONSTELLATION, constellation).putString(Users.LOGINTIME, logintime);
					mEditor.commit();

					// UDPThread
					mUDPSocketThread = UDPSocketThread.getInstance(mApplication, getApplicationContext());
					mUDPSocketThread.connectUDPSocket(); // 新建Socket线程
					mUDPSocketThread.notifyOnline(); // 发送上线广播

					return true;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (null != mSqlDBOperate) {
						mSqlDBOperate.close();
						mSqlDBOperate = null;
					}
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				if (result) {
					startActivity(MainTabActivity.class);
					finish();
				} else {
					showShortToast("操作失败,请检查网络是否正常。");
				}
			}
		});
	}

	public String getPhoneModel() {
		String str1 = Build.BRAND;
		String str2 = Build.MODEL;
		str2 = str1 + "_" + str2;
		return str2;
	}

	/**
	 * 设置IP地址信息
	 * 
	 * @param isClient
	 *            是否为客户端
	 */
	public void setIPaddress(boolean isClient) {
		mWifiUtils = WifiUtils.getInstance(this);
		mWifiUtils.setNewWifiManagerInfo();
		if (!isClient && !mWifiUtils.isWifiConnect()) {
			localIPaddress = mWifiUtils.getLocalIPAddress();
			serverIPaddres = localIPaddress;
			// serverIPaddres = localIPaddress = "192.168.43.1";
		} else {
			localIPaddress = mWifiUtils.getLocalIPAddress();
			serverIPaddres = mWifiUtils.getServerIPAddress();
		}
		showLogInfo(TAG, "localIPaddress:" + localIPaddress + " serverIPaddres:" + serverIPaddres);
	}

	/**
	 * IP地址正确性验证
	 * 
	 * @return boolean 返回是否为正确， 正确(true),不正确(false)
	 */
	private boolean isValidatedIp() {

		setIPaddress(isClient);
		String nullIP = "0.0.0.0";

		if (nullIP.equals(localIPaddress) || nullIP.equals(serverIPaddres) || localIPaddress == null || serverIPaddres == null) {
			// showShortToast(R.string.wifiap_toast_connectap_unavailable);
			return false;
		}

		return true;
	}
	// Del Wifi Mode By FanChenggong add End
	// -------------------------------------------------------
}
