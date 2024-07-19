package io.jenkins.plugins.teambition;


import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TbHelper {
    public static final Logger logger = Logger.getLogger(TbHelper.class.getName());
    
    public static boolean isURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isNotBlank(String value) {
        return StringUtils.isNotBlank(value);
    }
    
    public static boolean isValidObjectId(String id) {
        // ObjectId的正则表达式，匹配24个十六进制字符
        String objectIdPattern = "^[a-fA-F0-9]{24}$";
        Pattern pattern = Pattern.compile(objectIdPattern);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
    }
    
    public static String prettyJSON(Object object) {
        return new JSONObject(object).toString();
    }
    
}
