package cs6322;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler4jWebCrawler extends WebCrawler
{




	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private final static ArrayList<String> exactStopList =  new ArrayList<String>(
			Arrays.asList("http://www.dmoz.org/",
					"http://www.dmoz.org/docs/en/about.html",
					"http://blog.dmoz.org/",
					"http://www.dmoz.org/public/update?cat=News",
					"http://www.dmoz.org/public/abuse?cat=News&lang=en",
					"http://www.dmoz.org/help/en/helpmain.html",
					"http://www.dmoz.org/desc/News",
					"http://www.dmoz.org/docs/en/termsofuse.html",
					"http://www.mozilla.org/",
					"http://musicmoz.org/",
					"http://en.wikipedia.org/")
			); 

	private final static ArrayList<String> startsWithStopList = new ArrayList<>(
			Arrays.asList("http://search.aol.com/",
					"http://search.ask.com/",
					"http://www.bing.com/",
					"http://gigablast.com/",
					"http://www.google.com/",
					"http://search.lycos.com/web",
					"http://search.yahoo.com/",
					"http://new2.yippy.com/",
					"http://www.dmoz.org/editors/")
			);

	private Integer k=0;
	private Integer one=1;
	Hashtable<Page, Integer> HashWithOutgoingLinks=new Hashtable<Page, Integer>();
	Hashtable<Page, Integer> HashWithIncomingLinks=new Hashtable<Page, Integer>();

	HashMap <String, ArrayList<WebURL>> hitsOutgoingMap = new HashMap<>();
	HashMap <String, ArrayList<String>> hitsIncomingMap = new HashMap<>();


	List<WebURL> links = null;
	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		//System.out.println("Checking whether the page should be visited : " + url);
		String parentUrl = url.getParentUrl().toLowerCase();
		String urlString = url.getURL().toLowerCase();
		boolean result = (FILTERS.matcher(urlString).matches());
		if(result)
		{
			return false;
		}

		// check exact stopList
		for(String eachStopUrl : Crawler4jWebCrawler.exactStopList)
		{
			if(urlString.equals(eachStopUrl))
			{
				return false;
			}
		}

		// check startsWithStopList
		for(String eachStopUrl : Crawler4jWebCrawler.startsWithStopList)
		{
			if(urlString.startsWith(eachStopUrl))
			{
				return false;
			}
		}


		if(parentUrl.startsWith("http://www.dmoz.org/news"))
		{
			if(urlString.startsWith("http://www.dmoz.org/"))
			{
				if(urlString.startsWith("http://www.dmoz.org/news"))
				{
					result = true;
				}
				else
				{
					result = false;
				}
			}
			else
			{
				result = true;
			}
		}

		System.out.println("parent -> "+parentUrl +" --->>>" + url + ":" + result);
		return result;
	}

	/**               :
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {


		int docid = page.getWebURL().getDocid();
		FileWriter fstream1 = null;
		BufferedWriter out1=null;

		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();

		System.out.println("Docid: " + docid);
		System.out.println("URL: " + url);
		System.out.println("Domain: '" + domain + "'");
		System.out.println("Sub-domain: '" + subDomain + "'");
		System.out.println("Path: '" + path + "'");
		System.out.println("Parent page: " + parentUrl);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			links = htmlParseData.getOutgoingUrls();
			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
			CheckHashTable(page, links.size());
			String str = "ResultSet1\\crawl" + docid + ".html";
			System.out.println(str);
			BufferedWriter out = null;
			try {
				FileWriter fstream = new FileWriter(str);
				fstream1 = new FileWriter("ResultSet1\\Mapping.txt",true);
				out1=new BufferedWriter(fstream1);
				out = new BufferedWriter(fstream);
				out.write(html);
				out.newLine();
				out.flush();

				out1.write(str+"#"+url);
				out1.newLine();

				out1.flush();
			} catch (Exception e) {
			}

			// update outgoing and Incoming Map
			this.hitsOutgoingMap.put(url, new ArrayList<>(links));

			ArrayList<String> parentsList = this.hitsIncomingMap.get(parentUrl);
			if(parentsList == null)
			{
				ArrayList <String> newParentList = new ArrayList<>();
				newParentList.add(parentUrl);
				this.hitsIncomingMap.put(url, newParentList);
			}
			else
			{
				parentsList.add(parentUrl);
			}
		}



		// write the page structures to a file
		File f = new File("ResultSet1\\outLinkWriter.txt");

		if(f.exists())
		{

		}
		else
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		File f1 = new File("ResultSet1\\outLinkWriter.txt");

		if(f1.exists())
		{

		}
		else
		{
			try {
				f1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {

			FileOutputStream fout = new FileOutputStream("ResultSet1\\outLinkWriter.txt");
			FileOutputStream fout1 = new FileOutputStream("ResultSet1\\inLinkWriter.txt");

			ObjectOutputStream oos = new ObjectOutputStream(fout);  
			ObjectOutputStream oos1 = new ObjectOutputStream(fout1); 
			oos.writeObject(hitsOutgoingMap);
			oos1.writeObject(hitsIncomingMap);
			oos.close();
			oos1.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("=============");
	}

	private void CheckHashTable(Page s, Integer size) {
		try
		{
			if(!HashWithOutgoingLinks.isEmpty())
			{
				if(HashWithOutgoingLinks.containsKey(s))
				{

					k=HashWithIncomingLinks.get(s);
					k++;
					HashWithIncomingLinks.remove(s);
					HashWithIncomingLinks.put(s,k);
				}
				else
				{
					HashWithOutgoingLinks.put(s,size);
					HashWithIncomingLinks.put(s, one);
				}


			}
			else
			{
				HashWithOutgoingLinks.put(s,size);
			}
		} catch (Exception e){}


	}

}
