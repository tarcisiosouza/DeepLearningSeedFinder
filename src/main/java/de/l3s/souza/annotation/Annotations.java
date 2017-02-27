package de.l3s.souza.annotation;


import java.util.List;
import java.util.Vector;

import edu.stanford.nlp.util.Triple;

public class Annotations {
	
	private static StanfordNLPUtils nlp;
	private static LanguageProcessor lp;
	private static Vector<String> lang;
	private static List<Triple<String, Integer, Integer>> annotations;
	private String annotationText;
	private String annotationType;
	
	public void initialize() throws Exception
	{
		nlp = new StanfordNLPUtils ();
		lp = new LanguageProcessor ();
		annotationText = null;
		annotationType = null;
	}
	
	public void setLanguage (String input)
	{
		lang = lp.detectLanguage(input);
	}
	

	public Vector<String> getLanguage ()
	{
		return lang;
	}
	
	public String getLanguage (String input)
	{
		Vector<String> str = lp.detectLanguage(input);
		return str.elementAt(0);
	}
	
	public static Vector<String> getLang() {
		return lang;
	}

	public static void setLang(Vector<String> lang) {
		Annotations.lang = lang;
	}

	public static List<Triple<String, Integer, Integer>> getAnnotations() {
		return annotations;
	}

	public static void setAnnotations(
			List<Triple<String, Integer, Integer>> annotations) {
		Annotations.annotations = annotations;
	}

	public String getAnnotationText() {
		return annotationText;
	}

	public void setAnnotationText(String annotationText) {
		this.annotationText = annotationText;
	}

	public String getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}

	public void annotate (String input)
	{
		String firstLetterUpCaseStr = CapsFirst (input);
		setLanguage(firstLetterUpCaseStr);
		
		for (int i=0;i<3;i++)
		{
			if (lang.elementAt(i).contentEquals("de") || lang.elementAt(i).contentEquals("en"))
			{
				try {
					if (lang.elementAt(i).contentEquals("de"))
						annotations = nlp.annotateEntities(firstLetterUpCaseStr, Language.DE);
					else
						annotations = nlp.annotateEntities(firstLetterUpCaseStr, Language.EN);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				annotationText = null;
				for (Triple<String, Integer, Integer> el : annotations) {
					
					annotationType = el.first;
					annotationText = firstLetterUpCaseStr.substring(el.second, el.third).toLowerCase();

				}
				
				break;	
			}
		
		}
		if (!lang.contains("de")&&!lang.contains("en"))
		{
			try {
				annotations = nlp.annotateEntities(firstLetterUpCaseStr, Language.EN);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (Triple<String, Integer, Integer> el : annotations) {
				
				annotationType = el.first;
				annotationText = firstLetterUpCaseStr.substring(el.second, el.third).toLowerCase();

			}
		}
	}
	
	String CapsFirst(String str) {
	    String[] words = str.split(" ");
	    StringBuilder ret = new StringBuilder();
	    for(int i = 0; i < words.length; i++) {
	    	try {
	        ret.append(Character.toUpperCase(words[i].charAt(0)));
	        ret.append(words[i].substring(1));
	        if(i < words.length - 1) {
	            ret.append(' ');
	        }
	    	} catch (Exception e)
	    	{
	    		continue;
	    	}
	    }
	    return ret.toString();
	}
}

