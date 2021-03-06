
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.util.Calendar;


public class TST<Value> {

  //Implemented using Sedgewick and Waynes version found here https://algs4.cs.princeton.edu/52trie/TST.java.html
  

  /* A Node in your trie containing a Value val and a pointer to its children */
  private static class TrieNode<Value> {
		    private char c;                        // character
        private TrieNode<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
  }

  /* a pointer to the start of your trie */
  private TrieNode<Value> root = new TrieNode<Value>();
  /* initialise size to 0 */
  private int n = 0;

  //Create empty string symbol table
  public TST(){

  }

  /*
   * Returns the number of words in the trie
   */
  public int size() {
    return n;
  }

  /*
   * returns true if the word is in the trie, false otherwise
   */
  public boolean contains(String key) {
    	if(key == "")
        return false;

      else
        return (get(key) != null);
  }

  /*
   * return the value stored in a node with a given key, returns null if word is not in trie
   */
  public Value get(String key) {
    if(key == "")
      return null;

    TrieNode<Value> x = new TrieNode<Value>();
    x = get(root, key, 0);

    if(x == null)
    	return null;

    else
    	return x.val;
  }

  /*
   * return the subtrie corresponding to given key
   */
  public TrieNode<Value> get(TrieNode<Value> x, String key, int d){
  	if(x == null)
      return null;

    char c;
  	c = key.charAt(d);

  	//If character is less than node x's character, go left
  	if(c<x.c)
  		return get(x.left, key, d);

  	//If character is more than node x's character, go right
  	else if(c>x.c)
  		return get(x.right, key, d);

  	//Otherwise, if d is still within range, go middle
  	else if(d<key.length()-1)
  		return get(x.mid, key, d+1);

  	//Otherwise, at end. Return x
  	else 
  		return x;
  }

  /*
   * stores the Value val in the node with the given key
   */
  public void put(String key, Value val) {

  	if(!contains(key))
  		n++;

  	root = put(root, key, val, 0);
  }

  private TrieNode<Value> put(TrieNode<Value> x, String key, Value val, int d){
  	char c = key.charAt(d);

  	//If null node, create new node for char c 
  	if(x == null){
  		x = new TrieNode<Value>();
  		x.c = c;
  	}

  	//If char is less than char of x, go left
  	if(c<x.c)
  		x.left = put(x.left, key, val, d);

  	//If char is more than char of x, go right
  	else if(c>x.c)
  		x.right = put(x.right, key, val, d);

  	//If d is still within range, go mididle
  	else if(d<key.length() - 1)
  		x.mid = put(x.mid, key, val, d+1);

  	//Otherwise return current node
  	else 
  		x.val = val;

  	return x;
  }

  /*
   * returns the linked list containing all the keys present in the trie
   * that start with the prefix passes as a parameter, sorted in alphabetical order
   */
  public LinkedList<String> keysWithPrefix(String prefix) {

    LinkedList<String> linkedList = new LinkedList<String>();
    TrieNode<Value> x = get(root, prefix, 0);

    //If no keys found, return null LinkedList
    if(x == null)
    	return linkedList;

     //If x contains the prefix, add prefix to linked list
    Value val = get(prefix);

    if(val != null)
      linkedList.add(prefix);


    //Get remaining keys with prefix from x.mid onwards
    getKeysWithPrefix(x.mid, new StringBuilder(prefix), linkedList);

    return linkedList;
  }

  private void getKeysWithPrefix(TrieNode<Value> x, StringBuilder prefix, LinkedList<String> list){
  	if(x == null)
  		return;

  	//Search left for keys with prefix
  	getKeysWithPrefix(x.left, prefix, list);

  	//If node has a value, push prefix+c onto list
  	if(x.val != null)
  		list.add(prefix.toString() + x.c);

  	//Search middle for keys containing prefix+c
  	getKeysWithPrefix(x.mid, prefix.append(x.c), list);

  	//Remove c from end of prefix and search right
  	prefix.deleteCharAt(prefix.length()-1);
  	getKeysWithPrefix(x.right, prefix, list);
  }

