package com.jia.reggie.mock;

import org.junit.Test;

import java.util.UUID;

/**
 * @author kk
 */
public class FileTest {
    @Test
    public void test(){
        String originalFilename = "sfdadsf.jpg";
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid+suffix;
        System.out.println(fileName);
    }
}
