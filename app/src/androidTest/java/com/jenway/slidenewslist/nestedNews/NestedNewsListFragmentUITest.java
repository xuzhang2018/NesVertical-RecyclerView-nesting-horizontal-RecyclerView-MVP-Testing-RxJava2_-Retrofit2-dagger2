package com.jenway.slidenewslist.nestedNews;

import android.content.pm.ActivityInfo;

import androidx.test.InstrumentationRegistry;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.jenway.slidenewslist.MainActivity;
import com.jenway.slidenewslist.R;
import com.jenway.slidenewslist.RecyclerViewMatcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
/**
 * by Xu
 * Only load the local data when the debug mod is on
 * check if the RecyclerView has displayed
 * Check if horizontal RecyclerView work well
 * check if the menu button work well
 * check if the list keep the status (position and checked) in Protaiit and Landscape mode
 * check if the toast is working in fragment
 * check if the dialog in working in fragment
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class NestedNewsListFragmentUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRecyclerViewShow() throws Exception {
        //init UI
        Thread.sleep(5000);
        //check if the RecyclerView has displayed
        onView(withId(R.id.nested_rv)).check(matches(isDisplayed()));
        //check if the RecyclerView has right order of the data
        onView(withRecyclerView(R.id.nested_rv).atPosition(1))
                .check(matches(hasDescendant(withText("Colin Kaepernick, whose kneeling protest during the US national anthem before NFL games became a national issue, reminded league owners"
                ))));
        onView(withRecyclerView(R.id.nested_rv).atPosition(2))
                .check(matches(hasDescendant(withText("The cruelty of immigrant detention is occurring even though release is an entirely viable option, write Clara Long and Elora Mukherjee. Almost all children and families seeking asylum show up in court when given appropriate support. While Congress is home for the August recess, the American people must demand that they stop throwing money at detention centers and reinstate case management program."
                ))));
        onView(withId(R.id.nested_rv)).perform(scrollToPosition(8));
        Thread.sleep(1000);
        onView(withRecyclerView(R.id.nested_rv).atPosition(8)).check(matches(hasDescendant(withText("These days, we've swapped pop quizzes and glitter gel pens for early morning meetings and paper clips. And although we won't be returning to bulky three-ring binders, we will be revisiting our back-to-school shopping tactics to reorganize our adult lives. The same strategy we used to curate our school-supply list can now be chicly applied to our in-office desktops."
        ))));


        //test the horizontal item
        onView(withId(R.id.nested_rv)).perform(scrollToPosition(3));
        Thread.sleep(1000);
        onView(withRecyclerView(R.id.nested_rv).atPositionOnView(3, R.id.horizontal_rv_item)).perform(scrollToPosition(0))
                .check(matches(hasDescendant(withText("Disney's choice to cast singer and Grown-ish actress Halle Bailey as Ariel in their upcoming live-action adaptation of The Little Mermaid is bringing some much-needed diversity to the classic tale, but it has also brought with it some backlash. Certain fans of the original movie began using the hashtag #NotMyAriel to express their disappointment that the live-action Ariel won't look exactly like her animated counterpart, a racist reaction disguised as a demand for fidelity. Freeform, the home of Bailey's show Grown-ish, set the them straight with their own defense of the star, but it wasn't until Tuesday's Power Of Young Hollywood gala hosted by Variety that Bailey weighed in on the hate."
                ))));
        Thread.sleep(1000);
        onView(withRecyclerView(R.id.nested_rv).atPositionOnView(3, R.id.horizontal_rv_item)).perform(scrollToPosition(1)).check(matches(hasDescendant(withText("It's not every day that you see Neo working as a ball girl for the Los Angeles Dodgers. This unfortunate young lady almost took a screaming Matt Carpenter line drive right in the leg, but pulled off a move that would make any gymnast jealous to almost get out of the way, with the ball seemingly just barely grazing her shin. Pretty sure the Dodgers' ball girl took this Matt Carpenter liner right off the shin after making a near perfect move to get out of the way pic.twitter.com/Jqgsfz686x —..."
        ))));
        Thread.sleep(1000);
        onView(withRecyclerView(R.id.nested_rv).atPositionOnView(3, R.id.horizontal_rv_item)).perform(scrollToPosition(2)).check(matches(hasDescendant(withText("Victoria's Secret is the house that white men built and frankly, it's just about crumbled to the ground. Model Maria Farmer recently alleged that billionaire investor Jeffrey Epstein sexually assaulted her while posing as a recruiter for the Victoria's Secret catalog (Epstein was arrested earlier this month and charged with sex trafficking minors). After Maria came forward, The Model Alliance published an open letter on Tuesday asking Victoria's Secret do more to protect models from abuse."
        ))));
    }

    @Test
    public void testListStaySameStatusWhenSwicthProtaiitAndLandscape() throws Exception {
        //init UI
        Thread.sleep(5000);
        mActivityRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Thread.sleep(2000);
        //go the position 8
        onView(withId(R.id.nested_rv)).perform(scrollToPosition(10));
        Thread.sleep(1000);
        onView(withRecyclerView(R.id.nested_rv).atPosition(8)).check(matches(hasDescendant(withText("These days, we've swapped pop quizzes and glitter gel pens for early morning meetings and paper clips. And although we won't be returning to bulky three-ring binders, we will be revisiting our back-to-school shopping tactics to reorganize our adult lives. The same strategy we used to curate our school-supply list can now be chicly applied to our in-office desktops."
        ))));

        //change to landscape
        mActivityRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Thread.sleep(2000);
        //same position and checked
        onView(withRecyclerView(R.id.nested_rv).atPosition(8)).check(matches(hasDescendant(withText("These days, we've swapped pop quizzes and glitter gel pens for early morning meetings and paper clips. And although we won't be returning to bulky three-ring binders, we will be revisiting our back-to-school shopping tactics to reorganize our adult lives. The same strategy we used to curate our school-supply list can now be chicly applied to our in-office desktops."
        ))));

        //change to portrait
        mActivityRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Thread.sleep(4000);
        //same position and checked
        onView(withRecyclerView(R.id.nested_rv).atPosition(8)).check(matches(hasDescendant(withText("These days, we've swapped pop quizzes and glitter gel pens for early morning meetings and paper clips. And although we won't be returning to bulky three-ring binders, we will be revisiting our back-to-school shopping tactics to reorganize our adult lives. The same strategy we used to curate our school-supply list can now be chicly applied to our in-office desktops."
        ))));

    }

    @Test
    public void testRefreshButton() throws Exception {
        Thread.sleep(5000);
        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.myrefreshbutton)).perform(click());
        Thread.sleep(5000);
        //check if the RecyclerView has displayed
        onView(withId(R.id.nested_rv)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickButtonToShowToast() throws Exception {
        //init UI
        Thread.sleep(5000);
        //click the toast test button
        onView(withId(R.id.test_toast_button))
                .perform(click());

        //wait for process 1 second and Toast with "test toast" appear
        Thread.sleep(1000);
        onView(withText("test toast"))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testClickButtonToShowDialog() throws Exception {
        //init UI
        Thread.sleep(5000);
        //click the dialog test button
        onView(withId(R.id.test_dialog_button))
                .perform(click());

        //wait for process 1 second and Toast with "test toast" appear
        Thread.sleep(1000);
        onView(withText("test dialog"))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

}