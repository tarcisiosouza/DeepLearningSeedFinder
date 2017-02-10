package de.l3s.souza.deepLearningSeedFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Triple;

public class StanfordNLPUtils {

		private Map<Language, AbstractSequenceClassifier<CoreLabel>> classifiers;

		/**
		 * 
		 */
		public StanfordNLPUtils() {
			String serializedClassifier = "english.all.3class.distsim.crf.ser.gz";
			AbstractSequenceClassifier<CoreLabel> engLishClassifier = CRFClassifier
					.getClassifierNoExceptions(serializedClassifier);
			AbstractSequenceClassifier<CoreLabel> germanClassifier = CRFClassifier
					.getClassifierNoExceptions("dewac_175m_600.crf.ser.gz");

			classifiers = new HashMap<Language, AbstractSequenceClassifier<CoreLabel>>();

			classifiers.put(Language.EN, engLishClassifier);
			classifiers.put(Language.DE, germanClassifier);

		}

		/**
		 * Configure with classifier to use for German language
		 * 
		 * @param nr
		 *            0 for first classifier, 1 for second one
		 */
		public void changeGermanClassifier(int nr) {
			AbstractSequenceClassifier<CoreLabel> germanClassifier;
			if (nr == 0)
				germanClassifier = CRFClassifier.getClassifierNoExceptions("hgc_175m_600.crf.ser.gz ");
			else
				germanClassifier = CRFClassifier.getClassifierNoExceptions("dewac_175m_600.crf.ser.gz");
			classifiers.put(Language.DE, germanClassifier);
		}

		public List<CoreMap> splitSentences(String text, Language lang) {

			Properties props = new Properties();
			// props.setProperty("annotators",
			// "tokenize, ssplit, pos, lemma, parse, sentiment");
			props.setProperty("annotators", "tokenize, ssplit");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

			Annotation annotation = pipeline.process(text);
			List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

			return sentences;
		}

		public String[] splitSentencesToStrings(String text, Language lang) {

			List<CoreMap> sentences = splitSentences(text, lang);

			List<String> strs = new ArrayList<String>();

			for (CoreMap sentence : sentences) {
				strs.add(sentence.toString());
			}

			return strs.toArray(new String[strs.size()]);
		}

		public List<Triple<String, Integer, Integer>> annotateEntities(String text, Language lang) throws Exception {

			AbstractSequenceClassifier<CoreLabel> classifier = classifiers.get(lang);
			if (classifier == null) {
				throw new Exception("StanfordNLPUtils. Language is unknown: " + lang.toString());
			}
			List<Triple<String, Integer, Integer>> annotations = classifier.classifyToCharacterOffsets(text);
			// classifier.

			return annotations;

		}

		public Map<Integer, CorefChain> coreferenceEntities(String text, Language lang) throws Exception {
			Properties props = new Properties();
			props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

			// create an empty Annotation just with the given text
			Annotation document = new Annotation(text);

			// run all Annotators on this text
			pipeline.annotate(document);
			// CollapsedCCProcessedDependenciesAnnotation s;
			// these are all the sentences in this document
			// a CoreMap is essentially a Map that uses class objects as keys and
			// has values with custom types
			/*
			 * List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			 * 
			 * for(CoreMap sentence: sentences) { // traversing the words in the
			 * current sentence // a CoreLabel is a CoreMap with additional
			 * token-specific methods for (CoreLabel token:
			 * sentence.get(TokensAnnotation.class)) { // this is the text of the
			 * token String word = token.get(TextAnnotation.class); // this is the
			 * POS tag of the token String pos =
			 * token.get(PartOfSpeechAnnotation.class); // this is the NER label of
			 * the token String ne = token.get(NamedEntityTagAnnotation.class); }
			 * 
			 * // this is the parse tree of the current sentence Tree tree =
			 * sentence.get(TreeAnnotation.class);
			 * 
			 * // this is the Stanford dependency graph of the current sentence
			 * SemanticGraph dependencies =
			 * sentence.get(CollapsedCCProcessedDependenciesAnnotation.class); }
			 */

			// This is the coreference link graph
			// Each chain stores a set of mentions that link to each other,
			// along with a method for getting the most representative mention
			// Both sentence and token offsets start at 1!

			Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);

			return graph;
		}

		/**
		 * @param args
		 * @throws Exception
		 */
	}

