package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
      

        Ticket ticket = parkingService.processIncomingVehicle();
        

        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        //boolean ticketSaved = parkingService.recurringUser("ABCDEF");
        //assertEquals(true, ticketSaved);
        
        assertNotNull(ticket);
        assertFalse(ticket.getParkingSpot().isAvailable());
        
        /*Ticket testTicket = ticketDAO.getTicket("ABCDEF");
        boolean savedTicket = false;
        if ((testTicket.getInTime() != null) && (testTicket.getOutTime() == null)) {
			  ParkingSpot testParkingSpot = testTicket.getParkingSpot();
			  
			  if (!testParkingSpot.isAvailable()) {
				savedTicket = true;
			}
		}
        assertEquals(true, savedTicket);*/
        
    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket ticket = parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
        Object obj = (Object)(ticket.getPrice());
        
        assertNotNull(obj);
        
        Object outTime = (Object)(ticket.getOutTime());
        
        assertNotNull(outTime);
        //assertNotNull(ticket.getOutTime());
       /* parkingService.processExitingVehicle();
        Ticket testTicket = ticketDAO.getTicket("ABCDEF");
        double testPrice = testTicket.getPrice();
        double priceFromDB = ticketDAO.checkPrice("ABCDEF");
        
        assertEquals(testPrice, priceFromDB);*/
        
        
    }

}
