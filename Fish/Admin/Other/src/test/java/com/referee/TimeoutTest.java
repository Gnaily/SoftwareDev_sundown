package com.referee;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * Test class for demonstrating how the Timeout works in the Referee class
 */
public class TimeoutTest {

  // Note: This test is commented out since it takes 60 seconds to fail - if you would like this
  // test to run, just uncomment it. This demonstrates the functionality of our timeout for
  // when we call on players to get an action

  /*
  @Test(expected = TimeoutException.class)
  public void testTimeoutThrowsException() throws TimeoutException, InterruptedException {
    this.sleepsFor65Seconds();
    System.out.println("done");
  }
   */


  private void sleepsFor65Seconds() throws TimeoutException, InterruptedException {
    Callable<Integer> task = () -> {
      Thread.sleep(65000);
      return 1;
    };
    HexReferee.communicateWithPlayer(task);
  }
}
