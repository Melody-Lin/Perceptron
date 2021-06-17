package NN.hw1;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class gui extends JFrame implements KeyListener, ActionListener {
	JFrame jf = new JFrame("Input");
	JPanel panel = new JPanel();
	JLabel l1 = new JLabel("學習率");
	JLabel l2 = new JLabel("收斂條件");
	JLabel l3 = new JLabel("選擇檔案");
	JTextField t1 = new JTextField();
	JTextField t2 = new JTextField();
	JButton btn = new JButton("OK");
	JComboBox jc = new JComboBox();

	public gui() {
		jf.setSize(300, 280);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		panel.setBounds(0, 0, 100, 150);
		panel.setLayout(null);

		l1.setBounds(50, 5, 100, 50);
		l1.setFont(new Font(null, Font.PLAIN, 16));
		panel.add(l1);

		t1.setBounds(150, 10, 70, 40);
		panel.add(t1);

		l2.setBounds(50, 55, 120, 50);
		l2.setFont(new Font(null, Font.PLAIN, 16));
		panel.add(l2);

		t2.setBounds(150, 60, 70, 40);
		panel.add(t2);

		l3.setBounds(50, 105, 120, 50);
		l3.setFont(new Font(null, Font.PLAIN, 16));
		panel.add(l3);

		jc.setBounds(150, 105, 130, 50);
		jc.addItem("perceptron1");
		jc.addItem("perceptron2");
		jc.addItem("2Ccircle1");
		jc.addItem("2Circle1");
		jc.addItem("2Circle2");
		jc.addItem("2CloseS");
		jc.addItem("2CloseS2");
		jc.addItem("2CloseS3");
		jc.addItem("2cring");
		jc.addItem("2CS");
		jc.addItem("2Hcircle1");
		jc.addItem("2ring");
		jc.addActionListener(this);
		panel.add(jc);

		btn.setBounds(115, 170, 60, 40);
		btn.addActionListener(this);
		panel.add(btn);

		jf.setContentPane(panel);
		jf.setVisible(true);
	}

	public void keyPressed(KeyEvent event) {

	}

	public void keyReleased(KeyEvent event) {

	}

	public void keyTyped(KeyEvent event) {

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn) {
			setVisible(false);
			try {
				train tn1 = new train(Float.parseFloat(t1.getText()), Integer.valueOf(t2.getText()),
						jc.getSelectedIndex());
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
