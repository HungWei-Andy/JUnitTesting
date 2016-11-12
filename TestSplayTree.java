package test;

import java.io.*;
import java.util.*;
import org.junit.*;
import junit.framework.TestCase;

public class TestSplayTree extends TestCase{
    private final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errStream = new ByteArrayOutputStream();
    private PrintStream stdout_ = System.out;
    private PrintStream stderr_ = System.err;
    private final Random random = new Random();;
    private static final String CLEARLINE = "\u001b[1A\u001b[2K";
    private static final int LINEAR_TIME_LOOP_N = 1000001;
    private static final int LOG_TIME_LOOP_N = 1000001;
    private static final int DATA_LMT = 5000000;
    public static final int DATA_FILE_N = 0;

    public void setupStd() {
        System.setOut(new PrintStream(this.outStream));
        System.setErr(new PrintStream(this.errStream));
    }

    public void cleanStd() {
        System.setOut(stdout_);
        System.setErr(this.stderr_);
    }

    @Test
    public void testEmpty() {
        SplayTree splayTree = new SplayTree();
        
        stdout_.println("Test Empty...");
        Assert.assertEquals((long)splayTree.getSize(), (long)0);
        Assert.assertNull((Object)splayTree.search(10));
        Assert.assertNull((Object)splayTree.delete(10));
        Assert.assertFalse((boolean)splayTree.contains(10));
        
        Assert.assertEquals((long)splayTree.getSize(), (long)0);
        Assert.assertNull((Object)splayTree.search(0));
        Assert.assertNull((Object)splayTree.delete(0));
        Assert.assertFalse((boolean)splayTree.contains(0));
        Assert.assertEquals((long)splayTree.getSize(), (long)0);
        Assert.assertNull((Object)splayTree.search(-1));
        Assert.assertNull((Object)splayTree.delete(-1));
        Assert.assertFalse((boolean)splayTree.contains(-1));
        Assert.assertEquals((long)splayTree.getSize(), (long)0);
        Assert.assertNull((Object)splayTree.search(1));
        Assert.assertNull((Object)splayTree.delete(1));
        Assert.assertFalse((boolean)splayTree.contains(1));
        Assert.assertEquals((long)splayTree.getSize(), (long)0);

        setupStd();
        splayTree.printTreeInOrder();
        Assert.assertEquals((Object)outStream.toString().trim(), (Object)"");
        outStream.reset();
        splayTree.printTreePreOrder();
        Assert.assertEquals((Object)outStream.toString().trim(), (Object)"");
        outStream.reset();
        splayTree.printTreeInOrder();
        Assert.assertEquals((Object)outStream.toString().trim(), (Object)"");
        outStream.reset();
        cleanStd();
    }

    @Test(expected=NullPointerException.class)
    public void testEmptyGetMin() throws Exception {
        stdout_.println("Test EmptyGetMin...");
        SplayTree splayTree = new SplayTree();
        splayTree.getMinimum();
    }

    @Test
    public void testSuccessor() throws Exception {
        SplayTree splayTree = new SplayTree();
        ArrayList<Integer> vals = new ArrayList<Integer>();
        ArrayList<AbstractBinarySearchTree.Node> nodes = new ArrayList<AbstractBinarySearchTree.Node>();
        HashSet<Integer> set = new HashSet<Integer>();
        
        testInsert(new File("test_data/data_10_6"), splayTree, vals, nodes, set);
        int N = vals.size();
        
        Integer[] sorted_vals = new Integer[vals.size()];
        sorted_vals = vals.toArray(sorted_vals);
        Arrays.sort(sorted_vals);

        stdout_.println("Test Successor start...\n");
        for(int i=0; i < LOG_TIME_LOOP_N; i++){
            if (i % 100 == 0) {
                stdout_.print("\u001b[1A\u001b[2K");
                stdout_.printf("testing: %d/%d\n", i + 1, LOG_TIME_LOOP_N);
            }

            int cur_idx = Math.abs(this.random.nextInt()) % N;
            int cur_val = vals.get(cur_idx);
            int suc_idx = Arrays.binarySearch(sorted_vals, cur_val) + 1;

            if (suc_idx != N) {
                Assert.assertEquals(sorted_vals[suc_idx].intValue(), splayTree.getSuccessor(cur_val));
            }
        }
        stdout_.println("Test Successor Completed\n");
    }

