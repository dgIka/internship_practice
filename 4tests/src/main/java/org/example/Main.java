package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        int size = 100000;

        // Заполнение списков
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        // Вставка в середину
        long start = System.nanoTime();
        arrayList.add(size / 2, 999);
        long end = System.nanoTime();
        System.out.println("ArrayList вставка в середину: " + (end - start) + " нс");

        start = System.nanoTime();
        linkedList.add(size / 2, 999);
        end = System.nanoTime();
        System.out.println("LinkedList вставка в середину: " + (end - start) + " нс");
    }
}