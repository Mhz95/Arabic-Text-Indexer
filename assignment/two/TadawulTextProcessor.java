package assignment.two;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import assignment.obj.Doc;
import assignment.obj.Term;
import assignment.obj.Token;
import assignment.util.SnowballStemmer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Assignment 2: 
 * Text Processing and Indexing 
 * Selected Topics in AI, KSU March 2021
 * @author Malzaidan
 */
public class TadawulTextProcessor {

	static Map<String, Map<Long, List<Term>>> index = new HashMap<String, Map<Long, List<Term>>>();

	public static void tadawulTextProcessor(String stockCode) {

		List<Doc> docs = readXML(stockCode);

		// docs.forEach(value -> System.out.println(value.toString()));

		for (int x = 0; x < docs.size(); x++) {
			ArrayList<Token> tokens = tokenize(docs.get(x));

			ArrayList<Token> tokens_after_stopping = stopping(tokens);

			ArrayList<Token> terms = stemming(tokens_after_stopping);

			System.out.println("DOCUMENT ID: " + docs.get(x).getDocID() + "\n");
			System.out.println((x + 1) + " / " + docs.size() + "\n");

			Map<String, List<Term>> termsPositions = getTermsPositions(terms);

			index(x, docs, termsPositions, terms);
		}

		List<Map.Entry<Integer, String>> out = formatForIndexStorage(stockCode);
		saveResults(out, stockCode, "index", -1);

	}

	private static Map<String, List<Term>> getTermsPositions(ArrayList<Token> terms) {
		// TODO Auto-generated method stub
		Map<String, List<Term>> countByWords = new HashMap<String, List<Term>>();
		for (int i = 0; i < terms.size(); i++) {
			List<Term> pos;
			pos = countByWords.get(terms.get(i).getToken_stem());
			if (pos != null) {
				pos.add(new Term(terms.get(i).getTokenID(), terms.get(i).getToken(), terms.get(i).getType()));
				countByWords.put(terms.get(i).getToken_stem(), pos);
			} else {
				pos = new ArrayList<Term>();
				pos.add(new Term(terms.get(i).getTokenID(), terms.get(i).getToken(), terms.get(i).getType()));
				countByWords.put(terms.get(i).getToken_stem(), pos);
			}
		}
		return countByWords;
	}

