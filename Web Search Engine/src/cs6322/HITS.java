package cs6322;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import edu.uci.ics.crawler4j.url.WebURL;


public class HITS {

	public static void main(String [] args)
	{
		// load the graph
		HashMap <String, ArrayList<WebURL>> hitsOutgoingMap = new HashMap<>();
		HashMap <String, ArrayList<String>> hitsIncomingMap = new HashMap<>();

		try{

			FileInputStream fin = new FileInputStream("ResultSet1\\outLinkWriter.txt");
			FileInputStream fin1 = new FileInputStream("ResultSet1\\inLinkWriter.txt");
			ObjectInputStream ois = new ObjectInputStream(fin);
			ObjectInputStream ois1 = new ObjectInputStream(fin1);

			hitsOutgoingMap = (HashMap<String, ArrayList<WebURL>>) ois.readObject();
			hitsIncomingMap = (HashMap<String, ArrayList<String>>) ois1.readObject();

			ois.close();
			ois1.close();


		}catch(Exception ex){
			ex.printStackTrace();
		} 

		runHits(hitsIncomingMap, hitsOutgoingMap);


	}

	private static void runHits(HashMap<String, ArrayList<String>> hitsIncomingMap, HashMap<String, ArrayList<WebURL>> hitsOutgoingMap) {
		// TODO Auto-generated method stub
		HashMap<String, Double> HubScores = new HashMap<>();
		HashMap<String, Double> AuthScores = new HashMap<>();

		for(Entry<String, ArrayList<String>> eachEntry : hitsIncomingMap.entrySet())
		{
			HubScores.put(eachEntry.getKey(), 1.0);
			AuthScores.put(eachEntry.getKey(), 1.0);
		}

		for(Entry<String, ArrayList<WebURL>> eachEntry : hitsOutgoingMap.entrySet())
		{
			HubScores.put(eachEntry.getKey(), 1.0);
			AuthScores.put(eachEntry.getKey(), 1.0);
		}

		for(int i=0 ; i < 5 ; i++)
		{
			// hub update
			HashMap<String, Double> newHubScores = new HashMap<>();

			for(Entry<String, Double> eachEntry: HubScores.entrySet())
			{
				ArrayList<WebURL> outlinks = hitsOutgoingMap.get(eachEntry.getKey());
				double outLinkAuthScores = 0;

				for(WebURL eachOutLink : outlinks)
				{
					Double x = AuthScores.get(eachOutLink.getURL());

					if(x != null)
					{
						outLinkAuthScores += x;
					}
				}

				double newHubEntry = eachEntry.getValue() + outLinkAuthScores;
				newHubScores.put(eachEntry.getKey(), newHubEntry);
			}

			// auth update
			HashMap<String, Double> newAuthScores = new HashMap<>();

			for(Entry<String, Double> eachEntry: AuthScores.entrySet())
			{
				ArrayList<String> inlinks = hitsIncomingMap.get(eachEntry.getKey());
				double inLinkHubScores = 0;

				for(String eachInLink : inlinks)
				{
					Double x = HubScores.get(eachInLink);
					if(x != null)
					{
						inLinkHubScores += x;
					}
				}

				double newAuthEntry =  eachEntry.getValue() + inLinkHubScores;
				newAuthScores.put(eachEntry.getKey(), newAuthEntry);
			}

			HubScores = newHubScores;
			AuthScores = newAuthScores;
		}

		// write the scores to file
		try {

			FileOutputStream fout = new FileOutputStream("ResultSet1\\HubScores.txt");
			FileOutputStream fout1 = new FileOutputStream("ResultSet1\\AuthScores.txt");

			ObjectOutputStream oos = new ObjectOutputStream(fout);  
			ObjectOutputStream oos1 = new ObjectOutputStream(fout1); 
			oos.writeObject(HubScores);
			oos1.writeObject(AuthScores);
			oos.close();
			oos1.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
