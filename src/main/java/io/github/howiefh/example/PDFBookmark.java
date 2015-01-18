package io.github.howiefh.example;

import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.renderer.HtmlLink;
import io.github.howiefh.renderer.LinkType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class PDFBookmark {
	public static void main(String[] args) throws Exception {
        OutputStream os = null;
        try {
            final String[] inputs = new String[] { newPageHtml(1, "red"),
                    newPageHtml(2, "blue"), newPageHtml(3, "green") };
            final File outputFile = File.createTempFile("FlyingSacuer", ".pdf");
            os = new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(inputs[0]);
            renderer.layout();
            renderer.createPDF(os, false);
//            BlockBox rootBox = renderer.getRootBox();
            for (int i = 1; i < inputs.length; i++) {
                renderer.setDocumentFromString(inputs[i]);
                renderer.layout();
                renderer.writeNextDocument();
            }
            renderer.finishPDF();
            System.out.println("Sample file with " + inputs.length
                    + " documents rendered as PDF to " + outputFile);
            bookmark();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) { /* ignore */
                }
            }
        }
    }

    private static String newPageHtml(int pageNo, String color) {
        return "<html><head><bookmarks>"
                + "<bookmark name=\"bookmark"
                + pageNo
                + "\" href=\"#bookMark\"/></bookmarks></head>"
                + "<body><div style=\"color:"
                + color
                + ""
                + ";\"><a name=\"bookMark\">Book Mark Example</a></div></body></html>";
    }
    
    private static void bookmark() {
    	String html = "<html><head>"
                + "</head>"
                + "<body><div style=\"color:red"
                + ";\"><a name=\"bookMark\">Book Mark Example</a></div></body></html>";
    	List<HtmlLink> links = new ArrayList<HtmlLink>();
    	HtmlLink link = new HtmlLink("#bookMark", "bookmark", LinkType.LINK);
    	links.add(link);
    	Document document = Jsoup.parse(html);
    	document.head().append(ArticleExporter.generateBookmark(links));
    	System.out.println(document);
	}
}
