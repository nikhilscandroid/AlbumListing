package com.test.album.views;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.test.album.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AlbumsActivityTest {

    @Rule
    public ActivityTestRule<AlbumsActivity> mActivityTestRule = new ActivityTestRule<>(AlbumsActivity.class);

    @Test
    public void albumsActivityTest() {
        //check if on click a correct intent is made
        Intents.init();
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view_album_list),
                        childAtPosition(
                                withId(R.id.pull_to_refresh),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(2, click()));
        Intents.release();


    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
