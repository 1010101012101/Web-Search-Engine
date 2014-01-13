package cs6322;
/* SearchTest.java
 *
 * Copyright (c) 1997, 2000 Douglass R. Cutting.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

import com.lucene.analysis.Analyzer;
import com.lucene.analysis.StopAnalyzer;
import com.lucene.queryParser.QueryParser;
import com.lucene.search.Hits;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.Searcher;

import java.io.*;
import java.util.ArrayList;

/**
 * Gives a interface to ask Booleanqueries on underlying index. Usage: java
 * SearchFiles
 */

public class SearchFiles {

	public static void main(String[] args)
	{
		String query = "Bango Oil Refinery";
		get(query);

	}

	public static ArrayList<String> get(String queryString) {
		System.out.println("Inside the searchfiles.................");
		ArrayList<String> results = new ArrayList<>();
		try {

			Searcher searcher = new IndexSearcher("index");
			Analyzer analyzer = new StopAnalyzer();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {
				FileWriter fstream1 = null;
				BufferedWriter out = null;

				String line = queryString;

				if (line.length() == -1)
					break;

				Query query = QueryParser.parse(line, "contents", analyzer);
				System.out.println("Searching for: "
						+ query.toString("contents"));

				Hits hits = searcher.search(query);
				System.out.println(hits.length() + " total matching documents");
				final int HITS_PER_PAGE = 100;
				fstream1 = new FileWriter(
						"ResultSet1\\Query.txt");

				out = new BufferedWriter(fstream1);
				out.write("");
				for (int start = 0; start < hits.length(); start += HITS_PER_PAGE) {
					int end = Math.min(hits.length(), start + HITS_PER_PAGE);
					for (int i = start; i < end; i++) {
						if (hits.doc(i).get("url") != null) {
							String currentResult = hits.doc(i).get("url");
							System.out.println("Score : "+i+" "+hits.score(i));
							results.add(currentResult);
							System.out.println(i+"" + currentResult);
							out.write(currentResult);
							out.newLine();
							out.flush();
						}
					}
				}
				if(fstream1 != null){
					fstream1.flush();
					fstream1.close();
				}
				break;
			}

			searcher.close();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		}
		
		return results;
	}
}
