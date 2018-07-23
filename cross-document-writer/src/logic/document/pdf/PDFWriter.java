package logic.document.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import logic.document.*;
import logic.document.element.Element;
import logic.document.element.misc.Image;
import logic.document.element.misc.NewLine;
import logic.document.element.misc.NewPage;
import logic.document.element.table.Cell;
import logic.document.element.table.Table;
import logic.document.element.text.Paragraph;
import logic.document.element.text.Phrase;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * The XWPFWriter processes all added Elements and converts them into a DOCX
 * file. This new file can be written to disk or streamed to an PrimeFaces
 * compatible application.
 *
 * In order to fulfil the given tasks, the PDFWriter uses the iText library. A
 * short description of the iText lib:
 *
 * iText ® is a library that allows you to create and manipulate PDF documents.
 * It enables developers looking to enhance web- and other applications with
 * dynamic PDF document generation and/or manipulation. Developers can use iText
 * to:
 *
 * Serve PDF to a browser Generate dynamic documents from XML files or databases
 * Use PDF's many interactive features Add bookmarks, page numbers, watermarks,
 * etc. Split, concatenate, and manipulate PDF pages Automate filling out of PDF
 * Add digital signatures to a PDF file
 *
 * @author Thorsten Jojart
 */
public class PDFWriter implements DocumentWriter {

    /**
     * The iText document is used in order to create pdf elements
     */
    private Document doc = new Document();
    /*
     * The writer is used to set some properties of the document
     */
    private com.itextpdf.text.pdf.PdfWriter writer;
    /**
     * File destination which is used for the writeToDisk function.
     */
    private File pdfFile;
    /**
     * MetaInformation which will be added to newly created documents
     */
    private MetaInformation mi;
    /**
     * List of all Elements, that get added to this document.
     */
    private List<Element> containingElements = new ArrayList<>();
    /**
     * If a element has no style, the defaultStyle will be used
     */
    private Style defaultStyle = new Style(Style.HELVETICA, 12, false, false, Color.BLACK, null, Style.ALIGNMENT_LEFT);
    /**
     * The header element of the pdf document
     */
    private Phrase headerElement = new Phrase();
    /**
     * The footer element of the pdf document by default it renders the creation
     * date
     */
    private Phrase footerElement = new Phrase(String.format("erzeugt am %s", new SimpleDateFormat("dd.MM.yyyy, 'um' HH:mm").format(new Date())));
    /**
     * Sets the orientation format of the document either to upright
     * (Hochformat) or landscape (Querformat). Default is upright.
     */
    private int orientation = DocumentWriter.FORMAT_UPRIGHT;

    /**
     * This constructor creates a new PDFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @throws DocumentWriterException
     */
    public PDFWriter() throws DocumentWriterException {
        this(null, null);
    }

    /**
     * This constructor creates a new PDFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @param f File destination, .docx must be added manually
     * @throws DocumentWriterException
     */
    public PDFWriter(File f) throws DocumentWriterException {
        this(f, null);
    }

    /**
     * This constructor creates a new PDFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @param mi Metainformation which will be added to the document
     * @throws DocumentWriterException
     */
    public PDFWriter(MetaInformation mi) throws DocumentWriterException {
        this(null, mi);
    }

    /**
     * This constructor creates a new PDFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @param f File destination, .docx must be added manually
     * @param mi Metainformation which will be added to the document
     * @throws DocumentWriterException
     */
    public PDFWriter(File f, MetaInformation mi) throws DocumentWriterException {
        //test if file is null
        if (f != null) {
            this.pdfFile = f;
        } else {
            try {
                //tmp file to probably provide the file via an outputstream
                this.pdfFile = File.createTempFile("", ".tmp");
                //standard itextpdf writer-creation-process
                this.writer = com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
            } catch (DocumentException | IOException ex) {
                throw new DocumentWriterException(ex);
            }
        }

        //test if metainformation is null
        if (mi != null) {
            this.mi = mi;
        }

        try {
            //create new writer
            this.writer = com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
        } catch (DocumentException | FileNotFoundException ex) {
            throw new DocumentWriterException(ex);
        }

        //set boxSize for header issues (it is magic)
        writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
    }

    @Override
    public void addElement(Element e) throws DocumentWriterException {
        if (e == null) {
            //That was a very rude argument!
            throw new DocumentWriterException("A null element is not allowed!");
        } else {
            this.containingElements.add(e);
        }
    }

    @Override
    public void provideMetaData(MetaInformation meta) throws DocumentWriterException {
        if (meta == null) {
            //you shan't pass a null meta-info!
            throw new DocumentWriterException("Your meta-information should not be null!");
        }
        //everything went well
        this.mi = meta;
    }

    @Override
    public void setStandardStyle(Style s) throws DocumentWriterException {
        if (s == null) {
            //null is not ok
            throw new DocumentWriterException("Your style should not be null!");
        }
        //set style
        this.defaultStyle = s;

    }

