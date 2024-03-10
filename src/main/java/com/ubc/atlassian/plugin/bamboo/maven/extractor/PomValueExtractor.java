package com.ubc.atlassian.plugin.bamboo.maven.extractor;

public interface PomValueExtractor {

    String getValue(String property) throws NoSuchPropertyException;
}
