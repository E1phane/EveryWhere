package com.example.sugangdb;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private EditText subjectName;
    private ArrayAdapter gradeAdapter;
    private Spinner gradeSpinner;
    private ArrayAdapter areaAdapter;
    private Spinner areaSpinner;
    private ArrayAdapter majorAdapter;
    private Spinner majorSpinner;

    private ListView courseListView;
    private CourseListAdapter adapter;
    private List<Course> courseList;

    @Override
    public void onActivityCreated(Bundle b) {

        super.onActivityCreated(b);

        subjectName = (EditText) getView().findViewById(R.id.subjectName);  //과목 이름
        gradeSpinner = (Spinner) getView().findViewById(R.id.gradeSpinner); //학년
        areaSpinner = (Spinner) getView().findViewById(R.id.areaSpinner);   //이수구분
        majorSpinner = (Spinner) getView().findViewById(R.id.majorSpinner); //학과

        gradeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.grade, android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);
        areaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityArea, android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);
        majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.major, android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter((majorAdapter));

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(areaSpinner.getSelectedItem().equals("전공필수")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.major, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter((majorAdapter));
                }
                if(areaSpinner.getSelectedItem().equals("전공기초")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.major, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter((majorAdapter));
                }
                if(areaSpinner.getSelectedItem().equals("전공선택")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.major, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter((majorAdapter));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        courseListView = (ListView) getView().findViewById(R.id.courseListView);
        courseList = new ArrayList<Course>();
        adapter = new CourseListAdapter(getContext().getApplicationContext(), courseList, this);
        courseListView.setAdapter(adapter);

        Button searchButton = (Button) getView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                String subject = subjectName.getText().toString().trim();

                if (subject.isEmpty()) {
                    subject = "전체";
                }
                target = "http://15.165.171.57/CourseReference.php?courseArea=" +
                        URLEncoder.encode(areaSpinner.getSelectedItem().toString(), "UTF-8") +
                        "&courseMajor=" + URLEncoder.encode(majorSpinner.getSelectedItem().toString(), "UTF-8") +
                        "&courseName=" + URLEncoder.encode(subject, "UTF-8") +
                        "&courseGrade=" + URLEncoder.encode(gradeSpinner.getSelectedItem().toString(), "UTF-8");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }
            @Override
            public void onPostExecute(String result) {
                try {
                    courseList.clear();
                    JSONArray jsonArray = new JSONArray(result);
                    int count = 0;
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
                    while(count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        course_name = object.getString("course_name");
                        dept_name = object.getString("dept_name");
                        prof_name = object.getString("prof_name");
                        course_id = object.getString("course_id");
                        div_id = object.getInt("div_id");
                        course_time = object.getString("course_time");
                        target_grade = object.getInt("target_grade");
                        classification = object.getString("classification");
                        credit = object.getInt("credit");
                        current_count = object.getInt("current_count");
                        avail_count = object.getInt("avail_count");
                        Course course = new Course(course_name, dept_name, prof_name, course_id, div_id, course_time, target_grade, classification, credit, current_count, avail_count);
                        courseList.add(course);
                        count++;
                    }
                    if(count == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(CourseFragment.this.getActivity());
                        builder.setMessage("조회된 강의가 없습니다.").setPositiveButton("확인", null).create();
                        builder.show();
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}