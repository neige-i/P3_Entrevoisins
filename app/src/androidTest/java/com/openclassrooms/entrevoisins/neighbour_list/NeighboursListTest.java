
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.contrib.ViewPagerActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_list.DetailActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;
    private IdlingResource mIdlingResource;

    @Rule
    public IntentsTestRule<ListNeighbourActivity> mActivityRule =
            new IntentsTestRule<>(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @After
    public void tearDown() {

    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onRecyclerViewInteraction().check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT-1));

        // TODO: how to avoid undoing things when all tests are running one after the other
        onView(withId(R.id.add_neighbour)).perform(click());
        onView(withId(R.id.name)).perform(typeText(" "), closeSoftKeyboard());
        onView(withId(R.id.create)).perform(click());
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT));
    }

    @Test
    public void detailActivity_shouldNotBeNull() {
        // TODO: use anyOf()
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(DetailActivity.class.getName()));
    }

    @Test
    public void textView_shouldContainName() {
        String neighbourName = "Caroline";
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(neighbourName)), click()));
        intended(hasComponent(DetailActivity.class.getName()));
        onView(withId(R.id.name)).check(matches(withText(neighbourName)));
    }

    @Test
    public void favouriteTab_shouldDisplayFavouriteOnly() {
        int none = 0;
        onView(withId(R.id.container)).perform(ViewPagerActions.scrollRight());
        onView(withContentDescription(R.string.tab_favorites_title)).check(matches(isSelected()));
        onRecyclerViewInteraction().check(withItemCount(none));

        setFavourite(true);
        onRecyclerViewInteraction().check(withItemCount(none + 1));
        setFavourite(false); // TODO: how to avoid undoing things when all tests are running one after the other
    }

    @Test
    public void myFavouriteList_deleteAction_shouldRemoveItem() {
        int favItemCount = 1;
        // Given : We remove the element at position 2
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT));

        setFavourite(true);
        onRecyclerViewInteraction().check(withItemCount(favItemCount));

        // When perform a click on a delete icon
//        mIdlingResource = ((NeighbourFragment) mActivity.getSupportFragmentManager().getFragments().get(1)).getCountingIdlingResource();
//        IdlingRegistry.getInstance().register(mIdlingResource);
//        System.out.println("mIdlingResource=" + mIdlingResource);
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        setFavourite(false);
//        setFavourite();
        // Then : the number of element is 11
//        onRecyclerViewInteraction().check(withItemCount(favItemCount-1));
//        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
//        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT));
    }

    /**
     * Finds the RecyclerView that has focus, i.e. that is currently displayed in the ViewPager.
     * @return The RecyclerView that being displayed
     */
    private ViewInteraction onRecyclerViewInteraction() {
        return onView(allOf(withId(R.id.list_neighbours), hasFocus()));
    }

    private void setFavourite(boolean isFavourite) {
        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.favourite))
                .perform(click())
                .check(matches(withTagValue(is(isFavourite ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp))));
        pressBack();
        onView(withContentDescription(R.string.tab_favorites_title)).perform(click()).check(matches(isSelected()));
    }
}