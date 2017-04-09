package com.itservz.bookex.android.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.itservz.bookex.android.BR;
import com.itservz.bookex.android.backend.FirebaseCategoryService;
import com.itservz.bookex.android.util.DownloadImageTask;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Raju on 12/7/2016.
 */

public class Book extends BaseObservable implements Serializable{
    private static final String TAG = "Book";
    private String uuid = null;
    private String ISBN = null;
    private String title = null;
    private String author = null;
    private String edition = null;
    private String condition = null;
    private boolean missingPage = false;
    private int yourPrice = 0;
    private int msp = 0;
    private int mrp = 0;
    private String description;
    private byte[] image;
    private String imageUrl;
    private Bitmap imageFromUrl;
    private List<BookCategory> categories ;//new FirebaseCategoryService().getCategories();
    private Location location = new Location();
    private long uploadTime = 0;
    private long soldTime = 0;

    //for person
    private String phoneNumber;

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyChange();
    }

    @Bindable
    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
        notifyPropertyChanged(BR.book);
    }

    @Bindable
    public long getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(long soldTime) {
        this.soldTime = soldTime;
        notifyPropertyChanged(BR.book);
    }

    @Bindable
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
        notifyPropertyChanged(BR.book);
    }

    @Bindable
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
        notifyPropertyChanged(BR.book);
    }

    @Bindable
    public String getTitle() {
        Log.d(TAG, "getTitle: " + title);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        Log.d(TAG, "setTitle: " + this.title);
        notifyChange();
    }

    @Bindable
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        notifyChange();
    }

    @Bindable
    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
        notifyChange();
    }

    @Bindable
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        notifyChange();
    }

    @Bindable
    public boolean isMissingPage() {
        return missingPage;
    }

    public void setMissingPage(boolean missingPage) {
        this.missingPage = missingPage;
        notifyChange();
    }

    @Bindable
    public int getYourPrice() {
        return yourPrice;
    }

    public void setYourPrice(int yourPrice) {
        this.yourPrice = yourPrice;
        notifyChange();
    }

    @Bindable
    public int getMsp() {
        return msp;
    }

    public void setMsp(int msp) {
        this.msp = msp;
        notifyChange();
    }

    @Bindable
    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
        notifyChange();
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyChange();
    }

    @Bindable
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
        notifyChange();
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageFromUrl(Bitmap imageFromUrl) {
        this.imageFromUrl = imageFromUrl;
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            new DownloadImageTask(view).execute(imageUri);
        }
    }

    /*@BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }*/

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyChange();
    }

    @Bindable
    public List<BookCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<BookCategory> categories) {
        this.categories = categories;
        notifyChange();
    }

    @Bindable
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        notifyChange();
    }

    public Book() {
    }

    public Book(String ISBN, String title) {
        this.ISBN = ISBN;
        this.title = title;
    }

    public String getCategoriesAsString() {
        StringBuilder sb = new StringBuilder();
        for (BookCategory category : categories) {
            sb.append(category.longText);
            sb.append(" / ");
        }
        sb.setLength(sb.length() - 3);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Book{" +
                "uuid='" + uuid + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", edition='" + edition + '\'' +
                ", condition='" + condition + '\'' +
                ", missingPage=" + missingPage +
                ", yourPrice=" + yourPrice +
                ", msp=" + msp +
                ", mrp=" + mrp +
                ", description='" + description + '\'' +
                ", image=" + (image != null) +
                ", imageUrl=" + imageUrl +
                ", categories=" + categories +
                ", location=" + location +
                ", uploadTime=" + uploadTime +
                ", soldTime=" + soldTime +
                '}';
    }
}
