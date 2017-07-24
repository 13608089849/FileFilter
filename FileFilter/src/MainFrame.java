import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;

public class MainFrame extends JFrame {
	static SetFormat setFormat;
	private static JTextField textField_Folder;
	public static JTextField textField_Format;
	private static JButton button_Choose, button_Set, button_Filter, button_Stop, button_Export, button_Delete;
	private static JComboBox combox_Operation, combox_Process;
	private static Vector<String> vector_Matched, vector_Unmatched;
	private static JList list_Matched, list_Unmatched;
	private static File fileRoot = null;
	private static File fileExport = null;
	private static String[] string_OperationMode = { "Manual", "Automate" };
	private static String[] string_ProcessMode = { "Soft", "Violent" };
	private static boolean OperationMode, ProcessMode, Start = false;
	public static String format = "";
	private static TimerTask timeTask;
	private static Timer timer;
	public static String RegEx = "";

	private static void initLayout(JPanel panel) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		panel.setLayout(null);

		JLabel lable_Title = new JLabel("File Filter", JLabel.CENTER);
		lable_Title.setFont(new Font("宋体", Font.BOLD, 25));
		lable_Title.setBounds(300, 0, 200, 60);
		panel.add(lable_Title);

		JLabel lable_Folder = new JLabel("Folder:", JLabel.LEFT);
		lable_Folder.setBounds(25, 60, 50, 20);
		panel.add(lable_Folder);

		textField_Folder = new JTextField();
		textField_Folder.setBounds(75, 60, 190, 20);
		textField_Folder.setEditable(false);
		panel.add(textField_Folder);

