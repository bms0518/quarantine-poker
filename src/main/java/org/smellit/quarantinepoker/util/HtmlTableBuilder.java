package org.smellit.quarantinepoker.util;

public class HtmlTableBuilder {


    private final StringBuilder table = new StringBuilder();
    private static final String TABLE_START_BORDER = "<table border=\"1\">";
    private static final String TABLE_START = "<table>";
    private static final String TABLE_END = "</table>";
    private static final String HEADER_START = "<th>";
    private static final String HEADER_END = "</th>";
    private static final String ROW_START = "<tr>";
    private static final String ROW_END = "</tr>";
    private static final String COLUMN_START = "<td>";
    private static final String COLUMN_END = "</td>";

    private int columns;

    /**
     * @param border
     * @param columns
     */
    public HtmlTableBuilder(boolean border, int columns) {
        this.columns = columns;
        table.append(border ? TABLE_START_BORDER : TABLE_START);
        table.append(TABLE_END);
    }


    /**
     * @param values
     */
    public void addTableHeader(String... values) {
        if (values.length == columns) {
            int lastIndex = table.lastIndexOf(TABLE_END);
            if (lastIndex > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(ROW_START);
                for (String value : values) {
                    sb.append(HEADER_START);
                    sb.append(value);
                    sb.append(HEADER_END);
                }
                sb.append(ROW_END);
                table.insert(lastIndex, sb.toString());
            }
        }
    }


    /**
     * @param values
     */
    public void addRowValues(String... values) {
        if (values.length == columns) {
            int lastIndex = table.lastIndexOf(ROW_END);
            if (lastIndex > 0) {
                int index = lastIndex + ROW_END.length();
                StringBuilder sb = new StringBuilder();
                sb.append(ROW_START);
                for (String value : values) {
                    sb.append(COLUMN_START);
                    sb.append(value);
                    sb.append(COLUMN_END);
                }
                sb.append(ROW_END);
                table.insert(index, sb.toString());
            }
        }
    }


    /**
     * @return
     */
    public String build() {
        return table.toString();
    }
}
