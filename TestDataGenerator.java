package test;

import java.io.*;
import java.util.*;

public class TestDataGenerator {
    protected static void generate(String filename, int size){
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(filename));
            Set<Integer> set = new HashSet<Integer>();
            Random random = new Random();

            for (int cnt = 0; cnt < size; cnt++) {
                int val = random.nextInt();
                while (set.contains(val)) {
                    val = random.nextInt();
                }
                set.add(val);
                output.write(Integer.toString(val));
                output.newLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        generate("test_data/data_10_4", 10001);
        generate("test_data/data_10_6", 1000001);
        for(int i = 0; i < 10; i++){
            generate("test_data/data_10_6_"+i, 1000001);
        }
    }
}
