
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Random;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

class SetFormat extends JFrame {
	JTextField textField_Format, textField_Example;
	JTable table;
	JTextField textField_length_int, textField_length_char, textField_length_string,
			textField_length_symbol, textField_value_postfix;
	JComboBox<String> combobox;
	JButton button;
	JLabel label_null,label_1;
	String[] comboboxItem = { "", "!", "\"", "#", "$", "%", "&", "\'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";",
			"<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "{", "|", "}", "~" };

	SetFormat() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Format Setting");
				frame.setSize(400, 300);
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

		textField_Format = new JTextField();
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

		textField_Example = new JTextField();
		textField_Example.setBounds(10, 45, 280, 20);
		textField_Example.setEditable(false);
		panel.add(textField_Example);

		JButton button_Confirm = new JButton("Confirm");
		button_Confirm.setBounds(300, 45, 80, 20);
		button_Confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MainFrame.textField_Format.setText(textField_Format.getText());
			}
		});

		panel.add(button_Confirm);

		String[] HeaderString = { "Type", "Length", "Value", "Add" };
		JTextField textFieldNull = new JTextField();
		textFieldNull.setEditable(false);
		Object[] RawObject1 = { "Int", textField_length_int, label_null, button };
		Object[] RawObject2 = { "Float", label_1, label_null, button };
		Object[] RawObject3 = { "Chinese", textField_length_char, label_null, button };
		Object[] RawObject4 = { "English", textField_length_string, label_null, button };
		Object[] RawObject5 = { "Symbol", textField_length_symbol, combobox, button };
		Object[] RawObject6 = { "Postfix", label_1, textField_value_postfix, button };
		Object[][] tableObject = { RawObject1, RawObject2, RawObject3, RawObject4, RawObject5, RawObject6 };

		TableModel tableModel = new TableModel(HeaderString, tableObject);
		table = new JTable(tableModel);
		table.putClientProperty("terminateEditOnFocusLost", true);
		table.setRowHeight(22);

		table.setDefaultRenderer(JButton.class, new TableRenderer());

		TableRenderer tableRenderer = new TableRenderer();
		TableEditor tableEditor = new TableEditor();
		table.getColumnModel().getColumn(1).setCellRenderer(tableRenderer);
		table.getColumnModel().getColumn(1).setCellEditor(tableEditor);
		table.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
		table.getColumnModel().getColumn(2).setCellEditor(tableEditor);
		table.getColumnModel().getColumn(3).setCellRenderer(tableRenderer);
		table.getColumnModel().getColumn(3).setCellEditor(tableEditor);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 90, 370, 155);

		panel.add(scrollPane);
	}

	public class TableModel extends AbstractTableModel {
		private String columnNames[];
		private Object data[][];

		public TableModel() {
			super();
		}

		public TableModel(String[] columnNames, Object[][] data) {
			this();
			this.columnNames = columnNames;
			this.data = data;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			if (column == 0) {
				return false;
			} else if (column == 1 && row == 1) {
				return false;
			} else if (column == 1 && row == 5){
				return false;
			}else if (column == 2 && row <4) {
				return false;
			} else {
				return true;
			}
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}

		public String getColumnName(int c) {
			return columnNames[c];
		}

		public Class<?> getColumnClass(int columnIndex) {
			return data[0][columnIndex].getClass();
		}
	}

	public class TableEditor extends AbstractCellEditor implements TableCellEditor {
		public TableEditor() {
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
//					// TODO Auto-generated method stub
//					String Value = null;
//					String Example = null;
//					int index_row = table.getSelectedRow();
//					if (index_row == 0) {
//						Example = "5";
//						int length;
//						if (textField_length_int.getText().equals("*")) {
//							length = new Random().nextInt(10);
//							Value = "<int(*)>";
//						} else {
//							length = Integer.parseInt(textField_length_int.getText());
//							Value = "<int(" + length + ")>";
//						}
//						if (length > 1) {
//							int i;
//							for (i = 1; i < length; i++) {
//								Example += "5";
//							}
//						}
//					} else if (index_row == 1) {
//						Value = textField_value_float.getText();
//					} else if (index_row == 2) {
//						Value = textField_length_char.getText();
//					} else if (index_row == 3) {
//						Value = textField_length_string.getText();
//					} else if (index_row == 4) {
//						Value = combobox.getSelectedItem().toString();
//						String item = Value;
//						int length = Integer.parseInt(textField_length_symbol.getText());
//						if (length > 1) {
//							int i;
//							for (i = 1; i < length; i++) {
//								Value += item;
//							}
//						}
//					} else if (index_row == 5) {
//						Value = textField_value_postfix.getText();
//					}
//					textField_Format.setText(textField_Format.getText() + Value);
//					textField_Example.setText(textField_Example.getText() + Example);
//
				}
			});
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (column == 1) {
				if (row == 0)
					return textField_length_int;
				else if (row == 1)
					return label_1;
				else if (row == 2)
					return textField_length_char;
				else if (row == 3)
					return textField_length_string;
				else if (row == 4)
					return textField_length_symbol;
			} else if (column == 2 && row == 4) {
				return combobox;
			} else if (column == 2 && row == 5) {
				return textField_value_postfix;
			} else if (column == 3) {
				return button;
			}
			return label_null;
		}

		@Override
		public boolean stopCellEditing() {
			return super.stopCellEditing();
		}
	}

	public class TableRenderer implements TableCellRenderer {
		TableRenderer() {
			textField_length_int = new JTextField("*");
			textField_length_char = new JTextField("*");
			textField_length_string = new JTextField("*");
			textField_length_symbol = new JTextField("1");
			textField_value_postfix = new JTextField("*");
			button = new JButton("Add");
			combobox = new JComboBox<>(comboboxItem);
			label_null = new JLabel("");
			label_1 = new JLabel("1");
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (column == 1) {
				if (row == 0)
					return textField_length_int;
				else if (row == 1)
					return label_1;
				else if (row == 2)
					return textField_length_char;
				else if (row == 3)
					return textField_length_string;
				else if (row == 4)
					return textField_length_symbol;
			} else if (column == 2 && row == 4) {
				return combobox;
			} else if (column == 2 && row == 5) {
				return textField_value_postfix;
			} else if (column == 3) {
				return button;
			}
			return label_null;
		}
	}
}
