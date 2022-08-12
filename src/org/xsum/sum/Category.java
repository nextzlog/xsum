package org.xsum.sum;

import java.util.List;


/**
 * コンテストの各部門の基底インターフェースです。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public interface Category {
	/**
	 * カテゴリコードを返します。
	 * 
	 * @return カテゴリコード
	 */
	public String getCategoryCode();
	
	/**
	 * カテゴリの名前を返します。
	 * 
	 * @return カテゴリの名前
	 */
	public String getCategoryName();
	
	/**
	 * 総得点で降順に整列された{@link Summary}を返します。
	 * 
	 * @return サマリーのリスト
	 */
	public List<Summary> getSortedSummaryList();
}
