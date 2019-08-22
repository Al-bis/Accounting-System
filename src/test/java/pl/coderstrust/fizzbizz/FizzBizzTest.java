package pl.coderstrust.fizzbizz;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FizzBizzTest {

  @Test
  public void shouldReturnFooBar() {
    assertEquals("15 FooBar", FizzBizz.fooBar(15));
  }

  @Test
  public void shouldReturnFoo() {
    assertEquals("3 Foo", FizzBizz.fooBar(3));
  }

  @Test
  public void shouldReturnBar() {
    assertEquals("5 Bar", FizzBizz.fooBar(5));
  }

  @Test
  public void shouldReturnOnlyNumber() {
    assertEquals("1", FizzBizz.fooBar(1));
  }

  @Test
  public void shouldReturnResultWhen15IsPassed() {
    // given
    List<String> expected = Arrays.asList("0 FooBar", "1", "2", "3 Foo", "4", "5 Bar", "6 Foo",
        "7", "8", "9 Foo", "10 Bar", "11", "12 Foo", "13", "14", "15 FooBar");

    // when
    List<String> result = FizzBizz.fizzBizz(0, 16);

    // then
    assertEquals(expected, result);
  }

}
