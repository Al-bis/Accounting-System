package pl.coderstrust.fizzbizz;

import java.util.ArrayList;
import java.util.List;

public class FizzBizz {

  public static List<String> fizzBizz(int start, int end) {
    List<String> result = new ArrayList<>();
    for (int i = start; i < end; i++) {
      result.add(fooBar(i));
    }
    return result;
  }

  public static String fooBar(int number) {
    if (number % 15 == 0) {
      return number + " FooBar";
    }
    if (number % 3 == 0) {
      return number + " Foo";
    }
    if (number % 5 == 0) {
      return number + " Bar";
    }
    return "" + number;
  }

}
