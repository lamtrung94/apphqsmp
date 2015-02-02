package tung.lx.uetlinker.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import tung.lx.uetlinker.Constants;
import tung.lx.uetlinker.Utils.Global;
import tung.lx.uetlinker.Utils.LinkGetter;
import tung.lx.uetlinker.Models.LinkObject;
import tung.lx.uetlinker.NavigationDrawerFragment;
import tung.lx.uetlinker.R;
import tung.lx.uetlinker.Utils.Utils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Vector<LinkObject> lsLinkObject = new Vector<LinkObject>();

    private TextView tv;

    //private int endOfAnnouncement = 0, endOfExam = 0;

    private int sidebarPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sidebarPos = getIntent().getIntExtra("sidebarPos", 0);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        tv = (TextView) findViewById(R.id.textView);
    }

    private void Initialize() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getData();
        String html = "<P><h1>Thông báo:</h1></P>";
        if (sidebarPos == Constants.SIDEBAR_POS_EXAM) {
            html = "<P><h1>Lịch thi:</h1></P>";
        } else if (sidebarPos == Constants.SIDEBAR_POS_SCHEDULE) {
            html = "<P><h1>Thời khóa biểu:</h1></P>";
        }
        for (int i = 0; i < lsLinkObject.size(); i++) {

            html += "<P><a href=\"" + lsLinkObject.get(i).getUrl() + "\">" + lsLinkObject.get(i).getTitle() + "</a></P>";
        }
        final Spanned displayList = Html.fromHtml(html);
        tv.setText(displayList);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void getData() {
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        List<String> keywordsList = Utils.getKeywordList(settings.getString("keywords", ""));
        LinkGetter aLinkGetter = new LinkGetter();

        switch (Global.currentScreen) {
            case Constants.SIDEBAR_POS_HOME:
                if (Global.lsLinkObjectAnnouncement == null || Global.lsLinkObjectAnnouncement.size() == 0) {
                    Utils.refreshData(this);
                }
                break;
            case Constants.SIDEBAR_POS_EXAM:
                if (Global.lsLinkObjectExam == null || Global.lsLinkObjectExam.size() == 0) {
                    Utils.refreshData(this);
                }
                break;
            case Constants.SIDEBAR_POS_SCHEDULE:
                if (Global.lsLinkObjectSchedule == null || Global.lsLinkObjectSchedule.size() == 0) {
                    Utils.refreshData(this);
                }
                break;
        }


        if (sidebarPos == Constants.SIDEBAR_POS_EXAM) {
            lsLinkObject = Global.lsLinkObjectExam;
        } else if (sidebarPos == Constants.SIDEBAR_POS_SCHEDULE) {
            lsLinkObject = Global.lsLinkObjectSchedule;
        } else {
            lsLinkObject = Global.lsLinkObjectAnnouncement;
        }
        for (int i = 0; i < lsLinkObject.size(); i++) {
            for (int j = 0; j < keywordsList.size(); j++) {
                if (lsLinkObject.get(i).getTitle().trim().contains(keywordsList.get(j).trim())) {
                    if(!"(*)".equals(lsLinkObject.get(i).getTitle().substring(0, 2)))
                        lsLinkObject.get(i).setTitle("<b>(*)" + lsLinkObject.get(i).getTitle() + "</b>");
                    else
                        lsLinkObject.get(i).setTitle("<b>" + lsLinkObject.get(i).getTitle() + "</b>");
                    Toast.makeText(this, "Keywords detected!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.home_section);
                break;
            case 2:
                mTitle = getString(R.string.exam_section);
                break;
            case 3:
                mTitle = getString(R.string.schedule_section);
                break;
            case 4:
                mTitle = getString(R.string.keywords_section);
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Initialize();
    }
}
