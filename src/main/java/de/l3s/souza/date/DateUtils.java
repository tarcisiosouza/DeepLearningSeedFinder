package de.l3s.souza.date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.google.protobuf.TextFormat.ParseException;

public class DateUtils {

	public Date convertToDate(String input) throws java.text.ParseException, ParseException {
        Date date = null;
        String format;
        
        if(null == input) {
            return null;
        }
        
          ArrayList<SimpleDateFormat>  dateFormats = new ArrayList<SimpleDateFormat>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
            add(new SimpleDateFormat("M/dd/yyyy"));
            add(new SimpleDateFormat("dd/MM/yyyy"));
            add(new SimpleDateFormat("dd.M.yyyy"));
            add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.MMM.yyyy"));
            add(new SimpleDateFormat("dd-MMM-yyyy"));
            add(new SimpleDateFormat("dd-MM-yyyy"));
            add(new SimpleDateFormat("yyyyMMdd"));
//            add(new SimpleDateFormat("yyyy-MM"));
            add(new SimpleDateFormat("yyyy-MM-dd"));
            add(new SimpleDateFormat("dd.MM.yyyy"));
            add(new SimpleDateFormat("yyyy/MM/dd"));
            add(new SimpleDateFormat("yyyy.MM.dd"));
          }
          };
        for (SimpleDateFormat form : dateFormats) {
            form.setLenient(false);
            try {
            	date = form.parse(input);	
            	format = form.toPattern().toString();
            } catch (Exception e)
            {
            	continue;
            }
			
        }
        if (date == null)
        	return date;
        
        return date;
    }
}
