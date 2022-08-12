package zlo.allja1.cat;

import org.xsum.sum.BandRange;

import leaf.xlog.model.Record;

/**
 * 1エリア内電信部門です。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public class エリア内電信 extends 電信 {
	
	/**
	 * 指定されたコードと名前と周波数帯でカテゴリを構築します。
	 * 
	 * @param code カテゴリコード
	 * @param name カテゴリの名前
	 * @param ranges 周波数帯
	 */
	public エリア内電信(String code, String name, BandRange...ranges) {
		super(code, name, ranges);
	}
	
	@Override
	public String validate(Record record) {
		return super.validate(record);
	}

}
