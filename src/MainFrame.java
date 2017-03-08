import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class MainFrame extends JFrame {
	static JTextField textField_Folder, textField_Format;
	static JButton button_Choose, button_Reset, button_Filter, button_Stop;
	static JComboBox combox_Operation, combox_Process;
	static Vector<String> vector_Matched, vector_Unmatched;
	static JList list_Matched, list_Unmatched;
	static File fileRoot = null;
	static String[] string_OperationMode = { "Manual", "Automate" };
	static String[] string_ProcessMode = { "Soft", "Violent" };
	static boolean OperationMode, ProcessMode, Start = false;
	static String format = "";
	static TimerTask timeTask;
	static Timer timer;

	public static void InitLayout(JPanel panel) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		panel.setLayout(null);

		JLabel lable_Title = new JLabel("File Filter", JLabel.CENTER);
		lable_Title.setFont(new Font("宋体",Font.BOLD, 25));
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
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Choose root folder:");
				int result = fileChooser.showDialog(panel, "Choose");
				if (result == JFileChooser.APPROVE_OPTION) {
					fileRoot = fileChooser.getSelectedFile();
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
		lable_Fromat.setBounds(435, 60, 50, 20);
		panel.add(lable_Fromat);

		textField_Format = new JTextField();
		textField_Format.setBounds(495, 60, 190, 20);
		panel.add(textField_Format);

		button_Reset = new JButton("Reset");
		button_Reset.setBounds(700, 60, 80, 20);
		button_Reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textField_Format.setText(null);
			}
		});
		panel.add(button_Reset);

		JLabel lable_Operation = new JLabel("Operation Mode:", JLabel.LEFT);
		lable_Operation.setBounds(20, 100, 100, 20);
		panel.add(lable_Operation);

		combox_Operation = new JComboBox();
		combox_Operation.addItem(string_OperationMode[0]);
		combox_Operation.addItem(string_OperationMode[1]);
		combox_Operation.setBounds(140, 100, 100, 20);
		panel.add(combox_Operation);

		JLabel lable_Process = new JLabel("Process Mode:", JLabel.LEFT);
		lable_Process.setBounds(270, 100, 100, 20);
		panel.add(lable_Process);

		combox_Process = new JComboBox();
		combox_Process.addItem(string_ProcessMode[0]);
		combox_Process.addItem(string_ProcessMode[1]);
		combox_Process.setBounds(390, 100, 100, 20);
		panel.add(combox_Process);

		button_Filter = new JButton("Filter");
		button_Filter.setBounds(580, 100, 80, 20);
		button_Filter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilterPrepare();
			}
		});
		panel.add(button_Filter);

		button_Stop = new JButton("Stop");
		button_Stop.setBounds(680, 100, 80, 20);
		button_Stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Stop();
			}
		});
		panel.add(button_Stop);

		vector_Matched = new Vector<String>();
		list_Matched = new JList(vector_Matched);
		list_Matched.setBorder(BorderFactory.createTitledBorder("Matched:"));
		list_Matched.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_Matched.setBounds(10, 160, 360, 400);
		panel.add(list_Matched);

		vector_Unmatched = new Vector<String>();
		list_Unmatched = new JList(vector_Unmatched);
		list_Unmatched.setBorder(BorderFactory.createTitledBorder("Unmatched:"));
		list_Unmatched.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_Unmatched.setBounds(430, 160, 360, 400);
		panel.add(list_Unmatched);
	}

	public static void FilterPrepare() {
		button_Choose.setEnabled(false);
		textField_Format.setEditable(false);
		button_Reset.setEnabled(false);
		combox_Operation.setEnabled(false);
		combox_Process.setEnabled(false);
		button_Filter.setEnabled(false);
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

		Filter(OperationMode, ProcessMode);
	}

	public static void Filter(boolean boolean_opretaionMode, boolean boolean_processMode) {
		if (!Start)
			return;
		if (fileRoot == null)
			return;
		if (fileRoot.listFiles().length == 0)
			return;
		for (File file : fileRoot.listFiles()) {
			if (file.isFile()) {
				if (Match(file)) {
					if (vector_Matched.indexOf(file.getName()) < 0) {
						vector_Matched.add(file.getName());
						list_Matched.setListData(vector_Matched);
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
					list_Unmatched.setListData(vector_Unmatched);
					if (boolean_processMode)
						continue;
					file.delete();
				}
			}
		}

		if (boolean_opretaionMode) {
			Stop();
			return;
		}
		if (timeTask != null) {
			timeTask.cancel();
		}
		timeTask = new TimerTask() {
			@Override
			public void run() {
				Filter(boolean_opretaionMode, boolean_processMode);
			}
		};
		timer = new Timer();
		timer.schedule(timeTask, 3000);
	}

	public static boolean Match(File file) {
		// TODO

		Random r = new Random();
		if (r.nextInt() % 2 == 0) {
			return true;
		} else {
			return false;
		}
		// return true;
	}

	public static void Stop() {
		Start = false;
		button_Choose.setEnabled(true);
		textField_Format.setEditable(true);
		button_Reset.setEnabled(true);
		combox_Operation.setEnabled(true);
		combox_Process.setEnabled(true);
		button_Filter.setEnabled(true);
	}

	public static void ShowList(Vector<String> vector, JList list, ArrayList<String> arrayList) {

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("File Filter");
				frame.setSize(800, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				JPanel panel = new JPanel();
				frame.add(panel);
				frame.setVisible(true);
				InitLayout(panel);
			}
		});
	}
}