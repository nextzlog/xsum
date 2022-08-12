package org.xsum.cat;

import java.util.Date;

import org.xsum.sum.Prefecture;
import org.xsum.sum.Summary;


/**
 * 総合部門に登録される統合されたサマリーです。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public final class JointSummary implements Summary {
	private final BasicSummary sum1, sum2;
	private JointCategory category;
	
	/**
	 * HIGH/LOW部門の書類を指定してサマリーを構築します。
	 * 
	 * @param sum1 HIGH/LOW部門の書類
	 * @param sum2 HIGH/LOW部門の書類
	 */
	public JointSummary(BasicSummary sum1, BasicSummary sum2) {
		this.sum1 = sum1;
		this.sum2 = sum2;
	}
	
	/**
	 * 自動集計システムで用いるカテゴリコードを返します。
	 * 
	 * @return カテゴリコード
	 */
	@Override
	public String getCategoryCode() {
		return sum1.getCategoryCode().substring(0, 3);
	}
	
	/**
	 * カテゴリの名前を返します。
	 * 
	 * @return カテゴリの名前
	 */
	public String getCategoryName() {
		return category.getCategoryName();
	}
	
	/**
	 * このサマリーの確定日時を返します。
	 * 
	 * @return 確定日時
	 */
	@Override
	public Date getRegisteredDate() {
		Date d1 = sum1.getRegisteredDate();
		Date d2 = sum2.getRegisteredDate();
		return d1.after(d2)? d1 : d2;
	}
	
	/**
	 * この提出局のコールサインを返します。
	 * 
	 * @return コールサイン
	 */
	@Override
	public String getCallSign() {
		return sum1.getCallSign();
	}
	
	/**
	 * この提出局の名前を返します。
	 * 
	 * @return 名前
	 */
	@Override
	public String getName() {
		return sum1.getName();
	}
	
	/**
	 * この提出局が移動運用であるか返します。
	 * 
	 * @return 移動運用の場合true
	 */
	@Override
	public boolean isMobile() {
		return sum1.isMobile();
	}
	
	/**
	 * 運用場所のエリア番号を返します。
	 * 
	 * @return エリア番号
	 */
	@Override
	public int getOperationArea() {
		return sum1.getOperationArea();
	}
	
	/**
	 * 運用場所の都道府県を返します。
	 * 
	 * @return 都道府県
	 */
	@Override
	public Prefecture getOperationPrefecture() {
		return sum1.getOperationPrefecture();
	}
	
	/**
	 * 運用場所を返します。1エリア内は市区町村まで含まれます。
	 * 
	 * @return 運用場所を表す文字列
	 */
	@Override
	public String getOperationPlace() {
		return sum1.getOperationPlace();
	}
	
	/**
	 * この提出局の郵便番号を返します。
	 * 
	 * @return 郵便番号
	 */
	@Override
	public String getPostalCode() {
		return sum1.getPostalCode();
	}
	
	/**
	 * この提出局の住所を返します。
	 * 
	 * @return 住所
	 */
	@Override
	public String getAddress() {
		return sum1.getAddress();
	}
	
	/**
	 * この提出局の電話番号を返します。
	 * 
	 * @return 電話番号
	 */
	@Override
	public String getTelephoneNumber() {
		return sum1.getTelephoneNumber();
	}
	
	/**
	 * この提出局のEメールアドレスを返します。
	 * 
	 * @return メールアドレス
	 */
	@Override
	public String getEmailAddress() {
		return sum1.getEmailAddress();
	}
	
	/**
	 * この提出局からのコメントを返します。
	 * 
	 * @return コメント
	 */
	@Override
	public String getComment() {
		return "";
	}
	
	/**
	 * このサマリーのカテゴリを設定します。
	 * 
	 * @param cat カテゴリ
	 */
	public void setCategory(JointCategory cat) {
		this.category = cat;
	}
	
	/**
	 * このサマリーのカテゴリを返します。
	 * 
	 * @return カテゴリ
	 */
	public JointCategory getCategory() {
		return category;
	}
	
	/**
	 * このサマリーの有効交信局数を返します。
	 * 
	 * @return 有効交信局数
	 */
	@Override
	public int getScore() {
		return sum1.getScore() + sum2.getScore();
	}
	
	/**
	 * このサマリーの有効マルチ数を返します。
	 * 
	 * @return 有効マルチ数
	 */
	@Override
	public int getMulti() {
		return sum1.getMulti() + sum2.getMulti();
	}
	
	/**
	 * このサマリーの総得点を返します。
	 * 
	 * @return 総得点
	 */
	@Override
	public int getTotal() {
		return getScore() * getMulti();
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
		return another.getScore() - getScore();
	}

}
