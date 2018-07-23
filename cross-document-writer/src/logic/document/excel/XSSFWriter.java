package logic.document.excel;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * The XSSFWriter processes all added Elements and converts them into a XLSX
 * file. This new file can be written to disk or streamed to an PrimeFaces
 * compatible application.
 *
 * In order to fulfil the given tasks, the XSSFWriter uses the APACHE POI 3.9
 * library (XSSF). A short description of the XSSF lib:
 *
 * HSSF and XSSF provides ways to read spreadsheets create, modify, read and
 * write XLS and XLSX documents. They provide:
 *
 * low level structures for those with special needs an eventmodel api for
 * efficient read-only access a full usermodel api for creating, reading and
 * modifying DOC and DOCX files
 *
 * @author Harald Gleiss & Thorsten Jojart
 */
public class XSSFWriter implements DocumentWriter {

    /**
     * The xssf workbook is used in order to create xlsx elements and it
     * provides low level API functions.
     */
    private XSSFWorkbook doc;
    /*
     * List of all used sheets of the workbook
     */
    private List<Sheet> sheets;
    /**
     * File destination which is used for the writeToDisk function.
     */
    private File xssfFile;
    /**
     * MetaInformation which will be added to newly created documents
     */
    private MetaInformation mi;
    /**
     * List of all Elements, that get added to this document, except sheets
     */
    private List<Element> containingElements = new ArrayList<>();
    /**
     * If a element has no style, the defaultStyle will be used
     */
    private Style defaultStyle;
    /**
     * Index of the actual used sheet of the sheets list. New sheets in a new
     * workbook are starting at index 0.
     */
    private int actualSheet = -1;
    /**
     * Index of the actual used row. New rows in a new workbook are starting at
     * index 0.
     */
    private int actualRow = -1;
    /**
     * Index of the actual used column. New columns in a new workbook are
     * starting at index 0.
     */
    private int actualColumn = 0;
    /**
     * Sets the orientation format of the sheet either to upright (Hochformat)
     * or landscape (Querformat). Default is upright.
     */
    private int sheetOrientation = DocumentWriter.FORMAT_UPRIGHT;

    /**
     * This constructor creates a new XSSFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @throws DocumentWriterException
     */
    public XSSFWriter() throws DocumentWriterException {
        this(null, null);
    }

