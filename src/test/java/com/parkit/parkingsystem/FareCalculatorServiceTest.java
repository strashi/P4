package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    public static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    public void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
    	
    	//ARRANGE
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }
    
    
    @Test
    public void calculateFareBike(){
    	
    	//ARRANGE
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);

        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){ 
    	
    	//ARRANGE
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);
        
        //ACT + //ASSERT
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
     	
    	//ARRANGE
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);
        
        //ACT + //ASSERT
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
     	
    	//ARRANGE
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals( (0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        
     	//ARRANGE
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
     	
    	//ARRANGE
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    // Tests for free 30 minutes parking time
    
    @Test
    public void calculateFareBikeWith30MinutesOrLessParkingTime(){
     	
    	//ARRANGE
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );//30 minutes parking time should give free parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals(0, ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWith30MinutesOrLessParkingTime(){

    	//ARRANGE
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );//30 minutes parking time should give free parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals( 0 , ticket.getPrice());
    }
    
    // Tests for recurring users 
    
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTimeForRecurringUser(){
     	
    	//ARRANGE
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);
       
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals( (0.75 * Fare.BIKE_RATE_PER_HOUR* 0.95), ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTimeForRecurringUser(){
     	
    	//ARRANGE
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);
       
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR* 0.95), ticket.getPrice());
    }
       
    @Test
    public void calculateFareCarWithMoreThanADayParkingTimeForRecurringUsers(){
        
     	//ARRANGE
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR * 0.95) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithTwoHoursParkingTimeForRecurringUsers(){
     	
    	//ARRANGE
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  2 * 60 * 60 * 1000) );//2 hours parking time should give 2 * parking fare per hour * 0.95
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals((2 * Fare.BIKE_RATE_PER_HOUR * 0.95), ticket.getPrice() );
       
       
    }

    @Test
    public void calculateFareCarWithTwoHoursParkingTimeForRecurringUsers(){
     	
    	//ARRANGE
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 2 * 60 * 60 * 1000) );//2 hours parking time should 2 * parking fare per hour * 0.95
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);
        
        //ACT
        fareCalculatorService.calculateFare(ticket);
        
        //ASSERT
        assertEquals( (2 * Fare.CAR_RATE_PER_HOUR * 0.95) , ticket.getPrice());
        
       

    }
    
}
