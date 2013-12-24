package UserUi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JProgressBar;
import javax.swing.JList;
import javax.swing.JScrollPane;

import Action.Classfy;
import Action.WordCut;
import Helper.FileHelper;

import java.awt.GridLayout;

public class HomeFrame2 extends JFrame {

	private JPanel contentPane;
	private JButton chooseBtn;
	private JButton startBtn;
	private JTextField textField;
	private JList classList;
	private DefaultListModel<String> classListModel;
	private JScrollPane scrollPane;
	private JPanel viewPanel;
	public JProgressBar progressBar;
	
	/**
	 * 用户选择的文件数组
	 */
	private File[] userFiles = null;
	
	/**
	 * 分类结果列表
	 */
	private HashMap<String, ArrayList<File>> resultMap = new HashMap<String,ArrayList<File>>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeFrame2 frame = new HomeFrame2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HomeFrame2() {
		initComponents();
		initData();
		bindChooseBtnEvent();
		bindStartBtnEvent();
		bindClassListEvent();
	}
	
	private void initData(){
		classListModel = new DefaultListModel<String>();
		ArrayList<File> list1=new ArrayList<File>();
		list1.add(new File("F:\\1.txt"));
		list1.add(new File("F:\\2.txt"));
		list1.add(new File("F:\\3.txt"));
		list1.add(new File("F:\\新闻发布系统.doc"));
		list1.add(new File("F:\\需求分析.doc"));
		list1.add(new File("F:\\界面.doc"));
		ArrayList<File>list2=new ArrayList<File>();
		list2.add(new File("F:\\geci.txt"));
		list2.add(new File("F:\\4.txt"));
		list2.add(new File("F:\\suanfa.doc"));
		list2.add(new File("F:\\2.txt"));
		list2.add(new File("F:\\3.txt"));
		list2.add(new File("F:\\1.txt"));
		list2.add(new File("F:\\新闻发布系统.doc"));
		list2.add(new File("F:\\需求分析.doc"));
		list2.add(new File("F:\\界面.doc"));
		list2.add(new File("F:\\4.txt"));
		ArrayList<File> list3=new ArrayList<File>();
		list3.add(new File("F:\\1.txt"));
		list3.add(new File("F:\\2.txt"));
		list3.add(new File("F:\\3.txt"));
		list3.add(new File("F:\\4.txt"));
		resultMap.put("政治", list1);
		resultMap.put("体育", list2);
		resultMap.put("文学", list3);
	}
	
	/**
	 * 绑定选择按钮的事件
	 */
	private void bindChooseBtnEvent(){
		chooseBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("E:\\android\\windows\\Classification\\trainfile"));
				chooser.setMultiSelectionEnabled(true);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					System.out.println(chooser.getSelectedFile().getAbsolutePath());
					userFiles = chooser.getSelectedFiles();
					updateViewPanel(userFiles);
				}
				//允许开始分类按钮
				startBtn.setEnabled(true);
			}
		});
	}
	
	/**
	 * 更新文件显示区域的ui
	 * @param files
	 */
	private void updateViewPanel(File[] files){
		int height = ((files.length / 5) + 1) * 120;
		viewPanel.setPreferredSize(new Dimension(400, height));
		viewPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		viewPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		for (File file : files) {
			JButton button = new JButton();
			button.setText(file.getName());
			button.setPreferredSize(new Dimension(82, 120));
			if(FileHelper.getFileExt(file).equals("doc")){
				button.setIcon(new ImageIcon("image/doc.jpg"));
			}else{
				button.setIcon(new ImageIcon("image/txt.jpg"));
			}
			button.setVerticalTextPosition(JButton.BOTTOM);
			button.setHorizontalTextPosition(JButton.CENTER);
			viewPanel.add(button);
			System.out.println(button.getText());
		}
		repaint();
	}
	
	private void bindStartBtnEvent(){
		startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//进行分词
					WordCut.run(userFiles);
					//进行分类
					Classfy.run();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void bindClassListEvent(){
		classList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
			
			}
		});
	}
	
	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		chooseBtn = new JButton("\u9009\u62E9\u6587\u4EF6");
		textField = new JTextField();
		textField.setColumns(10);
		
		startBtn = new JButton("\u5F00\u59CB\u5206\u7C7B");
		
		JButton exportBtn = new JButton("\u5BFC\u51FA\u5206\u7C7B");
		exportBtn.setEnabled(false);
		
		progressBar = new JProgressBar();
		
		classList = new JList();
		
		scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 664, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 388, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(chooseBtn)
							.addGap(6)
							.addComponent(startBtn, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addGap(3)
							.addComponent(exportBtn, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(classList, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
							.addGap(27)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 468, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(10, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(54)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(chooseBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(startBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(exportBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
						.addComponent(classList, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE)))
		);
		
		viewPanel = new JPanel();
		scrollPane.setViewportView(viewPanel);
		contentPane.setLayout(gl_contentPane);
	
	}
}
