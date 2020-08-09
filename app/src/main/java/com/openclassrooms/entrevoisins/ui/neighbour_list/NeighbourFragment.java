package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.events.ShowNeighbourDetailEvent;
import com.openclassrooms.entrevoisins.events.UpdateFavouriteListEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private static final String NEIGHBOUR_PARAM = "neighbour";

    /**
     * Represents which neighbours to display in the {@link RecyclerView}.
     * It equals to either {@link #ALL_NEIGHBOURS} or {@link #FAVOURITE_NEIGHBOURS}.
     */
    private int mWhichNeighbours;

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;

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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        System.out.println("initList " + mWhichNeighbours);
        switch (mWhichNeighbours) {
            case ALL_NEIGHBOURS:        mNeighbours = mApiService.getNeighbours();      break;
            case FAVOURITE_NEIGHBOURS:  mNeighbours = mApiService.getFavNeighbours();   break;
        }
        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours, mWhichNeighbours));
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
    public void onStart() {
        super.onStart();
        initList();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        System.out.println("onDeleteNeighbour " + mWhichNeighbours);
        if (event.mWhichNeighbour == mWhichNeighbours) {
            switch (event.mWhichNeighbour) {
                case ALL_NEIGHBOURS:
                    mApiService.deleteNeighbour(event.mNeighbour);
                    // Sticky in case of the favourite fragment is not started yet (where EventBus is registered)
                    // But using post() here gives the same result
                    EventBus.getDefault().postSticky(new UpdateFavouriteListEvent());
                    break;
                case FAVOURITE_NEIGHBOURS:
                    mApiService.toggleFavourite(event.mNeighbour);
                    break;
            }
            initList();
        }
    }

    /**
     * Fired if the user clicks on a RecyclerView item
     */
    @Subscribe
    public void onShowNeighbourDetail(ShowNeighbourDetailEvent event) {
        System.out.println("onShowNeighbourDetail " + mWhichNeighbours);
        if (event.mWhichNeighbour == mWhichNeighbours)
            DetailActivity.navigate(getActivity(), event.mNeighbour);
    }

    /**
     * Fired if the favourite list needs to be updated
     */
    @Subscribe(sticky = true)
    public void onUpdateFavouriteList(UpdateFavouriteListEvent event) {
        System.out.println("onUpdateFavouriteList " + mWhichNeighbours);
        if (mWhichNeighbours == FAVOURITE_NEIGHBOURS) {
            initList();
            System.out.println("favourite list has been updated");
            EventBus.getDefault().removeStickyEvent(UpdateFavouriteListEvent.class);
        }
    }
}
