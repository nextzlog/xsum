package org.xsum.sum;

public enum Prefecture {
	北海道(8),
	青森県(7),
	岩手県(7),
	秋田県(7),
	山形県(7),
	宮城県(7),
	福島県(7),
	新潟県(0),
	長野県(0),
	東京都(1),
	神奈川県(1),
	千葉県(1),
	埼玉県(1),
	茨城県(1),
	栃木県(1),
	群馬県(1),
	山梨県(1),
	静岡県(2),
	岐阜県(2),
	愛知県(2),
	三重県(2),
	京都府(3),
	滋賀県(3),
	奈良県(3),
	大阪府(3),
	和歌山県(3),
	兵庫県(3),
	富山県(9),
	福井県(9),
	石川県(9),
	岡山県(4),
	島根県(4),
	山口県(4),
	鳥取県(4),
	広島県(4),
	香川県(5),
	徳島県(5),
	愛媛県(5),
	高知県(5),
	福岡県(6),
	佐賀県(6),
	長崎県(6),
	熊本県(6),
	大分県(6),
	宮崎県(6),
	鹿児島県(6),
	沖縄県(6);
	
	private final int area;
	
	private Prefecture(int area) {
		this.area = area;
	}
	
	public final boolean is1Area() {
		return area == 1;
	}
	
	public final int getArea() {
		return area;
	}
	
	/**
	 * 都道府県番号に対応する{@link Prefecture}を返します。
	 * 
	 * @param num 1から始まる都道府県番号
	 * @return 対応する都道府県
	 */
	public static final Prefecture valueOf(int num) {
		try {
			return values()[num - 1];
		} catch (IndexOutOfBoundsException ex) {
			throw new IndexOutOfBoundsException(
			"illegal prefecture code: " + num);
		}
	}

}
