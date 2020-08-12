package com.openclassrooms.entrevoisins.neighbour_list;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_detail.DetailActivity;
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
        // Given: we remove the element at position 2
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT));
        // When: perform a click on a delete icon
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then: the number of element is 11
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT - 1));
    }

    /**
     * When we click on a RecyclerView item, DetailActivity is launched
     */
    @Test
    public void detailActivity_shouldBeLaunched() {
        // Given: we want to see the details of the first element
        int position = 0;
        // When: we click on the item of the list
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
        // Then: the DetailActivity is launched
        intended(hasComponent(DetailActivity.class.getName()));
    }

    /**
     * When we click on a specific user, the appropriate TextView in the detail screen contains its name
     */
    @Test
    public void textView_shouldContainName() {
        // Given: we want to see the detail of a neighbour whose name is Caroline
        String neighbourName = "Caroline";
        // When: we click on the appropriate item of the list
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(neighbourName)), click()));
        // Then: the TextView displays the given name
        onView(withId(R.id.name)).check(matches(withText(neighbourName)));
    }

    /**
     * The favourite tab must show the favourite neighbours only
     */
    @Test
    public void favouriteTab_shouldDisplayFavouriteOnly() {
        // Given: there isn't any favourite neighbour
        int none = 0;

        // When: we scroll the ViewPager to show the favourite list
        onView(withId(R.id.container)).perform(swipeLeft());
        // Then: the list is empty
        onRecyclerViewInteraction().check(withItemCount(none));

        // When: we set a favourite neighbour
        setFavourite();
        // Then: the favourite list contains an extra element
        onRecyclerViewInteraction().check(withItemCount(none + 1));
    }

    /**
     * When we delete an item in the favourite tab, the item is no more shown in the favourite tab only.
     * The list in the 'all neighbours' tab remains unchanged.
     */
    @Test
    public void myFavouriteList_deleteAction_shouldRemoveItem() {
        // Given: we remove the only favourite neighbour
        int favItemCount = 1;
        onRecyclerViewInteraction().check(withItemCount(ITEMS_COUNT));

        // When: we set a favourite neighbour
        setFavourite();
        // Then: the favourite list contains 1 element
        onRecyclerViewInteraction().check(withItemCount(favItemCount));

        // When: we click on the delete icon
        onRecyclerViewInteraction().perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // Then: the favourite list is empty
        onRecyclerViewInteraction().check(withItemCount(favItemCount - 1));

        // When: we return to the 'all neighbours' tab
        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
        // Then: the main list remains the same
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