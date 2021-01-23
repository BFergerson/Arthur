package com.codebrig.arthur

import org.bblfsh.client.BblfshClient
import org.junit.AfterClass
import org.junit.BeforeClass

abstract class ArthurTest {

    static BblfshClient client

    @BeforeClass
    static void setUp() {
        client = new BblfshClient("localhost", 9432, Integer.MAX_VALUE)
    }

    @AfterClass
    static void tearDown() {
        client.close()
    }
}
