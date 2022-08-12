package org.xsum.out;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.xsum.sum.Category;
import org.xsum.sum.Summary;

/**
 * 集計結果をスプレッドシート向けにCSVで出力します。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public final class SpreadSheet implements ResultWriter {
	private final DateFormat dateFormat;
	
	public SpreadSheet() {
		dateFormat = new SimpleDateFormat("MM月dd日HH時mm分");
	}
	
	@Override
	public void write(PrintWriter out, Category[] cat) throws IOException {
		out.print("順位,");
		out.print("登録,");
		out.print("部門,");
		out.print("得点,");
		out.print("名前,");
		out.print("コール,");
		out.print("運用地,");
		out.print("電話,");
		out.print("郵便,");
		out.print("住所,");
		out.print("EMAIL,");
		out.print("コメント");
		out.println();
		
		for(Category category : cat) {
			List<Summary> summaries = category.getSortedSummaryList();
			if(summaries.isEmpty()) continue;
			
			int rank = 0;
			for(Summary sum : category.getSortedSummaryList()) {
				out.print(++rank);
				out.print(",\"");
				out.print(dateFormat.format(sum.getRegisteredDate()));
				out.print("\",");
				out.print(sum.getCategoryName());
				out.print(",");
				out.print(sum.getTotal());
				out.print(",");
				out.print(sum.getName());
				out.print(",");
				out.print(sum.getCallSign());
				out.print(",");
				out.print(sum.getOperationPlace());
				out.print(",");
				out.print(sum.getTelephoneNumber());
				out.print(",");
				out.print(sum.getPostalCode());
				out.print(",");
				out.print(sum.getAddress());
				out.print(",");
				out.print(sum.getEmailAddress());
				out.print(",\"");
				out.print(sum.getComment());
				out.println("\"");
				out.flush();
			}
		}
		out.flush();
		out.close();
	}

}
