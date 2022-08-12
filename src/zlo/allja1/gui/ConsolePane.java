package zlo.allja1.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.xsum.cat.BasicSummary;

import leaf.xlog.model.Document;

import zlo.allja1.Processor;
import zlo.allja1.Processor.SumFileFilter;
import zlo.log2xlog.ConvertEvent;
import zlo.log2xlog.ConvertListener;
import zlo.log2xlog.Converter;

/**
 * 自動集計プログラムのGUIです。
 * 
 * 
 * @author 東大アマチュア無線クラブ
 * 
 * @since 2013/08/22
 * 
 */
public final class ConsolePane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTextArea textArea;
	private final JPanel statusPanel;
	private final JButton openButton;
	private final JButton htmlButton;
	private final JButton saveButton;
	private final JButton calcButton;
	private final JButton stopButton;
	
	private final FileChooser chooser;
	
	private File inputDir = null;
	private File htmlFile = null;
	private File saveFile = null;
	
	private SwingWorker<String, Progress> worker;
	
	private static final class Progress {
		public final Object value;
		public final Type type;
		
		public Progress(Type type, Object value) {
			this.type = type;
			this.value = value;
		}
		
		enum Type {
			TOTAL, FILE, LOG;
		};
	}
	
	public ConsolePane() {
		super(new BorderLayout(5, 5));
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText(loadResource("tutorial.txt"));
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		openButton = new JButton(new OpenAction());
		htmlButton = new JButton(new HtmlAction());
		saveButton = new JButton(new SaveAction());
		calcButton = new JButton(new CalcAction());
		stopButton = new JButton(new StopAction());
		
		statusPanel = new JPanel(new BorderLayout());
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(openButton);
		toolBar.addSeparator();
		toolBar.add(htmlButton);
		toolBar.add(saveButton);
		toolBar.addSeparator();
		toolBar.add(calcButton);
		toolBar.add(stopButton);
		
		add(new JScrollPane(textArea), BorderLayout.CENTER);
		add(toolBar, BorderLayout.NORTH);
		add(statusPanel, BorderLayout.SOUTH);
		
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setPreferredSize(new Dimension(450, 300));
		
		chooser = new FileChooser();
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
			return "";
		}
	}
	
	private final class OpenAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public OpenAction() {
			super("Open");
			putValue(SHORT_DESCRIPTION, "select log/sum directory");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(chooser.showInputDirDialog()) {
				inputDir = chooser.getSelectedFile();
				if(htmlFile != null && saveFile != null) {
					calcButton.setEnabled(true);
				}
			}
		}
	}
	
	private final class HtmlAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public HtmlAction() {
			super("HTML");
			putValue(SHORT_DESCRIPTION, "select html file to save");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(chooser.showHtmlFileDialog()) {
				htmlFile = chooser.getSelectedFile();
				if(inputDir != null && saveFile != null) {
					calcButton.setEnabled(true);
				}
			}
		}
	}
	
	private final class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public SaveAction() {
			super("Save");
			putValue(SHORT_DESCRIPTION, "select csv file to save");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(chooser.showSaveFileDialog()) {
				saveFile = chooser.getSelectedFile();
				if(inputDir != null && htmlFile != null) {
					calcButton.setEnabled(true);
				}
			}
		}
	}
	
	private final class FileChooser extends JFileChooser {
		private static final long serialVersionUID = 1L;
		
		private final FileNameExtensionFilter htmlFilter;
		private final FileNameExtensionFilter saveFilter;
		
		private final String msg = "Are you sure to replace %s?";
		
		public FileChooser() {
			htmlFilter = new FileNameExtensionFilter("*.html", "html");
			saveFilter = new FileNameExtensionFilter("*.csv", "csv");
		}
		
		public boolean showInputDirDialog() {
			setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			resetChoosableFileFilters();
			setSelectedFile(inputDir);
			setCurrentDirectory(inputDir);
			return showOpenDialog(ConsolePane.this) == APPROVE_OPTION;
		}
		
		public boolean showHtmlFileDialog() {
			setFileSelectionMode(JFileChooser.FILES_ONLY);
			resetChoosableFileFilters();
			addChoosableFileFilter(htmlFilter);
			setFileFilter(htmlFilter);
			setSelectedFile(htmlFile);
			setCurrentDirectory(htmlFile);
			return showSaveDialog(ConsolePane.this) == APPROVE_OPTION;
		}
		
		public boolean showSaveFileDialog() {
			setFileSelectionMode(JFileChooser.FILES_ONLY);
			resetChoosableFileFilters();
			addChoosableFileFilter(saveFilter);
			setFileFilter(saveFilter);
			setSelectedFile(saveFile);
			setCurrentDirectory(saveFile);
			return showSaveDialog(ConsolePane.this) == APPROVE_OPTION;
		}
		
		@Override
		public void approveSelection() {
			File file = getSelectedFile();
			if(getDialogType() == SAVE_DIALOG && file.exists()) {
				String msg = String.format(this.msg, file);
				int op = JOptionPane.showConfirmDialog(this,
					msg, "Save", JOptionPane.YES_NO_OPTION);
				if(op != JOptionPane.YES_OPTION) return;
			}
			super.approveSelection();
		}
	}
	
	private final class CalcAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public CalcAction() {
			super("Calc");
			setEnabled(false);
			putValue(SHORT_DESCRIPTION, "start processing");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			final JProgressBar bar1 = new JProgressBar(0, 100);
			final JProgressBar bar2 = new JProgressBar(0, 100);
			setEnabled(false);
			stopButton.setEnabled(true);
			statusPanel.removeAll();
			statusPanel.add(bar1, BorderLayout.NORTH);
			statusPanel.add(bar2, BorderLayout.SOUTH);
			statusPanel.revalidate();
			textArea.setText("");
			try {
				worker = new ProcessWorker(bar1, bar2);
				worker.execute();
			} catch (IOException ex) {
				textArea.append(ex.toString());
			} catch (ParseException ex) {
				textArea.append(ex.toString());
			}
		}
	}
	
	private final class ProcessWorker extends SwingWorker<String, Progress> {
		private final Converter converter;
		private final Processor processor;
		
		private final JProgressBar bar1;
		private final JProgressBar bar2;
		
		private final int formatNum;
		private int formatCount = 0;
		
		public ProcessWorker(JProgressBar bar1, JProgressBar bar2)
		throws IOException, ParseException {
			this.bar1 = bar1;
			this.bar2 = bar2;
			converter = new Converter();
			processor = new Processor();
			formatNum = converter.getAvailableFormatCount();
			converter.addConvertListener(new ConvertHandler());
		}
		
		@Override
		protected String doInBackground() throws Exception {
			if(loadSummaryFiles(loadSummaryFileList())) {
				publish(new Progress(Progress.Type.LOG, htmlFile));
				processor.outputHTML(htmlFile);
				publish(new Progress(Progress.Type.LOG, saveFile));
				processor.outputSpreadSheet(saveFile);
				return "done";
			}
			return "cancelled";
		}
		
		private List<String> loadSummaryFileList() {
			publish(new Progress(Progress.Type.LOG, "Load files..."));
			List<String> list = new ArrayList<>();
			final int suflen = ".sum".length();
			for(File sumfile : inputDir.listFiles(new SumFileFilter())) {
				String path = sumfile.getPath();
				list.add(path.substring(0, path.length() - suflen));
			}
			return list;
		}
		
		private boolean loadSummaryFiles(List<String> recordnames) {
			int cnt = 0;
			int err = 0;
			for(String rn : recordnames) if(isCancelled()) {
				return false;
			} else try {
				File file = new File(rn + ".log");
				publish(new Progress(Progress.Type.LOG, file.getName()));
				Document log = converter.load(file);
				File sum = new File(rn + ".sum");
				processor.append(new BasicSummary(log, sum));
				int pc = 100 * cnt++ / recordnames.size();
				publish(new Progress(Progress.Type.TOTAL, pc));
				publish(new Progress(Progress.Type.FILE,   0));
			} catch (Exception ex) {
				cnt++;
				err++;
				formatCount = 0;
				publish(new Progress(Progress.Type.FILE, 0));
				publish(new Progress(Progress.Type.LOG, ex));
			}
			String msg1 = cnt + " summaries loaded";
			publish(new Progress(Progress.Type.LOG, msg1));
			String msg2 = err + " summaries failed";
			publish(new Progress(Progress.Type.LOG, msg2));
			return true;
		}
		
		@Override
		protected void process(List<Progress> chunks) {
			for(Progress p : chunks) switch(p.type) {
				case TOTAL: bar1.setValue((Integer) p.value); break;
				case FILE:  bar2.setValue((Integer) p.value); break;
				case LOG: textArea.append(p.value + "\n"); break;
			}
		}
		
		@Override
		protected void done() {
			calcButton.setEnabled(true);
			stopButton.setEnabled(false);
			statusPanel.removeAll();
			statusPanel.revalidate();
			try {
				textArea.append(get() + "\n");
			} catch (InterruptedException ex) {
				textArea.append(ex + "\n");
			} catch (ExecutionException ex) {
				textArea.append(ex + "\n");
			} catch (CancellationException ex) {
				textArea.setText("cancelled\n");
			}
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(htmlFile.toURI());
				desktop.browse(saveFile.toURI());
			} catch (IOException ex) {
			}
		}
		
		private final class ConvertHandler implements ConvertListener {
			@Override
			public void onFormatTrial(ConvertEvent e) {
				int pc = 100 * formatCount++ / formatNum;
				publish(new Progress(Progress.Type.FILE, pc));
			}
		}
	}
	
	private final class StopAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public StopAction() {
			super("Stop");
			setEnabled(false);
			putValue(SHORT_DESCRIPTION, "stop processing");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(worker != null && !worker.isDone()) {
				worker.cancel(true);
			}
			worker = null;
		}
	}
	
	public static void showConsole() throws Exception {
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		JFrame frame = new JFrame("xsum ALLJA1 processor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new ConsolePane(), BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
