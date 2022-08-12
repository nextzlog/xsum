package zlo.log2xlog;

import java.util.EventListener;

/**
 * コンバータによる交信記録の変換イベントを受け取ります。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/08/21
 * 
 */
public interface ConvertListener extends EventListener {
	/**
	 * 指定されたフォーマットでの読み込みを試みる時に通知されます。
	 * 
	 * @param e イベント
	 */
	public void onFormatTrial(ConvertEvent e);
}
