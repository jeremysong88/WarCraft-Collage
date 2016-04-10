package szu.wifichat.android.util.xml;


public class UiUtil{
	public static final String TAG = UiUtil.class.getSimpleName();
	
	/**
	 * News style.
	 */
	public final static String NEWS_STYLE = "<style type=\"text/css\">"
			+ "@font-face{ "
			+ "font-family:huawenxinwei;"
			+ "font-weight:normal;"
			+ "font-style:normal;"
			+ "src:url('file:///android_asset/huawenxinwei.TTF') format('truetype');"
			+ "}"
			+ "body{font-size:20px;line-height:25px;word-wrap:break-word;font-family:huawenxinwei;background-color:white;color:#0}"
			+ "p{text-indent:2em}"
			+ "img{max-width:310px} "
			+ "p img{display:block;margin:0 auto}"
			+ "a{text-decoration:none;color:#3E62A6}" 
			+ "h1{padding-top:1em;text-align:center;font-family:default;font-size:23px;line-height:30px}"
			+ "</style>";
	
	public final static String NEWS_STYLE_NIGHT = "<style type=\"text/css\">"
			+ "@font-face{"
			+ "font-family:huawenxinwei;"
			+ "font-weight:normal;"
			+ "font-style:normal;"
			+ "src:url('file:///android_asset/huawenxinwei.TTF') format('truetype');"
			+ "}"
			+ "body{font-size:20px;line-height:25px;word-wrap:break-word;font-family:huawenxinwei;background-color:black;color:#aaaaaa}"
			+ "p{text-indent:2em}"
			+ "img{max-width:310px}"
			+ "p img{display:block;margin:0 auto}"
			+ "a{text-decoration: none;color:#3E62A6}" 
			+ "h1{padding-top:1em;text-align:center;font-family:default;color:#aaaaaa;font-size:23px;line-height:30px}"
			+ "</style>";
	
}
