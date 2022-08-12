package org.xsum.sum;

import java.util.Date;


/**
 * コンテストの提出書類を表現するオブジェクトです。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public interface Summary extends Comparable<Summary> {
	/**
	 * 自動集計システムで用いるカテゴリコードを返します。
	 * 
	 * @return カテゴリコード
	 */
	public String getCategoryCode();
	
	/**
	 * カテゴリの名前を返します。
	 * 
	 * @return カテゴリの名前
	 * @throws NullPointerException カテゴリが確定していない場合
	 */
	public String getCategoryName() throws NullPointerException;
	
	/**
	 * このサマリーの提出日時を返します。
	 * 
	 * @return 提出日時
	 */
	public Date getRegisteredDate();
	
	/**
	 * この提出局のコールサインを返します。
	 * 
	 * @return コールサイン
	 */
	public String getCallSign();
	
	/**
	 * この提出局の名前を返します。
	 * 
	 * @return 名前
	 */
	public String getName();
	
	/**
	 * この提出局が移動運用であるか返します。
	 * 
	 * @return 移動運用の場合true
	 */
	public boolean isMobile();
	
	/**
	 * 運用場所のエリア番号を返します。
	 * 
	 * @return エリア番号
	 */
	public int getOperationArea();
	
	/**
	 * 運用場所の都道府県を返します。
	 * 
	 * @return 都道府県
	 */
	public Prefecture getOperationPrefecture();
	
	/**
	 * 運用場所(市区町村を含むこともある)を返します。
	 * 
	 * @return 運用場所を表す文字列
	 */
	public String getOperationPlace();
	
	/**
	 * この提出局の郵便番号を返します。
	 * 
	 * @return 郵便番号
	 */
	public String getPostalCode();
	
	/**
	 * この提出局の住所を返します。
	 * 
	 * @return 住所
	 */
	public String getAddress();
	
	/**
	 * この提出局の電話番号を返します。
	 * 
	 * @return 電話番号
	 */
	public String getTelephoneNumber();
	
	/**
	 * この提出局のEメールアドレスを返します。
	 * 
	 * @return メールアドレス
	 */
	public String getEmailAddress();
	
	/**
	 * この提出局からのコメントを返します。
	 * 
	 * @return コメント
	 */
	public String getComment();
	
	/**
	 * このサマリーの有効交信局数を返します。
	 * 
	 * @return 有効交信局数
	 */
	public int getScore();
	
	/**
	 * このサマリーの有効マルチ数を返します。
	 * 
	 * @return 有効マルチ数
	 */
	public int getMulti();
	
	/**
	 * このサマリーの総得点を返します。
	 * 
	 * @return 総得点
	 */
	public int getTotal();
	
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
	public int compareTo(Summary another);

}
