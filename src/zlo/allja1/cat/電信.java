package zlo.allja1.cat;




import java.util.Arrays;
import java.util.List;

import org.xsum.cat.BasicCategory;
import org.xsum.sum.BandRange;




import leaf.xlog.model.Record;
import leaf.xlog.xdts.Mode;

/**
 * ALLJA1コンテストの電信部門です。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public class 電信 extends BasicCategory {
	private static final List<String> modes;
	
	static {
		modes = Arrays.asList("CW", "RTTY");
	}
	
	/**
	 * 指定されたコードと名前と周波数帯でカテゴリを構築します。
	 * 
	 * @param code カテゴリコード
	 * @param name カテゴリの名前
	 * @param ranges 周波数帯
	 */
	public 電信(String code, String name, BandRange...ranges) {
		super(code, name, ranges);
	}
	
	@Override
	public String validate(Record record) {
		String band = super.validate(record);
		if(band == null) return null;
		Mode mode = record.getField(Mode.class);
		if(mode == null) return null;
		String str = mode.getValue().toUpperCase();
		return modes.contains(str)? band : null;
	}

}
