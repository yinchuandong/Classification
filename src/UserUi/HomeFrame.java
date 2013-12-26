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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JProgressBar;
import javax.swing.JList;
import javax.swing.JScrollPane;

import Action.Classfy;
import Action.WordCut;
import Helper.FileHelper;

import java.awt.GridLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.poi.util.TempFile;

import Widget.ImageButton;
import javax.swing.JLabel;

public class HomeFrame extends JFrame {

	private JPanel contentPane;
	private JButton chooseBtn;
	private JButton clearBtn;
	private JButton startBtn;
	private JButton exportBtn;
	private JButton stopBtn;
	private JList classList;
	private DefaultListModel<String> classListModel;
	private JScrollPane scrollPane;
	private JPanel viewPanel;
	public JProgressBar progressBar;//进度条
	private JLabel progressLabel; //进度百分比
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmNewMenuItem_2;

	private JPopupMenu popupMenu;
	
	/**
	 * 用户选择的文件数组
	 */
	private ArrayList<File> userFiles = new ArrayList<>();
	
	/**
	 * 分类结果列表
	 */
	private HashMap<String, ArrayList<File>> resultMap = new HashMap<String,ArrayList<File>>();
	
	private Thread classThread;
	private Thread exportThread;
	private int classiyProgress = 2; //分类的默认进度条长度
	private File selectedFile = null; //选中的file

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeFrame frame = new HomeFrame();
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
	public HomeFrame() {
		setResizable(false);
		initComponents();
		initData();
	}
	
	private void initData(){
		bindPopMenuEvent();
		bindChooseBtnEvent();
		bindClearBtnEvent();
		bindStartBtnEvent();
		bindClassListEvent();
		bindExportBtnEvent();
		bindStopBtnEvent();
	}
	
	/**
	 * 动态更新进度条
	 * @param curIndex
	 */
	public synchronized void updateProgressBar(int curIndex){
		if(curIndex <= progressBar.getMaximum()){
			progressBar.setValue(curIndex);
			int rate = (int)(((double)curIndex)/progressBar.getMaximum() *100 );
			progressLabel.setText(rate+"%");
		}
	}
	
