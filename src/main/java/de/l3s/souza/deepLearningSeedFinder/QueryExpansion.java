package de.l3s.souza.deepLearningSeedFinder;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Vector;

import de.l3s.elasticquery.Article;

public class QueryExpansion {
	
	private HashMap<String,Double> queryCandidatesScores;
	private HashMap<String,Double> usedTerms;
	private HashMap<String,Double> querySimilarTerms;
	private HashMap<String,Article> articlesWithoutDup;
	private double beta;
	private double alpha;
	private int expandTerms;
	private static boolean ASC = true;
    private static boolean DESC = false;
	private int totalSimilar;
	HashMap<String,Article> articles;
	private String currentQuery;
	private HashSet<String> nextQuery;
	
	public HashSet<String> getNextQuery() {
		return nextQuery;
	}

	public QueryExpansion(String cQuery,HashMap<String,Article> articlesWitDup,HashMap<String,Article> art,
			int totalSimilar,int expandedTerms, double alpha,double beta) {
		
		currentQuery = cQuery;
		nextQuery = new HashSet<String>();
		queryCandidatesScores = new HashMap<String,Double>();
		usedTerms = new HashMap<String,Double>();
		querySimilarTerms = new HashMap<String,Double>();
		articlesWithoutDup = new HashMap<String,Article>(articlesWitDup);
		articles = new HashMap<String,Article>(art);
		this.alpha = alpha;
		this.beta = beta;
		this.totalSimilar = totalSimilar;
		expandTerms = expandedTerms;
	}

	
	public HashMap<String, Article> getArticlesWithoutDup() {
		return articlesWithoutDup;
	}

