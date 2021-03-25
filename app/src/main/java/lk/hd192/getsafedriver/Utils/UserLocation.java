package lk.hd192.getsafedriver.Utils;

public class UserLocation implements Comparable{

    String passengerName,id;
    Double pickUpLatitude,pickUpLongitude,dropLatitude,dropLongitude,distance;

    String status;

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

    public UserLocation(String id,String passengerName, Double pickUpLatitude, Double pickUpLongitude, Double dropLatitude, Double dropLongitude, Double distance,String status) {
        this.id = id;
        this.passengerName = passengerName;
        this.pickUpLatitude = pickUpLatitude;
        this.pickUpLongitude = pickUpLongitude;
        this.dropLatitude = dropLatitude;
        this.dropLongitude = dropLongitude;
        this.distance = distance;
        this.status = status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


    @Override
    public int compareTo(Object o) {

        Double compare=((UserLocation)o).getDistance();
        /* For Ascending order*/
        return (int) (this.distance-compare);

    }
}