	/**
	 * 更新左侧分类查看器列表
	 */
	private void updateClassList(){
		classListModel = new DefaultListModel<String>();
		Iterator<String> iterator = resultMap.keySet().iterator();
		while (iterator.hasNext()) {
			String className = (String) iterator.next();
			classListModel.addElement(className);
		}
		classList.setModel(classListModel);
	}
	
	
	/**
	 * 更新文件显示区域的ui
	 * @param files
	 */
	private void updateViewPanel(ArrayList<File> files){
		int len = (files == null) ? 0 : files.size();
		int height = ((len / 5) + 1) * 140;
		viewPanel = new JPanel();
		scrollPane.setViewportView(viewPanel);
		viewPanel.setPreferredSize(new Dimension(400, height));
		viewPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		viewPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if (files == null) {
			return ;
		}
		for (File file : files) {
			
			ImageButton button = new ImageButton(file);
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
			final File tmpFile = file;
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					if (e.getClickCount() == 2) {//如果是双击
						JFrame frame = new FileViewFrame(tmpFile);
						frame.setDefaultCloseOperation(HIDE_ON_CLOSE);
						frame.setVisible(true);
					}else{
						if (e.isMetaDown()) {
							selectedFile = tmpFile;
							ImageButton clickBtn =  (ImageButton)e.getSource();
							int left = clickBtn.getX() + scrollPane.getX();
							int top = clickBtn.getY() + scrollPane.getY() + clickBtn.getHeight();
							popupMenu.show(HomeFrame.this, left, top);
						}
					}
				}
			});
			System.out.println(button.getText());
		}
		repaint();
	}
	
	/**
	 * 绑定弹出菜单事件
	 */
	private void bindPopMenuEvent(){
		popupMenu = new JPopupMenu();
		JMenuItem openItem = new JMenuItem("预览");
		JMenuItem delItem = new JMenuItem("删除");
		popupMenu.add(openItem);
		popupMenu.add(delItem);
		
		//预览
		openItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (selectedFile != null) {
					JFrame frame = new FileViewFrame(selectedFile);
					frame.setDefaultCloseOperation(HIDE_ON_CLOSE);
					frame.setVisible(true);
				}
			}
		});
		
		//删除文件
		delItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedFile != null) {
					userFiles.remove(selectedFile);
					updateViewPanel(userFiles);
					selectedFile = null;
					progressBar.setMaximum(userFiles.size()+classiyProgress);
				}
			}
		});
	}
	
	
	/**
	 * 绑定选择按钮的事件
	 */
	private void bindChooseBtnEvent(){
		chooseBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt&doc","txt","doc");
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("E:\\android\\windows\\Classification\\article"));
				chooser.setMultiSelectionEnabled(true);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					userFiles.clear();
					updateViewPanel(null);
					System.out.println(chooser.getSelectedFile().getAbsolutePath());
					File[] files = chooser.getSelectedFiles();
					for (File file : files) {
						String ext = FileHelper.getFileExt(file);
						if(ext.equals("doc") || ext.equals("txt")){
							userFiles.add(file);
						}
					}
					updateViewPanel(userFiles);
					progressBar.setMaximum(userFiles.size() + classiyProgress);
					progressBar.setMinimum(0);
					progressBar.setValue(0);
					//允许开始分类按钮
					startBtn.setEnabled(true);
					clearBtn.setEnabled(true);
				}
			}
		});
	}
	
	/**
	 * 绑定清空已选的事件
	 */
	private void bindClearBtnEvent(){
		clearBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userFiles.clear();
				updateViewPanel(null);
				startBtn.setEnabled(false);
			}
		});
	}
	
	/**
	 * 开始分类按钮事件
	 */
	private void bindStartBtnEvent(){
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				progressBar.setVisible(true);
				progressLabel.setVisible(true);
				stopBtn.setEnabled(true);
				classThread = new Thread(){
					public void run(){
						try {
							//进行分词
							WordCut.run(userFiles.toArray(new File[]{}), HomeFrame.this);
							//进行分类
							resultMap = Classfy.run(userFiles.toArray(new File[]{}));
							//更新进度条
							updateProgressBar(progressBar.getValue() + classiyProgress);
							//更新左侧分类查看器
							updateClassList();
							//分类完成时移除右侧面板显示的文件
							updateViewPanel(null);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						clearBtn.setEnabled(false);
						exportBtn.setEnabled(true);
						stopBtn.setEnabled(false);
					}
				};
				classThread.start();
			}
		});
	}
	
	/**
	 * 绑定停止按钮的事件
	 */
	private void bindStopBtnEvent(){
		stopBtn.addActionListener(new ActionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				progressBar.setVisible(false);
				progressLabel.setVisible(false);
				classThread.stop();
				stopBtn.setEnabled(false);
			}
		});
	}
	
	/**
	 * 绑定导出文件按钮的事件
	 */
	private void bindExportBtnEvent(){
		exportBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(){
					public void run(){
						JFileChooser chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						chooser.setCurrentDirectory(new File("E:\\test"));
						int result = chooser.showSaveDialog(null);
						if (result == JFileChooser.APPROVE_OPTION) {
							//设置进度条
							progressBar.setMaximum(userFiles.size());
							progressBar.setValue(0);
							
							File baseDir = chooser.getSelectedFile();
							Iterator<String> iterator = resultMap.keySet().iterator();
							while(iterator.hasNext()){
								String className = iterator.next();
								ArrayList<File> fileList = resultMap.get(className); 
								File subDir = new File(baseDir, className);
								if (!subDir.exists()) {
									subDir.mkdirs();
								}
								//将分类的文件导出到指定的文件夹中
								for (File file : fileList) {
									updateProgressBar(progressBar.getValue() + 1);
									File outFile = new File(subDir,file.getName());
									try {
										FileHelper.copyFile(file, outFile);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
							JOptionPane.showMessageDialog(null, "导出成功");
						}
					}
				}.start();
				
			}
		});
	}
	
	/**
	 * 绑定分类列表的事件
	 */
	private void bindClassListEvent(){
		classList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String className = (String)classList.getSelectedValue();
				updateViewPanel(resultMap.get(className));
			}
		});
	}
	
	
	/**
	 * 初始化控件布局
	 */
	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 466);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("\u83DC\u5355");
		menuBar.add(mnNewMenu);
		
		mntmNewMenuItem = new JMenuItem("\u5E2E\u52A9");
		mnNewMenu.add(mntmNewMenuItem);
		
		mntmNewMenuItem_1 = new JMenuItem("\u68C0\u67E5\u66F4\u65B0");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		mntmNewMenuItem_2 = new JMenuItem("\u5173\u4E8E\u6211\u4EEC");
		mnNewMenu.add(mntmNewMenuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		chooseBtn = new JButton("\u9009\u62E9\u6587\u4EF6");
		
		startBtn = new JButton("\u5F00\u59CB\u5206\u7C7B");
		startBtn.setEnabled(false);
		
		exportBtn = new JButton("\u5BFC\u51FA\u5206\u7C7B");
		exportBtn.setEnabled(false);
		
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		
		classList = new JList();
		classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scrollPane = new JScrollPane();
		
		stopBtn = new JButton("\u505C\u6B62\u5206\u7C7B");
		stopBtn.setEnabled(false);
		
		clearBtn = new JButton("\u6E05\u7A7A\u5DF2\u9009");
		clearBtn.setEnabled(false);
		
		progressLabel = new JLabel("0%");
		progressLabel.setForeground(Color.RED);
		progressLabel.setVisible(false);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(chooseBtn)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(clearBtn)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(startBtn, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(stopBtn)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(exportBtn, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(classList, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
									.addGap(11))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 586, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(progressLabel)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(31)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(exportBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
						.addComponent(stopBtn, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
						.addComponent(startBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
						.addComponent(chooseBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
						.addComponent(clearBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
						.addComponent(classList, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(progressLabel))
					.addGap(16))
		);
		
		viewPanel = new JPanel();
		scrollPane.setViewportView(viewPanel);
		contentPane.setLayout(gl_contentPane);
	
	}
}
