package edu.sdccd.cisc191;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ToDoListApp extends Application {
    private ListView<String> taskListView;
    private TextField taskInput;
    private ToDoList toDoList;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private static final String server_ip = "127.0.0.1";
    private static final int server_port = 12345;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("To Do List");

        try {
            clientSocket = new Socket(server_ip, server_port);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Connected to server: " + server_ip + " on port " + server_port);
        } catch (IOException e) {
            System.err.println("Couldn't connect to server: ");
        }

        toDoList = new ToDoList();

        taskListView = new ListView<>();
        taskInput = new TextField();
        Button addButton = new Button("Add Task");
        Button removeButton = new Button("Remove Task");
        Button refreshButton = new Button("Refresh Tasks");
        Button sortButton = new Button("Sort Tasks");
        Button searchButton = new Button("Search Task");

        addButton.setOnAction(e -> addTask());
        removeButton.setOnAction(e -> removeTask());
        refreshButton.setOnAction(e -> refreshTasks());
        sortButton.setOnAction(e -> sortTasks());
        searchButton.setOnAction(e -> searchTask());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(taskListView, taskInput, addButton, removeButton, refreshButton, sortButton, searchButton);

        Scene scene = new Scene(layout, 200, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        refreshTasks();
    }

    private void addTask() {
        String taskDescription = taskInput.getText();
        if (!taskDescription.isEmpty()) {
            Task newTask = new Task(taskDescription);
            toDoList.addTask(newTask);
            taskListView.getItems().add(newTask.getDescription());
            try {
                outputStream.writeObject(newTask);
            } catch (IOException e) {
                System.err.println("Server didn't receive task: ");
            }

            taskInput.clear();
        }
    }

    private void removeTask() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String taskDescription = taskListView.getItems().get(selectedIndex);
            Task taskToRemove = new Task(taskDescription);
            toDoList.removeTask(taskToRemove);
            try {
                outputStream.writeObject(taskToRemove);
            } catch (IOException e) {
                System.err.println("Customize Toolbar...: ");
            }
        }
    }

    private void sortTasks() {
        SearchingAndSortingModule.sortByDescription(toDoList.getTasks());
        refreshTasks();
    }

    private void searchTask() {
        String keyword = taskInput.getText();
        Task foundTask = SearchingAndSortingModule.searchByDescription(toDoList.getTasks(), keyword);
        if (foundTask != null) {
            System.out.println("Task found: " + foundTask.getDescription());
        } else {
            System.out.println("Task not found");
        }
    }

    private void refreshTasks() {
        taskListView.getItems().clear();
        List<Task> allTasks = toDoList.getAllTasks();
        for (Task task : allTasks) {
            taskListView.getItems().add(task.getDescription());
        }
    }
}