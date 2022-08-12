package org.xsum.cat;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.xsum.sum.BandRange;
import org.xsum.sum.Category;
import org.xsum.sum.Summary;


import leaf.xlog.model.Record;
import leaf.xlog.xdts.Call;
import leaf.xlog.xdts.Mode;
import leaf.xlog.xdts.Number;
import leaf.xlog.xdts.RST;

/**
 * コンテストの各部門の基底クラスです。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public abstract class BasicCategory implements Category {
	private static final List<String> telegraphs = Arrays.asList("CW", "RTTY");
	private final BandRange[] bandRanges;
	private final String categoryCode;
	private final String categoryName;
	private final Map<String, BasicSummary> summaries;
	
	/**
	 * 指定されたコードと名前と周波数帯でカテゴリを構築します。
	 * 
	 * @param code カテゴリコード
	 * @param name カテゴリの名前
	 * @param ranges 周波数帯
	 */
	public BasicCategory(String code, String name, BandRange...ranges) {
		this.categoryCode = code;
		this.categoryName = name;
		this.bandRanges = ranges;
		this.summaries = new HashMap<>();
	}
	
	/**
	 * カテゴリコードを返します。
	 * 
	 * @return カテゴリコード
	 */
	@Override
	public final String getCategoryCode() {
		return categoryCode;
	}
	
	/**
	 * カテゴリの名前を返します。
	 * 
	 * @return カテゴリの名前
	 */
	@Override
	public final String getCategoryName() {
		return categoryName;
	}
	
	/**
	 * 指定された{@link BasicSummary}を追加します。
	 * 
	 * @param sum 追加するサマリー
	 * @return 追加されなかった場合false
	 */
	public final boolean addSummary(BasicSummary sum) {
		String call = sum.getCallSign();
		if(summaries.containsKey(call)) {
			BasicSummary dup = summaries.get(call);
			Date d1 = sum.getRegisteredDate();
			Date d2 = dup.getRegisteredDate();
			if(d1.before(d2)) return false;
		}
		summaries.put(call, sum);
		calculateTotalScore(sum);
		return true;
	}
	
	/**
	 * 総得点で降順に整列された{@link Summary}を返します。
	 * 
	 * @return サマリーのリスト
	 */
	@Override
	public List<Summary> getSortedSummaryList() {
		List<Summary> list = new ArrayList<>();
		for(String call : summaries.keySet()) {
			list.add(summaries.get(call));
		}
		Collections.sort(list);
		return Collections.unmodifiableList(list);
	}
	
	/**
	 * 指定された{@link BasicSummary}の総得点を計算します。
	 * 
	 * @param sum サマリー
	 * @return 計算された総得点
	 */
	private int calculateTotalScore(BasicSummary sum) {
		HashSet<String> calls = new HashSet<>();
		HashSet<String> mults = new HashSet<>();
		for(Record record : sum.getOperations()) {
			String cat = validate(record);
			if(cat == null) continue;
			String call = getCallSign(record);
			String mult = getMultiplier(record);
			if(call != null && !call.isEmpty())
			if(mult != null && !mult.isEmpty())
			if( calls.add(cat + ":" + call)) {
				mults.add(cat + ":" + mult);
			}
		}
		sum.setScore(calls.size());
		sum.setMulti(mults.size());
		return sum.getTotal();
	}
	
	/**
	 * 指定された交信記録がこの部門で有効か確認します。
	 * 
	 * @param record 確認する交信記録
	 * @return 有効な周波数帯・モード名の組 無効ならnull
	 */
	public String validate(Record record) {
		String mode = record.getFieldValue(Mode.class);
		for(BandRange range : bandRanges) {
			String band = range.isInRange(record);
			if(band != null) return band + "." + mode;
		}
		return null;
	}
	
	/**
	 * 指定された交信記録の相手局コールサインを返します。
	 * 
	 * @param record 交信記録
	 * @return コールサイン
	 */
	public final String getCallSign(Record record) {
		String call = record.getFieldValue(Call.class);
		if(call == null) return null;
		int index = call.indexOf('/');
		return index >= 0? call.substring(0, index) : call;
	}
	
	/**
	 * 指定された交信記録のマルチプライヤーを返します。
	 * 
	 * @param record 交信記録
	 * @return マルチプライヤー
	 */
	public final String getMultiplier(Record record) {
		String sent = record.getRcvdFieldValue(Number.class);
		if(sent == null) return null;
		String mode = record.getFieldValue(Mode.class);
		if(record.getRcvdFieldValue(RST.class) != null) {
			return sent;
		} else if(telegraphs.contains(mode)) {
			return sent.substring(3);
		} else {
			return sent.substring(2);
		}
	}

}