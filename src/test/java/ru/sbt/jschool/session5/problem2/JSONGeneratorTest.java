package ru.sbt.jschool.session5.problem2;

import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 */
public class JSONGeneratorTest {
    @Test
    public void testJSONGeneratorPrimitive() throws Exception {
        doTest("0");
    }

    @Test
    public void testJSONGeneratorCollection() throws Exception {
        doTest("1");
    }

    @Test
    public void testJSONGeneratorDate() throws Exception {
        doTest("2");
    }

    @Test
    public void testJSONGeneratorNested() throws Exception {
        doTest("3");
    }

    @Test
    public void testJSONGeneratorPrivate() throws Exception {
        doTest("4");
    }

    @Test
    public void testJSONGeneratorWrapper() throws Exception {
        doTest("5");
    }

    private void doTest(String num) throws Exception {
        JSONGenerator generator = new JSONGenerator();

        String output = generator.generate(InputTestObjects.inputObject(num));
        System.out.println(output);
        try (Scanner actualOutput = new Scanner(output);
             Scanner expectedOutput = new Scanner(JSONGeneratorTest.class.getResourceAsStream("/output" + num + ".txt"))) {

            while (expectedOutput.hasNextLine()) {
                String expected = expectedOutput.nextLine();

                if (!actualOutput.hasNextLine())
                    throw new AssertionError("Expected output is \"" + expected + "\", but actual output is empty!");

                String actual = actualOutput.nextLine();

                assertEquals(expected, actual);
            }
        }
    }
}
