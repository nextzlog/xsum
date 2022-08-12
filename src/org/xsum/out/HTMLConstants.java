package org.xsum.out;

/**
 * 結果発表用HTMLファイルのテーブルの定数を設定します。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/08/21
 * 
 */
public interface HTMLConstants {
	public static final String HL_CATEGORY = "<font color=\"#000090\"><b> %s </b></font>";

	public static final String TABLE_START = "<table border=\"1\">";
	public static final String TABLE_END   = "</table>";

	public static final String TR_START    = "  <tr>";
	public static final String TR_END      = "  </tr>";

	public static final String TD_HEADERS0 = "    <td width=\"100px\" bgcolor=\"#EEEEEE\" align=\"center\"> <b>Callsign</b></td>";
	public static final String TD_HEADERS1 = "    <td width=\"60px\"  bgcolor=\"#EEEEEE\" align=\"center\"> <b>Score</b></td>";
	public static final String TD_HEADERS2 = "    <td width=\"60px\"  bgcolor=\"#EEEEEE\" align=\"center\"> <b>Multi</b></td>";
	public static final String TD_HEADERS3 = "    <td width=\"60px\"  bgcolor=\"#EEEEEE\" align=\"center\"> <b>Total</b></td>";
	public static final String TD_HEADERS4 = "    <td width=\"1px\"> </td>";
	public static final String TD_HEADERS5 = "    <td width=\"200px\" bgcolor=\"#EEEEEE\" align=\"center\"> <b>Place</b></td>";

	public static final String TD_ENTRIES0 = "    <td width=\"100px\">%s</td>";
	public static final String TD_ENTRIES1 = "    <td width=\"60px\" align=\"right\">%s</td>";
	public static final String TD_ENTRIES2 = "    <td width=\"60px\" align=\"right\">%s</td>";
	public static final String TD_ENTRIES3 = "    <td width=\"60px\" align=\"right\">%s</td>";
	public static final String TD_ENTRIES4 = "    <td width=\"1px\"></td>";
	public static final String TD_ENTRIES5 = "    <td width=\"200px\">%s</td>";

	public static final String TD_AWARDED0 = "    <td width=\"100px\" style=\"color:#EE0000\">%s</td>";
	public static final String TD_AWARDED1 = "    <td width=\"60px\"  style=\"color:#EE0000\" align=\"right\">%s</td>";
	public static final String TD_AWARDED2 = "    <td width=\"60px\"  style=\"color:#EE0000\" align=\"right\">%s</td>";
	public static final String TD_AWARDED3 = "    <td width=\"60px\"  style=\"color:#EE0000\" align=\"right\">%s</td>";
	public static final String TD_AWARDED4 = "    <td width=\"1px\"></td>";
	public static final String TD_AWARDED5 = "    <td width=\"200px\" style=\"color:#EE0000\">%s</td>";

	public static final String BR          = "<br>";
}
