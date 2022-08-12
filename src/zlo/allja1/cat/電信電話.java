package zlo.allja1.cat;


import org.xsum.cat.BasicCategory;
import org.xsum.sum.BandRange;



import leaf.xlog.model.Record;

/**
 * ALLJA1コンテストの電信電話部門です。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public class 電信電話 extends BasicCategory {
	
	/**
	 * 指定されたコードと名前と周波数帯でカテゴリを構築します。
	 * 
	 * @param code カテゴリコード
	 * @param name カテゴリの名前
	 * @param ranges 周波数帯
	 */
	public 電信電話(String code, String name, BandRange...ranges) {
		super(code, name, ranges);
	}
	
	@Override
	public String validate(Record record) {
		return super.validate(record);
	}

}