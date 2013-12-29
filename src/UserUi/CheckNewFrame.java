package UserUi;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.print.attribute.standard.Severity;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import Helper.AppHelper;
import Helper.FileHelper;

import net.sf.json.JSONObject;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class CheckNewFrame extends JFrame {

	private JPanel contentPane;
	private JEditorPane editorPane;
	private JSONObject server = null;
	private JLabel lblNewLabel;
	private JTextField versionTxt;
	private JLabel label;
	private JLabel label_1;
	private JTextField timeTxt;
	private JTextField nameTxt;
	private JButton downloadBtn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CheckNewFrame frame = new CheckNewFrame();
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
	public CheckNewFrame() {
		initComponents();
		initData();
		bindDownloadBtnEvent();
		
	}
	
	private void initData(){
		try {
			URLConnection connection = new URL("http://192.168.233.15:90/classify/app.json").openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String str = "";
			String temp = null;
			while((temp = reader.readLine()) != null){
				str += temp + "\n";
			}
			JSONObject server = JSONObject.fromObject(str);
			this.server = server;
			versionTxt.setText(server.getString("version"));
			timeTxt.setText(server.getString("upgradetime"));
			nameTxt.setText(server.getString("name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void bindDownloadBtnEvent(){
		downloadBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					download();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void download() throws IOException{
		URL url = new URL(server.getString("url"));
		URLConnection conn = url.openConnection();
		
		JFileChooser chooser = new JFileChooser("E:\\");
		chooser.setSelectedFile(new File("E:\\"+server.getString("name")));
		int result = chooser.showSaveDialog(null);
		File file = chooser.getSelectedFile();
		if (result == JFileChooser.APPROVE_OPTION){
			InputStream input= conn.getInputStream();
			FileOutputStream output = new FileOutputStream(file);
			byte[] buff = new byte[4096];
			int len = 0;
			while((len = input.read(buff)) != -1){
				output.write(buff, 0, len);
			}
			input.close();
			output.close();
		}
	}
	
	
	private void initComponents(){
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 351, 210);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		editorPane = new JEditorPane();
		
		lblNewLabel = new JLabel("\u7248\u672C\u53F7");
		
		versionTxt = new JTextField();
		versionTxt.setEditable(false);
		versionTxt.setColumns(10);
		
		label = new JLabel("\u66F4\u65B0\u65E5\u671F");
		
		label_1 = new JLabel("\u540D\u79F0");
		
		timeTxt = new JTextField();
		timeTxt.setEditable(false);
		timeTxt.setColumns(10);
		
		nameTxt = new JTextField();
		nameTxt.setEditable(false);
		nameTxt.setColumns(10);
		
		downloadBtn = new JButton("\u4E0B\u8F7D\u65B0\u7248\u672C");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(versionTxt, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(label)
							.addGap(18)
							.addComponent(timeTxt, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(label_1)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(downloadBtn)
								.addComponent(nameTxt, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE))))
					.addGap(18)
					.addComponent(editorPane))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(editorPane, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(versionTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(timeTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(nameTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(downloadBtn, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(99, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
