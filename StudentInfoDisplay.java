import java.util.*;
import java.io.*;
                    
                    class StudentInformation implements Serializable {
                        String name;
                        String Lname;
                        int id;
                        float cgpa;
                        float percentage;
                    
                        public StudentInformation(String name, String Lname, int id, float cgpa) {
                            this.name = name;
                            this.Lname = Lname;
                            this.id = id;
                            this.cgpa = cgpa;
                            this.percentage = cgpa * 100;
                        }
                    
                        @Override
                        public String toString() {
                            return "ID: " + id + ", Name: " + name + " " + Lname + ", CGPA: " + cgpa + ", Percentage: " + percentage;
                        }
                    }
                    
                    class StudentInfo {
                        void QuickSortByID(ArrayList<StudentInformation> students, int low, int high) {
                            if (low < high) {
                                int partition_point = partitionByID(students, low, high);
                                QuickSortByID(students, low, partition_point - 1);
                                QuickSortByID(students, partition_point + 1, high);
                            }
                        }
                    
                        int partitionByID(ArrayList<StudentInformation> students, int low, int high) {
                            int pivot = students.get(high).id;
                            int ptr = low - 1;
                            for (int i = low; i < high; i++) {
                                if (students.get(i).id < pivot) {
                                    ptr++;
                                    swap(students, i, ptr);
                                }
                            }
                            swap(students, ptr + 1, high);
                            return ptr + 1;
                        }
                    
                        void QuickSortByCGPA(ArrayList<StudentInformation> students, int low, int high) {
                            if (low < high) {
                                int partition_point = partitionByCGPA(students, low, high);
                                QuickSortByCGPA(students, low, partition_point - 1);
                                QuickSortByCGPA(students, partition_point + 1, high);
                            }
                        }
                    
                        int partitionByCGPA(ArrayList<StudentInformation> students, int low, int high) {
                            float pivot = students.get(high).cgpa;
                            int ptr = low - 1;
                            for (int i = low; i < high; i++) {
                                if (students.get(i).cgpa > pivot) {
                                    ptr++;
                                    swap(students, i, ptr);
                                }
                            }
                            swap(students, ptr + 1, high);
                            return ptr + 1;
                        }
                    
                        void MergeSortByLastName(ArrayList<StudentInformation> students, int low, int high) {
                            if (low < high) {
                                int mid = (low + high) / 2;
                                MergeSortByLastName(students, low, mid);
                                MergeSortByLastName(students, mid + 1, high);
                                mergeByLastName(students, low, mid, high);
                            }
                        }
                    
                        void mergeByLastName(ArrayList<StudentInformation> students, int low, int mid, int high) {
                            ArrayList<StudentInformation> leftArr = new ArrayList<>(students.subList(low, mid + 1));
                            ArrayList<StudentInformation> rightArr = new ArrayList<>(students.subList(mid + 1, high + 1));
                    
                            int i = 0, j = 0, k = low;
                            while (i < leftArr.size() && j < rightArr.size()) {
                                if (leftArr.get(i).Lname.compareTo(rightArr.get(j).Lname) <= 0) {
                                    students.set(k++, leftArr.get(i++));
                                } else {
                                    students.set(k++, rightArr.get(j++));
                                }
                            }
                            while (i < leftArr.size()) students.set(k++, leftArr.get(i++));
                            while (j < rightArr.size()) students.set(k++, rightArr.get(j++));
                        }
                    
                        void swap(ArrayList<StudentInformation> students, int i, int j) {
                            StudentInformation temp = students.get(i);
                            students.set(i, students.get(j));
                            students.set(j, temp);
                        }
                    }
                    
                    public class Main {
                        private static final String FILE_NAME = "students.dat";
                    
                        public static void saveData(ArrayList<StudentInformation> students) {
                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                                out.writeObject(students);
                            } catch (IOException e) {
                                System.out.println("Error saving student data.");
                            }
                        }
                    
                        public static ArrayList<StudentInformation> loadData() {
                            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                                return (ArrayList<StudentInformation>) in.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                return new ArrayList<>();
                            }
                        }
                    
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            ArrayList<StudentInformation> students = loadData();
                            HashMap<Float, ArrayList<StudentInformation>> percentageMap = new HashMap<>();
                            StudentInfo sorter = new StudentInfo();
                    
                            while (true) {
                                System.out.println("Choose an option:");
                                System.out.println("1. Add Student(s)");
                                System.out.println("2. Sort by ID and Print");
                                System.out.println("3. Sort by Last Name and Print");
                                System.out.println("4. Sort by CGPA (Descending) and Print");
                                System.out.println("5. Filter students by percentage range");
                                System.out.println("6. Delete all data");
                                System.out.println("7. Exit");
                                int choice = sc.nextInt();
                                sc.nextLine();
                    
                                switch (choice) {
                                    case 1:
                                        System.out.println("Enter number of students to add:");
                                        int num = sc.nextInt();
                                        sc.nextLine();
                                        for (int i = 0; i < num; i++) {
                                            System.out.println("Enter ID, First Name, Last Name, CGPA:");
                                            int id = sc.nextInt();
                                            sc.nextLine();
                                            boolean exists = students.stream().anyMatch(s -> s.id == id);
                                            if (exists) {
                                                System.out.println("ID already exists. Try again.");
                                                i--;
                                                continue;
                                            }
                                            String name = sc.nextLine();
                                            String Lname = sc.nextLine();
                                            float cgpa = sc.nextFloat();
                                            students.add(new StudentInformation(name, Lname, id, cgpa));
                                        }
                                        saveData(students);
                                        break;
                                        case 2:
                                        sorter.QuickSortByID(students, 0, students.size() - 1);
                                        students.forEach(System.out::println);
                                        break;
                                    case 3:
                                        sorter.MergeSortByLastName(students, 0, students.size() - 1);
                                        students.forEach(System.out::println);
                                        break;
                                    case 4:
                                        sorter.QuickSortByCGPA(students, 0, students.size() - 1);
                                        students.forEach(System.out::println);
                                        break;
                    
                                    case 5:
                                        System.out.println("Enter percentage range (min max):");
                                        float min = sc.nextFloat();
                                        float max = sc.nextFloat();
                                        students.stream().filter(s -> s.percentage >= min && s.percentage <= max)
                                                .forEach(System.out::println);
                                        break;
                                    case 6:
                                        students.clear();
                                        saveData(students);
                                        System.out.println("All student data deleted successfully.");
                                        break;
                                    case 7:
                                        saveData(students);
                                        return;
                                }
                            }
                        }
                    }
                                       