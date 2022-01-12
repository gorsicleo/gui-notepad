package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DefaultSingleDocumentModel implements SingleDocumentModel{

	private static final String NULL_PATH_MESSAGE = "Path must not be null!";
	private Path filePath;
	private boolean isModified;
	private JTextArea textArea;
	private List<SingleDocumentListener> textChangedListeners;
	
	
	public DefaultSingleDocumentModel(Path filePath, String textContent) {
		this.filePath = filePath;
		textArea = new JTextArea();
		textArea.setText(textContent);
		textChangedListeners = new ArrayList<SingleDocumentListener>();
		textArea.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				textChanged();
			}

			public void removeUpdate(DocumentEvent e) {
				textChanged();
			}

			public void changedUpdate(DocumentEvent e) {
				textChanged();
			}
			
			private void textChanged() {
				isModified = true;
				wakeListeners();
			}
	    });
		
	}


	protected void wakeListeners() {
		textChangedListeners.forEach(listener -> listener.documentModifyStatusUpdated(this));
	}


	public JTextArea getTextComponent() {
		return textArea;
	}


	public Path getFilePath() {
		return filePath;
	}


	public void setFilePath(Path path) {
		filePath = Objects.requireNonNull(path,NULL_PATH_MESSAGE);
	}


	public boolean isModified() {
		return isModified;
	}


	public void setModified(boolean modified) {
		isModified = modified;
	}


	public void addSingleDocumentListener(SingleDocumentListener l) {
		textChangedListeners.add(l);
	}


	public void removeSingleDocumentListener(SingleDocumentListener l) {
		textChangedListeners.remove(l);
	}
}
