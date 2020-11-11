package kr.ac.ssu.wherealarmyou.group;

import java.util.List;
import java.util.Map;

public class Group {
    String uid;

    String name;

    Map<String, Boolean> alarms;

    Map<String, String> members;


    Map<String, List<String>> roles;

}