    /**
     * This constructor creates a new XWPFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @param f File destination, .xlsx must be added manually
     * @throws DocumentWriterException
     */
    public XSSFWriter(File f) throws DocumentWriterException {
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
    public XSSFWriter(MetaInformation mi) throws DocumentWriterException {
        this(null, mi);
    }

    /**
     * This constructor creates a new XWPFWriter with default properties. If no
     * File is provided, it will create a temporary file. If no MetaInformation
     * is provided, it will not add any MetaInformation to the document.
     *
     * @param f File destination, .xlsx must be added manually
     * @param mi Metainformation which will be added to the document
     * @throws DocumentWriterException
     */
    public XSSFWriter(File f, MetaInformation mi) throws DocumentWriterException {
        //test if file is null
        if (f != null) {
            this.xssfFile = f;
        } else {
            try {
                //tmp file to probably provide the file via an outputstream
                this.xssfFile = File.createTempFile("", ".tmp");
            } catch (IOException ex) {
                throw new DocumentWriterException(ex);
            }
        }

        //test if metainformation is null
        if (mi != null) {
            this.mi = mi;
        }

        // Initialize values
        defaultStyle = new Style();
        doc = new XSSFWorkbook();
        sheets = new ArrayList<>();

        sheets.add(doc.createSheet(" "));
        actualSheet++;

        // new sheets have no rows
        this.newRow();
    }

    @Override
    public void addElement(Element e) throws DocumentWriterException {
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
    public StreamedContent streamDocument() {
        try {
            //ensure we are working with the actual version of the file
            this.writeToDisk();
            return new DefaultStreamedContent(new FileInputStream(this.xssfFile), "application/xlsx");
        } catch (DocumentWriterException | FileNotFoundException ex) {
            Logger.getLogger(XSSFWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void writeToDisk() throws DocumentWriterException {
        try {
            doc.write(new FileOutputStream(xssfFile));
        } catch (IOException ex) {
            throw new DocumentWriterException(ex);
        }
    }

    /**
     * Tries to find the corresponding Element for the Element e and adds it to
     * the document. Each type of Element is treated differently.
     *
     * @param e Element which will pe processed
     * @throws DocumentWriterException
     */
    private void processElement(logic.document.element.Element e) throws DocumentWriterException {
        if (e instanceof Phrase) {
            Phrase p = (Phrase) e;
            org.apache.poi.ss.usermodel.Cell c = this.sheets.get(actualSheet).getRow(actualRow).createCell(actualColumn);

            c.setCellStyle(this.getCorrespondingStyle(e.getStyle()));
            c.setCellValue(p.getContent());
            this.sheets.get(this.actualSheet).autoSizeColumn(this.actualColumn);
            //increment the cell position
            this.actualColumn++;

            for (Element subElement : p.getContainingElements()) {
                this.processElement(subElement);
            }
        }
        if (e instanceof Paragraph) {
            Paragraph p = (Paragraph) e;

            if (!(this.actualColumn == 0 && this.actualRow == 0)) {
                this.newRow();
            }

            org.apache.poi.ss.usermodel.Cell c = this.sheets.get(actualSheet).getRow(actualRow).createCell(actualColumn);
            c.setCellStyle(this.getCorrespondingStyle(e.getStyle()));
            c.setCellValue(p.getContent());
            this.sheets.get(this.actualSheet).autoSizeColumn(this.actualColumn);
            //increment the cell position
            this.actualColumn++;

            for (Element subElement : p.getContainingElements()) {
                this.processElement(subElement);
            }
        }
        // empty cells of table (no element) get ignored (difference to iText where they add a empty cell without text)
        if (e instanceof Table) {
            Table t = (Table) e;

            // iterate through all subcells
            for (int i = 0; i < t.getContainingElements().size(); i++) {
                if (i % t.getColumnCount() == 0 && i != 0) {
                    this.actualRow++;

                    this.actualColumn -= t.getColumnCount();
                    sheets.get(actualSheet).createRow(this.actualRow);
                    this.processElement(t.getContainingElements().get(i));
                } else if (t.getContainingElements().get(i) instanceof Cell) {
                    this.processElement(t.getContainingElements().get(i));
                }
            }
        }

        if (e instanceof Cell) {
            Cell c = (Cell) e;
            org.apache.poi.ss.usermodel.Cell ac = this.sheets.get(actualSheet).getRow(actualRow).createCell(actualColumn);

            ac.setCellStyle(this.getCorrespondingStyle(e.getStyle()));
            ac.setCellValue(c.getContent());

            //set borders for cells (only used in table)
            ac.getCellStyle().setBorderTop(org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN);
            ac.getCellStyle().setBorderLeft(org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN);
            ac.getCellStyle().setBorderRight(org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN);
            ac.getCellStyle().setBorderBottom(org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN);

            this.sheets.get(this.actualSheet).autoSizeColumn(this.actualColumn);

            //increment the cell position
            this.actualColumn++;


            for (Element subElement : c.getContainingElements()) {
                this.processElement(subElement);
            }
        }

        /**
         * Note that sheet name is Excel must not exceed 31 characters and must
         * not contain any of the any of the following characters: 0x0000 0x0003
         * colon (:) backslash (\) asterisk (*) question mark (?) forward slash
         * (/) opening square bracket ([) closing square bracket (])
         *
         * You can use
         * org.apache.poi.ss.util.WorkbookUtil#createSafeSheetName(String
         * nameProposal)} for a safe way to create valid names, this utility
         * replaces invalid characters with a space (' ') or can be used with a
         * char replaceChar to set the replacement char by yourself
         */
        if (e instanceof NewPage) {
            NewPage np = (NewPage) e;
            if (np.getContent() == null) {
                this.sheets.add(doc.createSheet("" + this.actualSheet));
            } else {
                this.sheets.add(doc.createSheet(WorkbookUtil.createSafeSheetName(np.getContent(), '_')));
            }

            this.actualSheet++;
            this.actualRow = -1;
            this.actualColumn = 0;

            // new sheets have no rows
            this.newRow();

            for (Element subElement : np.getContainingElements()) {
                this.processElement(subElement);
            }
        }

        if (e instanceof NewLine) {
            NewLine nl = (NewLine) e;
            this.newRow();

            for (Element subElement : nl.getContainingElements()) {
                this.processElement(subElement);
            }
        }

        if (e instanceof Image) {
            Image i = (Image) e;
            int pictureIndex = 0;
            byte[] bytes = null;
            FileInputStream is = null;
            Picture picture;
            Drawing drawing = this.sheets.get(actualSheet).createDrawingPatriarch();
            CreationHelper helper = doc.getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();

            // insert picture at P(x/y) where x represents col and y represents row
            anchor.setCol1(actualColumn);
            anchor.setRow1(actualRow);

            if (i.getImageFile() != null) {
                try {
                    is = new FileInputStream(i.getImageFile());
                    bytes = IOUtils.toByteArray(is);
                    pictureIndex = doc.addPicture(bytes, pictureFormat(i.getFileType()));
                    is.close();
                } catch (IOException ex) {
                    throw new DocumentWriterException(ex);
                }
            } else if (i.getData() != null) {
                bytes = i.getData();
                pictureIndex = doc.addPicture(bytes, pictureFormat(i.getFileType()));
            }
            picture = drawing.createPicture(anchor, pictureIndex);
            // resizing must be done, if not, excel will fail xml convertion and damage the .xlsx file
            picture.resize();
            // auto resize column
            // this.doc.getSheetAt(this.actualSheet).autoSizeColumn(this.actualColumn);

            for (Element subElement : i.getContainingElements()) {
                this.processElement(subElement);
            }

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

    /**
     * converts a Style to an XSSFCellStyle that can be used to style XSSF
     * cells.
     *
     * @param s Style that should be converted to a XSSFCellStyle
     * @return returns a new XSSFCellStyle with the styles applied from Style s
     */
    private XSSFCellStyle getCorrespondingStyle(Style s) {
        XSSFCellStyle cellStyle = doc.createCellStyle();
        XSSFFont font = doc.createFont();
        Color blackandwhiteBg, blackandwhiteFg;

        if (s != null) {
            // black is white and
            // white is black 
            // known (but not fixed) XSSF Bug
            blackandwhiteBg = s.getBackgroundColor();
            blackandwhiteFg = s.getForegroundColor();
            /*
             if (blackandwhiteBg == Color.BLACK) {
             blackandwhiteBg = Color.WHITE;
             } else if (blackandwhiteBg == Color.WHITE) {
             blackandwhiteBg = Color.BLACK;
             } */
            if (blackandwhiteFg == Color.BLACK) {
                blackandwhiteFg = Color.WHITE;
            } else if (blackandwhiteFg == Color.WHITE) {
                blackandwhiteFg = Color.BLACK;
            }

            //switch through font family
            switch (s.getFontFamily()) {
                case Style.COURIER_NEW:
                    font.setFontName("Courier New");
                    break;
                case Style.HELVETICA:
                    font.setFontName("Helvetica");
                    break;
                case Style.TIMES_NEW_ROMAN:
                    font.setFontName("Times New Roman");
                    break;
                default:
                    font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
            }

            font.setBold(s.isBold());
            font.setItalic(s.isItalic());
            font.setColor(new XSSFColor(blackandwhiteFg));
            font.setFontHeight(s.getFontSize());
            cellStyle.setFillForegroundColor(new XSSFColor(blackandwhiteBg));
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cellStyle.setFont(font);

            // set alignment
            switch (s.getAlignment()) {
                case Style.ALIGNMENT_CENTER:
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    break;
                case Style.ALIGNMENT_LEFT:
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                    break;
                case Style.ALIGNMENT_RIGHT:
                    cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                    break;
                default:
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
            }
            return cellStyle;
        }

        // black is white and
        // white is black 
        // known (but not fixed) XSSF Bug
        blackandwhiteBg = defaultStyle.getBackgroundColor();
        blackandwhiteFg = defaultStyle.getForegroundColor();

        if (blackandwhiteBg == Color.BLACK) {
            blackandwhiteBg = Color.WHITE;
        } else if (blackandwhiteBg == Color.WHITE) {
            blackandwhiteBg = Color.BLACK;
        }
        if (blackandwhiteFg == Color.BLACK) {
            blackandwhiteFg = Color.WHITE;
        } else if (blackandwhiteFg == Color.WHITE) {
            blackandwhiteFg = Color.BLACK;
        }


        //switch through font family
        switch (defaultStyle.getFontFamily()) {
            case Style.COURIER_NEW:
                font.setFontName("Courier New");
                break;
            case Style.HELVETICA:
                font.setFontName("Helvetica");
                break;
            case Style.TIMES_NEW_ROMAN:
                font.setFontName("Times New Roman");
                break;
            default:
                font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        }

        font.setBold(defaultStyle.isBold());
        font.setItalic(defaultStyle.isItalic());
        font.setColor(new XSSFColor(blackandwhiteFg));
        font.setFontHeight(defaultStyle.getFontSize());
        cellStyle.setFillForegroundColor(new XSSFColor(blackandwhiteBg));
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setFont(font);

        // set alignment
        switch (defaultStyle.getAlignment()) {
            case Style.ALIGNMENT_CENTER:
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                break;
            case Style.ALIGNMENT_LEFT:
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                break;
            case Style.ALIGNMENT_RIGHT:
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                break;
            default:
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
        }
        return cellStyle;
    }

    /**
     * compares the picture file extension with all possible picture formats
     * supported by XSSF
     *
     * @param pictureFile
     * @return if a corresponding format is found, the int value of
     * XSSFWorkbook.PICTURE_TYPE is returned, otherwise it will return -1
     */
    private static int pictureFormat(String pictureFile) throws DocumentWriterException {
        int format;
        if (pictureFile.endsWith("emf")) {
            format = XSSFWorkbook.PICTURE_TYPE_EMF;
        } else if (pictureFile.endsWith("wmf")) {
            format = XSSFWorkbook.PICTURE_TYPE_WMF;
        } else if (pictureFile.endsWith("pict")) {
            format = XSSFWorkbook.PICTURE_TYPE_PICT;
        } else if (pictureFile.endsWith("jpeg") || pictureFile.endsWith("jpg")) {
            format = XSSFWorkbook.PICTURE_TYPE_JPEG;
        } else if (pictureFile.endsWith("png")) {
            format = XSSFWorkbook.PICTURE_TYPE_PNG;
        } else if (pictureFile.endsWith("dib")) {
            format = XSSFWorkbook.PICTURE_TYPE_DIB;
        } else if (pictureFile.endsWith("gif")) {
            format = XSSFWorkbook.PICTURE_TYPE_GIF;
        } else if (pictureFile.endsWith("tiff")) {
            format = XSSFWorkbook.PICTURE_TYPE_TIFF;
        } else if (pictureFile.endsWith("eps")) {
            format = XSSFWorkbook.PICTURE_TYPE_EPS;
        } else if (pictureFile.endsWith("bmp")) {
            format = XSSFWorkbook.PICTURE_TYPE_BMP;
        } else if (pictureFile.endsWith("wpg")) {
            format = XSSFWorkbook.PICTURE_TYPE_WPG;
        } else {
            throw new DocumentWriterException("picture format not supported.\n"
                    + "following picture formats are supported: "
                    + "emf, wmf, pict, jpeg, jpg, png, dib, gif, tiff, eps, bmp or wpg");
        }
        return format;
    }

    @Override
    public void addFooter(Phrase p) throws DocumentWriterException {
        throw new UnsupportedOperationException("Excel format does not support footer.");
    }

    @Override
    public void addHeader(Phrase p) throws DocumentWriterException {
        throw new UnsupportedOperationException("Excel format does not support header.");
    }

    /**
     * Creates a new row and sets the actualColumn to 0, also actualRow gets
     * incremented by one.
     */
    private void newRow() {
        this.actualRow++;
        this.actualColumn = 0;
        sheets.get(actualSheet).createRow(this.actualRow);
    }

    @Override
    public void setOrientation(int orientation) {
        this.sheetOrientation = orientation;
    }

    @Override
    public void buildDocument() throws DocumentWriterException {
        // set orientation format
        if (sheetOrientation == DocumentWriter.FORMAT_UPRIGHT) {
            for (Sheet sheet : sheets) {
                sheet.getPrintSetup().setLandscape(false);
            }
        } else if (sheetOrientation == DocumentWriter.FORMAT_LANDSCAPE) {
            for (Sheet sheet : sheets) {
                sheet.getPrintSetup().setLandscape(true);
            }
        }

        // setting metainformation
        if (mi != null) {
            POIXMLProperties xmlProperties = doc.getProperties();
            CoreProperties coreProperties = xmlProperties.getCoreProperties();

            coreProperties.setCreator(mi.getAuthor());
            coreProperties.setTitle(mi.getTitle());
            coreProperties.setKeywords(mi.getKeywords());
            coreProperties.setDescription(mi.getComments());
            coreProperties.setSubjectProperty(mi.getSubject());
        }


        // create rows and cells
        for (Element element : containingElements) {
            processElement(element);
        }
    }

    @Override
    public void resetDocument() {
        this.containingElements.clear();
        this.defaultStyle = new Style();
        this.doc = new XSSFWorkbook();
        this.sheets.clear();
        this.mi = null;
        this.sheetOrientation = DocumentWriter.FORMAT_UPRIGHT;
        this.actualSheet = -1;
        this.actualRow = -1;
        this.actualColumn = 0;
        this.sheets.add(doc.createSheet(" "));
        this.actualSheet++;

        // new sheets have no rows
        this.newRow();
    }
}