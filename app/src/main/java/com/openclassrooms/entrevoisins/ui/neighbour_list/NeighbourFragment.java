package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.events.ShowNeighbourDetailEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NeighbourFragment extends Fragment {

    /**
     * Position in the ViewPager of the fragment displaying all the neighbours.
     * <br />It must be used as the argument when calling {@link #newInstance(int)}
     */
    public static final int ALL_NEIGHBOURS = 0;
    /**
     * Position in the ViewPager of the fragment displaying only the favourite neighbours.
     * <br />It must be used as the argument when calling {@link #newInstance(int)}
     */
    public static final int FAVOURITE_NEIGHBOURS = 1;
    /**
     * Key to set and retrieve the kind of neighbours this fragment will display.
     */
    private static final String NEIGHBOUR_PARAM = "NEIGHBOUR_PARAM";
//    /**
//     * Static list containing all the instances of this class. This is used to access other instances
//     * from another one. Here, the favourite list inside the {@link #FAVOURITE_NEIGHBOURS} fragment
//     * can be updated from the {@link #ALL_NEIGHBOURS} fragment.
//     */
//    private static List<NeighbourFragment> FRAGMENT_LIST = new ArrayList<>();

    /**
     * Represents which neighbours to display in the {@link RecyclerView}.
     * It equals to either {@link #ALL_NEIGHBOURS} or {@link #FAVOURITE_NEIGHBOURS}.
     */
    private int mWhichNeighbours;

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;

    @VisibleForTesting
    private CountingIdlingResource mCountingIdlingResource;

    @BindView(R.id.list_neighbours)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_favourite)
    TextView mNoFavouriteText;

    /**
     * Create and return a new instance
     * @param whichNeighbours Kind of neighbours to display.
     *                        Must be either {@link #ALL_NEIGHBOURS} or {@link #FAVOURITE_NEIGHBOURS}
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance(int whichNeighbours) {
        NeighbourFragment fragment = new NeighbourFragment();
        Bundle args = new Bundle();
        args.putInt(NEIGHBOUR_PARAM, whichNeighbours);
        fragment.setArguments(args);
//        FRAGMENT_LIST.add(fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
        mWhichNeighbours = getArguments() != null ? getArguments().getInt(NEIGHBOUR_PARAM) : ALL_NEIGHBOURS;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mCountingIdlingResource = new CountingIdlingResource("RecyclerView refresh");
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        switch (mWhichNeighbours) {
            case ALL_NEIGHBOURS:        mNeighbours = mApiService.getNeighbours();      break;
            case FAVOURITE_NEIGHBOURS:  mNeighbours = mApiService.getFavNeighbours();   break;
        }
        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours));
        updateTextVisibility();
    }

    /**
     * Shows a short text to the user if there isn't any favourite neighbour
     */
    private void updateTextVisibility() {
        if (mWhichNeighbours == FAVOURITE_NEIGHBOURS)
            mNoFavouriteText.setVisibility(mRecyclerView.getAdapter().getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // Lifecycle of a fragment inside a ViewPager can be tricky as it is not necessarily
        // destroyed if it is not visible anymore. Here, this fragment is used in both pages of the
        // ViewPager's adapter. Thus, if the fragments (un)register an EventBus in onStart() and
        // onStop(), when the user clicks on a Neighbour (which fires an event), then the
        // @Subscribe method is called twice as both fragments are aware of the event. This is why
        // the EventBus is (un)registered when the fragment becomes (in)visible to the user.
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        System.out.println("onDeleteNeighbour " + mWhichNeighbours);
//        switch (mWhichNeighbours) {
//            case ALL_NEIGHBOURS:
//                mApiService.deleteNeighbour(event.neighbour);
////                FRAGMENT_LIST.get(FAVOURITE_NEIGHBOURS).initList();
//                break;
//            case FAVOURITE_NEIGHBOURS:
//                mApiService.toggleFavourite(event.neighbour);
//                break;
//        }
//        mCountingIdlingResource.increment();
//        initList();
//        mCountingIdlingResource.decrement();
    }

    /**
     * Fired if the user clicks on a RecyclerView item
     * @param event
     */
    @Subscribe
    public void onShowNeighbourDetail(ShowNeighbourDetailEvent event) {
        System.out.println("onShowNeighbourDetail " + mWhichNeighbours);
        DetailActivity.navigate(getActivity(), event.neighbour);
    }

    public CountingIdlingResource getCountingIdlingResource() {
        return mCountingIdlingResource;
    }
}
