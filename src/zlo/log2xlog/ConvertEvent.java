package zlo.log2xlog;

import java.util.EventObject;

/**
 * コンバータによる交信記録の変換イベントです。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/08/21
 * 
 */
public class ConvertEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private final String format;
	
	/**
	 * イベントの発生源とフォーマット名を指定します。
	 * 
	 * @param source イベントの発生源
	 * @param format フォーマット名
	 */
	public ConvertEvent(Object source, String format) {
		super(source);
		this.format = format;
	}
	
	/**
	 * イベントに関連付けられたフォーマットを返します。
	 * 
	 * @return フォーマット
	 */
	public String getFormatName() {
		return format;
	}

}
