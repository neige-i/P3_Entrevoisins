package com.openclassrooms.entrevoisins.neighbour_list;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_list.DetailActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;

    @Rule
    public IntentsTestRule<ListNeighbourActivity> mActivityRule =
            new IntentsTestRule<>(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        ListNeighbourActivity activity = mActivityRule.getActivity();
        assertThat(activity, notNullValue());
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
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT - 1));
    }

    /**
     * When we click on a RecyclerView item, DetailActivity is launched
     */
    @Test
    public void detailActivity_shouldLaunched() {
        // Arbitrary position (but must be between 0 and 11)
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(DetailActivity.class.getName()));
    }

    /**
     * When we click on a specific user, the appropriate TextView in the detail screen contains its name
     */
    @Test
    public void textView_shouldContainName() {
        String neighbourName = "Caroline";
        // Perform a click on the item of the list with the given name
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(neighbourName)), click()));
        // The detail screen is displayed
        intended(hasComponent(DetailActivity.class.getName()));
        // The TextView displaying the neighbour's name should be the same as the given one
        onView(withId(R.id.name)).check(matches(withText(neighbourName)));
    }

    /**
     * The favourite tab must show the favourite neighbours only
     */
    @Test
    public void favouriteTab_shouldDisplayFavouriteOnly() {
        int none = 0;
        // Scroll the ViewPager to the right to show the favourite neighbours
        onView(withId(R.id.container)).perform(swipeLeft());
        // As no favourite has been set, the favourite list should be empty
        onRecyclerViewInteraction().check(withItemCount(none));

        // Set a favourite neighbour
        setFavourite();
        // Now, the favourite list should contain an extra element
        onRecyclerViewInteraction().check(withItemCount(none + 1));
    }

    /**
     * When we delete an item in the favourite tab, the item is no more shown in the favourite tab only.
     * The list in the 'all neighbours' tab remains unchanged.
     */
    @Test
    public void myFavouriteList_deleteAction_shouldRemoveItem() {
        int favItemCount = 1;
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT));

        // Set a favourite neighbour and check that the favourite list's size equals 1
        setFavourite();
        onRecyclerViewInteraction().check(withItemCount(favItemCount));

        // Perform a click on a delete icon
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // The favourite list's size should have been decreased by 1
        onRecyclerViewInteraction().check(withItemCount(favItemCount - 1));
        // Return to the 'all neighbours' tab
        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
        // The main list should remain the same
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT));
    }

    /**
     * @return the RecyclerView that is currently displayed to the user
     */
    private ViewInteraction onRecyclerViewInteraction() {
        return onView(allOf(withId(R.id.list_neighbours), isDisplayed()));
    }

    /**
     * Sets a favourite neighbour
     */
    private void setFavourite() {
        // Perform a click on the 'all neighbours' tab
        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
        // Perform a click on the first neighbour in the list
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Perform a click on the 'star' icon to make the neighbour a favourite one (and checks that it contains the correct drawable)
        onView(withId(R.id.favourite))
                .perform(click())
                .check(matches(withTagValue(is(R.drawable.ic_star_white_24dp))));
        // Go back to the previous screen
        pressBack();
        // Perform a click on the 'favourite' tab
        onView(withContentDescription(R.string.tab_favorites_title)).perform(click());
    }
}