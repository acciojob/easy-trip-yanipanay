package com.driver.Services;

import com.driver.Repository.AirportRepository;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class AirportService {
    AirportRepository airportRepository = new AirportRepository();

    public String addAirport(Airport airport){
        airportRepository.addAirport(airport);

        return "SUCCESS";
    }

    public String addFlight(Flight flight){
        airportRepository.addFlight(flight);
        return "SUCCESS";
    }

    public String addPassenger(Passenger passenger){
        airportRepository.addPassenger(passenger);
        return "SUCCESS";
    }

    public String addTicket(int FlightID,int PassengerId){
        Flight flight = airportRepository.getFlight(FlightID);
        if(flight.getMaxCapacity()<=airportRepository.PassengersInFlight(FlightID)) return "FAILURE";
        if(airportRepository.TicketBooked(FlightID,PassengerId)) return "FAILURE";

        airportRepository.BookTicket(FlightID,PassengerId);

        return "SUCCESS";
    }

    public String cancelTicket(int FlightID, int PassenegerID){
        if(!airportRepository.TicketBooked(FlightID,PassenegerID)) return "FAILURE";

        airportRepository.cancelTicket(FlightID,PassenegerID);
        return "SUCCESS";
    }

    public String getLargestAirport(){
        List<Airport> airports = airportRepository.getAllAirports();

        Airport largest = null;
        int terminals =0;
        for(Airport airport : airports){
            int n =airport.getNoOfTerminals();
            if(n>terminals)
            {
                largest =airport;
                terminals = n;
            }else if(n==terminals){
                if(airport.getAirportName().compareTo(largest.getAirportName())<0){
                    largest = airport;
                }
            }
        }

        return largest.getAirportName();
    }

    public int getTicketPrice(int FlightId){
        int num = airportRepository.PassengersInFlight(FlightId);

        int fare = 3000 + (num*50);

        return fare;
    }

    public double distanceBetweenCities(City src, City dst){
        double MinDuration = Double.MAX_VALUE;
        List<Flight> FList = airportRepository.getAllFlights();
        for(Flight flight : FList){
            if(flight.getFromCity().equals(src) && flight.getToCity().equals(dst)){
                if(Double.compare(flight.getDuration(),MinDuration) < 0){
                    MinDuration = flight.getDuration();
                }
            }
        }

        return MinDuration == Double.MAX_VALUE ? -1:MinDuration;
    }

    public int passengersInDay(Date date,String airportName){
        List<Airport> airports = airportRepository.getAllAirports();
        City city = null;
        for(Airport airport : airports){
            if (airport.getAirportName().equals(airportName)) city = airport.getCity();
        }

        List<Flight> flights = airportRepository.getAllFlights();
        int count=0;
        for(Flight flight : flights){
            if(flight.getFlightDate().equals(date) && (flight.getFromCity().equals(city) || flight.getToCity().equals(city))){
                count+=airportRepository.PassengersInFlight(flight.getFlightId());
            }
        }
        return count;
    }

    public int BookingByPassenger(int passengerID){
        return airportRepository.BookingByPassenger(passengerID);
    }

    public String getSrcAirport(int FLightId){
        Flight flight = airportRepository.getFlight(FLightId);
            if(flight==null) return null;
        City SrcCity = flight.getFromCity();

        List<Airport> airports = airportRepository.getAllAirports();

        for(Airport airport : airports) {
            if(airport.getCity().equals(SrcCity)) return airport.getAirportName();
        }
        return null;
    }

    public int TotalRevenue(int FlightID){
        int num = airportRepository.PassengersInFlight(FlightID);
        /* AP -
        *   d = 50;
        *   a = 3000;
        *   n = num
        * */
        int revenue = (3000*num)+(((num*(num-1))/2)*50);

        return  revenue;
    }
}
