package assignment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import assignment.obj.Term;
import assignment.two.TadawulTextProcessor;

/**
 * @author Malzaidan 
 * March, 2021  
 */
public class MainApp {

	public static void main(String[] args) {

		String[] stockCode = { "1820", "4050", "3010" };
		/** TadawulCrawler tc_1820 = new TadawulCrawler(stockCode[0]); **/

		/**
		 * Create Index and save it as JSON file in the hardesk ONLY run once or
		 * whenever index update is needed to store the index files
		 * 
		 * We already ran this, so they are available now as file, 
		 * however you may uncomment this to overrite them :
  		 **/

//		 TadawulTextProcessor.tadawulTextProcessor(stockCode[0]);
//		 TadawulTextProcessor.tadawulTextProcessor(stockCode[1]);
//		 TadawulTextProcessor.tadawulTextProcessor(stockCode[2]);

		/**
		 * Stock of intrest
		 **/
		String stock = stockCode[2];

		/**
		 * Read and parse index
		 **/
		Map<String, Map<Long, List<Term>>> index = (HashMap<String, Map<Long, List<Term>>>) TadawulTextProcessor
				.parseJSONIndex(stock);

		/**
		 * Use index extracted from index file to find top 10 words and to save the
		 * sorted count of all words
		 **/
		List<Map.Entry<Integer, String>> result = TadawulTextProcessor.formatForOutput(stock, index);
		TadawulTextProcessor.saveResults(result, stock, "top10", 10);
		System.out.println("\n-----| top10 frequent words in "+stock+" are saved into a file |-----");
		TadawulTextProcessor.saveResults(result, stock, "allcounts", -1);

		/**
		 * Word of intrest
		 **/
		//String word = "وفق";
		String word = "طرق";
		//String word = "احتراز";

		/**
		 * Use index extracted from index file to search for a word
		 **/
		lookup(word, index, stock);

	}

	/**
	 * Simple Search
	 **/
	private static void lookup(String word, Map<String, Map<Long, List<Term>>> index, String stock) {
		Map<Long, List<Term>> res = null;
		res = TadawulTextProcessor.findSpecificWord(word, index);
		System.out.println("\n\n--------------| Search |---------------");
		System.out.println("The results of searching for " + word + " in Stock " + stock + " are: \n");
		List<Map<Long, List<Term>>> search_res = Arrays.asList(res);
		if (search_res.get(0) != null) {
			System.out.println(Arrays.asList(res));
		} else {
			System.out.println("The word is not found, kindly try again!");
		}
	}

}
