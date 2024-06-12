package com.project.project.hospitalfinder;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.project.R;
import com.project.project.Utils;
import com.project.project.acommon.LoginActivity;
import com.project.project.hospitalfinder.data.Appointments;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewAppointmentsListActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();


    @BindView(R.id.listview)
    ListView lvListView;

    private Unbinder unbinderknife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_listview);

        unbinderknife = ButterKnife.bind(this);


        fetchData(!LoginActivity.isAdmin);


        lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

    }


    public void fetchData(boolean isCheckUsername) {

        Utils.showToast(getApplicationContext(), "Please Wait....");

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Appointments.class.getSimpleName());
        if(isCheckUsername){
            query.whereEqualTo(Appointments.username, LoginActivity.username);
        }


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {
                        updateList(objects);
                    } else {
                        Utils.showToast(getApplicationContext(), "No data found");
                    }

                } else {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "fetch error: " + e.getMessage());
                }
            }
        });
    }

    List<String> dataList = new ArrayList<>();

    public void updateList(List<ParseObject> poList) {
        for (ParseObject po : poList) {
            String data = po.getString(Appointments.hospitalName)
                    + "\n" + po.getString(Appointments.date)
                    + "\n" + po.getString(Appointments.time)
                    + "\n" + po.getString(Appointments.username)
                    + "\nSpecialist: " + po.getString(Appointments.specialist);
            dataList.add(data);
        }

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        lvListView.setAdapter(aa);
    }

}
