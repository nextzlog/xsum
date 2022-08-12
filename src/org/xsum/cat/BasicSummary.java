package org.xsum.cat;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.xsum.sum.Prefecture;
import org.xsum.sum.Summary;

import leaf.xlog.model.Document;
import leaf.xlog.model.Record;
import leaf.xlog.xdts.Mode;

/**
 * コンテストの提出書類を表すオブジェクトです。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public final class BasicSummary implements Summary {
	private static final DateFormat regdateFormat;
	private static final List<String> telegraphs;
	
	private final Properties properties;
	private final Document operations;
	private final Date registeredDate;
	private final File sumfile;
	
	private BasicCategory category;
	private int score, multi;
	
	static {
		regdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		telegraphs = Arrays.asList("CW", "RTTY");
	}
	
	/**
	 * 指定された交信記録と.sumファイルからサマリーを構築します。
	 * 
	 * @param log 交信記録
	 * @param sumfile .sumファイル
	 * @throws Exception 読み込みに失敗した場合
	 */
	public BasicSummary(Document log, File sumfile) throws Exception {
		operations = log;
		properties = readSumFile(this.sumfile = sumfile);
		registeredDate = regdateFormat.parse(getProperty("regdate"));
	}
	
	/**
	 * このサマリーが電信部門であるか交信記録で確認します。
	 * 
	 * @return 電信部門であればtrue
	 */
	public boolean isTelegraphOnly() {
		for(Record record : operations.getAllRecords()) {
			Mode mode = record.getField(Mode.class);
			if(mode == null) return false;
			String mn = mode.getValue().toUpperCase();
			if(!telegraphs.contains(mn)) return false;
		}
		return true;
	}
	
	/**
	 * 自動集計システムで用いるカテゴリコードを返します。
	 * 
	 * @return カテゴリコード
	 */
	@Override
	public String getCategoryCode() {
		int mode = isTelegraphOnly()? 0 : 1;
		String opnm = getProperty("openum");
		String band = getProperty("band");
		if(getOperationArea() == 1) {
			return "1" + mode + opnm + band;
		} else {
			return "0" + mode + opnm + band;
		}
	}
	
	/**
	 * カテゴリの名前を返します。
	 * 
	 * @return カテゴリの名前
	 * @throws NullPointerException カテゴリが確定していない場合
	 */
	public String getCategoryName() {
		return category.getCategoryName();
	}
	
	/**
	 * このサマリーの提出日時を返します。
	 * 
	 * @return 提出日時
	 */
	@Override
	public Date getRegisteredDate() {
		return registeredDate;
	}
	
	/**
	 * この提出局のコールサインを返します。
	 * 
	 * @return コールサイン
	 */
	@Override
	public String getCallSign() {
		String call = getProperty("callsign").toUpperCase();
		if(!isMobile()) return call;
		return call + "/" + getOperationArea();
	}
	
	/**
	 * この提出局の名前を返します。
	 * 
	 * @return 名前
	 */
	@Override
	public String getName() {
		return getProperty("name");
	}
	
	/**
	 * この提出局が移動運用であるか返します。
	 * 
	 * @return 移動運用の場合true
	 */
	@Override
	public boolean isMobile() {
		return getProperty("mobile").equals("1");
	}
	
	/**
	 * 運用場所のエリア番号を返します。
	 * 
	 * @return エリア番号
	 */
	@Override
	public int getOperationArea() {
		return getOperationPrefecture().getArea();
	}
	
	/**
	 * 運用場所の都道府県を返します。
	 * 
	 * @return 都道府県
	 */
	@Override
	public Prefecture getOperationPrefecture() {
		String pref = getProperty("opplace_pref");
		return Prefecture.valueOf(Integer.parseInt(pref));
	}
	
	/**
	 * 運用場所を返します。1エリア内は市区町村まで含まれます。
	 * 
	 * @return 運用場所を表す文字列
	 */
	@Override
	public String getOperationPlace() {
		Prefecture pref = getOperationPrefecture();
		if(!pref.is1Area()) return pref.toString();
		String city = getProperty("opplace_city");
		int i1 = city.indexOf("市");
		int i2 = city.indexOf("区");
		int i3 = city.indexOf("町");
		int i4 = city.indexOf("村");
		int os = Math.max(Math.max(i1, i2), Math.max(i3, i4));
		if(os >= 0) city = city.substring(0, os + 1);
		return pref.toString().concat(city);
	}
	
	/**
	 * この提出局の郵便番号を返します。
	 * 
	 * @return 郵便番号
	 */
	@Override
	public String getPostalCode() {
		return getProperty("postalcode");
	}
	
	/**
	 * この提出局の住所を返します。
	 * 
	 * @return 住所
	 */
	@Override
	public String getAddress() {
		return getProperty("address");
	}
	
	/**
	 * この提出局の電話番号を返します。
	 * 
	 * @return 電話番号
	 */
	@Override
	public String getTelephoneNumber() {
		return getProperty("tel");
	}
	
	/**
	 * この提出局のEメールアドレスを返します。
	 * 
	 * @return メールアドレス
	 */
	@Override
	public String getEmailAddress() {
		return getProperty("email");
	}
	
	/**
	 * この提出局からのコメントを返します。
	 * 
	 * @return コメント
	 */
	@Override
	public String getComment() {
		return getProperty("`comment`");
	}
	
	/**
	 * 指定されたプロパティの設定値を返します。
	 * 
	 * @param key プロパティキー
	 * @return プロパティの設定値
	 */
	private String getProperty(String key) {
		String origin = properties.getProperty(key);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < origin.length(); i++) {
			char ch = origin.charAt(i);
			if('ａ' <= ch && ch <= 'ｚ') {
				sb.append((char) (ch - 'ａ' + 'a'));
			} else if('Ａ' <= ch && ch <= 'Ｚ') {
				sb.append((char) (ch - 'Ａ' + 'A'));
			} else if('０' <= ch && ch <= '９') {
				sb.append((char) (ch - '０' + '0'));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	/**
	 * このサマリーの交信記録を返します。
	 * 
	 * @return 交信記録
	 */
	public Record[] getOperations() {
		return operations.getAllRecords();
	}
	
	/**
	 * このサマリーの読み込み元である.sumファイルを返します。
	 * 
	 * @return .sumファイル
	 */
	public File getSumFile() {
		return sumfile;
	}
	
	/**
	 * このサマリーのカテゴリを設定します。
	 * 
	 * @param cat カテゴリ
	 */
	public void setCategory(BasicCategory cat) {
		this.category = cat;
	}
	
	/**
	 * このサマリーのカテゴリを返します。
	 * 
	 * @return カテゴリ
	 */
	public BasicCategory getCategory() {
		return category;
	}
	
	/**
	 * このサマリーの有効交信局数を設定します。
	 * 
	 * @param score 有効交信局数
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * このサマリーの有効交信局数を返します。
	 * 
	 * @return 有効交信局数
	 */
	@Override
	public int getScore() {
		return score;
	}
	
	/**
	 * このサマリーの有効マルチ数を設定します。
	 * 
	 * @param multi 有効マルチ数
	 */
	public void setMulti(int multi) {
		this.multi = multi;
	}
	
	/**
	 * このサマリーの有効マルチ数を返します。
	 * 
	 * @return 有効マルチ数
	 */
	@Override
	public int getMulti() {
		return multi;
	}
	
	/**
	 * このサマリーの総得点を返します。
	 * 
	 * @return 総得点
	 */
	@Override
	public int getTotal() {
		return score * multi;
	}
	
	/**
	 * 点数でサマリーが降順に並ぶように比較します。
	 * 
	 * @param another 比較対象のサマリー
	 * @return
	 *  このサマリーの点数が大きい場合-1
	 *  このサマリーの点数が小さい場合 1
	 *  点数が等しい場合は0
	 */
	@Override
	public int compareTo(Summary another) {
		return another.getTotal() - getTotal();
	}
	
	/**
	 * 指定された.sumファイルから登録情報のプロパティを読み込みます。
	 * 
	 * @param sumfile .sumファイル
	 * @return 読み込まれた登録情報
	 * @throws IOException 読み込みに失敗した場合
	 */
	private Properties readSumFile(File sumfile) throws IOException {
		Charset cs = Charset.forName("utf-8");
		List<String> lines = Files.readAllLines(sumfile.toPath(), cs);
		if(lines.size() != 2) throw new IOException(sumfile.getName());
		String[] keys = new SummaryTokenizer(lines.get(0)).tokenize();
		String[] vals = new SummaryTokenizer(lines.get(1)).tokenize();
		if(keys.length != vals.length) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.printf("illegal summary file: %s\n", sumfile);
			pw.flush();
			final int len = Math.min(keys.length, vals.length);
			for(int i = 0; i < len; i++) {
				pw.printf("%s: %s\n", keys[i], vals[i]);
				pw.flush();
			}
			throw new IOException(sw.toString());
		} else {
			Properties properties = new Properties();
			for(int i = 0; i < keys.length; i++) {
				properties.setProperty(keys[i], vals[i]);
			}
			return properties;
		}
	}
	
	private static class SummaryTokenizer {
		private final String source;
		private int index = 0;
		
		public SummaryTokenizer(String source) {
			this.source = source;
		}
		
		public String nextToken() {
			boolean quoted = false;
			StringBuilder sb = new StringBuilder();
			while(index < source.length()) {
				char ch = source.charAt(index++);
				if(quoted) {
					if(ch == '\'') {
						quoted = false;
					} else {
						sb.append(ch);
					}
				} else if(ch == '\'') {
					quoted = true;
				} else if(ch == ',') {
					return sb.toString();
				} else if(ch != ' ') {
					sb.append(ch);
				}
			}
			return sb.length() == 0 ? null : sb.toString();
		}
		
		public String[] tokenize() {
			List<String> list = new ArrayList<String>();
			String token;
			while((token = nextToken()) != null) {
				list.add(token);
			}
			return list.toArray(new String[list.size()]);
		}
	}

}