		button_Choose = new JButton("Choose");
		button_Choose.setBounds(280, 60, 80, 20);
		button_Choose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser_Open = new JFileChooser();
				fileChooser_Open.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser_Open.setDialogTitle("Choose root folder:");
				int result = fileChooser_Open.showDialog(panel, "Choose");
				if (result == JFileChooser.APPROVE_OPTION) {
					fileRoot = fileChooser_Open.getSelectedFile();
					textField_Folder.setText(fileRoot.getAbsolutePath());
					vector_Matched.clear();
					vector_Unmatched.clear();
					list_Matched.setListData(vector_Matched);
					list_Unmatched.setListData(vector_Unmatched);
				}
			}
		});
		panel.add(button_Choose);

		JLabel lable_Fromat = new JLabel("Fromat:", JLabel.CENTER);
		lable_Fromat.setBounds(440, 60, 50, 20);
		panel.add(lable_Fromat);

		textField_Format = new JTextField();
		textField_Format.setBounds(490, 60, 190, 20);
		textField_Format.setEditable(false);
		panel.add(textField_Format);

		button_Set = new JButton("Set");
		button_Set.setBounds(695, 60, 80, 20);
		button_Set.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFormat = new SetFormat();
				setFormat.setVisible(true);
			}
		});
		panel.add(button_Set);

		JLabel lable_Operation = new JLabel("Operation Mode:", JLabel.LEFT);
		lable_Operation.setBounds(20, 110, 100, 20);
		panel.add(lable_Operation);

		combox_Operation = new JComboBox();
		combox_Operation.addItem(string_OperationMode[0]);
		combox_Operation.addItem(string_OperationMode[1]);
		combox_Operation.setBounds(140, 110, 100, 20);
		panel.add(combox_Operation);

		JLabel lable_Process = new JLabel("Process Mode:", JLabel.LEFT);
		lable_Process.setBounds(270, 110, 100, 20);
		panel.add(lable_Process);

		combox_Process = new JComboBox();
		combox_Process.addItem(string_ProcessMode[0]);
		combox_Process.addItem(string_ProcessMode[1]);
		combox_Process.setBounds(390, 110, 100, 20);
		panel.add(combox_Process);

		button_Filter = new JButton("Filter");
		button_Filter.setBounds(600, 110, 80, 20);
		button_Filter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField_Folder.getText().isEmpty() || textField_Format.getText().isEmpty())
					return;
				filterPrepare();
			}
		});
		panel.add(button_Filter);

		button_Stop = new JButton("Stop");
		button_Stop.setBounds(700, 110, 80, 20);
		button_Stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		panel.add(button_Stop);

		vector_Matched = new Vector<String>();
		list_Matched = new JList(vector_Matched);
		list_Matched.setBorder(BorderFactory.createTitledBorder("Matched:"));
		list_Matched.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane listScroller_Matched = new JScrollPane(list_Matched);
		listScroller_Matched.setPreferredSize(new Dimension(360, 400));
		listScroller_Matched.setLayout(new ScrollPaneLayout());
		listScroller_Matched.setBounds(10, 150, 360, 380);
		panel.add(listScroller_Matched);

		vector_Unmatched = new Vector<String>();
		list_Unmatched = new JList(vector_Unmatched);
		list_Unmatched.setBorder(BorderFactory.createTitledBorder("Unmatched:"));
		list_Unmatched.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane listScroller_Unmatched = new JScrollPane(list_Unmatched);
		listScroller_Unmatched.setPreferredSize(new Dimension(364, 400));
		listScroller_Unmatched.setLayout(new ScrollPaneLayout());
		listScroller_Unmatched.setBounds(430, 150, 360, 380);
		panel.add(listScroller_Unmatched);

		button_Export = new JButton("Export");
		button_Export.setBounds(140, 540, 80, 20);
		button_Export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser_Export = new JFileChooser();
				fileChooser_Export.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser_Export.setDialogTitle("Export to:");
				int result = fileChooser_Export.showDialog(panel, "Export");
				if (result == JFileChooser.APPROVE_OPTION) {
					fileExport = fileChooser_Export.getSelectedFile();
					try {
						if (!fileExport.exists()) {
							fileExport.mkdirs();
						}
						for (String fileName : vector_Matched) {
							String resName = fileRoot.getAbsolutePath() + "/" + fileName;
							String desName = fileExport.getAbsolutePath() + "/" + fileName;
							File resFile = new File(resName);
							File desFile = new File(desName);
							if (resFile.exists())
								resFile.renameTo(desFile);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(button_Export);

		button_Delete = new JButton("Delete");
		button_Delete.setBounds(580, 540, 80, 20);
		button_Delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					for (String fileName : vector_Unmatched) {
						String deleteName = fileRoot.getAbsolutePath() + "/" + fileName;
						File deleteFile = new File(deleteName);
						if (deleteFile.exists())
							deleteFile.delete();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		panel.add(button_Delete);
	}

	private static void filterPrepare() {
		button_Choose.setEnabled(false);
		button_Set.setEnabled(false);
		combox_Operation.setEnabled(false);
		combox_Process.setEnabled(false);
		button_Filter.setEnabled(false);
		button_Export.setEnabled(false);
		button_Delete.setEnabled(false);
		format = textField_Format.getText();

		if (combox_Operation.getSelectedItem().toString().equals(string_OperationMode[0])) {
			OperationMode = true;
		} else {
			OperationMode = false;
		}
		if (combox_Process.getSelectedItem().toString().equals(string_ProcessMode[0])) {
			ProcessMode = true;
		} else {
			ProcessMode = false;
		}

		Start = true;

		filter(OperationMode, ProcessMode);
	}

	private static void filter(boolean boolean_opretaionMode, boolean boolean_processMode) {
		if (!Start)
			return;
		if (fileRoot == null)
			return;
		if (fileRoot.listFiles().length == 0)
			return;
		for (File file : fileRoot.listFiles()) {
			if (file.isFile()) {
				if (match(file)) {
					if (vector_Matched.indexOf(file.getName()) < 0) {
						vector_Matched.add(file.getName());
					}
				} else {
					if (vector_Unmatched.indexOf(file.getName()) < 0) {
						vector_Unmatched.add(file.getName());
						list_Unmatched.setListData(vector_Unmatched);
						if (boolean_processMode)
							continue;
						file.delete();
					}
				}
			} else if (file.isDirectory()) {
				if (vector_Unmatched.indexOf(file.getName()) < 0) {
					vector_Unmatched.add(file.getName());
					if (boolean_processMode)
						continue;
					file.delete();
				}
			}
		}
		list_Matched.setListData(vector_Matched);
		list_Unmatched.setListData(vector_Unmatched);
		if (boolean_opretaionMode) {
			stop();
			return;
		}
		if (vector_Matched.size() + vector_Unmatched.size() > fileRoot.listFiles().length) {
			// Stop();
			// return;
		}
		if (timeTask != null) {
			timeTask.cancel();
		}
		timeTask = new TimerTask() {
			@Override
			public void run() {
				filter(boolean_opretaionMode, boolean_processMode);
			}
		};
		timer = new Timer();
		timer.schedule(timeTask, 2000);
	}

	public static void setFormat(String regEx) {
		RegEx = regEx;
	}

	private static boolean match(File file) {
		System.out.println(RegEx);
		String str = file.getName();
		System.out.println(str);
		Pattern pattern = Pattern.compile(RegEx);
		Matcher matcher = pattern.matcher(str);
		System.out.println(matcher.matches());
		if (matcher.matches())
			return true;
		return false;
	}

	private static void stop() {
		Start = false;
		button_Choose.setEnabled(true);
		button_Set.setEnabled(true);
		combox_Operation.setEnabled(true);
		combox_Process.setEnabled(true);
		button_Filter.setEnabled(true);
		button_Export.setEnabled(true);
		button_Delete.setEnabled(true);
		list_Matched.updateUI();
		list_Unmatched.updateUI();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("File Filter ©ZengYu of UESTC");
				frame.setSize(800, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				JPanel panel = new JPanel();
				frame.add(panel);
				frame.setVisible(true);
				initLayout(panel);
			}
		});
	}
}