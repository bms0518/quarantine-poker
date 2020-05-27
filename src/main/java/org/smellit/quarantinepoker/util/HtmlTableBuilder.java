package org.smellit.quarantinepoker.util;

import java.util.List;

public class HtmlTableBuilder {


    private final StringBuilder table = new StringBuilder();
    private static final String TABLE_START = " <table class=\"table table-bordered table-striped\">";
    private static final String TABLE_END = "</table>";
    private static final String HEADER_START = "<th scope=\"col\">";
    private static final String HEADER_END = "</th>";

    private static final String THEAD_START = "<thead>";
    private static final String THEAD_END = "</thead>";

    private static final String TBODY_START = "<tbody>";
    private static final String TBODY_END = "</tbody>";

    private static final String ROW_START = "<tr>";
    private static final String ROW_END = "</tr>";
    private static final String COLUMN_START = "<td>";
    private static final String COLUMN_END = "</td>";

    private int columns;

    /**
     * @param columns
     */
    public HtmlTableBuilder(int columns, List<String> tableHeaders, List<List<String>> rowValues) {
        this.columns = columns;
        table.append(TABLE_START);
        table.append(addTableHeader(tableHeaders));
        table.append(addTableBody(rowValues));
        table.append(TABLE_END);
    }


    private String addTableHeader(List<String> tableHeaders) {
        StringBuilder sb = new StringBuilder();
        if (tableHeaders.size() == columns) {
            sb.append(THEAD_START);
            sb.append(ROW_START);
            for (String value : tableHeaders) {
                sb.append(HEADER_START);
                sb.append(value);
                sb.append(HEADER_END);
            }
            sb.append(ROW_END);
            sb.append(THEAD_END);
        }
        return sb.toString();
    }


    private String addTableBody(List<List<String>> rowValues) {
        StringBuilder sb = new StringBuilder();

        sb.append(TBODY_START);

        int index = 1;
        for (List<String> row : rowValues) {
            sb.append(addRowValues(index, row));
            index++;
        }
        sb.append(TBODY_END);
        return sb.toString();
    }


    private String addRowValues(int rowIndex, List<String> row) {
        StringBuilder sb = new StringBuilder();
        sb.append(ROW_START);
        sb.append("<th scope=\"row\">");
        sb.append(rowIndex);
        sb.append("</th>");
        for (String value : row) {
            sb.append(COLUMN_START);
            sb.append(value);
            sb.append(COLUMN_END);
        }
        sb.append(ROW_END);
        return sb.toString();
    }


    /**
     * @return
     */
    public String build() {
        return table.toString();
    }
}
