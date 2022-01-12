package hr.fer.oprpp1.hw08.jnotepadpp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	private static final String CANNOT_OPEN_FILE_ERROR = "Cannot open file";
	private List<SingleDocumentModel> documents;
	private SingleDocumentModel currentDocument;
	private List<MultipleDocumentListener> listeners;

	public DefaultMultipleDocumentModel() {
		super();
		this.documents = new ArrayList<SingleDocumentModel>();
		listeners = new ArrayList<MultipleDocumentListener>();
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	@Override
	public JComponent getVisualComponent() {
		return this;
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newDoc = new DefaultSingleDocumentModel(null, "");
		listeners.forEach(listener -> listener.documentAdded(newDoc));
		return newDoc;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		try {
			String text = new String(Files.readAllBytes(path),StandardCharsets.UTF_8);
			SingleDocumentModel newDoc = new DefaultSingleDocumentModel(path, text);
			listeners.forEach(listener -> listener.documentAdded(newDoc));
			return newDoc;
		} catch (IOException e) {
			throw new IllegalArgumentException(CANNOT_OPEN_FILE_ERROR);
		}
	}

	
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Path filePath = (newPath == null) ? model.getFilePath() : newPath;
		documents.stream().forEach(document -> {
			//TODO: Implement save!
		});
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		listeners.forEach(listener -> listener.documentRemoved(model));
		documents.remove(model);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);

	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}

	@Override
	public SingleDocumentModel findForPath(Path path) {
		List<SingleDocumentModel> documentsWithGivenPath = documents
				.stream()
				.filter(document -> document.getFilePath().equals(path))
				.collect(Collectors.toList());
		
		return documentsWithGivenPath.size()==1 ? documentsWithGivenPath.get(0) : null;
	
	}

	@Override
	public int getIndexOfDocument(SingleDocumentModel doc) {
		return documents.indexOf(doc);
	}
	

}
