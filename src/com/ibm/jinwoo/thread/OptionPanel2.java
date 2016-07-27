package com.ibm.jinwoo.thread;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class OptionPanel2 extends JPanel {
	private static final long serialVersionUID = -5812698087409448751L;
	private JTextField textFieldid;
	private JTextField textField_1sn;
	private JTextField textField_2des;

	public OptionPanel2() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[4];
		gridBagLayout.rowHeights = new int[8];
		gridBagLayout.columnWeights = new double[] { 0.0D, 1.0D, 0.0D, 4.9E-324D };
		gridBagLayout.rowWeights = new double[] { 1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 4.9E-324D };
		setLayout(gridBagLayout);

		JLabel lblIdList = new JLabel("ID List");
		GridBagConstraints gbc_lblIdList = new GridBagConstraints();
		gbc_lblIdList.anchor = 17;
		gbc_lblIdList.insets = new Insets(10, 10, 5, 5);
		gbc_lblIdList.gridx = 0;
		gbc_lblIdList.gridy = 0;
		add(lblIdList, gbc_lblIdList);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(10, 0, 5, 5);
		gbc_scrollPane.fill = 1;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);

		JList<?> list = new JList<Object>();
		scrollPane.setViewportView(list);

		JButton btnNewButton = new JButton("New button");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = 11;
		gbc_btnNewButton.insets = new Insets(10, 0, 5, 10);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);

		JLabel lblNewLabelid = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabelid = new GridBagConstraints();
		gbc_lblNewLabelid.anchor = 13;
		gbc_lblNewLabelid.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabelid.gridx = 0;
		gbc_lblNewLabelid.gridy = 1;
		add(lblNewLabelid, gbc_lblNewLabelid);

		this.textFieldid = new JTextField();
		GridBagConstraints gbc_textFieldid = new GridBagConstraints();
		gbc_textFieldid.gridwidth = 2;
		gbc_textFieldid.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldid.fill = 2;
		gbc_textFieldid.gridx = 1;
		gbc_textFieldid.gridy = 1;
		add(this.textFieldid, gbc_textFieldid);
		this.textFieldid.setColumns(10);

		JLabel lblNewLabel_1sn = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_1sn = new GridBagConstraints();
		gbc_lblNewLabel_1sn.anchor = 13;
		gbc_lblNewLabel_1sn.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabel_1sn.gridx = 0;
		gbc_lblNewLabel_1sn.gridy = 2;
		add(lblNewLabel_1sn, gbc_lblNewLabel_1sn);

		this.textField_1sn = new JTextField();
		GridBagConstraints gbc_textField_1sn = new GridBagConstraints();
		gbc_textField_1sn.gridwidth = 2;
		gbc_textField_1sn.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1sn.fill = 2;
		gbc_textField_1sn.gridx = 1;
		gbc_textField_1sn.gridy = 2;
		add(this.textField_1sn, gbc_textField_1sn);
		this.textField_1sn.setColumns(10);

		JLabel lblNewLabel_2st = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_2st = new GridBagConstraints();
		gbc_lblNewLabel_2st.insets = new Insets(0, 10, 5, 5);
		gbc_lblNewLabel_2st.gridx = 0;
		gbc_lblNewLabel_2st.gridy = 3;
		add(lblNewLabel_2st, gbc_lblNewLabel_2st);

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = 1;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 3;
		add(scrollPane_1, gbc_scrollPane_1);

		JTextArea textAreast = new JTextArea();
		scrollPane_1.setViewportView(textAreast);

		JLabel d = new JLabel("New label");
		GridBagConstraints gbc_d = new GridBagConstraints();
		gbc_d.anchor = 13;
		gbc_d.insets = new Insets(0, 10, 5, 5);
		gbc_d.gridx = 0;
		gbc_d.gridy = 4;
		add(d, gbc_d);

		this.textField_2des = new JTextField();
		GridBagConstraints gbc_textField_2des = new GridBagConstraints();
		gbc_textField_2des.gridwidth = 2;
		gbc_textField_2des.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2des.fill = 2;
		gbc_textField_2des.gridx = 1;
		gbc_textField_2des.gridy = 4;
		add(this.textField_2des, gbc_textField_2des);
		this.textField_2des.setColumns(10);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridwidth = 3;
		gbc_panel.fill = 1;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 5;
		add(panel, gbc_panel);

		JButton btnNewButton_1 = new JButton("New button");
		panel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("New button");
		panel.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("New button");
		panel.add(btnNewButton_3);

		JLabel lblWarning = new JLabel("warning");
		GridBagConstraints gbc_lblWarning = new GridBagConstraints();
		gbc_lblWarning.gridwidth = 3;
		gbc_lblWarning.insets = new Insets(0, 10, 10, 10);
		gbc_lblWarning.gridx = 0;
		gbc_lblWarning.gridy = 6;
		add(lblWarning, gbc_lblWarning);
	}
}
