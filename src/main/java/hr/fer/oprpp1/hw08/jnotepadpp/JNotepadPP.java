package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.TextArea;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;







@SuppressWarnings("serial")
public class JNotepadPP extends JFrame {
	
	private DefaultMultipleDocumentModel documents;

	public JNotepadPP() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 600);
		setTitle("JNotepad++");
		documents = new DefaultMultipleDocumentModel();
		documents.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				documents.remove(model.getTextComponent());
			}
			
			@Override
			public void documentAdded(SingleDocumentModel model) {
				JComponent newDoc = model.getTextComponent();
				newDoc.setName("New Document");
				documents.add(newDoc);
				
			}
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				documents.remove(previousModel.getTextComponent());
				documents.add(currentModel.getTextComponent());
				
			}
		});
		documents.createNewDocument();
		initGUI();
	}

	private void initGUI() {
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(documents, BorderLayout.CENTER);
		
		createMenu();
		createToolbar();
		createStatusBar();

	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem("Open"));
		fileMenu.add(new JMenuItem("Save"));
		fileMenu.add(new JMenuItem("Save as"));
		fileMenu.add(new JMenuItem("Close"));

		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");
		editMenu.add(new JMenuItem("Cut"));
		editMenu.add(new JMenuItem("Copy"));
		editMenu.add(new JMenuItem("Paste"));
		editMenu.add(new JMenuItem("Delete"));

		menuBar.add(editMenu);

		JMenu languageMenu = new JMenu("Language");

		menuBar.add(languageMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void createToolbar() {
		JToolBar toolbar = new JToolBar("Alati");
		toolbar.setFloatable(true);

		toolbar.add(new JButton("Open"));
		toolbar.addSeparator();
		toolbar.add(new JButton("Save"));

		toolbar.addSeparator();
		toolbar.add(new JButton("Cut"));

		toolbar.addSeparator();
		toolbar.add(new JButton("Copy"));

		toolbar.addSeparator();
		toolbar.add(new JButton("Paste"));

		this.getContentPane().add(toolbar, BorderLayout.PAGE_START);
	}
	
	private void createStatusBar() {
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new JNotepadPP().setVisible(true);
			}
		});
	}
	
}
