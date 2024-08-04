import java.util.*;

class Task {
    private String title;
    private String description;
    private int priority;
    private String deadline;
    private String status;
    private List<Task> dependencies;

    public Task(String title, String description, int priority, String deadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = "pending";
        this.dependencies = new ArrayList<>();
    }

    public void addDependency(Task task) {
        dependencies.add(task);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Task> getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", deadline='" + deadline + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

class TaskPriorityQueue {
    private PriorityQueue<Task> queue;

    public TaskPriorityQueue() {
        queue = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority).reversed());
    }

    public void addTask(Task task) {
        queue.add(task);
    }

    public Task getNextTask() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class TaskGraph {
    private List<Task> tasks;
    private List<Task> sortedTasks;

    public TaskGraph() {
        tasks = new ArrayList<>();
        sortedTasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        if (!tasks.contains(task)) {
            tasks.add(task);
        }
    }

    public void addDependency(Task task1, Task task2) {
        addTask(task1);
        addTask(task2);
        task1.addDependency(task2);
    }

    public List<Task> topologicalSort() {
        boolean[] visited = new boolean[tasks.size()];
        Stack<Task> stack = new Stack<>();

        for (Task task : tasks) {
            if (!visited[tasks.indexOf(task)]) {
                topologicalSortUtil(task, visited, stack);
            }
        }

        sortedTasks.clear();
        while (!stack.isEmpty()) {
            sortedTasks.add(stack.pop());
        }

        return sortedTasks;
    }

    private void topologicalSortUtil(Task task, boolean[] visited, Stack<Task> stack) {
        visited[tasks.indexOf(task)] = true;
        for (Task neighbor : task.getDependencies()) {
            if (!visited[tasks.indexOf(neighbor)]) {
                topologicalSortUtil(neighbor, visited, stack);
            }
        }
        stack.push(task);
    }
}

class TaskStack {
    private Stack<Task> stack;

    public TaskStack() {
        stack = new Stack<>();
    }

    public void pushTask(Task task) {
        stack.push(task);
    }

    public Task popTask() {
        return stack.pop();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Task> taskList = new ArrayList<>();
    private static TaskPriorityQueue priorityQueue = new TaskPriorityQueue();
    private static TaskGraph taskGraph = new TaskGraph();
    private static TaskStack taskStack = new TaskStack();

    public static void main(String[] args) {
        while (true) {
            try {
                showMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addTask();
                        break;
                    case 2:
                        showTasks();
                        break;
                    case 3:
                        showNextTask();
                        break;
                    case 4:
                        showTaskDetails();
                        break;
                    case 5:
                        sortTasks();
                        break;
                    case 6:
                        showTaskHistory();
                        break;
                    case 7:
                        changeTaskStatus();
                        break;
                    case 8:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void showMenu() {
        System.out.println("Prioruty & Time Scheduler");
        System.out.println("1. Add Task");
        System.out.println("2. Show Tasks");
        System.out.println("3. Show Next Task");
        System.out.println("4. Show Task Details");
        System.out.println("5. Sort Tasks");
        System.out.println("6. Show Task History");
        System.out.println("7. Change Task Status");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addTask() {
        try {
            System.out.print("Enter title: ");
            String title = scanner.nextLine();
            System.out.print("Enter description: ");
            String description = scanner.nextLine();
            int priority = 0;
            boolean validPriority = false;

            while (!validPriority) {
                System.out.print("Enter priority (integer): ");
                String priorityInput = scanner.nextLine();
                try {
                    priority = Integer.parseInt(priorityInput);
                    validPriority = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Priority should be an integer.");
                }
            }

            System.out.print("Enter deadline: ");
            String deadline = scanner.nextLine();

            Task task = new Task(title, description, priority, deadline);
            taskList.add(task);
            priorityQueue.addTask(task);
            taskGraph.addTask(task);
            taskStack.pushTask(task);

            System.out.println("Task added successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred while adding the task: " + e.getMessage());
        }
    }

    private static void showTasks() {
        if (taskList.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            for (Task task : taskList) {
                System.out.println(task);
            }
        }
    }

    private static void showNextTask() {
        if (priorityQueue.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            Task nextTask = priorityQueue.getNextTask();
            System.out.println("Next Task: " + nextTask);
        }
    }

    private static void showTaskDetails() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        boolean found = false;
        for (Task task : taskList) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                System.out.println(task);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Task not found.");
        }
    }

    private static void sortTasks() {
        List<Task> sortedTasks = taskGraph.topologicalSort();
        System.out.println("Sorted Tasks:");
        for (Task task : sortedTasks) {
            System.out.println(task);
        }
    }

    private static void showTaskHistory() {
        if (taskStack.isEmpty()) {
            System.out.println("No task history available.");
        } else {
            System.out.println("Task History:");
            while (!taskStack.isEmpty()) {
                System.out.println(taskStack.popTask());
            }
        }
    }

    private static void changeTaskStatus() {
        try {
            if (taskList.isEmpty()) {
                System.out.println("No tasks available to change status.");
                return;
            }
            
            System.out.print("Enter task title: ");
            String title = scanner.nextLine();
            boolean found = false;
            
            for (Task task : taskList) {
                if (task.getTitle().equalsIgnoreCase(title)) {
                    System.out.print("Enter new status: ");
                    String newStatus = scanner.nextLine();
                    task.setStatus(newStatus);
                    found = true;
                    System.out.println("Task status updated successfully.");
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Task not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while changing task status: " + e.getMessage());
        }
    }
}