	public static List<Doc> readXML(String stockCode) {

		List<Doc> docs = new ArrayList<Doc>();

		try {
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File("./assignment/resources/" + stockCode + ".xml");
			Document document;
			document = (Document) builder.build(xmlFile);
			Element e = document.getRootElement();
			List<Element> allDocs = document.getRootElement().getChildren();

			for (Element element : allDocs) { // For each document in the XML file
				try {
					long unixTime = System.currentTimeMillis() / 1000;
					Random random = new Random();
					// generate a random integer from 0 to 899, then add 100
					int x = random.nextInt(900) + 100;
					unixTime = unixTime + x;
					
					Element date = element.getChild("DATE");
					Element time = element.getChild("TIME");
					Element title = element.getChild("TITLE");
					Element body = element.getChild("TEXT");

					String dateStr = "";
					String timeStr = "";
					dateStr = date.getValue();
					timeStr = time.getValue();
					if (dateStr != null && dateStr != "" && timeStr != null && timeStr != "") {
						SimpleDateFormat original = new SimpleDateFormat("dd/mm/yyyy");
						SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-mm-dd");
						Timestamp timestamp = null;
						if (!dateStr.isEmpty()) {
							Date dd = original.parse(dateStr);
							String reformattedStr = newFormat.format(dd);
							// Get timestamp to use it as document ID
							timestamp = Timestamp.valueOf(reformattedStr + " " + timeStr);

						}
						if (timestamp != null) {
							unixTime = (long) timestamp.getTime() / 1000;
						}
					} else {
						unixTime = System.currentTimeMillis() / 1000;
						unixTime = unixTime + x;
					}

					// Create Document Obj and add it to the list
					Doc doc = new Doc(stockCode, unixTime, title.getValue(), body.getValue());

					docs.add(doc);
					System.out.println("dateStr " + unixTime);

				} catch (ParseException er) {
					er.printStackTrace();
				}

			}

			// docs.forEach(value -> System.out.println(value.toString()));

		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return docs;
	}

	/**
	 * To tokenize a given text
	 * 
	 * @params stockCode
	 * @return
	 */
	public static ArrayList<Token> tokenize(Doc input) {
		ArrayList<Token> tokens = new ArrayList<Token>();

		Token newElement;
		// Convert input string to arraylist for further manipulation
		ArrayList<String> candidates = new ArrayList<String>();
		ArrayList<String> candidates_content = new ArrayList<String>();

		// Split input string
		String cleaned_title = input.getTitle().replaceAll("[^\\p{L}\\p{Z}]", "");
		String cleaned_content = input.getContent().replaceAll("[^\\p{L}\\p{Z}]", "");

		String[] tokenCan_title = cleaned_title.split("\\s+");
		String[] tokenCan_content = cleaned_content.split("\\s+");

		// Clean empty strings in arraylist
		candidates = cleanEmptyString(tokenCan_title);
		candidates_content = cleanEmptyString(tokenCan_content);
		int i = 0;
		// Final candiates array
		for (String t : candidates) {
			// Parse string value
			newElement = new Token(i, t, 't');
			tokens.add(newElement);
			i++;
		}
		for (String t : candidates_content) {
			// Parse string value
			newElement = new Token(i, t, 'c');
			tokens.add(newElement);
			i++;
		}

		return tokens;
	}

	/**
	 * To Clean empty string in Arraylist
	 * 
	 * @params dirtyString
	 * @return
	 */
	public static ArrayList<String> cleanEmptyString(String[] dirtyString) {
		ArrayList<String> nonEmpStrings = new ArrayList<String>();

		// Remove empty string from the candidate string array
		for (String s : dirtyString) {
			// Replace multiple spaces by empty string
			s.replaceAll("[\\t\\n\\r\\s]", "");
			// add nonempty string to new arraylist
			if (!s.matches(" ") && !s.matches(""))
				nonEmpStrings.add(s);
		}

		return nonEmpStrings;
	}

	/**
	 * To remove stopping words in a text
	 * 
	 * @params stockCode
	 * @return
	 */
	public static ArrayList<Token> stopping(ArrayList<Token> tokens) {
		String filename_ar = "./assignment/resources/stopword_ar.txt";
		String filename_en = "./assignment/resources/stopword_en.txt";
		try (Stream<String> lines = Files.lines(Paths.get(filename_ar))) {
			lines.forEachOrdered(line -> {
				for (int i = 0; i < tokens.size(); i++) {
					if (tokens.get(i).getToken().equals(line)) {
						tokens.remove(i);
					}
				}
			});
		} catch (IOException e) {
			System.out.println("unable to find file: " + filename_ar);
			System.exit(0);
		}
		try (Stream<String> lines = Files.lines(Paths.get(filename_en))) {
			lines.forEachOrdered(line -> {
				for (int i = 0; i < tokens.size(); i++) {
					if (tokens.get(i).getToken().equals(line)) {
						tokens.remove(i);
					}
				}
			});
		} catch (IOException e) {
			System.out.println("unable to find file: " + filename_en);
			System.exit(0);
		}
		return tokens;
	}

	/**
	 * To apply words stemming in a text
	 * 
	 * @params stockCode
	 * @return
	 */
	public static ArrayList<Token> stemming(ArrayList<Token> tokens) {
		Class stemClass_ar, stemClass_en;
		try {
			stemClass_ar = Class.forName("assignment.util.arabicStemmer");
			stemClass_en = Class.forName("assignment.util.englishStemmer");
			SnowballStemmer stemmer, stemmer_en;
			try {
				stemmer = (SnowballStemmer) stemClass_ar.newInstance();
				stemmer_en = (SnowballStemmer) stemClass_en.newInstance();
				tokens.forEach(value -> {
					if ((value.getToken()).matches("^[a-zA-Z][a-zA-Z\\s]+$")) {
						stemmer_en.setCurrent(value.getToken());
						stemmer_en.stem();
						if (stemmer_en.getCurrent() != null && stemmer_en.getCurrent() != "") {
							value.setToken_stem(stemmer_en.getCurrent());
						} else {
							value.setToken_stem(value.getToken());
						}
					} else {
						stemmer.setCurrent(value.getToken());
						stemmer.stem();
						if (stemmer.getCurrent() != null && stemmer.getCurrent() != "") {
							value.setToken_stem(stemmer.getCurrent());
						} else {
							value.setToken_stem(value.getToken());
						}
					}
				});

			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tokens;
	}

	/**
	 * To index a text
	 * 
	 * @params stockCode
	 * @return
	 */
	public static void index(int x, List<Doc> docs, Map<String, List<Term>> termsPos, ArrayList<Token> terms) {

		for (int i = 0; i < terms.size(); i++) {
			Map<Long, List<Term>> docTermPos = null;
			List<Term> pos;
			pos = termsPos.get(terms.get(i).getToken_stem());
			docTermPos = index.get(terms.get(i).getToken_stem());
			if (docTermPos != null) {
				docTermPos.put(docs.get(x).getDocID(), pos);
				index.put(terms.get(i).getToken_stem(), docTermPos);
			} else {
				docTermPos = new HashMap<Long, List<Term>>();
				docTermPos.put(docs.get(x).getDocID(), pos);
				index.put(terms.get(i).getToken_stem(), docTermPos);
			}
		}
	}

	/**
	 * To find a term in all documents
	 * 
	 * @params stockCode
	 * @return
	 */
	public static Map<Long, List<Term>> findSpecificWord(String query, Map<String, Map<Long, List<Term>>> index) {
		Map<Long, List<Term>> result = index.get(query);
		return result;
	}

	/**
	 * To set the format for output
	 * 
	 * @params stockCode
	 * @return
	 */
	public static List<Entry<Integer, String>> formatForOutput(String stockCode, Map<String, Map<Long, List<Term>>> index) {
		String topith = "";
		String all = "";
		Map<Integer, String> ctfs = new HashMap<Integer, String>();

		for (Entry<String, Map<Long, List<Term>>> entry : index.entrySet()) {
			String key = entry.getKey();

			Map<Long, List<Term>> value = entry.getValue();
			int df = value.size();
			int ctf = 0;
			for (Entry<Long, List<Term>> cont : value.entrySet()) {
				List<Term> val = cont.getValue();
				ctf = ctf + val.size();
			}
			// term--> df, ctf (d1, tf), (d2, tf), (d3, tf)
			topith = "\n" + key + "--> " + df + ", " + ctf + " ";

			for (Entry<Long, List<Term>> cont : value.entrySet()) {
				Long dID = cont.getKey();
				topith += "(" + dID;

				List<Term> val = cont.getValue();
				int tf = val.size();
				topith += ", " + tf + "), ";

			}
			ctfs.put(ctf, topith);
			all += topith + "\n";
			topith = "";
		}
		List<Map.Entry<Integer, String>> result = ctfs.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toList());

		return result;
	}

	private static List<Entry<Integer, String>> formatForIndexStorage(String stockCode) {
		String topith = "";
		Map<Integer, String> ctfs = new HashMap<Integer, String>();

		for (Entry<String, Map<Long, List<Term>>> entry : index.entrySet()) {
			String key = entry.getKey();

			Map<Long, List<Term>> value = entry.getValue();
			int ctf = 0;
			for (Entry<Long, List<Term>> cont : value.entrySet()) {
				List<Term> val = cont.getValue();
				ctf = ctf + val.size();
			}
			topith = "\n{\"" + key + "\" : [";

			for (Entry<Long, List<Term>> cont : value.entrySet()) {
				Long dID = cont.getKey();
				topith += "\n\t{\"" + dID +"\" : \n\t\t\t[";

				List<Term> val = cont.getValue();
				for(int i = 0 ; i < val.size(); i++) {
					topith += "\n\t\t\t\t{\"id\" : "+ val.get(i).getTermID() +",";
					topith += "\n\t\t\t\t\"term\" : \""+ val.get(i).getTerm() +"\",";
					topith += "\n\t\t\t\t\"type\" : \""+ (val.get(i).getType() == 't'? "Title": "Body") +"\"},";
				}
				topith += "\n\t\t\t]\n\t\t}, ";

			}
			topith += "\n\t]\n}, ";
			ctfs.put(ctf, topith);
			topith = "";
		}
		List<Map.Entry<Integer, String>> result = ctfs.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toList());

		return result;
	}
	
	public static void saveResults(List<Entry<Integer, String>> out, String stockCode, String filenamePrefix, int limit) {
		String rs = "";
		
		try {
			FileWriter fw = new FileWriter(new File("./assignment/"+filenamePrefix+"_" + stockCode + ".json"));
			StringBuffer sb = new StringBuffer("");
			int size = 0;
			if(limit != -1) {
				size = limit;
			} else {
				size = out.size();
			}
			for (int i = 1; i < size; i++) {
					rs = out.get(out.size() - i).getValue();
					sb.append(rs + "\n");
			}
			
			if(filenamePrefix.equals("index")) {
				fw.write("["+sb.toString()+"]");
			} else {
				fw.write(sb.toString());
			}

			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public static Map<String, Map<Long, List<Term>>> parseJSONIndex(String stock) {
		String filename = "./assignment/index_"+stock+".json";
		Map<String, Map<Long, List<Term>>> index = new HashMap<String, Map<Long, List<Term>>> ();
		
		JSONParser parser = new JSONParser();

		    try {

		        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filename));
		        for(Object ja : jsonArray) {
		        	if(ja instanceof JSONObject) {
				        JSONObject jsonobj = (JSONObject) ja;
				        Set entries = jsonobj.entrySet();
				        for(Object one : entries) {
					           Map.Entry entry = (Map.Entry) one;
					           Object key = entry.getKey();  
					           Object value = entry.getValue();  
						        Map<Long, List<Term>> map = new HashMap<Long, List<Term>>();

					           if(value instanceof JSONArray) {
					        	   JSONArray jArray = (JSONArray) value;
					        	   for(Object j : jArray) {
					        			   JSONObject jb = (JSONObject) j;
									        Set ento = jb.entrySet();
									        for(Object h : ento) {
									        	   Map.Entry en = (Map.Entry) h;
										           Object keyy = en.getKey(); 
								        		   //System.out.println(keyy.toString());
								        		   List<Term> list = new ArrayList<Term>();
										           Object valuee = en.getValue();
								        		   JSONArray jray = (JSONArray) valuee;
								        		   for(Object jj : jray) {
								        			   JSONObject jbb = (JSONObject) jj;
								        			   Set entoo = jbb.entrySet();
											           Term term = new Term(0, "", 'a');
								        			   for(Object hh : entoo) {
								    		        	   Map.Entry enn = (Map.Entry) hh;
												           Object keyyy = enn.getKey();  
												           Object valueee = enn.getValue(); 
												           if(keyyy.toString().equals("term")) {
												        	   term.setTerm(valueee.toString());
												           } else if(keyyy.toString().equals("id")) {
												        	   term.setTermID(((Long)valueee).intValue());
												           } else if(keyyy.toString().equals("type")) {
												        	   if(valueee.toString().equals("Title")) {
												        		   term.setType('t');
												        	   }else {
												        		   term.setType('c');
												        	   }
												           }
								        			   }
								        			   list.add(term);
								        			   //System.out.println(term.toString() + "_");
								        			   
								        		   }
								        		   long s = Long.valueOf(keyy.toString());
								        		   map.put(s, list);
									        }
					        	   }
					           }
						        index.put(key.toString(), map);
				        }
			
		        	}
		        }
		    } catch (org.json.simple.parser.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		     
		return index;
	}


}
