package com.chickenger.islam.tracker.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chickenger.islam.tracker.R;

public class Place implements Parcelable {

    public static final Place UNSET = new Place(0, "unset", R.string.place_unset, 0);
    public static final Place OTHER = new Place(1, "other", R.string.place_other, 0);
    public static final Place MOSQUE = new Place(2, "mosque", R.string.place_mosque, R.drawable.ic_place_mosque_black_18dp);
    public static final Place HOME = new Place(3, "home", R.string.place_home, R.drawable.ic_place_home_black_18dp);
    public static final Place OFFICE = new Place(4, "office", R.string.place_office, R.drawable.ic_place_office_black_18dp);
    public static final Place DEFAULT = UNSET;

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    private int id;
    private String key;
    private int labelResId;
    private int iconResId;

    private Place(int id, String key, int labelResId, int iconResId) {
        this.id = id;
        this.key = key;
        this.labelResId = labelResId;
        this.iconResId = iconResId;
    }

    protected Place(Parcel in) {
        this.id = in.readInt();
        this.key = in.readString();
        this.labelResId = in.readInt();
        this.iconResId = in.readInt();
    }

    public static Place ofId(int id) {
        switch (id) {
            case 0:
                return UNSET;
            case 1:
                return OTHER;
            case 2:
                return MOSQUE;
            case 3:
                return HOME;
            case 4:
                return OFFICE;
            default:
                throw new IllegalArgumentException("There is no Place with id " + id);
        }
    }

    public static Place ofKey(String key) {
        switch (key) {
            case "unset":
                return UNSET;
            case "other":
                return OTHER;
            case "mosque":
                return MOSQUE;
            case "home":
                return HOME;
            case "office":
                return OFFICE;
            default:
                throw new IllegalArgumentException("There is no Place with key " + key);
        }
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public int getLabelResId() {
        return labelResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (id != place.id) return false;
        if (labelResId != place.labelResId) return false;
        if (iconResId != place.iconResId) return false;
        return key.equals(place.key);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + key.hashCode();
        result = 31 * result + labelResId;
        result = 31 * result + iconResId;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.key);
        dest.writeInt(this.labelResId);
        dest.writeInt(this.iconResId);
    }
}
