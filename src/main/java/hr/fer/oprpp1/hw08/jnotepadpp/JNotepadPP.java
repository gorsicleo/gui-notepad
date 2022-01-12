package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


public class JNotepadPP extends JFrame {
	
	private DefaultMultipleDocumentModel documents;

	public JNotepadPP() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 600);
		documents = new DefaultMultipleDocumentModel();
		documents.add(new TextArea("Test1"));
		documents.add(new TextArea("Test2"));
		initGUI();
	}

	private void initGUI() {
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(documents, BorderLayout.CENTER);

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
