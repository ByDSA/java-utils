package es.danisales.io.search.rules;

import java.io.File;

public class ExtensionRule implements FinderRule {
    String extension;

    public ExtensionRule(String ext) {
        extension = ext;
    }

    @Override
    public boolean check(File f) {
        return f.getName().endsWith("." + extension);
    }
}