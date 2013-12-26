package UserUi;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.poi.hssf.util.HSSFColor.TEAL;

import Helper.FileHelper;
import javax.swing.ScrollPaneConstants;

public class FileViewFrame extends JFrame{

	private JPanel contentPane;
	private File file;
	private JTextArea textArea;
	private JScrollPane scrollPane;

	/**
	 * Create the frame.
	 */
	public FileViewFrame(File file) {
		this.file = file;
		this.setTitle(file.getAbsolutePath());
		initComponents();
		initData();
	}
	
	private void initData(){
		try {
			String content = FileHelper.readTxtOrDoc(file);
			textArea.append(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initComponents(){
		setBounds(100, 100, 600, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
		);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		contentPane.setLayout(gl_contentPane);
	}
}
