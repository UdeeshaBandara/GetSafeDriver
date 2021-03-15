package lk.hd192.getsafedriver.Utils;

public class UserLocation implements Comparable{

    String passengerName,id;
    Double pickUpLatitude,pickUpLongitude,dropLatitude,dropLongitude,distance;
    Boolean isPicked,isAbsent,isDropped;
    String tempString;

    UserLocation userLocation;

    public UserLocation() {

    }
    public UserLocation (String userLocTemp) {
//        userLocation = (UserLocation) userLocTemp;
    }
    public String getPassengerName() {
        return passengerName;
    }

    public Double getPickUpLatitude() {
        return pickUpLatitude;
    }

    public UserLocation(String id,String passengerName, Double pickUpLatitude, Double pickUpLongitude, Double dropLatitude, Double dropLongitude, Double distance, Boolean isPicked, Boolean isAbsent, Boolean isDropped) {
        this.id = id;
        this.passengerName = passengerName;
        this.pickUpLatitude = pickUpLatitude;
        this.pickUpLongitude = pickUpLongitude;
        this.dropLatitude = dropLatitude;
        this.dropLongitude = dropLongitude;
        this.distance = distance;
        this.isPicked = isPicked;
        this.isAbsent = isAbsent;
        this.isDropped = isDropped;
    }

    public Double getPickUpLongitude() {
        return pickUpLongitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getDropLatitude() {
        return dropLatitude;
    }

    public Double getDropLongitude() {
        return dropLongitude;
    }

    public Double getDistance() {
        return distance;
    }

    public Boolean getPicked() {
        return isPicked;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setPicked(Boolean picked) {
        isPicked = picked;
    }

    public Boolean getAbsent() {
        return isAbsent;
    }

    public void setAbsent(Boolean absent) {
        isAbsent = absent;
    }

    public Boolean getDropped() {
        return isDropped;
    }

    public void setDropped(Boolean dropped) {
        isDropped = dropped;
    }

    @Override
    public int compareTo(Object o) {

        Double compare=((UserLocation)o).getDistance();
        /* For Ascending order*/
        return (int) (this.distance-compare);

    }
}
