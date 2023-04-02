package org.squidmin.bigquery.logger;

public class LogFont {

    public static final String GREEN = "\u001b[32m";

    public static final String CYAN = "\u001b[36m";

    public static final String RESET = "\u001b[0m";

    public static final String BOLD = "\u001b[1m";

    public static String boldGreen(String arg) {
        return BOLD + GREEN + arg + RESET;
    }

}
