package com.twi.awayday2014.models;

import junit.framework.TestCase;


public class TweeterTest extends TestCase {

    public void test() {
        Tweeter tweeter = new Tweeter();
        boolean isAuthenticated = tweeter.auth("thotegowda.gr@gmail.com", "thote@123");
        assertTrue(isAuthenticated);
    }

}