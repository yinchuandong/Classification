package UserUi;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JEditorPane;

public class HelpFrame extends JFrame {

	private JPanel contentPane;
	private JEditorPane editorPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HelpFrame frame = new HelpFrame();
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
	public HelpFrame() {
		initComponents();
		initData();
		
	}
	
	private void initData(){
		try {
			Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI("http://192.168.233.15:90/html5/static/index.html"));
//			File file = new File("app/static/index.html");
//			System.out.println(file.exists());
//			editorPane.setPage("file:///"+file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 423);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		editorPane = new JEditorPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(editorPane, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(editorPane, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);
	}
}
