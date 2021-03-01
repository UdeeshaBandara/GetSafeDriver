package lk.hd192.getsafedriver.Utils;

public class UserLocation implements Comparable{

    String passengerName;
    Double Latitude,Longitude,Distance;

    public UserLocation() {

    }

    public UserLocation(String passengerName, Double latitude, Double longitude, Double distance) {
        this.passengerName = passengerName;
        Latitude = latitude;
        Longitude = longitude;
        Distance = distance;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getDistance() {
        return Distance;
    }

    public void setDistance(Double distance) {
        Distance = distance;
    }

    @Override
    public int compareTo(Object o) {

        Double compare=((UserLocation)o).getDistance();
        /* For Ascending order*/
        return (int) (this.Distance-compare);

    }
}
