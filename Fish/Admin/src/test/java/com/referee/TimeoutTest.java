package com.referee;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * Test class for demonstrating how the Timeout works in the Referee class
 */
public class TimeoutTest {

  @Test(expected = TimeoutException.class)
  public void testTimeoutThrowsException() throws TimeoutException, InterruptedException {
    this.sleepsFor45Seconds();
    System.out.println("done");
  }


  private void sleepsFor45Seconds() throws TimeoutException, InterruptedException {
    Callable<Integer> task = () -> {
      Thread.sleep(6000);
      return 1;
    };
    HexReferee.communicateWithPlayer(task);
  }
}
