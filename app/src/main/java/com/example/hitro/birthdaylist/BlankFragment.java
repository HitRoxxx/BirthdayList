package com.example.hitro.birthdaylist;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    private View vv;
    static ArrayList<Demo> al = new ArrayList<>();
    private ListView lv;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vv = inflater.inflate(R.layout.fragment_blank, container, false);

        setData(al);
        return vv;

    }

    public void setData(ArrayList<Demo> al) {
        lv = (ListView) vv.findViewById(R.id.lv);
        Collections.sort(al, new Comparator<Demo>() {
            @Override
            public int compare(Demo o1, Demo o2) {
                return (Integer.parseInt(o1.age) - Integer.parseInt(o2.age));
            }
        });

        lv.setAdapter(new MyAdapter(getActivity(), R.layout.birth_day_layout, al));
    }

    static class Demo {
        byte[] img;
        String name, birthday, age;

        Demo(byte[] img, String name, String birthday, String age) {
            this.img = img;
            this.name = name;
            this.birthday = birthday;
            this.age = age;
        }

    }


    class MyAdapter extends ArrayAdapter<Demo> {
        Context context;
        //  ArrayList<Demo> al = new ArrayList<Demo>();

        public MyAdapter(Context context, int resource, ArrayList<Demo> al) {
            super(context, resource, al);
            this.context = context;
            // this.al = al;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;//= convertView;
            Demo demo = getItem(position);
            v = LayoutInflater.from(context).inflate(R.layout.birth_day_layout, parent, false);

            ImageView iv = (ImageView) v.findViewById(R.id.img);
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView birtday = (TextView) v.findViewById(R.id.birthday);
            TextView dayLeft = (TextView) v.findViewById(R.id.dayleft);
            TextView dayLeft1 = (TextView) v.findViewById(R.id.dayleft1);

            String str = demo.name;
            char[] st = str.toCharArray();

            if (str.length() > 15) {
                str = "";
                for (int i = 0; i < 15; i++) {
                    str += st[i];
                }
            }

            Bitmap bitmap = BitmapFactory.decodeByteArray(demo.img, 0, demo.img.length);
            iv.setImageBitmap(bitmap);
            name.setText(str);
            birtday.setText(demo.birthday);
            dayLeft.setText(demo.age);
            if (Integer.parseInt(demo.age) == 1) {
                v.setBackgroundColor(Color.rgb(255, 0, 0));
            }
            if (Integer.parseInt(demo.age) == 0) {
                v.setBackgroundColor(Color.rgb(255, 64, 129));
                dayLeft.setText("1/2p BirthDay 2 Him");
                dayLeft1.setText("Wish Him");
            }
            return v;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  TextView tv = (TextView) view;
                TextView tv = (TextView) view.findViewById(R.id.name);
                String str = tv.getText().toString();
                Toast.makeText(getContext(), "Clicked on =" + str + " at position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
