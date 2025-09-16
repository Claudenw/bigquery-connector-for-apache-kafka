package io.aiven.site.tools;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.semver4j.Semver;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReleaseNotesGenerator {
    private final Map<Semver, File> fileMap;

    public ReleaseNotesGenerator(File dir) {
        FileFilter fileFilter = WildcardFileFilter.builder().setWildcards("*.txt").get();
        File[] files = dir.listFiles(fileFilter);
        fileMap = new TreeMap<>();
        if (files != null) {
            for (File f : files) {
                Semver version = Semver.parse(f.getName().substring(0, f.getName().lastIndexOf('.')));
                fileMap.put(version, f);
            }
        }
    }

    public final String entry(String version) throws FileNotFoundException {
        Semver semver = Semver.parse(version);
        File f = fileMap.get(semver);
        if (f == null) {
            throw new FileNotFoundException(version + ".txt");
        }
        return String.join( "\n", IOUtils.readLines(new FileInputStream(f), Charset.defaultCharset()));
    }

    public final String allEntries() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (File f : fileMap.values()) {
            String contents = String.join( "\n", IOUtils.readLines(new FileInputStream(f), Charset.defaultCharset()));
            sb.append(contents).append('\n');
        }
        return sb.toString();
        fileMap.values().stream().map(f -> Files.readString(f.toPath())).collect(Collectors.joining("\n\n"));
        for (File f : fileMap.values()) {

        }
    }
}
