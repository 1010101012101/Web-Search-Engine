package cs6322;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
public class ProjectGui	{
	public static String query = ""; 
	public static String ans;
	public static int count1=0;
	public static int count=0;
	
	public static HashMap<String, String> mapping = new HashMap<String, String>();
	public static void main(String[] args) throws IOException	{

		BufferedReader br = new BufferedReader(new FileReader("ResultSet1//Mapping.txt"));
		String line;
		while ((line = br.readLine()) != null) {
		  String[] parts = line.split("#");
		  System.out.println(parts[0]);
		  System.out.println(parts[1]);
		  mapping.put(parts[0], parts[1]);
		}
		br.close();

		JFrame frame = new JFrame("Search Engine");
		JPanel contentPane = new JPanel(new BorderLayout());
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		final JTextArea textArea = new JTextArea(2,30);
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		JButton submit = new JButton("Submit");
		JButton clear = new JButton("Reset		");
		JPanel content = new JPanel(new BorderLayout());
		final JTextArea google = new JTextArea(3,25);
		final JTextArea own = new JTextArea(3,10);
		final JTextArea bing = new JTextArea(3,25);
		submit.addActionListener(new ActionListener(){
			public void actionPerformed		(ActionEvent e)	{
				try{

					query = textArea.getText();
					String result;
					String result1;
					String finalized="";
					String finaley="";
					//String path = " cd C:\\Users\\Sahil\\workspace\\Web Search Engine\\GUI";
					String googleCMD = "python googleSearch.py"+" "+query;
					//System.out.println(googleCMD);
					String bingCMD = "python bingSearch.py"+"  "+query;
					//System.out.println(bingCMD);
					
					// call the boolean retrieval module 
					ArrayList<String> ourSearchResults = SearchFiles.get(query);
					
					System.out.println(ourSearchResults);
					Process googleSearch = Runtime.getRuntime().exec(googleCMD);
					Process bingSearch = Runtime.getRuntime().exec(bingCMD);

					for(String eachSearchResult : ourSearchResults)
					{
						System.out.println(eachSearchResult);
						String Key = eachSearchResult.replace("/", "\\");
						System.out.println(Key);
						String x = mapping.get(Key);
						System.out.println(x);
						own.append(x+ " doc id: " + Key +"\n");
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					Scanner googleResults = new Scanner(new File("C:/Users/Sahil/workspace/Web Search Engine/googlesearchresult.txt"));
					Scanner bingResults = new Scanner(new File("C:/Users/Sahil/workspace/Web Search Engine/microsearchresult.txt"));
					while(googleResults.hasNextLine())	{
						/*if (count>10)
							break;*/
						result = googleResults.nextLine();
						//result = count+")"+result;
						finalized = finalized+"\n"+result;
						//count+=1;
					}
					google.setText(finalized);
					googleResults.close();
					while(bingResults.hasNextLine())	{
						
						result1 = bingResults.nextLine();
						finaley = finaley+"\n"+result1;
						//count1+=1;
					}
					bing.setText(finaley);
					bingResults.close();
					//Scanner bingResults = new Scanner(new File("microsearchresult.txt"));
				} catch(IOException e1)	{
					System.out.println("IO Error");
				}
				/*String line=""; String ans="";
				// google.setText(textArea.getText());
				try {
					query = textArea.getText();
					final URL url = new URL("https://www.google.com/cse?cx=013269018370076798483%3A8eec3papwpi&ie=UTF-8&q=" + URLEncoder.encode(query, "UTF-8")+"&gsc.tab=0&gsc.q=charan&gsc.page=1");
    				final URLConnection connection = url.openConnection();
    				connection.setConnectTimeout(60000);
    				connection.setReadTimeout(60000);
    				connection.addRequestProperganety("User-Agent", "Mozilla/5.0");
    				final Scanner reader = new Scanner(connection.getInputStream(), "UTF-8");
    				while(reader.hasNextLine())	{
    					 line = reader.nextLine();
    					 ans= ans+line;
    					 ans = ans.replace("!DOCTYPE ","");
    					// google.append(line);
    					 //google.setText(line);
    					//System.out.println(line);

    				}
    				System.out.println(ans);
    				google.setText(ans);
    				reader.close();				 
    			} catch (IOException e1)	{

    			}*/
				//String s = ...;

			}
		});
		clear.addActionListener(new ActionListener(){
			public void actionPerformed		(ActionEvent e)	{
				count=0;
				count1=0;
				textArea.setText("");
				google.setText("");
				bing.setText("");
				own.setText("");
				try 	{
					PrintWriter writer = new PrintWriter("GUI/googlesearchresult.txt");
					PrintWriter writer1 = new PrintWriter("GUI/microsearchresult.txt");
					writer.print("");
					writer.close();
					writer1.print("");
					writer1.close();
				} catch(FileNotFoundException e2)	{
					System.out.println("File Not Found");
				}


			}
		});

		contentPane.add(textArea,BorderLayout.NORTH);
		contentPane.add(submit,BorderLayout.LINE_START);
		contentPane.add(clear,BorderLayout.LINE_END);

		google.setEditable(false);

		google.setLineWrap(true);
		Border border1 = BorderFactory.createLineBorder(Color.BLACK);
		google.setBorder(BorderFactory.createCompoundBorder(border1, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		//google.setBackground(Color.BLUE);
		//bing.setBackground(Color.GREEN);
		bing.setEditable(true);
		bing.setLineWrap(true);
		Border border2 = BorderFactory.createLineBorder(Color.BLACK);
		bing.setBorder(BorderFactory.createCompoundBorder(border2, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		//own.setBackground(Color.BLACK);
		own.setEditable(true);
		own.setLineWrap(true);
		Border border3 = BorderFactory.createLineBorder(Color.BLACK);
		own.setBorder(BorderFactory.createCompoundBorder(border3, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		content.add(google,BorderLayout.WEST);
		content.add(own,BorderLayout.CENTER);
		content.add(bing,BorderLayout.EAST);
		frame.add(contentPane,BorderLayout.PAGE_START);
		frame.add(content,BorderLayout.CENTER);
		frame.setSize(700,500);
		frame.setVisible(true);



	}
}