  public static void main(String[] args) throws IOException, FileNotFoundException, ParseException{

    //Uncomment to use TST with .txt files
    /*
    //Test using .txt input file (sheShells.txt)
    TST<Integer> st = new TST<Integer>();
    for (int i = 0; !StdIn.isEmpty(); i++) {
        String key = StdIn.readString();
        st.put(key, i);
    }
    LinkedList<String> keysList = st.getAllKeys();
    // print results
    if (st.size() < 100) {
        StdOut.println("keys(\"\"):");
        for (String key : keysList) {
            StdOut.println(key + " " + st.get(key));
        }
        StdOut.println();
    }
    StdOut.println("keysWithPrefix(\"a\"):");
    for (String s : st.keysWithPrefix("a"))
        StdOut.println(s);
    StdOut.println();
    */

    
    //Uncomment to use TST with .json files
    /*
    //Test for reading .json files
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader("./txt_files/BUSES_SERVICE_0.json"));
    JSONArray busArray = (JSONArray) obj;
    TST<Integer> busTST = new TST<Integer>();
    JSONObject tmp = new JSONObject();
    
    //Iterate over route array
    busArray.forEach( bus -> parseBusObject( (JSONObject) bus, busTST ) );
    //Printing results to txt file
    BufferedWriter writer = null;
    //create a temporary file
    String title = "bus_json_TST_keys_log";
    File resultsFile = new File(title);
    // This will output the full path where the file will be written to...
    System.out.println(resultsFile.getCanonicalPath());
    writer = new BufferedWriter(new FileWriter(resultsFile)); 
    LinkedList<String> keysList = busTST.getAllKeys();
    // print results
    if (busTST.size() < 1000) {
        //StdOut.println("keys(\"\"):");
        writer.write("keys(\"\"):\n"); 
        for (String key : keysList) {
            //StdOut.println(key + " " + busTST.get(key));
            writer.write(key + " " + busTST.get(key) + "\n"); 
        }
        writer.close();
    }
    StdOut.println("keysWithPrefix(\"DOWN\"):");
    for (String s : busTST.keysWithPrefix("DOWN")){
        StdOut.println(s);
      StdOut.println(busTST.get(s));
    }
    StdOut.println();
    */

    //Tests for google supplied .txt file
    TST<Long> googleTST = new TST<Long>();

    for (int i = 0; !StdIn.isEmpty(); i++) {
        String key = StdIn.readString();
        Long val = StdIn.readLong();
        googleTST.put(key, val);
    }
    LinkedList<String> keysList = googleTST.getAllKeys();
    // print results
    //StdOut.println("keys(\"\"):");
    for (String key : keysList) {
      //StdOut.println(key + " " + googleTST.get(key));
    }
    StdOut.println();
    StdOut.println("Total number of words in file = " + googleTST.size());
    StdOut.println("\nFrequency of word 'ALGORITHM' is " + googleTST.get("ALGORITHM"));
    StdOut.println("\nIs 'EMOJI' present in the text file? " + googleTST.contains("EMOJI"));
    StdOut.println("Is 'BLAH' present in the text file? " + googleTST.contains("EMOJI"));
    StdOut.println("\nCounting keys with prefix 'TEST'...");
    int n = 0;
    for (String s : googleTST.keysWithPrefix("TEST"))
        n++;
    StdOut.println("Found " + n + " keys with prefix 'TEST'\n");

    /*   
    StdOut.println("keysWithPrefix(\"DOWN\"):");
    for (String s : googleTST.keysWithPrefix("DOWN")){
        StdOut.println(s);
      StdOut.println(busTST.get(s));
    }
    StdOut.println();
    */
  }

  /*
   * Returns all the keys in TST in the form of a LinkedList
   */
  public LinkedList<String> getAllKeys(){
    LinkedList<String> list = new LinkedList<String>();
    getKeysWithPrefix(root, new StringBuilder(), list);
    return list;
  }

  
  private static void parseBusObject(JSONObject route, TST<Integer> busTST){
      //Get details within list
      System.out.println("VehicleNo = " + route.get("VehicleNo"));
      System.out.println("TripId = " + route.get("TripId"));
      System.out.println("RouteNo = " + route.get("RouteNo"));
      System.out.println("Direction = " + route.get("Direction"));
      System.out.println("Destination = " + route.get("Destination"));
      System.out.println("Pattern = " + route.get("Pattern"));
      System.out.println("Latitude = " + route.get("Latitude"));
      System.out.println("Longitude = " + route.get("Longitude"));
      System.out.println("RecordedTime = " + route.get("RecordedTime"));
      System.out.println("RouteMap = " + route.get("RouteMap") + "\n\n");
      
      String dest = "";
      int val = 0;
      dest = (String) route.get("Destination");
      //System.out.println("\n\nExtracted object with destination = " + dest);
      
      //If TST already contains a record for destination increment value (count)
      if(busTST.contains(dest)){
        //System.out.println("TST contains record of destination " + dest);
        val = busTST.get(dest);
        //System.out.println("Record count = " + val);
        busTST.put(dest, val+1);
        //System.out.println("Updating TST for " + dest + " with new count " + (val+1));
      }

      else{
        //System.out.println("\n\nNo record found in TST for destination " + dest);
        //System.out.println("Creating a record now...");
        busTST.put(dest, 1);
      }
  }
}