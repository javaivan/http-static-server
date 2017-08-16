package com.ivanmix.server;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

public class HelperTest {

    private String folderUrl;
    private String nonexistentFileName;
    private String existingFileName;

    @BeforeClass
    public void before() throws Exception {
        String url = Server.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        this.folderUrl = url.substring(1, url.length());
        this.nonexistentFileName = UUID.randomUUID().toString();
        this.existingFileName = UUID.randomUUID().toString();
        Path file = Paths.get(folderUrl + existingFileName);
        Files.write(file, Arrays.asList(existingFileName), Charset.forName("UTF-8"));
    }

    @AfterClass
    public void after() throws Exception {
        File file = new File(folderUrl + existingFileName);
        file.delete();
    }

    @Test
    public void getFolderUrl() {
        Assert.assertEquals(Helper.getFolderUrl(), folderUrl);
    }

    @Test(expectedExceptions = IOException.class)
    public void getBodyNonexistentFile() throws Exception {
        Helper.getBody(nonexistentFileName);
    }

    @Test
    public void getBodyExistingFileName() throws Exception {
        Assert.assertEquals(Helper.getBody(existingFileName).trim(), existingFileName);
    }

    @Test
    public void getErrorPage() {
        Assert.assertNotNull(Helper.getErrorPage());
    }
}
