package top.gabin.oa.think.equal;

import org.junit.Test;

/**
 * Created by linjiabin on 16/4/5.
 */
public class DogEqualTest {

    @Test
    public void testEqual() {
        Dog dog = new Dog();
        dog.setAge(11);
        dog.setName("dav");
        Dog dog2 = new Dog();
        System.out.println(dog);
        assert !dog.equals(dog2);
    }
}
