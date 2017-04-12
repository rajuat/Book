package com.itservz.bookex.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itservz.bookex.android.backend.CallService;
import com.itservz.bookex.android.backend.MessagingService;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.Location;
import com.itservz.bookex.android.util.AddressHelper;
import com.itservz.bookex.android.util.DistanceCalculator;
import com.itservz.bookex.android.util.LetterTileProvider;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Book book;
    private android.location.Location location;
    private BookDetailActivity activity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (BookDetailActivity) getActivity();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            //book = FirebaseDatabaseService.getInstance("").getBooks().get(getArguments().getString(ARG_ITEM_ID));
            book = (Book) getArguments().getSerializable(ARG_ITEM_ID);
            location = (android.location.Location) getArguments().getParcelable(Location.LOCATION_KEY);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(book.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);
        if (book != null) {
            ((TextView) rootView.findViewById(R.id.book_detail)).setText(book.getTitle());
            ((TextView) rootView.findViewById(R.id.booklist_yprice)).setText("₹ " + book.getYourPrice());
            if(book.getMrp() != 0){
                ((TextView) rootView.findViewById(R.id.book_list_mrp)).setText("₹ " + book.getMrp());
            }
            if(book.getLocation()!= null && book.getLocation().latitude != 0
                    && location != null && location.getLatitude() != 0){
                double distanceinKM = DistanceCalculator.distance(book.getLocation().latitude, book.getLocation().longitude, location.getLatitude(), location.getLongitude());
                ((TextView) rootView.findViewById(R.id.book_place_dis)).setText(distanceinKM + "km away");
            }

            ((TextView) rootView.findViewById(R.id.book_place)).setText(AddressHelper.getAddress(getContext(), book.getLocation()));
            if(book.getDescription() != null) {
                ((TextView) rootView.findViewById(R.id.book_description)).append(book.getDescription());
            }
            ((TextView) rootView.findViewById(R.id.book_cat)).append(""+book.getCategoriesAsString());
            ((TextView) rootView.findViewById(R.id.book_condition)).append(""+book.getCondition());

            rootView.findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = CallService.getCallIntent(book.getPhoneNumber());
                    startActivity(intent);
                }
            });
            rootView.findViewById(R.id.sms).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = MessagingService.getSMSIntent(book.getPhoneNumber());
                    startActivity(intent);
                }
            });
        }
        final Resources res = getResources();
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

        final LetterTileProvider tileProvider = new LetterTileProvider(this.getActivity());
        String login = activity.getLoginDisplayName();
        final Bitmap letterTile = tileProvider.getLetterTile(login, login, tileSize, tileSize);

        ImageView profile = (ImageView) rootView.findViewById(R.id.seller_profile);
        profile.setImageDrawable(new BitmapDrawable(getResources(), letterTile));

        ((TextView) rootView.findViewById(R.id.seller_name)).setText(book.seller.name);
        ((TextView) rootView.findViewById(R.id.seller_email)).setText(book.seller.email);
        ((TextView) rootView.findViewById(R.id.seller_phone)).setText(book.seller.phone);

        return rootView;
    }
}