    @Test(expected=NullPointerException.class)
    public void testMaxSuccessor() throws Exception {
        SplayTree splayTree = new SplayTree();
        ArrayList<Integer> vals = new ArrayList<Integer>();
        ArrayList<AbstractBinarySearchTree.Node> nodes = new ArrayList<AbstractBinarySearchTree.Node>();
        HashSet<Integer> set = new HashSet<Integer>();
        
        testInsert(new File("test_data/data_10_6"), splayTree, vals, nodes, set);
        splayTree.getSuccessor(Collections.max(vals).intValue());
    }

    @Test(timeout=3000)
    public void testTime() throws Exception {
        stdout_.println("Test Time...");
        BufferedReader bufferedReader = new BufferedReader(new FileReader("test_data/data_10_6"));
        ArrayList<Integer> vals = new ArrayList<Integer>();
        SplayTree splayTree = new SplayTree();
        String line;
  
        while((line = bufferedReader.readLine()) != null)
            vals.add(Integer.parseInt(line));

        int N = vals.size();
        stdout_.println("Data Read");
        
        for(int i = 0; i < N; i++)
            splayTree.insert(vals.get(i).intValue());
        
        for(int i = 0; i < N; i++)
            splayTree.search(vals.get(i).intValue());

        for(int i = 0; i < N; i++)
            splayTree.search(vals.get(i).intValue());
    }

    @Test
    public void testModify() throws Exception {
        stdout_.println("Test Modify...");

        for(int i = 0; i < DATA_FILE_N; i++){
            stdout_.println("Test Modify: test_data/data_10_6_" + i);
            try {
                ArrayList<Integer> vals = new ArrayList<Integer>();
                ArrayList<AbstractBinarySearchTree.Node> nodes = new ArrayList<AbstractBinarySearchTree.Node>();
                HashSet<Integer> set = new HashSet<Integer>(5000000);
                SplayTree splayTree = new SplayTree();

                this.testInsert(new File("test_data/data_10_6_" + i), splayTree, vals, nodes, set);
                this.testSearchInValues(splayTree, vals, nodes, set);
                this.testSearchOutValues(splayTree, vals, nodes, set);
                this.testDelete(splayTree, vals, nodes, set);
            }
            catch (Exception e) {
                this.stderr_.println(e.toString());
                this.stderr_.println("Error Occured");
            }
        }
    }

    public void testInsert(File file, SplayTree splayTree, List<Integer> vals, List<AbstractBinarySearchTree.Node> nodes, Set<Integer> set) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        int N = 0;
        AbstractBinarySearchTree.Node node = null;

        String line;
        while((line=bufferedReader.readLine()) != null){
            vals.add(Integer.parseInt(line));
        }
        
        N = vals.size();
        stdout_.println("Data Read..." + N + "\n");

        stdout_.println("Insertion Test Start...\n");
        for(int i = 0; i < N; i++){
            int val = vals.get(i);
            set.add(new Integer(val));
            Assert.assertEquals((Object)splayTree.contains(val), (Object)false);
            
            node = splayTree.search(val);
            Assert.assertEquals((Object)node, (Object)null);
            
            node = splayTree.insert(val);
            Assert.assertEquals((long)node.value.intValue(), (long)val);
            Assert.assertEquals((Object)node.parent, (Object)null);
            Assert.assertEquals((long)splayTree.getSize(), (long)(i + 1));
            Assert.assertEquals((Object)splayTree.contains(val), (Object)true);
            nodes.add(node);
            
            if (i % 100 == 0) {
                stdout_.print("\u001b[1A\u001b[2K");
                stdout_.printf("Inserting: %d/%d...\n", i + 1, N);
            }
        }

