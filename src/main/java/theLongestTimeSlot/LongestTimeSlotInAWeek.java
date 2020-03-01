package theLongestTimeSlot;// you can also use imports, for example:

import java.util.Arrays;

class LongestTimeSlotInAWeek {

  public static void main(String[] args) {
    String input = "Mon 01:00-23:00 \n"
        + "Tue 01:00-23:00 \n"
        + "Wed 01:00-23:00 \n"
        + "Thu 01:00-23:00 \n"
        + "Fri 01:00-23:00 \n"
        + "Sat 01:00-23:00 \n"
        + "Sun 01:00-21:00";
    System.out.println(new LongestTimeSlotInAWeek().solution(input));
  }

  public int solution(String S) {
    // write your code in Java SE 8
    String[] meetings = Arrays.stream(S.split("\\n")).sorted((o1, o2) -> {
      WeekDay day1 = WeekDay.valueOf(o1.substring(0, 3));
      WeekDay day2 = WeekDay.valueOf(o2.substring(0, 3));
      if (day1.ordinal() > day2.ordinal()) {
        return 1;
      } else if (day1.ordinal() < day2.ordinal()) {
        return -1;
      } else {
        String time1 = o1.substring(3);
        String time2 = o2.substring(3);
        return time1.compareTo(time2);
      }
    }).toArray(String[]::new);

    int max1 = datePeriod("Mon 00:00-00:00", meetings[0]);
    int max2 = datePeriod(meetings[meetings.length - 1], "Sun 24:00-24:00");
    int max = Math.max(max2, max1);
    for (int i = 0; i < meetings.length - 1; i++) {
      int result = datePeriod(meetings[i], meetings[i + 1]);
      System.out.println(meetings[i] + " -> " + meetings[i + 1] + " : " + result);
      if (max < result) {
        max = result;
      }
    }
    return max;
  }

  /**
   * calculate the minutes during the give time
   *
   * @param from string like "Mon 05:00-13:00"
   * @param to string like "Tue 15:00-21:00"
   * @return return the minutes result
   */
  private int datePeriod(String from, String to) {
    String[] fromArray = getDateAndTime(from);
    String[] toArray = getDateAndTime(to);
    if (fromArray[0].equals(toArray[0])) {
      return timePeriod(fromArray[2], toArray[1]);
    } else {
      int days = WeekDay.valueOf(toArray[0]).ordinal() - WeekDay.valueOf(fromArray[0]).ordinal();
      return timePeriod(fromArray[2], "24:00") + timePeriod("00:00", toArray[1]) + timePeriod("00:00", "24:00") * (days - 1);
    }
  }

  /**
   * split the give date time string into three parts
   *
   * @param item the provided string
   * @return the tokens like {"Mon", "05:00", "13:00"}
   */
  private String[] getDateAndTime(String item) {
    return Arrays.stream(item.split(" "))
        .map(str -> str.split("-"))
        .flatMap(Arrays::stream).toArray(String[]::new);
  }

  /**
   * calculate the minutes of the given time
   *
   * @param from string like "05:00"
   * @param to string like "13:00"
   * @return the minute result
   */
  private int timePeriod(String from, String to) {
    Integer[] fromArray = Arrays.stream(from.split(":")).map(LongestTimeSlotInAWeek::convertToInt).toArray(Integer[]::new);
    Integer[] toArray = Arrays.stream(to.split(":")).map(LongestTimeSlotInAWeek::convertToInt).toArray(Integer[]::new);
    int result;
    if (toArray[1] >= fromArray[1]) {
      result = toArray[1] - fromArray[1];
      result += (toArray[0] - fromArray[0]) * 60;
    } else {
      result = (toArray[0] - fromArray[0]) * 60 - (fromArray[1] - toArray[1]);
    }
    return result;
  }

  /**
   * convert a string number to an integer
   *
   * @param value string number like "13"
   * @return the integer type value
   */
  private static int convertToInt(String value) {
    int result = 0;
    for (int i = 0; i < value.length(); i++) {
      int num = value.charAt(i) - '0';
      result = result * 10 + num;
    }
    return result;
  }

  enum WeekDay {
    Mon,
    Tue,
    Wed,
    Thu,
    Fri,
    Sat,
    Sun
  }
}
