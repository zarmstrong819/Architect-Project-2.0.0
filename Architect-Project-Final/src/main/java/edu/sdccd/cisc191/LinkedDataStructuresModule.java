package edu.sdccd.cisc191;

import java.util.LinkedList;

public class LinkedDataStructuresModule {
    public static LinkedList<String> convertArrayToLinkedList(String[] dataArray) {
        LinkedList<String> linkedList = new LinkedList<>();
        for (String data : dataArray) {
            linkedList.add(data);
        }
        return linkedList;
    }
}
