package com.example.sugangdb;

public class Course {
    String course_name;     // 강의 이름
    String dept_name;       // 학과 이름
    String prof_name;       // 교수 이름
    String course_id;       // 과목번호
    int div_id;             // 분반
    String course_time;     // 강의 시간
    int target_grade;       // 대상학년
    String classification;  // 이수구분
    int credit;             // 학점
    int current_count;      // 현재 수강인원
    int avail_count;        // 최대 수강인원
    int stu_avail_credit;   // 최대 수강학점

    public int getStu_avail_credit() {
        return stu_avail_credit;
    }

    public void setStu_avail_credit(int stu_avail_credit) {
        this.stu_avail_credit = stu_avail_credit;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getProf_name() {
        return prof_name;
    }

    public void setProf_name(String prof_name) {
        this.prof_name = prof_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public int getDiv_id() {
        return div_id;
    }

    public void setDiv_id(int div_id) {
        this.div_id = div_id;
    }

    public String getCourse_time() {
        return course_time;
    }

    public void setCourse_time(String course_time) {
        this.course_time = course_time;
    }

    public int getTarget_grade() {
        return target_grade;
    }

    public void setTarget_grade(int target_grade) {
        this.target_grade = target_grade;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCurrent_count() {
        return current_count;
    }

    public void setCurrent_count(int current_count) {
        this.current_count = current_count;
    }

    public int getAvail_count() {
        return avail_count;
    }

    public void setAvail_count(int avail_count) {
        this.avail_count = avail_count;
    }

    public Course(String course_id, String course_name, int div_id, int target_grade, int avail_count, int current_count) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.div_id = div_id;
        this.target_grade = target_grade;
        this.avail_count = avail_count;
        this.current_count = current_count;
    }

    public Course(String course_id, String course_name, int div_id, int target_grade, int avail_count, int current_count, int credit) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.div_id = div_id;
        this.target_grade = target_grade;
        this.avail_count = avail_count;
        this.current_count = current_count;
        this.credit = credit;
    }

    public Course(String course_name, String dept_name, String prof_name, String course_id, int div_id, String course_time, int target_grade, String classification, int credit, int current_count, int avail_count) {
        this.course_name = course_name;
        this.dept_name = dept_name;
        this.prof_name = prof_name;
        this.course_id = course_id;
        this.div_id = div_id;
        this.course_time = course_time;
        this.target_grade = target_grade;
        this.classification = classification;
        this.credit = credit;
        this.current_count = current_count;
        this.avail_count = avail_count;
    }
}
