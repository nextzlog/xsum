package zlo.allja1;

import java.io.File;

import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.xsum.cat.BasicSummary;
import org.xsum.out.HTML;
import org.xsum.out.SpreadSheet;
import org.xsum.sum.Category;

import leaf.xlog.model.Document;

import zlo.allja1.cat.Categories;
import zlo.allja1.gui.ConsolePane;
import zlo.log2xlog.Converter;

/**
 * ALLJA1コンテストの自動集計を行います。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 *
 */
public final class Processor {
	private final Categories categories;
	
	/**
	 * 自動集計プロセッサを構築します。
	 * 
	 * @throws IOException  設定ファイルが読み込めない場合
	 * @throws ParseException 設定ファイルの日付の処理例外
	 */
	public Processor() throws IOException, ParseException {
		categories = loadCategoryConfig();
	}
	
	Categories loadCategoryConfig() throws IOException, ParseException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		Date hbs = df.parse(prop.getProperty("HighBandStartDate"));
		Date hbe = df.parse(prop.getProperty("HighBandEndDate"));
		Date lbs = df.parse(prop.getProperty("LowBandStartDate"));
		Date lbe = df.parse(prop.getProperty("LowBandEndDate"));
		return new Categories(hbs, hbe, lbs, lbe);
	}
	
	/**
	 * 指定された名前の交信記録を処理して部門に追加します。
	 * 
	 * @param recordname 交信記録の名前(拡張子を含まない)
	 * @throws Exception 処理に失敗した場合
	 */
	public void append(String recordname) throws Exception {
		File xml = new File(recordname + ".log");
		File sum = new File(recordname + ".sum");
		Document log = new Converter().load(xml);
		append(new BasicSummary(log, sum));
	}
	
	/**
	 * 指定されたサマリーを処理して部門に追加します。
	 * 
	 * @param summary サマリー
	 * @throws Exception 処理に失敗した場合
	 */
	public void append(BasicSummary summary) throws Exception {
		categories.addSummary(summary);
	}
	
	/**
	 * 集計結果の一覧を返します。
	 * 
	 * @return 集計結果の一覧
	 */
	public Category[] getResult() {
		return categories.getCategories();
	}
	
	/**
	 * 指定されたファイルに集計結果をHTMLで出力します。
	 * 
	 * @param file 出力先ファイル
	 * @throws IOException 入出力例外
	 */
	public void outputHTML(File file) throws IOException {
		PrintWriter pw = new PrintWriter(file);
		new HTML().write(pw, getResult());
	}
	
	/**
	 * 指定されたファイルに集計結果をCSVで出力します。
	 * 
	 * @param file 出力先ファイル
	 * @throws IOException 入出力例外
	 */
	public void outputSpreadSheet(File file) throws IOException {
		PrintWriter pw = new PrintWriter(file);
		new SpreadSheet().write(pw, getResult());
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length > 0) {
			processAll(new File(args[0]));
		} else {
			ConsolePane.showConsole();
		}
	}
	
	private static void processAll(File dir) throws Exception {
		Processor processor = new Processor();
		final int suflen = ".sum".length();
		for(File sumfile : dir.listFiles(new SumFileFilter())) {
			String path = sumfile.getPath();
			String name = path.substring(0, path.length() - suflen);
			try {
				processor.append(name);
			} catch(Exception ex) {
				ErrorLog.error(ex);
			}
		}
		processor.outputSpreadSheet(new File("result.csv"));
		processor.outputHTML(new File("result.html"));
	}
	
	/**
	 * 自動集計プログラムで発生したエラーをファイルに出力します。
	 * 
	 * 
	 * @author 東大アマチュア無線クラブ
	 * 
	 * @since 2013/06/29
	 *
	 */
	private static final class ErrorLog {
		private static final String FILE = "processor.log";
		private static ErrorLog instance;
		private final Logger errLogger;
		
		public ErrorLog() throws SecurityException, IOException {
			FileHandler errHandler = new FileHandler(FILE, true);
			errHandler.setFormatter(new LogFormatter());
			errLogger = Logger.getLogger(FILE);
			errLogger.setUseParentHandlers(false);
			errLogger.addHandler(errHandler);
		}
		
		/**
		 * 指定されたメッセージで警告を出力します。
		 * 
		 * @param msg 警告メッセージ
		 */
		public static void warning(String msg) {
			if(instance == null) try {
				instance = new ErrorLog();
			} catch (SecurityException | IOException ex) {
				ex.printStackTrace();
			}
			if(instance != null) instance.errLogger.warning(msg);
		}
		
		/**
		 * 指定された例外を出力します。
		 * 
		 * @param ex 出力する例外
		 */
		public static void error(Exception ex) {
			warning(ex.toString());
		}
		
		private class LogFormatter extends Formatter {
			@Override
			public String format(LogRecord record) {
				StringBuilder sb = new StringBuilder();
				sb.append(super.formatMessage(record));
				return sb.append('\n').toString();
			}
		}

	}
	
	/**
	 * .sumファイルの一覧を取得するためのファイルフィルタです。
	 * 
	 * 
	 * @author 東大アマチュア無線クラブ
	 * 
	 * @since 2013/06/29
	 * 
	 */
	public static class SumFileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String sumfile) {
			if(sumfile.endsWith(".sum")) {
				return new File(dir, sumfile).canRead();
			}
			return false;
		}
	}

}