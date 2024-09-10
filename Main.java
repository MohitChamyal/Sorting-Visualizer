import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class Main extends JPanel {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static final int NUM_BARS = 200;
    private static final int BAR_WIDTH = WIDTH / NUM_BARS;
    private int[] array;
    private int swapIndex;
    private int compareIndex;
    private int currentIndex;
    private Timer timer;

    Main() {
        array = new int[NUM_BARS];
        for (int i = 0; i < NUM_BARS; i++) {
            array[i] = i + 1;
        }
        shuffleArray();

        JButton insertionSort = createButton("Insertion Sort", new Color(102, 153, 255), e -> new Thread(this::insertionSort).start());
        JButton selectionSort = createButton("Selection Sort", new Color(255, 102, 102), e -> new Thread(this::selectionSort).start());
        JButton bubbleSort = createButton("Bubble Sort", new Color(255, 255, 102), e -> new Thread(this::bubbleSort).start());
        JButton heapSort = createButton("Heap Sort", new Color(102, 255, 102), e -> new Thread(this::heapSort).start());
        JButton mergeSort = createButton("Merge Sort", new Color(153, 102, 255), e -> new Thread(() -> mergeSort(0, NUM_BARS - 1)).start());
        JButton quickSort = createButton("Quick Sort", new Color(255, 153, 102), e -> new Thread(() -> quickSort(0, NUM_BARS - 1)).start());
        JButton bucketSort = createButton("Bucket Sort", new Color(102, 255, 255), e -> new Thread(this::bucketSort).start());
        JButton countSort = createButton("Count Sort", new Color(255, 204, 153), e -> new Thread(this::countSort).start());
        JButton radixSort = createButton("Radix Sort", new Color(153, 255, 204), e -> new Thread(this::radixSort).start());
        JButton resetButton = createButton("Reset", new Color(255, 255, 255), e -> resetArray());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 5, 5, 5));
        buttonPanel.add(selectionSort);
        buttonPanel.add(bubbleSort);
        buttonPanel.add(insertionSort);
        buttonPanel.add(mergeSort);
        buttonPanel.add(quickSort);
        buttonPanel.add(heapSort);
        buttonPanel.add(bucketSort);
        buttonPanel.add(countSort);
        buttonPanel.add(radixSort);
        buttonPanel.add(resetButton);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        JFrame frame = new JFrame("Sorting Algorithm Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
    }

    private JButton createButton(String text, Color background, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(background);
        button.setForeground(Color.BLACK);
        button.addActionListener(action);
        return button;
    }

    private void shuffleArray() {
        Random rand = new Random();
        for (int i = 0; i < NUM_BARS; i++) {
            int randomIndex = rand.nextInt(NUM_BARS);
            int temp = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = temp;
        }
    }

    private void resetArray() {
        shuffleArray();
        currentIndex = -1;
        swapIndex = -1;
        compareIndex = -1;
        repaint();
    }

    private void insertionSort() {
        timer = new Timer(10, new ActionListener() {
            int i = 1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (i < NUM_BARS) {
                    int key = array[i];
                    int j = i - 1;
                    while (j >= 0 && array[j] > key) {
                        array[j + 1] = array[j];
                        j--;
                    }
                    array[j + 1] = key;
                    currentIndex = i;
                    repaint();
                    i++;
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void selectionSort() {
        timer = new Timer(10, new ActionListener() {
            int i = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (i < NUM_BARS - 1) {
                    int minIndex = i;
                    for (int j = i + 1; j < NUM_BARS; j++) {
                        if (array[j] < array[minIndex]) {
                            minIndex = j;
                        }
                    }
                    int temp = array[minIndex];
                    array[minIndex] = array[i];
                    array[i] = temp;
                    swapIndex = minIndex;
                    compareIndex = i;
                    repaint();
                    i++;
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void bubbleSort() {
        timer = new Timer(10, new ActionListener() {
            int i = 0;
            int j = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (i < NUM_BARS - 1) {
                    if (j < NUM_BARS - i - 1) {
                        if (array[j] > array[j + 1]) {
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                            swapIndex = j;
                            compareIndex = j + 1;
                            repaint();
                        }
                        j++;
                    } else {
                        j = 0;
                        i++;
                    }
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void heapSort() {
        new Thread(() -> {
            buildHeap();
            for (int i = NUM_BARS - 1; i > 0; i--) {
                int temp = array[0];
                array[0] = array[i];
                array[i] = temp;
                heapify(0, i);
                swapIndex = 0;
                compareIndex = i;
                repaint();
                delay();
            }
        }).start();
    }

    private void buildHeap() {
        for (int i = NUM_BARS / 2 - 1; i >= 0; i--) {
            heapify(i, NUM_BARS);
        }
    }

    private void heapify(int index, int size) {
        int largest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        if (left < size && array[left] > array[largest]) {
            largest = left;
        }
        if (right < size && array[right] > array[largest]) {
            largest = right;
        }
        if (largest != index) {
            int temp = array[index];
            array[index] = array[largest];
            array[largest] = temp;
            heapify(largest, size);
        }
    }

    private void mergeSort(int low, int high) {
        if (low < high) {
            int mid = low + (high - low) / 2;
            mergeSort(low, mid);
            mergeSort(mid + 1, high);
            merge(low, mid, high);
        }
    }

    private void merge(int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int i = low;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= high) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = array[i++];
        }
        while (j <= high) {
            temp[k++] = array[j++];
        }
        for (i = low; i <= high; i++) {
            array[i] = temp[i - low];
        }
        currentIndex = high;
        repaint();
        delay();
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            currentIndex = j;
            compareIndex = high;
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
            repaint();
            delay();
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    public void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);

            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    private void bucketSort() {
        int max = getMax();
        int[] buckets = new int[max + 1];
        for (int i = 0; i < NUM_BARS; i++) {
            buckets[array[i]]++;
        }
        int index = 0;
        for (int i = 0; i <= max; i++) {
            while (buckets[i] > 0) {
                array[index++] = i;
                buckets[i]--;
                currentIndex = index;
                repaint();
                delay();
            }
        }
    }

    private void radixSort() {
        int max = getMax();
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countSortWithExp(exp);
            repaint();
            delay();
        }
    }

    private void countSortWithExp(int exp) {
        int[] output = new int[NUM_BARS];
        int[] count = new int[10];
        Arrays.fill(count, 0);
        for (int i = 0; i < NUM_BARS; i++) {
            count[(array[i] / exp) % 10]++;
        }
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        for (int i = NUM_BARS - 1; i >= 0; i--) {
            output[count[(array[i] / exp) % 10] - 1] = array[i];
            count[(array[i] / exp) % 10]--;
            repaint();
            delay();
        }
        System.arraycopy(output, 0, array, 0, NUM_BARS);
        currentIndex = NUM_BARS - 1;
        repaint();
        delay();
    }

    private void countSort() {
        int max = getMax();
        int[] output = new int[NUM_BARS];
        int[] count = new int[max + 1];
        Arrays.fill(count, 0);
        for (int i = 0; i < NUM_BARS; i++) {
            count[array[i]]++;
        }
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }
        for (int i = NUM_BARS - 1; i >= 0; i--) {
            output[count[array[i]] - 1] = array[i];
            count[array[i]]--;
            repaint();
            delay();
        }
        System.arraycopy(output, 0, array, 0, NUM_BARS);
        currentIndex = NUM_BARS - 1;
        repaint();
        delay();
    }

    private int getMax() {
        int max = array[0];
        for (int i = 1; i < NUM_BARS; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private void delay() {
        try {
            Thread.sleep(10); // Adjust this for smoothness
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < NUM_BARS; i++) {
            if (i == swapIndex) {
                g.setColor(Color.RED);
            }
            else if (i == compareIndex) {
                g.setColor(Color.GREEN);
            }
            else if (i == currentIndex) {
                g.setColor(Color.BLUE);
            }
            else {
                g.setColor(Color.BLACK);
            }
            g.fillRect(i * BAR_WIDTH, HEIGHT - array[i] * (HEIGHT / NUM_BARS), BAR_WIDTH, array[i] * (HEIGHT / NUM_BARS));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
