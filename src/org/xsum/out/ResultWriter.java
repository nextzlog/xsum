package org.xsum.out;

import java.io.IOException;
import java.io.PrintWriter;

import org.xsum.sum.Category;

/**
 * 集計結果を出力するためのライターのインターフェースです。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/08/21
 * 
 */
public interface ResultWriter {
	/**
	 * 指定されたストリームに指定された全カテゴリの結果を出力して閉じます。
	 * 
	 * @param out 出力先のストリーム
	 * @param cat 出力する全カテゴリ
	 * @throws IOException 入出力時の例外
	 */
	public void write(PrintWriter out, Category[] cat) throws IOException;

}
