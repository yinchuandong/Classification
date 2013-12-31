package UserUi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
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
import javax.swing.SwingUtilities;
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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JProgressBar;
import javax.swing.JList;
import javax.swing.JScrollPane;

import Action.Classfy;
import Action.WordCut;
import Helper.AppHelper;
import Helper.FileHelper;

import java.awt.GridLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.poi.util.TempFile;

import Widget.ImageButton;
import javax.swing.JLabel;
import java.awt.Font;

public class HomeFrame extends JFrame {

	private JPanel contentPane;
	private JLabel countsLabel;
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
	private JMenuItem helpMenu;
	private JMenuItem checkNewMenu;
	private JMenuItem aboutMenu;

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
		bindMenuBarEvent();
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
	
	private void updateSelectedCounts(){
		countsLabel.setText("已选择"+userFiles.size()+"个文件");
	}
	
	/**
	 * 更新左侧分类查看器列表
	 */
	private void updateClassList(){
		classListModel = new DefaultListModel<String>();
		Iterator<String> iterator = resultMap.keySet().iterator();
		while (iterator.hasNext()) {
			String className = (String) iterator.next();
			classListModel.addElement(className + "(" + resultMap.get(className).size()+ ")");
		}
		classList.setModel(classListModel);
	}
	
	
	/**
	 * 更新文件显示区域的ui
	 * @param files
	 */
	private void updateViewPanel(ArrayList<File> files){
		int len = (files == null) ? 0 : files.size();
		int height = ((len / 5) + 1) * 125;
		viewPanel = new JPanel();
		scrollPane.setViewportView(viewPanel);
		viewPanel.setPreferredSize(new Dimension(400, height));
		viewPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		viewPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if (files == null) {
			return ;
		}
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.isMetaDown()){
					popupMenu.show(scrollPane, e.getX(), e.getY());
				}
			}
		});

		for (File file : files) {
			
			final ImageButton button = new ImageButton(file);
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
							//button 向 scrollpane传递事件
							scrollPane.dispatchEvent(SwingUtilities.convertMouseEvent(button, e, scrollPane));
						}
					}
				}
			});
			System.out.println(button.getText());
		}
		repaint();
	}
	
	
	
	/**
	 * 绑定顶部菜单栏的事件
	 */
	private void bindMenuBarEvent(){
		//帮助按钮
		helpMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop desktop = Desktop.getDesktop();
					desktop.browse(new URI("http://192.168.233.15:90/classify/static/index.html#help"));
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(HomeFrame.this, "网络连接中断");
					ex.printStackTrace();
				}
			}
		});
		
		//检查更新
		checkNewMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					new Thread(){
						public void run(){
							try {
								if (!AppHelper.checkNewVersion()) {
									JOptionPane.showMessageDialog(HomeFrame.this, "已经是最新版本");
								}else {
									EventQueue.invokeLater(new Runnable() {
										public void run() {
											CheckNewFrame frame = new CheckNewFrame();
											frame.setVisible(true);
										}
									});
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(HomeFrame.this, "网络连接中断");
					e1.printStackTrace();
				}
			}
		});
		
		//关于我们的按钮
		aboutMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					URI url = new URI("http://192.168.233.15:90/classify/static/index.html#aboutus");
					Desktop desktop = Desktop.getDesktop();
					desktop.browse(url);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(HomeFrame.this, "网络连接中断");
					e1.printStackTrace();
				}
			}
		});
		
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
					updateSelectedCounts();
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
				chooser.setCurrentDirectory(new File("E:\\"));//E:\\android\\windows\\Classification\\article
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
					updateSelectedCounts();
					progressBar.setMaximum(userFiles.size() + classiyProgress);
					progressBar.setMinimum(0);
					updateProgressBar(0);
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
				updateSelectedCounts();
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
				try{
					String str = (String)classList.getSelectedValue();
					String[] arr = str.split("\\(");
					String className = arr[0];
					updateViewPanel(resultMap.get(className));
				}catch(Exception ex){
					
				}
				
			}
		});
	}
	
	
	/**
	 * 初始化控件布局
	 */
	private void initComponents(){
		setTitle("超级文本自动分类系统");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 466);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("\u83DC\u5355");
		menuBar.add(mnNewMenu);
		
		helpMenu = new JMenuItem("\u5E2E\u52A9");
		mnNewMenu.add(helpMenu);
		
		checkNewMenu = new JMenuItem("\u68C0\u67E5\u66F4\u65B0");
		mnNewMenu.add(checkNewMenu);
		
		aboutMenu = new JMenuItem("\u5173\u4E8E\u6211\u4EEC");
		mnNewMenu.add(aboutMenu);
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
		
		clearBtn = new JButton("\u6E05\u7A7A\u6240\u6709");
		clearBtn.setEnabled(false);
		
		progressLabel = new JLabel("0%");
		progressLabel.setForeground(Color.RED);
		progressLabel.setVisible(false);
		
		countsLabel = new JLabel("\u5DF2\u9009\u62E90\u4E2A\u6587\u4EF6");
		countsLabel.setFont(new Font("SimSun", Font.PLAIN, 14));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(progressLabel)
					.addGap(39))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(classList, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
							.addGap(18))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(countsLabel)
							.addGap(10)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 479, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(chooseBtn)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(clearBtn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(startBtn, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(stopBtn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(exportBtn, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(31)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(exportBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
								.addComponent(stopBtn, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
								.addComponent(startBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
								.addComponent(chooseBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
								.addComponent(clearBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
							.addGap(18))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(countsLabel)
							.addGap(18)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(classList, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(progressLabel))
					.addContainerGap())
		);
		
		viewPanel = new JPanel();
		scrollPane.setViewportView(viewPanel);
		contentPane.setLayout(gl_contentPane);
		
		//设置jframe背景图片
		ImageIcon bgImg = new ImageIcon("image/background2.jpg");
		JLabel label = new JLabel(bgImg);
		label.setBounds(0,0,bgImg.getIconWidth(),bgImg.getIconHeight());
		contentPane.setOpaque(false);
		getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
	
	}
}
