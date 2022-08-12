package org.xsum.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import org.xsum.sum.Category;
import org.xsum.sum.Summary;

/**
 * 結果発表向けにHTMLで集計結果を出力します。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/06/29
 * 
 */
public final class HTML implements ResultWriter, HTMLConstants {
	private final String header;
	private final String footer;
	
	public HTML() {
		header = loadResource("html-header.txt");
		footer = loadResource("html-footer.txt");
	}
	
	private String loadResource(String name) {
		String ls = System.getProperty("line.separator");
		InputStream is = getClass().getResourceAsStream(name);
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while((line = br.readLine()) != null) {
				sb.append(line).append(ls);
			}
			return sb.toString();
		} catch (Exception ex) {
			Logger.getGlobal().severe("cannot read " + name);
			return "";
		}
	}
	
	@Override
	public void write(PrintWriter out, Category[] cat) throws IOException {
		out.println(header);
		
		for(Category category : cat) {
			List<Summary> summaries = category.getSortedSummaryList();
			if(summaries.isEmpty()) continue;
			
			out.printf(HL_CATEGORY, category.getCategoryName());
			out.println();
			out.println(TABLE_START);
			
			out.println(TR_START);
			out.println(TD_HEADERS0);
			out.println(TD_HEADERS1);
			out.println(TD_HEADERS2);
			out.println(TD_HEADERS3);
			out.println(TD_HEADERS4);
			out.println(TD_HEADERS5);
			out.println(TR_END);
			
			int rank = 0, max = Math.min((summaries.size() -1) / 10, 6);
			for(Summary sum : summaries) {
				out.println("  <tr>");
				if(rank++ <= max) {
					out.printf(TD_AWARDED0, sum.getCallSign());
					out.println();
					out.printf(TD_AWARDED1, sum.getScore());
					out.println();
					out.printf(TD_AWARDED2, sum.getMulti());
					out.println();
					out.printf(TD_AWARDED3, sum.getTotal());
					out.println();
					out.printf(TD_AWARDED4);
					out.println();
					out.printf(TD_AWARDED5, sum.getOperationPlace());
					out.println();
				} else {
					out.printf(TD_ENTRIES0, sum.getCallSign());
					out.println();
					out.printf(TD_ENTRIES1, sum.getScore());
					out.println();
					out.printf(TD_ENTRIES2, sum.getMulti());
					out.println();
					out.printf(TD_ENTRIES3, sum.getTotal());
					out.println();
					out.printf(TD_ENTRIES4);
					out.println();
					out.printf(TD_ENTRIES5, sum.getOperationPlace());
					out.println();
				}
				out.println(TR_END);
				out.flush();
			}
			out.println(TABLE_END);
			out.println(BR);
		}
		
        out.println(footer);
		out.flush();
		out.close();
	}

}
