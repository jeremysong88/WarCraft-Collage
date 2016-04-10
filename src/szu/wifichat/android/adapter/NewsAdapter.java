package szu.wifichat.android.adapter;

import java.util.List;

import szu.wifichat.android.R;
import szu.wifichat.android.entity.MyNews;
import szu.wifichat.android.util.xml.ImageAsyncUtils;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {

	public NewsAdapter(Activity activity, List<MyNews> news) {
		this.activity = activity;
		this.mNews = news;
	}

	private Activity activity;
	private List<MyNews> mNews;

	@Override
	public int getCount() {
		if (mNews != null) {
			return mNews.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mNews != null) {
			return mNews.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MyNews news = mNews.get(position);

		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(R.layout.news_list_item, parent, false);
		}

		ImageView iv = (ImageView) convertView.findViewById(R.id.news_item_iv);
		TextView tvContent = (TextView) convertView.findViewById(R.id.news_item_tv_content);

		// TODO 图片异步加载

		tvContent.setText(news.getTitle());
		if (!TextUtils.isEmpty(news.getImgURL())) {
			new ImageAsyncUtils(activity).mImageLoader.displayImage(news.getImgURL(), iv, ImageAsyncUtils.getOpt());
		}

		return convertView;
	}

}
