package com.example.sugangdb;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Schedule {
    private static Schedule instance;
    private String[] monday;
    private String[] tuesday;
    private String[] wednesday;
    private String[] thursday;
    private String[] friday;

    private List<String> scheduleList;

    public Schedule() {
        monday = new String[14];
        tuesday = new String[14];
        wednesday = new String[14];
        thursday = new String[14];
        friday = new String[14];
        for (int i = 0; i < 14; i++) {
            monday[i] = "";
            tuesday[i] = "";
            wednesday[i] = "";
            thursday[i] = "";
            friday[i] = "";
        }

        scheduleList = new ArrayList<>();
    }

    public static Schedule getInstance() {
        if (instance == null) {
            instance = new Schedule();
        }
        return instance;
    }

    public List<String> getScheduleList() {
        return scheduleList;
    }

    public void addSchedule(String scheduleText) {
        String[] tokens = scheduleText.split("\\s+"); // 공백을 기준으로 문자열 분리
        for (int i = 0; i < tokens.length; i ++) {
            String[] dayTokens = tokens[i].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

            // dayTokens[0]에는 한글이, dayTokens[1]에는 숫자가 들어감
            String day = dayTokens[0];
            String time = dayTokens[1];

            // 시간 값이 숫자인 경우에만 처리
            if (!time.isEmpty() && time.matches("\\d+")) {
                int parsedTime = Integer.parseInt(time);
                Log.d("Schedule", "parsedTime: " + parsedTime);


                switch (day) {
                    case "월":
                        if (parsedTime >= 0 && parsedTime < monday.length) {
                            monday[parsedTime] = "수업";
                        }
                        break;
                    case "화":
                        if (parsedTime >= 0 && parsedTime < tuesday.length) {
                            tuesday[parsedTime] = "수업";
                        }
                        break;
                    case "수":
                        if (parsedTime >= 0 && parsedTime < wednesday.length) {
                            wednesday[parsedTime] = "수업";
                        }
                        break;
                    case "목":
                        if (parsedTime >= 0 && parsedTime < thursday.length) {
                            thursday[parsedTime] = "수업";
                        }
                        break;
                    case "금":
                        if (parsedTime >= 0 && parsedTime < friday.length) {
                            friday[parsedTime] = "수업";
                        }
                        break;
                    default:
                        break;

                }
            }
        }
        scheduleList.add(scheduleText);
        Log.d("Schedule", "Monday: " + Arrays.toString(monday));
        Log.d("Schedule", "Tuesday: " + Arrays.toString(tuesday));
        Log.d("Schedule", "wednesday: " + Arrays.toString(wednesday));
        Log.d("Schedule", "thursday: " + Arrays.toString(thursday));
        Log.d("Schedule", "friday: " + Arrays.toString(friday));
    }

    public boolean validate(String scheduleText) {
        if (scheduleText.equals("")) {
            return true; // 빈 문자열은 항상 유효함
        }

        String[] tokens = scheduleText.split("\\s+");

        if (tokens.length % 2 != 0) {
            // 홀수일 경우 마지막 요소 제거
            tokens = Arrays.copyOf(tokens, tokens.length - 1);
        }

        for (int i = 0; i < tokens.length; i += 2) {
            String day = tokens[i].replaceAll("[^가-힣]", ""); // 한글만 추출
            String time = (i + 1 < tokens.length) ? tokens[i + 1] : "";

            int parsedTime;
            try {
                // 숫자만 추출하여 변환
                parsedTime = Integer.parseInt(time.replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                // 정수로 변환할 수 없는 경우 예외 처리
                System.out.println("Invalid time format: " + time);
                return false;
            }

            switch (day) {
                case "월":
                    if (parsedTime >= 0 && parsedTime < monday.length && monday[parsedTime].equals("수업")) {
                        return false;
                    }
                    break;
                case "화":
                    if (parsedTime >= 0 && parsedTime < tuesday.length && tuesday[parsedTime].equals("수업")) {
                        return false;
                    }
                    break;
                case "수":
                    if (parsedTime >= 0 && parsedTime < wednesday.length && wednesday[parsedTime].equals("수업")) {
                        return false;
                    }
                    break;
                case "목":
                    if (parsedTime >= 0 && parsedTime < thursday.length && thursday[parsedTime].equals("수업")) {
                        return false;
                    }
                    break;
                case "금":
                    if (parsedTime >= 0 && parsedTime < friday.length && friday[parsedTime].equals("수업")) {
                        return false;
                    }
                    break;
                default:
                    System.out.println("Invalid day format: " + day);
                    return false;
            }
        }
        return true; // 모든 조건을 통과하면 유효함
    }

    public void addSchedule(String scheduleText, String course_name, String prof_name) {
        String professor;
        if(prof_name.equals("")){
            professor = "";
        }
        else{
            professor = ""; //교수 이름
        }

        String[] tokens = scheduleText.split("\\s+"); // 공백을 기준으로 문자열 분리
        for (int i = 0; i < tokens.length; i ++) {
            String[] dayTokens = tokens[i].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

            // dayTokens[0]에는 한글이, dayTokens[1]에는 숫자가 들어감
            String day = dayTokens[0];
            String time = dayTokens[1];

            // 시간 값이 숫자인 경우에만 처리
            if (!time.isEmpty() && time.matches("\\d+")) {
                int parsedTime = Integer.parseInt(time);

                switch (day) {
                    case "월":
                        if (parsedTime >= 0 && parsedTime < monday.length) {
                            monday[parsedTime] = course_name + professor;
                        }
                        break;
                    case "화":
                        if (parsedTime >= 0 && parsedTime < tuesday.length) {
                            tuesday[parsedTime] = course_name + professor;
                        }
                        break;
                    case "수":
                        if (parsedTime >= 0 && parsedTime < wednesday.length) {
                            wednesday[parsedTime] = course_name + professor;
                        }
                        break;
                    case "목":
                        if (parsedTime >= 0 && parsedTime < thursday.length) {
                            thursday[parsedTime] = course_name + professor;
                        }
                        break;
                    case "금":
                        if (parsedTime >= 0 && parsedTime < friday.length) {
                            friday[parsedTime] = course_name + professor;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void setting(AutoResizeTextView[] monday, AutoResizeTextView[] tuesday, AutoResizeTextView[] wednesday, AutoResizeTextView[] thursday, AutoResizeTextView[] friday, Context context) {
        int maxLength = 0;
        String maxString = "";
        for(int i = 0; i < 14; i++) {
            if(this.monday[i].length() > maxLength) {
                maxLength = this.monday[i].length();
                maxString = this.monday[i];
            }
            if(this.tuesday[i].length() > maxLength) {
                maxLength = this.tuesday[i].length();
                maxString = this.tuesday[i];
            }
            if(this.wednesday[i].length() > maxLength) {
                maxLength = this.wednesday[i].length();
                maxString = this.wednesday[i];
            }
            if(this.thursday[i].length() > maxLength) {
                maxLength = this.thursday[i].length();
                maxString = this.thursday[i];
            }
            if(this.friday[i].length() > maxLength) {
                maxLength = this.friday[i].length();
                maxString = this.friday[i];
            }

        }
        for(int i = 0; i < 14; i++) {
            if(!this.monday[i].equals("")) {
                monday[i].setText(this.monday[i]);
                monday[i].setTextColor(context.getResources().getColor(R.color.lilpa_700));
            }
            else {
                monday[i].setText(maxString);
            }
            if(!this.tuesday[i].equals("")) {
                tuesday[i].setText(this.tuesday[i]);
                tuesday[i].setTextColor(context.getResources().getColor(R.color.lilpa_700));
            }
            else {
                tuesday[i].setText(maxString);
            }
            if(!this.wednesday[i].equals("")) {
                wednesday[i].setText(this.wednesday[i]);
                wednesday[i].setTextColor(context.getResources().getColor(R.color.lilpa_700));
            }
            else {
                wednesday[i].setText(maxString);
            }
            if(!this.thursday[i].equals("")) {
                thursday[i].setText(this.thursday[i]);
                thursday[i].setTextColor(context.getResources().getColor(R.color.lilpa_700));
            }
            else {
                thursday[i].setText(maxString);
            }
            if(!this.friday[i].equals("")) {
                friday[i].setText(this.friday[i]);
                friday[i].setTextColor(context.getResources().getColor(R.color.lilpa_700));
            }
            else {
                friday[i].setText(maxString);
            }
            monday[i].resizeText();
            tuesday[i].resizeText();
            wednesday[i].resizeText();
            thursday[i].resizeText();
            friday[i].resizeText();
        }
    }

    public boolean isScheduleOverlap(String existingTime, String newTime) {
        return existingTime.equals(newTime);
    }

}