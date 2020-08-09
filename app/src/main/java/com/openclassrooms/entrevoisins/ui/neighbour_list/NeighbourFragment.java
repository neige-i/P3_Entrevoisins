package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setTag(mWhichNeighbours);
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
    public void onResume() {
        super.onResume();
        initList();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        // Lifecycle of a fragment inside a ViewPager can be tricky as it is not necessarily
//        // destroyed if it is not visible anymore. Here, this fragment is used in both pages of the
//        // ViewPager's adapter. Thus, if the fragments (un)register an EventBus in onStart() and
//        // onStop(), when the user clicks on a Neighbour (which fires an event), then the
//        // @Subscribe method is called twice as both fragments are aware of the event. This is why
//        // the EventBus is (un)registered when the fragment becomes (in)visible to the user.
//        super.setUserVisibleHint(isVisibleToUser);
//        System.out.println("setUserVisibleHint");
//        if (isVisibleToUser) {
//            EventBus.getDefault().register(this);
//        } else {
//            EventBus.getDefault().unregister(this);
//        }
//    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        System.out.println("onDeleteNeighbour " + mWhichNeighbours);
        if (event.whichNeighbour == mWhichNeighbours) {
            switch (event.whichNeighbour) {
                case ALL_NEIGHBOURS:
                    mApiService.deleteNeighbour(event.neighbour);
                    EventBus.getDefault().post(new UpdateFavouriteListEvent());
                    break;
                case FAVOURITE_NEIGHBOURS:
                    mApiService.toggleFavourite(event.neighbour);
                    break;
            }
            initList();
        }
    }

    /**
     * Fired if the user clicks on a RecyclerView item
     * @param event
     */
    @Subscribe
    public void onShowNeighbourDetail(ShowNeighbourDetailEvent event) {
        if (event.whichNeighbour == mWhichNeighbours)
            DetailActivity.navigate(getActivity(), event.neighbour);
    }

    @Subscribe()
    public void onUpdateFavouriteList(UpdateFavouriteListEvent event) {
        System.out.println("onUpdateFavouriteList " + mWhichNeighbours);
        if (mWhichNeighbours == FAVOURITE_NEIGHBOURS) {
            System.out.println("YES");

            initList();
        }
    }
}