    @Override
    public StreamedContent streamDocument() {
        try {
            //ensure we are working with the actual version of the file
            this.writeToDisk();
            return new DefaultStreamedContent(new FileInputStream(this.pdfFile), "application/pdf");
        } catch (DocumentWriterException | FileNotFoundException ex) {
            Logger.getLogger(PDFWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void writeToDisk() throws DocumentWriterException {
        this.buildDocument();

        //open the document for use
        this.doc.open();

        //init rect for header & footer
        Rectangle rect = writer.getBoxSize("art");

        //Set header
        ColumnText.showTextAligned(writer.getDirectContent(),
                com.itextpdf.text.Element.ALIGN_RIGHT, (com.itextpdf.text.Phrase) getCorrespondingElement(this.headerElement),
                rect.getRight(), rect.getTop(), 0);

        //Set Footer
        ColumnText.showTextAligned(
                writer.getDirectContent(),
                com.itextpdf.text.Element.ALIGN_CENTER,
                (com.itextpdf.text.Phrase) getCorrespondingElement(this.footerElement),
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        {
            for (Element el : containingElements) {
                try {
                    com.itextpdf.text.Element e = getCorrespondingElement(el);
                    this.doc.add(e);
                } catch (DocumentException ex) {
                    throw new DocumentWriterException(ex);
                }
            }
        }

        //close the document to finish writing
        this.doc.close();
    }

    /**
     * converts a Style to an iText Font that can be used to style phrases,
     * paragraphs and cells.
     *
     * @param s Style that should be converted to a iText Font
     * @return returns a new iText Font with the styles applied from Style s
     */
    private com.itextpdf.text.Font getCorrespondingFont(Style s) {
        if (s != null) {
            Font.FontFamily fontFam;
            int fontStyle = 0;

            //switch through font family
            switch (s.getFontFamily()) {
                case Style.COURIER_NEW:
                    fontFam = Font.FontFamily.COURIER;
                    break;
                case Style.HELVETICA:
                    fontFam = Font.FontFamily.HELVETICA;
                    break;
                case Style.TIMES_NEW_ROMAN:
                    fontFam = Font.FontFamily.TIMES_ROMAN;
                    break;
                default:
                    fontFam = Font.FontFamily.UNDEFINED;
            }

            //Set the style of the font
            if (s.isBold()) {
                fontStyle |= Font.BOLD;
            }
            if (s.isItalic()) {
                fontStyle |= Font.ITALIC;
            }

            Color c = s.getForegroundColor();
            return new Font(fontFam, (float) s.getFontSize(), fontStyle, new BaseColor(c.getRed(), c.getGreen(), c.getBlue()));
        }
        return new Font();
    }

    /**
     * Tries to find the corresponding Element for the Element e and adds it to
     * the document. Each type of Element is treated differently.
     *
     * @param e Element which will pe processed
     * @return the corresponding element, which can be used for iText documents
     * @throws DocumentWriterException
     */
    private com.itextpdf.text.Element getCorrespondingElement(final Object e) throws DocumentWriterException {

        if (e instanceof Phrase) {
            Phrase p = (Phrase) e;
            com.itextpdf.text.Phrase pdfPhrase = new com.itextpdf.text.Phrase(p.getContent(), getCorrespondingFont(p.getStyle()));
            for (Element element : p.getContainingElements()) {
                pdfPhrase.add(getCorrespondingElement(element));
            }
            return pdfPhrase;
        }
        if (e instanceof Paragraph) {
            Paragraph p = (Paragraph) e;
            com.itextpdf.text.Paragraph pdfPara = new com.itextpdf.text.Paragraph(p.getContent(), getCorrespondingFont(p.getStyle()));

            // set alignment
            if (p.getStyle() != null) {
                switch (p.getStyle().getAlignment()) {
                    case Style.ALIGNMENT_CENTER:
                        pdfPara.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                        break;
                    case Style.ALIGNMENT_LEFT:
                        pdfPara.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                        break;
                    case Style.ALIGNMENT_RIGHT:
                        pdfPara.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                        break;
                    default:
                        pdfPara.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                }
            } else {
                pdfPara.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
            }

            for (Element element : p.getContainingElements()) {
                pdfPara.add(getCorrespondingElement(element));
            }
            return pdfPara;
        }
        if (e instanceof Cell) {
            Cell c = (Cell) e;
            com.itextpdf.text.pdf.PdfPCell pdfCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(c.getContent(), getCorrespondingFont(c.getStyle())));
            if (c.getStyle() != null) {
                if (c.getStyle().getBackgroundColor() != null) {
                    Color bc = c.getStyle().getBackgroundColor();
                    pdfCell.setBackgroundColor(new BaseColor(bc.getRed(), bc.getGreen(), bc.getBlue()));
                    switch (c.getStyle().getAlignment()) {
                        case Style.ALIGNMENT_CENTER:
                            pdfCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                            break;
                        case Style.ALIGNMENT_LEFT:
                            pdfCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                            break;
                        case Style.ALIGNMENT_RIGHT:
                            pdfCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                            break;
                        default:
                            pdfCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                    }
                    pdfCell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                }
            }

            for (Element subElement : c.getContainingElements()) {
                pdfCell.addElement(this.getCorrespondingElement(subElement));
            }
            return pdfCell;
        }
        if (e instanceof Table) {
            Table t = (Table) e;
            com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(t.getColumnCount());
            table.setWidthPercentage(100f);
            //iterate through all subcells
            for (Element subCell : t.getContainingElements()) {
                if (subCell instanceof Cell) {
                    table.addCell((com.itextpdf.text.pdf.PdfPCell) getCorrespondingElement(subCell));
                }
            }
            table.completeRow();
            return table;
        }
        if (e instanceof Image) {
            Image i = (Image) e;
            com.itextpdf.text.Image pdfImg = null;

            try {
                if (i.getImageFile() != null) {
                    pdfImg = com.itextpdf.text.Image.getInstance(i.getImageFile().getAbsolutePath());
                } else if (i.getData() != null) {
                    java.awt.Image img = ImageIO.read(new ByteArrayInputStream(i.getData()));
                    pdfImg = com.itextpdf.text.Image.getInstance(img, null);
                }

            } catch (BadElementException | MalformedURLException ex) {
                throw new DocumentWriterException("Failed to add picture!");
            } catch (IOException ex) {
                throw new DocumentWriterException("Not able to read picture!");
            }

            return pdfImg;
        }
        if (e instanceof NewPage) {
            NewPage np = (NewPage) e;
            com.itextpdf.text.Phrase p = new com.itextpdf.text.Phrase();
            this.doc.newPage();

            for (Element element : np.getContainingElements()) {
                p.add(getCorrespondingElement(element));
            }

            return p;
        }
        if (e instanceof NewLine) {
            NewLine nl = (NewLine) e;
            com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph();
            try {
                this.doc.add(Chunk.NEWLINE);
            } catch (DocumentException ex) {
                Logger.getLogger(PDFWriter.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (Element element : nl.getContainingElements()) {
                p.add(getCorrespondingElement(element));
            }

            return p;
        }
        return null;
    }

    @Override
    public void addHeader(Phrase p) throws DocumentWriterException {
        if (p != null) {
            this.headerElement = p;
        }
    }

    @Override
    public void addFooter(Phrase p) throws DocumentWriterException {
        if (p != null) {
            this.footerElement = p;
        }
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation == DocumentWriter.FORMAT_UPRIGHT) {
            //doc.setPageSize(PageSize.LETTER.rotate());
            //doc.setPageSize(PageSize.A4);
            // default is UPRIGHT
        } else if (orientation == DocumentWriter.FORMAT_LANDSCAPE) {
            doc.setPageSize(PageSize.LETTER.rotate());
        }
    }

    @Override
    public void buildDocument() throws DocumentWriterException {
        if (this.mi != null) {
            //add metadata
            this.doc.addAuthor(this.mi.getAuthor());
            this.doc.addCreationDate();
            //this.doc.addCreator(this.mi.getCreator());
            this.doc.addKeywords(mi.getKeywords());
            this.doc.addSubject(this.mi.getSubject());
            this.doc.addTitle(this.mi.getTitle());
        }
    }

    @Override
    public void resetDocument() {
        this.doc = new Document();
        this.mi = null;
        try {
            //standard itextpdf writer-creation-process
            this.writer = com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
        } catch (FileNotFoundException | DocumentException ex) {
            try {
                //tmp file to probably provide the file via an outputstream
                this.pdfFile = File.createTempFile("", ".tmp");
                //standard itextpdf writer-creation-process
                this.writer = com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
            } catch (DocumentException | IOException ex1) {
                System.out.println("something really bad happened - writing into a newly created tmp file failed");
            }

        }
        this.containingElements.clear();
        this.defaultStyle = new Style(Style.HELVETICA, 12, false, false, Color.BLACK, null, Style.ALIGNMENT_LEFT);
        this.headerElement = new Phrase();
        this.footerElement = new Phrase(String.format("erzeugt am %s", new SimpleDateFormat("dd.MM.yyyy, 'um' HH:mm").format(new Date())));
        this.orientation = DocumentWriter.FORMAT_UPRIGHT;
    }

    /**
     * Disables the header element of the document by overriding the existing
     * text elements with an empty phrase.
     *
     * @throws DocumentWriterException
     */
    public void disableHeader() throws DocumentWriterException {
        this.addHeader(new Phrase(""));
    }

    /**
     * Disables the footer element of the document by overriding the existing
     * text elements with an empty phrase.
     *
     * @throws DocumentWriterException
     */
    public void disableFooter() throws DocumentWriterException {
        this.addFooter(new Phrase(""));

    }

    /**
     * Disables the header and footer element of the document by overriding the
     * existing text elements with an empty phrase.
     *
     * @throws DocumentWriterException
     */
    public void disableHeaderFooter() throws DocumentWriterException {
        this.disableFooter();
        this.disableHeader();
    }
}
