package zlo.allja1.cat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.xsum.cat.JointCategory;
import org.xsum.cat.JointSummary;
import org.xsum.cat.BasicCategory;
import org.xsum.cat.BasicSummary;
import org.xsum.sum.BandRange;
import org.xsum.sum.Category;

/**
 * ALLJA1コンテストの各カテゴリを管理します。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public class Categories {
	private final SummaryTable summaryTable;
	private final List<Category> categoryList;
	private final List<BasicCategory> singleCategoryList;
	private final List<JointCategory> jointCategoryList;
	
	/**
	 * 指定したコンテスト時間帯で{@link Categories}を構築します。
	 * 
	 * @param hbs ハイバンド部門開始時刻
	 * @param hbe ハイバンド部門終了時刻
	 * @param lbs ローバンド部門開始時刻
	 * @param lbe ローバンド部門終了時刻
	 */
	public Categories(Date hbs, Date hbe, Date lbs, Date lbe) {
		summaryTable = new SummaryTable();
		categoryList = new ArrayList<>();
		singleCategoryList = new ArrayList<>();
		jointCategoryList  = new ArrayList<>();
		
		BandRange band019 = new BandRange( 1810,  1913, lbs, lbe, "1.9MHz");
		BandRange band035 = new BandRange( 3500,  3687, lbs, lbe, "3.5MHz");
		BandRange band070 = new BandRange( 7000,  7200, lbs, lbe, "7MHz");
		BandRange band140 = new BandRange(14000, 14350, hbs, hbe, "14MHz");
		BandRange band210 = new BandRange(21000, 21450, hbs, hbe, "21MHz");
		BandRange band280 = new BandRange(28000, 29700, hbs, hbe, "28MHz");
		BandRange band500 = new BandRange(50000, 54000, hbs, hbe, "50MHz");
		
		BandRange[] bandlow  = {band019, band035, band070};
		BandRange[] bandhigh = {band140, band210, band280, band500};
		BandRange[] bandall  = {band019, band035, band070, band140, band210, band280, band500};
		
		addCategory(new エリア内電信("1000", "1エリア内電信 14MHz帯", band140));
		addCategory(new エリア内電信("1001", "1エリア内電信 21MHz帯", band210));
		addCategory(new エリア内電信("1002", "1エリア内電信 28MHz帯", band280));
		addCategory(new エリア内電信("1003", "1エリア内電信 50MHz帯", band500));
		addCategory(new エリア内電信("1004", "1エリア内電信 HIGHバンド", bandhigh));
		addCategory(new エリア内電信("1005", "1エリア内電信 1.9MHz帯", band019));
		addCategory(new エリア内電信("1006", "1エリア内電信 3.5MHz帯", band035));
		addCategory(new エリア内電信("1007", "1エリア内電信 7MHz帯", band070));
		addCategory(new エリア内電信("1008", "1エリア内電信 LOWバンド", bandlow));
		
		addCategory(new JointCategory("100", "1エリア内電信 総合"));
		addCategory(new エリア内電信("101", "1エリア内電信 ALLバンド", bandall));
		
		addCategory(new エリア内電信電話("1100", "1エリア内電信電話 14MHz帯", band140));
		addCategory(new エリア内電信電話("1101", "1エリア内電信電話 21MHz帯", band210));
		addCategory(new エリア内電信電話("1102", "1エリア内電信電話 28MHz帯", band280));
		addCategory(new エリア内電信電話("1103", "1エリア内電信電話 50MHz帯", band500));
		addCategory(new エリア内電信電話("1104", "1エリア内電信電話 HIGHバンド", bandhigh));
		addCategory(new エリア内電信電話("1105", "1エリア内電信電話 1.9MHz帯", band019));
		addCategory(new エリア内電信電話("1106", "1エリア内電信電話 3.5MHz帯", band035));
		addCategory(new エリア内電信電話("1107", "1エリア内電信電話 7MHz帯", band070));
		addCategory(new エリア内電信電話("1108", "1エリア内電信電話 LOWバンド", bandlow));
		
		addCategory(new JointCategory("110", "1エリア内電信電話 総合"));
		addCategory(new エリア内電信電話("111", "1エリア内電信電話 ALLバンド", bandall));
		
		addCategory(new エリア外電信("0000", "1エリア外電信 14MHz帯", band140));
		addCategory(new エリア外電信("0001", "1エリア外電信 21MHz帯", band210));
		addCategory(new エリア外電信("0002", "1エリア外電信 28MHz帯", band280));
		addCategory(new エリア外電信("0003", "1エリア外電信 50MHz帯", band500));
		addCategory(new エリア外電信("0004", "1エリア外電信 HIGHバンド", bandhigh));
		addCategory(new エリア外電信("0005", "1エリア外電信 1.9MHz帯", band019));
		addCategory(new エリア外電信("0006", "1エリア外電信 3.5MHz帯", band035));
		addCategory(new エリア外電信("0007", "1エリア外電信 7MHz帯", band070));
		addCategory(new エリア外電信("0008", "1エリア外電信 LOWバンド", bandlow));
		
		addCategory(new JointCategory("000", "1エリア外電信 総合"));
		addCategory(new エリア内電信("001", "1エリア外電信 ALLバンド", bandall));
		
		addCategory(new エリア外電信電話("0100", "1エリア外電信電話 14MHz帯", band140));
		addCategory(new エリア外電信電話("0101", "1エリア外電信電話 21MHz帯", band210));
		addCategory(new エリア外電信電話("0102", "1エリア外電信電話 28MHz帯", band280));
		addCategory(new エリア外電信電話("0103", "1エリア外電信電話 50MHz帯", band500));
		addCategory(new エリア外電信電話("0104", "1エリア外電信電話 HIGHバンド", bandhigh));
		addCategory(new エリア外電信電話("0105", "1エリア外電信電話 1.9MHz帯", band019));
		addCategory(new エリア外電信電話("0106", "1エリア外電信電話 3.5MHz帯", band035));
		addCategory(new エリア外電信電話("0107", "1エリア外電信電話 7MHz帯", band070));
		addCategory(new エリア外電信電話("0108", "1エリア外電信電話 LOWバンド", bandlow));
		
		addCategory(new JointCategory("010", "1エリア外電信電話 総合"));
		addCategory(new エリア内電信電話("011", "1エリア外電信電話 ALLバンド", bandall));
	}
	
	/**
	 * 指定されたカテゴリをリストに追加します。
	 * 
	 * @param category 追加するカテゴリ
	 */
	private void addCategory(Category category) {
		categoryList.add(category);
		if(category instanceof BasicCategory) {
			singleCategoryList.add((BasicCategory) category);
		} else if(category instanceof JointCategory) {
			jointCategoryList.add((JointCategory) category);
		}
	}
	
	/**
	 * 総合部門も含めた全てのカテゴリを返します。
	 * 
	 * @return カテゴリの配列
	 */
	public Category[] getCategories() {
		return categoryList.toArray(new Category[categoryList.size()]);
	}
	
	/**
	 * {@link BasicSummary}に対して適切なカテゴリを返します。
	 * 
	 * @param sum サマリー
	 * @return 追加されるカテゴリ
	 * @throws IllegalArgumentException カテゴリが不適切な場合
	 */
	private BasicCategory getCategory(BasicSummary sum) throws IllegalArgumentException {
		String code = sum.getCategoryCode();
		for(BasicCategory cat : singleCategoryList) {
			if(code.startsWith(cat.getCategoryCode())) {
				return cat;
			}
		}
		String msg = String.format("unknown category '%s': %s", code, sum.getSumFile());
		throw new IllegalArgumentException(msg);
	}
	
	/**
	 * {@link JointSummary}に対して適切なカテゴリを返します。
	 * 
	 * @param sum サマリー
	 * @return 追加されるカテゴリ
	 * @throws IllegalArgumentException カテゴリが不適切な場合
	 */
	private JointCategory getCategory(JointSummary sum) throws IllegalArgumentException {
		String code = sum.getCategoryCode();
		for(JointCategory cat : jointCategoryList) {
			if(code.startsWith(cat.getCategoryCode())) {
				return cat;
			}
		}
		String msg = String.format("unknown category '%s': %s", code, sum.getCallSign());
		throw new IllegalArgumentException(msg);
	}
	
	/**
	 * {@link BasicSummary}を適切なカテゴリに追加します。
	 * 
	 * @param sum 追加するサマリー
	 * @throws IllegalArgumentException カテゴリが不適切な場合
	 */
	public void addSummary(BasicSummary sum) throws IllegalArgumentException {
		summaryTable.addSummary(sum);
		BasicCategory cat = getCategory(sum);
		if(cat.addSummary(sum)) sum.setCategory(cat);
		
		String call = sum.getCallSign();
		BasicSummary sum_h = summaryTable.getMultiHighBandSummary(call);
		BasicSummary sum_l = summaryTable.getMultiLowBandSummary (call);
		
		if(sum_h == null || sum_l == null) return;
		
		String opplace_h = sum_h.getOperationPlace();
		String opplace_l = sum_l.getOperationPlace();
		
		if(!opplace_h.equals(opplace_l)) return;
		
		JointSummary  js = new JointSummary(sum_h, sum_l);
		JointCategory jc = getCategory(js);
		if(jc.addSummary(js)) js.setCategory(jc);
	}
	
	/**
	 * コールサインと、その提出書類のリストを管理するテーブルです。
	 * 
	 * 
	 * @author 東大アマチュア無線クラブ
	 * 
	 * @since 2013/06/29
	 * 
	 */
	private static class SummaryTable extends HashMap<String, List<BasicSummary>> {
		private static final long serialVersionUID = 1L;

		/**
		 * 提出書類をテーブルに追加します。
		 * 
		 * @param sum 追加する書類
		 */
		public void addSummary(BasicSummary sum) {
			List<BasicSummary> list = super.get(sum.getCallSign());
			if(list == null) {
				super.put(sum.getCallSign(), list = new ArrayList<BasicSummary>());
			}
			list.add(sum);
			Collections.sort(list, new DateComparator());
		}
		
		/**
		 * 最後に登録したサマリーがリストの最初に来るように並べ替えます。
		 * 
		 * 
		 * @author 東大アマチュア無線クラブ
		 * 
		 * @since 2013/06/29
		 *
		 */
		private static class DateComparator implements Comparator<BasicSummary> {
			@Override
			public int compare(BasicSummary sum1, BasicSummary sum2) {
				long d1 = sum1.getRegisteredDate().getTime();
				long d2 = sum2.getRegisteredDate().getTime();
				return Long.compare(d2, d1);
			}
		}
		
		/**
		 * 指定されたコールサインでHIGHマルチバンド部門のサマリーを検索します。
		 * 
		 * @param call コールサイン
		 * @return HIGHマルチバンド部門の書類 ない場合null
		 */
		public BasicSummary getMultiHighBandSummary(String call) {
			List<BasicSummary> list = super.get(call);
			if(list == null) return null;
			for(BasicSummary sum : list) {
				String code = sum.getCategoryCode();
				if(code.endsWith("04")) return sum;
			}
			return null;
		}
		
		/**
		 * 指定されたコールサインでLOWマルチバンド部門のサマリーを検索します。
		 * 
		 * @param call コールサイン
		 * @return LOWマルチバンド部門の書類 ない場合null
		 */
		public BasicSummary getMultiLowBandSummary(String call) {
			List<BasicSummary> list = super.get(call);
			if(list == null) return null;
			for(BasicSummary sum : list) {
				String code = sum.getCategoryCode();
				if(code.endsWith("08")) return sum;
			}
			return null;
		}
	}

}
