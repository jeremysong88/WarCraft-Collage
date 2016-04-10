package szu.wifichat.android.util.xml;

import szu.wifichat.android.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class ImageAsyncUtils {

	private Context context;
	public ImageLoader mImageLoader = ImageLoader.getInstance();

	public ImageAsyncUtils(Context context) {
		super();
		this.context = context;
		mImageLoader.init(getConfiguration());
	}

	/**
	 * 重设图片尺寸
	 * 
	 * @param path
	 * @param baseSize
	 */
	public static Bitmap resizeBitmap(String path, int baseSize) {

		Bitmap mBitmap = null;
		BitmapFactory.Options mOptions = new BitmapFactory.Options();
		mOptions.inJustDecodeBounds = false;

		BitmapFactory.decodeFile(path, mOptions);
		int w = mOptions.outWidth;
		int h = mOptions.outHeight;
		// 找最小变计算缩放比率，防止图片缩放失衡
		int min = w < h ? w : h;
		int rate = min / baseSize;
		if (rate < 0) {
			rate = 1;
		}

		mOptions.inSampleSize = rate;
		mOptions.inJustDecodeBounds = false;

		mBitmap = BitmapFactory.decodeFile(path, mOptions);

		return mBitmap;
	}

	/**
	 * 将图片资源异步加载到ImageView
	 * 
	 * @param uri
	 *            指定图片文件的Uri
	 * @param view
	 *            指定的ImageView
	 */
	public void setImageToView(String uri, ImageView view) {

		ImageLoader mImageLoader = ImageLoader.getInstance();
		// 初始化ImageLoader
		mImageLoader.init(getConfiguration());
		// 给ImageView设置图片
		// mImageLoader.displayImage(uri, view);
		mImageLoader.displayImage(uri, view, getOpt());

	}

	public static DisplayImageOptions getOpt() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_news_loading) // 加载中显示的图片
				.showImageForEmptyUri(R.drawable.ic_news_default) // URI空显示的图片
				.showImageOnFail(R.drawable.ic_news_failure) // 加载失败显示的图片
				// .resetViewBeforeLoading(false) //是否重设View
				// .delayBeforeLoading(0) //加载延时
				.cacheInMemory(true) // 是否缓存内存
				// .cacheOnDisk(false) //释放缓存SDcard
				// .preProcessor(null) //图片处理器（进缓存之前）
				// .postProcessor(null) //图片处理器（显示之前）
				// .extraForDownloader(null) //下载器辅助类
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 图片比例类型，Image生成Bitmap的类型
				.bitmapConfig(Bitmap.Config.ARGB_4444) // 生成Bitmap的编码方式
				// .decodingOptions(null) //解码参数
				// .displayer(new SimpleBitmapDisplayer()) // default
				// 可以设置动画，比如圆角或者渐变
				// .handler(new Handler()) //
				.build();

		return options;
	}

	/**
	 * 初始化ImageLoaderConfiguration
	 * 
	 * @return
	 */
	public ImageLoaderConfiguration getConfiguration() {

		ImageLoaderConfiguration mConfiguration = new ImageLoaderConfiguration.Builder(context)
		// 设置缓存区的大小
				.memoryCacheSize(5 * 1024 * 1024)
				// 内存缓存图片的最大尺寸
				.memoryCacheExtraOptions(300, 400)
				// 设置线程池的线程数
				.threadPoolSize(3)
				// 设置线程的执行顺序
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 设置线程的优先级
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// 禁止一张图片多种尺寸
				.denyCacheImageMultipleSizesInMemory()
				// 设置图片下载器
				.imageDownloader(new BaseImageDownloader(context))
				// 打印调试信息
				.writeDebugLogs().build();

		return mConfiguration;
	}

	/**
	 * 获取相册图片路径。
	 * 
	 * @param context
	 * @param uri
	 *            图片Uri
	 * @return path
	 */
	public static String getAlbumImagePath(Context context, Uri uri) {
		String path = "";
		// 字段名
		String[] proj = { MediaStore.Images.Media.DATA };
		// 查询
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
		// 字段名拿索引
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		// 通过索引拿数值
		path = cursor.getString(column_index);
		cursor.close();
		return path;
	}

}
