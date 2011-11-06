package net.sarcommand.swingextensions.test.binding;

import junit.framework.TestCase;
import net.sarcommand.swingextensions.binding.Keypath;
import net.sarcommand.swingextensions.binding.KeypathAccessException;
import net.sarcommand.swingextensions.binding.KeypathElement;
import net.sarcommand.swingextensions.binding.KeypathElementCache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Test class for the keypath related stuff.
 */
public class KeypathTest extends TestCase {
    protected Person _alice;
    protected Person _bob;
    protected Person _charlie;
    protected Person _doreen;
    protected Person _ezechiel;

    protected void setUp() throws Exception {
        super.setUp();
        _doreen = new Person("Doreen", null, null, 84, 0);
        _ezechiel = new Person("Ezechiel", null, null, 93, 312);
        _alice = new Person("Alice", null, _doreen, 43, 0);
        _bob = new Person("Bob", _ezechiel, null, 52, 1000);
        _charlie = new Person("Charlie", _bob, _alice, 2, 0);
    }

    /**
     * Testing a public getter and no setter.
     */
    public void testKeypathElement0() {
        final KeypathElement ageElement = new KeypathElement(Person.class, "age", true);
        assertTrue("Obtained wrong getter for father propery", ageElement.getAccessorForGet() instanceof Method);
        assertTrue("Obtained wrong setter for father propery", ageElement.getAccessorForSet() == null);
        assertTrue("canGet incorrectly returned false", ageElement.canPerformGet());
        assertFalse("canSet incorrectly returned true", ageElement.canPerformSet());
        assertEquals("Could not invoke getter for age propery", ageElement.get(_doreen), 84);
    }

    /**
     * Testing a protected accessor method which has priority over a private field
     */
    public void testKeypathElement2() {
        final KeypathElement fatherElement = new KeypathElement(Person.class, "father", true);
        assertTrue("Obtained wrong getter for father propery", fatherElement.getAccessorForGet() instanceof Method);
        assertTrue("Obtained wrong setter for father propery", fatherElement.getAccessorForSet() instanceof Field);
        assertTrue("canGet incorrectly returned false", fatherElement.canPerformGet());
        assertTrue("canSet incorrectly returned false", fatherElement.canPerformSet());
        assertEquals("Could not invoke getter for father propery", fatherElement.get(_charlie), _bob);
        fatherElement.set(_bob, _doreen);
        assertEquals("Failed give bob a mommy", fatherElement.get(_bob), _doreen);
    }

    /**
     * Testing the priority of a public field over a protected accessor.
     */
    public void testKeypathElement3() {
        final KeypathElement incomeElement = new KeypathElement(Person.class, "income", true);
        assertTrue("Obtained wrong getter for income propery", incomeElement.getAccessorForGet() instanceof Field);
        assertTrue("Obtained wrong setter for income propery", incomeElement.getAccessorForSet() instanceof Field);
        assertTrue("canGet incorrectly returned false", incomeElement.canPerformGet());
        assertTrue("canSet incorrectly returned false", incomeElement.canPerformSet());
        assertEquals("Could not invoke getter for income propery", incomeElement.get(_bob), 1000);
    }

    /**
     * Tests the KeypathElementCache.
     */
    public void testKeypathElementCache() {
        final KeypathElement element0 = KeypathElementCache.getElement(Person.class, "father", true);
        assertNotNull(element0);
        assertTrue(element0.canPerformGet());
        final KeypathElement element1 = KeypathElementCache.getElement(Person.class, "father", true);
        assertTrue(element0 == element1);
    }

    /**
     * Tests get and set for simple, one-element keypaths.
     */
    public void testKeypathAccess0() {
        final Keypath keypath = new Keypath("father");
        assertTrue("Could not resolve keypath", keypath.canResolve(_charlie));
        assertEquals("Could not perform get on keypath", _bob, keypath.get(_charlie));
        keypath.set(_charlie, _doreen);
        assertEquals("Could not invoke setter on keypath", _doreen, _charlie.getFather());
    }

    /**
     * Testing access using complex keypaths.
     */
    public void testKeypathAccess1() {
        final Keypath<Integer> keypath0 = new Keypath<Integer>("father.father.income");
        assertFalse(keypath0.canResolve(_bob));
        assertTrue(keypath0.canResolve(_charlie));
        assertEquals(312, (int) keypath0.get(_charlie));
        assertNull(keypath0.get(_bob));
        try {
            keypath0.set(_bob, 12);
            fail();
        } catch (KeypathAccessException e) {
            /* Expected */
        }

        keypath0.set(_charlie, 6666);
        assertEquals(6666, _ezechiel.income);
    }

    protected static class Person {
        private String _name;
        private Person father;
        private Person _mother;
        private int _age;
        public int income;

        public Person(final String name, final Person father, final Person mother, final int age, final int income) {
            _name = name;
            this.father = father;
            _mother = mother;
            _age = age;
            this.income = income;
        }

        protected Person getFather() {
            return father;
        }

        public int getAge() {
            return _age;
        }

        protected int getIncome() {
            return income + 10;
        }
    }
}
