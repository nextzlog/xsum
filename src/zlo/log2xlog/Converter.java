package zlo.log2xlog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import leaf.xlog.model.Document;
import leaf.xlog.serial.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 交信記録ファイルを読み込んでxlog交信記録オブジェクトに変換します。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/08/21
 * 
 */
public final class Converter {
	private final List<LogSheetFormat> formatList;
	private final List<ConvertListener> listeners;
	
	/**
	 * コンバータを構築します。
	 */
	public Converter() {
		listeners = new ArrayList<>();
		formatList = new ArrayList<>();
		LogSheetManager manager = new LogSheetManager();
		for(LogSheetFormat format : manager.getFormats()) {
			if(format.getMimeTypes().contains("text/plain")){
				formatList.add(format);
			}
		}
	}
	
	/**
	 * このコンバータが対応しているフォーマットの個数を返します。
	 * 
	 * @return フォーマットの個数
	 */
	public int getAvailableFormatCount() {
		return formatList.size();
	}
	
	/**
	 * {@link ConvertListener}を追加します。
	 * 
	 * @param listener 追加するリスナー
	 */
	public void addConvertListener(ConvertListener listener) {
		listeners.add(listener);
	}
	
	private void fireFormatTrial(LogSheetFormat format) {
		String name = format.getFormatDescription();
		ConvertEvent e = new ConvertEvent(this, name);
		for(ConvertListener l : listeners) {
			l.onFormatTrial(e);
		}
	}
	
	/**
	 * 指定された交信記録ファイルを読み込んで変換を試みます。
	 * 
	 * @param file 交信記録ファイル
	 * @return 変換された交信記録
	 * 
	 * @throws IOException 読み込みに失敗した場合
	 */
	public Document load(File file) throws IOException {
		try {
			InputStream in = new FileInputStream(file);
			String text = new JarlSummarySheetUnpacker(in).unpack();
			byte[] bytes = text.getBytes("SJIS");
			InputStream bais = new ByteArrayInputStream(bytes);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			for(LogSheetFormat format : formatList) {
				fireFormatTrial(format);
				try {
					return format.decode(bais);
				} catch(Exception ex) {
					pw.print(format.getFormatName());
					pw.print(":");
					pw.println(ex.getMessage());
					bais.reset();
				}
			}
			throw new IOException("cannot read: " + file + ":\n" + sw);
		} catch(SAXException ex) {
			throw new IOException("cannot read: " + file + ":\n" + ex);
		}
	}
	
	private static final class JarlSummarySheetUnpacker {
		private final InputStreamReader reader;
		private final SAXParserFactory parserFactory;
		private final LogSheetHandler logSheetHandler;
		
		JarlSummarySheetUnpacker(InputStream stream) throws IOException {
			reader = new InputStreamReader(stream, "UTF8");
			parserFactory = SAXParserFactory.newInstance();
			logSheetHandler = new LogSheetHandler();
		}
		
		public final String unpack() throws IOException, SAXException {
			try {
				SAXParser parser = parserFactory.newSAXParser();
				parser.parse(preprocess(), logSheetHandler);
				return logSheetHandler.buffer.toString();
			} catch (ParserConfigurationException ex) {
				throw new SAXException(ex);
			}
		}
		
		private final String readStream() throws IOException {
			BufferedReader br = new BufferedReader(reader);
			try {
				String line;
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine()) != null) {
					sb.append(line).append('\n');
				}
				return sb.toString();
			} finally {
				br.close();
			}
		}
		
		private final String prepreprocess() throws IOException {
			String text = readStream();
			StringBuilder buffer = new StringBuilder();
			StringBuilder escape = new StringBuilder();
			for(int i = 0; i < text.length(); i++) {
				char ch = text.charAt(i);
				if(escape.length() == 0) {
					if(ch == '&') escape.append(ch);
					else buffer.append(ch);
				} else if(ch == ';') {
					switch(escape.toString()) {
					case "&gt": buffer.append('>'); break;
					case "&lt": buffer.append('<'); break;
					default: buffer.append(escape);
					}
					escape.setLength(0);
				} else escape.append(ch);
			}
			return buffer.append(escape).toString();
		}
		
		private final InputSource preprocess() throws IOException {
			String text = prepreprocess();
			StringBuilder sb = new StringBuilder();
			sb.append("<document>");
			boolean isTag = false;
			boolean isAttr = false;
			boolean isQuoted = false;
			StringBuilder attrVal = new StringBuilder();
			for(int i = 0; i < text.length() ; i++) {
				char ch = text.charAt(i);
				if(isTag && !isAttr && ch == '=') {
					isAttr = true;
					sb.append(ch);
				} else if(isAttr) {
					if(!isQuoted) {
						if(ch == '"') isQuoted = true;
						else if(ch == '>' || ch == ' ') {
							isAttr = false;
							sb.append('"');
							sb.append(attrVal);
							sb.append('"');
							sb.append(ch);
							attrVal.setLength(0);
							if(ch == '>') isTag = false;
						}
						else attrVal.append(ch);
					}
					else if(ch == '"') {
						isQuoted = false;
					}
					else attrVal.append(ch);
				} else if(!isTag && ch == '<') {
					isTag = true;
					sb.append(ch);
				} else if(isTag && ch == '>') {
					isTag = false;
					sb.append(ch);
				} else if(ch == '<') {
					sb.append("&lt;");
				} else if(ch == '>') {
					sb.append("&gt;");
				} else if(ch == '&') {
					sb.append("&amp;");
				} else if(ch == '"') {
					sb.append("&quot;");
				} else if(ch == '\'') {
					sb.append("&apos;");
				} else if(isTag) {
					sb.append(Character.toUpperCase(ch));
				} else sb.append(ch);
			}
			String xml = sb.append("</document>").toString();
			return new InputSource(new StringReader(xml));
		}
		
		private class LogSheetHandler extends DefaultHandler {
			private StringBuilder buffer = new StringBuilder();
			private boolean isSumSheet = false;
			private boolean isLogSheet = false;
			
			@Override
			public void startElement
			(String uri, String ln, String qn, Attributes attrs) {
				if(qn.equals("SUMMARYSHEET")) isSumSheet = true;
				isLogSheet = !isSumSheet||qn.equals("LOGSHEET");
			}
			
			@Override
			public void characters(char[] ch, int off, int len) {
				if(isLogSheet) buffer.append(ch, off, len);
			}
		}
	}

}
