package com.example.mountaindiary;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MountainDiaryActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private  GlobalData gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_diary);
        gd = new GlobalData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mountain_diary, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Hmmm...")
                .setContentText("Czy na pewno chcesz zamknąć aplikację ?")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                        }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_export:
                Intent intent = new Intent(MountainDiaryActivity.this,ExportActivity.class);
                startActivity(intent);
                break;
            case R.id.action_language:
                break;
            case R.id.action_colors:
                break;
            case R.id.action_info:
                break;
            case R.id.action_my_account:
                Intent intent2 = new Intent(MountainDiaryActivity.this,MyProfilActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_find:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_find);

                final AutoCompleteTextView text =  dialog.findViewById(R.id.textFind);
                Button dialogButton = (Button) dialog.findViewById(R.id.buttonFind);

                final String[] PEAKS = new String[gd.getPeaksList().size()];
                int i=0;

                for(Peak peak : gd.getPeaksList()){
                    PEAKS[i] = peak.getTitle() + " (" + peak.getHeight() + ")";
                    i++;
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PEAKS);
                text.setAdapter(adapter);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String idString = text.getText().toString().replace(" (","").replace(")","");
                                                        final Peak tmpPeak;

                                                        if(gd.getPeakNameAndHeight(idString).size()<=0){
                                                            tmpPeak = null;
                                                            dialog.dismiss();
                                                            new SweetAlertDialog(MountainDiaryActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Oops...")
                                                                    .setContentText("w bazie nie odnaleziono szczytu o podanej nazwie. Sprawdź poprawność wpisanej frazy, lub dodaj nieistniejący jeszcze szczyt")
                                                                    .setConfirmText("Ok, poprawię")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                        }
                                                                    })
                                                                    .show();

                                                        }
                                                        else{
                                                            dialog.dismiss();
                                                            finish();
                                                            tmpPeak = gd.getPeakNameAndHeight(idString).get(0);
                                                            Intent intent4 = new Intent(MountainDiaryActivity.this, PeakActivity.class);
                                                            intent4.putExtra("id", gd.getPeaksList().indexOf(tmpPeak));
                                                            startActivity(intent4);
                                                        }
                                                    }
                                                });
                dialog.show();
                break;
            case R.id.action_home:
                Intent intent3 = new Intent(this,MountainDiaryActivity.class);
                finish();
                startActivity(intent3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_mountain_diary, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new Fragment_1();
                    break;
                case 1:
                    fragment = new Fragment_2();
                    break;
                case 2:
                    fragment = new Fragment_3();
                    break;
                case 3:
                    fragment = new Fragment_4();
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }
}
