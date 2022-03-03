import com.example.demo.entity.Constant;
import com.example.demo.entity.TestObject;
import org.openjdk.jol.info.ClassLayout;

import java.util.Arrays;

/**
 * @author ChangLF 2022-02-24
 */
public class TestClass {
    public static void main(String[] args) {
        int i = 1;
        Integer a = i;
        Integer b = 1;
        Integer c = Constant.ONE;
        Integer d = new Integer(1);
        Integer e = new Integer(1);
        Integer f = Integer.valueOf(1);
        Integer g = Integer.valueOf(1);
        TestObject testObject = new TestObject();
//        Integer anInt = testObject.getAnInt();
        TestObject testObject1 = new TestObject();



        Long x = null;
        Object y = null;
        System.out.println(x == y);

        System.out.println(i);
    }
}
