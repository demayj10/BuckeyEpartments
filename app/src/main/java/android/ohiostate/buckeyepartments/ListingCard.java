package android.ohiostate.buckeyepartments;

public class ListingCard {
    private String previewImageUrl, streetAddress, city, zip, bed, bath, rent, key;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public ListingCard() {
    }

    public ListingCard(String previewImageUrl, String streetAddress, String city, String zip,
                       String bed, String bath, String rent, String key)
    {
        this.previewImageUrl = previewImageUrl;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zip = zip;
        this.bed = bed;
        this.bath = bath;
        this.rent = rent;
        this.key = key;
    }

    // getters and setters

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getBath() {
        return bath;
    }

    public void setBath(String bath) {
        this.bath = bath;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }
}
