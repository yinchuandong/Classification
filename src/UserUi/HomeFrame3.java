package UserUi;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicMenuUI;

import Helper.FileHelper;
//import com.sun.java.util.jar.pack.Package.File;

class MenuUi extends BasicMenuUI{
	public MenuUi(Color color, Color fcolor){
		super.selectionBackground=color;
		super.selectionForeground=fcolor;
	}
}
public class HomeFrame3 extends JFrame {

	private JPanel contentPane;
	private JTextField filefiltertextField;
	private JButton chooseBtn;
	private JButton startBtn;
	private JButton commitBtn;
	private JList showtypelist;
	private JScrollPane addPane;
	private JPanel imagePanel;
	private DefaultListModel showtypelistModel = new DefaultListModel();
	private String str="";
	private HashMap<String,ArrayList<File>> hashresult;
	private ArrayList<File>  arraylist;
	private ImageIcon wordIcon= new ImageIcon(new ImageIcon("image/word.jpg").getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
	private ImageIcon txtIcon= new ImageIcon(new ImageIcon("image/txt.jpg").getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
//
	private JMenuBar menuBar;//工具栏
	private JMenu listmenu;//菜单
	private JMenuItem checknewmenuitem;//检查更新
	private JMenu helpmenu;//帮助
	private JMenu aboutusmenu;//关于我们

	private ImageIcon backgroundimage;//背景图片
	private JLabel imagelabel;//背景图片按钮
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeFrame3 frame = new HomeFrame3();
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
	public HomeFrame3() {
		initComponents();
		bindChooseBtnEvent();
	}
	
	/**
	 * 绑定选择按钮的事件
	 */
	private void bindChooseBtnEvent(){
		chooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt&doc","txt","doc");  // can only chose word and txt
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("E:\\android\\windows\\Classification\\trainfile"));
				chooser.setMultiSelectionEnabled(true);   // multiple files to be selected
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  // can only choose file
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					System.out.println(chooser.getSelectedFile().getAbsolutePath());
					//   my add   一次选多个文件为实现 。
					str=str+chooser.getSelectedFile().getAbsolutePath();
					filefiltertextField.setText(str);
				}
			}
		});
		
		 startBtn.addActionListener(new ActionListener(){
			 @Override
			 public void actionPerformed(ActionEvent arg0){
				 
			 /**
			  *    to change code here;   用filechooseTfd 得到了要分类的文件， 然后调用函数，得到结果 集，
			  */
				
			      hashresult=new HashMap<String,ArrayList<File>>();
			      ArrayList<File> list1=new ArrayList();
			      list1.add(new File("F:\\1.txt"));
			      list1.add(new File("F:\\2.txt"));
			      list1.add(new File("F:\\3.txt"));
			      list1.add(new File("F:\\新闻发布系统.doc"));
			      list1.add(new File("F:\\需求分析.doc"));
			      list1.add(new File("F:\\界面.doc"));
			      ArrayList<File>list2=new ArrayList();
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
			      ArrayList<File> list3=new ArrayList();
			      list3.add(new File("F:\\1.txt"));
			      list3.add(new File("F:\\2.txt"));
			      list3.add(new File("F:\\3.txt"));
			      list3.add(new File("F:\\4.txt"));
			      hashresult.put("list1", list1);
			      hashresult.put("list2", list2);
			      hashresult.put("list3", list3);
		      
		      /**
		       *  my add  
		       */
			     
			     showtypelistModel.removeAllElements();
			     imagePanel.removeAll();
			     /**
			      * may appear nullpointerexception 
			      */
			     Iterator iterator=hashresult.keySet().iterator();
			     while(iterator.hasNext()){
			    	 
			    	String list=(String) iterator.next();
			    	showtypelistModel.addElement(list);
			    	
			     }
			        
			     
					
		      
			 }
		 });
		 
		 
		 /**
		  *  showtypelist value change event
		  */
		 showtypelist.addListSelectionListener(new ListSelectionListener(){
	    		@Override
				public void valueChanged(ListSelectionEvent e) {
					// TODO Auto-generated method stub
	    			
	    		
	    		
	    			
	    			imagePanel.removeAll();
	    			imagePanel.setLayout(new FlowLayout()); 
					String select=(String)showtypelist.getSelectedValue();
					int size;
					try{
					 arraylist=(ArrayList<File>)hashresult.get(select);
					  size=arraylist.size();
					}catch(NullPointerException exc){
						
						size=0;
					}
					
				
					for(int i=0;i<size;i++)
					{
					  // start for
						
						
					   final File file=arraylist.get(i);
					   String filename=file.getName();
					   String extention="";
					   
					   // get the extention name
					   if(filename.length()>0&filename!=null)
					   {
						  int index=filename.lastIndexOf(".");
						  if(index>-1&index<filename.length()){
							  extention=filename.substring(index+1);
						  }
					   }
					   
					   
					   
					   /**
					    *     imagePanel change
					    */
					  JButton fileBtn=new JButton();
					  fileBtn.setSize(40, 40);
					  if(filename.length()<=11)
					  {
						  fileBtn.setText(filename);
					  
					  }else{
						  String subname=filename.substring(filename.length()-11, filename.length());
						  fileBtn.setText(subname);
					  }
					 
					  fileBtn.setBorderPainted(false);
					  fileBtn.setBackground(Color.WHITE);
					  
					  
					  // setIcon
					  if(extention.equals("doc"))
					  {
						  
					  fileBtn.setIcon(wordIcon);
					  }
					  else
					  {
						  fileBtn.setIcon(txtIcon);
					  }
					  fileBtn.setVerticalTextPosition(JButton.BOTTOM);
					  fileBtn.setHorizontalTextPosition(JButton.CENTER);  
					  
					  
					  imagePanel.add(fileBtn);
					  
					  
					  /**
					   * 图标按钮事件
					   */
					  fileBtn.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							try {
							String text;
							
								text = FileHelper.readTxtOrDoc(file);
						        
						        /**
						         *    要改弹出窗口 在这改 
						         */
						       
						        JTextArea tx=new JTextArea();
						        JFrame frame=new JFrame();
						        JScrollPane js=new JScrollPane(tx);
						        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						        frame.getContentPane().add(js);
						        tx.setText(text);
						        frame.setVisible(true);
						        frame.setSize(400,400);
						        
						        
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								
							}
							
							
						}
						  
					  }); // end 图标按钮事件  
					  
					 //end for
					}
					
					imagePanel.updateUI();
				
					
				}
	    	   
	    	   
	       });
		
		
	
		 
		 /**
		  * 
		  */
		 commitBtn.addActionListener(new ActionListener(){
			 @Override
			 public void actionPerformed(ActionEvent arg0){
				 
				 /**
				  *    未能读取内容；
				  */
				
				 JFileChooser jf = new JFileChooser();  
				 jf.setFileSelectionMode(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY);  
				 jf.showDialog(null,null);  
				 File fi = jf.getSelectedFile(); 
				 
				 /**
				  *  如果没有点击开始分类,会出现空指针
				  */
				  try{
				  Iterator iterator=hashresult.keySet().iterator(); 
				  String sortname="";
				  while(iterator.hasNext()){
				  sortname=(String) iterator.next();
				  String sortpath = fi.getAbsolutePath()+"/"+sortname; 
				  File sortFile=new File(sortpath);
				  if(!sortFile.exists())
				  { sortFile.mkdir();} 
				  ArrayList<File>  outfilelist=hashresult.get(sortname);
			      Iterator outfilelistIterator=outfilelist.iterator();
			      while(outfilelistIterator.hasNext())
				  {   
					 File outfile=(File)outfilelistIterator.next();
					  
					 File infile= new File(sortpath+"/"+outfile.getName());  
				     try {
				    	if(!infile.exists())
						{
				    		infile.createNewFile();
						    FileHelper.copyFile(outfile, infile);
						}else {
							JOptionPane.showMessageDialog(null, infile.getName()+"已存在当前目录中");
						}
					} catch (IOException e) {
						
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "导出失败");
					}
				  }		    
				  }
				  JOptionPane.showMessageDialog(null, "成功导出");
				  }catch(NullPointerException e){
					  JOptionPane.showMessageDialog(null,"请先点击开始分类按钮");
				  }
			 }
		 });
	}
	
	
	
	
	public void initComponents(){
		
		setTitle("\u57FA\u4E8E\u5185\u5BB9\u7684\u6587\u4EF6\u5206\u7C7B\u7CFB\u7EDF\r\n");
		setForeground(Color.BLUE);
		setBackground(Color.BLUE);
		setBounds(100, 100, 604, 438);
		
		menuBar = new JMenuBar();
		menuBar.setForeground(Color.BLACK);
		setJMenuBar(menuBar);
		menuBar.setOpaque(false);
		
		listmenu = new JMenu("\u83DC\u5355");
		listmenu.setForeground(Color.BLACK);
		listmenu.setBackground(Color.WHITE);
		listmenu.setHorizontalAlignment(SwingConstants.LEFT);
		listmenu.setUI(new MenuUi(Color.WHITE,Color.BLACK));
		listmenu.setLayout(new GroupLayout(listmenu));
		listmenu.setOpaque(false);
		listmenu.addSeparator();
		menuBar.add(listmenu);
		
	    checknewmenuitem = new JMenuItem("\u68C0\u67E5\u66F4\u65B0");
	    checknewmenuitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UNDEFINED, 0));
		listmenu.add(checknewmenuitem);
		
		helpmenu = new JMenu("\u5E2E\u52A9");
		helpmenu.setHorizontalAlignment(SwingConstants.LEFT);
		helpmenu.setForeground(Color.BLACK);
		helpmenu.setBackground(Color.BLUE);
		helpmenu.setUI(new MenuUi(Color.WHITE,Color.BLACK));
		helpmenu.setLayout(new GroupLayout(helpmenu));
		helpmenu.setOpaque(false);
		menuBar.add(helpmenu);
		
		aboutusmenu = new JMenu("\u5173\u4E8E\u6211\u4EEC");
		aboutusmenu.setForeground(Color.BLACK);
		aboutusmenu.setBackground(Color.BLUE);
		aboutusmenu.setUI(new MenuUi(Color.WHITE,Color.BLACK));
		aboutusmenu.setLayout(new GroupLayout(aboutusmenu));
		aboutusmenu.setOpaque(false);

		menuBar.add(aboutusmenu);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel mainpanel = new JPanel();
		contentPane.add(mainpanel);
		contentPane.setOpaque(false);
		mainpanel.setOpaque(false);
		mainpanel.setLayout(new BorderLayout(0, 0));
		
       backgroundimage=new ImageIcon("image/background.jpg");
       imagelabel=new JLabel(backgroundimage);
       imagelabel.setBounds(0,0, backgroundimage.getIconWidth(), backgroundimage.getIconHeight());
       this.getLayeredPane().add(imagelabel,new Integer(Integer.MIN_VALUE));

        JPanel headpanel = new JPanel();
		headpanel.setOpaque(false);;
		
		JPanel filefilterpanel_2 = new JPanel();
		filefilterpanel_2.setOpaque(false);
		
		filefiltertextField = new JTextField();
		filefiltertextField.setHorizontalAlignment(SwingConstants.LEFT);
		filefiltertextField.setColumns(28);
		
		
		 chooseBtn= new JButton("\u9009\u62E9\u6587\u4EF6");
		 chooseBtn.setBackground(Color.WHITE);
		 chooseBtn.setFont(new Font("宋体", Font.BOLD, 12));
		
	     startBtn = new JButton("\u5206\u7C7B\u5F00\u59CB");
		 startBtn.setBackground(Color.WHITE);
		 startBtn.setFont(new Font("宋体", Font.BOLD, 12));
		
		 commitBtn = new JButton("\u786E\u8BA4\u5BFC\u51FA");
		 commitBtn.setBackground(Color.WHITE);
		 commitBtn.setFont(new Font("宋体", Font.BOLD, 12));
		GroupLayout gl_filefilterpanel_2 = new GroupLayout(filefilterpanel_2);
		gl_filefilterpanel_2.setHorizontalGroup(
			gl_filefilterpanel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filefilterpanel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(filefiltertextField, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(chooseBtn, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(startBtn, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(commitBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_filefilterpanel_2.setVerticalGroup(
			gl_filefilterpanel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filefilterpanel_2.createSequentialGroup()
					.addGroup(gl_filefilterpanel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_filefilterpanel_2.createSequentialGroup()
							.addGap(5)
							.addGroup(gl_filefilterpanel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(commitBtn)
								.addComponent(startBtn)
								.addComponent(chooseBtn)))
						.addGroup(gl_filefilterpanel_2.createSequentialGroup()
							.addGap(6)
							.addComponent(filefiltertextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		filefilterpanel_2.setLayout(gl_filefilterpanel_2);
		mainpanel.add(headpanel, BorderLayout.NORTH);
		
		JPanel centerpanel = new JPanel();
		centerpanel.setOpaque(false);
		
		
	    showtypelist = new JList(showtypelistModel);
		showtypelist.setVisibleRowCount(23);
		showtypelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showtypelist.setBorder(new LineBorder(new Color(0, 0, 0)));
		showtypelist.setBackground(Color.WHITE);
		showtypelist.setVisibleRowCount(23);
		
		
	

	     imagePanel=new JPanel();
	     imagePanel.setPreferredSize(new Dimension(100,800));
	     imagePanel.setBackground(Color.white);
	     addPane=new JScrollPane(imagePanel);
	     addPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	     addPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		GroupLayout gl_centerpanel = new GroupLayout(centerpanel);
		gl_centerpanel.setHorizontalGroup(
			gl_centerpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_centerpanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(showtypelist, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(addPane, GroupLayout.PREFERRED_SIZE, 388, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(11, Short.MAX_VALUE))
		);
		gl_centerpanel.setVerticalGroup(
			gl_centerpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_centerpanel.createSequentialGroup()
					.addGap(13)
					.addGroup(gl_centerpanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(addPane, Alignment.LEADING)
						.addComponent(showtypelist, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
					.addGap(47))
		);
		centerpanel.setLayout(gl_centerpanel);
		GroupLayout gl_headpanel = new GroupLayout(headpanel);
		gl_headpanel.setHorizontalGroup(
			gl_headpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_headpanel.createSequentialGroup()
					.addGroup(gl_headpanel.createParallelGroup(Alignment.LEADING)
						.addComponent(filefilterpanel_2, GroupLayout.PREFERRED_SIZE, 588, GroupLayout.PREFERRED_SIZE)
						.addComponent(centerpanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_headpanel.setVerticalGroup(
			gl_headpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_headpanel.createSequentialGroup()
					.addGap(18)
					.addComponent(filefilterpanel_2, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(centerpanel, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE))
		);
		headpanel.setLayout(gl_headpanel);
		
		JPanel buttompanel = new JPanel();
		mainpanel.add(buttompanel, BorderLayout.SOUTH);
		buttompanel.setOpaque(false);
		
		JLabel lblcopyright = new JLabel("@copyright");
		lblcopyright.setForeground(Color.RED);
		buttompanel.add(lblcopyright);
	
	}



}
