package com.ibm.jinwoo.thread;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class OptionPanel1 extends JPanel {
	private static final long serialVersionUID = 1606913711808075156L;
	private JTextField textField;

	public OptionPanel1(Configuration cfg) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[5];
		gridBagLayout.rowHeights = new int[7];
		gridBagLayout.columnWeights = new double[] { 0.0D, 1.0D, 0.0D, 0.0D, 4.9E-324D };
		gridBagLayout.rowWeights = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 4.9E-324D };
		setLayout(gridBagLayout);

		JLabel lblDefaultDirectory = new JLabel("Default directory");
		GridBagConstraints gbc_lblDefaultDirectory = new GridBagConstraints();
		gbc_lblDefaultDirectory.fill = 2;
		gbc_lblDefaultDirectory.insets = new Insets(10, 10, 5, 5);
		gbc_lblDefaultDirectory.gridx = 0;
		gbc_lblDefaultDirectory.gridy = 0;
		add(lblDefaultDirectory, gbc_lblDefaultDirectory);

		this.textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(10, 0, 5, 5);
		gbc_textField.fill = 2;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(this.textField, gbc_textField);
		this.textField.setColumns(10);

		JButton btnBrowse = new JButton("Browse");
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.fill = 2;
		gbc_btnBrowse.insets = new Insets(10, 0, 5, 10);
		gbc_btnBrowse.gridx = 2;
		gbc_btnBrowse.gridy = 0;
		add(btnBrowse, gbc_btnBrowse);

		JLabel lblColor = new JLabel("Color");
		GridBagConstraints gbc_lblColor = new GridBagConstraints();
		gbc_lblColor.anchor = 17;
		gbc_lblColor.insets = new Insets(0, 10, 5, 5);
		gbc_lblColor.gridx = 0;
		gbc_lblColor.gridy = 1;
		add(lblColor, gbc_lblColor);

		JComboBox<String> comboBox = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = 2;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		add(comboBox, gbc_comboBox);

		JCheckBox chckbxVerboseMode = new JCheckBox("Verbose Mode");
		GridBagConstraints gbc_chckbxVerboseMode = new GridBagConstraints();
		gbc_chckbxVerboseMode.anchor = 13;
		gbc_chckbxVerboseMode.insets = new Insets(0, 10, 5, 5);
		gbc_chckbxVerboseMode.gridx = 0;
		gbc_chckbxVerboseMode.gridy = 2;
		add(chckbxVerboseMode, gbc_chckbxVerboseMode);

		JCheckBox chckbxSaveOption = new JCheckBox("Save Option");
		GridBagConstraints gbc_chckbxSaveOption = new GridBagConstraints();
		gbc_chckbxSaveOption.anchor = 17;
		gbc_chckbxSaveOption.insets = new Insets(0, 5, 5, 5);
		gbc_chckbxSaveOption.gridx = 1;
		gbc_chckbxSaveOption.gridy = 2;
		add(chckbxSaveOption, gbc_chckbxSaveOption);

		JLabel lblLookAndFeel = new JLabel("Look and Feel");
		GridBagConstraints gbc_lblLookAndFeel = new GridBagConstraints();
		gbc_lblLookAndFeel.insets = new Insets(0, 10, 5, 5);
		gbc_lblLookAndFeel.gridx = 0;
		gbc_lblLookAndFeel.gridy = 3;
		add(lblLookAndFeel, gbc_lblLookAndFeel);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(1, null, null, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 10, 5, 10);
		gbc_panel.fill = 1;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		add(panel, gbc_panel);

		JRadioButton rdbtnMetal = new JRadioButton("Metal");
		panel.add(rdbtnMetal);

		JRadioButton rdbtnNimbus = new JRadioButton("Nimbus");
		panel.add(rdbtnNimbus);

		JRadioButton rdbtnSystem = new JRadioButton("System");
		panel.add(rdbtnSystem);

		JButton btnOk = new JButton("OK");
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 10, 5);
		gbc_btnOk.gridx = 1;
		gbc_btnOk.gridy = 5;
		add(btnOk, gbc_btnOk);
	}
}
