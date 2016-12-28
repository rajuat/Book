package com.itservz.bookex.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itservz.bookex.android.BookDetailActivity;
import com.itservz.bookex.android.BookDetailFragment;
import com.itservz.bookex.android.BookListActivity;
import com.itservz.bookex.android.R;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.service.GoogleBooksAPIService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Raju on 12/26/2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.BookViewHolder> {
    private SortedList<Book> mBooks;

    public MyAdapter() {
        mBooks = new SortedList<Book>(Book.class, new SortedList.Callback<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.yourPrice - o2.yourPrice;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Book oldItem, Book newItem) {
                // return whether the items' visual representations are the same or not.
                return oldItem.uuid.equals(newItem.uuid);
            }

            @Override
            public boolean areItemsTheSame(Book item1, Book item2) {
                return item1.uuid == item2.uuid;
            }
        });

    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_content, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookViewHolder holder, int position) {
        final Book book = mBooks.get(position);
        holder.mItem = book;
        byte[] img = book.image;
        if (img != null)
            holder.mImageView.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
        holder.mTitleView.setText(book.title);
        holder.mCategories.setText("physics / class 11 / science");
        //holder.mCategories.setText(book.getCategoriesAsString());
        holder.mYourPriceView.setText("₹ " + book.yourPrice);
        holder.mMRPView.setText("₹ " + book.mrp);
        /*if(book.mrp == 0){
            new GoogleBooksAPIService().getBookByISBN(holder);
        }*/

        holder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Context context = v.getContext();
                                                Intent intent = new Intent(context, BookDetailActivity.class);
                                                intent.putExtra(BookDetailFragment.ARG_ITEM_ID, holder.mItem.uuid);

                                                context.startActivity(intent);
                                            }
                                        }
        );

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    // region BookList Helpers
    public Book get(int position) {
        return mBooks.get(position);
    }

    public int add(Book item) {
        return mBooks.add(item);
    }

    public int indexOf(Book item) {
        return mBooks.indexOf(item);
    }

    public void updateItemAt(int index, Book item) {
        mBooks.updateItemAt(index, item);
    }

    public void addAll(List<Book> items) {
        mBooks.beginBatchedUpdates();
        for (Book item : items) {
            mBooks.add(item);
        }
        mBooks.endBatchedUpdates();
    }

    public void addAll(Book[] items) {
        addAll(Arrays.asList(items));
    }

    public boolean remove(Book item) {
        return mBooks.remove(item);
    }

    public Book removeItemAt(int index) {
        return mBooks.removeItemAt(index);
    }

    public void clear() {
        mBooks.beginBatchedUpdates();
        //remove items at end, to avoid unnecessary array shifting
        while (mBooks.size() > 0) {
            mBooks.removeItemAt(mBooks.size() - 1);
        }
        mBooks.endBatchedUpdates();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitleView;
        public final TextView mCategories;
        public final TextView mYourPriceView;
        public final TextView mMRPView;
        public Book mItem;

        public BookViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.bookImage);
            mTitleView = (TextView) view.findViewById(R.id.book_list_title);
            mCategories = (TextView) view.findViewById(R.id.book_list_cat);
            mYourPriceView = (TextView) view.findViewById(R.id.booklist_yprice);
            mMRPView = (TextView) view.findViewById(R.id.book_list_mrp);
            mMRPView.setPaintFlags(mMRPView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

}

