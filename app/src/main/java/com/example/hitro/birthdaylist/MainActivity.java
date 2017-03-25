package com.example.hitro.birthdaylist;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.example.hitro.birthdaylist.BlankFragment.al;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MyDatabase md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        al = new ArrayList<>();
        md = new MyDatabase(this);

        // one time run only
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            databaseSetup();

            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = DialogFragment.newInstance();
                newFragment.setCancelable(false);
                newFragment.show(ft, "dialog");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getData(false);


        getSupportFragmentManager().beginTransaction().replace(R.id.main_Window, new BlankFragment(), "f1").commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void databaseSetup() {

        SQLiteDatabase sq = md.getWritableDatabase();
        //  SQLiteDatabase sq=  md.getDatabase();
        String query = "CREATE TABLE BIRTHDAY ( ID  INTEGER PRIMARY KEY AUTOINCREMENT , NAME  TEXT NOT NULL, PHONE_NO  TEXT , EMAIL TEXT , DATE TEXT NOT NULL, IMAGE  BLOB NOT NULL)";
        sq.execSQL(query);
        // Toast.makeText(this, "Table created"+md.getDatabase(), Toast.LENGTH_SHORT).show();
    }

    public void getData(boolean flag) {
        SQLiteDatabase sq = md.getReadableDatabase();
        Cursor cur = null;
        al = new ArrayList<>();
        //cur = sq.query(true, "BIRTHDAY", new String[]{ "ID" , "NAME", "PHONE_NO", "EMAIL", "DATE", "IMAGE",}, null, null, null, null, "NAME", "ASC",null, null);
        //   cur =sq.query("BIRTHDAY",new String[]{ "ID" , "NAME", "PHONE_NO", "EMAIL", "DATE", "IMAGE",},null,null,"NAME",null,"ASC",null);
       // cur = sq.rawQuery("SELECT ID, NAME, PHONE_NO, EMAIL, DATE, IMAGE FROM BIRTHDAY GROUP BY NAME ORDER BY NAME ASC", null);
         cur = sq.rawQuery("SELECT * FROM BIRTHDAY  ORDER BY NAME ASC", null);
        int count = 1;
     //   cur.moveToFirst();
        while (cur.moveToNext()) {

            String date = cur.getString(4);
            String splitedBd[] = date.split("\\/+");

            int[] bd = new int[splitedBd.length];
            int i = 0;
            for (String str : splitedBd) {
                bd[i] = Integer.parseInt(str);
                i++;
            }

            String data = calculateAge(bd);
            al.add(new BlankFragment.Demo(cur.getBlob(5), cur.getString(1), cur.getString(4), data));
            if (flag) {
                BlankFragment ba = (BlankFragment) getSupportFragmentManager().findFragmentByTag("f1");
                ba.setData(al);
            }

            count++;
        }
      /*
        {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }*/
      //  BlankFragment bf = (BlankFragment) getSupportFragmentManager().findFragmentByTag("f1");
     //   bf.setData(al);

        cur.close();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_Window, new BlankFragment(), "f1").commit();
    }


    private String calculateAge(int bd[]) {
        // LocalDate birthdate =  new LocalDate(bd[2], bd[1], bd[0]);
        int years = 0;
        int months = 0;
        int days = 0;

        Calendar birthDay = Calendar.getInstance();
        birthDay.set(bd[2], bd[1] - 1, bd[0]);
        Calendar now = Calendar.getInstance();

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;
        //if month difference is in negative then reduce years by one and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }
        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }

        birthDay.set(now.get(Calendar.YEAR)+1, bd[1] - 1,bd[0] );
        //days between two days
        long msDiff = birthDay.getTimeInMillis()- Calendar.getInstance().getTimeInMillis() ;
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

        if(birthDay.getActualMaximum(Calendar.DAY_OF_YEAR) > 365  )
        {
            if((birthDay.get(Calendar.MONTH) + 1)> 2)
            {
                if(daysDiff>366)
                {
                    return ""+(daysDiff-366);
                }
                else
                {
                        return ""+daysDiff;
                }

            }
            else
            {
                if(daysDiff>365)
                {
                    return ""+(daysDiff-365);
                }
                else
                {
                    return ""+daysDiff;
                }
            }

        }
        else
        {
            if(daysDiff>365)
            {
                return ""+(daysDiff-365);
            }
            else
            {

                return ""+daysDiff;
            }
        }
        //return ""+daysDiff;

        //return "Year :" + years + " Month :" + months + " Day :" + days;
    }


}
