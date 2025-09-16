package io.aiven.site.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.semver4j.Semver;

public class ReleaseNotesGenerator {
  private final Map<Semver, File> fileMap;

  public ReleaseNotesGenerator() {
    fileMap = new TreeMap<>((o1, o2) -> o2.compareTo(o1));
  }

  public final void configure(final String directory) {
    File dir = new File(directory);
    FileFilter fileFilter = WildcardFileFilter.builder().setWildcards("*.txt").get();
    File[] files = dir.listFiles(fileFilter);
    fileMap.clear();
    if (files != null) {
      for (File f : files) {
        Semver version = Semver.parse(f.getName().substring(0, f.getName().lastIndexOf('.')));
        if (version != null) {
          fileMap.put(version, f);
        }
      }
    }
  }

  public final String entry(final String version) throws FileNotFoundException {
    Semver semver = Semver.parse(version);
    File f = fileMap.get(semver);
    if (f == null) {
      throw new FileNotFoundException(version + ".txt");
    }
    return String.join("\n", IOUtils.readLines(new FileInputStream(f), Charset.defaultCharset()));
  }

  public final String allEntries() throws FileNotFoundException {
    StringBuilder sb = new StringBuilder();
    for (File f : fileMap.values()) {
      String contents = String.join("\n", IOUtils.readLines(new FileInputStream(f), Charset.defaultCharset()));
      sb.append(contents).append('\n');
    }
    return sb.toString();
  }
}
