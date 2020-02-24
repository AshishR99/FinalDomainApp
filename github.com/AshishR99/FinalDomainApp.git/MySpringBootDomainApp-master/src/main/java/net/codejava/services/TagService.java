package net.codejava.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.entities.ChartDataBasedOnLevel;
import net.codejava.entities.Content;
import net.codejava.repositories.ChartRepository;
import net.codejava.repositories.ContentRepository;

@Service
public class TagService {


	//Hahsmap & sordted Hashmap creation for Tags
	public List<String> tagsHashMapCreation(String[] inputs,String[] textAll)
	{
		HashMap<String,Integer> hm = new HashMap<String,Integer>();
		Map<String, Integer> sortedhm = new HashMap<String,Integer>();
		List<String> listOfSelectedTags = new ArrayList<String>();
		
		
		for(int i=0;i<inputs.length;i++)
		{
			int count = 0;
			for(int j=0;j<textAll.length;j++)
			{     
				if(inputs[i].equalsIgnoreCase(textAll[j]))
				{
					count++;
				}
			}
			System.out.println(inputs[i]+" repeated: "+count);
			if(count>=1)
			{
				hm.put(inputs[i], count);
			}
			
			
			
			
		}
		
		System.out.println("hash map"+hm);
		System.out.println("Sorted Hash map"+sortedhm);

		if(hm.size()>3)
		{
			sortedhm = sortByValue(hm);
			int noOfTags = 3;
			listOfSelectedTags = fetchLastRecordsByValue(sortedhm,noOfTags);
			
		}
		else
		{
			for (Map.Entry<String, Integer> en : hm.entrySet()) 
			{ 
				System.out.println("Key = " + en.getKey() + ", Value = " + en.getValue()); 
				listOfSelectedTags.add(en.getKey());
//				return listOfSelectedTags;
			} 
			
		}
		
		
		return listOfSelectedTags;
		
	}


	// function to sort tags hashmap by values 
	public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
	{ 
		// Create a list from elements of HashMap 
		List<Map.Entry<String, Integer> > list = 
				new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 

		// Sort the list 
		Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
			public int compare(Map.Entry<String, Integer> o1,  
					Map.Entry<String, Integer> o2) 
			{ 
				return (o1.getValue()).compareTo(o2.getValue()); 
			} 
		}); 

		// put data from sorted list to hashmap  
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
		for (Map.Entry<String, Integer> aa : list) { 
			temp.put(aa.getKey(), aa.getValue()); 
		} 
		return temp; 
	} 


	// Fetch 
	public static List<String> fetchLastRecordsByValue(Map<String, Integer> sortedhm,int noOfTags)
	{
		//        System.out.println(sortedhm.size()-3);
		List<String> listOfTags = new ArrayList<String>();
		List<String> listOfSelectedTags = new ArrayList<String>();
		for (Map.Entry<String, Integer> en : sortedhm.entrySet()) 
		{ 
			System.out.println("Key = " + en.getKey() + ", Value = " + en.getValue()); 
			listOfTags.add(en.getKey());
		} 

//		// print the sorted hashmap 
		for(int i=listOfTags.size()-1;i>=listOfTags.size()-noOfTags;i--)
		{
			System.out.println("list of ele"+listOfTags.get(i));
			listOfSelectedTags.add(listOfTags.get(i));
		}
		
		return listOfSelectedTags;
	}



}
