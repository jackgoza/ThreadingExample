
import java.util.*;

/**
 *
 * @author jack
 *
 */
class thread3 implements Runnable {

    private static int x, y, z;

    public static synchronized void f() {
	x = x + 1;
	y = y + 1;
	z = z + x - y;
    }

    public static void printValues() {
	System.out.println("x = " + x);
	System.out.println("y = " + y);
	System.out.println("z = " + z);
    }

    @Override
    public void run() {
	for (int i = 0; i < 100000000; i++) { // 100,000,000
	    f();
	}
    }
}

class thread2 implements Runnable {

    private static int x, y, z;

    public static void f() {
	x = x + 1;
	y = y + 1;
	z = z + x - y;
    }

    public static void printValues() {
	System.out.println("x = " + x);
	System.out.println("y = " + y);
	System.out.println("z = " + z);
    }

    @Override
    public void run() {
	for (int i = 0; i < 100000000; i++) { // 100,000,000
	    f();
	}
    }
}

public class thread1 implements Runnable {

    private static int x, y, z;
    public static int[][] matrix = new int[3][100000000]; // if you run out of memory during execution, reduce the second dimension

    private static Random rand = new Random();

    public static void f() {
	x = x + 1;
	y = y + 1;
	z = z + x - y;
    }

    public static void printValues() {
	System.out.println("x = " + x);
	System.out.println("y = " + y);
	System.out.println("z = " + z);
    }

    @Override
    public void run() {
	for (int i = 0; i < 200000000; i++) { // 200,000,000
	    f();
	}
    }

    // Initialize matrix with random numbers
    public static void setMatrix() {
	for (int x = 0; x < matrix.length; x++) {
	    for (int y = 0; y < matrix[x].length; y++) {
		int randomNum = rand.nextInt(200); // 0 - 199
		matrix[x][y] = randomNum;

	    }
	}
    }

    public static void main(String[] args) {

	Thread t1, t2;

	setMatrix();

	long startTime = System.nanoTime();

	(t1 = new Thread(new thread3())).start(); // Change this to test other thread types
	(t2 = new Thread(new thread3())).start(); // This too.

	try {
	    t1.join();
	    t2.join();

	    long endTime = System.nanoTime();

	    thread3.printValues();

	    System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
	}
	catch (Exception e) {
	    e.printStackTrace();
	}

	int sum = 0;

	startTime = System.nanoTime();

	for (int x = 0; x < matrix.length; x++) {
	    for (int y = 0; y < matrix[x].length; y++) {
		sum = sum + (int) Math.log(matrix[x][y]);
	    }
	}
	long endTime = System.nanoTime();
	System.out.println("");
	System.out.println("One thread sum:  " + sum);
	System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");

	sum = 0;
	int[] rowSums = new int[matrix.length];

	class thread4 implements Runnable {

	    private int matrixRow;

	    public thread4(int x) {
		matrixRow = x;
	    }

	    public void run() {
		int rowSum = 0;
		for (int j = 0; j < matrix[matrixRow].length; j++) {
		    rowSum += (int) Math.log(matrix[matrixRow][j]);
		}

		rowSums[matrixRow] = rowSum;

	    }

	};

	Thread tArray[] = new Thread[matrix.length];

	startTime = System.nanoTime();
	for (int i = 0; i < matrix.length; i++) {
	    tArray[i] = new Thread(new thread4(i));
	    tArray[i].start();
	}

	try {
	    for (int i = 0; i < matrix.length; i++) {
		tArray[i].join();
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}

	for (int i = 0; i < matrix.length; i++) {
	    sum += rowSums[i];
	}
	endTime = System.nanoTime();
	System.out.println("");
	System.out.println("Multiple thread sum:  " + sum);
	System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
    }
}
