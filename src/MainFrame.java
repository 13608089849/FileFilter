import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class MainFrame extends JFrame {

	public static void InitLayout(JPanel panel) {
		// 确保一个漂亮的外观风格
		JFrame.setDefaultLookAndFeelDecorated(true);

		panel.setLayout(null);

		JLabel lable_Title = new JLabel("File Filter", JLabel.CENTER);
		lable_Title.setBounds(350, 10, 100, 50);
		panel.add(lable_Title);

		JLabel lable_Folder = new JLabel("Folder:", JLabel.LEFT);
		lable_Folder.setBounds(20, 60, 50, 20);
		panel.add(lable_Folder);

		JTextField textField_Folder = new JTextField();
		textField_Folder.setBounds(80, 60, 200, 20);
		panel.add(textField_Folder);

		JButton button_Choose = new JButton("Choose");
		button_Choose.setBounds(300, 60, 80, 20);
		panel.add(button_Choose);

		JLabel lable_Fromat = new JLabel("Fromat:", JLabel.CENTER);
		lable_Fromat.setBounds(430, 60, 50, 20);
		panel.add(lable_Fromat);

		JTextField textField_Format = new JTextField();
		textField_Format.setBounds(500, 60, 200, 20);
		panel.add(textField_Format);

		JLabel lable_Operation = new JLabel("Operation Mode:", JLabel.LEFT);
		lable_Operation.setBounds(20, 100, 100, 20);
		panel.add(lable_Operation);

		JComboBox combox_Operation = new JComboBox();
		combox_Operation.addItem("Automate");
		combox_Operation.addItem("Manual");
		combox_Operation.setBounds(140, 100, 100, 20);
		panel.add(combox_Operation);

		JLabel lable_Process = new JLabel("Process Mode:", JLabel.LEFT);
		lable_Process.setBounds(270, 100, 100, 20);
		panel.add(lable_Process);

		JComboBox combox_Process = new JComboBox();
		combox_Process.addItem("Violent");
		combox_Process.addItem("Soft");
		combox_Process.setBounds(390, 100, 100, 20);
		panel.add(combox_Process);

		JButton button_Filter = new JButton("Filter");
		button_Filter.setBounds(580, 100, 80, 20);
		panel.add(button_Filter);

		JButton button_Stop = new JButton("Stop");
		button_Stop.setBounds(680, 100, 80, 20);
		panel.add(button_Stop);

		Vector<String> vector_Matched = new Vector<String>();
		vector_Matched.add("File 1");
		vector_Matched.add("File 2");
		vector_Matched.add("File 3");
		JList list_Matched = new JList(vector_Matched);
		list_Matched.setBorder(BorderFactory.createTitledBorder("Matched:"));
		list_Matched.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_Matched.setBounds(10, 160, 360, 400);
		panel.add(list_Matched);

		Vector<String> vector_Unmatched = new Vector<String>();
		vector_Unmatched.add("File 1");
		vector_Unmatched.add("File 2");
		vector_Unmatched.add("File 3");
		JList list_Unmatched = new JList(vector_Unmatched);
		list_Unmatched.setBorder(BorderFactory.createTitledBorder("Unmatched:"));
		list_Unmatched.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_Unmatched.setBounds(410, 160, 360, 400);
		panel.add(list_Unmatched);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// 创建 JFrame 实例
				JFrame frame = new JFrame("File Filter");
				// Setting the width and height of frame
				frame.setSize(800, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JPanel panel = new JPanel();
				frame.add(panel);

				InitLayout(panel);

				frame.setVisible(true);
			}
		});
	}
}