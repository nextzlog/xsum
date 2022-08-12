package org.xsum.cat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xsum.sum.Category;
import org.xsum.sum.Summary;

/**
 * 総合部門です。交信局数とマルチは単純に合計されます。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public final class JointCategory implements Category {
	private final String categoryCode;
	private final String categoryName;
	private final Map<String, JointSummary> summaries;
	
	/**
	 * 指定されたコードと名前でカテゴリを構築します。
	 * 
	 * @param code カテゴリコード
	 * @param name カテゴリの名前
	 */
	public JointCategory(String code, String name) {
		this.categoryCode = code;
		this.categoryName = name;
		summaries = new HashMap<>();
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
	 * 指定された{@link JointSummary}を追加します。
	 * 
	 * @param sum 追加するサマリー
	 * @return 追加されなかった場合false
	 */
	public boolean addSummary(JointSummary sum) {
		String call = sum.getCallSign();
		if(summaries.containsKey(call)) {
			JointSummary dup = summaries.get(call);
			Date d1 = sum.getRegisteredDate();
			Date d2 = dup.getRegisteredDate();
			if(d1.before(d2)) return false;
		}
		summaries.put(call, sum);
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

}
