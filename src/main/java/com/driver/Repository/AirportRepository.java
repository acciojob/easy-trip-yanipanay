package com.driver.Repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AirportRepository {

    HashMap<Integer, Flight> FlightDB;

    HashMap<Integer, Passenger> PassengerDB;

    HashMap<String, Airport> AirportDB;

    HashMap<Integer, List<Integer>> TripDB;

    HashMap<Integer,Integer> TicketNo;




    public AirportRepository() {
        FlightDB = new HashMap<>();
        PassengerDB = new HashMap<>();
        AirportDB = new HashMap<>();
        TripDB = new HashMap<>();
        TicketNo = new HashMap<>();
    }


    //add airport, flight, passenger, ticket
    public void addFlight(Flight flight){
        int Id = flight.getFlightId();
        FlightDB.put(Id,flight);
    }
    public void addPassenger(Passenger passenger){
        int id = passenger.getPassengerId();
        PassengerDB.put(id,passenger);
    }
    public void addAirport(Airport airport){
        String name = airport.getAirportName();
        AirportDB.put(name,airport);
    }

    public void BookTicket(Integer FlightID,Integer PassengerID){
        TicketNo.put(PassengerID,TicketNo.getOrDefault(PassengerID,0)+1);

        List<Integer> Plist = new ArrayList<>();
        if(TripDB.containsKey(FlightID)) Plist = TripDB.get(FlightID);
        Plist.add(PassengerID);

        TripDB.put(FlightID,Plist);
    }

    public void cancelTicket(Integer FlightID,Integer PassengerID){
        TicketNo.put(PassengerID,TicketNo.get(PassengerID)-1);

        List<Integer> Plist = TripDB.get(FlightID);

        Plist.remove(PassengerID);

        TripDB.put(FlightID,Plist);
    }

    public int PassengersInFlight(int FlightId){

        int size = (TripDB.containsKey(FlightId) ? TripDB.get(FlightId).size():0);
        return size;
    }

    public boolean TicketBooked(int Flightid, int PassengerID){

        if(!FlightDB.containsKey(Flightid)) return false;
        List<Integer> PassengerList = TripDB.get(Flightid);

        for(int ID:PassengerList){
            if (ID==PassengerID) return true;
        }
        return false;
    }

    public Flight getFlight(int Flightid){
        return FlightDB.get(Flightid);
    }

    public List<Airport> getAllAirports(){
        List<Airport> airports = new ArrayList<>();
        for(Airport ap : AirportDB.values()){
            airports.add(ap);
        }
        return airports;
    }

    public List<Flight> getAllFlights(){
        return FlightDB.values().stream().collect(Collectors.toList());
    }

    public int BookingByPassenger(int PassengerId){
        return TicketNo.containsKey(PassengerId) ? TicketNo.get(PassengerId):0 ;
    }
}

