package com.webpageparser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final Pattern mailPattern = Pattern.compile
            ("[a-zA-Z]{1}[a-zA-Z\\d\\u002E\\u005F]+@([a-zA-Z]+\\u002E){1,2}((net)|(com)|(org)|(ru))");
    private static final Pattern linkPattern = Pattern.compile
            ("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]", Pattern.CASE_INSENSITIVE);


    public static List<String> getAllMails(StringBuilder page) {
        List<String> result = new ArrayList<>();
        Matcher matcher = mailPattern.matcher(page);
        while (matcher.find()) {
            result.add(page.substring(matcher.start(), matcher.end()));
        }
        System.out.println(result);
        return result;
    }

    public static List<String> getAllLinksOnPage(StringBuilder page) {
        List<String> result = new ArrayList<>();
        Matcher matcher = linkPattern.matcher(page);
        while (matcher.find()) {
            String res = page.substring(matcher.start(), matcher.end());
            result.add(res.substring(9, res.length() - 1));
        }
        return result;
    }
}
