package assignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import assignment.obj.Term;
import assignment.two.TadawulTextProcessor;

/**
 * @author Malzaidan 
 * April, 2021  
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

		 TadawulTextProcessor.tadawulTextProcessor(stockCode[2]);

		/**
		 * Stock of intrest
		 **/
		String stock = stockCode[2];

		/**
		 * Read and parse index
		 **/
		Map<String, Map<Long, List<Term>>> index = (HashMap<String, Map<Long, List<Term>>>) TadawulTextProcessor.parseJSONIndex(stock);
		/**
		 * Features Extraction for Stock Code = 3010
		 * Using index to construct features matrix (Documents x Words)
		 * **/
		long[] doc_ids = {1609648876,
				1608098558,
				1607922012,
				1607403607,
				1606107614,
				1604988350,
				1603803385,
				1599999829,
				1595767079,
				1595766794,
				1592802018,
				1589087883,
				1587361103,
				1586861246,
				1586861227,
				1586063123,
				1585140273,
				1583125233,
				1583125212,
				1576499157,
				1572179576,
				1569327901,
				1564898311,
				1564898018,
				1556108548,
				1555390821,
				1554700644,
				1554699766,
				1554613688,
				1554268337,
				1552565616,
				1551589904,
				1551589845,
				1542629952,
				1541484149,
				1541480461,
				1533101500,
				1530188043,
				1524546016,
				1523250022,
				1522758014,
				1521634374,
				1521549072,
				1519301863,
				1519046241,
				1519043717,
				1514869271,
				1514809844,
				1514808795,
				1514092054,
				1512648517,
				1512369459,
				1512364605,
				1511845229,
				1509626240,
				1509538173,
				1508735559,
				1507702396,
				1507120022,
				1506341689,
				1505713625,
				1505131828,
				1502281671,
				1502280673,
				1502257099,
				1500552594,
				1492578965,
				1492578179,
				1491887058,
				1490272500,
				1488434069,
				1487569441,
				1487567737,
				1484569246,
				1479730232,
				1477485284,
				1476707330,
				1474779787,
				1474779777,
				1472537983,
				1472129507,
				1471352767,
				1470314099,
				1468844397,
				1468844228,
				1468326144,
				1462191052,
				1462080636,
				1461474014,
				1460981889,
				1460638099,
				1460441083,
				1460032489,
				1459747256,
				1459082807,
				1457959343,
				1456808464,
				1455514249,
				1455513573,
				1453121078,
				1451824726,
				1451451620,
				1445258650,
				1436448436,
				1436446349,
				1432126964,
				1431261691,
				1429534047,
				1428295406,
				1427867697,
				1427694127,
				1424869335,
				1424267262,
				1424004292,
				1421731032,
				1420116855,
				1420116252,
				1417497159,
				1416833713,
				1416806769,
				1416141896,
				1413467592,
				1410701252,
				1408861322,
				1405517863,
				1405516480,
				1398602795,
				1397744441,
				1396370007,
				1395752385,
				1395296084,
				1393851719,
				1393744010,
				1393163649,
				1393162331,
				1393159671,
				1390224851,
				1389591657,
				1389535614,
				1389535452,
				1389503455,
				1389161521,
				1387458271,
				1382885169,
				1382879619,
				1374070125,
				1373980622,
				1367758226,
				1366092471,
				1364882460,
				1364129152,
				1362921199,
				1361971534,
				1361109736,
				1361108744,
				1361107801,
				1359812003,
				1358662987,
				1358603574,
				1350480158,
				1349786533,
				1348295342,
				1342624615,
				1342593022,
				1341206925,
				1340716013,
				1340456922,
				1340433103,
				1338729194,
				1335706142,
				1334644610,
				1333344062,
				1332851855,
				1329917399,
				1329744165,
				1329743157,
				1329138982,
				1327131720,
				1326028179,
				1326027813,
				1326027786,
				1324877047,
				1324387027,
				1318773189,
				1318166434,
				1317820914,
				1312895136,
				1310907094,
				1309958714,
				1309958661,
				1308488664,
				1303047210,
				1301894853,
				1301288839,
				1298811532,
				1298810211,
				1295355123,
				1293368474,
				1293367967,
				1287327506,
				1287319679,
				1279430934,
				1277704800,
				1271568478,
				1271163514,
				1269235393,
				1268717136,
				1266041187,
				1266039925,
				1265808386,
				1263967981,
				1260597951,
				1256017251,
				1248068882,
				1248068866,
				1246339311,
				1245733782,
				1240319215,
				1240146248,
				1237986324,
				1237786108,
				1237293313,
				1236775164,
				1235565247,
				1235220238,
				1234758169,
				1234355537,
				1232541704,
				1229923584,
				1229494654,
				1229346202,
				1226235565,
				1225174559,
				1224679480,
				1222088793,
				1217396030,
				1216535505,
				1208242346,
				1205326663,
				1204351567,
				1201698429,
				1201329829,
				1200402332,
				1199798502,
				1198931873,
				1196833103,
				1196512645,
				1195363900,
				1194442763,
				1192884065,
				1191934781,
				1191934684,
				1191845406,
				1191672901,
				1189514739,
				1189404052,
				1186489933,
				1184159971,
				1184048062,
				1180443372,

};
	    Integer[][] matrix = new Integer[index.size()][260]; // words x documents 
	    
	    for(int left = 0; left < index.size(); left ++) {
	    	for(int right = 0; right < 260; right ++) {
	    		matrix[left][right] = 0;
	    	}
	    }

	    ArrayList<String> words = new ArrayList<String>();

		index.entrySet().forEach(word -> {
			words.add(word.getKey());
		    //System.out.println(word.getKey() + " " + word.getValue().toString());
			Map<Long, List<Term>> occurances = word.getValue();
			occurances.entrySet().forEach(occurance -> {
				for(int i = 0; i < doc_ids.length ; i++) {
					if(occurance.getKey() == doc_ids[i]) {
						matrix[(words.size()-1)][i] = occurance.getValue().size();
					}
				}
				
			});
		});
		
		/**
		for(int left = 0; left < index.size(); left ++) {
			System.out.print(index.keySet().toArray()[left]+ ",");
		}
    	System.out.println();
	    for(int right = 0; right < 260; right ++) {
	    	 //System.out.print(index.keySet().toArray()[left]+ " ");
	    	System.out.print(doc_ids[right]+ ",");
	    	for(int left = 0; left < index.size(); left ++) {
	    		System.out.print(matrix[left][right]+ ",");
	    	}
	    	
	    	System.out.println();
	    }**/
	    
		String rs = "";
		try {
			FileWriter fw = new FileWriter(new File("./matrix.csv"));
			StringBuffer sb = new StringBuffer("");
			rs = "words,";
			for(int left = 0; left < index.size(); left ++) {
				rs = rs + (left + 1)+ ",";
			}
			sb.append(rs + "\n");
			rs = "";
		    for(int right = 0; right < 260; right ++) {
		    	rs = rs + doc_ids[right]+ ",";
		    	for(int left = 0; left < index.size(); left ++) {
			    	rs = rs + matrix[left][right]+ ",";
		    	}
		    	rs = rs + "\n";
		    }
			sb.append(rs);
			fw.write(sb.toString());
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