        stdout_.println("Insertion Test Completed\n");
    }

    public void testSearchInValues(SplayTree splayTree, List<Integer> vals, List<AbstractBinarySearchTree.Node> nodes, Set<Integer> set) throws Exception {
        int N = vals.size();
        AbstractBinarySearchTree.Node node = null;
        AbstractBinarySearchTree.Node node2 = null;
        stdout_.println("Search Inside Tree Test Start...\n");
        
        for(int i = 0; i < N; i++){
            int val = vals.get(i);
            node = splayTree.search(val);
            node2 = nodes.get(i);
            Assert.assertSame((Object)node, (Object)node2);
            Assert.assertEquals((Object)node.equals((Object)node2), (Object)true);
            Assert.assertEquals((long)node.value.intValue(), (long)val);
            Assert.assertEquals((Object)node.parent, (Object)null);
            Assert.assertEquals((long)splayTree.getSize(), (long)N);
            Assert.assertEquals((Object)splayTree.contains(val), (Object)true);
            
            if (i % 100 == 0) {
                stdout_.print("\u001b[1A\u001b[2K");
                stdout_.printf("searching: %d/%d\n", i + 1, N);
            }
        }
        stdout_.println("Search Inside Tree Test Completed\n");
    }

    public void testSearchOutValues(SplayTree splayTree, List<Integer> vals, List<AbstractBinarySearchTree.Node> nodes, Set<Integer> set) throws Exception {
        int N = vals.size(), val;
        AbstractBinarySearchTree.Node node = null;
        stdout_.println("Search Test Start...\n");
        
        for(int i = 0; i < LOG_TIME_LOOP_N; i++){
            int dice = this.random.nextInt() % 6;
            if (dice != 0) {
                val = this.random.nextInt();
                while(set.contains(val)){
                    val = this.random.nextInt();
                }
                Assert.assertEquals((Object)splayTree.search(val), (Object)null);
                Assert.assertEquals((Object)splayTree.contains(val), (Object)false);
            } else {
                int idx = Math.abs(this.random.nextInt()) % N;
                val = vals.get(idx);
                node = splayTree.search(val);
                Assert.assertSame((Object)node, (Object)nodes.get(idx));
                Assert.assertEquals((long)node.value.intValue(), (long)val);
                Assert.assertEquals((Object)node.parent, (Object)null);
                Assert.assertEquals((Object)splayTree.contains(val), (Object)true);
            }
            Assert.assertEquals((long)splayTree.getSize(), (long)N);
            
            if (i % 100 == 0) {
                stdout_.print("\u001b[1A\u001b[2K");
                stdout_.printf("index: %d/%d\n", i + 1, 1000001);
            }
        }
        stdout_.println("Search Test Completed\n");
    }

    public void testDelete(SplayTree splayTree, List<Integer> vals, List<AbstractBinarySearchTree.Node> nodes, Set<Integer> set) throws Exception {
        AbstractBinarySearchTree.Node node = null;
        int N = vals.size(), val;

        stdout_.println(N);
        
        stdout_.println("Delete Null Test Start...\n");
        for(int i = 0; i < LINEAR_TIME_LOOP_N; i++){
            val = this.random.nextInt();
            while(set.contains(val)){
                val = this.random.nextInt();
            }
            
            Assert.assertEquals((Object)splayTree.search(val), (Object)null);
            Assert.assertEquals((Object)splayTree.contains(val), (Object)false);
            Assert.assertEquals((Object)splayTree.delete(val), (Object)null);
            
            if (i % 100 == 0) {
                stdout_.print("\u001b[1A\u001b[2K");
                stdout_.printf("Deletion: %d/%d\n", i + 1, 1000001);
            }
        }
        stdout_.println("Delete Null Test Completed...\n");
        stdout_.println("Delete Test Start...\n");
        
        for(int i = N-1; i >= 0; i--){
            val = vals.get(i);
            Assert.assertTrue((boolean)splayTree.contains(val));
            
            node = splayTree.delete(val);
            Assert.assertEquals((Object)splayTree.contains(val), (Object)false);
            Assert.assertEquals((Object)splayTree.search(val), (Object)null);
            Assert.assertEquals((long)splayTree.getSize(), (long)i);
            if ((N - i - 1) % 100 == 0) {
                stdout_.print("\u001b[1A\u001b[2K");
                stdout_.printf("Deletion: %d/%d\n", N - i, N);
            }
        }
        stdout_.println("Delete Test Completed...\n");
        
        for(int i = 0; i < 1000; i++){
            Assert.assertNull(splayTree.delete(this.random.nextInt()));
        }
    }
}
