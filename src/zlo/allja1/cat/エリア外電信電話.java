package zlo.allja1.cat;

import org.xsum.sum.BandRange;

import leaf.xlog.model.Record;

/**
 * 1エリア外電信電話部門です。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 *
 */
public class エリア外電信電話 extends 電信電話 {
	
	/**
	 * 指定されたコードと名前と周波数帯でカテゴリを構築します。
	 * 
	 * @param code カテゴリコード
	 * @param name カテゴリの名前
	 * @param ranges 周波数帯
	 */
	public エリア外電信電話(String code, String name, BandRange...ranges) {
		super(code, name, ranges);
	}
	
	@Override
	public String validate(Record record) {
		String band = super.validate(record);
		if(band == null) return null;
		String mul = super.getMultiplier(record);
		int p = Integer.parseInt(mul.substring(0, 2));
		return (10 <= p && p <= 17)? band : null;
	}

}
