package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

@SuppressWarnings("serial")
public class JNotepadPP extends JFrame {

	private DefaultMultipleDocumentModel documents;

	private JLabel statusLabel;

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

				JScrollPane scroller = new JScrollPane(newDoc);
				String tabTitle = model.getFilePath() == null ? "New Document"
						: model.getFilePath().getFileName().toString();
				scroller.setName(tabTitle);
				documents.add(scroller);

			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				addCarretListenerForModel(documents);

			}
		});

		documents.createNewDocument();
		addCarretListenerForModel(documents);

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
		fileMenu.add(new JMenuItem(new OpenAction()));
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
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(true);

		toolbar.add(new JButton(new OpenAction()));
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

	private String textLengthDisplay(int ln, int col) {
		return "length: " + documents.getCurrentDocument().getTextComponent().getText().length() + " ".repeat(20)
				+ "Ln: " + ln + "   Col: " + col;
	}

	private void createStatusBar() {
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel(textLengthDisplay(0, 0));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
	}

	private void showOpenFileDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Open File");
		if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		Path path = chooser.getSelectedFile().toPath();

		documents.loadDocument(path);
	}

	private class OpenAction extends AbstractAction {

		public OpenAction() {
			putValue(Action.NAME, "Open");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			showOpenFileDialog();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new JNotepadPP().setVisible(true);
			}
		});
	}

	private void addCarretListenerForModel(MultipleDocumentModel model) {
		model.getCurrentDocument().getTextComponent().addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				JTextArea editArea = documents.getCurrentDocument().getTextComponent();

				int linenum = 1;
				int columnnum = 1;

				try {
					int caretpos = editArea.getCaretPosition();
					linenum = editArea.getLineOfOffset(caretpos);

					columnnum = caretpos - editArea.getLineStartOffset(linenum);

					linenum += 1;
				} catch (BadLocationException ex) {
				}

				statusLabel.setText(textLengthDisplay(linenum, columnnum));
			}
		});
	}

}