	public void setArticlesWithoutDup(HashMap<String, Article> newArticleSet) {
		articlesWithoutDup.clear();
		articlesWithoutDup = new HashMap<String,Article>(newArticleSet);
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int getTotalSimilar() {
		return totalSimilar;
	}

	public void setTotalSimilar(int totalSimilar) {
		this.totalSimilar = totalSimilar;
	}

	public String getCurrentQuery() {
		return currentQuery;
	}

	public void setCurrentQuery(String currentQuery) {
		this.currentQuery = currentQuery;
	}

	public void resetQueryExpansionTerms ()
	{
		querySimilarTerms.clear();
		queryCandidatesScores.clear();
		nextQuery.clear();
	}
	
	public void extractSimilarTermsQuery (deepLearningUtils deepLearning, EntityUtils annotations, HashMap<String,Double> entities)
	{
		String currentTerm ;
		StringTokenizer token = new StringTokenizer (currentQuery);
		resetQueryExpansionTerms();
		querySimilarTerms = new HashMap<String,Double> ();
		
		while (token.hasMoreTokens())
		{
			currentTerm = token.nextToken();
			Collection<String> nearest = deepLearning.getWordsNearest(currentTerm, totalSimilar);
			
			  for (Iterator iterator = nearest.iterator(); iterator.hasNext();) 
			  {
			        String element = (String) iterator.next();
			        querySimilarTerms.put(element, 1.0);

			  }
			
		}
		
		for (Entry<String, Double> s : querySimilarTerms.entrySet())	
		{
			token = new StringTokenizer (currentQuery);
		
			currentTerm = s.getKey();
			double sim = 0;
			
			while (token.hasMoreTokens())
			{
				sim = sim + deepLearning.getCosSimilarity(token.nextToken(), currentTerm);
				if (sim > 1)
				{
					sim = 1;
				}
			}
			querySimilarTerms.put(currentTerm,sim);
		}
		
		calculateScores(annotations,deepLearning);
		querySimilarTerms = normalizeScores (querySimilarTerms);
	}
	
	private void calculateScores (EntityUtils annotations, deepLearningUtils deepLearning) {
		
		 int sumTermFrequency;
		
		  if (!querySimilarTerms.isEmpty())
		  {
			for (Entry<String, Double> s : querySimilarTerms.entrySet())
			{
				sumTermFrequency = 0;
				String currentTerm = s.getKey();
				double score = 0.0f;
				
				for(Entry<String, Article> s2 : articlesWithoutDup.entrySet())
				{
					int termFrequency = getTermFrequency(s2.getValue().getText(),currentTerm);
					sumTermFrequency = sumTermFrequency + termFrequency;
				}
				
					score = (sumTermFrequency*alpha) + (1-alpha)*s.getValue();
					
					querySimilarTerms.put(currentTerm, score);
					queryCandidatesScores.put(currentTerm, score);
			}
		  }
		  calculateScoresQueryTerms(totalSimilar,annotations,deepLearning);
	}
	
	
	//softmax normalization
	private HashMap<String,Double> normalizeScores (HashMap<String,Double> origin)
	{
		HashMap<String,Double> result = new HashMap<String,Double>();
		double sumExp=0;
		for (Entry<String,Double>  s : origin.entrySet() )
			sumExp = sumExp + Math.exp(s.getValue());
		
		for (Entry<String,Double>  s : origin.entrySet() )
		{
			double normalizedScore = Math.exp(s.getValue())/sumExp;
			result.put(s.getKey(), normalizedScore);
		}
		
		return result;
	}
	
	public HashMap<String,Double> getNewScoreEntities (deepLearningUtils deepLearning,HashMap<String,Double> entities)
	{
		HashMap<String,Double> entitiesOutput = new HashMap<String,Double>();
		
		for (Entry<String, Double> s : entities.entrySet())
		{
			if (queryCandidatesScores.containsKey(s.getKey()))
				continue;
				
				StringTokenizer token = new StringTokenizer (currentQuery);
			
				String currentTerm = s.getKey();
				double sum = 0;
				while (token.hasMoreTokens())
				{
					double sim;
					sim = deepLearning.getCosSimilarity(token.nextToken(), currentTerm);
					sum = sum + sim;
				}
				
				
				sum = sum/currentQuery.length();
				entitiesOutput.put(currentTerm, sum);

		}
		return entitiesOutput;
		
	}
	public int getTermFrequency (String document, String term)
	{
		int frequency = 0;
		String current = " ";
		StringTokenizer token = new StringTokenizer (document);
		
		while (token.hasMoreTokens())
		{
			current = token.nextToken().toLowerCase();
			if (current.contentEquals(term))
				frequency++;
		}
		return frequency;
		
	}
	
	private void calculateScoresQueryTerms (int totalSimilar, EntityUtils annotations,deepLearningUtils deepLearning)
	{
			HashSet<String> domains = new HashSet<String>();
			HashSet<String> years = new HashSet<String>();
			HashSet<String> languages = new HashSet<String>();
			double sim;
			double currentScore;
			String element = null;
			
			for(Entry<String, Article> s : articles.entrySet())
			{
				String domain = s.getValue().getDomain();
				domains.add(domain);
			//	annotations.setLanguage(s.getValue().getText());
			//	Vector<String> language = annotations.getLanguage();
			//	languages.add(language.firstElement());
				years.add(s.getValue().getTimestamp().substring(0, 3));
			}
			
			StringTokenizer token = new StringTokenizer (currentQuery);
			
			while (token.hasMoreTokens())
			{
				String currentQueryTerm = token.nextToken();
				
				Collection<String> similarTerms = deepLearning.getWordsNearest(currentQueryTerm, totalSimilar);
				
				 for (Iterator iterator = similarTerms.iterator(); iterator.hasNext();) 
				 {
				        element = (String) iterator.next();
				        break;
				 }
				 
			 if (element!=null)	 
				sim = deepLearning.getCosSimilarity(currentQueryTerm, element);
			 else
				sim = 0;
			 
			 currentScore = beta *(domains.size() + languages.size() + years.size()+ sim);
			
			 usedTerms.put(currentQueryTerm, currentScore);
			 queryCandidatesScores.put(currentQueryTerm, currentScore);
			}
			
			Map<String, Double> ordered = sortByComparator (queryCandidatesScores,DESC);
			int terms = 0;
			for (Entry<String, Double> s : ordered.entrySet())
			{
				terms ++;
				if (terms <= expandTerms)
					nextQuery.add(s.getKey());
			}
			
	}
	
	private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap, final boolean order)
	{

	            List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

	            // Sorting the list based on values
	            Collections.sort(list, new Comparator<Entry<String, Double>>()
	            {
	                public int compare(Entry<String, Double> o1,
	                        Entry<String, Double> o2)
	                {
	                    if (order)
	                    {
	                        return o1.getValue().compareTo(o2.getValue());
	                    }
	                    else
	                    {
	                        return o2.getValue().compareTo(o1.getValue());

	                    }
	                }
	            });

	            // Maintaining insertion order with the help of LinkedList
	            Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
	            for (Entry<String, Double> entry : list)
	            {
	                sortedMap.put(entry.getKey(), entry.getValue());
	            }

	            return sortedMap;
	}
	
}
