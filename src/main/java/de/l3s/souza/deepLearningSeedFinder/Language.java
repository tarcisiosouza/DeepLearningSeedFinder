package de.l3s.souza.deepLearningSeedFinder;

public enum Language {
	EN("en"), DE("de"), ES("es"), RU("ru"), CS("cs"), FR("fr"), IT("it"), PL("pl"), PT("pt"), NL("nl"), SE("se"), DA("da"), HU("hu"), TR("tr");

	private String lang;

	// Constructor
	Language(String l) {
		lang = l;
	}

	// Overloaded constructor
	Language() {
		lang = null;
	}

	public String getLanguage() {
		return lang;
	}

	public String toString() {
		return lang;
	}

	public static Language getLanguage(String langStr) {
		for (Language lang : Language.values()) {
			if (langStr.equals(lang.getLanguage())) {
				return lang;
			}
		}
		return null;

	}

	/*
	 * //initialize enum from String public class LanguageFactory{ private
	 * Language language; public LanguageFactory(String langStr){
	 * 
	 * for (Language lang : Language.values()) {
	 * if(langStr.equals(lang.getLanguage())){ language=lang; } }
	 * 
	 * }
	 * 
	 * public Language getLanguage (){ return language; }
	 * 
	 * 
	 * }
	 */
}


