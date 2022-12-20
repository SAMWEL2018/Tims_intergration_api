package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        getDuplicateChar("Hello");

    }

    public static void getDuplicateChar(String strChar){

        List<Character> charStore = new ArrayList<>();

        if (!(strChar.isEmpty())){
            int length = strChar.length()-1;

            char[] ca = strChar.toLowerCase().toCharArray();

            for (int i = 0; i <= length; i++){
                for (int k=0; k<=length; k++){
                    if (ca[i] == ca[k] && i!=k){
                        if (!charStore.contains(ca[i])){
                            charStore.add(ca[i]);
                        }
                    }
                }
            }
        }
        charStore.forEach(System.out::println);
    }
}
