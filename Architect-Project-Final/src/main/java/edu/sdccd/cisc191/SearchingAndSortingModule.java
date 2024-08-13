package edu.sdccd.cisc191;

import java.util.Comparator;
import java.util.List;

public class SearchingAndSortingModule {

    public static Task searchByDescription(List<Task> tasks, String keyword) {
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                return task;
            }
        }
        return null;
    }


    public static void sortByDescription(List<Task> tasks) {
        tasks.sort(Comparator.comparing(Task::getDescription));
    }
}