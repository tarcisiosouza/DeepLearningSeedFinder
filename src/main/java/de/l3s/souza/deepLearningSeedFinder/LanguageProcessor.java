package de.l3s.souza.deepLearningSeedFinder;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Vector;

import de.spieleck.app.cngram.NGramProfiles;

/**
 * This class offers methods to determine language of the strings and perform
 * translation using ngramj http://ngramj.sourceforge.net/index.html and
 * microsoft translator api http://api.microsofttranslator.com
 * 
 * @author Elena Demidova
 * @date Jan 2012
 */
public class LanguageProcessor {
	private int requestCount=0;

	/**
	 * 
	 */
	public int getRequestCount() {
		return requestCount;
	}

	private NGramProfiles nps;

	public LanguageProcessor() throws Exception {
		nps = new NGramProfiles();

	}

	/**
	 * Translate given name into targetLang
	 */
	public String translate(String name, String targetLang) throws Exception {
		String translated = "";
		Vector<String> sourceLang = detectLanguage(name);

		String sourceLangStr=sourceLang.elementAt(0);
		
		//language ranking is not always accurate
		//push en, de and gr values if they appear in top-3.
		if (sourceLang.contains(targetLang)) {
			// probably no translation is required, targetLang is within top-3
			translated = name;
			return translated;
		}else if(sourceLang.contains("el")){
			//greek
			sourceLangStr="el";
		}else if(sourceLang.contains("de")){
			//german
			sourceLangStr="de";
		}
		else{
			sourceLangStr=null;
		}
		
		requestCount++;
		String tobetranslated = URLEncoder.encode("" + name, "UTF-8")
				.toLowerCase();

		// translation to targetLang using microsoft translator
		//from argument can be missing, but we do detect the language first to avoid unnecessary translation calls
		
		String urltranslator = "http://api.microsofttranslator.com/v2/Ajax.svc/Translate?appId=143AC7860B77C129FCE40593FDACC5402DE3D1A7&text="
				+ tobetranslated;
		if(sourceLangStr!=null){
				
			urltranslator += "&from="
				+ sourceLangStr;
		}
		urltranslator += "&to="
				+ targetLang;
		URL url = new URL(urltranslator);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);

		BufferedReader in = new BufferedReader(new InputStreamReader(connection
				.getInputStream(), "UTF-8"));

		String translatedString;
		while ((translatedString = in.readLine()) != null) {
			translated += translatedString;
		}
		in.close();
		if(translated.toLowerCase().contains("exception")){
		//	System.out.println(translated);
			return name;
		}
		return translated.trim();
	}

	/**
	 * Detect language of the "todetect"
	 * 
	 * @return top-3 languages
	 */
	Vector<String> detectLanguage(String todetect) {
		Vector<String> resultV = new Vector<String>();

		NGramProfiles.Ranker ranker = nps.getRanker();
		ranker.account(todetect);
		NGramProfiles.RankResult res = ranker.getRankResult();
		resultV.add(res.getName(0));
		resultV.add(res.getName(1));
		resultV.add(res.getName(2));

		return resultV;
	}

}

