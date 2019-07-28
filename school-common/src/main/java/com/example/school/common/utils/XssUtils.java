package com.example.school.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 20:56
 * description: Web防火墙工具类
 */
public class XssUtils {

    /**
     * 过滤XSS脚本内容
     *
     * @param value
     * @return
     */
    public static String stripXSS(String value) {
        String rlt = null;

        if (null != value) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            rlt = value.replaceAll("", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid anything in a src='...' type of expression
			/*scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");*/

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE
                    | Pattern.MULTILINE | Pattern.DOTALL);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE
                    | Pattern.MULTILINE | Pattern.DOTALL);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE
                    | Pattern.MULTILINE | Pattern.DOTALL);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE
                    | Pattern.MULTILINE | Pattern.DOTALL);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
        }

        return rlt;
    }

    /**
     * 过滤SQL注入内容
     *
     * @param value
     * @return
     */
    public static String stripSqlInjection(String value) {
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|,|like|//|/|%|#";

        if (StringUtils.isNotEmpty(value)) {
            String[] values = value.split(" ");

            String[] badStrArr = badStr.split("\\|");
            for (String aBadStrArr : badStrArr) {
                for (int j = 0; j < values.length; j++) {
                    if (values[j].equalsIgnoreCase(aBadStrArr)) {
                        values[j] = "forbid";
                        values[j] = "";
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                if (i == values.length - 1) {
                    sb.append(values[i]);
                } else {
                    sb.append(values[i]).append(" ");
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 过滤SQL 和 XSS注入内容
     *
     * @param value
     * @return
     */
    public static String stripSqlXss(String value) {
        return stripXSS(stripSqlInjection(value));
    }
}
