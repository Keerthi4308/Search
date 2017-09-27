
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.commons.io.FileUtils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;

import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class generateindex {

	private static IndexWriter writer;

	public static void main(String args[]) throws Exception {

		String inputfilepath = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\corpus";
		int k = createIndex(inputfilepath);
		System.out.println("index for %d files created" + k);
		return;

	}

	public void close() throws CorruptIndexException, IOException {
		writer.close();
	}

	public static Document getText(Node node, Document document) {

		NodeList list = node.getChildNodes();

		if (!node.hasChildNodes())
			return document;
		for (int i = 0; i < list.getLength(); i++) {
			Node subnode = list.item(i);
			if (subnode.getNodeType() == Node.TEXT_NODE) {
				String name = subnode.getNodeName();
				String value = subnode.getNodeValue();

				document.add(new TextField(name, value, org.apache.lucene.document.Field.Store.NO));

			}

		}
		return document;
	}

	public static Document chosendata(String found, Document document) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document doc = db.parse(found);

		doc.getDocumentElement().normalize();

		return (getText(doc.getDocumentElement(), document));

	}

	public static Document match_reg(File file) throws Exception {
		String REG = "<DOC>+(/d */d)+</DOC>";
		String input = FileUtils.readFileToString(file, "utf-8");

		Pattern r = Pattern.compile(REG);

		Matcher m = r.matcher(input);

		Document document = new Document();

		String found = null;

		while (m.find()) {
			found = m.group();
			document = chosendata(found, document);
			found = null;
		}

		return document;

	}

	public static int createIndex(String dataDirPath) throws Exception {
		// get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();
		String indexPath = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\index";

		Directory dir = FSDirectory.open(Paths.get(indexPath));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

		iwc.setOpenMode(OpenMode.CREATE);

		writer = new IndexWriter(dir, iwc);

		for (File file : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead()) {

				writer.addDocument(match_reg(file));
				System.out.println("file added: "+ file.getName());

			}
		}
		return writer.numDocs();
	}
}
