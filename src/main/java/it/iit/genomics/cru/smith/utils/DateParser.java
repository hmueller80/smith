package it.iit.genomics.cru.smith.utils;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.sampleBeans.UploadSampleRequestBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @(#)DateParser.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Utility class for parsing dates.
 * 
 * http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
 * Date and Time Pattern            Result
 * "yyyy.MM.dd G 'at' HH:mm:ss z"      2001.07.04 AD at 12:08:56 PDT
 * "EEE, MMM d, ''yy"                  Wed, Jul 4, '01
 * "h:mm a"                            12:08 PM
 * "hh 'o''clock' a, zzzz"             12 o'clock PM, Pacific Daylight Time
 * "K:mm a, z"                         0:08 PM, PDT
 * "yyyyy.MMMMM.dd GGG hh:mm aaa"      02001.July.04 AD 12:08 PM
 * "EEE, d MMM yyyy HH:mm:ss Z"        Wed, 4 Jul 2001 12:08:56 -0700
 * "yyMMddHHmmssZ"                     010704120856-0700
 * "yyyy-MM-dd'T'HH:mm:ss.SSSZ"        2001-07-04T12:08:56.235-0700
 * "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"      2001-07-04T12:08:56.235-07:00
 * "YYYY-'W'ww-u"                      2001-W27-3
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
public class DateParser {
    
    
    /**
     * Parses a date in a given format.
     *
     * @author Heiko Muller
     * @param date
     * @param format
     * @return Date
     * @since 1.0
     */
    public static Date parseDateItaly(String date, String format){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        df.setLenient(false); 
        try{
            return df.parse("01/01/1970");
        }
        catch(ParseException ex){ 
            ex.printStackTrace();  
            Logger.getLogger(UploadSampleRequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date(System.currentTimeMillis());
    }
    
     /**
     * Parses a date in a given format.
     *
     * @author Heiko Muller
     * @param date
     * @param format
     * @return Date
     * @since 1.0
     */
    public static Date parseDateAustria(String date, String format){
        if(date == null){
            date = new String("1.1.1970");
        }
        if(date.length() == 0){
            date = new String("1.1.1970");
        }
        
        DateFormat df = new SimpleDateFormat(format, Locale.GERMANY);
        df.setLenient(false); 
        try{
            return df.parse(date);
        }
        catch(ParseException ex){ 
            ex.printStackTrace();  
            Logger.getLogger(UploadSampleRequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date(System.currentTimeMillis());
    }
    
    /**
     * Returns a Date as a String in the desired format.
     *
     * @author Heiko Muller
     * @param date
     * @param format
     * @return String
     * @since 1.0
     */
    public static String parseDateToStringUK(Date date, String format){
        if(date == null){
            return "NULL";
        }
        DateFormat df = new SimpleDateFormat(format, Locale.UK);
        return df.format(date);       
    }
    
}
