package org.xsum.sum;

import java.util.Date;

import leaf.xlog.model.Record;
import leaf.xlog.xdts.Band;
import leaf.xlog.xdts.Freq;

/**
 * コンテスト周波数帯の範囲を表現します。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public final class BandRange {
	public final int min, max;
	private final String name;
	private final Date start, end;
	
	/**
	 * 指定した下限と上限及び時間帯で範囲を構築します。
	 * 
	 * @param min 下限周波数
	 * @param max 上限周波数
	 * @param s 開始時刻
	 * @param e 終了時刻
	 * @param name 周波数帯の名前
	 */
	public BandRange(int min, int max, Date s, Date e, String name) {
		this.min = min;
		this.max = max;
		this.start = s;
		this.end   = e;
		this.name  = name;
	}
	
	/**
	 * 指定した交信が範囲内であるか確認します。
	 * 
	 * @param record 確認する交信
	 * @return 範囲内であれば周波数帯の名前 それ以外はnull
	 */
	public String isInRange(Record record) {
		Band b = record.getField(Band.class);
		Freq f = record.getField(Freq.class);
		if(b == null&&f == null) return null;
		int hz = (b != null? b : f).getValue();
		if(hz < min || max < hz) return null;
		Date d = record.getField(
		leaf.xlog.xdts.Date.class).getValue();
		if(d.before(start) || d.after(end)) {
			return null;
		} else {
			return name;
		}
	}

}
