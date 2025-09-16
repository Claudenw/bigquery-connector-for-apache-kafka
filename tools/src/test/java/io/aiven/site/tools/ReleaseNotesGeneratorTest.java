package io.aiven.site.tools;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReleaseNotesGeneratorTest {

    private static ReleaseNotesGenerator releaseNotesGenerator = new ReleaseNotesGenerator();


    @BeforeEach
    public void setup() {
        releaseNotesGenerator.configure("src/test/resources/ReleaseNotesTest");
    }
    @Test
    void entryTest() throws FileNotFoundException {
        String actual = releaseNotesGenerator.entry("1.1.1");
        assertEquals("File 1.1.1", actual);
    }

    @Test
    void allEntriesTest() throws FileNotFoundException {
        String expected =
                "File 2.0.0\n" +
                "File 1.2.0\n" +
                "File 1.1.2\n" +
                "File 1.1.1\n" +
                "File 1.1.1-stuff\n" +
                "File 1.1.0\n" +
                "File 1.0.0\n";
        String actual = releaseNotesGenerator.allEntries();
        assertEquals(expected, actual);
    }
}
