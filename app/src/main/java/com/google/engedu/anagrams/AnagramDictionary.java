/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static HashMap<String,ArrayList<String>> letterToWord;
    private static HashSet<String> wordSet;
    private static ArrayList<String> wordList;

    public AnagramDictionary(Reader reader) throws IOException {
        wordList=new ArrayList<>();

        BufferedReader in = new BufferedReader(reader); // collects data from the text file as an input stream(of characters)
        letterToWord=new HashMap<>();
        wordSet =new HashSet<>();
        String line;

        while((line = in.readLine()) != null) {

            String word = line.trim(); // trims out the newline characters to give only the word
            /**
             *My code below
             * --> need to store all the words in the dictionary as key-value pairs where key would be the lexicographically sorted form of the
             *  word and the value would be an arraylist of all the anagrams (that presently we are checking via sortletters helper)
             *  -->first check whether the word (sorted form) is already a key in the hash map .If so add the word (unsorted) to the arraylist
             *  of that key.
             *  --> If the key is not present yet, add it to the hashmap by put() method.
             */
            //Log.i("check",sortLetters(word)+"");

            wordList.add(word);
            wordSet.add(word);
            if(letterToWord.containsKey(sortLetters(word)))
            {
                letterToWord.get(sortLetters(word)).add(word); //get() gets the arraylist associated with the key, and add() adds the word to it.
            }
            else
            {
                ArrayList<String> anag=new ArrayList<>();
                anag.add(word);
                letterToWord.put(sortLetters(word),anag); // put() makes a new entry into the hashmap with a sorted key and a new arraylist

            }

        }
    }

    /**
     * Checks if the word is valid (in wordset) and not a substring of the currentword.
     * Call from {@link AnagramsActivity} processWord() method
     * @param word:- word the user types in the editText;
     * @param base:- the word for which anagram has been asked
     * @return boolean
     */

    public boolean isGoodWord(String word, String base) {
    if(wordSet.contains(word))
    {
        if (word.contains(base))
            return false;
        else
            return true;
    }

        return false;

    }

    /**
     * Helper to check for anagrams
     * @param word_check : input word from getAnagrams()
     * @return : a sorted string
     */
    public String sortLetters(String word_check)
    {
        char charArray[]=word_check.toCharArray();
        Arrays.sort(charArray);
        String sortedString=new String(charArray);
        return sortedString;
    }


    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
       /* for(int i=0;i<wordList.size();i++)
        {
            if(sortLetters(wordList.get(i)).equals(sortLetters(targetWord)))
                result.add(wordList.get(i));
        }*/

         result=letterToWord.get(sortLetters(targetWord));

        return result;
    }

    /**
     * My code below.
     * This method is used to get anagrams with one extra letter.
     * To find such words we try appending each letter to the the word chosen and check if its a valid word.
     *
     * @param word
     * @return List
     */
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char letter='a';letter<='z';letter++)
        {
            if(letterToWord.containsKey(sortLetters(letter+word)))
            {
                ArrayList<String> list_anagrams=letterToWord.get(sortLetters(letter+word));
                for(int i=0;i<list_anagrams.size();i++)
                {
                    if(isGoodWord(list_anagrams.get(i),word))
                        result.add(list_anagrams.get(i));
                }
            }
        }
        return result;
    }

    /**
     * My code below
     * This method picks up a good starting word for the user. The filter here is chooosing the words which have
     * certain minimum number of anagrams only
     * @return A good starting word
     */
    public String pickGoodStarterWord() {
        String word="";
        int  random=new Random().nextInt(wordList.size());
        for(int i=random;i<wordList.size();i++)
        {
            word=wordList.get(i);
            if(letterToWord.get(sortLetters(word)).size()<=MIN_NUM_ANAGRAMS)
            {
                break;
            }
        }
        return word;
    }
}
