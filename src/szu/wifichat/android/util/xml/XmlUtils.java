package szu.wifichat.android.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import szu.wifichat.android.entity.MyNews;

public class XmlUtils {

	// �������б�ǩ
	public static final String regexpForHtml = "<([^>]*)>";
	// ����img��ǩ
	public static final String regexpForImg = "(<|;)\\s*(IMG|img)\\s+([^;^>]*)\\s*(;|>)";
	// ��ȡimg��ǩ��url
	public static final String regexpForImgUrl = "http://([^\"]+)\"";
	// ����<>�е�style
	public static final String regexpForStyle = "\\s*style=\"([^\"]*)\"";
	// ��ȡencoding
	public static final String regexpForEncoding = "\\s*encoding=\"([^\"]*)\"";

	public static ArrayList<String> getImageSrcs(String input) {
		ArrayList<String> srcs = new ArrayList<String>();

		Pattern tagPattern = Pattern.compile(regexpForImg);
		Matcher tagMatcher = tagPattern.matcher(input);
		Pattern srcPattern = Pattern.compile(regexpForImgUrl);

		while (tagMatcher.find()) {
			Matcher srcMatcher = srcPattern.matcher(tagMatcher.group());
			while (srcMatcher.find()) {
				String src = srcMatcher.group().replace("\"", "");
				srcs.add(src);
			}
		}
		return srcs;
	}

	public List<MyNews> parserNews(InputStream is) throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory mFactory = SAXParserFactory.newInstance();
		SAXParser mParser = mFactory.newSAXParser();
		MyHandler dh = new MyHandler();
		mParser.parse(is, dh);

		return dh.getList();
	}

	class MyHandler extends DefaultHandler {

		private List<MyNews> list;
		private StringBuilder sb;
		private MyNews news;

		public List<MyNews> getList() {
			return list;
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			list = new ArrayList<MyNews>();
			sb = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (qName.equalsIgnoreCase("item")) {
				news = new MyNews();
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			super.characters(ch, start, length);
			sb.append(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (news != null) {
				if (qName.equalsIgnoreCase("title")) {
					news.setTitle(sb.toString().trim());
				}
				if (qName.equalsIgnoreCase("description")) {
					news.setDescription(sb.toString().trim());
					List<String> imgUrl = getImageSrcs(sb.toString().trim());
					if (imgUrl.size() != 0) {
						news.setImgURL(imgUrl.get(0));
					}
				}
				if (qName.equalsIgnoreCase("pubDate")) {
					news.setPubDate(sb.toString().trim());
				}
			}
			sb.setLength(0);

			if (qName.equalsIgnoreCase("item")) {
				list.add(news);
			}
		}

		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}
	}

}
