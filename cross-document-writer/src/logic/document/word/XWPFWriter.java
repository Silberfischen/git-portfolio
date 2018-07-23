package logic.document.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.document.DocumentWriter;
import logic.document.DocumentWriterException;
import logic.document.MetaInformation;
import logic.document.Style;
import logic.document.element.Element;
import logic.document.element.misc.Image;
import logic.document.element.misc.NewLine;
import logic.document.element.misc.NewPage;
import logic.document.element.table.Cell;
import logic.document.element.table.Table;
import logic.document.element.text.Paragraph;
import logic.document.element.text.Phrase;
import logic.document.element.text.TextElement;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * The XWPFWriter processes all added Elements and converts them into a DOCX
 * file. This new file can be written to disk or streamed to an PrimeFaces
 * compatible application.
 *
 * In order to fulfil the given tasks, the XWPFWriter uses the APACHE POI 3.9
 * library (XWPF). A short description of the XWPF lib:
 *
 * HWPF and XWPF provides ways to read spreadsheets create, modify, read and
 * write DOC and DOCX documents. They provide:
 *
 * low level structures for those with special needs an eventmodel api for
 * efficient read-only access a full usermodel api for creating, reading and
 * modifying DOC and DOCX files
 *
 * @author Harald Gleiss & Thorsten Jojart
 */
public class XWPFWriter implements DocumentWriter {

    /**
     * The custom xwpf document is used in order to create docx elements and it
     * provides low level API functions.
     */
    private CustomXWPFDocument doc;
    /**
     * List of all Elements, that get added to this document.
     */
    private List<Element> containingElements = new ArrayList<>();
    /*
     * The actual paragraph, new runs will be add to this paragraph.
     */
    private XWPFParagraph actualparagraph;
    /**
     * The actual run, represents the current cursor in the document. ( I )
     */
    private XWPFRun actualrun;
    /**
     * File destination which is used for the writeToDisk function.
     */
    private File xwpfFile;
    /**
     * MetaInformation which will be added to newly created documents
     */
    private MetaInformation mi;
    /**
     * If a element has no style, the defaultStyle will be used
     */
    private Style defaultStyle;
    /**
     * If a picture is added to the document, XWPFDocument declares a id for it
     * this id starts at 0 for the first picture and for each further picture it
     * will get increased by one
     */
    private int actualPictureIndex = 0;
    /**
     * Sets the orientation format of the document either to upright
     * (Hochformat) or landscape (Querformat). Default is upright.
     */
    private int orientation = DocumentWriter.FORMAT_UPRIGHT;

    /**
     * This constructor creates a new XWPFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @throws DocumentWriterException
     */
    public XWPFWriter() throws DocumentWriterException {
        this(null, null);
    }

    /**
     * This constructor creates a new XWPFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @param f File destination, .docx must be added manually
     * @throws DocumentWriterException
     */
    public XWPFWriter(File f) throws DocumentWriterException {
        this(f, null);
    }

    /**
     * This constructor creates a new XWPFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @param mi Metainformation which will be added to the document
     * @throws DocumentWriterException
     */
    public XWPFWriter(MetaInformation mi) throws DocumentWriterException {
        this(null, mi);
    }

    /**
     * This constructor creates a new XWPFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @param f File destination, .docx must be added manually
     * @param mi Metainformation which will be added to the document
     * @throws DocumentWriterException
     */
    public XWPFWriter(File f, MetaInformation mi) throws DocumentWriterException {
        // Initialize values
        defaultStyle = new Style();
        doc = new CustomXWPFDocument();

        // test if file is null
        if (f != null) {
            this.xwpfFile = f;
        } else {
            try {
                // tmp file to probably provide the file via an outputstream
                this.xwpfFile = File.createTempFile("", ".tmp");
            } catch (IOException ex) {
                throw new DocumentWriterException(ex);
            }
        }

        // test if metainformation is null
        if (mi != null) {
            this.mi = mi;
        }
    }

