
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

class SetFormat extends JFrame {

	SetFormat() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Format Setting");
				frame.setSize(400, 240);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setResizable(false);
				JPanel panel = new JPanel();
				frame.add(panel);
				frame.setVisible(true);
				InitLayout(panel);
			}
		});
	}

	public void InitLayout(JPanel panel) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		panel.setLayout(null);

		JTextField textField_Format = new JTextField();
		textField_Format.setBounds(10, 10, 280, 20);
		textField_Format.setEditable(false);
		panel.add(textField_Format);

		JButton button_Reset = new JButton("Reset");
		button_Reset.setBounds(300, 10, 80, 20);
		button_Reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				textField_Format.setText(null);
			}
		});
		panel.add(button_Reset);

		JTextField textField_Example = new JTextField();
		textField_Example.setBounds(10, 45, 280, 20);
		textField_Example.setEditable(false);
		panel.add(textField_Example);

		JButton button_Confirm = new JButton("Confirm");
		button_Confirm.setBounds(300, 45, 80, 20);

		panel.add(button_Confirm);

		JTable table = new JTable(7, 4);
		table.setBounds(10, 80, 370, 115);
		panel.add(table);
	}
}
