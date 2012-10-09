package ru.katas.n4

import org.junit.Test

/**
 * User: dima
 * Date: 09/10/2012
 */
class DataMunging5 {
  @Test void shouldFindDayWithMinTemperatureSpread() {
    def text = new File("/Users/dima/IdeaProjects/katas/src/main/scala/ru/katas/n4/weather.dat").readLines()
    text = text.subList(8, text.size() - 2).collect{ it.split() }
            .collect{ [key: it[0], value1: asInt(it[1]), value2: asInt(it[2])] }

    def dayWithMinTemperatureSpread = text.min { (it.value1 - it.value2).abs() }.key

    assert text.size() == 30
    assert text[0].key == "1"
    assert text[0].value1 == 88
    assert text[0].value2 == 59

    assert dayWithMinTemperatureSpread == "14"
  }

  @Test public void findTeamWithMinGoalDifference() {
    def text = new File("/Users/dima/IdeaProjects/katas/src/main/scala/ru/katas/n4/football.dat").readLines()
    text = text.subList(5, text.size() - 1).findAll{ !it.trim().matches(/---+/) }.collect{ it.split() }
    def data = text.collect{ [key: it[1], value1: asInt(it[6]), value2: asInt(it[8])] }

    def teamWithMinGoalDiff = data.min{ (it.value1 - it.value2).abs() }.key

    assert text.size() == 20
    assert data.size() == 20
    assert data[0].key == "Arsenal"
    assert data[0].value1 == 79
    assert data[0].value2 == 36

    assert teamWithMinGoalDiff == "Aston_Villa"
  }

  private static def asInt(String s) {
    Integer.valueOf(s.replace("*", ""))
  }
}