    @Override
    public void addElement(Element e) {
        this.containingElements.add(e);
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
    public StreamedContent streamDocument() throws DocumentWriterException {
        try {
            //ensure we are working with the actual version of the file
            this.writeToDisk();
            return new DefaultStreamedContent(new FileInputStream(this.xwpfFile), "application/docx");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XWPFWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void writeToDisk() throws DocumentWriterException {
        try {
            doc.write(new FileOutputStream(xwpfFile));
        } catch (IOException ex) {
            throw new DocumentWriterException(ex);
        }
    }

    /**
     * Tries to find the corresponding Element for the Element e and adds it to
     * the document. Each type of Element is treated differently.
     *
     * @param e Element which will pe processed
     */
    private void processElement(logic.document.element.Element e) throws DocumentWriterException {
        if (e instanceof Phrase) {
            Phrase p = (Phrase) e;

            if (this.actualparagraph == null) {
                this.actualparagraph = this.doc.createParagraph();
                this.actualparagraph.setSpacingAfter(0);
                //this.actualparagraph.setSpacingBefore(0);
            }

            this.actualrun = styleRun(e, this.actualparagraph.createRun());

            // if styleRun failed to set content and style,
            // a empty default run with Words default style is made
            if (this.actualrun == null) {
                this.actualrun = this.actualparagraph.createRun();
            }

            // iterate through all subElements
            for (Element subElement : p.getContainingElements()) {
                this.processElement(subElement);
            }
        }
        if (e instanceof Paragraph) {
            Paragraph p = (Paragraph) e;
            this.actualparagraph = this.doc.createParagraph();
            this.actualparagraph.setSpacingAfter(0);
            //this.actualparagraph.setSpacingBefore(0);

            // set alignment
            if (p.getStyle() != null) {
                switch (e.getStyle().getAlignment()) {
                    case Style.ALIGNMENT_CENTER:
                        this.actualparagraph.setAlignment(ParagraphAlignment.CENTER);
                        break;
                    case Style.ALIGNMENT_LEFT:
                        this.actualparagraph.setAlignment(ParagraphAlignment.LEFT);
                        break;
                    case Style.ALIGNMENT_RIGHT:
                        this.actualparagraph.setAlignment(ParagraphAlignment.RIGHT);
                        break;
                    default:
                        this.actualparagraph.setAlignment(ParagraphAlignment.LEFT);
                }
            } else {
                this.actualparagraph.setAlignment(ParagraphAlignment.LEFT);
            }

            this.actualrun = styleRun(e, this.actualparagraph.createRun());

            // if styleRun failed to set content and style, a default run is made
            if (this.actualrun == null) {
                this.actualrun = this.actualparagraph.createRun();
            }

            // iterate through all subElements
            for (Element subElement : p.getContainingElements()) {
                this.processElement(subElement);
            }
        }

        /* 
         * XWPF tables does NOT support pictures inside their cells!
         * empty cells (no element) get ignored (difference to iText,
         * where they add a empty cell without text)
         */
        if (e instanceof Table) {
            Table t = (Table) e;

            // int h(elp), is used to correctly iterate and create the rows of the table
            int h = 1;
            XWPFTable table;
            XWPFTableRow row = null;
            XWPFTableCell cell = null;
            XWPFParagraph p = null;
            XWPFRun r = null;

            table = doc.createTable();


            //initialize table styles
            CTTblPr mar = table.getCTTbl().addNewTblPr();

            /* mar.addNewTblpPr().setTopFromText(BigInteger.valueOf(50));
             mar.getTblpPr().setBottomFromText(BigInteger.valueOf(50));
             mar.getTblpPr().setLeftFromText(BigInteger.valueOf(50));
             mar.getTblpPr().setRightFromText(BigInteger.valueOf(50)); */

            // cell margins in units
            // units = twentieth of a point, 360 = 0.25"

            mar.addNewTblCellMar().addNewTop().setW(BigInteger.valueOf(100));
            mar.getTblCellMar().addNewLeft().setW(BigInteger.valueOf(100));
            mar.getTblCellMar().addNewRight().setW(BigInteger.valueOf(100));
            mar.getTblCellMar().addNewBottom().setW(BigInteger.valueOf(100));


            // iterate through all containing elemets
            for (int i = 0; i < t.getContainingElements().size(); i++) {
                /*
                 * first iteration does not add a new row and a new cell, because a table starts already with one row and one cell
                 * new table (cell X already exsists):
                 *      Column
                 * Row  X 
                 */
                if (i == 0 && t.getContainingElements().get(i) instanceof Cell) {
                    row = table.getRow(i);
                    cell = row.getCell(i);
                    cell = this.processCell(cell, t.getContainingElements().get(i));

                    // This attribute controls whether to repeat a table's header row at the top
                    // of a table split across pages.
                    row.setRepeatHeader(true);

                    // get table row properties (trPr)
                    CTTrPr trPr = row.getCtRow().addNewTrPr();

                    /* 
                     * set row height; units = twentieth of a point, 360 = 0.25"
                     * 
                     * The CTHeight element specifies the height of the current table row within the
                     * current table. This height shall be used to determine the resulting
                     * height of the table row, which may be absolute or relative (depending on
                     * its attribute values). If omitted, then the table row shall automatically
                     * resize its height to the height required by its contents 
                     */
                    CTHeight ht = trPr.addNewTrHeight();
                    ht.setVal(BigInteger.valueOf((int) t.getContainingElements().get(i).getStyle().getFontSize() * 50));

                    // after it reached the last cell of a row, it inserts a new row
                } else if (i != 0 && i % t.getColumnCount() == 0 && t.getContainingElements().get(i) instanceof Cell) {
                    row = table.insertNewTableRow(h);
                    // new row, so increase the h(elp) value by one
                    h++;

                    cell = row.addNewTableCell();
                    cell = this.processCell(cell, t.getContainingElements().get(i));

                } else if (t.getContainingElements().get(i) instanceof Cell) {
                    cell = row.addNewTableCell();
                    cell = this.processCell(cell, t.getContainingElements().get(i));
                }
            }
            // finished building table
        }

        if (e instanceof NewPage) {
            NewPage np = (NewPage) e;
            // new paragagh is needed, because otherwise the last paragraph
            // of the page, would be shifted to the new page
            this.actualparagraph = this.doc.createParagraph();
            // PageBreak(true) is used to create a new page
            // actualparagraph continues on new page
            this.actualparagraph.setPageBreak(true);

            for (Element subElement : np.getContainingElements()) {
                this.processElement(subElement);
            }
        }

        if (e instanceof Image) {
            Image i = (Image) e;
            FileInputStream is = null;

            /* 
             * blipId is used by OLE2 and XWPF to identify the picture,
             * it will be generated and returned by the addPicture function
             * if u can set it somewhere, do NOT (you will fail :D)
             * create it manually this variable is only used for testing purposes
             */
            String blipId;

            try {
                if (i.getImageFile() != null) {
                    is = new FileInputStream(i.getImageFile());
                    // add picture to the document
                    blipId = doc.addPictureData(is, pictureFormat(i.getImageFile().toString()));

                    // create the previously added picture at the current run location
                    doc.createPicture(this.actualPictureIndex, 128, 128);

                    // increment the actualPictureIndex
                    this.actualPictureIndex++;
                    is.close();
                } else if (i.getData() != null) {
                    // add picture to the document
                    blipId = doc.addPictureData(i.getData(), pictureFormat(i.getFileType()));

                    // create the previously added picture at the current run location
                    doc.createPicture(this.actualPictureIndex, 128, 128);

                    // increment the actualPictureIndex
                    this.actualPictureIndex++;
                }
            } catch (InvalidFormatException ex) {
                throw new DocumentWriterException("picture format not supported.\n"
                        + "following picture formats are supported: "
                        + "emf, wmf, pict, jpeg, jpg, png, dib, gif, tiff, eps, bmp or wpg");
            } catch (IOException ex) {
                throw new DocumentWriterException("not able to read the picture");
            }


        }
        if (e instanceof NewLine) {
            NewLine nl = (NewLine) e;

            if (this.actualrun == null) {
                if (this.actualparagraph == null) {
                    this.actualparagraph = this.doc.createParagraph();
                    this.actualrun = this.actualparagraph.createRun();
                } else {
                    this.actualrun = this.actualparagraph.createRun();
                }
            }
            this.actualrun.addBreak(BreakClear.ALL);

            // iterate through all subElements
            for (Element subElement : nl.getContainingElements()) {
                this.processElement(subElement);
            }
        }
    }

    /**
     * Processes the given xwpfcell with the content and style of the given
     * cell.
     *
     * @param cell this Element will get added the content & style of the
     * element
     * @param e the content & style of this element will be used
     * @return returns the processed xwpfcell with the content & style applied
     */
    private XWPFTableCell processCell(XWPFTableCell cell, Element e) {
        XWPFParagraph p;
        XWPFRun r;

        // get first paragraph from cell and apply text & style
        p = cell.getParagraphs().get(0);
        p.setSpacingAfter(0);
        r = styleRun(e, p.createRun());

        // set alignment
        if (e.getStyle() != null) {
            switch (e.getStyle().getAlignment()) {
                case Style.ALIGNMENT_CENTER:
                    p.setAlignment(ParagraphAlignment.CENTER);
                    break;
                case Style.ALIGNMENT_LEFT:
                    p.setAlignment(ParagraphAlignment.LEFT);
                    break;
                case Style.ALIGNMENT_RIGHT:
                    p.setAlignment(ParagraphAlignment.RIGHT);
                    break;
                default:
                    p.setAlignment(ParagraphAlignment.LEFT);
            }
        } else {
            p.setAlignment(ParagraphAlignment.LEFT);
        }

        // set cell color
        cell.setColor(String.format("%02x%02x%02x",
                e.getStyle().getBackgroundColor().getRed(),
                e.getStyle().getBackgroundColor().getGreen(),
                e.getStyle().getBackgroundColor().getBlue()));

        //place the text in the middle of the cell
        // get a table cell properties element (tcPr)
        CTTcPr tcpr = cell.getCTTc().addNewTcPr();

        // set vertical alignment to "center"
        CTVerticalJc va = tcpr.addNewVAlign();
        va.setVal(STVerticalJc.CENTER);

        cell.getTableRow().getCtRow().addNewTrPr().addNewTblCellSpacing().setW(BigInteger.valueOf(0));
        cell.getTableRow().getCtRow().getTrPr().addNewTrHeight().setVal(BigInteger.valueOf(10));

        return cell;
    }

    /**
     * applies the given text and style to the given XWPFRun and returns it can
     * only be used for paragraphs, phrases or cells
     *
     * @param e this Element is used in order to extract the text and style
     * information
     * @param r text and style information is added to this XWPFRun
     * @return returns the given XWPFRun r with the added text and styles of
     * Element e
     */
    private XWPFRun styleRun(Element e, XWPFRun r) {
        String text;
        if (e instanceof Paragraph || e instanceof Phrase || e instanceof Cell) {
            text = ((TextElement) e).getContent();
            r.setText(text);
            // set styling information
            if (e.getStyle() != null) {
                // switch through font family
                switch (e.getStyle().getFontFamily()) {
                    case Style.COURIER_NEW:
                        r.setFontFamily("Courier New");
                        break;
                    case Style.HELVETICA:
                        r.setFontFamily("Helvetica");
                        break;
                    case Style.TIMES_NEW_ROMAN:
                        r.setFontFamily("Times New Roman");
                        break;
                    default:
                        // Helvetica is the default FontFamily
                        r.setFontFamily("Helvetica");
                }
                r.setFontSize((int) (e.getStyle().getFontSize()));
                r.setBold(e.getStyle().isBold());
                r.setItalic(e.getStyle().isItalic());
                // run.setColor() only accepts hex colors like FF0AB5
                // color names or the color rgb value don't work
                // this.actualrun.setColor(p.getStyle().getForegroundColor());
                r.setColor(String.format("%02x%02x%02x",
                        e.getStyle().getForegroundColor().getRed(),
                        e.getStyle().getForegroundColor().getGreen(),
                        e.getStyle().getForegroundColor().getBlue()));
            } else {
                Style s = new Style();
                r.setFontFamily("Helvetica");
                r.setFontSize((int) (s.getFontSize()));
                r.setBold(s.isBold());
                r.setItalic(s.isItalic());
            }
            return r;
        }
        return null;
    }

    /**
     * Compares the picture file extension with all possible picture formats
     * supported by XWPF
     *
     * @param pictureFile
     * @return if a corresponding format is found, the int value of
     * XWPFDocument.PICTURE_TYPE is returned, otherwise it will return -1
     */
    private int pictureFormat(String pictureFile) throws DocumentWriterException {
        int format;
        if (pictureFile.endsWith("emf")) {
            format = XWPFDocument.PICTURE_TYPE_EMF;
        } else if (pictureFile.endsWith("wmf")) {
            format = XWPFDocument.PICTURE_TYPE_WMF;
        } else if (pictureFile.endsWith("pict")) {
            format = XWPFDocument.PICTURE_TYPE_PICT;
        } else if (pictureFile.endsWith("jpeg") || pictureFile.endsWith("jpg")) {
            format = XWPFDocument.PICTURE_TYPE_JPEG;
        } else if (pictureFile.endsWith("png")) {
            format = XWPFDocument.PICTURE_TYPE_PNG;
        } else if (pictureFile.endsWith("dib")) {
            format = XWPFDocument.PICTURE_TYPE_DIB;
        } else if (pictureFile.endsWith("gif")) {
            format = XWPFDocument.PICTURE_TYPE_GIF;
        } else if (pictureFile.endsWith("tiff")) {
            format = XWPFDocument.PICTURE_TYPE_TIFF;
        } else if (pictureFile.endsWith("eps")) {
            format = XWPFDocument.PICTURE_TYPE_EPS;
        } else if (pictureFile.endsWith("bmp")) {
            format = XWPFDocument.PICTURE_TYPE_BMP;
        } else if (pictureFile.endsWith("wpg")) {
            format = XWPFDocument.PICTURE_TYPE_WPG;
        } else {
            throw new DocumentWriterException("picture format not supported.\n"
                    + "following picture formats are supported: "
                    + "emf, wmf, pict, jpeg, jpg, png, dib, gif, tiff, eps, bmp or wpg");
        }
        return format;
    }

    @Override
    public void addFooter(Phrase p) throws DocumentWriterException {
        System.out.println("XWPF Apache POI 3.8 has problems to convert and insert headers into a .doc oder .docx file.");
    }

    @Override
    public void addHeader(Phrase p) throws DocumentWriterException {
        System.out.println("XWPF Apache POI 3.8 has problems to convert and insert headers into a .doc oder .docx file.");
    }

    @Override
    public void provideMetaData(MetaInformation meta) throws DocumentWriterException {
        if (meta == null) {
            // you shan't pass a null meta-info!
            throw new DocumentWriterException("Your meta-information should not be null!");
        }
        // everything went wells
        this.mi = meta;
    }

    @Override
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public void buildDocument() throws DocumentWriterException {
        if (this.orientation == DocumentWriter.FORMAT_UPRIGHT) {
            try {
                this.doc.getDocument().getBody().getSectPr().getPgSz().setOrient(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.PORTRAIT);
            } catch (NullPointerException ex) {
                this.doc.getDocument().getBody().addNewSectPr().addNewPgSz().setOrient(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.PORTRAIT);
            }
        } else if (this.orientation == DocumentWriter.FORMAT_LANDSCAPE) {
            try {
                // this code line will throw a nullpointer exception, because a newly created document (from the scratch) has no SectPr nor PgSz
                this.doc.getDocument().getBody().getSectPr().getPgSz().setOrient(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.LANDSCAPE);
            } catch (NullPointerException ex) {
                // set format to landscape
                this.doc.getDocument().getBody().addNewSectPr().addNewPgSz().setOrient(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.LANDSCAPE);
                // taken from a normal docx file generated by MS WORD 2010
                this.doc.getDocument().getBody().getSectPr().getPgSz().setW(BigInteger.valueOf(16838));
                this.doc.getDocument().getBody().getSectPr().getPgSz().setH(BigInteger.valueOf(11906));
            }
        }


        // iterate throw all elements and process them
        for (Element element : containingElements) {
            processElement(element);
        }

        // setting metainformation
        if (mi != null) {
            POIXMLProperties xmlProperties = doc.getProperties();
            POIXMLProperties.CoreProperties coreProperties = xmlProperties.getCoreProperties();

            coreProperties.setCreator(mi.getAuthor());
            coreProperties.setTitle(mi.getTitle());
            coreProperties.setKeywords(mi.getKeywords());
            coreProperties.setDescription(mi.getComments());
            coreProperties.setSubjectProperty(mi.getSubject());
        }
    }

    @Override
    public void resetDocument() {
        this.doc = new CustomXWPFDocument();
        this.containingElements.clear();
        this.actualparagraph = null;
        this.actualrun = null;
        this.mi = null;
        this.defaultStyle = new Style();
        this.actualPictureIndex = 0;
        this.orientation = DocumentWriter.FORMAT_UPRIGHT;
    }
}