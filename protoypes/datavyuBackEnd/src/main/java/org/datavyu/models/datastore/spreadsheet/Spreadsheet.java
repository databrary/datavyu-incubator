package java.org.datavyu.models.datastore.spreadsheet;

import java.org.datavyu.models.datastore.Code;
import java.org.datavyu.models.datastore.spreadsheet.cell.Cell;
import java.org.datavyu.models.datastore.spreadsheet.column.Column;
import java.util.List;

public interface Spreadsheet {

    /**
     *
     * @param title
     * @return
     */
    Spreadsheet createDefaultSpreadsheet(final String title);

    /**
     *
     * @return
     */
    int getID();

    /**
     *
     * @return
     */
    String getTitle();

    /**
     *
     * @param title
     * @return
     */
    Spreadsheet setTitle(final String title);

    /**
     *
     * @param title
     * @return
     */
    Spreadsheet createColumn(final String title);

    /**
     *
     * @param title
     * @param codes
     * @return
     */
    Spreadsheet createColumn(final String title, final List<Code> codes);

    /**
     *
     * @param title
     * @param codes
     * @param cells
     * @return
     */
    Spreadsheet createColumn(final String title, final List<Code> codes, final List<Cell> cells);

    /**
     *
     * @param column
     * @return
     */
    Spreadsheet addColumn(final Column column);

    /**
     *
     * @param columns
     * @return
     */
    Spreadsheet addColumns(final List<Column> columns);

    /**
     *
     * @param column
     * @return
     */
    Spreadsheet removeColumn(final Column column);

    /**
     *
     * @param columns
     * @return
     */
    Spreadsheet removeColumn(final List<Column> columns);

    /**
     *
     * @return
     */
    List<Column> getColumns();

    /**
     *
     * @param cell
     * @return
     */
    Column getColumn(final Cell cell);

    /**
     *
     * @param column
     * @return
     */
    boolean contains(final Column column);
}
